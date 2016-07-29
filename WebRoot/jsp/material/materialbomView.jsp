<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%String contextPath = request.getContextPath();%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
 	var grid;
 	$(function() {
 		grid = $('#grid').treegrid({
 			title : '',
 			url : ext.contextPath + '/material/materialbom/getBOMDetailsTree.do?pid=${materialBOM.id}',
 			striped : true,
 			rownumbers: true,
			collapsible:true,
			showFooter:true,
 			idField : 'id',
 			treeField: 'materialcode',
 			columns : [ [
				{width : '200', title : '物料编码', field : 'materialcode', halign:'center',align:'left'},
				{width : '320', title : '物料名称', field : 'materialname', halign:'center',align:'left'},
 				{width : '85', title : '单位数量', field : 'num', halign:'center',align:'center'},
 				{width : '50', title : '单位', field : 'unit', sortable : true, align:'center',halign:'center'},
 				{width : '50', title : '版本', field : 'version', halign:'center',align:'center'}
 			] ]
 		});
 	});
</script>
</head>
<body>
	<div class="easyui-panel" title="BOM信息" style="padding:10px;"
		data-options="tools:'#tt'">
		<form method="post" class="form">
		<input type="hidden" name="id" value="${materialBOM.id }"/>
		<input type="hidden" id="num" name="num" value="${materialBOM.num }" />
			<table class="table">
				<tr>
					<th>物料编码</th>
					<td>${materialBOM.materialcode }</td>
					<th>物料名称</th>
					<td>${materialBOM.materialname }</td>
				</tr>
				<tr>
					<th>序号</th>
					<td>${materialBOM.ordernumber }</td>
					<th>版本</th>
					<td>${materialBOM.version }</td>
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3">${materialBOM.remark }</td>
				</tr>
			</table>
		</form>
	</div>
	<br/>
	<div class="easyui-panel" title="物料清单">
		<div>
			<table id="grid" data-options="border:false" class="easyui-treegrid"></table>
		</div>
	</div>
</body>
</html>
