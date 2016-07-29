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
			$.post(ext.contextPath + "/work/grouptype/save.do", $(".form").serialize(), function(data) {
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
					<th>班次名称</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				<tr>
					<th>开始时间</th>
					<td><input id="sdt" name="sdt" class="Wdate"  
						value="" onfocus="WdatePicker({dateFmt:'HH:mm'})"
							readonly>
					</td>
				</tr>
				<tr>
					<th>结束时间</th>
					<td><input id="edt" name="edt" class="Wdate" value=""
							onfocus="WdatePicker({dateFmt:'HH:mm'})" 
							readonly>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td><input name="remark" class="easyui-textbox" style="overflow:auto;height:80px;width:100%" value=""
					data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250" />
					</td>
				</tr>
				
			</table>
		</form>
</body>
</html>
