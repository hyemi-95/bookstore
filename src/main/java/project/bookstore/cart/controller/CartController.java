package project.bookstore.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.bookstore.cart.dto.CartDto;
import project.bookstore.cart.dto.CartSearchCondition;
import project.bookstore.cart.service.CartService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    //장바구니 조회
    @GetMapping
    public String cartList(@ModelAttribute("condition") CartSearchCondition condition, Model model, Principal principal) {
        CartDto cart = cartService.getCartByMember(principal.getName(), condition);
        model.addAttribute("cart", cart);
        return "cart/cartList";
    }

    //장바구니 담기
    @PostMapping("/add")
    public String addToCart(@RequestParam(required = false) Long bookId,
                            @RequestParam (required = false)Long usedBookId,
                            @RequestParam(defaultValue = "1") int count,
                            Principal principal) {
        //둘 다 null이면 예외
        if (bookId == null && usedBookId == null) {
            throw new IllegalArgumentException("책 또는 중고책 ID는 필수입니다.");
        }
        cartService.addToCart(principal.getName(), bookId, usedBookId, count);
        return "redirect:/cart";
    }

    //장바구니에서 삭제
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return "redirect:/cart";
    }
}
