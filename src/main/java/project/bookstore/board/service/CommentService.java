package project.bookstore.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.board.entity.Board;
import project.bookstore.board.entity.Comment;
import project.bookstore.board.repository.BoardRepository;
import project.bookstore.board.repository.CommentRepository;
import project.bookstore.member.entity.Member;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 등록
    public Long saveComment(Long boardId, Member writer, String content) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Comment comment = new Comment(writer, content);
        board.addComment(comment);
        commentRepository.save(comment);

        return comment.getId();
    }

    private Comment getComment(Long commentId) {//공통
        return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
    }

    //댓글 수정
    public void updateComment(Long commentId, String content, Member currentUser) throws AccessDeniedException {
        Comment comment = getComment(commentId);
        if (!comment.getWriter().equals(currentUser)) {
            throw new AccessDeniedException("본인 댓글만 수정할 수 있습니다.");
        }
        comment.updateContent(content);
    }

    //댓글 삭제
    public void deleteComment(Long commentId, Member currentUser) throws AccessDeniedException {
        Comment comment = getComment(commentId);
        if (!comment.getWriter().equals(currentUser) || !currentUser.isAdmin()) {
            throw new AccessDeniedException("본인/관리자만 삭제가 가능합니다.");
        }
        commentRepository.delete(comment);
    }

    //댓글 블라인드 처리
    public void blindComment(Long commentId, Member currentUser) throws AccessDeniedException {
        if (!currentUser.isAdmin()) throw new AccessDeniedException("관리자만 블라인드 가능합니다.");
        Comment comment = getComment(commentId);
        comment.blind();
    }

    //댓글 목록
    @Transactional(readOnly = true)
    public List<Comment> findComments(Long boardId) {
        return commentRepository.findByBoardIdOrderByCreatedDateDesc(boardId);
    }

    public void unblindComment(Long commentId, Member currentUser) throws AccessDeniedException {
        if (!currentUser.isAdmin()) throw new AccessDeniedException("관리자만 복구가 가능합니다.");
        Comment comment = getComment(commentId);
        comment.show();
    }
}
