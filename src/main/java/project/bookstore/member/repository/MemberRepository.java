package project.bookstore.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom {
    Optional<Member> findByEmail(String name);//email
}
