package project.bookstore.cart.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import project.bookstore.book.entity.Book;
import project.bookstore.cart.entity.Cart;

@Getter @Setter
public class CartItemDto {
    private Long cartItemId;//장바구니아이템 pk
    private Long bookId;//책 pk

    private String bookType; // NEW or USED
    private String bookTitle; //책 제목
    private int price;//책 가격

    private int count;//수량

    public CartItemDto(Long cartItemId, Long bookId, String bookType, String bookTitle, int price, int count) {
        this.cartItemId = cartItemId;
        this.bookId = bookId;
        this.bookType = bookType;
        this.bookTitle = bookTitle;
        this.price = price;
        this.count = count;
    }
}
