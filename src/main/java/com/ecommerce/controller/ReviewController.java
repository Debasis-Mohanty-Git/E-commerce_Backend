package com.ecommerce.controller;

import java.net.ResponseCache;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.exception.ProductException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.request.ReviewRequest;
import com.ecommerce.service.ReviewService;
import com.ecommerce.service.UserService;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/creates")
	public ResponseEntity<Review> createReviewhandler(@RequestHeader("Authorization")String jwt,
			@RequestBody ReviewRequest req)throws UserException,ProductException{
		
		User user=userService.findProfileByJwt(jwt);
		Review review=reviewService.createReview(req, user);
		
		return new ResponseEntity<Review>(review,HttpStatus.ACCEPTED);
  }
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Review>> getAllreviewHandler(@PathVariable Long productId){
		
		List<Review> review=reviewService.getAllreview(productId);
		
		return new ResponseEntity<>(review,HttpStatus.ACCEPTED);
	}
	
	
}
