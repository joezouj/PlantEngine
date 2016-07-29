<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		showdetailsFun('${materialDeliver.id }');
	});
	var showdetailsFun = function(pid) {
		$("#rightFrameMaterial").attr("src",ext.contextPath+"/plan/deliverdetail/showviewlist.do?pid="+pid);
		$("#rightFrameProcessor").attr("src",ext.contextPath+"/plan/deliverprocessor/showviewlist.do?pid="+pid);
	};
</script>
</head>
<body>
	<div class="easyui-panel" title="配送任务" style="padding:10px;">
		<form method="post" class="form">
			<input id="id" name="id" type="hidden" value="${materialDeliver.id }"/>
			<table class="table">
				<tr>
					<th>计划信息</th>
					<td>${materialDeliver.dailyPlanSummary.name }</td>
					<th>计划日期</th>
					<td>${fn:substring(materialDeliver.dailyPlanSummary.plandt,0,10) }</td>
				</tr>
				<tr>
					<th>所属工位</th>
					<td>${materialDeliver.workstation.name }</td>
					<th>工位编号</th>
					<td>${materialDeliver.workstation.serial }</td>
				</tr>
				<tr>
					<th >配送开始时间</th>
					<td >${fn:substring(materialDeliver.starttime,0,16) }</td>	
					<th >配送截至时间</th>
					<td >${fn:substring(materialDeliver.endtime,0,16) }</td>			
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3"><input id="remark" name="remark" class="easyui-textbox" value="${materialDeliver.remark }" 
						data-options="multiline:true,editable:false" style="width:100%;height:100px"/></td>
				</tr>
			</table>
		</form>
	</div>	
		<br/>
	<div id="tooltab" class="easyui-tabs" style="height:300px;">
		    <div title="配料详情">   
		        <iframe id="rightFrameMaterial" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div>
		    <div title="配料人员">   
		         <iframe id="rightFrameProcessor" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div> 
	</div>
</body>
</html>
