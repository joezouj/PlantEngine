<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave() {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/user/saveDept.do", $(".form").serialize(), function(result) {
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
	</div>
	<div class="easyui-panel" title="新增部门" style="padding:10px;"
		data-options="fit:true,tools:'#tt'">
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th>名称</th>
					<td>
						<input name="name" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="" />
					</td>
					<th>上级</th>
					<td>
						<input id="pname" name="pname" class="easyui-combotree" value="${pname}" />
						<input id="pid" name="pid" type="hidden" value="${param.pid}"/>
					</td>
				</tr>
				<tr>
					<th>简称</th>
					<td colspan="3">
						<input name="sname" class="easyui-textbox" value="" />
					</td>
				</tr>
				<tr>
					<th>地址</th>
					<td colspan="3">
						<input name="office" class="easyui-textbox" style="width:400px" value="" />
					</td>
				</tr>
				<tr>
					<th>顺序</th>
					<td colspan="3">
						<input name="morder" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="0" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
