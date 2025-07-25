package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.OrderException;
import com.ecommerce.model.Address;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.User;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;

@Service
public class OrderServiceImplementation implements OrderService{
	
	
	@Autowired
	private CartService cartService;
    @Autowired
	private OrderRepository orderRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userReposirory;
    
    
	@Override
	public Order createOrder(User user, Address shippingAddress) {
	
		shippingAddress.setUser(user);
		Address address=addressRepository.save(shippingAddress);
		user.getAddresses().add(address);
		userReposirory.save(user);
		
		Cart cart=cartService.findUserCart(user.getId());
		List<OrderItem> orderItems=new ArrayList<>();
		
		for(CartItem item:cart.getCartItem()) {
			OrderItem orderItem =new OrderItem();
			orderItem.setPrice(item.getPrice());
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUserId(item.getUserId());
			orderItem.setDiscountedPrice(item.getDiscountPrice());
			
			OrderItem createOrderItem=orderItemRepository.save(orderItem);
			orderItems.add(createOrderItem);
		}
		
		Order createOrder=new Order();
		createOrder.setUser(user);
		createOrder.setOrderItems(orderItems);
		createOrder.setTotalPrice(cart.getTotalPrice());
		createOrder.setTotalDiscountedPrice(cart.getTotalDiscountPrice());
		createOrder.setDiscount(cart.getDiscount());
		createOrder.setTotalItem(cart.getTotalItem());
		
		createOrder.setShippingAddress(shippingAddress);
		createOrder.setOrderDate(LocalDateTime.now());
		createOrder.setOrderStatus("PENDING");
		createOrder.setCreatedAt(LocalDateTime.now());
		
		Order saveOrder=orderRepository.save(createOrder);
		
		for(OrderItem item:orderItems) {
			item.setOrder(saveOrder);
			orderItemRepository.save(item);
		}
		
		return saveOrder;
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException{
		Optional<Order> opt=orderRepository.findById(orderId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new OrderException("Order not exists with yhis is:"+orderId);
	}

	@Override
	public List<Order> userOrderHistory(Long userId) throws OrderException {
		List<Order> orders =orderRepository.getUserOrders(userId);
		return orders;
	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("PLACED");
		order.getPaymentDetails().setStatus("COMPLETED");
		return order;
	}

	@Override
	public Order confirmOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("CONFIRMED");
		
		return orderRepository.save(order);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("SHIPPED");
		
		return orderRepository.save(order);
	}

	@Override
	public Order deleveredOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("DELIVERED");
		
		return orderRepository.save(order);
	}

	@Override
	public Order canceledOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus("CANCELED");
		
		return orderRepository.save(order);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		orderRepository.deleteById(orderId);
	}

}
