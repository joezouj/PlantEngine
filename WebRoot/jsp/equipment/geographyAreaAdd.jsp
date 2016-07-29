<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,grid) {		
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/equipment/saveGeographyArea.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
						top.$.messager.alert('提示', "保存成功", 'info', function() {
								//刷新列表
								grid.datagrid('reload');
								dialog.dialog('destroy');
							});				
					
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	}

</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
								
				<tr>
					<th >位置名称</th>
					<td ><input id="name" name="name" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;" value="" /></td>				
				</tr>				
				<tr>
					<th >管理状态</th>
					<td ><select id="status" name="status" class="easyui-combobox" data-options="panelHeight:'auto'">
							<option value="启用">启用</option>
							<option value="禁用">禁用</option>						
						</select></td>				
				</tr>
				<tr>
					<th >备注</th>
					<td ><input id="remark" name="remark" class="easyui-textbox"	data-options="multiline:true" style="overflow:auto;width:100%;height:80px;" value=""  validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>				
				</tr>	
				
			</table>
		
	</form>
</body>
</html>
