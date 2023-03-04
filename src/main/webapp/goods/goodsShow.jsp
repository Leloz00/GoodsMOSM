<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>商品详情</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>商品详情</h3>

	<c:if test="${ goods != null }">
	<table style="width:900px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>商品名称</td>
			<td class="left" style="font-weight:bold;">${ goods.goodsName }</td>
		</tr>
		<tr>
			<td>商品编码</td>
			<td class="left">${ goods.goodsNo }</td>
		</tr>
		<tr>
			<td>分类</td>
			<td class="left">${ goods.typeName }</td>
		</tr>
		<tr>
			<td>价格</td>
			<td class="left">${ goods.price }</td>
		</tr>
		<tr>
			<td>库存</td>
			<td class="left">${ goods.stock }</td>
		</tr>
		<tr>
			<td>起售时间</td>
			<td class="left">${ goods.timeSale }</td>
		</tr>
		<tr>
			<td>商品图片</td>
			<td class="left">
				<div style="height:100px; margin:0; padding:0; display:flex; align-items:center;">	<!-- 内容上下居中对齐 -->
					${ goods.image }
					<c:if test="${ myUser.role == 'admin' || myUser.role == 'user' }">
						<div style='margin-top:60px; font-size:small;'>
							&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
							<c:if test="${ goods.image == '（无图片）' }">
								（<a href="goodsShowImageRenew?goodsId=${ goods.goodsId }">添加图片</a>）
							</c:if>
							<c:if test="${ goods.image != '（无图片）' }">
								（<a href="goodsShowImageRenew?goodsId=${ goods.goodsId }">更换图片</a>）
							</c:if>
						</div>
					</c:if>
				</div>
			</td>
		</tr>
		<tr>
			<td>商品简介</td>
			<td class="left" style="width:650px;">${ goods.detail }</td>
		</tr>
		<tr>
			<td>更新时间</td>
			<td class="left">${ goods.timeRenew }</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:if test="${ myUser.role == 'admin' || myUser.role == 'user' }">
					<a href="goodsEdit?goodsId=${ goods.goodsId }">修改</a>&emsp;
					<a href="goodsDeleteDo?goodsId=${ goods.goodsId }"
							onclick="return confirm('确定要删除吗？');">删除</a>
				</c:if>
				<c:if test="${ myUser.role != 'admin' && myUser.role != 'user' }">
					<a href="#" onclick="alert('已添加到购物车。')">立刻购买</a>&ensp;
				</c:if>
			</td>
		</tr>
	</table>
  	</c:if>

	<div id="msg" class="msg">${ msg }</div>
	<br>
	<c:if test="${ myUser.role == 'admin' || myUser.role == 'user' }">
		<a href="goodsList">返回商品列表</a>
	</c:if>
	<c:if test="${ myUser.role != 'admin' && myUser.role != 'user' }">
		<a href="goodsListLayout">返回商品列表</a>
	</c:if>
	
	<%@ include file="../footer.jsp" %>
  </body>
</html>
