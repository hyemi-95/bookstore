package project.bookstore.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.book.entity.Book;
import project.bookstore.book.service.BookService;
import project.bookstore.member.entity.Member;
import project.bookstore.member.service.MemberService;
import project.bookstore.order.dto.OrderDto;
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


    /***
     * 주문 생성 폼
     * @param bookId
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/new")
    public String createForm(@RequestParam("bookId") Long bookId, Model model, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        Book book = bookService.findById(bookId);

        model.addAttribute("member", member);
        model.addAttribute("book", book);

        return "order/orderForm";
    }

    /***
     * 주문 저장
     * @param bookId
     * @param count
     * @param principal
     * @return
     */
    @PostMapping("/new")
    public String createOrder(@RequestParam("bookId") Long bookId,
                              @RequestParam("count") int count,
                              Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        orderService.order(member.getId(), bookId, count);

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
