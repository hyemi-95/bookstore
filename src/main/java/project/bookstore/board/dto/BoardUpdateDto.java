package project.bookstore.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BoardUpdateDto {
    private Long id;
    private String title;
    private String content;

    private List<MultipartFile> newFiles;//수정시 새로 추가될 파일
    private List<Long> deleteFileIds;//삭제할 첨부파일 id리스트

    private  List<BoardFileDto> files;
}
