package com.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.CartItemException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;

@Service
public class cartItemserviceImplementation implements CartItemService {
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartRepository cartRepository;

	@Override
	public CartItem createCartItem(CartItem cartItem) {
		cartItem.setQuantity(1);
		cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
		cartItem.setDiscountPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());
		
		CartItem createdcardItem=cartItemRepository.save(cartItem);
		return createdcardItem;
	}

	@Override
	public CartItem updatecartItem(Long userId, Long cartItemId, int quantity) throws CartItemException {
	    // validate cart item belongs to user, exists etc.
	    CartItem item = cartItemRepository.findById(cartItemId)
	                        .orElseThrow(() -> new CartItemException("Cart item not found"));
	    
	    item.setQuantity(quantity);
	    return cartItemRepository.save(item);
	}


	@Override
	public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
		CartItem cartItem=cartItemRepository.isExist(cart, product, size, userId);
		return cartItem;
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException {
	    CartItem cartItem = cartItemRepository.findById(cartItemId)
	        .orElseThrow(() -> new CartItemException("Cart item not found with id: " + cartItemId));

	    if (!cartItem.getUserId().equals(userId)) {
	        throw new CartItemException("This cart item does not belong to you");
	    }

	    cartItemRepository.deleteById(cartItemId);
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		Optional<CartItem> opt=cartItemRepository.findById(cartItemId);
		
		if(opt.isEmpty()) {
			return opt.get();
		}
		throw new CartItemException("Cart not Found with id:"+cartItemId);
	}

	 @Override
	    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem newCartItem) throws CartItemException {
	        Cart cart = cartRepository.findByUserId(userId);
	        if (cart == null) {
	            throw new CartItemException("Cart not found for user ID: " + userId);
	        }

	        CartItem existingItem = cartItemRepository.findById(cartItemId)
	                .orElseThrow(() -> new CartItemException("Cart item not found with ID: " + cartItemId));

	        if (!existingItem.getCart().getId().equals(cart.getId())) {
	            throw new CartItemException("Cart item does not belong to the current user.");
	        }

	        // Update fields (you can expand based on what's allowed)
	        existingItem.setSize(newCartItem.getSize());
	        existingItem.setProduct(newCartItem.getProduct());
	        existingItem.setQuantity(newCartItem.getQuantity());

	        return cartItemRepository.save(existingItem);
	    }



}
