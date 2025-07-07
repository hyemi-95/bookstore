package project.bookstore.member.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.member.entity.Member;
import project.bookstore.member.entity.MemberStatus;
import project.bookstore.member.entity.Role;

import java.time.LocalDateTime;

@Getter @Setter
public class MemberListDto {
    private Long id;
    private String email;
    private String nickname;
    private Role role;
    private MemberStatus status;
    private LocalDateTime createdDate;
    private String suspendReason;

    public static MemberListDto from(Member member) {
        MemberListDto dto = new MemberListDto();
        dto.setId(member.getId());
        dto.setEmail(member.getEmail());
        dto.setNickname(member.getNickname());
        dto.setRole(member.getRole());
        dto.setStatus(member.getStatus());
        dto.setCreatedDate(member.getCreatedDate());
        dto.setSuspendReason(member.getSuspendReason()!= null ?member.getSuspendReason() : null );
        return dto;
    }
}
