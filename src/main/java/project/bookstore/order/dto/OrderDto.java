package project.bookstore.order.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.bookstore.deilvery.dto.DeliveryDto;
import project.bookstore.deilvery.entity.DeliveryStatus;
import project.bookstore.member.entity.Member;
import project.bookstore.order.entity.OrderItem;
import project.bookstore.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//주문목록용(DTO)
@Getter @Setter
@NoArgsConstructor
public class OrderDto {
    private Long orderId;               //주무ID
    private OrderStatus status;         //주문 상태
    private LocalDateTime orderDate;    //주문 일시

    //배송정보
    private String receiver;
    private String address;
    private String phone;
    private DeliveryStatus deliveryStatus;

    //주문 상품 리스트
    private List<OrderItemDto>  items;

    private int totalPrice;//전체 합계
}


