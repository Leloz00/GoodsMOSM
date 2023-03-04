<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>分类详情</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
	<script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>分类详情</h3>

	<c:if test="${ type != null }">
	<table style="width:500px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>分类名称</td>
			<td class="left">${ type.typeName }</td>
		</tr>
		<tr>
			<td>备注</td>
			<td class="left">${ type.note }</td>
		</tr>
		<tr>
			<td>更新时间</td>
			<td class="left">${ type.timeRenew }</td>
		</tr>
		<tr>
			<td colspan="2">
				<a href="typeEdit?typeId=${ type.typeId }">修改</a>&emsp;
				<a href="typeDeleteDo?typeId=${ type.typeId }"
						onclick="return confirm('确定要删除吗？');">删除</a>
			</td>
		</tr>
	</table>
	</c:if>

	<div id="msg" class="msg">${ msg }</div>
	<br>
	<a href="typeList">返回分类列表</a>
	
	<%@ include file="../footer.jsp" %>
  </body>
</html>
