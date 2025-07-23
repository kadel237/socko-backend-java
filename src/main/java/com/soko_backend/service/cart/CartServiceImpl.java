// CartServiceImpl.java
package com.soko_backend.service.cart;

import com.soko_backend.dto.cart.*;
import com.soko_backend.entity.cart.Cart;
import com.soko_backend.entity.cart.CartItem;
import com.soko_backend.entity.product.Product;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.exception.ResourceNotFoundException;
import com.soko_backend.repository.*;
import com.soko_backend.repository.cart.CartItemRepository;
import com.soko_backend.repository.cart.CartRepository;
import com.soko_backend.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    public CartResponse getCartByUser() {
        Cart cart = getOrCreateCart();
        return toCartResponse(cart);
    }

    @Override
    public CartResponse addItemToCart(AddCartItemRequest request) {
        Cart cart = getOrCreateCart();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Override
    public CartResponse updateCartItem(UpdateCartItemRequest request) {
        CartItem item = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        return toCartResponse(item.getCart());
    }

    @Override
    public CartResponse removeCartItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        Cart cart = item.getCart();
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        return toCartResponse(cart);
    }

    @Override
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.clearItems();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart() {
        UserEntity user = currentUserService.getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non connecté"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }
    private CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream().map(item -> {
            Product p = item.getProduct();
            return new CartItemResponse(
                    item.getId(),
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    item.getQuantity()
            );
        }).collect(Collectors.toList());

        double total = itemResponses.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        return new CartResponse(cart.getId(), itemResponses, total);
    }
}
