package com.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
//import org.springframework.stereotype.Repository;

import com.pojo.Goods;

//@Repository		注意：此处不能应用@Repository注解。因为这里由MyBatis应用了AOP动态代理技术
public class GoodsDaoProvider {

	public String selectCount (String search) {
		
		String sql = new SQL() {{
			SELECT("count(*)");
			FROM("tb_goods");
			
			if (search != null && search.trim().equals("") == false) {		//如果有搜索内容
				WHERE("CONCAT_WS(',', goodsNo, goodsName, typeName) "
						+ " like CONCAT('%', #{ search }, '%')");
			}
		}}.toString();
			
		return sql;
	}
	
	public String selectAll (
			@Param("search") 	  String search, 
			@Param("countShowed") int 	 countShowed, 
			@Param("pageSize")    int 	 pageSize) { 						//带3个参数
		
		String sql = new SQL() {{
			SELECT("*");
			FROM("tb_goods");
			
			if (search != null && search.trim().equals("") == false) {		//如果有搜索内容
				WHERE("CONCAT_WS(',', goodsNo, goodsName, typeName) "
						+ " like CONCAT('%', #{ search }, '%')");
			}
			
			LIMIT("#{ countShowed }, #{ pageSize }");
		}}.toString();
			
		return sql;
	}	
	
	public String updateGoods (Goods goods) {
		
		String goodsNo, goodsName, typeId, typeName, price, stock, timeSale, image, detail;
		
		goodsNo  	= goods.getGoodsNo();
		goodsName  	= goods.getGoodsName();
		typeId  	= goods.getTypeId();
		typeName  	= goods.getTypeName();
		price		= goods.getPrice();
		stock  		= goods.getStock();
		timeSale  	= goods.getTimeSale();
		image  		= goods.getImage();
		detail  	= goods.getDetail();
		
		String sql = new SQL() {{									//匿名类方式构造
				UPDATE("tb_goods");
				
				if (goodsNo != null) {								//如果输入的值与数据表中的值不同，才更新
					SET("goodsNo = #{ goodsNo }");
				}
				if (goodsName != null) {
					SET("goodsName = #{ goodsName }");
				}
				if (typeId != null) {
					SET("typeId = #{ typeId }");
				}
				if (typeName != null) {
					SET("typeName = #{ typeName }");
				}
				if (price != null) {
					SET("price = #{ price }");
				}
				if (stock != null) {
					SET("stock = #{ stock }");
				}
				if (timeSale != null) {
					SET("timeSale = #{ timeSale }");
				}
				if (image != null) {
					SET("image = #{ image }");
				}
				if (detail != null) {
					SET("detail = #{ detail }");
				}
				
				SET("timeRenew = (now())");
				WHERE("goodsId = #{ goodsId }");
		}}.toString();
			
		return sql;
	}
}
