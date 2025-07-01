package project.bookstore.delivery.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.delivery.entity.DeliveryStatusHistory;

import java.util.List;

public interface DeliveryStatusHistoryRepository extends JpaRepository<DeliveryStatusHistory, Long> {

    List<DeliveryStatusHistory> findByDeliveryId(Long deliveryId);

}
