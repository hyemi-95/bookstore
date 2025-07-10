package project.bookstore.admin.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.order.entity.Order;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class AdminDeliveryDetailDto {
    private Long deliveryId;
    private String receiver;
    private String address;
    private String phone;
    private String status;

    // 주문정보
    private Long orderId;
    private String ordererName;
    private String orderDate;

    // 주문상품
    private List<OrderItemForDeliveryDto> orderItems;

    public static AdminDeliveryDetailDto from(Delivery delivery) {
        AdminDeliveryDetailDto dto = new AdminDeliveryDetailDto();
        Order order = delivery.getOrder();

        dto.deliveryId = delivery.getId();
        dto.receiver = delivery.getReceiver();
        dto.address = delivery.getAddress();
        dto.phone = delivery.getPhone();
        dto.status = delivery.getStatus().name();
        if (order != null) {
            dto.orderId = order.getId();
            dto.ordererName = order.getMember().getNickname();
            dto.orderDate = order.getOrderDate() != null ?
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "";
            dto.orderItems = order.getOrderItems().stream()
                    .filter(item -> item.getDelivery().getId().equals(delivery.getId()))
                    .map(OrderItemForDeliveryDto::from)
                    .collect(Collectors.toList());
        }
        return dto;
    }
}
