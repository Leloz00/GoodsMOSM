package com.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.pojo.Type;

@Service
public interface TypeService {
	
	public boolean selectAllForGOODS (HttpServletRequest request);			//用大写的GOODS，是为了在查找替换时不被更改	
	public boolean selectByTypeId (HttpServletRequest request);
	
	//----------------------------------------------------------------
	
	
}
