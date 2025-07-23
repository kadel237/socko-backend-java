// WishlistServiceImpl.java
package com.soko_backend.service.wishlist;

import com.soko_backend.dto.wishlist.*;
import com.soko_backend.entity.product.Product;
import com.soko_backend.entity.user.UserEntity;
import com.soko_backend.entity.wishList.Wishlist;
import com.soko_backend.entity.wishlist.WishlistItem;
import com.soko_backend.exception.ResourceNotFoundException;
import com.soko_backend.repository.*;
import com.soko_backend.repository.wishlist.WishlistItemRepository;
import com.soko_backend.repository.wishlist.WishlistRepository;
import com.soko_backend.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    @Override
    public WishlistResponse getWishlistByUser() {
        Wishlist wishlist = getOrCreateWishlist();
        return toResponse(wishlist);
    }

    @Override
    public WishlistResponse addToWishlist(AddToWishlistRequest request) {
        Wishlist wishlist = getOrCreateWishlist();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        boolean alreadyExists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));

        if (!alreadyExists) {
            WishlistItem item = new WishlistItem();
            item.setProduct(product);
            item.setWishlist(wishlist);
            wishlist.getItems().add(item);
        }

        wishlistRepository.save(wishlist);
        return toResponse(wishlist);
    }

    @Override
    public WishlistResponse removeFromWishlist(Long wishlistItemId) {
        WishlistItem item = wishlistItemRepository.findById(wishlistItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Élément introuvable"));
        Wishlist wishlist = item.getWishlist();
        wishlist.getItems().remove(item);
        wishlistItemRepository.delete(item);
        return toResponse(wishlist);
    }

    private Wishlist getOrCreateWishlist() {
        UserEntity user = currentUserService.getCurrentUser()
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non connecté"));
        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist w = new Wishlist();
                    w.setUser(user);
                    return wishlistRepository.save(w);
                });
    }

    private WishlistResponse toResponse(Wishlist wishlist) {
        List<WishlistItemResponse> items = wishlist.getItems().stream().map(item -> {
            Product p = item.getProduct();
            return new WishlistItemResponse(item.getId(), p.getId(), p.getName(), p.getPrice());
        }).collect(Collectors.toList());

        return new WishlistResponse(wishlist.getId(), items);
    }
}
