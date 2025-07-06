package project.bookstore.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.bookstore.board.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardIdOrderByCreatedDateDesc(Long boardId);
}
