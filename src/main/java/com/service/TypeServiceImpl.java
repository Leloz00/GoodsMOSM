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
	public boolean queryAllForGOODS (HttpServletRequest request) {		//----给TypeAdd.java和TypeEdit.java用。用大写的GOODS，是为了在查找替换时不被更改
		
		List<Type> typeList = new ArrayList<Type>(); 					//记录列表
		String msg = "";
		
		try {			
			typeList = typeDao.queryAllForGOODS();						//****获取所有记录
			
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
			
			request.setAttribute("typeList", typeList);					//****传递记录列表
		}
		
		return false;
	}	
	
	
	@Override	
	public boolean queryByTypeId (HttpServletRequest request) {
		
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
			
			type = typeDao.queryByTypeId(typeId);					//****根据ID获取记录
			
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
			
			if (request.getSession().getAttribute("msg") != null) {				//如果session中有消息。新添或修改记录后保存的消息
				msg = request.getSession().getAttribute("msg").toString() + msg;//读取session中的消息
				request.getSession().removeAttribute("msg");					//从session中移除此键值
				request.setAttribute("msg", msg);								//将消息赋值给request
			}
			
			request.setAttribute("type", type);									//****传递对象
		}
		
		return false;
	}	
	
	//---------------------------------------------------------------------
	
	@Override
	public boolean queryAll (HttpServletRequest request) {

		List<Type> typeList = new ArrayList<Type>(); 		//记录列表
		String search = ""; 								//搜索的内容
		int countShowed = 0;	 							//（要略过的）之前的记录数		
		int pageShow = 1; 									//当前页码					
		String page = ""; 									//页码链接组	
		String msg = "";
		
		try {
			if (request.getParameter("buttonDelete") != null) { 				//如果单击了删除按钮
				
				String[] typeIdArray = request.getParameterValues("typeId"); 	//获取ID数组		
				
				if (typeIdArray != null && typeIdArray.length > 0) {
					deleteByTypeIdLot(typeIdArray, request);					//****删除所选。在本类中定义的方法
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
			
			int countRow = typeDao.queryCount(search);		//****获得记录总数
					
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
				countShowed = (pageShow - 1) * pageSize;						//（要略过的）之前的记录数
			}
			
			typeList = typeDao.queryAll(search, countShowed, pageSize);		//****获取当前页的记录列表
			
			if (typeList == null || typeList.size() == 0) {
				msg = "（查无记录）";
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
			request.setAttribute("typeList", typeList);							//****传递对象
		}
		
		return false;
	}
	
	
	@Override
	public boolean existByTypeName (Type type, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String typeName = type.getTypeName();
			
			Type typeSelect = typeDao.queryByTypeName(typeName);				//****根据typeName获取记录
			
			if (typeSelect == null) {
				return false;
			}
			
			//msg = "分类列表中已存在相同的分类名称！";			
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
	public boolean existByTypeNameExceptTypeId (Type type, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String typeName = type.getTypeName();
			String typeId = type.getTypeId();			
			
			Type typeSelect = typeDao.queryByTypeNameExceptTypeId(typeName, typeId);	//****获取记录
			
			if (typeSelect == null) {
				return false;
			}
			
			//msg = "其他分类已存在相同的分类名称！";
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
	public String addType (Type type, HttpServletRequest request) {
		
		String typeId = "0";
		String msg = "";
		
		try {
			if (existByTypeName(type, request)) {						//**** 本类中的方法	
				msg += "所输入的分类名称已经存在，请修改后再提交！";
				return typeId;
			}
			
			int result = typeDao.addType(type);						//****新添记录
			
			if (result == 0) {				
				msg += "新添记录失败！";
				return typeId;
			}
			
			typeId = type.getTypeId();									//****获取新添记录的ID

			if (typeId.equals("0")) {
				msg += "新添记录失败！请重试。";
				return typeId;
			}

			msg += "新添记录成功。";	
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在详情页中		
			return typeId;												//返回新添记录的ID
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
		
		return typeId;
	}

	
	@Override
	public boolean editType (Type type, HttpServletRequest request) {

		String msg = "";
		
		try {	
			String typeId, typeName, note;
			
			typeId		= type.getTypeId(); 								//从校验过的type中获取值
			typeName  	= type.getTypeName();
			note		= type.getNote();

			Type typeOld = typeDao.queryByTypeId(typeId);					//****	
			
			if (typeOld == null) {	
				msg += "对应的记录已不存在，请刷新页面后重试！";
				return false;
			}

			if (existByTypeNameExceptTypeId(type, request)) {				//**** 本类中的方法
				msg += "所输入的分类名称已经存在，请修改后再提交！";
				return false;
			}			

			if (typeName.equals(typeOld.getTypeName())) {
				type.setTypeName(null);
			}
			if (note.equals(typeOld.getNote())) {
				type.setNote(null);
			}			

			int count = typeDao.editType(type);							//****更新记录
			
			if (count == 0) {
				msg += "分类信息修改失败！请重试。。";
				return false;
			}

			List<Goods> goodsList = goodsDao.queryByTypeId(typeId);
			if (goodsList != null && goodsList.size() > 0) {				//****如果在商品记录中还在使用该分类

				count = goodsDao.editGoodsForTypeNameByTypeId(typeName, typeId);	//****更新商品记录中的分类名称
				
				if (count == 0) {
					msg += "分类信息修改失败！请重试。。";
					return false;
				}
			}

			msg += "分类信息修改成功！";
			request.getSession().setAttribute("msg", msg);					//通过session传递消息。消息将显示在详情页中
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
	public boolean deleteByTypeId (HttpServletRequest request) {
		
		String msg = "";
		
		try {	
			String typeId = request.getParameter("typeId"); 			//取得地址栏参数id
						
			try { 									 
				Integer.parseInt(typeId);				
			} catch (Exception e) {
				msg = "参数typeId错误！";
				return false;
			}

			if (typeDao.queryByTypeId(typeId) == null) {				//****	
				msg = "要删除的记录已不存在，请刷新页面后重试！";
				return false;
			}

			List<Goods> goodsList = goodsDao.queryByTypeId(typeId);
			if (goodsList != null && goodsList.size() > 0) {			//****在删除此分类之前，需先判断在商品记录中是否还在使用它
				msg = "所选的分类在商品列表中已被使用，不许删除此分类！";
				return false;
			}
			
			int result = typeDao.deleteByTypeId(typeId);				//****删除这条记录
			
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
	
	
	public boolean deleteByTypeIdLot (String[] typeIdArray, HttpServletRequest request) {		//被本类中的queryAll()方法调用
		
		String msg = "";
		
		try {
			String typeIdLot = "";
			
			for (int i = 0; i < typeIdArray.length; i++) {
				try { 
					String typeId = typeIdArray[i];														 
					Integer.parseInt(typeId);									//检测是否为整数		

					if (typeDao.queryByTypeId(typeId) == null) {				//****	
						msg = "要删除的某条记录已不存在，请刷新页面后重试！";
						return false;
					}
					
					List<Goods> goodsList = goodsDao.queryByTypeId(typeId);
					if (goodsList != null && goodsList.size() > 0) {			//****在删除某分类之前，需先判断在商品记录中是否还在使用它
						msg = "所选的某个分类在商品列表中已被使用，不许删除此分类！";
						return false;
					}
					
					typeIdLot += "," + typeIdArray[i];
					
				} catch (Exception e) {
					continue;													//略过此项
				}
			}
			
			if (typeIdLot.isEmpty()) {
				return false;
			}
			typeIdLot = typeIdLot.substring(1);								//去除最前面的逗号
			
			int count = typeDao.deleteByTypeIdLot(typeIdLot);				//****删除这些记录

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
}
