package project.bookstore.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;//주문자

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();//주문 상품들

    private LocalDateTime orderDate;//주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;//주문상태

    //양방향 편의메서드
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.addOrder(this);
    }

    //생성 메서드
    public static Order createOrder(Member member, List<OrderItem> items){
        Order order = new Order();
        order.member = member;

        for (OrderItem item : items) {
            order.addOrderItem(item);
        }
        order.status = OrderStatus.ORDER;
        order.orderDate = LocalDateTime.now();
        return order;
    }

    //주문 취소
    public void cancel(){
        this.status = OrderStatus.CANCEL;

        for (OrderItem item : orderItems) {
            item.cancel();
        }
    }
}
