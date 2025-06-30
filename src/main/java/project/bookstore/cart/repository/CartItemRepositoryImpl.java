package project.bookstore.cart.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import project.bookstore.book.entity.QBook;
import project.bookstore.cart.dto.CartSearchCondition;
import project.bookstore.cart.entity.Cart;
import project.bookstore.cart.entity.CartItem;
import project.bookstore.cart.entity.QCartItem;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<CartItem> searchByCartAndCondition(Cart cart, CartSearchCondition condition) {
        QCartItem cartItem = QCartItem.cartItem;
        QBook book = QBook.book;

       return queryFactory
                .selectFrom(cartItem)
                .join(cartItem.book, book).fetchJoin()
                .where(
                        carEq(cartItem,cart),
                        titleContains(book, condition.getTitle()),
                        createDateGoe(cartItem, condition.getStartDate()),
                        createDateLoe(cartItem, condition.getEndDate())
                )
                .fetch();

    }


    //조건 메서드

    private BooleanExpression carEq(QCartItem cartItem, Cart cart) {//장바구니 조건
        return cart != null ? cartItem.cart.eq(cart) : null;
    }
    private BooleanExpression titleContains(QBook book, String title) {//책 제목 조건
        return (title != null && !title.isBlank()) ? book.title.contains(title) : null;
    }

    private Predicate createDateGoe(QCartItem cartItem, LocalDate startDate) {//시작일(이상)
        //2025-06-29 -> 2025-06-29 00:00:00 부터 포함
        return (startDate != null) ? cartItem.createdDate.goe(startDate.atStartOfDay()) : null;
    }

    private Predicate createDateLoe(QCartItem cartItem, LocalDate endDate) {//종료일(미만)
        //2025-06-30 -> 2025-07-01 00:00:00 미만 즉, 2025-06-30 23:59:59 까지
        return (endDate != null) ? cartItem.createdDate.lt(endDate.plusDays(1).atStartOfDay()) : null;
    }


    
}
