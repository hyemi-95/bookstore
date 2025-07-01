package project.bookstore.order.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.book.entity.Book;
import project.bookstore.book.service.BookService;
import project.bookstore.cart.dto.CartDto;
import project.bookstore.cart.dto.CartItemDto;
import project.bookstore.cart.dto.CartSearchCondition;
import project.bookstore.cart.service.CartService;
import project.bookstore.member.entity.Member;
import project.bookstore.member.security.CustomUserDetails;
import project.bookstore.member.service.MemberService;
import project.bookstore.order.dto.OrderDto;
import project.bookstore.order.dto.OrderFormDto;
import project.bookstore.order.dto.OrderSearchCondition;
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
    public String orderFromCart(@RequestParam(value = "cartItemIds", required = false) List<Long> cartItemIds,
                                @RequestParam(value = "counts", required = false) List<Integer> counts,
                                Principal principal, Model model) {

        //체크된 상품이 없으면 에러 메시지와 함께 장바구니로 이동(이중방어)
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            CartDto cart = cartService.getCartByMember(principal.getName(), new CartSearchCondition());
            model.addAttribute("cart",cart);
            model.addAttribute("error","상품을 1개 이상 선택해주세요.");
            return "cart/cartList";
        }

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
    public String orderList(Model model, Principal principal, @ModelAttribute("condition") OrderSearchCondition condition) {
        Member member = memberService.findByEmail(principal.getName());
        List<OrderDto> orders = orderService.findOrdersByMember(member.getId(), condition);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member currentUser = userDetails.getMember();
        orderService.cancelOrder(orderId, currentUser);
        return "redirect:/orders";
    }
}
