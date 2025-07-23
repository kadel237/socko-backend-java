// WishlistService.java
package com.soko_backend.service.wishlist;

import com.soko_backend.dto.wishlist.AddToWishlistRequest;
import com.soko_backend.dto.wishlist.WishlistResponse;

public interface WishlistService {
    WishlistResponse getWishlistByUser();
    WishlistResponse addToWishlist(AddToWishlistRequest request);
    WishlistResponse removeFromWishlist(Long wishlistItemId);
}
