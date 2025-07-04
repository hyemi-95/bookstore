package project.bookstore.usedbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.member.entity.Member;
import project.bookstore.usedbook.entity.UsedBookRequest;

import java.util.List;

public interface UsedBookRequestRepository extends JpaRepository<UsedBookRequest, Long> {

    List<UsedBookRequest> findBySeller(Member seller);
}
