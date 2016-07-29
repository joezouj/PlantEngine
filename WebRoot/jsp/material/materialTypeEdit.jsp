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
			$.post(ext.contextPath + "/material/materialtype/update.do", $(".form").serialize(), function(data) {
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
			url : ext.contextPath + '/material/materialtype/getMenusJsonActive.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			required:true,
			onClick : function(node) {
				$("#parentid").val(node.id);				
			},
			onLoadSuccess: function(node,data){//编辑界面初始化填充，解析树节点				
				var pid='${materialType.parentid}';
				var t = $('#pname').combotree('tree');
				var node = t.tree('find', pid);				
				$('#pname').combotree('setValue',node.text);	

			}
		});		
	});
</script>
</head>
<body>
		<form method="post" class="form">
		<input type="hidden" name="id" value="${materialType.id }"/>
			<table class="table">
				<tr>
					<th>物料类型</th>
					<td><input name="typename" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${materialType.typename }" /></td>
				</tr>
				<tr>
					<th>类型代码</th>
					<td><input name="typecode" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${materialType.typecode }" /></td>
				</tr>
				<tr>
					<th>上级物料类型</th>
						<td>
							<select id="pname" name="pname" class="easyui-combotree"  value="${materialType.parentid}" ></select>
							<input id="parentid" name="parentid" type="hidden" value="${materialType.parentid}"/>
						</td>
				</tr>
				<tr>
					<th>状态</th>
					<td>
						<select name="status" class="easyui-combobox" data-options="panelHeight:'auto',editable:false">
							<option value="1" <c:if test="${materialType.status=='1'}">selected</c:if>>启用</option>
							<option value="0" <c:if test="${materialType.status=='0'}">selected</c:if>>禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>
						<input name="remark" class="easyui-textbox" value="${materialType.remark }" data-options="multiline:true" style="width:100%;height:100px"
						 validtype="length[0,250]" invalidMessage="有效长度0-250"/>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
