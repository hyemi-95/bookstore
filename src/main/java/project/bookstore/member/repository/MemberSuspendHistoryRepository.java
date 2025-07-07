package project.bookstore.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.member.entity.MemberSuspendHistory;

public interface MemberSuspendHistoryRepository extends JpaRepository<MemberSuspendHistory, Long> {
}
