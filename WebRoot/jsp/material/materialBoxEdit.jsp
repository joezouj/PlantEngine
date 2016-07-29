<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,grid) {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/material/materialbox/update.do", $(".form").serialize(), function(data) {
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
		<input type="hidden" name="id" value="${materialBox.id }"/>
			<table class="table">
				<tr>
					<th>编号</th>
					<td><input name="boxnumber" class="easyui-textbox" value="${materialBox.boxnumber }" data-options="required:true,validType:'isBlank'"/></td>
				</tr>
				<tr>
					<th>名称</th>
					<td><input name="name" class="easyui-textbox" value="${materialBox.name }" /></td>
				</tr>
				<tr>
					<th>状态</th>
					<td>
						<select name="status" class="easyui-combobox" data-options="required:true,validType:'isBlank',panelHeight:'auto',editable:false,value:'${materialBox.status}'">
							<option value="1" >可用</option>
							<option value="0">不可用</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>
						<input name="remark" class="easyui-textbox" value="${materialBox.remark }" data-options="multiline:true" style="width:100%;height:100px"
						 validtype="length[0,250]" invalidMessage="有效长度0-250" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
