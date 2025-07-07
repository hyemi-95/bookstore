package project.bookstore.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import project.bookstore.member.entity.MemberStatus;
import project.bookstore.member.entity.Role;

import java.time.LocalDate;

@Getter @Setter
public class MemberSearchCondition {

    private String email;
    private String nickname;
    private Role role;
    private MemberStatus status;//계정 상태

    // 가입일 검색 (기간)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDateFrom; // 가입 시작일

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDateTo;   // 가입 종료일

}
