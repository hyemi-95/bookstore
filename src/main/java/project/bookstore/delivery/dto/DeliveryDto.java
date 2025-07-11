package project.bookstore.delivery.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.order.dto.OrderItemDto;

import java.util.List;

@Getter @Setter
public class DeliveryDto { //배송정보 입력/조회용
    private Long id;
    private String receiver; //수령자
    private String address; //주소
    private String phone; //전화번호
    private DeliveryStatus status;// 배송상태
    private String sellerNickname; //null -> 본사/관리자 배송
    private List<OrderItemDto> items;//배송별 상품

    public static DeliveryDto from(Delivery delivery) {
        DeliveryDto dto = new DeliveryDto();
        dto.setId(delivery.getId());
        dto.setReceiver(delivery.getReceiver());
        dto.setAddress(delivery.getAddress());
        dto.setPhone(delivery.getPhone());
        dto.setStatus(delivery.getStatus());
        dto.setSellerNickname(delivery.getSeller() != null ? delivery.getSeller().getNickname(): "본사배송");
        List<OrderItemDto> itemDtos = delivery.getOrderItems().stream()
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
