<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	
</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
								
				<tr>
					<th >类型名称</th>
					<td >${equipmentclass.name}</td>				
				</tr>				
				<tr>
					<th >管理状态</th>
					<td >${equipmentclass.status}</td>				
				</tr>
				<tr>
					<th >备注</th>
					<td >${equipmentclass.remark}</td>				
				</tr>	
				
			</table>
		
	</form>
</body>
</html>
