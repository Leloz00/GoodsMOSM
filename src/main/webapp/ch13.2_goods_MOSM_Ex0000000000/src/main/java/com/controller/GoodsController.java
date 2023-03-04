package com.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pojo.Goods;
import com.service.GoodsImageService;
import com.service.GoodsService;
import com.service.TypeService;
import com.validator.GoodsValidator;

@Controller
public class GoodsController {
	
	@Autowired
	private GoodsService goodsService;			//自动注入
	
	@Autowired
	private TypeService  typeService;	
	
	@Autowired
	GoodsImageService goodsImageService;	
	
	
	@RequestMapping("/goodsList")				//商品列表页
	public String goodsList (HttpServletRequest request) {
				
		goodsService.selectAll(request);
		//####【添加代码】						//****获取分类列表，用大写的GOODS，是为了在查找替换时不被更改	
		
		return "goods/goodsList.jsp";
	}
	
	
	@RequestMapping("/goodsListLayout")			//商品平铺页
	public String goodsListLayout (HttpServletRequest request) {
				
		goodsService.selectAll(request);
		//####【添加代码】						//****获取分类列表，用大写的GOODS，是为了在查找替换时不被更改
		
		return "goods/goodsListLayout.jsp";
	}
	
	
	@RequestMapping("/goodsShow")
	public String goodsShow (HttpServletRequest request) {
				
		//####【添加代码】				//****根据ID获取记录

		//-------------------------- 生成商品图片的标签和链接
		Goods goods = (Goods) request.getAttribute("goods");		//获取在Service中已经生成的goods		
		
		if (goods == null) {
			return "goods/goodsShow.jsp";
		}
		
		String image = goods.getImage();
		//####【添加代码】		//****生成商品图片标签和图片链接 
		
		goods.setImage(image);
		request.setAttribute("goods", goods);						//将image的新值更新到goods
		
		return "";		//####【修改代码】
	}
	
	
	@RequestMapping("/goodsShowImageRenew")
	public String goodsShowImageRenew (HttpServletRequest request) {
				
		goodsService.selectByGoodsId(request);
	
		return "goods/goodsShowImageRenew.jsp";
	}
	
	
	@RequestMapping("/goodsAdd")
	public String goodsAdd (HttpServletRequest request) {

		//####【添加代码】		//****获取分类列表

		String goodsUUID = "8888";		//####【修改代码】	//生成通用唯一标识，作为临时的ID，用于保存上传的图片
		Goods goods = new Goods();
		goods.setGoodsId(goodsUUID);
		goods.setImage("");
		request.setAttribute("goods", goods);						//用于给网页的iframe的src和隐藏域控件赋值
		
		return "goods/goodsAdd.jsp";
	}
	
	
	@InitBinder														//将首先执行的动作
	public void initBinder (DataBinder binder) {
		
		binder.setValidator(new GoodsValidator());					//进行数据校验
	}
	
	
	//####【添加代码】									//避免返回的中文内容为乱码
	//####【添加代码】									//返回数据给Ajax，而不是转发到网址
	public String goodsAddDo (@Validated Goods goods, Errors errors, HttpServletRequest request) {
		
		String msg = "";
		
		//####【添加代码】								//如果校验时生成了错误信息
		
		//####【添加代码】								//将错误消息置于msg
		
		String goodsId = "";	//####【修改代码】
		
		if (goodsId.equals("0")) {									//添加失败
			msg = (String) request.getAttribute("msg");
			return msg;												//输出失败消息
		}
		
		return "";	//####【修改代码】					//添加成功，将由JS实现网页跳转
	}
	

	@RequestMapping("/goodsEdit")
	public String goodsEdit (HttpServletRequest request) {
				
		//####【添加代码】						//****获取该记录
		//####【添加代码】						//****获取分类列表
		
		return "goods/goodsEdit.jsp";
	}	
	
	
	@RequestMapping(value="/goodsEditDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String goodsEditDo (@Validated Goods goods, Errors errors, HttpServletRequest request) {
		
		String msg = "";
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();
			}
			
			return msg;
		}
		
		boolean result = false;		//####【修改代码】				//****更新记录
		
		if (result == false) {										//更新失败
			msg = (String) request.getAttribute("msg");
			return msg;
		}
		
		return "";		//####【修改代码】		//更新成功
	}
	
	
	@RequestMapping("/goodsDeleteDo")
	public String goodsDeleteDo (HttpServletRequest request) {
				
		//####【添加代码】
		
		return "";		//####【修改代码】						//重定向到商品列表页
	}
}
