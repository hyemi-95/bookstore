package project.bookstore.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;//제목

    @Column(nullable = false)
    private  String author;//저자

    private Integer price; //가격

    private Integer stockQuantity; //재고

    private String isbn; //국제 표준 도서번호(13자리의 숫자 : 978-89-5090-173-0)

    @Lob
    @Column(nullable = true)
    private String description; // 책 내용 설명

    @Column(nullable = false)
    private Boolean isUsed = false; //기본값은 false

    public Book(String title, String author, int price, int stockQuantity, Boolean isUsed, String isbn, String description) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.isUsed = isUsed;
        this.isbn = isbn;
        this.description =description;
    }

    public void update(String title, String author, int price, int stockQuantity, Boolean isUsed,  String isbn,String description) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.isUsed = isUsed;
        this.isbn = isbn;
        this.description =description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id != null && id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
