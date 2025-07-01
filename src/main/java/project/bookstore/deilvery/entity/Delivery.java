package project.bookstore.deilvery.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.baseEntity;
import project.bookstore.order.entity.Order;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends baseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver; //수령인
    private String address; //배송 주소
    private String phone; //연락처

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // 배송상태 : READY, SHIPPING, COMPLETE

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    public Delivery(String receiver, String address, String phone) {
        this.receiver = receiver;
        this.address = address;
        this.phone = phone;
        this.status = DeliveryStatus.READY; //생성 시 기본 배송상태 지정
    }

    public void addDelivery(Order order) {
        this.order = order;
    }
}
