package project.bookstore.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> ,BoardRepositoryCustom{
}
