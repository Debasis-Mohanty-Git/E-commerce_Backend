package com.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Rating;
import com.ecommerce.model.User;
import com.ecommerce.request.RatingRequest;

@Service
public interface RatingService {
	
	public Rating createRating(RatingRequest req,User user)throws ProductException;
	public List<Rating> getProductRating(Long productId);
}
