<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave() {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/process/realDetails/update.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info');
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	}
	
	var showdetailsFun = function() {
		$("#rightFrameStep").attr("src",ext.contextPath+"/process/realDetailsStep/showList.do?pid=${realDetails.id}");
		$("#rightFrameWorkstation").attr("src",ext.contextPath+"/process/realDetailsWorkstation/showList.do?pid=${realDetails.id}");
		$("#rightFrameJob").attr("src",ext.contextPath+"/process/realDetailsJob/showList.do?pid=${realDetails.id}");
		$("#rightFrameMaterial").attr("src",ext.contextPath+"/process/realDetailsMaterial/showList.do?pid=${realDetails.id}");
		$("#rightFrameEquipment").attr("src",ext.contextPath+"/process/realDetailsEquipment/showList.do?pid=${realDetails.id}");
		$("#rightFrameBook").attr("src",ext.contextPath+"/process/realDetailsBook/showList.do?pid=${realDetails.id}");
	};
	
	$(function(){
		showdetailsFun();
	});
</script>
</head>
	<body class="easyui-layout">
	<div data-options="region:'north'" style="overflow: hidden;padding:5px;height:234px">
		<div id="tt">
			<a href="javascript:void(0)" class="icon-save" title="保存" onclick="dosave()"></a>
		</div>
		<div class="easyui-panel" title="产品工序" data-options="tools:'#tt'" style="padding:5px;">
			<form method="post" class="form">
				<input type="hidden" name="id" value="${realDetails.id}"/>
				<input type="hidden" name="pid" value="${realDetails.pid}"/>
				<table class="table">
					<tr>
						<th>产品工艺名称</th>
						<td>
							${realDetails.pname}
						</td>
					</tr>
					<tr>
						<th>工序名称</th>
						<td>
							${realDetails.name}
						</td>
					</tr>
					<tr>
						<th>标准工时</th>
						<td>
							<input name="worktime" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="${realDetails.worktime}" />
						</td>
					</tr>
					<tr>
						<th>描述</th>
						<td>
							<textarea name="description" class="easyui-textbox" data-options="multiline:true" style="height:100px;width:300px">${realDetails.description}</textarea>
						</td>
					</tr>
				</table>
			</form>
		</div>
		</div>
		<div data-options="region:'center'" style="overflow: hidden;padding:5px;">
		<div class="easyui-tabs" style="height:320px">
		    <div title="相关工位">   
		         <iframe id="rightFrameWorkstation" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;margin-bottom:-4px" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div> 
		    <div title="相关工种">   
		         <iframe id="rightFrameJob" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;margin-bottom:-4px" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div>
		    <div title="相关物料">   
		        <iframe id="rightFrameMaterial" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;margin-bottom:-4px" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div>   
		    <div title="相关设备">   
		         <iframe id="rightFrameEquipment" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;margin-bottom:-4px" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div> 
		    <div title="作业指导书">   
		         <iframe id="rightFrameBook" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;margin-bottom:-4px" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div>   
		    <div title="工序步骤">   
		         <iframe id="rightFrameStep" src="" allowTransparency="true" style="border: 0; width: 100%;height: 100%;margin-bottom:-4px" 
					frameBorder="0" scrolling="auto"></iframe>
		    </div> 
		</div>
		</div>
	</body>
</html>
