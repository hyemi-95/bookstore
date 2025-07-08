package project.bookstore.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import project.bookstore.book.dto.BookDetailDto;
import project.bookstore.book.dto.BookForm;
import project.bookstore.book.dto.BookListDto;
import project.bookstore.book.dto.BookSearchCondition;
import project.bookstore.book.entity.Book;
import project.bookstore.book.service.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /***
     * 책 등록 폼 화면
     * @param model
     * @return
     */
    @GetMapping("/books/new")
    public String bookForm(Model model) {
        model.addAttribute("bookForm", new BookForm());

        return "book/bookForm";
    }

    /**
     * 책 등록 저장
     *
     * @param form
     * @param result
     * @return
     */
    @PostMapping("/books/new")
    public String register(@Valid BookForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "book/bookForm";
        }

        bookService.register(form);
        return "redirect:/books";
    }


    /***
     * 책 목록
     * @param model
     * @return
     */
    @GetMapping("/books")
    public String bookList(@ModelAttribute("condition") BookSearchCondition condition, @PageableDefault(size = 5) Pageable pageable, Model model) {

        Page<BookListDto> books = bookService.searchBooks(condition, pageable);

        model.addAttribute("books", books.getContent()); //현재 페이지의 책 리스트
        model.addAttribute("page", books); //전체 페이징 정보

        return "book/bookList";
    }

    /**
     * 상세페이지
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/books/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        BookDetailDto bookDetailDto = new BookDetailDto(book);

        model.addAttribute("book",bookDetailDto);
        return "book/bookDetail";
    }

//    /***
//     * 책 목록 수정
//     * @param id
//     * @param model
//     * @return
//     */
//    @GetMapping("/books/edit/{id}")
//    public String editeForm(@PathVariable Long id, Model model) {
//        Book book = bookService.findById(id);
//        BookForm form = new BookForm();
//
//        form.setTitle(book.getTitle());
//        form.setAuthor(book.getAuthor());
//        form.setPrice(book.getPrice());
//        form.setStockQuantity(book.getStockQuantity());
//        form.setIsUsed(book.getIsUsed());
//        form.setIsbn(book.getIsbn());
//        form.setDescription(book.getDescription());
//
//        model.addAttribute("bookForm", form);
//        model.addAttribute("bookId", id);
//
//        return "book/bookEditForm";
//    }
//
//    //수정 - 업데이트
//    @PostMapping("/books/edit/{id}")
//    public String update(@PathVariable Long id, @Valid @ModelAttribute("bookForm") BookForm form, BindingResult result) {
//        if (result.hasErrors()) {
//            return "book/bookEditForm";
//        }
//
//        bookService.upate(id, form);
//        return "redirect:/books";
//    }
//
//    //삭제
//    @PostMapping("/books/delete/{id}")
//    public String delete(@PathVariable Long id){
//        bookService.delete(id);
//        return "redirect:/books";//목록으로 이동
//    }

}
