package project.bookstore.usedbook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.member.security.CustomUserDetails;
import project.bookstore.usedbook.entity.UsedBookRequest;
import project.bookstore.usedbook.service.UsedBookRequestService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/request")
public class AdminUsedBookRequestController {//관리자용
    
    private final UsedBookRequestService usedBookRequestService;

    // 전체 목록
    @GetMapping
    public String requestList(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<UsedBookRequest> requests = usedBookRequestService.findAll();
        model.addAttribute("requests", requests);
        return "admin/usedBookRequestList";
    }

    // 상세 보기
    @GetMapping("/{id}")
    public String requestDetail(@PathVariable Long id, Model model) {
        UsedBookRequest request = usedBookRequestService.findById(id);
        model.addAttribute("request", request);
        return "admin/usedBookRequestDetail";
    }

    // 승인 처리
    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        usedBookRequestService.approveRequest(id, userDetails.getMember());
        return "redirect:/admin/request";
    }

    // 거절 폼
//    @GetMapping("/{id}/reject")
//    public String rejectForm(@PathVariable Long id, Model model) {
//        model.addAttribute("requestId",id);
//        return "admin/rejectUsedBookRequestForm";
//    }

    // 거절 처리
    @PostMapping("/{id}/reject")
    @ResponseBody // JAJX응답일 경우 추가되는 어노테이션
    public String reject(@PathVariable Long id, @RequestParam String rejectreason, @AuthenticationPrincipal CustomUserDetails userDetails) {
        usedBookRequestService.rejectRequest(id, userDetails.getMember(), rejectreason);
        return "OK";
    }


}
