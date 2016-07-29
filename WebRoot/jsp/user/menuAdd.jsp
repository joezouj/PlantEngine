<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave() {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/user/saveMenu.do", $(".form").serialize(), function(result) {
				if (result.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						//刷新树
						parent.reloadTree();
						parent.$("#mainFrame").attr("src",ext.contextPath+"/user/showMenuEdit.do?id="+result.id);
					});
				} else {
					top.$.messager.alert('提示', "保存失败", 'info');
				}
			},'json');
		}
	}
	$(function() {
		$('#pname').combotree({
			url : ext.contextPath + '/user/getMenusJson.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			required:true,
			onClick : function(node) {
				$("#pid").val(node.id);
			}
		});
	});
</script>
</head>
<body>
	<div id="tt">
		<a href="javascript:void(0)" class="icon-save" title="保存" onclick="dosave()"></a>
	</div>
	<div class="easyui-panel" title="新增菜单" style="padding:10px;"
		data-options="tools:'#tt'">
		<form method="post" class="form">
			<input type="hidden" name="type" value="menu"/>
			<table class="table">
				<tr>
					<th>名称</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" /></td>
					<th>上级菜单</th>
					<td>
						<input id="pname" name="pname" class="easyui-combotree" value="${pname}" />
						<input id="pid" name="pid" type="hidden" value="${param.pid}"/>
					</td>
				</tr>
				<tr>
					<th>地址</th>
					<td colspan="3"><input name="location" class="easyui-textbox"
						style="width:500px" value="" /></td>
				</tr>
				<tr>
					<th>顺序</th>
					<td><input name="morder" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="0" /></td>
					<th>启用</th>
					<td>
						<select name="active" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,required:true,validType:'isBlank'">
							<option value="启用">启用</option>
							<option value="禁用">禁用</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
