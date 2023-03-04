package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.pojo.User;

public class RoleInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {

		String url = "";
		String path = request.getServletPath();									//当前请求（动作）的路径名	
		
		int count = path.length() - path.replace("/", "").length();				//路径中字符“/”的个数
		
		for (int i = 1; i < count; i++) {
			url += "../";
		}
		
		url += "main";
		
		User myUser 	= (User) request.getSession().getAttribute("myUser");
		String myRole 	= myUser.getRole();		
		String myUserId = myUser.getUserId();
		String userId	= request.getParameter("userId");
		
		if (myRole.equals("admin")) {													//如果是管理员
			return HandlerInterceptor.super.preHandle(request, response, handler);		//不拦截。查看是否有其他拦截器
		}
		
		if (myRole.equals("user")) {			//user用户可以访问商品和分类相关的所有页面
			if (path.indexOf("/goods") == 0 || path.indexOf("/type") == 0) {			//以goods和type开头的请求
				return HandlerInterceptor.super.preHandle(request, response, handler);	//不拦截。查看是否有其他拦截器
			}
		}
		
		if (userId != null && userId.equals(myUserId)) {								//如果管理的是自己的信息
			if (path.indexOf("/userShow") == 0 
					|| path.indexOf("/userEdit") == 0
					|| path.indexOf("/userEditDo") == 0) {
				return HandlerInterceptor.super.preHandle(request, response, handler);	//不拦截。查看是否有其他拦截器
			}
		}
		
		System.out.println("因角色被拦截的路径：" + path);
		
		String msg = "您无权限访问该页面。";
		request.setAttribute("msg", msg);
		
		request.getRequestDispatcher(url).forward(request, response);			//转发
		return false;
	}
}
