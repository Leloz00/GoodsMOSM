<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>用户详情</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>用户详情</h3>

	<c:if test="${ user != null }">
	<table style="width:500px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>用户名</td>
			<td class="left" style="font-weight:bold;">${ user.username }</td>
		</tr>
		<tr>
			<td>真实姓名</td>
			<td class="left">${ user.realName }</td>
		</tr>
		<tr>
			<td>角色级别</td>
			<td class="left">${ user.role }</td>
		</tr>
		<tr>
			<td>更新时间</td>
			<td class="left">${ user.timeRenew }</td>
		</tr>
		<tr>
			<td colspan="2">
				<a href="userEdit?userId=${ user.userId }">修改</a>&emsp;
				<c:if test="${ myUser.role == 'admin' }">
					<a href="userDeleteDo?userId=${ user.userId }"
							onclick="return confirm('确定要删除吗？');">删除</a>
				</c:if>
			</td>
		</tr>
	</table>
	</c:if>

	<div id="msg" class="msg">${ msg }</div>
	<br>
	<c:if test="${ myUser.role == 'admin' }">
		<a href="userList">返回用户列表</a>	
	</c:if>
	<c:if test="${ myUser.role != 'admin' }">
		<a href="main">返回用户功能页</a>	
	</c:if>
	
	<%@ include file="../footer.jsp" %>
  </body>
</html>
