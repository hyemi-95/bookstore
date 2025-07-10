package project.bookstore.admin.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.order.entity.Order;

@Getter @Setter
public class AdminDeliveryListDto {
    private Long deliveryId;//배송번호
    private String ordererName; // 주문자(구매자)
    private Long orderId;//주문번호
    private String receiver;//수령자
    private String address;//주소
    private String phone;
    private DeliveryStatus status;

    public static AdminDeliveryListDto from(Delivery d) {
        AdminDeliveryListDto dto = new AdminDeliveryListDto();
        Order order = d.getOrder();
        dto.deliveryId = d.getId();
        dto.ordererName = order != null ? order.getMember().getNickname() : "";
        dto.orderId = order != null ? order.getId() : null;
        dto.receiver = d.getReceiver();
        dto.address = d.getAddress();
        dto.phone = d.getPhone();
        dto.status = d.getStatus();
        return dto;
    }
}
