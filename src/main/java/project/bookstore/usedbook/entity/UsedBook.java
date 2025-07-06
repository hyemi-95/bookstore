package project.bookstore.usedbook.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.BaseEntity;
import project.bookstore.member.entity.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UsedBook extends BaseEntity { //중고책 실물 데이터

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //중고책 정보
    @Column(nullable = false)
    private String title;//제목
    @Column(nullable = false)
    private  String author;//저자
    private Integer price; //가격
    private String isbn; //국제 표준 도서번호(13자리의 숫자 : 978-89-5090-173-0)
    @Lob
    @Column(nullable = true)
    private String description; // 책 내용 설명

    //판매자(신청자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @Enumerated(EnumType.STRING)
    private UsedStatus status = UsedStatus.FOR_SALE;

    //생성자
    public UsedBook(String title, String author, Integer price, String isbn, String description, Member seller, UsedStatus status) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
        this.seller = seller;
        this.status = status;
    }

    //변경 메서드
    public void markSold() {
        this.status = UsedStatus.SOLD;
    }
    public void markForSale() {
        this.status = UsedStatus.FOR_SALE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsedBook usedBook = (UsedBook) o;
        return id != null && id.equals(usedBook.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
