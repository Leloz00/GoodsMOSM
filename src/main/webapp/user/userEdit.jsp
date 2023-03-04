<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>用户修改</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
	<script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>用户修改</h3>

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
			<td>密码</td>
			<td class="left">
				<input type="password" name="password" maxlength="45">&ensp;
				<span class="msg">*不修改请留空</span>
			</td>
		</tr>
		<tr>
			<td>确认密码</td>
			<td class="left">
				<input type="password" name="password2" maxlength="45">&ensp;
				<span class="msg">*不修改请留空</span>
			</td>
		</tr>
		<tr>
			<td>真实姓名</td>
			<td class="left">
				<input type="text" name="realName" value="${ user.realName }" maxlength="45">&ensp;
			</td>
		</tr>
				<c:if test="${ myUser.role == 'guest' }">				<!-- 当前用户的角色为guest -->
					<input type="hidden" name="role" value="guest">		<!-- 隐藏域 -->
				</c:if>
				<c:if test="${ myUser.role == 'user' }">
					<input type="hidden" name="role" value="user">		
				</c:if>
				<c:if test="${ myUser.role == 'admin' }">
		<tr>
			<td>角色级别</td>
			<td class="left">
				<label><input type="radio" name="role" value="guest" 
					<c:if test="${ user.role == 'guest' }">checked='checked'</c:if> >guest</label>&ensp;
				<label><input type="radio" name="role" value="user" 
					<c:if test="${ user.role == 'user'  }">checked='checked'</c:if> >user</label>&ensp;
				<label><input type="radio" name="role" value="admin"  
					<c:if test="${ user.role == 'admin' }">checked='checked'</c:if> >admin</label>&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
				</c:if>
		<tr>
			<td>上次更新时间</td>
			<td class="left">${ user.timeRenew }</td>
		</tr>
		<tr height="40">
			<td colspan="2" style="padding-left:100px; font-size:small;">			
				<input type="submit" value="提交" onclick="ajax()">	
				&emsp;&emsp;
				<a href="userShow?userId=${ user.userId }">返回用户详情页</a>
			</td>
		</tr>
	</table>
	</c:if>

	<div id="msg" class="msg" style="line-height:25px;">${ msg }</div>
	<br>
	<c:if test="${ user.role == 'admin' }">
		<a href="userList">返回用户列表</a>
	</c:if>
	<c:if test="${ user.role != 'admin' }">
		<a href="main.jsp">返回用户功能页</a>
	</c:if>
	
	<%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">	

function ajax() {
	//var username 	= $("[name='username']").val();			//根据name获取输入的值
	var password 	= $("[name='password']").val();
	var password2 	= $("[name='password2']").val();
	var realName 	= $("[name='realName']").val();

	var role  		= $("[name='role']:checked").val();		//获取被勾选单选按钮的值。复选框的处理方法类似	
	if (role == undefined) {								//如果没有值，即没有勾选了的单选按钮
		role 		= $("[name='role']").val();				//从隐藏域获取值，角色为guest或user的用户
	}
	
	var url = "userEditDo?userId=${ user.userId }";
	var arg = {	"password" 	: password, 
			   	"password2" : password2,  	"realName" : realName, 
			   	"role"   	: role};						//JSON格式数据： {key:value, k2:v2}

	$.post(url, arg, function(data) {
		setMsg(data);
	});
}
  
function setMsg(msg) {

	var prefix = "@Redirect:";								//前缀
	
	if (msg.indexOf(prefix) == 0) {							//如果msg的前面部分是“@Redirect:”
		var url = msg.substring(prefix.length); 			//截取前缀后面的路径名
		window.location.href = url;							//网页转向
		return;
	}
	
	$("#msg").html(msg);									//显示消息
}
</script>
</html>
