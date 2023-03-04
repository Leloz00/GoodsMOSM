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
			
			//####新添代码：在新添执行页、修改执行页，将通过其输入页的Ajax实现跳转到用户登录页
			
			
			//####新添代码：在商品图片上传页和商品图片删除页，给出登录链接	
			
			
			
			msg = "<br><br>&emsp;<span style='font-size:small; color:red;'>" + msg + "</span>";
			msg += "&emsp;<a href='" + url + "' target='_top'>用户登录</a>";			//生成登录链接
			
			
			
			
			request.setAttribute("msg", msg);			
			request.getRequestDispatcher(url).forward(request, response);		//转发到登录页
			return false;			
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);	//查看是否有其他拦截器
	}
}
