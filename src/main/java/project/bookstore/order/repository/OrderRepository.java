package project.bookstore.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.member.entity.Member;
import project.bookstore.order.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMember(Member member);
}
