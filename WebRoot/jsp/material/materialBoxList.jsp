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
	var addFun = function() {
		var dialog = parent.ext.modalDialog({
			title : '添加单位信息',
			url : ext.contextPath + '/material/materialbox/add.do',
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	var viewFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '查看料盒信息',
			url : ext.contextPath + '/material/materialbox/view.do?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑料盒信息',
			url : ext.contextPath + '/material/materialbox/edit.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/material/materialbox/delete.do', {id : id}, function(data) {
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
					$.post(ext.contextPath + '/material/materialbox/deletes.do', {ids:datas} , function(data) {
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
			url : ext.contextPath + '/material/materialbox/getMaterialBoxes.do',
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
				{width : '180', title : '编号', field : 'boxnumber', sortable : true, align:'center',halign:'center'}, 
				{width : '180', title : '名称', field : 'name', sortable : true, align:'center',halign:'center'}, 
				{width : '100', title : '状态', field : 'status', sortable : true, align:'center',halign:'center',formatter : function(value, row) {
					switch (value) {
					case '0':
						return '可用';
					case '1':
						return '不可用';
					}
				}}, 
				{width : '500', title : '备注', field : 'remark', halign:'center'},
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/>';
							<%if (sessionManager.havePermission(session,"material/materialbox/edit.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
							<%}%>
							<%if (sessionManager.havePermission(session,"material/materialbox/delete.do")) {%>
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
							<%if (sessionManager.havePermission(session,"material/materialbox/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"material/materialbox/delete.do")) {%>
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
								<td>单位名称</td>
								<td><input name="search_name" class="easyui-textbox" /></td>
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