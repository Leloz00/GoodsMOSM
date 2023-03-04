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

	@Autowired
	private UserDao userDao;			//自动注入

	
	@Override
	public boolean queryAll (HttpServletRequest request) {

		List<User> userList = new ArrayList<User>(); 		//记录列表
		String search = ""; 								//搜索的内容
		int countShowed = 0;	 							//（要略过的）之前的记录数		
		int pageShow = 1; 									//当前页码					
		String page = ""; 									//页码链接组	
		String msg = "";
		
		try {
			if (request.getParameter("buttonDelete") != null) { 				//如果单击了删除按钮
				
				String[] userIdArray = request.getParameterValues("userId"); 	//获取ID列表	
				
				if (userIdArray != null) {
					deleteByUserIdLot(userIdArray, request);						//****删除所选。调用在本类中定义的方法
				}
			}	

			String buttonSearch	= request.getParameter("buttonSearch");	//数据查询按钮		
			String buttonPage  	= request.getParameter("buttonPage"); 	//页码提交按钮
			String pageInput  	= "1";									//输入的页码
			
			if (buttonSearch != null) { 								//如果按下了数据查询按钮
				search = request.getParameter("search").trim(); 		//搜索内容
			} else if (buttonPage != null) { 							//如果按下了页码提交按钮
				search 	= request.getParameter("search").trim(); 		//搜索内容
				pageInput	= request.getParameter("pageShow"); 		//页码输入框中的值
			} else { 													//点击了页码链接，或者刚打开此页
				if (request.getParameter("searchUrl") != null) { 
					search = request.getParameter("searchUrl"); 		//不需要进行解码操作，系统会自动解码
				}
				
				if (request.getParameter("pageUrl") != null) {			//地址栏中的页码
					pageInput = request.getParameter("pageUrl");
				}
			}
			
			int countRow = userDao.queryCount(search);		//****获得记录总数
					
			int pageSize  = 6;  							//每页6条记录
			int pageCount = 0; 								//预设总页数为0
		
			if (countRow % pageSize == 0) {					//如果余数为0，即能整除
				pageCount = countRow / pageSize; 			//总页数
			} else {
				pageCount = countRow / pageSize + 1;		//不能整除则加1页。如果除数为小数，将自动去除小数部分得到整数			
			}
			
			try { 									 
				pageShow = Integer.parseInt(pageInput);		//如果是数字，返回字符串对应的整数
			} catch (Exception e) {
				//showPage = 1; 							//如果抛出异常，则取预设值
			}				
		
			if (pageShow < 1) {								//如果当前页码小于1
				pageShow = 1;
			} else if (pageShow > pageCount && pageCount >= 1) { 	//如果当前页码大于总页数，且总页数>=1
				pageShow = pageCount;
			}
			
			String searchUrl = "";
			
			if (search.equals("") == false) {
				searchUrl = URLEncoder.encode(search, "UTF-8");		//进行URL编码，以便在地址栏传递		
			}
			
			if (pageShow <= 1) {
				page += "<span style='color:gray;'>首页&ensp;";
				page += "上一页&ensp;</span>";
			} else { 
				page += "<a href='?pageUrl=1&searchUrl=" + searchUrl + "'>首页</a>&ensp;";
				page += "<a href='?pageUrl=" + (pageShow - 1) + "&searchUrl=" + searchUrl 
						+ "'>上一页</a>&ensp;";
			}
	
			if (pageShow >= pageCount) {
				page += "<span style='color:gray;'>下一页&ensp;";
				page += "尾页</span>";
			} else { 
				page += "<a href='?pageUrl=" + (pageShow + 1) + "&searchUrl=" + searchUrl 
						+ "'>下一页</a>&ensp;";
				page += "<a href='?pageUrl=" + pageCount + "&searchUrl=" + searchUrl + "'>尾页</a>";
			}
			
			page += "&emsp;&emsp;";
			page += "页码：" + pageShow + "/" + pageCount + "&emsp;";
			page += "记录数：" + countRow + "&emsp;&emsp;";	
			
			page += "输入页码:";
			page += "	<input type='text' name='pageShow' value='" + pageShow 
					+ "' style='width:40px; text-align:center;'>";
			page += "	<input type='submit' name='buttonPage' value='提交'>&emsp;";

			if (pageShow > 0) {
				countShowed = (pageShow - 1) * pageSize;					//（要略过的）之前的记录数
			}
			
			userList = userDao.queryAll(search, countShowed, pageSize);	//****获取当前页的记录列表
			
			if (userList == null || userList.size() == 0) {
				msg = "查无记录。";
				return false;
			}

			return true;
		
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
			
			if (request.getSession().getAttribute("msg") != null) {				//如果session中有消息。在详情页删除记录后保存的消息
				msg = request.getSession().getAttribute("msg").toString() + msg;//读取session中的消息
				request.getSession().removeAttribute("msg");					//从session中移除此键值
				request.setAttribute("msg", msg);								//将消息赋值给request
			}

			request.setAttribute("search", search);
			request.setAttribute("countShowed", countShowed);
			request.setAttribute("page", page);
			request.setAttribute("userList", userList);							//****传递对象
		}
		
		return false;
	}
	
	
	@Override
	public boolean queryByUserId (HttpServletRequest request) {
		
		User user = null;
		String msg = "";
		
		try {
			String userId = request.getParameter("userId"); 		//获取地址栏参数id的值 
			
			try { 									 
				Integer.parseInt(userId);							//尝试转换为整数，即判断其是否为整数
			} catch (Exception e) {
				msg = "参数userId错误！";
				return false;
			}	
			
			user = userDao.queryByUserId(userId);					//****根据ID获取记录
			
			if (user == null) {
				msg = "（查无此用户记录）";
				return false;
			}
			
			return true;
		
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
			
			if (request.getSession().getAttribute("msg") != null) {				//如果session中有消息。新添或修改记录后保存的消息
				msg = request.getSession().getAttribute("msg").toString() + msg;//读取session中的消息
				request.getSession().removeAttribute("msg");					//从session中移除此键值
				request.setAttribute("msg", msg);								//将消息赋值给request
			}
			
			request.setAttribute("user", user);									//****传递对象
		}
		
		return false;
	}


	@Override
	public boolean existByUsername (User user, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String username = user.getUsername();
			
			User userSelect = userDao.queryByUsername(username);				//****根据username获取记录
			
			if (userSelect == null) {
				//msg = "在用户列表中无相同的用户编码！";	
				return false;
			}
			
			//msg = "在用户列表中已存在相同的用户编码！";			
			return true;
		
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
		}
		
		return false;
	}


	@Override
	public boolean existByUsernameExceptUserId (User user, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String username = user.getUsername();
			String userId = user.getUserId();			
			
			User userSelect = userDao.queryByUsernameExceptUserId(username, userId);	//****获取记录
			
			if (userSelect == null) {
				//msg = "在其他用户中无相同的用户编码！";
				return false;
			}
			
			//msg = "在其他用户中已存在相同的用户编码！";
			return true;
		
		} catch (Exception e) {
			msg = "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
		}
		
		return false;
	}


	@Override
	public String addUser (User user, HttpServletRequest request) {
		
		String userId = "0";
		String msg = "";
		
		try {
			if (existByUsername(user, request)) {				//**** 本类中的方法	
				msg += "所输入的用户编码已经存在，请修改后再提交！";
				return userId;
			}

			String salt = "login_fdsfj45349fd";					//加点料（盐）。此料应与登录验证LoginCheck.java中加的料相同
			String username = user.getUsername();
			String password = user.getPassword();
			password = Md5.getMd5(username + password + salt);	//对用户名+密码+料进行MD5加密
			user.setPassword(password);
			
			int result = userDao.addUser(user);					//****新添记录

			//System.out.println(2 / 0);						//测试数据回滚时，将此行的注释取消。将抛出异常，以测试是否发生数据回滚
			
			if (result == 0) {				
				msg += "新添用户失败！";
				return userId;
			}
			
			userId = user.getUserId();							//****获取新添记录的ID

			if (userId.equals("0")) {
				msg += "新添用户失败！请重试。";
				return userId;
			}

			msg += "新添用户成功。";	
			request.getSession().setAttribute("msg", msg);		//通过session传递消息。消息将显示在详情页中		
			return userId;										//返回新添记录的ID
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
			//throw new RuntimeException("发生unchecked类型的异常，数据将回滚");		//测试数据回滚时，将此行的注释取消
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
		
		return userId;		//测试数据回滚时，将此行的注释掉
	}


	@Override
	public boolean editUser (User user, HttpServletRequest request) {

		String msg = "";
		
		try {	
			String userId, username, password, realName, role;
			
			userId		= user.getUserId(); 							//从校验过的user中获取值
			password  	= user.getPassword();
			realName  	= user.getRealName();
			role		= user.getRole();

			User userOld = userDao.queryByUserId(userId);				//****	
			
			if (userOld == null) {	
				msg += "userId号对应的记录已不存在，请刷新页面后重试！";
				return false;
			}

			if (existByUsernameExceptUserId(user, request)) {			//**** 本类中的方法
				msg += "所输入的用户编码已经存在，请修改后再提交！";
				return false;
			}			
			
			if (password.length() > 0) {
				username = userOld.getUsername();
			
				String salt = "login_fdsfj45349fd";						//加点料（盐）。此料应与登录验证LoginCheck.java中加的料相同
				password = Md5.getMd5(username + password + salt);		//对用户名+密码+料进行MD5加密
				user.setPassword(password);	
				user.setUsername(null);									//将不予更新到数据表
				
				if (password.equals(userOld.getPassword())) {
					user.setPassword(null);
				} 
			} else {
				user.setPassword(null);
			}
			
			if (realName.equals(userOld.getRealName())) {
				user.setRealName(null);
			}
			
			if (role.equals(userOld.getRole())) {
				user.setRole(null);
			}	

			if (isMe(request, userId)) {								//****调用本类中的方法isMe()。如果是自己
				if (role.equals(userOld.getRole()) == false) {						
					msg = "不许更改自己的角色。";
					return false;
				}
			}

			int count = userDao.editUser(user);							//****更新记录
			
			if (count == 0) {
				msg += "用户信息修改失败！请重试。。";
				return false;
			}

			msg += "用户信息修改成功！";
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在详情页中
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


	@Override
	public boolean deleteByUserId (HttpServletRequest request) {
		
		String msg = "";
		
		try {	
			String userId = request.getParameter("userId"); 			//取得地址栏参数id
						
			try { 									 
				Integer.parseInt(userId);				
			} catch (Exception e) {
				msg = "参数userId错误！";
				return false;
			}

			if (userDao.queryByUserId(userId) == null) {				//****	
				msg = "userId号对应的记录已不存在，请刷新页面后重试！";
				return false;
			}
			
			if (isMe(request, userId)) {								//****调用本类中的方法isMe()。如果是自己
				msg = "不许删除自己。";
				return false;
			}			
			
			int result = userDao.deleteByUserId(userId);						//****删除这条记录
			
			if (result == 0) {
				msg = "删除记录失败！请重试。";
				return false;
			}

			msg += "删除记录成功。";
			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
			
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在列表页中
		}
		
		return false;
	}
	
	
	public boolean deleteByUserIdLot (String[] userIdArray, HttpServletRequest request) {		//被本类中的queryAll()方法调用
		
		String msg = "";
		
		try {
			String userIdLot = "";
			
			for (int i = 0; i < userIdArray.length; i++) {
				try { 			
					String userId = userIdArray[i];
					Integer.parseInt(userId);							//检测是否为整数	

					if (isMe(request, userId)) {						//****调用本类中的方法isMe()。如果是自己
						msg = "不许删除自己。";
						return false;
					}	
					
					userIdLot += "," + userIdArray[i];
					
				} catch (Exception e) {
					continue;											//略过此项
				}
			}
			
			if (userIdLot.isEmpty()) {
				return false;
			}
			userIdLot = userIdLot.substring(1);							//去除最前面的逗号
			
			int count = userDao.deleteByUserIdLot(userIdLot);			//****删除这些记录

			if (count == 0) {
				msg += "删除记录失败！请重试。";
				return false;
			}

			msg = "删除记录成功。";
			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
		}
		
		return false;	
	}	
	
	
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
			
			
			//----------------- 为了在测试时加快用户登录速度，可将如下有关验证码部分的代码注释掉，在登录时就可不用输入验证码了
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
			//----------------- 为了在测试时加快用户登录速度，可将如上有关验证码部分的代码注释掉，在登录时就可不用输入验证码了

			
			String salt = "login_fdsfj45349fd";							//加点料（放点盐）。此料应与登录页login.jsp中加的料相同
			password = Md5.getMd5(username + password + salt);			//****对用户名+密码+料进行MD5加密
			
			User myUser = userDao.queryByUsernameAndPassword(username, password);		//****
			
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
