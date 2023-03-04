package com.validator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.pojo.Goods;

public class GoodsValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		
		return Goods.class.equals(arg0);			//只针对Goods类的对象进行数据校验
	}
	
	@Override
	public void validate(Object object, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "goodsNo", 	null, "商品编码不能为空<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "goodsName", 	null, "商品名称不能为空<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "typeId", 	null, "请选择商品分类<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", 		null, "商品价格不能为空<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "stock", 		null, "商品库存不能为空<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timeSale", 	null, "起售时间不能为空<br>");
		
		Goods goods = (Goods) object;
		
		String goodsId 		= goods.getGoodsId();				//新添记录时，ID为null，所以这里不能有trim()方法
		String goodsNo		= goods.getGoodsNo().trim();
		String goodsName 	= goods.getGoodsName().trim();
		String typeId 		= goods.getTypeId().trim();
		String price 		= goods.getPrice().trim();
		String stock 		= goods.getStock().trim();
		String timeSale		= goods.getTimeSale().trim();
		String detail		= goods.getDetail().trim();
		
		goods.setGoodsNo(goodsNo);
		goods.setGoodsName(goodsName);
		goods.setTypeId(typeId);
		goods.setPrice(price);
		goods.setStock(stock);
		goods.setTimeSale(timeSale);
		goods.setDetail(detail);
		
		if (goodsId != null) {									//编辑商品时才有参数goodsId
			goodsId = goodsId.trim();
			goods.setGoodsId(goodsId);
			
			try { 									 
				Integer.parseInt(goodsId);					
			} catch (Exception e) {
				errors.rejectValue("goodsId", null, "参数goodsId错误<br>");
			}
		}
		
		if (typeId.length() > 0) {
			try { 									 
				Integer.parseInt(typeId);					
			} catch (Exception e) {
				errors.rejectValue("typeId", null, "请选择商品分类<br>");
			}
		}
		
		if (price.length() > 0) {		
			try {
				double priceDouble = Double.parseDouble(price);
				price = (new DecimalFormat("0.00")).format(priceDouble);
				
				goods.setPrice(price);
				
				if (priceDouble < 0) {
					errors.rejectValue("price", null, "商品价格输入有误<br>");
				}
			} catch (Exception e) {
				errors.rejectValue("price", null, "商品价格需为整数或小数<br>");
			}
		}
		
		if (stock.length() > 0) {
			try { 									 
				Integer.parseInt(stock);					
			} catch (Exception e) {
				errors.rejectValue("stock", null, "商品库存输入有误<br>");
			}
		}
		
		if (timeSale.length() > 0) {		
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sdf.parse(timeSale);
			} catch (Exception e) {
				errors.rejectValue("timeSale", null, "请输入正确的起售时间<br>");
			}
		}
			
		if (goodsNo.length() > 45) {
			errors.rejectValue("goodsNo", null, "商品编码不能多于45个字符<br>");
		}
		
		if (goodsName.length() > 45) {
			errors.rejectValue("goodsName", null, "商品名称不能多于45个字符<br>");
		}
		
		if (detail.length() > 10000) {
			errors.rejectValue("detail", null, "商品简介不能多于10000个字符<br>");
		}
	}
}