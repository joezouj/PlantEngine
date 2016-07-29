<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	$(function(){
		createBarcode('div128','${materialInfo.materialcode }','B');
	});
</script>
<style>
	body{ text-align:center}
</style>
</head>
<body>
		<form method="post" class="form">
		<input type="hidden" name="id" value="${materialInfo.id }"/>
			<table class="table">				
				
				<tr>
					<th>物料编码</th>
					<td>${materialInfo.materialcode }</td>
				</tr>
				<tr>
					<th>物料名称</th>
					<td>${materialInfo.materialname }</td>
				</tr>
				<tr>
					<th>图号</th>
					<td>${materialInfo.figure.number }</td>
				</tr>
				<tr>
					<th>图名</th>
					<td>${materialInfo.figure.docname}</td>
				</tr>
				<tr>
					<th>规格参数</th>
					<td>${materialInfo.spec }</td>
				</tr>
				<tr>
					<th>单位</th>
					<td>${materialInfo.unit }</td>
				</tr>
				<tr>
					<th>类型</th>
					<td>${materialInfo._typename }</td>
				</tr>
				<tr>
					<th>配送类型</th>
					<td>${materialInfo.delivertype=="0"?"预约":"申领"}</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>${materialInfo.remark }</td>
				</tr>				
			</table>
			<br />		 			
			 <div class="barcode2" id="div128" style="text-align:center;margin-left:250px;margin-top:200px;"></div>
		</form>
</body>
</html>
