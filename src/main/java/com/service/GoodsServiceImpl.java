package com.service;

import java.io.File;
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
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsDao goodsDao;			//自动注入
	
	@Autowired
	private TypeDao typeDao;
	
	
	@Override
	public boolean queryAll (HttpServletRequest request) {

		List<Goods> goodsList = new ArrayList<Goods>(); 	//记录列表
		String search = ""; 								//搜索的内容
		int countShowed = 0;	 							//（要略过的）之前的记录数		
		int pageShow = 1; 									//当前页码					
		String page = ""; 									//页码链接组	
		String msg = "";
		
		try {
			if (request.getParameter("buttonDelete") != null) { 				//如果单击了删除按钮
				
				String[] goodsIdArray = request.getParameterValues("goodsId"); 	//获取ID列表	
				
				if (goodsIdArray != null) {
					deleteByGoodsIdLot(goodsIdArray, request);					//****删除所选。调用在本类中定义的方法
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
			
			if (request.getParameter("buttonFilter") != null) { 		//如果单击了分类筛选按钮
				String typeName = request.getParameter("typeName"); 	//获取分类下拉列表的值		
				
				if (typeName != null && typeName.equals("") == false) {
					search = typeName; 									//设为搜索内容
				}
			}
			
			int countRow = goodsDao.queryCount(search);	//****获得记录总数
					
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
			
			String mapName = request.getServletPath();				//当前的请求名称
			//System.out.println(mapName);
			
			if (pageShow <= 1) {
				page += "<span style='color:gray;'>首页&ensp;";
				page += "上一页&ensp;</span>";
			} else { 
				page += "<a href='" + mapName + "?pageUrl=1"
						+ "&searchUrl=" + searchUrl + "'>首页</a>&ensp;";
				page += "<a href='" + mapName + "?pageUrl=" + (pageShow - 1) 
						+ "&searchUrl=" + searchUrl + "'>上一页</a>&ensp;";
			}
	
			if (pageShow >= pageCount) {
				page += "<span style='color:gray;'>下一页&ensp;";
				page += "尾页</span>";
			} else { 
				page += "<a href='" + mapName + "?pageUrl=" + (pageShow + 1) 
						+ "&searchUrl=" + searchUrl + "'>下一页</a>&ensp;";
				page += "<a href='" + mapName + "?pageUrl=" + pageCount 
						+ "&searchUrl=" + searchUrl + "'>尾页</a>";
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
			
			goodsList = goodsDao.queryAll(search, countShowed, pageSize);	//****获取当前页的记录列表
			
			if (goodsList == null || goodsList.size() == 0) {
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
			request.setAttribute("goodsList", goodsList);						//****传递对象
		}
		
		return false;
	}
	
	
	@Override
	public boolean queryByGoodsId (HttpServletRequest request) {
		
		Goods goods = null;
		String msg = "";
		
		try {
			String goodsId = request.getParameter("goodsId"); 		//获取地址栏参数goodsId的值 
			
			try { 									 
				Integer.parseInt(goodsId);							//尝试转换为整数，即判断其是否为整数
			} catch (Exception e) {
				msg = "参数goodsId错误！";
				return false;
			}	
			
			goods = goodsDao.queryByGoodsId(goodsId);				//****根据ID获取记录
			
			if (goods == null) {
				msg = "（查无此商品记录）";
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
			
			request.setAttribute("goods", goods);								//****传递对象
		}
		
		return false;
	}


	@Override
	public boolean existByGoodsNo (Goods goods, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String goodsNo = goods.getGoodsNo();
			
			Goods goodsSelect = goodsDao.queryByGoodsNo(goodsNo);				//****根据goodsNo获取记录
			
			if (goodsSelect == null) {
				//msg = "在商品列表中无相同的商品编码！";	
				return false;
			}
			
			//msg = "在商品列表中已存在相同的商品编码！";			
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
	public boolean existByGoodsNoExceptGoodsId (Goods goods, HttpServletRequest request) {
		
		String msg = "";
		
		try {
			String goodsNo = goods.getGoodsNo();
			String goodsId = goods.getGoodsId();			
			
			Goods goodsSelect = goodsDao.queryByGoodsNoExceptGoodsId(goodsNo, goodsId);	//****获取记录
			
			if (goodsSelect == null) {
				//msg = "在其他商品中无相同的商品编码！";
				return false;
			}
			
			//msg = "在其他商品中已存在相同的商品编码！";
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
	public String addGoods (Goods goods, HttpServletRequest request) {
		
		String goodsId = "0";
		String msg = "";
		
		try {
			if (existByGoodsNo(goods, request)) {						//**** 本类中的方法	
				msg += "所输入的商品编码已经存在，请修改后再提交！";
				return goodsId;
			}
			
			//-------------------------------------- 根据typeId获取typeName
			String typeId, typeName;
			
			typeId = goods.getTypeId();

			Type type = typeDao.queryByTypeId(typeId);					//****
			
			if (type == null) {			
				msg += "商品分类对应的记录已不存在，请刷新页面后重试！";
				return goodsId;
			}
			typeName = type.getTypeName();
			goods.setTypeName(typeName);

			
			int result = goodsDao.addGoods(goods);					//****新添记录
			
			if (result == 0) {				
				msg += "新添记录失败！";
				return goodsId;
			}
			
			goodsId = goods.getGoodsId();								//****获取新添记录的ID

			if (goodsId.equals("0")) {
				msg += "新添记录失败！请重试。";
				return goodsId;
			}
			
			//------------------------ 将图片用新的goodsId重命名，更新数据表中字段image的值
			String path = request.getServletContext().getRealPath("\\") + "\\"; 	//当前项目运行时所在的物理目录
			String uploadFolder = "fileUpload\\goodsImage\\";						//存放文件的子目录
			path += uploadFolder; 													//文件存放的目录

			String goodsUUID = request.getParameter("goodsUUID");					//从隐藏域控件中获取值
			
			if (goodsUUID == null || goodsUUID.equals("")) {
				msg += "获取通用唯一标识发生错误。";
				return goodsId;
			}
			
			String imageUUID = (String) request.getSession().getAttribute("image" + goodsUUID);		//从session中获得图片名，含UUID	
			
			if (imageUUID != null && imageUUID.equals("") == false) {
				File fileOld = new File(path, imageUUID); 								//创建File对象
				
				if (fileOld.exists() == false || fileOld.isFile() == false) {			//如果不存在，或者不是文件
					msg += "上传的文件已不存在。";
					return goodsId;
				}
				
				int index = imageUUID.lastIndexOf(".");					//路径中最后一个“.”的位置
				String nameExt = imageUUID.substring(index + 1);		//获取文件的扩展名
				
				String imageNew = goodsId + "." + nameExt;				//用新记录的goodsId生成的图片名			
				File fileNew = new File(path, imageNew); 				//创建File对象
				
				if (fileNew.exists()) {									//如果存在
					if (fileNew.delete() == false) {					//如果删除失败
						msg += "删除旧文件失败。";
						return goodsId;
					}
				}
				
				if (fileOld.renameTo(fileNew) == false) {				//如果图片重命名失败
					msg += "文件重命名失败。";
					return goodsId;
				}
							
				int count = goodsDao.editImage(imageNew, goodsId);	//****更新记录。目的是根据goodsId更新image字段
				
				if (count == 0) {
					msg += "数据表中的商品图片名称更新失败！";
					return goodsId;
				}
			}

			msg += "新添记录成功。";	
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在详情页中		
			return goodsId;												//返回新添记录的ID
		
		} catch (Exception e) {
			msg += "系统发生错误。";	
			e.printStackTrace();
		} finally {
			if (request.getAttribute("msg") != null) {
				msg += request.getAttribute("msg").toString();
			}
			request.setAttribute("msg", msg);
		}
		
		return goodsId;
	}


	@Override
	public boolean editGoods (Goods goods, HttpServletRequest request) {

		String msg = "";
		
		try {	
			String goodsId, goodsNo, goodsName, typeId, typeName, price, stock, timeSale, image, detail;
			
			goodsId		= goods.getGoodsId(); 							//从校验过的goods中获取值
			goodsNo  	= goods.getGoodsNo();
			goodsName  	= goods.getGoodsName();
			typeId  	= goods.getTypeId();
			price  		= goods.getPrice();
			stock  		= goods.getStock();
			timeSale  	= goods.getTimeSale();
			image  		= goods.getImage();
			detail		= goods.getDetail();

			Goods goodsOld = goodsDao.queryByGoodsId(goodsId);			//****	
			
			if (goodsOld == null) {	
				msg += "goodsId号对应的记录已不存在，请刷新页面后重试！";
				return false;
			}

			if (existByGoodsNoExceptGoodsId(goods, request)) {			//**** 本类中的方法
				msg += "所输入的商品编码已经存在，请修改后再提交！";
				return false;
			}

			//-------------------------------------- 根据typeId获取typeName
			Type type = typeDao.queryByTypeId(typeId);					//****
			
			if (type == null) {			
				msg += "商品分类对应的记录已不存在，请刷新页面后重试！";
				return false;
			}
			typeName = type.getTypeName();
			goods.setTypeName(typeName);
			
			
			if (goodsNo.equals(goodsOld.getGoodsNo())) {				//如果输入的值与数据表中的值相同
				goods.setGoodsNo(null);									//将不予更新到数据表
			}
			if (goodsName.equals(goodsOld.getGoodsName())) {
				goods.setGoodsName(null);
			}
			if (typeId.equals(goodsOld.getTypeId())) {
				goods.setTypeId(null);
			}
			if (typeName.equals(goodsOld.getGoodsName())) {
				goods.setTypeName(null);
			}
			if (price.equals(goodsOld.getPrice())) {
				goods.setPrice(null);
			}
			if (stock.equals(goodsOld.getStock())) {
				goods.setStock(null);
			}
			if (timeSale.equals(goodsOld.getTimeSale())) {
				goods.setTimeSale(null);
			}
			if (image == null || image.equals(goodsOld.getImage())) {	//修改商品时，image=null。更新图片时，image有值
				goods.setImage(null);
			}
			if (detail.equals(goodsOld.getDetail())) {
				goods.setDetail(null);
			}			

			int count = goodsDao.editGoods(goods);					//****更新记录
			
			if (count == 0) {
				msg += "商品信息修改失败！请重试。。";
				return false;
			}

			msg += "商品信息修改成功！";
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
	public boolean deleteByGoodsId (HttpServletRequest request) {
		
		String msg = "";
		
		try {	
			String goodsId = request.getParameter("goodsId"); 			//取得地址栏参数id
						
			try { 									 
				Integer.parseInt(goodsId);				
			} catch (Exception e) {
				msg = "参数goodsId错误！";
				return false;
			}
			
			Goods goods = goodsDao.queryByGoodsId(goodsId);			//****	
			
			if (goods == null) {
				msg += "goodsId号对应的记录已不存在，请刷新页面后重试！";
				return false;
			}
			
			int result = goodsDao.deleteByGoodsId(goodsId);					//****删除这条记录
			
			if (result == 0) {
				msg = "删除记录失败！请重试。";
				return false;
			}

			msg += "删除记录成功。";		

			String path = request.getServletContext().getRealPath("\\") + "\\"; //当前项目运行时所在的物理目录
			String uploadFolder = "fileUpload\\goodsImage\\";					//存放文件的子目录
			path += uploadFolder; 	
			
			String image = goods.getImage();
			
			if (image == null || image.trim().equals("")) {						//如果image无效
				return true;
			}
			
			File file = new File(path, image);									//创建File对象
			
			if (file.exists()) {												//如果已存在同名文件，应先删除它
				if (file.delete() == false) {									//如果删除失败
					msg += "将文件" + image + "删除时失败。";
					return false;
				}
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
			
			request.getSession().setAttribute("msg", msg);				//通过session传递消息。消息将显示在列表页中
		}
		
		return false;
	}	
	
	
	public boolean deleteByGoodsIdLot (String[] goodsIdArray, HttpServletRequest request) {		//被本类中的queryAll()方法调用
		
		String msg = "";	
		String path = request.getServletContext().getRealPath("\\") + "\\"; 	//当前项目运行时所在的物理目录
		String uploadFolder = "fileUpload\\goodsImage\\";								//存放文件的子目录
		path += uploadFolder; 	
		
		try {
			Goods goods = null;
			String goodsIdLot = "";
			
			for (int i = 0; i < goodsIdArray.length; i++) {
				try { 									 
					Integer.parseInt(goodsIdArray[i]);							//检测是否为整数
					
					goods = goodsDao.queryByGoodsId(goodsIdArray[i]);			//****	
					
					if (goods == null) {
						//msg += "goodsId号对应的记录已不存在，请刷新页面后重试！";
						continue;
					}
					
					goodsIdLot += "," + goodsIdArray[i];
					
					String image = goods.getImage();
					
					if (image == null || image.trim().equals("")) {				//如果image无效
						continue;
					}
					
					File file = new File(path, image);							//创建File对象
					
					if (file.exists()) {										//如果已存在同名文件，应先删除它
						if (file.delete() == false) {							//如果删除失败
							continue;
						}
					}
					
				} catch (Exception e) {
					continue;													//略过此项
				}
			}
			
			if (goodsIdLot.isEmpty()) {
				return false;
			}
			goodsIdLot = goodsIdLot.substring(1);								//去除最前面的逗号
			
			int count = goodsDao.deleteByGoodsIdLot(goodsIdLot);				//****删除这些记录

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
