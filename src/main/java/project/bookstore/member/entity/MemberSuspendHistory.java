package project.bookstore.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSuspendHistory extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String reason;           // 정지 사유

    @Enumerated(EnumType.STRING)
    private MemberHistoryType historyType;// 이력상태

    public MemberSuspendHistory(Member member, String reason , MemberHistoryType historyType) {
        this.reason = reason;
        this.member = member;
        this.historyType = historyType;
    }
}
