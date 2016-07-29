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
			$.post(ext.contextPath + "/work/workstationType/update.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
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
		<input name="id" type="hidden" value="${workstationType.id}"/>
			<table class="table">
				<tr>
					<th>工位类型编号</th>
					<td><input name="serial" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${workstationType.serial}" />
					</td>
				</tr>
				<tr>
					<th>工位类型名称</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${workstationType.name}" />
					</td>
				</tr>
				<tr>
					<th>工位类型描述</th>
					<td>
						<textarea name="intro" class="easyui-textbox" data-options="multiline:true" 
						validtype="length[0,250]" invalidMessage="有效长度0-250"
						style="width:100%;height:100px">${workstationType.intro}</textarea>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
