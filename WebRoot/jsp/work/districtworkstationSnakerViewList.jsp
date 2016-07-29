<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<%String contextPath = request.getContextPath();%>
<html>
	<head>
		<title>工艺流程</title>
		<jsp:include page="/jsp/inc.jsp"></jsp:include>
		
		<script src="<%=contextPath%>/JS/raphael-min.js" type="text/javascript"></script>
		<script src="<%=contextPath%>/JS/snaker/extends/snaker.designer.select.js" type="text/javascript"></script>
		<script src="<%=contextPath%>/JS/snaker/snaker.model.js" type="text/javascript"></script>
		
		<script type="text/javascript">
			var returnVal = "";//用于传递是否需要刷新页面refresh
		
			$(function() {
				$('#snakerflow').snakerflow($.extend(true,{
						basePath : ext.contextPath+"/JS/snaker/",
						restore : eval("(${process})"),
						title:"${processrealname} - ",
						modalDialogWidth:"1000",
						modalDialogHeight:"600",
						url:ext.contextPath + "/work/worktaskworkstation/districtworkstation.do?taskid=${param.id}"
					},eval("(${state})")
				));
			});
			
		</script>				
</head>

<body>
<div id="snakerflow"></div>
</body>
</html>
