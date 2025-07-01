package project.bookstore.book.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.book.entity.Book;

@Getter @Setter
public class BookDetailDto { //상세용
    private Long id;
    private String title;//제목
    private  String author;//저자
    private int price; //가격
    private  int stockQuantity;//재고
    private  String isbn;
    private Boolean isUsed;//중고 여부

    private String description; // 책 내용

    public BookDetailDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.price = book.getPrice();
        this.stockQuantity = book.getStockQuantity();
        this.isbn = book.getIsbn();
        this.isUsed = book.getIsUsed();
        this.description = book.getDescription();
    }
}
