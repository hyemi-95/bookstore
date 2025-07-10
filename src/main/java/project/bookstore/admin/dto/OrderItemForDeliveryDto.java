package project.bookstore.admin.dto;

import lombok.Getter;
import lombok.Setter;
import project.bookstore.order.entity.OrderItem;
import project.bookstore.usedbook.entity.UsedBook;

@Getter @Setter
public class OrderItemForDeliveryDto {
    private String title;
    private String author;
    private String type; // 신/중고
    private String sellerName;
    private Integer price;
    private Integer quantity;

    public static OrderItemForDeliveryDto from(OrderItem item) {
        OrderItemForDeliveryDto dto = new OrderItemForDeliveryDto();
        if (item.getBook() != null) {
            dto.title = item.getBook().getTitle();
            dto.author = item.getBook().getAuthor();
            dto.type = "신책";
            dto.sellerName = "관리자";
            dto.price = item.getBook().getPrice();
        } else if (item.getUsedBook() != null) {
            UsedBook ub = item.getUsedBook();
            dto.title = ub.getTitle();
            dto.author = ub.getAuthor();
            dto.type = "중고책";
            dto.sellerName = ub.getSeller().getNickname();
            dto.price = ub.getPrice();
        }
        dto.quantity = item.getCount();
        return dto;
    }
}

