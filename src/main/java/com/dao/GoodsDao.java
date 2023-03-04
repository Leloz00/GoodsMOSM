package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
//import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import com.pojo.Goods;

@Repository							//持久层
@Mapper								//注解为映射
public interface GoodsDao {

	//@Select("select count(*) from tb_goods where CONCAT_WS(',', goodsNo, goodsName, typeName)"
	//		+ " like CONCAT('%', #{ search }, '%')")
	//public int queryCount (String search);
	
	//@Select("select * from tb_goods where CONCAT_WS(',', goodsNo, goodsName, typeName)"
	//		+ " like CONCAT('%', #{ search }, '%')"
	//		+ " order by goodsName"
	//		+ " limit #{ countShowed }, #{ pageSize }")		
	//public List<Goods> queryAll (String search, int countShowed, int pageSize);	//带3个参数	
	
	@SelectProvider(type = GoodsDaoProvider.class, method = "queryCount")			//如果search为空字符串时，应用动态SQL将能提高效率
	public int queryCount (String search);
	

	@SelectProvider(type = GoodsDaoProvider.class, method = "queryAll")			//如果search为空字符串时，应用动态SQL将能提高效率
	public List<Goods> queryAll (String search, int countShowed, int pageSize);	//带3个参数
	
	
	@Select("select * from tb_goods where goodsId = #{ goodsId }")	
	public Goods queryByGoodsId (String goodsId);									//根据goodsId查询
	
	
	@Select("select * from tb_goods where goodsNo = #{ goodsNo }")	
	public Goods queryByGoodsNo (String goodsNo);	
	
	
	@Select("select * from tb_goods where goodsNo = #{ goodsNo } and goodsId != #{ goodsId }")	
	public Goods queryByGoodsNoExceptGoodsId (String goodsNo, String goodsId);	
	
	
	@Insert("insert into tb_goods "
			+ " (goodsNo, goodsName, typeId, typeName, price, stock, timeSale, detail) "
			+ " values(#{ goodsNo }, #{ goodsName }, #{ typeId }, #{ typeName }, "
			+ " #{ price }, #{ stock }, #{ timeSale }, #{ detail })")
	@Options(useGeneratedKeys = true, keyProperty = "goodsId")				//将主键自动生成的值，赋值给对象的userId属性
	public int addGoods (Goods goods);
	
	
	@Update("update tb_goods set image = #{ image }, timeRenew = (now()) where goodsId = #{ goodsId }")
	public int editImage (String image, String goodsId);	
	
	
//	不采用此方法，而采用下边的基于注解的动态SQL方法：update()	
//	@Update("update tb_goods set goodsNo = #{ goodsNo }, goodsName = #{ goodsName }"
//			+ ", typeId = #{ typeId }, typeName = #{ typeName }, price = #{ price }"
//			+ ", stock = #{ stock }, timeSale = #{ timeSale }, image = #{ image }"
//			+ ", detail = #{ detail }, timeRenew = (now()) where goodsId = #{ goodsId }")
//	public int edit(Goods goods);
	
	@UpdateProvider(type = GoodsDaoProvider.class, method = "editGoods")
	int editGoods (Goods goods);			//采用基于注解的动态SQL
	
	
	@Delete("delete from tb_goods where goodsId = #{ goodsId }")
	public int deleteByGoodsId (String goodsId);	
	
	
	@Delete("delete from tb_goods where goodsId in (${ goodsIdLot })")		//注意：占位符中的符号是“$”。此种占位符表示直接替换
	public int deleteByGoodsIdLot (String goodsIdLot);						//批量删除
	
	//------------------------------------------------------------------------
	
	@Select("select * from tb_goods where typeId = #{ typeId }")			//在删除某分类之前，需先判断在商品记录中是否还在使用该分类
	public List<Goods> queryByTypeId (String typeId);						//由于某分类可能对应多个商品，故返回类型用List<Goods>
	
	
	@Delete("update tb_goods set typeName = #{ typeName } where typeId = #{ typeId }")	//更新某分类名称时，商品记录中的该分类名称也要被更新
	public int editGoodsForTypeNameByTypeId (String typeName, String typeId);
}
