package project.bookstore.delivery.controller;

import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.delivery.dto.DeliveryDto;
import project.bookstore.delivery.dto.DeliveryStatusHistoryDto;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.delivery.entity.DeliveryStatusHistory;
import project.bookstore.delivery.repository.DeliveryRepository;
import project.bookstore.delivery.service.DeliveryService;
import project.bookstore.member.entity.Member;
import project.bookstore.member.security.CustomUserDetails;

import org.springframework.security.access.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliveryRepository deliveryRepository;

    /**
     * 배송 상세/상태변경 폼
     */
    @GetMapping("/{id}")
    public String deliveryDetail(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails,Model model) {
        Delivery delivery = deliveryService.findById(id);
        DeliveryDto deliveryDto = DeliveryDto.from(delivery);

        List<DeliveryStatusHistory> historyList = deliveryService.getDeliveryHistories(delivery.getId());
        List<DeliveryStatusHistoryDto> history = historyList.stream().map(DeliveryStatusHistoryDto::form).collect(Collectors.toList());

        Member currentUser = userDetails.getMember();
        model.addAttribute("delivery",deliveryDto);
        model.addAttribute("historyList", history);
        model.addAttribute("currentUser", currentUser);

        return "delivery/deliveryDetail";
    }

    /**
     * 배송상태 변경 처리(POST)
     */
    @PostMapping("{id}/status-change")
    public String changeStatus(@PathVariable Long id, @RequestParam("newStatus")DeliveryStatus newStatus,
                               @AuthenticationPrincipal CustomUserDetails userDetails) throws AccessDeniedException {
        Long changerId = userDetails.getMember().getId();
        deliveryService.changeDeliveryStatus(id, newStatus, changerId);

        return "redirect:/delivery/"+id;
    }
}
