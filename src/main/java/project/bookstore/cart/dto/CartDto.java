package project.bookstore.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDto {
    private Long cartId;//장바구니 pk
    private List<CartItemDto> cartItems;//장바구니아이템

    public CartDto(Long cartId, List<CartItemDto> cartItems) {
        this.cartId = cartId;
        this.cartItems = cartItems;
    }
}
