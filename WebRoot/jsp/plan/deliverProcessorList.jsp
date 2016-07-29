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
/* 	var addFun = function() {
		var dialog = top.ext.modalDialog({
			title : '添加配送员',
			width:600,
			height:500,
			url : ext.contextPath + '/plan/deliverprocessor/add.do?pid='+'${pid}',
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					grid.datagrid('reload');
				}
			} ]
		});
	}; */
	function addFun(){
		var dialog = parent.ext.modalDialog({
				title : '选择人员',
				width: 600,
				height:480,
				closeOnEscape:true,
				url : ext.contextPath + '/plan/deliverprocessor/add.do?pid='+'${pid}',				  
				buttons : [
				{
					text : '选中',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.addToSelectMulti();
					}
				},{
					text : '全选',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.addToSelectAll();
					}
				},{
					text : '清除',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.removeFromSelectAll();
					}
				},{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.selectOK(dialog, grid);
					}
				},{
					text : '取消',
					iconCls:'icon-cancel',
					handler : function() {
						dialog.dialog('destroy');				
					}
				}]
			});
	}
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/plan/deliverprocessor/delete.do', {id : id}, function(data) {
					if(data==1){
						parent.$.messager.alert('提示','删除成功','info');
						grid.datagrid('reload');
					}else{
						parent.$.messager.alert('提示','删除失败','info');
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
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要删除的记录','info');
		}else{
			parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/plan/deliverprocessor/deletes.do', {ids:datas} , function(data) {
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
			url : ext.contextPath + '/plan/deliverprocessor/getDeliverProcessors.do?pid='+'${pid}',
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
				{width : '180', title : '姓名', field : 'name', align:'center', formatter : function(value, row){
					if(row.processor!=null){
						return row.processor.name;
					}else{
						return "";
					}
				}},
				{width : '180', title : '工号', field : 'serial', align:'center', formatter : function(value, row){
					if(row.processor!=null){
						return row.processor.serial;
					}else{
						return "";
					}
				}},
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							<%if (sessionManager.havePermission(session,"plan/materialdeliver/delete.do")) {%>
							str += '<img class="iconImg ext-icon-table_delete" title="删除" onclick="deleteFun(\''+row.id+'\');"/>';
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
							<%if (sessionManager.havePermission(session,"plan/materialdeliver/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"plan/materialdeliver/delete.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deletesFun();">删除</a>
							</td>
							<%}%>
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