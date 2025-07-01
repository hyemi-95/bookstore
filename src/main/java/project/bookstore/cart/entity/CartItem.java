package project.bookstore.cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.book.entity.Book;
import project.bookstore.global.config.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;//어떤 장바구니에 담겨있는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;//어떤 책을 담았는지

    private int count;//수량

    public CartItem(Cart cart, Book book, int count) {
        this.cart = cart;
        this.book = book;
        this.count = count;
    }

    //수량 변경 로직
    public void addCount(int add){
        this.count += add;
    }

    public void changeCount(int newCount) {//장바구니에서 수량 직접변경 시
        this.count = newCount;
    }


}
