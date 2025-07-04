package project.bookstore.usedbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.bookstore.usedbook.dto.UsedBookSearchCondition;
import project.bookstore.usedbook.entity.QUsedBook;
import project.bookstore.usedbook.entity.UsedBook;
import java.util.List;
import static project.bookstore.usedbook.entity.QUsedBook.*;

@RequiredArgsConstructor
public class UsedBookRepositoryImpl implements UsedBookRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UsedBook> searchUsedBook(UsedBookSearchCondition condition) {

        return queryFactory
                .selectFrom(usedBook)
                .where(buildSearchCondition(condition))
                .fetch();

    }

    private Predicate buildSearchCondition(UsedBookSearchCondition condition) {
        QUsedBook book = usedBook;
        BooleanBuilder builder = new BooleanBuilder();

        if (condition.getTitle() != null && !condition.getTitle().isBlank()) {
            builder.and(book.title.containsIgnoreCase(condition.getTitle()));
        }
        if (condition.getStatus() != null) {
            builder.and(book.status.eq(condition.getStatus()));
        }
        return builder;
    }
}
