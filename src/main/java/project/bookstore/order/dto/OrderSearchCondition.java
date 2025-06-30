package project.bookstore.order.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.order.entity.OrderStatus;

import java.time.LocalDate;


@Getter @Setter
public class OrderSearchCondition {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private OrderStatus status;
}
