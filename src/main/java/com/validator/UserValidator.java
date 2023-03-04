package com.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.pojo.User;

public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		
		return User.class.equals(arg0);			//只针对User类的对象进行数据校验
	}
	
	@Override
	public void validate(Object object, Errors errors) {

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "realName", 	null, "真实姓名不能为空<br>");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", 		null, "用户角色不能为空<br>");
		
		User user = (User) object;
		
		String userId 		= user.getUserId();					//新添记录时，ID为null，所以这里不能有trim()方法
		String username		= user.getUsername();
		String password 	= user.getPassword();
		String password2 	= user.getPassword2();
		String realName 	= user.getRealName().trim();
		String role 		= user.getRole().trim();
		
		user.setRealName(realName);
		
		if (userId != null) {									//编辑用户时才有参数userId
			userId = userId.trim();
			user.setUserId(userId);
			
			try { 									 
				Integer.parseInt(userId);					
			} catch (Exception e) {
				errors.rejectValue("userId", null, "参数userId错误<br>");
			}
		}
		
		if (username != null) {									//新添用户时才有参数username			
			username = username.trim();
			user.setUsername(username);
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", 	null, "用户名称不能为空<br>");
			
			if (username.length() > 45) {
				errors.rejectValue("username", null, "用户名不能多于45个字符<br>");
			}
		}

		if (password == null || password2 == null) {
			errors.rejectValue("password", null, "密码输入错误<br>");			//在修改用户信息时，不修改密码请留空
		}
		if (password.equals(password2) == false) {
			errors.rejectValue("password", null, "2次输入的密码不一致<br>");
		}
		
		if (realName.length() > 45) {
			errors.rejectValue("realName", null, "真实姓名不能多于45个字符<br>");
		}
		
		if (!role.equals("guest") && !role.equals("user") && !role.equals("admin")) {
			errors.rejectValue("role", null, "请选择角色。<br>");
		}
	}
}