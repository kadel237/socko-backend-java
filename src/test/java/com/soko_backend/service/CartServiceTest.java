package com.soko_backend.service;

import com.soko_backend.dto.cart.AddCartItemRequest;
import com.soko_backend.dto.cart.UpdateCartItemRequest;
import com.soko_backend.entity.cart.Cart;
import com.soko_backend.entity.cart.CartItem;
import com.soko_backend.entity.product.Product;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.repository.*;
import com.soko_backend.repository.cart.CartItemRepository;
import com.soko_backend.repository.cart.CartRepository;
import com.soko_backend.security.CurrentUserService;
import com.soko_backend.service.cart.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private CartServiceImpl cartService;

    private UserEntity user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Produit Test");
        product.setPrice(100.0);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        when(currentUserService.getCurrentUser()).thenReturn(Optional.of(user));
    }

    @Test
    void testAddItemToCart() {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        var response = cartService.addItemToCart(request);

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals(200.0, response.getTotal());
    }

    @Test
    void testClearCart() {
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        cartService.clearCart();
        verify(cartRepository).save(cart);
    }

    @Test
    void testUpdateCartItem() {
        CartItem item = new CartItem();
        item.setId(10L);
        item.setProduct(product);
        item.setQuantity(1);
        item.setCart(cart);
        cart.setItems(new ArrayList<>(List.of(item)));

        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setCartItemId(10L);
        request.setQuantity(5);

        when(cartItemRepository.findById(10L)).thenReturn(Optional.of(item));

        var response = cartService.updateCartItem(request);

        assertNotNull(response);
        assertEquals(5, response.getItems().get(0).getQuantity());
    }
}