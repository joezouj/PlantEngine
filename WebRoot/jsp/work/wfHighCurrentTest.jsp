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
			$.post(ext.contextPath + "/work/workOrderExcute/excuteHighCurrentTest.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "处理完成", 'info', function() {
						grid.datagrid('reload');
						dialog.dialog('destroy');
					});
				}else if(data.res == 0){
					top.$.messager.alert('提示', "处理失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	}
	
</script>
</head>
<body>
	<div id="tt">
		<a href="javascript:void(0)" class="icon-save" title="保存" onclick="dosave()"></a>
	</div>
	<div class="easyui-panel" title="任务" style="padding:10px;"
		data-options="tools:'#tt'">
		<form method="post" class="form">
		<input name="id" type="hidden" value="${workOrderExcute.id}"/>
		<input name="wftaskid" type="hidden" value="${workOrderExcute.wftaskid}"/>
			<table class="table">
				<tr>
					<th>测试结果</th>
					<td>
						<select class="easyui-combobox" name="pass" style="width:100px;">
       					<option value="1">通过</option>
       					<option value="0">不通过</option>
       					</select>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>
						<input name="remark" class="easyui-textbox" value="" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
