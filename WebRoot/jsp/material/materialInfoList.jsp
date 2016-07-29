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
			title : '添加物料信息',
			url : ext.contextPath + '/material/materialinfo/add.do',
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
			title : '查看物料信息',
			url : ext.contextPath + '/material/materialinfo/view.do?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑物料信息',
			url : ext.contextPath + '/material/materialinfo/edit.do?id=' + id,
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
				$.post(ext.contextPath + '/material/materialinfo/delete.do', {id : id}, function(data) {
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
					$.post(ext.contextPath + '/material/materialinfo/deletes.do', {ids:datas} , function(data) {
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
	
	var downFun = function() {
		searchForm.action = ext.contextPath + '/material/materialinfo/downtemplate.do';
		
		searchForm.submit();
	};
	
	var importFun = function() {
		var dialog = parent.ext.modalDialog({
			title : '选择文件',
			width: 600,
			height:300,
			closeOnEscape:true,
			url : ext.contextPath + '/material/materialinfo/doimport.do',
			buttons : [ {
				text : '导入数据',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.doimport(dialog, grid);
				}
			} ]
		});
	};
	
	var exportFun = function() {
		//通过response导出，用户自己选择路径
 		searchForm.action = ext.contextPath + '/material/materialinfo/exportByResponse.do';
		searchForm.submit();
 		var win = $.messager.progress({
			title:'提示',
			msg:'文件正在导出，请稍后...'
		});
		setTimeout(function(){
			$.messager.progress('close');
		},3000);
		
	};
	var exportByIdFun = function() {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas+=item.id+",";
		}); 
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要导出的记录','info');
		}else{
			searchForm.idStr.value=datas;
			searchForm.action = ext.contextPath + '/material/materialinfo/exportByIds.do';
			searchForm.submit();
	 		var win = $.messager.progress({
				title:'提示',
				msg:'文件正在导出，请稍后...'
			});
			setTimeout(function(){
				$.messager.progress('close');
			},3000);
		}
	};
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/material/materialinfo/getMaterialInfos.do',
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
				{width : '120', title : '物料编码', field : 'materialcode', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value;
					}else{
						return "";
					}
					}},
				{width : '320', title : '物料名称', field : 'materialname', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value;
					}else{
						return "";
					}
					}},			
				{width : '100', title : '图号', field : 'D.number', sortable : true, halign:'center', formatter : function(value, row){
					if(row.figure!=null){
						return row.figure.number;
					}else{
						return "";
					}
					}},
				{width : '80', title : '图名', field : 'D.docname', sortable : true, halign:'center', formatter : function(value, row){
					if(row.figure!=null){
						return row.figure.docname;
					}else{
						return "";
					}
					}},
				{width : '180', title : '规格参数', field : 'spec', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value;
					}else{
						return "";
					}
					}},
				{width : '80', title : '单位', field : 'unit', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value;
					}else{
						return "";
					}
					}},
				{width : '100', title : '类型', field : 'materialtype', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value.typename;
					}else{
						return "";
					}
					}},
				{width : '100', title : '配料类型', field : 'delivertype', sortable : true, align:'center', formatter : function(value, row){
					if(value=="0"){
						return "预约";
					}else if(value=="1"){
						return "申领";
					}else{
						return "";
					}
					}},
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/>';
							<%if (sessionManager.havePermission(session,"material/materialinfo/edit.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
							<%}%>
							<%if (sessionManager.havePermission(session,"material/materialinfo/delete.do")) {%>
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
							<%if (sessionManager.havePermission(session,"material/materialinfo/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"material/materialinfo/delete.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deletesFun();">删除</a>
							</td>
							<%}%>
							<td><div class="datagrid-btn-separator"></div></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_save',plain:true" onclick="downFun();">模版下载</a></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_add',plain:true" onclick="importFun();">导入</a></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="exportFun();">全部导出</a></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="exportByIdFun();">指定导出</a></td>
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
								<td>物料名称</td>
								<td><input name="search_name" class="easyui-textbox" /></td>
								<td>物料编码</td>
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