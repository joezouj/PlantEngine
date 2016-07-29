<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">	
	var showdetailsFun = function() {
		$("#rightFrameWorkstation").attr("src",ext.contextPath+"/process/taskworkstation/showList.do?pid=${pid}&taskname=${taskname}");
		$("#rightFrameMaterial").attr("src",ext.contextPath+"/process/taskmaterial/showList.do?pid=${pid}&taskname=${taskname}");
		$("#rightFrameEquipment").attr("src",ext.contextPath+"/process/taskequipment/showList.do?pid=${pid}&taskname=${taskname}");
	};
	
	$(function(){
		showdetailsFun();
	});
</script>
</head>
	<body>
		<div id="tt" class="easyui-tabs" style="height:540px;">
		    <div title="相关工位">   
		         <iframe id="rightFrameWorkstation" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div> 
		    <div title="相关物料">   
		        <iframe id="rightFrameMaterial" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div>   
		    <div title="相关设备">   
		         <iframe id="rightFrameEquipment" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div> 
		</div>
	</body>
</html>
