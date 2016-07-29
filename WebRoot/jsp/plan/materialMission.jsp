<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$("#mainFrame").attr("src",ext.contextPath+"/plan/materialdeliver/add.do");
	});
</script>
</head>

<body>
	<div class="easyui-layout" data-options="fit:true">
			<iframe id="mainFrame" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" frameBorder="0" scrolling="auto"></iframe>
	</div>
</body>
</html>
