package project.bookstore.usedbook.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.bookstore.global.dto.ResultDto;
import project.bookstore.member.entity.Member;
import project.bookstore.member.security.CustomUserDetails;
import project.bookstore.usedbook.dto.UsedBookRequestForm;
import project.bookstore.usedbook.entity.RequestStatus;
import project.bookstore.usedbook.entity.UsedBookRequest;
import project.bookstore.usedbook.service.UsedBookRequestService;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/seller/request")
public class SellerUsedBookRequestController {//판매자용
    private final UsedBookRequestService usedBookRequestService;

    /**
     * 판매신청 폼
     *
     * @param model
     * @return
     */
    @GetMapping("/new")
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
    @PostMapping("/new")
    public String submitRequest(@ModelAttribute UsedBookRequestForm form, BindingResult result, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (result.hasErrors()) {
            return "seller/requestForm";
        }
        Member seller = userDetails.getMember();
        usedBookRequestService.requestSell(
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
    @GetMapping("/my")
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
    @GetMapping("my/{id}")
    public String myRequestDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UsedBookRequest request = usedBookRequestService.findById(id);
        //퀀한 체크
        if (!request.getSeller().getId().equals(userDetails.getMember().getId())) {
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
    @GetMapping("/my/{id}/edit")
    public String editRequestForm(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UsedBookRequest request = usedBookRequestService.findById(id);
        //본인 상태 확인
        if (!request.getSeller().getId().equals(userDetails.getMember().getId())) {
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

    @PostMapping("my/{id}/edit")
    public String updateRequest(@PathVariable Long id, @ModelAttribute UsedBookRequestForm form, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        ResultDto request =  usedBookRequestService.updateRequest(id, userDetails.getMember().getId(), form);
        if (!request.isSuccess()) {
            model.addAttribute("message", request.getMessage());
            return "error/customError";
        }
        return "redirect:/seller/request/my";
    }

}
