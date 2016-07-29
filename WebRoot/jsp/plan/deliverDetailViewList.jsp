<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/plan/deliverdetail/getDeliverDetails.do?pid='+'${pid}',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: false,
			ctrlSelect: false,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{width : '350', title : '物料名称', field : 'materialname', halign:'center', formatter : function(value, row){
					if(row.materialInfo!=null){
						return row.materialInfo.materialname;
					}else{
						return "";
					}
				}},
				{width : '200', title : '物料编码', field : 'materialcode', halign:'center', formatter : function(value, row){
					if(row.materialInfo!=null){
						return row.materialInfo.materialcode;
					}else{
						return "";
					}
				}},
				{width : '160', title : '计划配送量', field : 'planamount', halign:'center', formatter : function(value, row){
					if(value>0){
						return value+row.materialInfo.unit;
					}else{
						return "";
					}
				}}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
			}
		});
	});
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>
							<form id="searchForm">
							<table class="tooltable" >
								<tr>
										<td>物料名称</td>
										<td><input name="search_materialname" class="easyui-textbox" /></td>
										<td>物料编码</td>
										<td><input name="search_materialcode" class="easyui-textbox" /></td>
										<td>
											<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-search',plain:true" 
												onclick="grid.datagrid('load',ext.serializeObject($('#searchForm')));">搜索</a>
											<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" 
												onclick="$('#searchForm').form('clear');grid.datagrid('load',{});">重置</a>
										</td>
								</tr>
							</table>
							</form>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<form id="searchForm">
						<input name="idStr" type="hidden" />
					</form>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
</body>
</html>