package project.bookstore.order.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.bookstore.book.entity.QBook;
import project.bookstore.member.entity.Member;
import project.bookstore.order.dto.OrderSearchCondition;
import project.bookstore.order.entity.Order;
import project.bookstore.order.entity.OrderStatus;
import project.bookstore.order.entity.QOrder;
import project.bookstore.order.entity.QOrderItem;
import project.bookstore.usedbook.entity.QUsedBook;
import project.bookstore.usedbook.entity.UsedBook;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findByMemberAndCondition(Member member, OrderSearchCondition condition) {
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;
        QBook book = QBook.book;
        QUsedBook usedBook = QUsedBook.usedBook;

        return queryFactory
                .selectFrom(order)
                .distinct()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(orderItem.book, book).fetchJoin()
                .leftJoin(orderItem.usedBook, usedBook).fetchJoin()
                .where(
                    order.member.eq(member),
                    orderDateGoe(order, condition.getStartDate()),
                    orderDateLt(order, condition.getEndDate()),
                    bookTitleCantains(book, usedBook, condition.getTitle()),
                    orderStatusEq(order, condition.getStatus())
                )
                .fetch();
    }

    private BooleanExpression orderDateGoe(QOrder order, LocalDate startDate) {
        return (startDate != null) ? order.createdDate.goe(startDate.atStartOfDay()) : null;
    }

    private BooleanExpression orderDateLt(QOrder order, LocalDate endDate) {
        return (endDate != null) ? order.createdDate.lt(endDate.plusDays(1).atStartOfDay()) : null;
    }

    private BooleanExpression bookTitleCantains(QBook book, QUsedBook usedBook, String title) {
        return (title != null && !title.isBlank())? (book.title.containsIgnoreCase(title).or(usedBook.title.containsIgnoreCase(title))): null;
    }

    private BooleanExpression orderStatusEq(QOrder order, OrderStatus status) {
        return status != null ? order.status.eq(status) : null;
    }
}
