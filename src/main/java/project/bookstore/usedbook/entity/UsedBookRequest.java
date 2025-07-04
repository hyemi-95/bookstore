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
public class UsedBookRequest extends BaseEntity { //중고책 판매 신청

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //판매자(신청자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

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

    //신청 상태
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    //거절사유 (거절 시 입력)
    private String rejectReason;

    //승인/거절 처리자(관리자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Member admin;

    //생성자
    public UsedBookRequest(Member seller, String title, String author, Integer price, String isbn, String description, RequestStatus status) {
        this.seller = seller;
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
        this.status = status;
    }

    //비즈니스 메서드
    public void approve(Member admin) {
        this.status = RequestStatus.APPROVED;
        this.rejectReason = null;
        this.admin = admin;
    }

    public void reject(String rejectReason, Member admin) {
        this.status = RequestStatus.REJECTED;
        this.rejectReason = rejectReason;
        this.admin = admin;
    }

    //수정 시
    public void updateInfo(String title, String author, Integer price, String isbn, String description) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
    }
}
