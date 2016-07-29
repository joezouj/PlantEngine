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
			$.post(ext.contextPath + "/work/workorder/update.do", $(".form").serialize(), function(data) {
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
	
	$(function() {
	});

</script>
</head>
<body>
		<form method="post" class="form">
		<input name="id" type="hidden" value="${workorder.id}"/>
			<table class="table">
				<tr>
					<th>产品序列号</th>
					<td><input name="productuid" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${workorder.productuid}" />
					</td>
				</tr>
				<tr>
					<th>生产订单号</th>
					<td><input name="productionorderno" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${workorder.productionorderno}" />
					</td>
				</tr>
				<tr>
					<th>产品编码</th>
					<td><input name="productno" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${workorder.productno}" />
					</td>
				</tr>
				<tr>
					<th>产品规格</th>
					<td><input id="productname" name="productname" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${workorder.productname}" />
					</td>
				</tr>
				<tr>
					<th>交货日期</th>
					<td><input id="eddt" name="eddt" class="Wdate" value="${workorder.eddt }" 
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
						>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
