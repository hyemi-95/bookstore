package project.bookstore.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.global.config.BaseEntity;
import project.bookstore.member.entity.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;//주문자

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();//주문 상품들

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Delivery> deliveries = new ArrayList<>();

    private LocalDateTime orderDate;//주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;//주문상태

    //양방향 편의메서드
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.addOrder(this);
    }

    private void addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
        delivery.addOrder(this);
    }

    //생성 메서드
    public static Order createOrder(Member member, List<OrderItem> items, List<Delivery> deliveries){
        Order order = new Order();
        order.member = member;

        for (OrderItem item : items) {
            order.addOrderItem(item);
        }
//        order.addDelivery(delivery); //양방향 연결
        for (Delivery delivery : deliveries) {
            order.addDelivery(delivery);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id != null && id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
