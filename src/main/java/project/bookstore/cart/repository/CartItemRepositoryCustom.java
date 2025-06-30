package project.bookstore.cart.repository;

import project.bookstore.cart.dto.CartSearchCondition;
import project.bookstore.cart.entity.Cart;
import project.bookstore.cart.entity.CartItem;

import java.util.List;

public interface CartItemRepositoryCustom {
    List<CartItem> searchByCartAndCondition(Cart cart, CartSearchCondition condition);
}
