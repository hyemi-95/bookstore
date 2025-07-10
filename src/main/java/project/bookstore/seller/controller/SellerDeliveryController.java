package project.bookstore.seller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.delivery.dto.DeliveryDto;
import project.bookstore.delivery.dto.DeliveryStatusHistoryDto;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.delivery.service.DeliveryService;
import project.bookstore.member.entity.Member;
import project.bookstore.member.security.CustomUserDetails;
import project.bookstore.seller.dto.SellerDeliveryDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller/delivery")
public class SellerDeliveryController {
    private final DeliveryService deliveryService;

    @GetMapping
    public String myDeliveryList(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, Pageable pageable) {
        Member seller = userDetails.getMember();
        Page<SellerDeliveryDto> deliveries = deliveryService.findBySeller(seller, pageable);
        model.addAttribute("deliveries", deliveries.getContent());
        model.addAttribute("page", deliveries);
        return "seller/deliveryList";
    }

    @GetMapping("/{id}")
    public String myDeliveryDetail(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // 권한체크(내가 판매자일 경우만)
        Member seller = userDetails.getMember();
        SellerDeliveryDto delivery = deliveryService.findDtoByIdForSeller(id,seller);
        List<DeliveryStatusHistoryDto> history = deliveryService.getDeliveryHistoryDtos(id);
        model.addAttribute("delivery", delivery);
        model.addAttribute("historyList", history);
        return "seller/deliveryDetail";
    }

    @PostMapping("/{id}/status-change")
    public String changeStatus(@PathVariable Long id, @RequestParam("newStatus") DeliveryStatus newStatus, @AuthenticationPrincipal CustomUserDetails userDetails) {
        deliveryService.changeDeliveryStatus(id, newStatus, userDetails.getMember().getId());
        return "redirect:/seller/delivery/" + id;
    }
}
