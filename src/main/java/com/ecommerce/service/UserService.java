package com.ecommerce.service;


import org.springframework.stereotype.Service;

import com.ecommerce.exception.UserException;
import com.ecommerce.model.User;

@Service
public interface UserService {
	public User findUserById(Long userId) throws UserException;
	public User findProfileByJwt(String jwt) throws UserException;
}
