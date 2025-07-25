package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.ProductException;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.ReviewRepository;
import com.ecommerce.request.ReviewRequest;

@Service
public class ReviewServiceImplementation implements ReviewService{
	
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Review createReview(ReviewRequest req, User user) throws ProductException {
	   Product product=productService.findProductById(req.getProductId());
	   Review review=new Review();
	   
	   review.setUser(user);
	   review.setReview(null);;
	   review.setProduct(product);
	   review.setCreatedAt(LocalDateTime.now());
	   
	   return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllreview(Long productId) {
		
		return reviewRepository.getAllProductsReview(productId);
	}
}
