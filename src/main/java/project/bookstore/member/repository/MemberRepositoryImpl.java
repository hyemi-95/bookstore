package project.bookstore.member.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.bookstore.member.dto.MemberSearchCondition;
import project.bookstore.member.entity.Member;
import project.bookstore.member.entity.QMember;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Member> searchMembers(MemberSearchCondition condition, Pageable pageable) {
        QMember member = QMember.member;

        List<Member> content = queryFactory
                .selectFrom(member)
                .where(
                        emailContains(condition, member),
                        nicknameContains(condition, member),
                        roleEq(condition, member),
                        statusEq(condition, member),
                        joinDateGoe(condition, member),
                        joinDateLoe(condition, member)
                )
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(member.count())
                .from(member)
                .where(
                        emailContains(condition, member),
                        nicknameContains(condition, member),
                        roleEq(condition, member),
                        statusEq(condition, member),
                        joinDateGoe(condition, member),
                        joinDateLoe(condition, member)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    // --- 조건 메서드들 ---

    private BooleanExpression emailContains(MemberSearchCondition cond, QMember m) {
        return cond.getEmail() != null && !cond.getEmail().isBlank()
                ? m.email.containsIgnoreCase(cond.getEmail())
                : null;
    }

    private BooleanExpression nicknameContains(MemberSearchCondition cond, QMember m) {
        return cond.getNickname() != null && !cond.getNickname().isBlank()
                ? m.nickname.containsIgnoreCase(cond.getNickname())
                : null;
    }

    private BooleanExpression roleEq(MemberSearchCondition cond, QMember m) {
        return cond.getRole() != null ? m.role.eq(cond.getRole()) : null;
    }

    private BooleanExpression statusEq(MemberSearchCondition cond, QMember m) {
        return cond.getStatus() != null ? m.status.eq(cond.getStatus()) : null;
    }

    private BooleanExpression joinDateGoe(MemberSearchCondition cond, QMember m) {
        return cond.getJoinDateFrom() != null ? m.createdDate.goe(cond.getJoinDateFrom().atStartOfDay()) : null;
    }

    private BooleanExpression joinDateLoe(MemberSearchCondition cond, QMember m) {
        return cond.getJoinDateTo() != null ? m.createdDate.loe(cond.getJoinDateTo().atTime(23, 59, 59)) : null;
    }

}
