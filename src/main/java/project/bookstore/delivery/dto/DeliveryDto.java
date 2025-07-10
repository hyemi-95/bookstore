package project.bookstore.delivery.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.delivery.entity.DeliveryStatus;

@Getter @Setter
public class DeliveryDto { //배송정보 입력/조회용
    private Long id;
    private String receiver; //수령자
    private String address; //주소
    private String phone; //전화번호
    private DeliveryStatus status;// 배송상태

    public static DeliveryDto from(Delivery delivery) {
        DeliveryDto dto = new DeliveryDto();
        dto.setId(delivery.getId());
        dto.setReceiver(delivery.getReceiver());
        dto.setAddress(delivery.getAddress());
        dto.setPhone(delivery.getPhone());
        dto.setStatus(delivery.getStatus());
        return dto;
    }
}
