<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<style>
	body{ text-align:center}
</style>
</head>
<body>
		<form method="post" class="form">
		<input type="hidden" name="id" value="${materialInfo.id }"/>
			<table class="table">				
				<tr>
					<th>刀具编码</th>
					<td>${cutterInfo.cuttercode }</td>
				</tr>
				<tr>
					<th>刀具名称</th>
					<td>${cutterInfo.cuttername }</td>
				</tr>
				<tr>
					<th>类型</th>
					<td>${cutterInfo._typename }</td>
				</tr>
				<tr>
					<th>位置</th>
					<td>${cutterInfo._positionname }</td>
				</tr>
				<tr>
					<th>使用寿命</th>
					<td>${cutterInfo.life } h</td>
				</tr>
				<tr>
					<th>长度</th>
					<td>${cutterInfo.length } mm</td>
				</tr>
				<tr>
					<th>宽度</th>
					<td>${cutterInfo.width } mm</td>
				</tr>
				<tr>
					<th>厚度</th>
					<td>${cutterInfo.ply } mm</td>
				</tr>
				<tr>
					<th>供应商</th>
					<td>${cutterInfo.producer }</td>
				</tr>
				<tr>
					<th>状态</th>
					<td>
						<c:if test="${cutterInfo.status=='1'}">启用</c:if>
						<c:if test="${cutterInfo.status=='0'}">禁用</c:if>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>${cutterInfo.remark }</td>
				</tr>			
			</table>
		</form>
</body>
</html>
