<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>用户新添</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
	<script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>用户新添</h3>

	<table style="width:500px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>用户名</td>
			<td class="left">
				<input type="text" name="username" maxlength="45">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>密码</td>
			<td class="left">
				<input type="password" name="password" maxlength="45">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>确认密码</td>
			<td class="left">
				<input type="password" name="password2" maxlength="45">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>真实姓名</td>
			<td class="left">
				<input type="text" name="realName" maxlength="45">&ensp;
			</td>
		</tr>
		<tr>
			<td>角色级别</td>
			<td class="left">
				<label><input type="radio" name="role" value="guest">guest</label>&ensp;
				<label><input type="radio" name="role" value="user">user</label>&ensp;
				<label><input type="radio" name="role" value="admin">admin</label> &emsp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr height="40">
			<td colspan="2" style="padding-left:30px; font-size:small;">			
				<input type="submit" value="提交" onclick="ajax()">	
			</td>
		</tr>
	</table>

	<div id="msg" class="msg" style="line-height:25px;">${ msg }</div>
	<br>
	<a href="userList">返回用户列表</a>
	
	<%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">	

function ajax() {
	var username 	= $("[name='username']").val();			//根据name获取输入的值
	var password 	= $("[name='password']").val();
	var password2 	= $("[name='password2']").val();
	var realName 	= $("[name='realName']").val();
	
	var role 		= $("[name='role']:checked").val();		//获取被勾选单选按钮的值。复选框的处理方法类似	
	if (role == undefined) {								//如果没有值，即没有勾选了的单选按钮
		setMsg("请选择用户角色");							//提示信息
		return;												//不提交信息给后台
	}

	var url = "userAddDo";									//****目标地址
	var arg = {	"username" 	: username, 	"password" : password, 
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
	
	$("#msg").html(msg);									//显示内容
}
</script>
</html>
