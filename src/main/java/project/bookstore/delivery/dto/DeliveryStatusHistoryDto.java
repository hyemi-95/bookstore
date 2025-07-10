package project.bookstore.delivery.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.delivery.entity.DeliveryStatusHistory;

import java.time.LocalDateTime;

@Getter @Setter
public class DeliveryStatusHistoryDto {

    private LocalDateTime createdDate;

    private DeliveryStatus beforeStatus; //변경 저

    private DeliveryStatus afterStatus; //변경 후

    private String changer; //변경한 사람 (관리자 or 판매자)

    public DeliveryStatusHistoryDto(DeliveryStatusHistory history) {
        this.createdDate =history.getCreatedDate();
        this.beforeStatus = history.getBeforeStatus();
        this.afterStatus = history.getAfterStatus();
        this.changer = history.getChanger().getNickname();
    }

    public static DeliveryStatusHistoryDto form(DeliveryStatusHistory history) {
        DeliveryStatusHistoryDto dto = new DeliveryStatusHistoryDto(history);
        return dto;
    }
}
