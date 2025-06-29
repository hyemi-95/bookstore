package project.bookstore.order.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.bookstore.member.entity.Member;
import project.bookstore.order.entity.OrderItem;
import project.bookstore.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderDto {
    private Long orderId;                    //주무ID
    private String bookTitle;           //책 제목
    private int orderPrice;             //주문 가격
    private int count;                  //주문 수량

    private OrderStatus status;         //주문 상태

    private LocalDateTime orderDate;    //주문 일시

    public OrderDto(Long orderId, String bookTitle, int orderPrice, int count, OrderStatus status, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.bookTitle = bookTitle;
        this.orderPrice = orderPrice;
        this.count = count;
        this.status = status;
        this.orderDate = orderDate;
    }
}


