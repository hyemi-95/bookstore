package project.bookstore.book.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;//제목

    @NotBlank(message = "저자는 필수입니다.")
    private  String author;//저자

    @PositiveOrZero(message = "가격은 0 이상이여야 합니다.")
    private int price; //가격

    @PositiveOrZero(message = "재고 수량은 0 이상이여야 합니다.")
    private int stockQuantity; //재고

    private String isbn;

    private String description; // 책 내용

    private Boolean isUsed;//중고 여부

}
