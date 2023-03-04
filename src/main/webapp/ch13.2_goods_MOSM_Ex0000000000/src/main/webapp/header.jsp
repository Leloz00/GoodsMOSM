<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- //####【新添代码】：引入JSTL标签库 -->

<link rel="stylesheet" href="css/cssHeaderFooter.css" type="text/css" />
	
<div class="header">
	<div style="width:900px; margin:0 auto;">
		<div class="headerTitle">
			<img src="image/logoGoods1.png" style="width:245px; height:62px;">
			<img src="image/logoGoods2.png" style="width:206px; height:35px;">  	
			<!-- 上网搜索并访问“在线生成艺术字”网站，将生成的艺术字图片另存到本地后，
				  再复制到Word，然后在Word中裁剪、缩放图片，最后另存为网页（*.htm;*.html），
				  在保存该网页的文件夹的子文件夹*.files中，能看到所需的艺术字图片 -->
		</div>

		<!-- //####【新添代码】 -->		<!-- 未登录 -->
			<div class="headerUsername">
				<br><br><br>&emsp;&emsp;
				<a href='goodsListLayout'>商品列表</a>&emsp;&emsp;
				<a href="login">用户登录</a>&emsp;&emsp;
				<a href="register">用户注册</a>
			</div>
		<!-- //####【新添代码】 -->
		
		<!-- //####【新添代码】 -->		<!-- 无管理权限的注册用户，角色为guest -->
			<div class="headerUsername">
				<br><br><br>
				用户：<span style="color:#800;">${ myUser.username }</span>&emsp;&emsp;
				<a href='goodsListLayout'>商品列表</a>&emsp;
				购物车&emsp;
				订单&emsp;
				<a href="main">用户功能</a>&emsp;
				<a href="logout">退出</a>
			</div>
		<!-- //####【新添代码】 -->

		<!-- //####【新添代码】 -->		<!-- 有管理权限，角色role为“admin”或“user” -->
			<div class="headerUsername">
				<br><br><br>
				用户：<span style="color:#800;">${ myUser.username }</span>&emsp;&emsp;
				管理：&ensp;<a href='goodsList'>商品</a>&emsp;
				<a href='typeList'>分类</a>&emsp;
				<a href="main">用户功能</a>&emsp;
				<a href="logout">退出</a>
			</div>
		<!-- //####【新添代码】 -->
	</div>
</div>
    