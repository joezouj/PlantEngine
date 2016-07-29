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
		var dialog = top.ext.modalDialog({
			title : '添加配料详情',
			width:800,
			height:400,
			url : ext.contextPath + '/plan/deliverdetail/add.do?pid=${pid}&planid=${planid}&workstationid=${workstationid}',
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					grid.datagrid('reload');
				}
			} ]
		});
	};
	var editFun = function(id) {
		var dialog = top.ext.modalDialog({
			title : '编辑配料详情',
			width:800,
			height:400,
			url : ext.contextPath + '/plan/deliverdetail/edit.do?id=' + id,
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
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/plan/deliverdetail/delete.do', {id : id}, function(data) {
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
					$.post(ext.contextPath + '/plan/deliverdetail/deletes.do', {ids:datas} , function(data) {
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
				{width : '180', title : '物料名称', field : 'materialname', align:'center', formatter : function(value, row){
					if(row.materialInfo!=null){
						return row.materialInfo.materialname;
					}else{
						return "";
					}
				}},
				{width : '180', title : '物料编码', field : 'materialcode', align:'center', formatter : function(value, row){
					if(row.materialInfo!=null){
						return row.materialInfo.materialcode;
					}else{
						return "";
					}
				}},
				{width : '180', title : '计划配送量', field : 'planamount', align:'center', formatter : function(value, row){
					if(value>0){
						return value+row.materialInfo.unit;
					}else{
						return "";
					}
				}},
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							<%if (sessionManager.havePermission(session,"plan/materialdeliver/edit.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
							<%}%>
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
							<td style='padding-left:20px;'>
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