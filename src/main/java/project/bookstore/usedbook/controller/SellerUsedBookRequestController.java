package project.bookstore.usedbook.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.bookstore.global.dto.ResultDto;
import project.bookstore.member.entity.Member;
import project.bookstore.member.security.CustomUserDetails;
import project.bookstore.usedbook.dto.*;
import project.bookstore.usedbook.entity.RequestStatus;
import project.bookstore.usedbook.entity.UsedBook;
import project.bookstore.usedbook.entity.UsedBookRequest;
import project.bookstore.usedbook.service.UsedBookRequestService;
import project.bookstore.usedbook.service.UsedbookService;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerUsedBookRequestController {//판매자용
    private final UsedBookRequestService usedBookRequestService;
    private final UsedbookService usedbookService;

    /**
     * 판매자 홈
     * @return
     */
    @GetMapping
    public String sellerHome() {
        return "seller/sellerHome";
    }
    
    /**
     * 판매신청 폼
     *
     * @param model
     * @return
     */
    @GetMapping("/request/new")
    public String requestForm(Model model) {
        model.addAttribute("usedBookRequest", new UsedBookRequestForm());
        return "seller/requestForm";
    }

    /**
     * 판매신청 등록
     *
     * @param form
     * @param userDetails
     * @return
     */
    @PostMapping("/request/new")
    public String submitRequest(@ModelAttribute UsedBookRequestForm form, BindingResult result, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (result.hasErrors()) {
            return "seller/requestForm";
        }
        Member seller = userDetails.getMember();
        usedBookRequestService.requestSeller(
                seller,
                form.getTitle(),
                form.getAuthor(),
                form.getPrice(),
                form.getIsbn(),
                form.getDescription()
        );
        return "redirect:/seller/request/my";
    }

    /**
     * 내 신청 목록
     *
     * @param userDetails
     * @param model
     * @return
     */
    @GetMapping("/request/my")
    public String myRequest(@Valid @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Member seller = userDetails.getMember();
        List<UsedBookRequest> myRequests = usedBookRequestService.findMyRequest(seller);
        model.addAttribute("requests", myRequests);
        return "seller/myRequestList";
    }

    /**
     * 내 신청 상세
     *
     * @param id
     * @param model
     * @param userDetails
     * @return
     */
    @GetMapping("/request/my/{id}")
    public String myRequestDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UsedBookRequestDetailDto request = usedBookRequestService.findById(id);
        //퀀한 체크
        if (!request.getSellerId().equals(userDetails.getMember().getId())) {
            return "error/403";
        }
        model.addAttribute("request", request);
        return "seller/myRequestDetail";
    }

    /**
     * 내 판매신청 수정 폼
     * @param id
     * @param userDetails
     * @param model
     * @return
     */
    @GetMapping("/request/my/{id}/edit")
    public String editRequestForm(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UsedBookRequestDetailDto request = usedBookRequestService.findById(id);
        //본인 상태 확인
        if (!request.getSellerId().equals(userDetails.getMember().getId())) {
            return "error/403";
        }
        if(request.getStatus() != RequestStatus.PENDING){
            ResultDto resultDto = new ResultDto(false, "대기 상태에서만 수정이 가능합니다.");
            model.addAttribute("message", resultDto.getMessage());
            return "error/customError";
        }
        UsedBookRequestForm form = new UsedBookRequestForm();
        form.setTitle(request.getTitle());
        form.setAuthor(request.getAuthor());
        form.setPrice(request.getPrice());
        form.setIsbn(request.getIsbn());
        form.setDescription(request.getDescription());

        model.addAttribute("usedBookRequest", form);
        model.addAttribute("requestId", id);

        return "seller/editRequestForm";
    }

    //수정 저장
    @PostMapping("/request/my/{id}/edit")
    public String updateRequest(@PathVariable Long id, @ModelAttribute UsedBookRequestForm form, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        ResultDto request =  usedBookRequestService.updateRequest(id, userDetails.getMember().getId(), form);
        if (!request.isSuccess()) {
            model.addAttribute("message", request.getMessage());
            return "error/customError";
        }
        return "redirect:/seller/request/my";
    }

    //내 신청내역 삭제
    @GetMapping("/request/my/{id}/delete")
    public String myRequestDelete(@PathVariable Long id){
        usedBookRequestService.delete(id);
        return "redirect:/seller/request/my";//목록으로 이동
    }

    //내 승인 중고책
    @GetMapping("/request/myUsedBook")
    public String myUsedBookList(@ModelAttribute("condition") UsedBookSearchCondition condition,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model, Pageable pageable) {
        Member seller = userDetails.getMember();
        Page<UsedBookListDto> books = usedbookService.searchMyUsedBook(seller, condition, pageable);
        model.addAttribute("usedBooks", books.getContent());
        model.addAttribute("page", books);
        model.addAttribute("condition", condition);
        return "seller/sellerMyUsedBook";
    }


}
