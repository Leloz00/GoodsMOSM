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

import com.pojo.Type;

@Repository							//持久层
@Mapper								//注解为映射
public interface TypeDao {
	
	@Select("select typeId, typeName from tb_type order by typeName")	
	public List<Type> selectAllForGOODS ();			
	
	
	@Select("select * from tb_type where typeId = #{ typeId }")	
	public Type selectByTypeId (String typeId);
	


	
}
