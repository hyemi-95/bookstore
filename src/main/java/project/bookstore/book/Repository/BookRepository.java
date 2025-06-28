package project.bookstore.book.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoyCustom {
}
