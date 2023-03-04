package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pojo.Goods;
import com.service.GoodsImageService;

@Controller
public class GoodsImageController {
	
	@Autowired
	private GoodsImageService goodsImageService;			//自动注入

	
	@RequestMapping("/goodsImageUpload")
	public String goodsImageUpload (HttpServletRequest request) {
		
		String msg = goodsImageService.getImageTagAndLink(request);			//****生成商品图片的标签、图片链接和管理链接。图片名称来自于地址栏中的参数值 
		request.setAttribute("msg", msg);
		
		return "goods/goodsImageUpload.jsp";								//转发到商品图片上传页
	}
	
	@RequestMapping("/goodsImageUploadDo")
	public String goodsImageUploadDo (HttpServletRequest request) {
				
		goodsImageService.imageUploadDo(request);							//****上传图片，并更新数据表中的image字段，或在新添商品页中更新session中的image的值 
		
		return "goods/goodsImageUpload.jsp";								//转发到商品图片上传页
	}
	
	@RequestMapping("/goodsImageDeleteDo")
	public String goodsImageDeleteDo (HttpServletRequest request) {
				
		Goods goods = goodsImageService.imageDeleteDo(request);
		
		String goodsId = goods.getGoodsId();
		String image   = goods.getImage();	
		
		String url = "goodsImageUpload?goodsId=" + goodsId + "&image=" + image;
		return "redirect:" + url;											//重定向到图片上传页
	}
}
