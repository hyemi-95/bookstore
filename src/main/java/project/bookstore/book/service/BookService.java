package project.bookstore.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.book.Repository.BookRepository;
import project.bookstore.book.dto.BookForm;
import project.bookstore.book.dto.BookListDto;
import project.bookstore.book.dto.BookSearchCondition;
import project.bookstore.book.entity.Book;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    /**
     * 책등록
     */
    @Transactional
    public Long register(BookForm form) {
        Book book = new Book(
                form.getTitle(),
                form.getAuthor(),
                form.getPrice(),
                form.getStockQuantity(),
                false,
                form.getIsbn(),
                form.getDescription()
        );

        bookRepository.save(book);
        return book.getId();
    }

    public List<Book> findAll() {
        return bookRepository.findAll(); //전체 리스트 조회
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다."));// Optional을 바로 꺼내서 사용
    }

    @Transactional
    public void upate(Long id, BookForm form) {
        Book book = findById(id);//영속상태

        book.update(
                form.getTitle(),
                form.getAuthor(),
                form.getPrice(),
                form.getStockQuantity(),
                false,
                form.getIsbn(),
                form.getDescription()
        );

    }

    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public Page<BookListDto> searchBooks(BookSearchCondition condition, Pageable pageable) {
        Page<Book> books = bookRepository.search(condition, pageable);
        return books.map(BookListDto::new);
    }
}
