package com.ecommerce.service;

import org.springframework.stereotype.Service;

import com.ecommerce.model.OrderItem;

@Service
public interface OrderItemService {
	public OrderItem createOrderItem(OrderItem orderItem);

}
