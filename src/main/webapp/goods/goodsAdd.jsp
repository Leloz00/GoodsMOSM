<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="zh">
  <head>
	<title>商品新添</title>
	<link rel="stylesheet" href="css/css.css" type="text/css" />
	<script type="text/javascript" src="UEditor/third-party/jquery-1.10.2.min.js"></script>
  </head>
  <body>
	<%@ include file="../header.jsp" %>	
    <h3>商品新添</h3>

	<table style="width:900px; margin:0 auto;" class="table_border table_padding10">
		<tr class="tr_header">
			<td>项目</td>
			<td>内容&emsp;&emsp;&emsp;&emsp;</td>
		</tr>
		<tr>
			<td>商品名称</td>
			<td class="left">
				<input type="text" name="goodsName" maxlength="45" style="width:400px;">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>商品编码</td>
			<td class="left">
				<input type="text" name="goodsNo" maxlength="45">&ensp;
				<span class="msg">**</span>
			</td>
		</tr>
		<tr>
			<td>分类</td>
			<td class="left">
				<select name="typeId">
					<option value="">&lt;请选择&gt;</option>
					<c:forEach var="type" items="${ typeList }">
						<option value="${ type.typeId }">${ type.typeName }</option>
					</c:forEach>
				</select>&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>价格</td>
			<td class="left">
				<input type="text" name="price" maxlength="10">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>库存</td>
			<td class="left">
				<input type="text" name="stock" maxlength="5">&ensp;
				<span class="msg">*</span>
			</td>
		</tr>
		<tr>
			<td>起售时间</td>
			<td class="left">
				<input type="text" name="timeSale" value="2099-01-01 08:00:00" maxlength="19">&ensp;
				<span class="msg">*</span>&ensp;&ensp;
				<span class="note">例如：2022-05-26 08:00:00</span>
			</td>
		</tr>
		<tr>
			<td>商品图片</td>
			<td class="left" style="padding:0 3px;">		<!-- 内联框架。对象goods在Servlet类GoodsAdd中生成 -->
				<iframe src="goodsImageUpload?goodsId=${ goods.goodsId }&image=${ goods.image }" 
						style="height:155px; width:760px; border:0;"></iframe>
				<input type="hidden" name="goodsUUID" value="${ goods.goodsId }">		<!-- 临时的ID -->
			</td>
		</tr>
		<tr>
			<td>商品简介</td>
			<td class="left" style="width:750px;">
				<script type="text/plain" id="detail"></script>		<!-- 加载编辑器的容器 -->
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
	<a href="goodsList">返回商品列表</a>
	
	<%@ include file="../footer.jsp" %>
  </body>
<script type="text/javascript">	

function ajax() {
	
	var goodsNo 	= $("[name='goodsNo']").val();					//根据控件的name属性值获取输入的值
	var goodsName 	= $("[name='goodsName']").val();
	var typeId 		= $("[name='typeId']").val();
	var price 		= $("[name='price']").val();
	var stock 		= $("[name='stock']").val();
	var timeSale 	= $("[name='timeSale']").val();
	
	var goodsUUID 	= $("[name='goodsUUID']").val();				//临时的ID
	var detail 		= ue.getContent();								//获取富文本编辑器ue中的输入内容
	
	var url = "goodsAddDo";											//****目标地址
	var arg = {	"goodsNo" : goodsNo, "goodsName" : goodsName, 
			   	"typeId"  : typeId,  "price"     : price, 
			   	"stock"   : stock,   "timeSale"  : timeSale, 
			   	"detail"  : detail,  "goodsUUID" : goodsUUID };		//JSON格式数据： {key:value, k2:v2}

	$.post(url, arg, function(data) {
		setMsg(data);
	});
}
  
function setMsg(msg) {

	var prefix = "@Redirect:";							//前缀
	
	if (msg.indexOf(prefix) == 0) {						//如果msg的前面部分是“@Redirect:”
		var url = msg.substring(prefix.length); 		//截取前缀后面的路径名
		window.location.href = url;						//网页转向
		return;
	}
	
	$("#msg").html(msg);								//显示消息
}
</script>

<script type="text/javascript" src="UEditor/ueditor.config.js"></script>	<!-- 配置文件。可自行修改 -->
<script type="text/javascript" src="UEditor/ueditor.all.min.js"></script>	<!-- 编辑器源码文件 -->
<script type="text/javascript">
	var ue = UE.getEditor("detail", {					//通过id实例化编辑器，得到的对象为ue
		autoHeightEnabled: false,						//高度不自动适应
		initialFrameHeight:300,							//初始化编辑器高度,默认320
		//initialFrameWidth:500,						//初始化编辑器宽度,默认500
	});
</script>
</html>
