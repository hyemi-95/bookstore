package project.bookstore.usedbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bookstore.usedbook.dto.UsedBookSearchCondition;
import project.bookstore.usedbook.entity.UsedBook;

import java.util.List;

public interface UsedBookRepositoryCustom {
    Page<UsedBook> searchUsedBook(UsedBookSearchCondition condition, Pageable pageable);
}
