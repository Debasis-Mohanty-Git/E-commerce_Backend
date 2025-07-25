package com.ecommerce.service;

import java.util.List;
import com.ecommerce.exception.OrderException;
import com.ecommerce.model.Address;
import com.ecommerce.model.Order;
import com.ecommerce.model.User;

public interface OrderService {
	
	public Order createOrder(User user,Address shippingAddress);
	public Order findOrderById(Long orderId) throws OrderException;
	public List<Order> userOrderHistory(Long userId) throws OrderException;
	public Order placedOrder(Long orderId) throws OrderException;
	public Order confirmOrder(Long orderId) throws OrderException;
	public Order shippedOrder(Long orderId) throws OrderException;
	public Order deleveredOrder(Long orderId) throws OrderException;
	public Order canceledOrder(Long orderId) throws OrderException;
	
	public List<Order> getAllOrders();
	public void deleteOrder(Long orderId) throws OrderException;
	

}
