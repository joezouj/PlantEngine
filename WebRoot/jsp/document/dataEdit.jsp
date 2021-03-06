<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title></title>
<script type="text/javascript" charset="utf-8" src="ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="ueditor.all.js"> </script>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave() {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/document/updateWorkOrder.do", $(".form").serialize(), function(result) {
				if (result == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						//刷新树
						parent.reloadTree();
					});
				}else if(result == 0) {
					top.$.messager.alert('提示', "保存失败", 'info');
				} else{
					top.$.messager.alert('提示', result.res, 'info');
				}
			},'json');
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
	var ue = UE.getEditor('details');
		$('#pname').combotree({
			url : ext.contextPath + '/document/getDataJson.do?random=' + Math.random(),
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
	<div class="easyui-panel" title="编辑工作指令" style="padding:10px;"
		data-options="fit:true,tools:'#tt'">
		<form method="post" class="form">
			<input type="hidden" name="id" value="${data.id}"/>
			<table class="table">
							<tr>
					<th>名称</th>
					<td>
						<input name="docname" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="${data.docname}" />
					</td>
					<th>上级</th>
					<td>
						<input id="pname" name="pname" class="easyui-combotree" value="${pname}" />
						<input id="pid" name="pid" type="hidden" value="${data.pid}"/>
					</td>
				</tr>
				<tr>
					<th>编号</th>
					<td>
						<input name="number" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="${data.number}" />
					</td>
					<th>类型</th>
					<td>
						<input id="type" name="type" class="easyui-combotree" value="工作指令" disabled />
						<input id="doctype" name="doctype" type="hidden" value="A"/>
					</td>
				</tr>
				<tr>
					<th>地址</th>
					<td colspan="3">
						<input name="path" class="easyui-textbox" style="width:400px" value="${data.path}" />
					</td>
				</tr>
				<tr>
					<th>启用</th>
					<td>
						<select name="st" class="easyui-combobox" data-options="required:true,validType:'isBlank',editable:false,panelHeight:'auto',value:'${data.st}'" >
							<option value="1">启用</option>
							<option value="0">禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>内容</th>
					<td colspan="3"><script id="details" type="text/plain" style="width:918px;height:500px;">${data.details}</script></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
