package project.bookstore.book.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bookstore.book.dto.BookSearchCondition;
import project.bookstore.book.entity.Book;

public interface BookRepositoyCustom {

    //조건 + 페이징 결과 반환
    Page<Book> search(BookSearchCondition condition, Pageable pageable);
}
