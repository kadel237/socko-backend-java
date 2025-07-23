// CartService.java
package com.soko_backend.service.cart;

import com.soko_backend.dto.cart.*;
import java.util.List;

public interface CartService {
    CartResponse getCartByUser();
    CartResponse addItemToCart(AddCartItemRequest request);
    CartResponse updateCartItem(UpdateCartItemRequest request);
    CartResponse removeCartItem(Long cartItemId);
    void clearCart();
}
