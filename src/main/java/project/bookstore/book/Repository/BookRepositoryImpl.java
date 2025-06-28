package project.bookstore.book.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.bookstore.book.dto.BookSearchCondition;
import project.bookstore.book.entity.Book;
import project.bookstore.book.entity.QBook;

import java.util.List;

@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoyCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Book> search(BookSearchCondition condition, Pageable pageable) {

        QBook book = QBook.book;

        //동적 조건
        BooleanBuilder builder = new BooleanBuilder();
        if (condition.getKeyword() != null && !condition.getKeyword().isBlank()) {
            builder.and(book.title.containsIgnoreCase(condition.getKeyword()));
        }

        if (condition.getIsUsed() != null) {
            builder.and(book.isUsed.eq(condition.getIsUsed()));
        }

        //조회쿼리
        List<Book> content = queryFactory
                .selectFrom(book)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(book.id.desc())
                .fetch();

        long total = queryFactory.
                select(book.count())
                .from(book)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content,pageable, total);
    }
}
