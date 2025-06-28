package project.bookstore.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.book.Repository.BookRepository;
import project.bookstore.book.entity.Book;
import project.bookstore.member.entity.Member;
import project.bookstore.member.repository.MemberRepository;
import project.bookstore.order.dto.OrderDto;
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
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    //주문 생성
    public Long order(Long memberId, Long bookId, int count) {
        //회원조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        //책 조회
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new IllegalArgumentException("책을 찾을 수 없습니다."));

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(book, book.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, List.of(orderItem));

        //저장
        orderRepository.save(order);

        return order.getId();
    }

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
    public List<OrderDto> findOrdersByMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        List<Order> orders = orderRepository.findByMember(member);

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
}
