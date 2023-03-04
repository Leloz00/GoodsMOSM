<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>商品列表</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
	<style type="text/css">
		.goodsName {
			display:		inline-block; 		/*行内块级元素*/
			width:			350px; 
			vertical-align:	middle;				/*上下居中对齐*/
			/* //####【新添代码】 */				/*不换行*/
			/* //####【新添代码】 */				/*隐藏超出部分*/
			/* //####【新添代码】 */				/*不显示部分以三个点表示*/
		}
	</style>
  </head>
  <body>
	<%@ include file="../header.jsp" %>

	<form action="" method="post">		<!-- 表单提交给对应的goodsList -->
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
		<table style="width:900px; margin:0 auto;" class="table_border table_border_bg table_hover">
			<tr class="tr_header">
				<td style="width:46px;">序号</td>
				<td>名称</td>
				<td>编码</td>
				<td>价格</td>
				<td style="width:95px;">详情/修改</td>
				<td style="width:46px;">选择</td>
			</tr>
			
			<c:forEach var="goods" items="${ goodsList }" varStatus="status">
				<tr title="【库存】${ goods.stock }，&emsp;【起售时间】${ goods.timeSale }，&emsp;【分类】${ goods.typeName }">
					<td class="note">${status.index + countShowed + 1 }</td>
					<td style="font-weight:bold; text-align:left;">		<!-- 如果图片加载失败则不显示 -->
						<a href="goodsShow?goodsId=${ goods.goodsId }" style="text-decoration:none;">
							<img src="fileUpload/goodsImage/${ goods.image }">&ensp;	
							<!-- //####【新添代码】 -->
							
							<span title="${ goods.goodsName }" class="goodsName">${ goods.goodsName }</span></a></td>
					<td>${ goods.goodsNo }</td>
					<td>${ goods.price }</td>
					<td>
						<a href="goodsShow?goodsId=${ goods.goodsId }" title="详情">
								<img src="image/icon_show.gif" border="0" /></a>&emsp;
						<a href="goodsEdit?goodsId=${ goods.goodsId }" title="修改">
								<img src="image/icon_edit.gif" border="0" /></a>
					</td>
					<td>
						<input type="checkbox" name="goodsId" value="${ goods.goodsId }">
					</td>
				</tr>
			</c:forEach>
			  
			<tr>
				<td colspan="8" class="note" style="text-align:right; height:50px;">
					（<a href="goodsAdd">新添商品</a>）
					&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
					<input type="submit" name="buttonDelete" value="删除" 
							onclick="return confirm('确认删除所选记录？');">
					&emsp;
					<label>全选:<input type="checkbox" name="checkboxAll" onchange="checkAll()"></label>&emsp;
				</td>
			</tr>
		</table>
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
