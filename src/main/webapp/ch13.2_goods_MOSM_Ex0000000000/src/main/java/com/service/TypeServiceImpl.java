package com.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.GoodsDao;
import com.dao.TypeDao;
import com.pojo.Goods;
import com.pojo.Type;

@Service
@Transactional							//开启数据库事务，如果进行数据的增、改、删操作时发生非检查异常，数据将回滚
public class TypeServiceImpl implements TypeService {

	@Autowired
	private GoodsDao goodsDao;			//自动注入
	
	@Autowired
	private TypeDao typeDao;
	
	
	@Override	
	public boolean selectAllForGOODS (HttpServletRequest request) {		//----给TypeAdd.java和TypeEdit.java用。用大写的GOODS，是为了在查找替换时不被更改
		
		List<Type> typeList = null; 	//####【添加代码】				//记录列表
		String msg = "";
		
		try {			
			typeList = null;	//####【添加代码】				//****获取当前页的记录列表。获取第0条之后的9999条记录，相当于获取所有记录
			
			if (typeList == null || typeList.size() == 0) {
				msg += "查无分类记录。";
				return false;
			}

			return true;
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg = request.getAttribute("msg").toString() + msg;
			}
			request.setAttribute("msg", msg);
			
			//####【添加代码】					//传递记录列表
		}
		
		return false;
	}	
	
	
	@Override	
	public boolean selectByTypeId (HttpServletRequest request) {
		
		Type type = null;
		String msg = "";
		
		try {
			String typeId = request.getParameter("typeId"); 		//获取地址栏参数id的值 
			
			try { 									 
				Integer.parseInt(typeId);							//尝试转换为整数，即判断其是否为整数
			} catch (Exception e) {
				msg = "参数typeId错误！";
				return false;
			}	
			
			type = typeDao.selectByTypeId(typeId);					//****根据ID获取记录
			
			if (type == null) {
				msg = "（查无此分类记录）";
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
			
			if (request.getSession().getAttribute("msg") != null) {				//如果session中有消息。添加或修改记录后保存的消息
				msg = request.getSession().getAttribute("msg").toString() + msg;//读取session中的消息
				request.getSession().removeAttribute("msg");					//从session中移除此键值
				request.setAttribute("msg", msg);								//将消息赋值给request
			}
			
			request.setAttribute("type", type);									//****传递对象
		}
		
		return false;
	}	
	
	//---------------------------------------------------------------------
	
	
}
