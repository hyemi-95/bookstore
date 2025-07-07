package project.bookstore.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.board.dto.BoardDetailDto;
import project.bookstore.board.dto.BoardSearchCondition;
import project.bookstore.board.entity.Board;
import project.bookstore.board.service.BoardService;
import project.bookstore.book.dto.BookListDto;
import project.bookstore.book.dto.BookSearchCondition;
import project.bookstore.book.service.BookService;
import project.bookstore.member.dto.MemberListDto;
import project.bookstore.member.dto.MemberSearchCondition;
import project.bookstore.member.entity.Member;
import project.bookstore.member.security.CustomUserDetails;
import project.bookstore.member.service.MemberService;
import project.bookstore.usedbook.dto.UsedBookListDto;
import project.bookstore.usedbook.dto.UsedBookSearchCondition;
import project.bookstore.usedbook.entity.UsedBookRequest;
import project.bookstore.usedbook.service.UsedBookRequestService;
import project.bookstore.usedbook.service.UsedbookService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController { // 관리자 홈, 회원관리, 게시판관리, 신책관리, 중고책 관리 => 중고책 판매 신청은 AdminUsedBookRequestController 로 분리

    private final MemberService memberService;
    private final BoardService boardService;

    private final BookService bookService;
    private final UsedbookService usedbookService;
    private final UsedBookRequestService usedBookRequestService;

    // 관리자 홈(대시보드)
    @GetMapping
    public String adminHome() {
        return "admin/adminHome";
    }

    // 회원관리
    @GetMapping("/members")
    public String memberList(Model model,
                             @ModelAttribute("condition") MemberSearchCondition condition,
                             @PageableDefault(size = 10) Pageable pageable) {
        Page<MemberListDto> page = memberService.searchMembers(condition, pageable);
        model.addAttribute("members", page.getContent());//리스트
        model.addAttribute("page", page);//페이징
        model.addAttribute("condition", condition);//페이징

        return "admin/memberList";
    }

    // 계정정지 (비동기, JSON)
    @PostMapping("members/{id}/suspend")
    @ResponseBody
    public ResponseEntity<?> suspendMember(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String suspendReason = body.get("reason");
        if(suspendReason == null || suspendReason.trim().isEmpty()){
            return ResponseEntity.badRequest().body("정지사유를 입력하세요.");
        }
        try {
            memberService.suspendMember(id, suspendReason);
            // 별도 MemberSuspendHistory 저장하려면 여기서 호출
            // memberSuspendHistoryService.saveHistory(id, reason, ...)
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.status(500).body("에러: " + e.getMessage());
        }
    }

    //게시글관리
    @GetMapping("/boards")
    public String adminBoardList(@ModelAttribute BoardSearchCondition condition,
                                 Pageable pageable,
                                 Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember().isAdmin() ? userDetails.getMember() : null;

        Page<Board> boardPage = boardService.searchBoard(condition, pageable);
        Page<BoardDetailDto> dtoPage = boardPage.map(board -> BoardDetailDto.from(board, member));

        model.addAttribute("boards",dtoPage);
        model.addAttribute("page", dtoPage);
        model.addAttribute("condition", condition);

        return "admin/boardList";
    }

    //게시글 삭제
    @GetMapping("/boards/{id}/delete")
    public String delete(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return "redirect:/admin/boards";
    }

    //신책 관리
    @GetMapping("/books")
    public String bookList(@ModelAttribute("condition") BookSearchCondition condition, @PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<BookListDto> books = bookService.searchBooks(condition, pageable);

        model.addAttribute("books", books.getContent()); //현재 페이지의 책 리스트
        model.addAttribute("page", books); //전체 페이징 정보
        model.addAttribute("condition", condition);

        return "admin/bookList";
    }

    //중고책 관리(승인된 건 리스트)
    @GetMapping("/used-books")
    public String usedBookList(@ModelAttribute("condition") UsedBookSearchCondition condition, Model model, Pageable pageable) {
        Page<UsedBookListDto> books = usedbookService.searchUsedBook(condition, pageable);
        model.addAttribute("usedBooks", books.getContent());
        model.addAttribute("page", books);
        model.addAttribute("condition", condition);
        return "admin/usedBookList";
    }
}
