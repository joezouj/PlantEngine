<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
</script>
</head>
<body>
		<form method="post" class="form">
		<input type="hidden" name="id" value="${cutterType.id }"/>
			<table class="table">
				<tr>
					<th>刀具类型</th>
					<td>${cutterType.typename }</td>
				</tr>
				<tr>
					<th>上级类型</th>
						<td>
						${parentType.typename }
						</td>
				</tr>
				<tr>
					<th>状态</th>
					<td>
						<c:if test="${cutterType.status=='1'}">启用</c:if>
						<c:if test="${cutterType.status=='0'}">禁用</c:if>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>${cutterType.remark }</td>
				</tr>
			</table>
		</form>
</body>
</html>
