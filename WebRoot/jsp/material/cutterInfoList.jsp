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
			title : '添加刀具信息',
			url : ext.contextPath + '/material/cutterinfo/add.do',
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
			title : '查看刀具信息',
			url : ext.contextPath + '/material/cutterinfo/view.do?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑刀具信息',
			url : ext.contextPath + '/material/cutterinfo/edit.do?id=' + id,
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
				$.post(ext.contextPath + '/material/cutterinfo/delete.do', {id : id}, function(data) {
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
					$.post(ext.contextPath + '/material/cutterinfo/deletes.do', {ids:datas} , function(data) {
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
			url : ext.contextPath + '/material/cutterinfo/getCutterInfos.do',
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
				{width : '120', title : '刀具编码', field : 'cuttercode', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value;
					}else{
						return "";
					}
					}},
				{width : '120', title : '刀具名称', field : 'cuttername', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value;
					}else{
						return "";
					}
					}},	
				{width : '80', title : '类型', field : 'cutterType', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value.typename;
					}else{
						return "";
					}
					}},
				{width : '120', title : '位置', field : 'cutterPosition', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value.name;
					}else{
						return "";
					}
					}},
				{width : '80', title : '使用寿命', field : 'life', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value+" h";
					}else{
						return "";
					}
					}},
				{width : '80', title : '长度', field : 'length', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value+" mm";
					}else{
						return "";
					}
					}},
				{width : '80', title : '宽度', field : 'width', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value+" mm";
					}else{
						return "";
					}
					}},
				{width : '80', title : '厚度', field : 'ply', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value+" mm";
					}else{
						return "";
					}
					}},
				{width : '150', title : '供应商', field : 'producer', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value;
					}else{
						return "";
					}
					}},
				{width : '60', title : '状态', field : 'status', sortable : true, align:'center', formatter : function(value, row){
					if(value=="0"){
						return "禁用";
					}else if(value=="1"){
						return "启用";
					}else{
						return "";
					}
					}},
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/>';
							<%if (sessionManager.havePermission(session,"material/cutterinfo/edit.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
							<%}%>
							<%if (sessionManager.havePermission(session,"material/cutterinfo/delete.do")) {%>
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
							<%if (sessionManager.havePermission(session,"material/cutterinfo/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"material/cutterinfo/delete.do")) {%>
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
						<table class="tooltable">
							<tr>
								<td>刀具名称</td>
								<td><input name="search_name" class="easyui-textbox" /></td>
								<td>刀具编码</td>
								<td><input name="search_code" class="easyui-textbox" /></td>
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