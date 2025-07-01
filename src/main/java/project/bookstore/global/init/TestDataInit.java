package project.bookstore.global.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.book.Repository.BookRepository;
import project.bookstore.book.entity.Book;
import project.bookstore.member.entity.Member;
import project.bookstore.member.entity.Role;
import project.bookstore.member.repository.MemberRepository;

@Component
@Profile("local")//local 프로필에서만 작동하도록
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct//spring 초기화 직후 실행
    @Transactional
    public void init() {
        Member member1 = new Member(
                "test@book.com",
                passwordEncoder.encode("123456"),
                "테스트회원",
                Role.USER
        );
        Member admin = new Member(
                "admin@book.com",
                passwordEncoder.encode("123456"),
                "관리자",
                Role.ADMIN
        );
        Member seller = new Member(
                "seller@book.com",
                passwordEncoder.encode("123456"),
                "테스트판매자",
                Role.SELLER
        );
        memberRepository.save(member1);
        memberRepository.save(admin);
        memberRepository.save(seller);

        Book book1 = new Book(
                "이펙티브 자바",
                "조슈아 블로크",
                38000,
                10,
                false,
                "978-89-7914-874-6",
                "이펙티브 자바에 대한 책 설명"
        );
        Book book2 = new Book(
                "스프링 마스터",
                "이몽룡",
                20000,
                4000,
                false,
                "",
                "스프링 마스터에 대한 책 설명"
        );
        Book book3 = new Book(
                "혼공머신",
                "신데렐라",
                35000,
                600,
                true,
                "",
                "혼공머신에 대한 책 설명"
        );
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }
}
