package project.bookstore.order.repository;

import project.bookstore.member.entity.Member;
import project.bookstore.order.dto.OrderSearchCondition;
import project.bookstore.order.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findByMemberAndCondition(Member member, OrderSearchCondition condition);
}
