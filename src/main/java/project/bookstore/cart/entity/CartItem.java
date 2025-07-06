package project.bookstore.cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.book.entity.Book;
import project.bookstore.global.config.BaseEntity;
import project.bookstore.usedbook.entity.UsedBook;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usedbook_id")
    private UsedBook usedBook;//어떤 책을 담았는지

    private int count;//수량

    //신간
    public CartItem(Cart cart, Book book, int count) {
        this.cart = cart;
        this.book = book;
        this.count = count;
        this.usedBook = null;
    }

    //중고책
    public CartItem(Cart cart, UsedBook usedBook, int count) {
        this.cart = cart;
        this.usedBook = usedBook;
        this.count = count;
        this.book = null;
    }

    //수량 변경 로직
    public void addCount(int add){
        this.count += add;
    }

    public void changeCount(int newCount) {//장바구니에서 수량 직접변경 시
        this.count = newCount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return id != null && id.equals(cartItem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
