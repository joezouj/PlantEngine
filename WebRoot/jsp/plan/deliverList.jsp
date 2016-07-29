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
	var deliverFun = function(id) {
		var dialog = top.ext.modalDialog({
			title : '编辑配料详情',
			width:800,
			height:400,
			url : ext.contextPath + '/plan/deliverdetail/deliver.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					grid.datagrid('reload');
					grid.datagrid('destroy');
				}
			} ]
		});
	};
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/plan/deliverdetail/getDeliverDetails.do?pid='+'${pid}',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: false,
			ctrlSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '280', title : '物料名称', field : 'materialname', halign:'center', formatter : function(value, row){
					if(row.materialInfo!=null){
						return row.materialInfo.materialname;
					}else{
						return "";
					}
				}},
				{width : '180', title : '物料编码', field : 'materialcode', halign:'center', formatter : function(value, row){
					if(row.materialInfo!=null){
						return row.materialInfo.materialcode;
					}else{
						return "";
					}
				}},
				{width : '80', title : '计划配送量', field : 'planamount', halign:'center', formatter : function(value, row){
					if(value>0){
						return value+row.materialInfo.unit;
					}else{
						return "";
					}
				}},
				{width : '80', title : '已配送量', field : 'deliveramount', halign:'center', formatter : function(value, row){
					if(value>0){
						return value+row.materialInfo.unit;
					}else{
						return "";
					}
				}},
				{title : '操作', field : 'action', width : '70', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-cart_go" title="配送" onclick="deliverFun(\''+row.id+'\');"/>';
						return str;
					}
				} 
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