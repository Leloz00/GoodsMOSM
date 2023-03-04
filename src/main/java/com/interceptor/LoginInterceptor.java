package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {

		String url = "";
		String path = request.getServletPath();									//当前请求（动作）的路径名	
		
		int count = path.length() - path.replace("/", "").length();				//路径中字符“/”的个数
		
		for (int i = 1; i < count; i++) {
			url += "../";
		}
		
		url += "login";
		
		if (request.getSession().getAttribute("myUser") == null) {				//如果尚未登录

			String msg = "您尚未登录或登录已失效！请先登录。";
			
			if (path.indexOf("AddDo") > 0 || path.indexOf("EditDo") > 0) {		//在新添执行页、修改执行页，将通过其输入页的Ajax实现跳转到用户登录页
				request.getSession().setAttribute("msg", msg);					//消息存入session，将在登录页显示
				
				msg = "@Redirect:" + url;										//通过其输入页的Ajax实现跳转到用户登录页
				response.getWriter().print(msg);	
				return false;	
			}
			
			if (path.indexOf("goodsImage") > 0) {								//在商品图片上传页和商品图片删除页，给出登录链接							//如果是商品图片上传、删除的框架页
				request.getSession().setAttribute("msg", msg);					//消息存入session，将在登录页显示
				
				msg = "<br><br>&emsp;<span style='font-size:small; color:red;'>" + msg + "</span>";
				msg += "&emsp;<a href='" + url + "' target='_top'>用户登录</a>";	//生成登录链接
				response.setContentType("text/html; charset=UTF-8");			//以页面输出，链接才会有效
				response.getWriter().print(msg);								//输出到页面
				return false;	
			}
			
			request.setAttribute("msg", msg);									//将在登录页显示的消息
			request.getRequestDispatcher(url).forward(request, response);		//转发到登录页
			
			return false;														//中断程序的执行
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);	//查看是否有其他拦截器
	}
}
