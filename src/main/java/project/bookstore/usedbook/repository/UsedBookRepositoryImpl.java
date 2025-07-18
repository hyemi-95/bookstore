package project.bookstore.usedbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.bookstore.member.entity.Member;
import project.bookstore.usedbook.dto.UsedBookSearchCondition;
import project.bookstore.usedbook.entity.QUsedBook;
import project.bookstore.usedbook.entity.UsedBook;
import java.util.List;
import static project.bookstore.usedbook.entity.QUsedBook.*;

@RequiredArgsConstructor
public class UsedBookRepositoryImpl implements UsedBookRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UsedBook> searchUsedBook(UsedBookSearchCondition condition, Pageable pageable) {

        List<UsedBook> result = queryFactory
                .selectFrom(usedBook)
                .where(buildSearchCondition(null, condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(usedBook.count())
                .from(usedBook)
                .where(buildSearchCondition(null, condition))
                .fetchOne();

        return new PageImpl<>(result,pageable, total != null ? total : 0L); //null예외방지용
    }

    @Override
    public Page<UsedBook> searchMyUsedBook(Member seller, UsedBookSearchCondition condition, Pageable pageable) {
        List<UsedBook> result = queryFactory
                .selectFrom(usedBook)
                .where(buildSearchCondition(seller, condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(usedBook.count())
                .from(usedBook)
                .where(buildSearchCondition(seller, condition))
                .fetchOne();

        return new PageImpl<>(result,pageable, total != null ? total : 0L); //null예외방지용
    }

    private Predicate buildSearchCondition(Member seller ,UsedBookSearchCondition condition) {
        QUsedBook book = usedBook;
        BooleanBuilder builder = new BooleanBuilder();
        if (seller != null) {
            builder.and(book.seller.eq(seller));
        }

        if (condition.getTitle() != null && !condition.getTitle().isBlank()) {
            builder.and(book.title.containsIgnoreCase(condition.getTitle()));
        }
        if (condition.getAuthor() != null && !condition.getAuthor().isBlank()) {
            builder.and(book.author.containsIgnoreCase(condition.getAuthor()));
        }
        if (condition.getSellerNickname()!= null && !condition.getSellerNickname().isBlank()) {
            builder.and(book.seller.nickname.containsIgnoreCase(condition.getSellerNickname()));
        }
        if (condition.getStatus() != null) {
            builder.and(book.status.eq(condition.getStatus()));
        }
        return builder;
    }
}
