package project.bookstore.usedbook.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.member.entity.Member;
import project.bookstore.usedbook.entity.RequestStatus;
import project.bookstore.usedbook.entity.UsedBookRequest;

import java.time.LocalDateTime;

@Getter @Setter
public class UsedBookRequestDetailDto {

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

    private String rejectReason;

    private String adminNickname;


    // 정적
    public static UsedBookRequestDetailDto from(UsedBookRequest usedBookRequest) {
        UsedBookRequestDetailDto dto = new UsedBookRequestDetailDto();
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
        dto.adminNickname = usedBookRequest.getAdmin() != null ? usedBookRequest.getAdmin().getNickname() : null;
        dto.rejectReason = usedBookRequest.getRejectReason();

        return dto;
    }

}
