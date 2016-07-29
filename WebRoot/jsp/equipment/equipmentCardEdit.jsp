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
			$.post(ext.contextPath + "/equipment/updateEquipmentCard.do", $(".form").serialize(), function(data) {
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
		<input id="id" name="id"  type="hidden" value="${ equipmentcard.id}"/>
			<table class="table">
				<tr>
					<th >设备编号</th>
					<td ><input id="equipmentcardid" name="equipmentcardid" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;" value="${ equipmentcard.equipmentcardid}" /></td>				
				</tr>				
				<tr>
					<th >设备名称</th>
					<td ><input id="equipmentname" name="equipmentname" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;" value="${ equipmentcard.equipmentname}" /></td>				
				</tr>
				<tr>
					<th >设备型号</th>
					<td ><input id="equipmentmodel" name="equipmentmodel" class="easyui-textbox"	 style="overflow:auto;" value="${ equipmentcard.equipmentmodel}" /></td>				
				</tr>
				<tr>
					<th >设备类型</th>
					<td ><select id="equipmentclassid" name="equipmentclassid" class="easyui-combobox" data-options="panelHeight:'auto'">
						<c:forEach items="${equipmentClassList}" var="ecObj">
							<option value="${ecObj.id}" <c:if test="${equipmentcard.equipmentclassid==ecObj.id }"> selected</c:if>><c:out value="${ecObj.name}"></c:out></option>
						</c:forEach>					
						</select></td>				
				</tr>
				<tr>
					<th >存放位置</th>
					<td ><select id="areaid" name="areaid" class="easyui-combobox" data-options="panelHeight:'auto'">
						<c:forEach items="${geographyAreaList}" var="gaObj">
							<option value="${gaObj.id}" <c:if test="${equipmentcard.areaid==gaObj.id }"> selected</c:if>><c:out value="${gaObj.name}"></c:out></option>
						</c:forEach>					
						</select></td>				
				</tr>
				<tr>
					<th >管理状态</th>
					<td ><select id="currentmanageflag" name="currentmanageflag" class="easyui-combobox" data-options="panelHeight:'auto'">
							<option value="启用" <c:if test="${equipmentcard.currentmanageflag=='启用' }"> selected</c:if>>启用</option>
							<option value="禁用" <c:if test="${equipmentcard.currentmanageflag=='禁用' }"> selected</c:if>>禁用</option>						
						</select></td>				
				</tr>
				<tr>
					<th >备注</th>
					<td ><input id="remark" name="remark" class="easyui-textbox"	
					data-options="multiline:true" style="overflow:auto;width:100%;height:80px;"
					validtype="length[0,250]" invalidMessage="有效长度0-250"
					value="${ equipmentcard.remark}" /></td>				
				</tr>	
				
			</table>		
	</form>
</body>
</html>
