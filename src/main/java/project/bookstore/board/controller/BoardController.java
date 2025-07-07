package project.bookstore.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.bookstore.board.dto.BoardDetailDto;
import project.bookstore.board.dto.BoardSaveDto;
import project.bookstore.board.dto.BoardSearchCondition;
import project.bookstore.board.dto.BoardUpdateDto;
import project.bookstore.board.entity.Board;
import project.bookstore.board.service.BoardService;
import project.bookstore.member.entity.Member;
import project.bookstore.member.security.CustomUserDetails;


@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    //게시글 등록 폼

    @GetMapping("/new")
    public String form(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("boardSaveDto", new BoardSaveDto());
        model.addAttribute("nickname", userDetails.getMember().getNickname());
        return "board/boardForm";
    }

    // 게시글 등록 처리
    @PostMapping("/new")
    public String save(@Valid @ModelAttribute BoardSaveDto dto, BindingResult result,
                       @AuthenticationPrincipal CustomUserDetails userDetails
                        , Model model) {
        if (result.hasErrors()) { //필드검증
            model.addAttribute("nickname", userDetails.getMember().getNickname());
            return "board/boardForm";
        }
        try {
            boardService.createBoard(dto, userDetails.getMember());
        } catch (IllegalArgumentException e) {//비즈니스 예외
            model.addAttribute("fileError",e.getMessage());
            model.addAttribute("nickname", userDetails.getMember().getNickname());
        }
        return "redirect:/board";
    }

    //게시글 목록 + 검색/페이징
    @GetMapping
    public String list(@ModelAttribute BoardSearchCondition condition, Pageable pageable, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member currentUser = (userDetails != null) ? userDetails.getMember() : null;
        Page<Board> boardPage = boardService.searchBoard(condition, pageable);
        Page<BoardDetailDto> dtoPage = boardPage.map(board -> BoardDetailDto.from(board, currentUser));

        model.addAttribute("boards", dtoPage);
        model.addAttribute("condition", condition);

        return "board/boardList";
    }

    // 게시글 상세
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member currentUser = (userDetails != null) ? userDetails.getMember() : null;
        BoardDetailDto dto = boardService.getBoardDetail(id, currentUser);
        model.addAttribute("board", dto);
        return "board/boardDetail";
    }

    // 게시글 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member currentUser = (userDetails != null) ? userDetails.getMember() : null;
        BoardDetailDto dto = boardService.getBoardDetail(id, currentUser);
        model.addAttribute("boardUpdateDto", dto.toUpdateDto()); // 폼용 DTO 생성 메서드 추가(필요한 값만 넘겨주기 위해서
        model.addAttribute("nickname", currentUser.getNickname());
        return "board/boardEditForm";
    }

    // 게시글 수정 처리
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute BoardUpdateDto dto) {
        boardService.updateBoard(id, dto);
        return "redirect:/board/{id}";
    }

    // 게시글 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return "redirect:/board";
    }


}
