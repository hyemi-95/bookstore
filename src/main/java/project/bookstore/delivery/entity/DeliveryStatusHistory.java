package project.bookstore.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.BaseEntity;
import project.bookstore.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor
public class DeliveryStatusHistory extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //어떤 배송에 대한 것인지

    @Enumerated(EnumType.STRING)
    private DeliveryStatus beforeStatus; //변경 저

    @Enumerated(EnumType.STRING)
    private DeliveryStatus afterStatus; //변경 후

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changer_id")
    private Member changer; //변경한 사람 (관리자 or 판매자)

    public DeliveryStatusHistory(Delivery delivery, DeliveryStatus beforeStatus, DeliveryStatus afterStatus, Member changer) {
        this.delivery = delivery;
        this.beforeStatus = beforeStatus;
        this.afterStatus = afterStatus;
        this.changer = changer;
    }
}
