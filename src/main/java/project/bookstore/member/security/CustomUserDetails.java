package project.bookstore.member.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.bookstore.member.entity.Member;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + member.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return member.getPassword(); // 암호화된 비밀번호
    }

    @Override
    public String getUsername() {
        return member.getEmail(); // 로그인 ID로 사용할 값
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부: true = 사용 가능
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부: true = 사용 가능
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }

    public Member getMember() {//객체 넘기기
        return member;
    }
}
