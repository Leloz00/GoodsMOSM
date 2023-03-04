<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
  <head>
	<title>图片上传</title>
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-control" content="no-cache">	<!-- 设置不缓存页面，有助于刷新图片 -->
	<meta http-equiv="Cache" content="no-cache">
	<style type="text/css">
		.imgText {
			display:	flex; 			/*弹性布局*/
			align-items:center;			/*上下居中对齐*/
			height:		100px; 
			margin:		0 0 10px 0; 
			padding:	0; 
			line-height:30px; 
			font-size:	small;
		}
		.form {
			margin:		0; 
			padding:	0;
		}
	</style>
  </head>
  
  <body>
	<div class="imgText">${ msg }</div>
	
	<form action="${ action }" enctype="multipart/form-data" method="post" class="form">
		<input type="file" name="image" style="width:250px; border:1px solid #ddd;">
		<input type="submit" value="提交图片">
		<span style="color:gray; font-size:small;">
			&emsp;（图片支持jpg,jpeg,png,gif,bmp类型，大小不超过2M）
		</span>
	</form>
  </body>
</html>
