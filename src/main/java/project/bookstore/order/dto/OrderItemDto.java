package project.bookstore.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {
    private String bookTitle;//책 제목
    private int orderPrice;//책 가격
    private int count;//수량

    private int totalPrice; //상품별 합계

    public OrderItemDto(String bookTitle, int orderPrice, int count) {
        this.bookTitle = bookTitle;
        this.orderPrice = orderPrice;
        this.count = count;
        this.totalPrice = orderPrice * count;
    }
}
