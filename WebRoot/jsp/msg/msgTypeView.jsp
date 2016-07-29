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
						<td >${msgType.name}</td>				
				</tr>
				<tr>
						<th >ID</th>
						<td >${msgType.id}</td>				
				</tr>
				<tr>
						<th >类型种类</th>
						<td >${msgType.pid}</td>				
				</tr>
				<tr>
						<th >接收角色</th>
						<td >${msgrolename}	</td>				
				</tr>				
				<tr>
						<th >消息发送用户</th>
						<td >${msgusername}</td>				
				</tr>
				<tr>
						<th >短信发送用户</th>
						<td >${smsusername}</td>										
				</tr>
				<tr>
						<th >信息发送方式</th>
						<td ><c:if test="${msgType.sendway=='msg' }">仅消息</c:if>
						<c:if test="${msgType.sendway=='sms' }">仅短信</c:if>
						<c:if test="${msgType.sendway=='both' }">消息+短信</c:if>
						
						</td>										
				</tr>
				<tr>
						<th >备注</th>
						<td >${ msgType.remark}</td>							
				</tr>	
			</table>
		</form>
</body>
</html>
