package project.bookstore.usedbook.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsedBookRequestForm {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "저자는 필수입니다.")
    private String author;
    @NotNull(message = "가격은 필수입니다.")
    @PositiveOrZero(message = "가격은 0 이상이여야 합니다.")
    private Integer price;

    private String isbn;

    private String description;
}
