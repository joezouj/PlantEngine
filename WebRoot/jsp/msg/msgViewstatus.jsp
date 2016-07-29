<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
					<th style="text-align:center">已浏览人</th>
					<td  style="overflow:auto;width:100%;height:80px;">${checker}</td>
				</tr>
				<tr>
					<th style="text-align:center">未浏览人</th>
					<td  style="overflow:auto;width:100%;height:80px;">${notchecker}</td>
				</tr>
			</table>
		</form>
</body>
</html>
