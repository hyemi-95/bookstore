package project.bookstore.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.book.entity.Book;
import project.bookstore.cart.entity.CartItem;
import project.bookstore.cart.repository.CartItemRepository;
import project.bookstore.delivery.dto.DeliveryDto;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.delivery.entity.DeliveryStatusHistory;
import project.bookstore.delivery.repository.DeliveryStatusHistoryRepository;
import project.bookstore.member.entity.Member;
import project.bookstore.member.repository.MemberRepository;
import project.bookstore.order.dto.OrderDto;
import project.bookstore.order.dto.OrderItemDto;
import project.bookstore.order.dto.OrderSearchCondition;
import project.bookstore.order.entity.Order;
import project.bookstore.order.entity.OrderItem;
import project.bookstore.order.entity.OrderStatus;
import project.bookstore.order.repository.OrderRepository;
import project.bookstore.usedbook.entity.UsedBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private  final CartItemRepository cartItemRepository;
    private  final DeliveryStatusHistoryRepository historyRepository;

    //주문 취소 -> 전체 취소(모든 배송이 READY여야 취소 가능)
    public void cancelOrder(Long orderId, Member currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        //배송상태가 READY가 아닐 때는 취소 불가(배송상태 별 제한)
        boolean canCancel = order.getDeliveries().stream().allMatch(delivery -> delivery.getStatus() == DeliveryStatus.READY);
        if (!canCancel) {
            throw new IllegalStateException("배송이 시작된 주문은 취소할 수 없습니다.");
        }
        
        order.cancel();//주문 상태 변경 및 재고복구 등 비즈니스 로직

        for (Delivery delivery : order.getDeliveries()) {
             DeliveryStatus before = delivery.getStatus();//변경 전 상태
            //취문 취소에 따라 배송상태도  CANCEL
            delivery.changeStatus(DeliveryStatus.CANCEL); //상태 변경
            //취소에 따른 배송상태 이력 추가
            DeliveryStatusHistory history = new DeliveryStatusHistory(delivery, before, DeliveryStatus.CANCEL, currentUser);
            historyRepository.save(history);
        }


    }

    //주문목로(DTO)
    @Transactional
    public List<OrderDto> findOrdersByMember(Long memberId, OrderSearchCondition condition){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        List<Order> orders = orderRepository.findByMemberAndCondition(member, condition);

        List<OrderDto> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(toOrderDto(order));
        }
        return result;
    }

    private OrderDto toOrderDto(Order order) {
        //주문상태 및 배송정보
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setStatus(order.getStatus());
        orderDto.setOrderDate(order.getOrderDate());
        
        List<DeliveryDto> deliveryDtos = new ArrayList<>();
        for (Delivery delivery : order.getDeliveries()) {
            DeliveryDto dDto = new DeliveryDto();
            dDto.setReceiver(delivery.getReceiver());
            dDto.setAddress(delivery.getAddress());
            dDto.setPhone(delivery.getPhone());
            dDto.setStatus(delivery.getStatus());
            dDto.setId(delivery.getId());
            dDto.setSellerNickname(delivery.getSeller() != null? delivery.getSeller().getNickname() : "본사배송");
            //배송별 상품리스트 세팅
            List<OrderItemDto> items = new ArrayList<>();
            for (OrderItem item : delivery.getOrderItems()) {
                String title;
                String bookType;
                if (item.getBook() != null) {
                    title = item.getBook().getTitle();
                    bookType = "NEW";
                } else if (item.getUsedBook() != null) {
                    title = item.getUsedBook().getTitle();
                    bookType = "USED";
                } else {
                    title = "알수업음";
                    bookType = "";
                }
                items.add(new OrderItemDto(
                        title,
                        bookType,
                        item.getOrderPrice(),
                        item.getCount()
                ));
            }
            dDto.setItems(items);
            deliveryDtos.add(dDto);
        }
        orderDto.setDeliveries(deliveryDtos);

        int orderTotalPrice = deliveryDtos.stream().flatMap(d -> d.getItems().stream()).mapToInt(i->i.getOrderPrice() * i.getCount()).sum();
        orderDto.setTotalPrice(orderTotalPrice);//주문별 총액
        boolean canCancel = order.getStatus() == OrderStatus.ORDER && order.getDeliveries().stream().allMatch(delivery -> delivery.getStatus() == DeliveryStatus.READY);
        orderDto.setCancellable(canCancel);
        return orderDto;
    }

    public void orderFromCartWithDeliveryInfo(Long memberId, List<Long> cartItemIds, List<Integer> counts, String receiver, String address, String phone) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalArgumentException("회원을 찾을 수 없습니다."));

        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < cartItemIds.size(); i++) {
            CartItem cartItem = cartItemRepository.findById(cartItemIds.get(i))
                    .orElseThrow(()->new IllegalArgumentException("장바구니에 해당 항목이 존재하지 않습니다."));
            Book book = cartItem.getBook();
            UsedBook usedBook = cartItem.getUsedBook();

            cartItem.changeCount(counts.get(i));//수량 최신화

            OrderItem orderItem;
            if (book != null) {//신간
                orderItem = OrderItem.createOrderItem(book, book.getPrice(), cartItem.getCount());
            } else if (usedBook != null) {//중고
                orderItem = OrderItem.createOrderItem(usedBook, usedBook.getPrice(), cartItem.getCount());
            } else {
                throw new IllegalStateException("책 정보가 없습니다");
            }
            orderItems.add(orderItem);//여러 주문항목을 하나의 리스트로 모으는 작업 -> 주문서에 담을 상품들을 먼저 리스트로 모아놓음(아직 Order와 노상관)

            cartItemRepository.delete(cartItem);//주문 완료 건은 장바구니에서 삭제
        }
        //1.배송 주체별 그룹핑
        Map<Member, List<OrderItem>> deliveryGroups = new HashMap<>();
        for (OrderItem item : orderItems) {
            Member seller;
            if (item.getUsedBook() != null) {
                seller = item.getUsedBook().getSeller();//중고책 판매자
            } else {
                seller = null; //신책(관지라 : 본사 배송)
            }
            //seller(Key)에 이미 리스트가 있으면 꺼내고 없으면 새 ArrayList 만들어서 추가, 그 리스트에 item넣어주기
            deliveryGroups.computeIfAbsent(seller, k -> new ArrayList<>()).add(item);
        }
        
        //2.그룹별로 Delivery 생성
        List<Delivery> deliveries = new ArrayList<>();
        //위에서 만든 Map의 Key-Value(seller와 해당 상품들) 순회해서 담음
        for (Map.Entry<Member, List<OrderItem>> entry : deliveryGroups.entrySet()) {
            //배송정보 생성
            Delivery delivery = new Delivery(receiver,address,phone);
            //판매자 배송이면 seller할당
            if (entry.getKey() != null) {
                delivery.changeSeller(entry.getKey());
            }
            for (OrderItem item : entry.getValue()) {
                delivery.addOrderItem(item);//편의메서드로 양방향 연결
            }
            deliveries.add(delivery);

            //주문에 따른 첫 기본정보 이력 추가
            DeliveryStatusHistory history = new DeliveryStatusHistory(delivery, null, DeliveryStatus.READY, member);
            historyRepository.save(history);
        }

        //주문 생성
        Order order = Order.createOrder(member, orderItems, deliveries);//해당 메서드에서 양방향 메서드를 호출(Order내부에 주문 항목 추가(이떄 Order와 양방향이 연결됨)
        orderRepository.save(order);

    }

    //주문상세
    public OrderDto findOrderDetailById(Long orderId, Member member) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        // 주문 소유자 검증(관리자면 패스, 일반회원은 본인만)
        if (!order.getMember().getId().equals(member.getId()) && !member.isAdmin()) {
            throw new IllegalStateException("본인 주문만 조회할 수 있습니다.");
        }
        return toOrderDto(order); // 기존 메서드 재사용
    }

}
