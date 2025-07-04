package project.bookstore.usedbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.member.entity.Member;
import project.bookstore.usedbook.entity.UsedBook;
import project.bookstore.usedbook.entity.UsedStatus;

import java.util.List;

public interface UsedBookRepository extends JpaRepository<UsedBook, Long> ,UsedBookRepositoryCustom{

    List<UsedBook> findBySeller(Member seller);

    List<UsedBook> findByStatus(UsedStatus status);
}
