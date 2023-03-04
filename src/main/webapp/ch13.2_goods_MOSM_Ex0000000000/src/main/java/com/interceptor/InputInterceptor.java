package com.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class InputInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		
		String url = "";
		String path = request.getServletPath();									//当前请求（动作）的路径名
		
		int count = path.length() - path.replace("/", "").length();				//路径中字符“/”的个数
		
		for (int i = 1; i < count; i++) {
			url += "../";
		}
		
		url += "error.jsp";
		
		Map<String, String[]> paramMap = request.getParameterMap();
		
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
        	
        	String 	 key 		= entry.getKey();								//键名，控件名，参数名
        	String[] valueArray = entry.getValue();								//值，返回的是一个String[]
        	
        	if (key.equals("detail") || key.equals("note")) {					//如果是商品详情或分类备注
        		continue;														//忽略
        	}
	          
	      	for(String value : valueArray) {
	      		//System.out.println("输入的值：" + key + "=" + value);
	      			      		
	      		if (value.indexOf(" or ") >= 0) {								//如果包含“ or ”
	    			response.sendRedirect(url);									//重定向到error.jsp页
	    			return false;
	      		}
	      	}
        }
				
		return HandlerInterceptor.super.preHandle(request, response, handler);	//查看是否有其他拦截器
	}
}
