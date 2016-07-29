<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function initcombobox(){
		var roleids = "";
		<c:forEach var="role" items="${user.roles}">
			roleids+="${role.name},";
		</c:forEach>
		if(roleids != ""){
			roleids=roleids.substring(0,roleids.length-1);
		}
		$('#role').text(roleids);
		
		var jobids = "";
		<c:forEach var="job" items="${user.jobs}">
			jobids+="${job.name},";
		</c:forEach>
		if(jobids != ""){
			jobids=jobids.substring(0,jobids.length-1);
		}
		$('#job').text(jobids);
	}
	
	$(function() {
		 initcombobox();
		
	});
</script>
</head>
<body>
		<form method="post" class="form">
		<input type="hidden" name="id" value="${user.id }"/>
			<table class="table">
				<tr>
					<th>名称</th>
					<td>${user.name}</td>
				</tr>
				<tr>
					<th>姓名</th>
					<td>${user.caption}</td>
				</tr>
				<tr>
					<th>工号</th>
					<td>${user.serial}</td>
				</tr>
				<tr>
					<th>卡号</th>
					<td>${user.cardid}</td>
				</tr>
				<tr>
					<th>性别</th>
					<td>
						<c:if test="${user.sex=='1'}">男</c:if>
						<c:if test="${user.sex=='0'}">女</c:if>
					</td>
				</tr>
				<tr>
					<th>部门</th>
					<td>
						${user._pname}
					</td>
				</tr>
				<tr>
					<th>职位</th>
					<td>
						<span id="job"></span>
					</td>
				</tr>
				<tr>
					<th>权限</th>
					<td>
						<span id="role"></span>
					</td>
				</tr>
				<tr>
					<th>用户状态</th>
					<td>
						<c:if test="${user.sex=='1'}">启用</c:if>
						<c:if test="${user.sex=='0'}">禁用</c:if>
						<c:if test="${user.sex=='3'}">离职</c:if>
						<c:if test="${user.sex=='2'}">退休</c:if>
					</td>
				</tr>
				<tr>
					<th>顺序</th>
					<td>${user.morder}</td>
				</tr>
			</table>
		</form>
</body>
</html>
