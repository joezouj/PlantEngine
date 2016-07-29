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
			$.post(ext.contextPath + "/process/realDetailsJob/update.do", $(".form").serialize(), function(data) {
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
	
	$(function(){
		$('#unitid').combotree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			required:true,
			method:'get',
			onClick: function(node) {
				$('#jobid').combobox({ 
					url:ext.contextPath + '/user/getJsonJobByUnit.do?unitid='+node.id, 
					valueField:'id', 
					textField:'name'
				});
			},
			onLoadSuccess:function(){
				$('#unitid').combotree("setValue","${realDetailsJob.unitid}");
				
				$('#jobid').combobox({ 
					url:ext.contextPath + "/user/getJsonJobByUnit.do?unitid=${realDetailsJob.unitid}", 
					valueField:'id', 
					textField:'name',
					onLoadSuccess:function(){
						$('#jobid').combobox("setValue","${realDetailsJob.jobid}");
					}
				});
			}
		});
		
		$('#jobid').combobox({ 
			required:true,
			editable:false
		});
	});
</script>
</head>
<body>
		<form method="post" class="form">
			<input type="hidden" name="id" value="${realDetailsJob.id}"/>
			<table class="table">
				<tr>
					<th>车间</th>
					<td>
						<input id="unitid" name="unitid" class="easyui-combotree" value="" />
					</td>
				</tr>
				<tr>
					<th>工种</th>
					<td>
						<input id="jobid" name="jobid" class="easyui-combotree" value="" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
