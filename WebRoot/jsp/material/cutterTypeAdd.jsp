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
			$.post(ext.contextPath + "/material/cuttertype/save.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						//刷新列表
						grid.treegrid('reload');
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
	$(function() {
		$('#pname').combotree({
			url : ext.contextPath + '/material/cuttertype/getMenusJsonActive.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			required:true,
			onClick : function(node) {
				$("#parentid").val(node.id);				
			}
		});
	});
</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th>刀具类型</th>
					<td><input name="typename" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" /></td>
				</tr>
				<tr>
					<th>上级类型</th>
						<td>
							<select id="pname" name="pname" class="easyui-combotree" value="" ></select>
							<input id="parentid" name="parentid" type="hidden" value=""/>
						</td>
				</tr>
				<tr>
					<th>状态</th>
					<td>
						<select name="status" class="easyui-combobox" data-options="panelHeight:'auto',editable:false">
							<option value="1" selected >启用</option>
							<option value="0">禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>
						<input name="remark" class="easyui-textbox" style="width:100%;height:100px" 
						 value=""data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
