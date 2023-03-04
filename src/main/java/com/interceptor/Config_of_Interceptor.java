package com.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config_of_Interceptor implements WebMvcConfigurer {

	@Override						//Spring MVC会自动调用的拦截器配置方法
	public void addInterceptors (InterceptorRegistry registry) {
		
        System.out.println("对拦截器进行配置：");
        
        InterceptorRegistration interceptor = null;  
        HandlerInterceptor myInterceptor = null;
        
        //-------------------------- 登录拦截器，检查是否已经登录
        List<String> listAdd = new ArrayList<String>();
        listAdd.add("/main");	 								//被拦截的单个请求
        listAdd.add("/goods*");									//拦截能匹配的请求
        listAdd.add("/type*");
        listAdd.add("/user*");
        
        List<String> listExclude = new ArrayList<String>();
        listExclude.add("/goodsListLayout");					//不拦截的单个请求
        listExclude.add("/goodsShow");     
 
        myInterceptor = new LoginInterceptor();					//权限拦截器对象
        interceptor = registry.addInterceptor(myInterceptor);	//引入拦截器
        interceptor.addPathPatterns(listAdd);					//添加被拦截的请求列表
        interceptor.excludePathPatterns(listExclude);			//添加不被拦截的请求列表
        
        //-------------------------- 角色拦截器，检查是否拥有权限
        listAdd.clear();										//清除所有子元素
        listAdd.add("/goods*");									//拦截能匹配的请求
        listAdd.add("/type*");
        listAdd.add("/user*");
        
        listExclude.clear();
        listExclude.add("/main");  								//不拦截的请求
        listExclude.add("/goodsListLayout");
        listExclude.add("/goodsShow");  

        myInterceptor = new RoleInterceptor();					//权限拦截器对象
        interceptor = registry.addInterceptor(myInterceptor);	//引入拦截器
        interceptor.addPathPatterns(listAdd);					//添加被拦截的请求列表
        interceptor.excludePathPatterns(listExclude);			//添加不被拦截的请求列表
  
        //-------------------------- 输入拦截器
        listAdd.clear();										//清除所有子元素
        listAdd.add("/**");										//被拦截的目录：所有目录
        
        myInterceptor = new InputInterceptor();        			//输入拦截器对象
        interceptor = registry.addInterceptor(myInterceptor);	//引入拦截器
        interceptor.addPathPatterns(listAdd);					//添加被拦截的请求
	}
}
