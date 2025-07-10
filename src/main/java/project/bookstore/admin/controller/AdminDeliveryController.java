package project.bookstore.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.admin.dto.AdminDeliveryDetailDto;
import project.bookstore.admin.dto.AdminDeliveryListDto;
import project.bookstore.delivery.dto.DeliveryDto;
import project.bookstore.delivery.dto.DeliveryStatusHistoryDto;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.delivery.entity.DeliveryStatusHistory;
import project.bookstore.delivery.service.DeliveryService;
import project.bookstore.member.security.CustomUserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {
    private final DeliveryService deliveryService;

    @GetMapping
    public String deliveryList(Model model, Pageable pageable) {
        // 전체 배송목록 (검색/페이징 등)
        Page<AdminDeliveryListDto> deliveries = deliveryService.findAllForAdmin(pageable);
        model.addAttribute("deliveries", deliveries.getContent());
        model.addAttribute("page", deliveries);
        return "admin/deliveryList";
    }

    @GetMapping("/{id}")
    public String deliveryDetail(@PathVariable Long id, Model model) {//이력
        Delivery detail = deliveryService.findDetailForAdmin(id);
        AdminDeliveryDetailDto deliveryDto = AdminDeliveryDetailDto.from(detail);
        List<DeliveryStatusHistory> history = deliveryService.getDeliveryHistories(id);
        List<DeliveryStatusHistoryDto> historyDtos = history.stream().map(DeliveryStatusHistoryDto::form).collect(Collectors.toList());
        model.addAttribute("deliveryDetail", deliveryDto);
        model.addAttribute("historyList", historyDtos);
        return "admin/deliveryDetail";
    }

    @PostMapping("/{id}/status-change")
    public String changeStatus(@PathVariable Long id, @RequestParam("newStatus") DeliveryStatus newStatus, @AuthenticationPrincipal CustomUserDetails userDetails) {
        deliveryService.changeDeliveryStatus(id, newStatus, userDetails.getMember().getId()); // 관리자 변경
        return "redirect:/admin/delivery/" + id;
    }
}
