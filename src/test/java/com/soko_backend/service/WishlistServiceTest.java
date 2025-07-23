package com.soko_backend.service;

import com.soko_backend.dto.wishlist.AddToWishlistRequest;
import com.soko_backend.entity.product.Product;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.entity.wishList.Wishlist;
import com.soko_backend.entity.wishlist.WishlistItem;
import com.soko_backend.repository.*;
import com.soko_backend.repository.wishlist.WishlistItemRepository;
import com.soko_backend.repository.wishlist.WishlistRepository;
import com.soko_backend.security.CurrentUserService;
import com.soko_backend.service.wishlist.WishlistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;
    @Mock
    private WishlistItemRepository wishlistItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    private UserEntity user;
    private Product product;
    private Wishlist wishlist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserEntity();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Produit Test");
        product.setPrice(50.0);

        wishlist = new Wishlist();
        wishlist.setId(1L);
        wishlist.setUser(user);
        wishlist.setItems(new ArrayList<>());

        when(currentUserService.getCurrentUser()).thenReturn(Optional.of(user));
    }

    @Test
    void testAddToWishlist() {
        AddToWishlistRequest request = new AddToWishlistRequest();
        request.setProductId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(wishlistRepository.findByUser(user)).thenReturn(Optional.of(wishlist));

        var response = wishlistService.addToWishlist(request);

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals("Produit Test", response.getItems().get(0).getProductName());
    }

    @Test
    void testRemoveFromWishlist() {
        WishlistItem item = new WishlistItem();
        item.setId(1L);
        item.setProduct(product);
        item.setWishlist(wishlist);
        wishlist.getItems().add(item);

        when(wishlistItemRepository.findById(1L)).thenReturn(Optional.of(item));

        var response = wishlistService.removeFromWishlist(1L);

        assertNotNull(response);
        assertEquals(0, response.getItems().size());
        verify(wishlistItemRepository).delete(item);
    }

    @Test
    void testGetWishlistByUser() {
        when(wishlistRepository.findByUser(user)).thenReturn(Optional.of(wishlist));
        var response = wishlistService.getWishlistByUser();
        assertNotNull(response);
        assertEquals(0, response.getItems().size());
    }
}
