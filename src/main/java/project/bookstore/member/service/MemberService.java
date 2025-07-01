package project.bookstore.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.member.dto.SignupForm;
import project.bookstore.member.entity.Member;
import project.bookstore.member.entity.Role;
import project.bookstore.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화용 SecufityConfig에 등록 되어있음

    /***
     * 회원가입 처리 메서드
     * @param form 회원가입 폼에서 넘어온 DTO
     */
    public void signup(SignupForm form) {
        //이메일 중복검사
        if (memberRepository.findByEmail(form.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(form.getPassword());

        //생성자 방식으로 엔티티 생성
        Member member = new Member(
                form.getEmail(),
                encodedPassword,
                form.getNickname(),
                form.getRole()
        );

        //회원 저장
        memberRepository.save(member);
    }


    public Member findByEmail(String name) {
       return memberRepository.findByEmail(name).orElseThrow(()->new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }
}
