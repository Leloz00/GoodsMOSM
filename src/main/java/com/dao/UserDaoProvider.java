package com.dao;

import org.apache.ibatis.jdbc.SQL;

import com.pojo.User;

//@Repository		注意：此处不能应用@Repository注解。因为这里由MyBatis应用了AOP动态代理技术
public class UserDaoProvider {
	
	public String queryCount (String search) {
		
		String sql = new SQL() {{
			SELECT("count(*)");
			FROM("tb_user");
			
			if (search != null && search.trim().equals("") == false) {			//如果有搜索内容
				WHERE("CONCAT_WS(',', username, realName, role) "
						+ " like CONCAT('%', #{ search }, '%')");
			}
		}}.toString();
			
		return sql;
	}
	
	public String queryAll (String search, int countShowed, int pageSize) { 	//带3个参数
		
		String sql = new SQL() {{
			SELECT("*");
			FROM("tb_user");
			
			if (search != null && search.trim().equals("") == false) {			//如果有搜索内容
				WHERE("CONCAT_WS(',', username, realName, role) "
						+ " like CONCAT('%', #{ search }, '%')");
			}
			
			ORDER_BY("username");
			LIMIT("#{ countShowed }, #{ pageSize }");
		}}.toString();
			
		return sql;
	}
	
	public String editUser (User user) {
		
		String username, password, realName, role;
		
		username  	= user.getUsername();
		password  	= user.getPassword();
		realName  	= user.getRealName();
		role		= user.getRole();
		
		String sql = new SQL() {{									//匿名类方式构造
				UPDATE("tb_user");
				
				if (username != null) {								//如果输入的值与数据表中的值不同，才更新
					SET("username = #{ username }");
				}
				if (password != null && password.equals("") == false) {		//密码为空则不修改
					SET("password = #{ password }");
				}
				if (realName != null) {
					SET("realName = #{ realName }");
				}
				if (role != null) {
					SET("role = #{ role }");
				}
				
				SET("timeRenew = (now())");
				WHERE("userId = #{ userId }");
		}}.toString();
			
		return sql;
	}
}
