<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave() {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/user/updateDept.do", $(".form").serialize(), function(result) {
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
		top.$.messager.confirm('提示', '确定删除此部门？', function(r) {
			if (r) {
				$.post(ext.contextPath + "/user/deleteDept.do", $(".form").serialize(), function(result) {
					if (result == 1) {
						top.$.messager.alert('提示', "删除成功", 'info', function() {
							//刷新树
							parent.reloadTree();
							parent.$("#mainFrame").attr("src",ext.contextPath+"/user/showDeptAdd.do?pid="+$("#pid").val());
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
	<div class="easyui-panel" title="编辑部门" style="padding:10px;"
		data-options="fit:true,tools:'#tt'">
		<form method="post" class="form">
			<input type="hidden" name="id" value="${dept.id}"/>
			<table class="table">
				<tr>
					<th>名称</th>
					<td>
						<input name="name" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="${dept.name }" />
					</td>
					<th>上级</th>
					<td>
						<input id="pname" name="pname" class="easyui-combotree" value="${pname}" />
						<input id="pid" name="pid" type="hidden" value="${dept.pid}"/>
					</td>
				</tr>
				<tr>
					<th>简称</th>
					<td colspan="3">
						<input name="sname" class="easyui-textbox" value="${dept.sname }" />
					</td>
				</tr>
				<tr>
					<th>办公室</th>
					<td colspan="3">
						<input name="office" class="easyui-textbox" style="width:400px" value="${dept.office }" />
					</td>
				</tr>
				<tr>
					<th>顺序</th>
					<td colspan="3">
						<input name="morder" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="${dept.morder }" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
