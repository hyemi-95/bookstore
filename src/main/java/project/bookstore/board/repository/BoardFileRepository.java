package project.bookstore.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.board.entity.BoardFile;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}
