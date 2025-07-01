package project.bookstore.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.delivery.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
