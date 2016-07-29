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
		if($("#recvid").val()!=""){
			var processorids = $("#recvid").val();
			if ($(".form").form('validate')) {
				$.post(ext.contextPath + "/plan/deliverprocessor/save.do", {processorids:processorids,pid:'${pid}'}, function(data) {
					if (data.res != "") {
						top.$.messager.alert('提示', data.res, 'info', function() {
							grid.datagrid('reload');
						});
					}else{
						top.$.messager.alert('提示', "操作无效", 'info');
					}
				},'json');
			}
		}else{
			top.$.messager.alert('提示', "请选择配料员", 'info');
		}
	}
</script>
</head>
<body>
		<form method="post" class="form">
			<input id="pid" name="pid" type="hidden" value="${pid }"/>
			<table class="table">
				<tr>
					<th>配料人员</th>
					<td>					 
					<input id="recvname" name="recvname" class="easyui-textbox"	
						data-options="required:true,validType:'isBlank',multiline:true,iconCls:'icon-man',readonly:true" 
						style="overflow:auto;width:100%;height:80px;" value="" />
					<input id="recvid" name="recvid" type="hidden"	/>
					<script type="text/javascript">
						$(function() {
							$("#recvname").textbox('textbox').bind("click", function () { 
								selectUsers("recvname","recvid","${param.iframeId}");
							});
						});
					</script>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
