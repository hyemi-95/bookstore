package project.bookstore.usedbook.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.usedbook.entity.UsedStatus;

@Getter
@Setter
public class UsedBookSearchCondition {
    private String title;
    private String author;
    private UsedStatus status;
    private String sellerNickname;
}
