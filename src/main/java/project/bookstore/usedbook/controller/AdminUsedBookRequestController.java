package project.bookstore.usedbook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.member.security.CustomUserDetails;
import project.bookstore.usedbook.dto.UsedBookRequestDetailDto;
import project.bookstore.usedbook.dto.UsedBookRequestDto;
import project.bookstore.usedbook.service.UsedBookRequestService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/request")
public class AdminUsedBookRequestController {//관리자용
    
    private final UsedBookRequestService usedBookRequestService;

    // 전체 목록
    @GetMapping
    public String requestList(Model model ,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Page<UsedBookRequestDto> requests = usedBookRequestService.findAll(PageRequest.of(page, size));
        model.addAttribute("requests", requests.getContent());
        model.addAttribute("page", requests);
        return "admin/usedBookRequestList";
    }

    // 상세 보기
    @GetMapping("/{id}")
    public String requestDetail(@PathVariable Long id, Model model) {
        UsedBookRequestDetailDto request = usedBookRequestService.findById(id);
        model.addAttribute("request", request);
        return "admin/usedBookRequestDetail";
    }

    // 승인 처리
    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        usedBookRequestService.approveRequest(id, userDetails.getMember());
        return "redirect:/admin/request";
    }

    // 거절 처리
    @PostMapping("/{id}/reject")
    @ResponseBody // JAJX응답일 경우 추가되는 어노테이션
    public String reject(@PathVariable Long id, @RequestParam String rejectreason, @AuthenticationPrincipal CustomUserDetails userDetails) {
        usedBookRequestService.rejectRequest(id, userDetails.getMember(), rejectreason);
        return "OK";
    }


}
