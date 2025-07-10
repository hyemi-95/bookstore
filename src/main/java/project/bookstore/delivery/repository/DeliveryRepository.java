package project.bookstore.delivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.member.entity.Member;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> , DeliveryRepositoryCustom{

    Page<Delivery> findBySellerIsNull(Pageable pageable);

}
