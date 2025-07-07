package project.bookstore.usedbook.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.usedbook.entity.RequestStatus;
import project.bookstore.usedbook.entity.UsedBookRequest;
import project.bookstore.usedbook.entity.UsedStatus;

import java.time.LocalDateTime;

@Getter @Setter
public class UsedBookRequestDto {

    private Long id;
    private String title;
    private String author;
    private Integer price;

    private String isbn;

    private String description;
    private Long sellerId;
    private String sellerNickname;
    private RequestStatus status;

    private LocalDateTime createdDate;


    // 정적
    public static UsedBookRequestDto from(UsedBookRequest usedBookRequest) {
        UsedBookRequestDto dto = new UsedBookRequestDto();
        dto.id = usedBookRequest.getId();
        dto.title = usedBookRequest.getTitle();
        dto.author = usedBookRequest.getAuthor();
        dto.price = usedBookRequest.getPrice();
        dto.isbn = usedBookRequest.getIsbn();
        dto.description = usedBookRequest.getDescription();
        if (usedBookRequest.getSeller() != null) {
            dto.sellerId = usedBookRequest.getSeller().getId();
            dto.sellerNickname = usedBookRequest.getSeller().getNickname();
        }
        dto.status = usedBookRequest.getStatus();
        dto.createdDate = usedBookRequest.getCreatedDate();

        return dto;
    }

}
