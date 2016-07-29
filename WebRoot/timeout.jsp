<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="jsp/inc.jsp"></jsp:include>

<script type="text/javascript" charset="utf-8">
	top.$.messager.alert('提示','登录已超时，请重新登录！','info',function(){
		top.location.href=ext.contextPath; //跳转到登陆页面
	});
</script>

</head>
<body>
</body>
</html>