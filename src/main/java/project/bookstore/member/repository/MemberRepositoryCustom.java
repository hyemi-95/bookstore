package project.bookstore.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bookstore.member.dto.MemberSearchCondition;
import project.bookstore.member.entity.Member;

public interface MemberRepositoryCustom {
    Page<Member> searchMembers(MemberSearchCondition condition, Pageable pageable);
}
