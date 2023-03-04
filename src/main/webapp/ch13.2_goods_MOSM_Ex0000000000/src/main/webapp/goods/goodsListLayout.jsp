<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>商品列表</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
	<link rel="stylesheet" href="css/cssGoodsListLayout.css" type="text/css" />
  </head>
  <body>
	<%@ include file="../header.jsp" %>

	<form action="" method="post">		<!-- 表单提交给对应的goodsListLayout -->
	<div class="divGrid note right" style="margin-top:-25px;">
		&emsp;&emsp;按分类筛选：
		<select name="typeName">
			<option value="">&nbsp;</option>
			<c:forEach var="type" items="${ typeList }">
				<c:if test="${ type.typeName != search }">
					<option value="${ type.typeName }">${ type.typeName }</option>
				</c:if>
				<c:if test="${ type.typeName == search }">
					<option value="${ type.typeName }" selected="selected">${ type.typeName }</option>
				</c:if>
			</c:forEach>
		</select>&ensp;
		<input type="submit" name="buttonFilter" value="筛选">
		&emsp;
		&emsp;&emsp;搜索：
		<input type="text" name="search" value="${ search }" style="width:80px;">
		<input type="submit" name="buttonSearch" value="搜索">
		<span class="note">（在编码、名称、分类中搜索）</span>
	</div>	
	
	<h3>商品列表</h3>
	
  	<c:if test="${ goodsList.size() > 0 }">		<%-- 这里使用了jstl标签 --%>
		<div class="divGrid">		
			<div class="list">
				<ul>
							
	<c:forEach var="goods" items="${ goodsList }" varStatus="status">
		<li>
			 <div class="imgDiv">
			 	<a href="goodsShow?goodsId=${ goods.goodsId }">
				 	<img src="fileUpload/goodsImage/${ goods.image }" class="img"></a>
				 	<!-- //####【新添代码】 -->		<!-- 图片不存在时以默认的图片代替 -->
			 </div>
			 <div class="goodsName">
			 	<a href="goodsShow?goodsId=${ goods.goodsId }">${goods.goodsName }</a>
				<!-- //####【新添代码】：鼠标悬停时显示提示信息 -->
			 </div>
			 
			 <div class="priceDiv">
				 &emsp;&emsp;&emsp;&emsp;
				 <span class="priceCode">￥</span>
				 <span class="price"> ${goods.price }</span>&emsp;				 
				 <a href="#" onclick="alert('已添加到购物车。')"> <span class="cart">立刻购买</span></a>
			 </div>
		</li>
	</c:forEach>		
				
				</ul>	
			</div>
		</div>
		
		<div class="divGrid note right">
			${ page }
		</div>
  	</c:if>
	</form>
		
	<div id="msg" class="msg">${ msg }</div>
	
	<%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">
function checkAll() {
	var checkboxList = document.getElementsByName("goodsId");			//获取复选框列表
	var checkboxAll  = document.getElementsByName("checkboxAll")[0];	//全选复选框
	
	for (var i = 0; i < checkboxList.length; i++) {						//对于列表中的每一个复选框
		checkboxList[i].checked = checkboxAll.checked;					//此复选框的勾选情况与全选复选框一致
	}
}
</script>
</html>
