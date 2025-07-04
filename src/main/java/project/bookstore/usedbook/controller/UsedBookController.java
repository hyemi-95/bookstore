package project.bookstore.usedbook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.bookstore.usedbook.dto.UsedBookSearchCondition;
import project.bookstore.usedbook.entity.UsedBook;
import project.bookstore.usedbook.service.UsedbookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/used")
public class UsedBookController {//구매자 및 전체 사용자
    private final UsedbookService usedbookService;

    /**
     * 중고책 목록 (승인건은 자동으로 중고책 엔티티 저장
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String usedBookList(@ModelAttribute("condition") UsedBookSearchCondition condition, Model model) {
        List<UsedBook> books = usedbookService.searchUsedBook(condition);
        model.addAttribute("usedBooks", books);
        model.addAttribute("condition", condition);
        return "usedbook/publicUsedBookList";
    }

    @GetMapping("/{id}")
    public String usedBookDetail(@PathVariable Long id, Model model) {
        UsedBook book = usedbookService.findById(id);
        model.addAttribute("usedBooks", book);
        return "usedbook/publicUsedBookDetail";
    }
}
