package com.ecommerce.service;

import org.springframework.stereotype.Service;

import com.ecommerce.exception.CartItemException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;

@Service
public interface CartItemService {
	public CartItem createCartItem(CartItem cartItem);
	public CartItem isCartItemExist(Cart cart,Product product,String size,Long userId);
	public void removeCartItem(Long userId,Long cartItemId)throws CartItemException,UserException;
	public CartItem findCartItemById(Long cartItemId)throws CartItemException;
	public CartItem updatecartItem(Long userId, Long cartItemId, int quantity) throws CartItemException;
	CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws CartItemException;

}
