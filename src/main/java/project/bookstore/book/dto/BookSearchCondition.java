package project.bookstore.book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookSearchCondition {

    //제목 키워드(부분 일치)
    private String keyword;

    //중고 여부(null이면 전체)
    private Boolean isUsed;

}
