package com.ecommerce.controller;

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
import com.ecommerce.model.Rating;
import com.ecommerce.model.User;
import com.ecommerce.request.RatingRequest;
import com.ecommerce.service.RatingService;
import com.ecommerce.service.UserService;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RatingService ratingService;
	
	@PostMapping("/create")
	public ResponseEntity<Rating> createRatinghandler(@RequestBody RatingRequest req,
			@RequestHeader("Authorization")String jwt)throws UserException,ProductException{
		
		User user=userService.findProfileByJwt(jwt);
		Rating rating =ratingService.createRating(req, user);
		
		return new ResponseEntity<Rating>(rating,HttpStatus.OK);
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<List<Rating>> getProductRatingHandler(
	        @PathVariable Long productId) throws UserException, ProductException {

	    List<Rating> rating = ratingService.getProductRating(productId);

	    return new ResponseEntity<>(rating, HttpStatus.CREATED);
	}

}
