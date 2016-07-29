<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
</head>
<body>
		<form method="post" class="form">
		<input type="hidden" name="id" value="${materialBox.id }"/>
			<table class="table">
				<tr>
					<th>编号</th>
					<td>${materialBox.boxnumber }</td>
				</tr>
				<tr>
					<th>名称</th>
					<td>${materialBox.name }</td>
				</tr>
				<tr>
					<th>状态</th>
					<td>
						<c:if test="${materialBox.status=='1'}">可用</c:if>
						<c:if test="${materialBox.status=='0'}">不可用</c:if>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>
						${materialBox.remark }
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
