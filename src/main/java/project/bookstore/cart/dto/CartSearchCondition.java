package project.bookstore.cart.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class CartSearchCondition {//장바구니 조회
    private String title; //책 제목
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate; //시작일
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate; //종료일
}
