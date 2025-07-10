package project.bookstore.delivery.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.BaseEntity;
import project.bookstore.member.entity.Member;
import project.bookstore.order.entity.Order;
import project.bookstore.order.entity.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver; //수령인
    private String address; //배송 주소
    private String phone; //연락처

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // 배송상태 : READY, SHIPPING, COMPLETE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();//해당 배송에 포함된 주문 상품들

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    public Delivery(String receiver, String address, String phone) {

        this.receiver = receiver;
        this.address = address;
        this.phone = phone;
        this.status = DeliveryStatus.READY; //생성 시 기본 배송상태 지정
    }

    //양방향 편의 메서드
    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        item.addDelivery(this);
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public void changeStatus(DeliveryStatus newStatus) {
        this.status = newStatus;
    }

    public void changeSeller(Member seller) {
        this.seller = seller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return id != null && id.equals(delivery.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
