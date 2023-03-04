package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.UserService;

//####【新添代码】		
public class LoginController {
	
	//####【新添代码】							//自动注入
	//####【新添代码】
	
	
	@RequestMapping("/")					//默认首页
	public String index() {
		
		return "index.jsp";
	}

	
	@RequestMapping("/login")
	public String login (HttpServletRequest request) {
		
		//-------------------- 将显示用户注册完成时的消息
		if (request.getSession().getAttribute("msg") != null) {				//如果session中有消息。新添或修改记录后保存的消息
			String msg = "";
			msg += request.getSession().getAttribute("msg").toString();		//读取session中的消息
			request.getSession().removeAttribute("msg");					//从session中移除此键值			
			request.setAttribute("msg", msg);								//将消息赋值给request
		}
		
		return "login.jsp";
	}
		
	
	//####【新添代码】	//避免返回的中文内容为乱码
	//####【新添代码】												//返回数据给Ajax，而不是转发到网址
	public String loginDo (HttpServletRequest request) {
		
		boolean result = false;		//####【新添代码】				//验证登录
		
		if (result == false) {									//登录失败
			String msg = (String) request.getAttribute("msg");
			return "";		//####【新添代码】						//输出消息
		}

		return "";		//####【新添代码】							//登录成功，由JavaScript实现网页跳转
	}
	
	
	@RequestMapping("/main")
	public String main (HttpServletRequest request) {

		return "main.jsp";
	}
	
	
	@RequestMapping("/logout")
	public String logout (HttpServletRequest request) {
				
		request.getSession().invalidate();						//使session失效，能清除本浏览器在此网站中的所有session

		String msg = "已退出登录！";
		request.setAttribute("msg", msg);			

		return "login.jsp";
	}
}
