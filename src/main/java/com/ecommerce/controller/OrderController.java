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

import com.ecommerce.exception.OrderException;
import com.ecommerce.exception.ProductException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.Address;
import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/")
	public ResponseEntity<Order> createOrderHandler(@RequestBody Address shippingAddress,
			@RequestHeader("Authorization")String jwt)throws UserException{
		User user=userService.findProfileByJwt(jwt);
		Order order=orderService.createOrder(user, shippingAddress);
		System.out.println("Order :" +order);
		return new ResponseEntity<Order>(order,HttpStatus.CREATED);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<Order>> userOrderHistoryHandler(@RequestHeader("Authorization")String jwt)
			throws UserException,OrderException{
		
		User user=userService.findProfileByJwt(jwt);
		List<Order> orders=orderService.userOrderHistory(user.getId());
		return new ResponseEntity<List<Order>>(orders,HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Order> findOrderByIdHandler(@PathVariable("id") Long orderId,@RequestHeader("Authorization")String jwt)
	throws UserException,OrderException{
		
		User user=userService.findProfileByJwt(jwt);
		Order order=orderService.findOrderById(orderId);
		
		return new ResponseEntity<Order>(order,HttpStatus.ACCEPTED);
	}
	
}
