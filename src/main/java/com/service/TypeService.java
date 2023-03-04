package com.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.pojo.Type;

@Service
public interface TypeService {
	
	public boolean queryAllForGOODS (HttpServletRequest request);			//用大写的GOODS，是为了在查找替换时不被更改	
	public boolean queryByTypeId (HttpServletRequest request);
	
	//----------------------------------------------------------------
	
	public boolean queryAll (HttpServletRequest request);
	public boolean existByTypeName (Type type, HttpServletRequest request);
	public boolean existByTypeNameExceptTypeId (Type type, HttpServletRequest request);
	
	public String  addType (Type type, HttpServletRequest request);
	public boolean editType (Type type, HttpServletRequest request);
	public boolean deleteByTypeId (HttpServletRequest request);
	public boolean deleteByTypeIdLot (String[] typeIdArray, HttpServletRequest request);
}
