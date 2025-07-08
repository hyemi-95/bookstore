package project.bookstore.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.member.entity.MemberSuspendHistory;

import java.util.List;

public interface MemberSuspendHistoryRepository extends JpaRepository<MemberSuspendHistory, Long> {
    List<MemberSuspendHistory> findByMemberIdOrderByCreatedDateDesc(Long memberId);
}
