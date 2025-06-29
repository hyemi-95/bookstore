package project.bookstore.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.baseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends baseEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;//제목

    @Column(nullable = false)
    private  String author;//저자

    private Integer price; //가격

    private Integer stockQuantity; //재고

    private String isbn; //국제 표준 도서번호(13자리의 숫자 : 978-89-5090-173-0)

    @Column(nullable = false)
    private Boolean isUsed = false; //기본값은 false

    public Book(String title, String author, int price, int stockQuantity, Boolean isUsed, String isbn) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.isUsed = isUsed;
        this.isbn = isbn;
    }

    public void update(String title, String author, int price, int stockQuantity, Boolean isUsed,  String isbn) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.isUsed = isUsed;
        this.isbn = isbn;
    }

    public void removeStock(int quantity) { //재고 차감
        int restStork = this.stockQuantity - quantity;
        if (restStork < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stockQuantity = restStork;
    }

    public void addStork(int quantity) {// 재고 복구
        this.stockQuantity += quantity;
    }
}
