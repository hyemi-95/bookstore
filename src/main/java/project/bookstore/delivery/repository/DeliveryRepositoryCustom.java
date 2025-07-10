package project.bookstore.delivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.member.entity.Member;

public interface DeliveryRepositoryCustom {
    Page<Delivery> findAllBySeller(Member seller, Pageable pageable);
}
