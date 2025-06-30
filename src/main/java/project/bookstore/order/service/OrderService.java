package project.bookstore.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.book.Repository.BookRepository;
import project.bookstore.book.entity.Book;
import project.bookstore.cart.entity.CartItem;
import project.bookstore.cart.repository.CartItemRepository;
import project.bookstore.deilvery.entity.Delivery;
import project.bookstore.member.entity.Member;
import project.bookstore.member.repository.MemberRepository;
import project.bookstore.order.dto.OrderDto;
import project.bookstore.order.dto.OrderSearchCondition;
import project.bookstore.order.entity.Order;
import project.bookstore.order.entity.OrderItem;
import project.bookstore.order.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    private  final CartItemRepository cartItemRepository;

    //주문 취소
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        order.cancel();
    }

    //단건 조회
    public Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()->new IllegalArgumentException("주문을 찾을 수 없습니다."));
    }

    //주문목로(DTO)
    @Transactional
    public List<OrderDto> findOrdersByMember(Long memberId, OrderSearchCondition condition){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        List<Order> orders = orderRepository.findByMemberAndCondition(member, condition);

        List<OrderDto> result = new ArrayList<>();

        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                result.add(new OrderDto(
                        order.getId(),
                        item.getBook().getTitle(),
                        item.getOrderPrice(),
                        item.getCount(),
                        order.getStatus(),
                        order.getOrderDate()
                ));
            }
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

            cartItem.changeCount(counts.get(i));//수량 최신화

            OrderItem orderItem = OrderItem.createOrderItem(book, book.getPrice(), cartItem.getCount());
            orderItems.add(orderItem);//여러 주문항목을 하나의 리스트로 모으는 작업 -> 주문서에 담을 상품들을 먼저 리스트로 모아놓음(아직 Order와 노상관)

            cartItemRepository.delete(cartItem);//주문 완료 건은 장바구니에서 삭제
        }

        //배송정보 생성
        Delivery delivery = new Delivery(receiver,address,phone);
        //주문 생성
        Order order = Order.createOrder(member, orderItems);//해당 메서드에서 양방향 메서드를 호출(Order내부에 주문 항목 추가(이떄 Order와 양방향이 연결됨)

        order.addDelivery(delivery);
        orderRepository.save(order);

    }
}
