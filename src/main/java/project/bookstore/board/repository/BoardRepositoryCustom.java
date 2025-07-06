package project.bookstore.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.bookstore.board.dto.BoardSearchCondition;
import project.bookstore.board.entity.Board;

public interface BoardRepositoryCustom {
    Page<Board> searchBoards(BoardSearchCondition condition, Pageable pageable);
}
