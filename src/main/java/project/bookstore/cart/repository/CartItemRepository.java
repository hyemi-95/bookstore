package project.bookstore.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.book.entity.Book;
import project.bookstore.cart.entity.Cart;
import project.bookstore.cart.entity.CartItem;
import project.bookstore.usedbook.entity.UsedBook;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {
   //cart 내 모든 item조회
    List<CartItem> findByCart(Cart cart);
    
    //cart + book 으로 이미 담긴 item 찾기
    Optional<CartItem> findByCartAndBook(Cart cart, Book book);

    //중고책
    Optional<CartItem> findByCartAndUsedBook(Cart cart, UsedBook usedBook);

}
