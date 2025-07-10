package project.bookstore.delivery.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.member.entity.Member;

import java.util.List;

import static project.bookstore.delivery.entity.QDelivery.*;
import static project.bookstore.order.entity.QOrder.*;
import static project.bookstore.order.entity.QOrderItem.*;
import static project.bookstore.usedbook.entity.QUsedBook.*;

@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Delivery> findAllBySeller(Member seller, Pageable pageable) {

        List<Delivery> content = queryFactory
                .select(delivery).distinct()
                .from(delivery)
                .join(delivery.order, order)
                .join(order.orderItems,  orderItem)
                .join( orderItem.usedBook, usedBook)
                .where(usedBook.seller.eq(seller),orderItem.delivery.eq(delivery))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(delivery.countDistinct())
                .from(delivery)
                .join(delivery.order, order)
                .join(order.orderItems,  orderItem)
                .join( orderItem.usedBook, usedBook)
                .where(usedBook.seller.eq(seller),orderItem.delivery.eq(delivery))
                .fetchOne();


        return new PageImpl<>(content, pageable, total);
    }
}
