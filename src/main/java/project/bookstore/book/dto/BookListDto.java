package project.bookstore.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import project.bookstore.book.entity.Book;

@Getter @Setter
public class BookListDto { //목록 조회용

    private Long id;
    private String title;//제목
    private  String author;//저자
    private int price; //가격
    private Boolean isUsed;//중고 여부

    private String description; // 책 내용

    public BookListDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.price = book.getPrice();
        this.isUsed = book.getIsUsed();
        this.description = book.getDescription();
    }
}
