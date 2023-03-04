package com.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.UserDao;
import com.pojo.User;
import com.util.Md5;

@Service
@Transactional							//开启数据库事务，如果进行数据的增、改、删操作时发生非检查异常，数据将回滚
public class UserServiceImpl implements UserService {

	//####【新添代码】				//自动注入

	
		
	
	public boolean isMe (HttpServletRequest request, String userId) {	//被本类中的DeleteUser()和deleteByIdList()方法调用
		
		User myUser = (User) request.getSession().getAttribute("myUser");
		
		if (myUser == null) {
			return false;
		}
		
		String myUserId = myUser.getUserId();
		
		if (myUserId.equals(userId)) {									//如果是用户自己
			return true;
		}
		
		return false;
	}	
	
	
	public boolean loginDo (HttpServletRequest request) {				//登录验证

		String msg = "";
		HttpSession session = request.getSession();	
		
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String code 	= request.getParameter("code");				//输入的验证码
			
			if (username == null || username.trim().equals("")) {
				msg = "请输入用户名！";
				return false;											//即使这里有return，也会在程序结束之前执行finally中的代码
			}
			username = username.trim();
			
			if (password == null || password.equals("")) {
				msg = "请输入密码！";
				return false;
			}
			
			if (code == null || code.trim().equals("")) {				//对输入的验证码进行校验
				msg = "@RefreshCode:请输入验证码！";							//将通过JavaScript代码在浏览器端刷新验证码图片
				return false;
			}
			code = code.trim().toLowerCase();							//转换为小写字母
			
			if (session.getAttribute("code") == null) {					//session中的验证码
				msg = "@RefreshCode:验证码已过期。请重新输入。";
				return false;
			}
			
			String codeSession = session.getAttribute("code").toString();
			codeSession = codeSession.toLowerCase();
			
			if (code.equals(codeSession) == false) {
				msg = "@RefreshCode:您输入的验证码错误。请重新输入。";
				return false;
			}

			String salt = "login_fdsfj45349fd";							//加点料（放点盐）。此料应与登录页login.jsp中加的料相同
			//####【新添代码】			//****对用户名+密码+料进行MD5加密
			
			User myUser = null;		//####【新添代码】	//****
			
			if (myUser == null)	{										//如果未读到记录	
				msg = "登录失败！您输入的用户名或者密码不正确。";
				return false;
			}
			
			//msg = "登录成功！";
			session.setAttribute("myUser", myUser); 					//用户对象user，添加到session
			session.setMaxInactiveInterval(20 * 60);					//有效时长20分钟。在生成验证码时设置的时长是30秒
			return true;

		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
	
		return false;
	}
}
