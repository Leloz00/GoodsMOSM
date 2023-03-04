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

import com.pojo.User;
import com.service.UserService;
import com.validator.UserValidator;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;			//自动注入
	
	
	@RequestMapping("/userList")
	public String userList (HttpServletRequest request) {
				
		userService.queryAll(request);								//****
		
		return "user/userList.jsp";
	}
	
	
	@RequestMapping("/userShow")
	public String userShow (HttpServletRequest request) {
				
		userService.queryByUserId(request);						//****
		
		return "user/userShow.jsp";
	}
	
	
	@RequestMapping("/userAdd")
	public String userAdd (HttpServletRequest request) {
		
		return "user/userAdd.jsp";
	}
	
	
	@InitBinder											//将首先执行的动作
	public void initBinder (DataBinder binder) {
		
		binder.setValidator(new UserValidator());		//进行数据校验
	}
	
	
	@RequestMapping(value="/userAddDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String userAddDo (@Validated User user, Errors errors, HttpServletRequest request) {
		
		String msg = "";
		
		String password = user.getPassword();
		
		if (password.length() < 1) {
			errors.rejectValue("password", null, "*密码不能少于1个字符");
		}
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();					//该字段的错误信息
			}
			
			return msg;
		}
		
		String userId = userService.addUser(user, request);		//****
		
		if (userId.equals("0")) {									//新添失败
			msg = (String) request.getAttribute("msg");
			return msg;
		}
		
		return "@Redirect:userShow?userId=" + userId;				//新添成功
	}
	

	@RequestMapping("/userEdit")
	public String userEdit (HttpServletRequest request) {
				
		userService.queryByUserId(request);						//****
		
		return "user/userEdit.jsp";
	}	
	
	
	@RequestMapping(value="/userEditDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String userEditDo (@Validated User user, Errors errors, HttpServletRequest request) {

		String msg = "";
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();					//该字段的错误信息
			}
			
			return msg;
		}
		
		boolean result = userService.editUser(user, request);		//****
		
		if (result == false) {										//更新失败
			msg = (String) request.getAttribute("msg");
			return msg;
		}
		
		return "@Redirect:userShow?userId=" + user.getUserId();		//更新成功
	}
	
	
	@RequestMapping("/userDeleteDo")
	public String userDeleteDo (HttpServletRequest request) {
				
		userService.deleteByUserId(request);							//****
		
		return "redirect:userList";									//重定向到用户列表页
	}
	

	@RequestMapping("/register")
	public String register (HttpServletRequest request) {
		
		return "user/register.jsp";
	}

	
	@RequestMapping(value="/registerDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String registerDo (@Validated User user, Errors errors, HttpServletRequest request) {
		
		String msg = "";
		
		String password = user.getPassword();
		
		if (password.length() < 1) {
			errors.rejectValue("password", null, "*密码不能少于1个字符");
		}
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();					//该字段的错误信息
			}
			
			return msg;
		}
		
		String userId = userService.addUser(user, request);		//****
		
		if (userId.equals("0")) {									//新添失败
			msg = (String) request.getAttribute("msg");
			return msg;
		}
		
		return "@Redirect:login";
	}
}
