package project.bookstore.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.bookstore.cart.dto.CartDto;
import project.bookstore.cart.service.CartService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    //장바구니 조회
    @GetMapping
    public String cartList(Model model, Principal principal) {
        CartDto cart = cartService.getCartByMember(principal.getName());
        model.addAttribute("cart", cart);
        return "cart/cartList";
    }

    //장바구니 담기
    @PostMapping("/add")
    public String addToCart(@RequestParam Long bookId,
                            @RequestParam(defaultValue = "1") int count,
                            Principal principal) {
        cartService.addToCart(principal.getName(), bookId, count);
        return "redirect:/cart";
    }

    //장바구니에서 삭제
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return "redirect:/cart";
    }
}
