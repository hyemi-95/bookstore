package project.bookstore.member.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.member.entity.MemberSuspendHistory;

@Getter @Setter
public class MemberSuspendHistoryDto {
    private String createdDate;
    private String historyType;
    private String reason;

    public static MemberSuspendHistoryDto from(MemberSuspendHistory history) {
        return new MemberSuspendHistoryDto(
                history.getCreatedDate().toString().substring(0, 16),
                history.getHistoryType().name(),
                history.getReason()
        );
    }
    public MemberSuspendHistoryDto(String createdDate, String historyType, String reason) {
        this.createdDate = createdDate;
        this.historyType = historyType;
        this.reason = reason;
    }
}
