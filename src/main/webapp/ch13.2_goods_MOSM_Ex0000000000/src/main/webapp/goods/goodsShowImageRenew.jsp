<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>商品详情 - 更换图片</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>商品详情 - 更换图片</h3>

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
			<td class="left" style="padding:0 5px;">		<!-- 内联框架 -->
				<!-- //####【新添代码】 -->
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
				<a href="goodsEdit?goodsId=${ goods.goodsId }">修改</a>&emsp;
				<a href="goodsDeleteDo?goodsId=${ goods.goodsId }"
						onclick="return confirm('确定要删除吗？');">删除</a>
			</td>
		</tr>
	</table>
  	</c:if>

	<div id="msg" class="msg">${ msg }</div>
	<br>
	<a href="goodsList">返回商品列表</a>
	
	<%@ include file="../footer.jsp" %>
  </body>
</html>
