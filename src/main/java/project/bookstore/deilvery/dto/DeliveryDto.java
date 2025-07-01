package project.bookstore.deilvery.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.deilvery.entity.DeliveryStatus;

@Getter @Setter
public class DeliveryDto { //배송정보 입력/조회용
    private String receiver; //수령자
    private String address; //주소
    private String phone; //전화번호
    private DeliveryStatus status;// 배송상태
}
