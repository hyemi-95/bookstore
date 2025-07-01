package project.bookstore.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.bookstore.cart.dto.CartItemDto;
import project.bookstore.deilvery.entity.DeliveryStatus;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class OrderFormDto {
    // 주문에 담긴 아이템(장바구니에서 넘어온 정보)
    private List<CartItemDto> cartItems;

    // 배송지 입력 정보
    private String address;     // 도로명/지번 주소 등
    private String receiver;    // 받는사람 이름
    private String phone;       // 연락처
    private DeliveryStatus status; // 배송상태

    public OrderFormDto(List<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }
}
