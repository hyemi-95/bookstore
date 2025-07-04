package project.bookstore.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResultDto {//오류메시지 dto방식 (필요한 필드 추가 가능 code 등)
    private boolean success; //성공/실패 여부
    private String message; //결과 메시지

    public ResultDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
