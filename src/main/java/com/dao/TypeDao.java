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

import com.pojo.Type;

@Repository							//持久层
@Mapper								//注解为映射
public interface TypeDao {
	
	@Select("select typeId, typeName from tb_type order by typeName")	
	public List<Type> queryAllForGOODS ();			
	
	
	@Select("select * from tb_type where typeId = #{ typeId }")	
	public Type queryByTypeId (String typeId);
	
	
	//--------------------------------------------------------------------------
	
	@SelectProvider(type = TypeDaoProvider.class, method = "queryCount")		//如果search为空字符串时，应用动态SQL将能提高效率
	public int queryCount (String search);
	

	@SelectProvider(type = TypeDaoProvider.class, method = "queryAll")			//如果search为空字符串时，应用动态SQL将能提高效率
	public List<Type> queryAll (String search, int countShowed, int pageSize);	//带3个参数
	
	
	@Select("select * from tb_type where typeName = #{ typeName }")	
	public Type queryByTypeName (String typeName);		
	
	
	@Select("select * from tb_type where typeName = #{ typeName } and typeId != #{ typeId }")	
	public Type queryByTypeNameExceptTypeId (String typeName, String typeId);	
	
	
	@Insert("insert into tb_type (typeName, note) values(#{ typeName }, #{ note })")
	@Options(useGeneratedKeys = true, keyProperty = "typeId")			//将主键自动生成的值，赋值给对象的typeId属性
	public int addType (Type type);
	
	
//	不采用此方法，而采用下边的基于注解的动态SQL方法：update()	
//	@Update("update tb_type set typeName = #{ typeName }"
//			+ ", note = #{ note }, timeRenew = (now()) where typeId = #{ typeId }")
//	public int editType(Type type);
	
	@UpdateProvider(type = TypeDaoProvider.class, method = "editType")
	int editType (Type type);			//采用基于注解的动态SQL
	
	
	@Delete("delete from tb_type where typeId = #{ typeId }")
	public int deleteByTypeId (String typeId);

	
	@Delete("delete from tb_type where typeId in (${ typeIdLot })")		//注意：占位符中的符号是“$”。此种占位符表示直接替换
	public int deleteByTypeIdLot (String typeIdLot);
}
