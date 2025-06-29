package project.bookstore.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.book.entity.Book;
import project.bookstore.book.service.BookService;
import project.bookstore.cart.dto.CartDto;
import project.bookstore.cart.dto.CartItemDto;
import project.bookstore.cart.entity.Cart;
import project.bookstore.cart.entity.CartItem;
import project.bookstore.cart.repository.CartItemRepository;
import project.bookstore.cart.repository.CartRepository;
import project.bookstore.member.entity.Member;
import project.bookstore.member.repository.MemberRepository;
import project.bookstore.member.service.MemberService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberService memberService;
    private final BookService bookService;

    //장바구니 조회(DTO)
    public CartDto getCartByMember(String email){
        Member member = memberService.findByEmail(email);
        Cart cart = cartRepository.findByMember(member)
                .orElseGet(()-> createCart(member));//없으면 새로 생성

        List<CartItem> items = cartItemRepository.findByCart(cart);
        List<CartItemDto> itemDtos = items.stream()
                .map(item -> new CartItemDto(
                        item.getId(),
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getBook().getPrice(),
                        item.getCount()
                )).collect(toList());

        return new CartDto(cart.getId(), itemDtos);
        
    }

    //장바구니 생성
    @Transactional
    public Cart createCart(Member member) {
        Cart cart = new Cart(member);
        return cartRepository.save(cart);
    }

    //장바구니 아이템 담기
    @Transactional
    public void addToCart(String email, Long bookId, int count) {
        Member member = memberService.findByEmail(email);
        Cart cart = cartRepository.findByMember(member)
                .orElseGet(()->createCart(member));
        Book book = bookService.findById(bookId);

        //이미 장바구니에 담겨있는지 확인
        Optional<CartItem> optItem = cartItemRepository.findByCartAndBook(cart, book);

        if (optItem.isPresent()) {
            CartItem item = optItem.get();
            item.addCount(count);
        }else {
            CartItem item = new CartItem(cart, book, count);
            cartItemRepository.save(item);
        }
    }

    @Transactional
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public List<CartItemDto> findCartItemsByIds(List<Long> cartItemIds) {
        List<CartItem> items = cartItemRepository.findAllById(cartItemIds);

        //DTO변환
        List<CartItemDto> collect = items.stream().map(item -> new CartItemDto(
                item.getId(),
                item.getBook().getId(),
                item.getBook().getTitle(),
                item.getBook().getPrice(),
                item.getCount()
        )).collect(toList());

        return collect;
    }

    //장바구니 아이템 삭제
}
