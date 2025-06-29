package project.bookstore.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.book.entity.Book;
import project.bookstore.book.service.BookService;
import project.bookstore.cart.dto.CartItemDto;
import project.bookstore.cart.service.CartService;
import project.bookstore.member.entity.Member;
import project.bookstore.member.service.MemberService;
import project.bookstore.order.dto.OrderDto;
import project.bookstore.order.dto.OrderFormDto;
import project.bookstore.order.service.OrderService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final BookService bookService;
    private final CartService cartService;


    /**
     * 주문확인 및 배송지 입력 폼
     * @param cartItemIds
     * @param counts
     * @param principal
     * @param model
     * @return
     */

    @PostMapping("/form-cart")
    public String orderFromCart(@RequestParam("cartItemIds") List<Long> cartItemIds, @RequestParam("counts") List<Integer> counts, Principal principal, Model model) {

        List<CartItemDto> selectedItem = cartService.findCartItemsByIds(cartItemIds);

        model.addAttribute("orderItems", selectedItem);
        model.addAttribute("counts", counts);
        model.addAttribute("orderForm", new OrderFormDto());//배송지 폼용 Dto

        return "order/orderForm";
    }


    /**
     * 주문 저장
     * @param cartItemIds
     * @param counts
     * @param receiver
     * @param address
     * @param phone
     * @param principal
     * @return
     */
    @PostMapping("/submit-cart")
    public String submitCartOrder(@RequestParam("cartItemIds") List<Long> cartItemIds, @RequestParam("counts") List<Integer> counts,
                                  @RequestParam("receiver") String receiver, @RequestParam("address") String address, @RequestParam("phone") String phone, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());

        orderService.orderFromCartWithDeliveryInfo(member.getId(), cartItemIds, counts, receiver, address, phone);

        return "redirect:/orders";
    }


    /***
     * 주문 목록(DTO)
     * @param model
     * @param principal
     * @return
     */
    @GetMapping
    public String orderList(Model model, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        List<OrderDto> orders = orderService.findOrdersByMember(member.getId());
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
