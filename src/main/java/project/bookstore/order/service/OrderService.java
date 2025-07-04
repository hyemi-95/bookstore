package project.bookstore.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.book.entity.Book;
import project.bookstore.cart.entity.CartItem;
import project.bookstore.cart.repository.CartItemRepository;
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
import project.bookstore.order.repository.OrderRepository;
import project.bookstore.usedbook.entity.UsedBook;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private  final CartItemRepository cartItemRepository;
    private  final DeliveryStatusHistoryRepository historyRepository;

    //주문 취소
    public void cancelOrder(Long orderId, Member currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        //배송상태가 READY가 아닐 때는 취소 불가(배송상태 별 제한)
        if (order.getDelivery().getStatus() != DeliveryStatus.READY) {
            throw new IllegalStateException("배송이 시작된 주문은 취소할 수 없습니다.");
        }
        
        order.cancel();//주문 상태 변경 및 재고복구 등 비즈니스 로직

        Delivery delivery = order.getDelivery();
        DeliveryStatus before = delivery.getStatus();//변경 전 상태


        //취문 취소에 따라 배송상태도  CANCEL
        order.getDelivery().changeStatus(DeliveryStatus.CANCEL); //상태 변경

        //취소에 따른 배송상태 이력 추가
        DeliveryStatusHistory history = new DeliveryStatusHistory(delivery, before, DeliveryStatus.CANCEL, currentUser);
        historyRepository.save(history);
    }

    //주문목로(DTO)
    @Transactional
    public List<OrderDto> findOrdersByMember(Long memberId, OrderSearchCondition condition){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        List<Order> orders = orderRepository.findByMemberAndCondition(member, condition);

        List<OrderDto> result = new ArrayList<>();

        for (Order order : orders) {
            //주문상태 및 배송정보
            OrderDto orderDto = new OrderDto();
            orderDto.setOrderId(order.getId());
            orderDto.setStatus(order.getStatus());
            orderDto.setOrderDate(order.getOrderDate());
            orderDto.setReceiver(order.getDelivery().getReceiver());
            orderDto.setAddress(order.getDelivery().getAddress());
            orderDto.setPhone(order.getDelivery().getPhone());
            orderDto.setDeliveryStatus(order.getDelivery().getStatus());
            orderDto.setDeliveryId(order.getDelivery().getId());

            //상품리스트 세팅
            List<OrderItemDto> items = new ArrayList<>();
            
            for (OrderItem item : order.getOrderItems()) {
                items.add(new OrderItemDto(
                        item.getBook().getTitle(),
                        item.getOrderPrice(),
                        item.getCount()
                ));
            }

            int orderTotalPrice = items.stream().mapToInt(OrderItemDto::getTotalPrice).sum();//상품별 총액을 추출해서 int로 변환 후 모두더함
            orderDto.setItems(items);
            orderDto.setTotalPrice(orderTotalPrice);//주문별 총액

            result.add(orderDto);

        }
        return result;
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

        //배송정보 생성
        Delivery delivery = new Delivery(receiver,address,phone);
        //주문 생성
        Order order = Order.createOrder(member, orderItems, delivery);//해당 메서드에서 양방향 메서드를 호출(Order내부에 주문 항목 추가(이떄 Order와 양방향이 연결됨)
;
        orderRepository.save(order);

    }
}
