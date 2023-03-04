package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import com.pojo.User;

//####【新添代码】							//持久层
//####【新添代码】							//注解为映射
public interface UserDao {
	
	//####【新添代码】		
	public User selectByUsernameAndPassword (				//登录用
			String username, 
			String password);	//####【新添代码】				//带2个参数
	
	
	
		
}
