<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave() {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/user/updateCompany.do", $(".form").serialize(), function(result) {
				if (result == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						//刷新树
						parent.reloadTree();
					});
				} else {
					top.$.messager.alert('提示', "保存失败", 'info');
				}
			});
		}
	}
	
	function dodel() {
		top.$.messager.confirm('提示', '确定删除此公司？', function(r) {
			if (r) {
				$.post(ext.contextPath + "/user/deleteCompany.do", $(".form").serialize(), function(result) {
					if (result == 1) {
						top.$.messager.alert('提示', "删除成功", 'info', function() {
							//刷新树
							parent.reloadTree();
							parent.$("#mainFrame").attr("src",ext.contextPath+"/user/showCompanyAdd.do?pid="+$("#pid").val());
						});
					} else {
						top.$.messager.alert('提示', "删除失败", 'info');
					}
				});
			}
		});
	}
	
	$(function() {
		$('#pname').combotree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
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
		<a href="javascript:void(0)" class="icon-remove" title="删除" onclick="dodel()"></a>
	</div>
	<div class="easyui-panel" title="编辑公司" style="padding:10px;"
		data-options="fit:true,tools:'#tt'">
		<form method="post" class="form">
			<input type="hidden" name="id" value="${company.id}"/>
			<table class="table">
				<tr>
					<th>名称</th>
					<td>
						<input name="name" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="${company.name }" />
					</td>
					<th>上级</th>
					<td>
						<input id="pname" name="pname" class="easyui-combotree" value="${pname}" />
						<input id="pid" name="pid" type="hidden" value="${company.pid}"/>
					</td>
				</tr>
				<tr>
					<th>简称</th>
					<td colspan="3">
						<input name="sname" class="easyui-textbox" value="${company.sname }" />
					</td>
				</tr>
				<tr>
					<th>地址</th>
					<td colspan="3">
						<input name="address" class="easyui-textbox" style="width:400px" value="${company.address }" />
					</td>
				</tr>
				<tr>
					<th>顺序</th>
					<td>
						<input name="morder" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="${company.morder }" />
					</td>
					<th>启用</th>
					<td>
						<select name="active" class="easyui-combobox" data-options="required:true,validType:'isBlank',editable:false,panelHeight:'auto',value:'${company.active}'" >
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
