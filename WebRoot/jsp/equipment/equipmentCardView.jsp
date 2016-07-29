<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>

<script type="text/javascript">	
	//二维码转格式	
	function utf16to8(str) {
		var out, i, len, c;
		out = "";
		len = str.length;
		for (i = 0; i < len; i++) {
			c = str.charCodeAt(i);
			if ((c >= 0x0001) && (c <= 0x007F)) {
				out += str.charAt(i);
			} else if (c > 0x07FF) {
				out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
				out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
				out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
			} else {
				out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
				out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
			}
		}
		return out;
	}
</script>
</head>
<body>
	<form method="post" class="form">			
		<table class="table">				
				<tr>
					<th >设备编号</th>
					<td id="cfilenametd">${equipmentcard.equipmentcardid}</td>
					<td rowspan="7" width="150px">
						<div id="qrcodeTable2" style="display:block;align:center; width:150px"></div>
						<script >
							var filename2 = $('#cfilenametd').html();				
							var cidd2 = utf16to8(filename2);				 
							$('#qrcodeTable2').qrcode({
								render : "canvas",
								width : 150, //设置宽度   
								height : 150, //设置高度 								
								typeNumber : -1, //计算模式   
								correctLevel : QRErrorCorrectLevel.H,//纠错等级   
								background : "#ffffff",//背景颜色   
								foreground : "#000000", //前景颜色
								text : cidd2
							});
							 
						</script>
<!-- 					</div> -->
					</td>				
				</tr>				
				<tr>
					<th >设备名称</th>
					<td >${equipmentcard.equipmentname}</td>				
				</tr>
				<tr>
					<th >设备型号</th>
					<td >${equipmentcard.equipmentmodel}</td>				
				</tr>
				<tr>
					<th >设备类型</th>
					<td >						
							<c:if test="${equipmentcard.equipmentclass.id==equipmentcard.equipmentclassid}">	
								${equipmentcard.equipmentclass.name}
							</c:if>											
					</td>				
				</tr>
				<tr>
					<th >存放位置</th>
					<td >						
							<c:if test="${equipmentcard.geographyarea.id==equipmentcard.areaid}">	
								${equipmentcard.geographyarea.name}
							</c:if>												
					</td>				
				</tr>
				<tr>
					<th >管理状态</th>
					<td >${equipmentcard.currentmanageflag}</td>				
				</tr>
				<tr>
					<th >备注</th>
					<td >${equipmentcard.remark}</td>				
				</tr>	
			</table>
		
	</form>
</body>
</html>
