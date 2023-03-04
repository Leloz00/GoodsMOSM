package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import com.pojo.User;

@Repository							//持久层
@Mapper								//注解为映射
public interface UserDao {
	
	@Select("select * from tb_user where username = #{ username } and password = #{ password }")	
	public User queryByUsernameAndPassword (String username, String password);	//登录用
	
	//----------------------------------------------------
	
	@SelectProvider(type = UserDaoProvider.class, method = "queryCount")		//如果search为空字符串时，应用动态SQL将能提高效率
	public int queryCount (String search);
	

	@SelectProvider(type = UserDaoProvider.class, method = "queryAll")			//如果search为空字符串时，应用动态SQL将能提高效率
	public List<User> queryAll (String search, int countShowed, int pageSize);	//带3个参数	
	
	
	@Select("select * from tb_user where userId = #{ userId }")	
	public User queryByUserId (String userId);
	
	
	@Select("select * from tb_user where username = #{ username }")	
	public User queryByUsername (String username);	
	
	
	@Select("select * from tb_user where username = #{ username } and userId != #{ userId }")	
	public User queryByUsernameExceptUserId (String username, String userId);	
	
	
	@Insert("insert into tb_user (username, password, realName, role) "
			+ " values(#{ username }, #{ password }, #{ realName }, #{ role })")
	@Options(useGeneratedKeys = true, keyProperty = "userId")					//将主键自动生成的值，赋值给对象的userId属性
	public int addUser (User user);
	
	
//	不采用此方法，而采用下边的基于注解的动态SQL方法：update()	
//	@Update("update tb_user set username = #{ username }"
//			+ ", password = #{ password }, realName = #{ realName }"
//			+ ", role = #{ role }, timeRenew = (now()) where userId = #{ userId }")
//	public int editUser (User user);
	
	@UpdateProvider(type = UserDaoProvider.class, method = "editUser")
	int editUser (User user);			//采用基于注解的动态SQL
	
	
	@Delete("delete from tb_user where userId = #{ userId }")
	public int deleteByUserId (String userId);	
	
	
	@Delete("delete from tb_user where userId in (${ userIdLot })")		//注意：占位符中的符号是“$”。此种占位符表示直接替换
	public int deleteByUserIdLot (String userIdLot);
}
