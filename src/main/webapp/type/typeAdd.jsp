<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh">
  <head>
	<title>新添分类名称</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
	<script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>新添分类名称</h3>

	<table style="width:500px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>分类名称</td>
			<td class="left">
				<input type="text" name="typeName" maxlength="45">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td class="left">
				<textarea name="note" rows="5" cols="30"></textarea>&ensp;
			</td>
		</tr>
		<tr height="40">
			<td colspan="2" style="padding-left:90px; font-size:small;">			
				<input type="submit" value="提交" onclick="ajax()">	
			</td>
		</tr>
	</table>

	<div id="msg" class="msg" style="line-height:25px;">${ msg }</div>
	<br>
	<a href="typeList">返回分类列表</a>
	
	<%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">	

function ajax() {
	var typeName 	= $("[name='typeName']").val();			//根据name获取输入的值
	var note 		= $("[name='note']").val();

	var url = "typeAddDo";									//****目标地址
	var arg = {	"typeName" 	: typeName, 	"note" : note };//JSON格式数据： {key:value, k2:v2}

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
