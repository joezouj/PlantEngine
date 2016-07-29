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
			$.post(ext.contextPath + "/process/realDetailsStep/save.do", $(".form").serialize(), function(data) {
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
			<input type="hidden" name="pid" value="${param.pid}"/>
			<table class="table">
				<tr>
					<th>序号</th>
					<td>
						<input name="ord" class="easyui-numberspinner" data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				<tr>
					<th>步骤内容</th>
					<td>
						<input name="content" class="easyui-textbox" style="width:300%;height:100px" 
						 value=""data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250" />

					</td>
				</tr>
			</table>
		</form>
</body>
</html>
