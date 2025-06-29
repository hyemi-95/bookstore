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

    private String receiver;
    private String address;
    private String phone;

    private DeliveryStatus status; // READY, SHIPPING, COMPLETE

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    public Delivery(String receiver, String address, String phone) {
        this.receiver = receiver;
        this.address = address;
        this.phone = phone;
    }

    public void addDelivery(Order order) {
        this.order = order;
    }
}
