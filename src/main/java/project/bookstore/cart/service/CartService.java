package project.bookstore.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.book.entity.Book;
import project.bookstore.book.service.BookService;
import project.bookstore.cart.dto.CartDto;
import project.bookstore.cart.dto.CartItemDto;
import project.bookstore.cart.dto.CartSearchCondition;
import project.bookstore.cart.entity.Cart;
import project.bookstore.cart.entity.CartItem;
import project.bookstore.cart.repository.CartItemRepository;
import project.bookstore.cart.repository.CartRepository;
import project.bookstore.member.entity.Member;
import project.bookstore.member.service.MemberService;
import project.bookstore.usedbook.entity.UsedBook;
import project.bookstore.usedbook.entity.UsedStatus;
import project.bookstore.usedbook.service.UsedbookService;

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

    private final UsedbookService usedbookService;

    //장바구니 조회(DTO)
    public CartDto getCartByMember(String email, CartSearchCondition condition) {
        Member member = memberService.findByEmail(email);
        Cart cart = cartRepository.findByMember(member)
                .orElseGet(() -> createCart(member));//없으면 새로 생성

        List<CartItem> items = cartItemRepository.searchByCartAndCondition(cart, condition);
        List<CartItemDto> itemDtos = items.stream()
                .map(this::getCartItemDto).collect(toList());

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
    public void addToCart(String email, Long bookId, Long usedBookId, int count) {
        Member member = memberService.findByEmail(email);
        Cart cart = cartRepository.findByMember(member)
                .orElseGet(() -> createCart(member));

        // Book 또는 UsedBook 중 하나만 선택
        Object bookOrUsed = null;
        if (bookId != null) {
            bookOrUsed = bookService.findById(bookId);
        } else if (usedBookId != null) {
            bookOrUsed = usedbookService.findById(usedBookId);
        } else {
            throw new IllegalArgumentException("책 정보가 없습니다.");
        }

        // 이미 장바구니에 담겨있는지 확인 (메서드 오버로딩 필요)
        Optional<CartItem> optItem;
        if (bookOrUsed instanceof Book) {
            optItem = cartItemRepository.findByCartAndBook(cart, (Book) bookOrUsed);
        } else if (bookOrUsed instanceof UsedBook) {
            optItem = cartItemRepository.findByCartAndUsedBook(cart, (UsedBook) bookOrUsed);
        } else {
            throw new IllegalArgumentException("지원하지 않는 타입입니다.");
        }

        if (optItem.isPresent()) {
            CartItem item = optItem.get();
            item.addCount(count);
        } else {
            CartItem item;
            if (bookOrUsed instanceof Book) {
                item = new CartItem(cart, (Book) bookOrUsed, count);
            } else if (bookOrUsed instanceof UsedBook) {
                item = new CartItem(cart, (UsedBook) bookOrUsed, count);
            } else {
                throw new IllegalArgumentException("지원하지 않는 타입입니다.");
            }
            cartItemRepository.save(item);
        }
    }

    //장바구니 아이템 삭제
    @Transactional
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public List<CartItemDto> findCartItemsByIds(List<Long> cartItemIds) {

        List<CartItem> items = cartItemRepository.findAllById(cartItemIds);

        //DTO변환
        List<CartItemDto> collect = items.stream()
                .map(this::getCartItemDto)
                .collect(toList());

        return collect;
    }

    private CartItemDto getCartItemDto(CartItem item) { //중복코트 메서드
        if (item.getBook() != null) {
            Book b = item.getBook();
            return new CartItemDto(item.getId(), b.getId(), "NEW", b.getTitle(), b.getPrice(), item.getCount(),false);
        } else if (item.getUsedBook() != null) {
            UsedBook ub = item.getUsedBook();
            boolean isSold = ub.getStatus() == UsedStatus.SOLD;
            return new CartItemDto(item.getId(), ub.getId(), "USED", ub.getTitle(), ub.getPrice(), item.getCount(),isSold);
        } else {
            return new CartItemDto(item.getId(), null, "", "알수없음", 0, item.getCount(),false);
        }
    }

}
