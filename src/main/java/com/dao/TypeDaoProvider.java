package com.dao;

import org.apache.ibatis.jdbc.SQL;

import com.pojo.Type;

//@Repository		注意：此处不能应用@Repository注解。因为这里由MyBatis应用了AOP动态代理技术
public class TypeDaoProvider {
	
	public String queryCount (String search) {
		
		String sql = new SQL() {{
			SELECT("count(*)");
			FROM("tb_type");
			
			if (search != null && search.trim().equals("") == false) {			//如果有搜索内容
				WHERE("typeName like CONCAT('%', #{ search }, '%')");
			}
		}}.toString();
			
		return sql;
	}
	
	public String queryAll (String search, int countShowed, int pageSize) { 	//带3个参数
		
		String sql = new SQL() {{
			SELECT("*");
			FROM("tb_type");
			
			if (search != null && search.trim().equals("") == false) {			//如果有搜索内容
				WHERE("typeName like CONCAT('%', #{ search }, '%')");
			}

			ORDER_BY("typeName");
			LIMIT("#{ countShowed }, #{ pageSize }");
		}}.toString();
			
		return sql;
	}
	
	public String editType (Type type) {
		
		String typeName, note;
		
		typeName  	= type.getTypeName();
		note  		= type.getNote();
		
		String sql = new SQL() {{									//匿名类方式构造
				UPDATE("tb_type");
				
				if (typeName != null) {
					SET("typeName = #{ typeName }");
				}
				if (note != null) {
					SET("note = #{ note }");
				}
				
				SET("timeRenew = (now())");
				WHERE("typeId = #{ typeId }");
		}}.toString();
			
		return sql;
	}
}
