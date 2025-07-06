package project.bookstore.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardSearchCondition {

    private  String keyword;
    private  String writerNickname;
}
