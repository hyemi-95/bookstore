package project.bookstore.seller.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.delivery.dto.DeliveryDto;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.order.dto.OrderItemDto;
import project.bookstore.order.entity.Order;

import java.util.List;

@Getter @Setter
public class SellerDeliveryDto {
    private Long id;
    private Long orderId;
    private String receiver; //수령자
    private String address; //주소
    private String phone; //전화번호
    private DeliveryStatus status;// 배송상태
    private String sellerNickname; //null -> 본사/관리자 배송
    private List<OrderItemDto> items;//배송별 상품

    public static SellerDeliveryDto from(Delivery delivery) {
        SellerDeliveryDto dto = new SellerDeliveryDto();
        Order order = delivery.getOrder();
        dto.setId(delivery.getId());
        dto.setOrderId(order.getId());
        dto.setReceiver(delivery.getReceiver());
        dto.setAddress(delivery.getAddress());
        dto.setPhone(delivery.getPhone());
        dto.setStatus(delivery.getStatus());
        dto.setSellerNickname(delivery.getSeller() != null ? delivery.getSeller().getNickname() : "본사배송");
        List<OrderItemDto> itemDtos = delivery.getOrder().getOrderItems()
                .stream()
                .filter(item -> item.getDelivery() != null && item.getDelivery().getId().equals(delivery.getId()))
                .map(item -> new OrderItemDto(
                        item.getBook() != null ? item.getBook().getTitle() : item.getUsedBook().getTitle(),
                        item.getBook() != null ? "NEW" : "USED",
                        item.getOrderPrice(),
                        item.getCount()
                )).toList();
        dto.setItems(itemDtos);
        return dto;
    }
}
