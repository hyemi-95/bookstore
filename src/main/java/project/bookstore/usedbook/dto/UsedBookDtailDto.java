package project.bookstore.usedbook.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.usedbook.entity.UsedBook;

import java.time.LocalDateTime;

@Getter @Setter
public class UsedBookDtailDto {

    private Long id;
      private String title;
      private String author;
      private Integer price;

      private String isbn;

      private String description;
      private Long sellerId;
      private String sellerNickname;
      private String status;

      private LocalDateTime createdDate;


    // 정적
    public static UsedBookDtailDto from(UsedBook usedBook) {
        UsedBookDtailDto dto = new UsedBookDtailDto();
        dto.id = usedBook.getId();
        dto.title = usedBook.getTitle();
        dto.author = usedBook.getAuthor();
        dto.price = usedBook.getPrice();
        if (usedBook.getSeller() != null) {
            dto.sellerId = usedBook.getSeller().getId();
            dto.sellerNickname = usedBook.getSeller().getNickname();
        }
        dto.status = usedBook.getStatus().name(); // enum이면 .name()
        dto.createdDate = usedBook.getCreatedDate();
        dto.isbn = usedBook.getIsbn();
        dto.description =usedBook.getDescription();

        return dto;
    }
}
