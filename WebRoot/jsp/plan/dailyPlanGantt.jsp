<!DOCTYPE html>
<%String contextPath = request.getContextPath();%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html lang="en">
	<head>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge;chrome=1">
		
		<link rel="stylesheet" href="<%=contextPath%>/JS/jquery-gantt/css/style.css" />
		
		<script src="<%=contextPath%>/JS/jquery-gantt/js/jquery.min.js"></script>
		<script src="<%=contextPath%>/JS/jquery-gantt/js/jquery.fn.gantt.js"></script>
		<script src="<%=contextPath%>/JS/jquery-gantt/js/jquery.cookie.js"></script>
		
	</head>
	<script type="text/javascript">
       	
        $(document).ready(function(){
     	  	gantt();
        });
        
        function gantt(){
		        $(".gantt").gantt({
		            source:${result},
		            months: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"], //月份显示的语言
		            dow: ["日", "一", "二", "三", "四", "五", "六"], //星期显示的语言
		            scale: "days",   //默认显示的粒度
		            maxScale: "weeks",   //最大显示的粒度
		            minScale: "days", //最小显示的粒度
		            itemsPerPage: 20,   //每页显示的数目
		            onRender:resize
		        });
        }
        
        function resize(){
        	var main = $(window.parent.document).find("#iframe1");
	        var thisheight = $(document).height()+30;
	        main.height(thisheight);
        }
	</script>
	<body>
		<div class="gantt"></div>
	</body>
</html>
