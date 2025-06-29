package project.bookstore.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.cart.entity.Cart;
import project.bookstore.member.entity.Member;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMember(Member member);// 회원으로 장바구니 조회
}
