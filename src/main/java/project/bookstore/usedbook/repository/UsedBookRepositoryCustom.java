package project.bookstore.usedbook.repository;

import project.bookstore.usedbook.dto.UsedBookSearchCondition;
import project.bookstore.usedbook.entity.UsedBook;

import java.util.List;

public interface UsedBookRepositoryCustom {
    List<UsedBook> searchUsedBook(UsedBookSearchCondition condition);
}
