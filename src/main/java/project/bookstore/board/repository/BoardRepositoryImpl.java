package project.bookstore.board.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.bookstore.board.dto.BoardSearchCondition;
import project.bookstore.board.entity.Board;
import project.bookstore.board.entity.QBoard;
import project.bookstore.member.entity.QMember;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Board> searchBoards(BoardSearchCondition condition, Pageable pageable) {

        QBoard board = QBoard.board;
        QMember member = QMember.member;

        List<Board> result = queryFactory
                .selectFrom(board)
                .leftJoin(board.writer,member).fetchJoin()
                .where(containsTitleContent(condition, board),containsWriterNickname(condition, member))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(board.count())
                .from(board)
                .leftJoin(board.writer,member)
                .where(containsTitleContent(condition, board),containsWriterNickname(condition, member))
                .fetchOne();

        return new PageImpl<>(result,pageable, total != null ? total : 0L); //null예외방지용
    }

    private BooleanExpression containsWriterNickname(BoardSearchCondition condition, QMember member) {
        return condition.getWriterNickname()!=null && !condition.getWriterNickname().isBlank()? member.nickname.containsIgnoreCase(condition.getWriterNickname()) : null;
    }

    private BooleanExpression containsTitleContent(BoardSearchCondition condition, QBoard board) {
        return condition.getKeyword() != null && !condition.getKeyword().isBlank() ? board.title.containsIgnoreCase(condition.getKeyword()).or(board.content.containsIgnoreCase(condition.getKeyword())) : null;
    }

}
