<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="jsp/inc.jsp"></jsp:include>
<%
		request.getRequestDispatcher("/Login/login.do").forward(request, response);
%>

</head>
<body>
</body>
</html>