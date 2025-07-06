package project.bookstore.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.bookstore.board.dto.CommentDto;
import project.bookstore.board.entity.Comment;
import project.bookstore.board.service.CommentService;
import project.bookstore.member.entity.Member;
import project.bookstore.member.security.CustomUserDetails;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestController //JS/Ajax가 직접 호출 → 데이터를 받아 동적으로 화면 업데이트-> JSON만 보내줌
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    //댓글 목록
    @GetMapping("/{boardId}")
    public List<CommentDto> getComments(@PathVariable Long boardId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member currentUser = (userDetails!=null)? userDetails.getMember():null;
        List<Comment> comments = commentService.findComments(boardId);
        return comments.stream()
                .map((Comment comment) -> CommentDto.from(comment,currentUser))
                .collect(Collectors.toList());
    }

    //댓글 등록
    @PostMapping
    public CommentDto saveComment(@RequestBody CommentDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member currentUser = userDetails.getMember();
        Long commentId = commentService.saveComment(dto.getBoardId(), currentUser, dto.getContent());
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);
        return commentDto;
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public void updateComment(@PathVariable Long id, @RequestBody CommentDto dto , @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        Member currentUser = userDetails.getMember();
        commentService.updateComment(id, dto.getContent(), currentUser);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        Member currentUser = userDetails.getMember();
        commentService.deleteComment(id, currentUser);
    }

    // 댓글 블라인드
    @PutMapping("/{id}/blind")
    public void blindComment(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        Member currentUser = userDetails.getMember();
        commentService.blindComment(id, currentUser);
    }

    // 댓글 블라인드 복구
    @PutMapping("/{id}/unblind")
    public void unblindComment(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        Member currentUser = userDetails.getMember();
        commentService.unblindComment(id, currentUser);
    }

}
