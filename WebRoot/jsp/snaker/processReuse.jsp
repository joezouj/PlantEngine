<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<%String contextPath = request.getContextPath();%>
<html>
	<head>
		<title>流程定义</title>
		<jsp:include page="/jsp/inc.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/snaker/style.css">
		<link type="text/css" href="<%=contextPath%>/JS/jquery-ui-1.8.4.custom/css/smoothness/jquery-ui-1.8.4.custom.css" rel="stylesheet" />
		<script type="text/javascript" src="<%=contextPath%>/JS/jquery-ui-1.8.4.custom/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/JS/jquery-ui-1.8.4.custom/js/jquery-ui-1.8.4.custom.min.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/JS/snaker/table.js"></script>
		<script src="<%=contextPath%>/JS/raphael-min.js" type="text/javascript"></script>
		<script src="<%=contextPath%>/JS/snaker/snaker.designer.js" type="text/javascript"></script>
		<script src="<%=contextPath%>/JS/snaker/snaker.model.js" type="text/javascript"></script>
		<script src="<%=contextPath%>/JS/snaker/snaker.editors.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(function() {
				var json="${process }";
				var model;
				if(json) {
					model=eval("(" + json + ")");
				} else {
					model="";
				}
				$('#snakerflow').snakerflow({
					basePath : ext.contextPath+"/JS/snaker/",
                    ctxPath : ext.contextPath,
					restore : model,          
                    formPath : "forms/",
					tools : {
						save : {
							onclick : function(data) {
								parent.$.messager.confirm('提示', '您确定重用当前工艺设计？', function(r) {
								if(r){
									saveModel(data);
								}
								});
							}
						}
					}
				});
			});
			
			function saveModel(data) {
				$.ajax({
					type:'POST',
					url:ext.contextPath+"/snaker/process/deployXml.do",
					data:"model=" +data,
					async: false,
					globle:false,
					error: function(){
						alert('数据处理错误！');
						return false;
					},
					success: function(data){
						if(data=="true"){
							top.$.messager.alert('提示', "保存成功", 'info',function(){
								parent.$('#designer').dialog('close');
								parent.$('#designer').dialog('destroy');
							});
						}else{
							alert('数据处理错误！');
						}
					}
				});
			}
		</script>				
</head>
<body BACKGROUND='<%=contextPath%>/JS/snaker/images/bg.png'>

<div id="toolbox" style="position: Fixed; top: 10; left: 10; background-color: #fff; width: 100px; cursor: default; padding: 5px;" class="ui-widget-content">
	<div id="toolbox_handle" style="text-align: left;background-color: #acd6ff;" class="ui-widget-header">工具集</div>
	<div class="node" id="save"><img src="<%=contextPath%>/JS/snaker/images/save.gif" />&nbsp;&nbsp;保存</div>
	<div>
	<hr />
	</div>
	<div class="node selectable" id="pointer">
	    <img src="<%=contextPath%>/JS/snaker/images/select16.gif" />&nbsp;&nbsp;选择
	</div>
	<div class="node selectable" id="path">
	    <img src="<%=contextPath%>/JS/snaker/images/16/flow_sequence.png" />&nbsp;&nbsp;路线
	</div>
	<div>
	<hr/>
	</div>
	<div class="node state" id="start" type="start"><img
		src="<%=contextPath%>/JS/snaker/images/16/start_event_empty.png" />&nbsp;&nbsp;开始</div>
	<div class="node state" id="end" type="end"><img
		src="<%=contextPath%>/JS/snaker/images/16/end_event_terminate.png" />&nbsp;&nbsp;结束</div>
	<div class="node state" id="task" type="task"><img
		src="<%=contextPath%>/JS/snaker/images/16/task_empty.png" />&nbsp;&nbsp;工序</div>
<!-- 	<div class="node state" id="task" type="custom"><img -->
<!-- 		src="<%=contextPath%>/JS/snaker/images/16/task_empty.png" />&nbsp;&nbsp;自定义</div> -->
<!-- 	<div class="node state" id="task" type="subprocess"><img -->
<!-- 		src="<%=contextPath%>/JS/snaker/images/16/task_empty.png" />&nbsp;&nbsp;子流程</div> -->
	<div class="node state" id="fork" type="decision"><img
		src="<%=contextPath%>/JS/snaker/images/16/gateway_exclusive.png" />&nbsp;&nbsp;决策点</div>
	<div class="node state" id="fork" type="fork"><img
		src="<%=contextPath%>/JS/snaker/images/16/gateway_parallel_red.png" />&nbsp;&nbsp;分支点</div>
	<div class="node state" id="join" type="join"><img
		src="<%=contextPath%>/JS/snaker/images/16/gateway_parallel_green.png" />&nbsp;&nbsp;汇合点</div>
</div>

<div id="properties" style="position: Fixed; top: 10; right: 10;background-color: #fff; width: 220px; cursor: default; padding: 5px;" class="ui-widget-content">
	<div id="properties_handle" class="ui-widget-header">属性</div>
		<table class="properties_all" cellpadding="0" cellspacing="0" width="100%">
		</table>
	<div>&nbsp;</div>
</div>

<div id="snakerflow"></div>
</body>
</html>
