package project.bookstore.usedbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bookstore.member.entity.Member;
import project.bookstore.usedbook.dto.UsedBookSearchCondition;
import project.bookstore.usedbook.entity.UsedBook;

public interface UsedBookRepositoryCustom {
    Page<UsedBook> searchUsedBook(UsedBookSearchCondition condition, Pageable pageable);

    Page<UsedBook> searchMyUsedBook(Member seller , UsedBookSearchCondition condition, Pageable pageable);

}
