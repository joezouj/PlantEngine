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
			$.post(ext.contextPath + "/work/workorder/save.do", $(".form").serialize(), function(data) {
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
		/* $('#lineid').combobox({
			url : ext.contextPath + '/work/line/getSelectList.do?random=' + Math.random(),
			valueField : 'id',
			textField : 'name',
			method:'get',
			width:250,
			panelHeight:'auto'
		});
		
		$('#typeid').combobox({
			url : ext.contextPath + '/work/workstationType/getselectlist.do?random=' + Math.random(),
			valueField:'id', 
			textField:'name',
			method:'get'
		}); */
		
	});

</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th>产品编码</th>
					<td><input name="productno" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				<tr>
					<th>生产订单号</th>
					<td><input name="taskno" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				<tr>
					<th>产品规格</th>
					<td><input  name="productformat" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				<tr>
					<th>交货日期</th>
					<td><input name="eddt" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				
			</table>
		</form>
</body>
</html>
