package project.bookstore.board.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.board.entity.Comment;
import project.bookstore.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private Long boardId;
    private Long writerId;
    private String writerEmail;
    private String writerNickName; // 필요시
    private String content;
    private boolean visible;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private boolean editable; //본인/관리자 여부
    private boolean deletable; //본인/관리자 여부
    private boolean blindable; //블라인드 여부

    public static CommentDto from(Comment comment, Member currentUser) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId()); // 댓글 PK
        dto.setBoardId(comment.getBoard() != null ? comment.getBoard().getId() : null); // 게시글 PK
        if (comment.getWriter() != null) {
            dto.setWriterId(comment.getWriter().getId());
            dto.setWriterEmail(comment.getWriter().getEmail());
            dto.setWriterNickName(comment.getWriter().getNickname()); // Member에 name 필드 있을 때
        }
        dto.setContent(comment.getContent());
        dto.setVisible(comment.isVisible());
        dto.setCreatedDate(comment.getCreatedDate());
        dto.setModifiedDate(comment.getModifiedDate());

        //권한정보 추가
        boolean isWriter = currentUser != null && comment.getWriter().equals(currentUser);
        boolean isAdmin = currentUser != null && currentUser.isAdmin();

        dto.setEditable(isWriter || isAdmin);
        dto.setDeletable(isWriter || isAdmin);
        dto.setBlindable(isAdmin);
        return dto;
    }

}
