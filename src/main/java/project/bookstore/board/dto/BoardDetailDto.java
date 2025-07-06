package project.bookstore.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import project.bookstore.board.entity.Board;
import project.bookstore.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardDetailDto {
    private Long id;
    private String title;
    private String content;
    private Long writerId;
    private String writerEmail;
    private String writerNickName; // 필요시
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private List<BoardFileDto> files;
    private List<CommentDto> comments;

    public static BoardDetailDto from(Board board, Member currentUser) {
        BoardDetailDto dto = new BoardDetailDto();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        if (board.getWriter() != null) {
            dto.setWriterId(board.getWriter().getId());
            dto.setWriterEmail(board.getWriter().getEmail());
            dto.setWriterNickName(board.getWriter().getNickname()); // Member에 name 필드 있을 때
        }
        dto.setCreatedDate(board.getCreatedDate());
        dto.setModifiedDate(board.getModifiedDate());

        //파일 리스트 DTO로 변화
        if (board.getFiles() != null) {
            List<BoardFileDto> fileDtos = board.getFiles().stream()
                    .map(file -> BoardFileDto.from(file))
                    .toList();
            dto.setFiles(fileDtos);
        }

        //댓글 리스트 DTO로 변화
        if (board.getComments() != null) {
            List<CommentDto> commentDtos = board.getComments().stream()
                    .map(content -> CommentDto.from(content,currentUser))
                    .toList();
            dto.setComments(commentDtos);
        }
        return dto;
    }

    public BoardUpdateDto toUpdateDto() {
        BoardUpdateDto dto = new BoardUpdateDto();
        dto.setId(this.id);
        dto.setTitle(this.title);
        dto.setContent(this.content);
        dto.setFiles(this.files);
        // newFiles, deleteFileIds는 기본값(null)
        return dto;
    }
}
