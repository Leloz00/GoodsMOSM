package com.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.pojo.User;

@Service
public interface UserService {
	
	public boolean loginDo (HttpServletRequest request);
	
	
	
	
}
