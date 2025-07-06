package project.bookstore.board.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.board.entity.BoardFile;

@Getter
@Setter
public class BoardFileDto {
    private Long id;
    private String originalFileName;
    private String storedFileName;

    public static BoardFileDto from(BoardFile file) {
        BoardFileDto dto = new BoardFileDto();
        dto.setId(file.getId());
        dto.setOriginalFileName(file.getOriginalFileName());
        dto.setStoredFileName(file.getStoredFileName());
        return dto;
    }
}
