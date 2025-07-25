package com.ecommerce.controller;

import com.ecommerce.exception.CartItemException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.User;
import com.ecommerce.request.AddItemRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.CartItemService;
import com.ecommerce.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    // 1. Create Cart Item
    @PostMapping("/create")
    public ResponseEntity<CartItem> createCartItemHandler(@RequestBody CartItem cartItem) {
        CartItem createdItem = cartItemService.createCartItem(cartItem);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem) throws UserException, CartItemException {

        User user = userService.findProfileByJwt(jwt);
        CartItem updatedItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);

        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }




    // 4. Remove Cart Item
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<ApiResponse> removeCartItemHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long cartItemId) throws UserException, CartItemException {

        User user = userService.findProfileByJwt(jwt);  // Extract user from JWT
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res = new ApiResponse();
        res.setMessage("Cart item removed successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // 5. Get Cart Item by ID
    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItem> getCartItemByIdHandler(@PathVariable Long cartItemId)
            throws CartItemException {
        CartItem item = cartItemService.findCartItemById(cartItemId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
}
