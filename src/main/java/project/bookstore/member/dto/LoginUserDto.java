package project.bookstore.member.dto;

import lombok.Getter;
import project.bookstore.member.entity.Member;

@Getter
public class LoginUserDto {
    private final String email;
    private final String nickname;

    public LoginUserDto(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
