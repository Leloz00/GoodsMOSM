package com.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.pojo.Goods;

@Service
public interface GoodsService {
	
	public boolean selectAll (HttpServletRequest request);
	public boolean selectByGoodsId (HttpServletRequest request);
	public boolean existByGoodsNo (Goods goods, HttpServletRequest request);
	public boolean existByGoodsNoExceptGoodsId (Goods goods, HttpServletRequest request);
	
	public String  insertGoods (Goods goods, HttpServletRequest request);
	public boolean updateGoods (Goods goods, HttpServletRequest request);
	public boolean deleteByGoodsId (HttpServletRequest request);
	public boolean deleteByGoodsIdLot (String[] goodsIdArray, HttpServletRequest request);
}
