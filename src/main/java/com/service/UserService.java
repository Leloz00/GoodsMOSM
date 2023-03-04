package com.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.pojo.User;

@Service
public interface UserService {
	
	public boolean loginDo(HttpServletRequest request);
	
	//-------------------------------------------------------
	
	public boolean queryAll (HttpServletRequest request);
	public boolean queryByUserId (HttpServletRequest request);
	public boolean existByUsername (User user, HttpServletRequest request);
	public boolean existByUsernameExceptUserId (User user, HttpServletRequest request);
	
	public String  addUser (User user, HttpServletRequest request);
	public boolean editUser (User user, HttpServletRequest request);
	public boolean deleteByUserId (HttpServletRequest request);
	public boolean deleteByUserIdLot (String[] userIdArray, HttpServletRequest request);
}
