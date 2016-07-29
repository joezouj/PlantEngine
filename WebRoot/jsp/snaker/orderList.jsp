<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html>
<html>
	<head>
		<title>流程实例</title>
		<jsp:include page="/jsp/inc.jsp"></jsp:include>
		<script type="text/javascript">
		var grid;
		var terminateFun = function(id) {
			parent.$.messager.confirm('提示', '您确定要废弃此流程实例？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/snaker/order/terminate.do', {id : id}, function(data) {
						if(data==1){
							parent.$.messager.alert('提示','废弃成功','info');
							grid.datagrid('reload');
						}else{
							parent.$.messager.alert('提示','废弃失败','info');
						}
					});
				}
			});
		};
		var deletesFun = function() {
			var checkedItems = $('#grid').datagrid('getChecked');
			var datas="";
			$.each(checkedItems, function(index, item){
				datas+=item.id+",";
			}); 
			//alert(datas);
			if(datas==""){
				top.$.messager.alert('提示', '请先选择要删除的记录','info');
			}else{
				parent.$.messager.confirm('提示', '将级联流程实例的数据，且不可恢复,您确定要执行本操作？', function(r) {
					if (r) {
						$.post(ext.contextPath + '/snaker/order/cascadeRemove.do', {id:datas} , function(data) {
							if(data>0){
								parent.$.messager.alert('提示','成功删除'+data+'条记录','info');
								grid.datagrid('reload');
								grid.datagrid('clearChecked');
							}else{
								parent.$.messager.alert('提示','删除失败','info');
							}
						});
					}
				});
			}
		};
		
		$(function() {
			grid = $('#grid').datagrid({
				title : '',
				url : ext.contextPath + '/snaker/order/getOrders.do',
				striped : true,
				rownumbers : true,
				pagination : true,
				singleSelect: false,
				ctrlSelect:true,
				selectOnCheck: false,
				checkOnSelect: false,
				idField : 'id',
				pageSize : 50,
				pageList : [ 20, 50, 100],
				columns : [ [
					{checkbox:true , field : 'ck'},
					{width : '20%', title : '流程ID', field : 'processId', sortable : true, halign:'center'}, 
					{width : '30%', title : '流程实例ID', field : 'id', sortable : true, halign:'center'},
					{width : '60', title : '版本号', field : 'version', sortable : true, halign:'center'},
					{width : '120', title : '开始时间', field : 'createTime', sortable : true, halign:'center', formatter : function(value, row){
							if(value.length<20){
								return value;
							}else{
								return value.substring(0,19);
							}					
							}},
					{width : '120', title : '最新操作时间', field : 'lastUpdateTime', sortable : true, halign:'center', formatter : function(value, row){
							if(value.length<20){
								return value;
							}else{
								return value.substring(0,19);
							}					
							}},
					{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
							var str = '';
								<%if (sessionManager.havePermission(session,"snaker/order/terminate.do")) {%>
								str += '<img class="iconImg ext-icon-tag_blue_delete" title="废弃" onclick="terminateFun(\''+row.id+'\');"/>';
								<%}%>
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
								<%if (sessionManager.havePermission(session,"snaker/order/cascadeRemove.do")) {%>
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
										onclick="deletesFun();">删除</a>
								</td>
								<%}%>
								<td><div class="datagrid-btn-separator"></div></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<form id="searchForm">
							<table class="tooltable">
								<tr>
									<td>流程实例名称</td>
									<td><input id="search_pid" name="search_pid" class="easyui-combotree"/></td>
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
		</div>
		<div data-options="region:'center',fit:true,border:false">
			<table id="grid" data-options="fit:true,border:false"></table>
		</div>
	</body>
</html>
