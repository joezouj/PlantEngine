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
			title : '添加BOM信息',
			url : ext.contextPath + '/material/materialbom/add.do',
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
			title : '查看BOM信息',
			url : ext.contextPath + '/material/materialbom/view.do?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '配置BOM信息',
			url : ext.contextPath + '/material/materialbom/edit.do?id=' + id,
			onClose : function(){
				grid.datagrid('reload');
			}
		});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/material/materialbom/delete.do', {id : id}, function(data) {
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
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/material/materialbom/deletes.do', {ids:datas} , function(data) {
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
	};
	
	var downFun = function() {
		searchForm.action = ext.contextPath + '/material/materialbom/downtemplate.do';
		searchForm.submit();
	};
	
	var importFun = function() {
		var dialog = parent.ext.modalDialog({
			title : '选择文件',
			width: 600,
			height:300,
			closeOnEscape:true,
			url : ext.contextPath + '/material/materialbom/doimport.do',
			buttons : [ {
				text : '导入数据',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.doimport(dialog, grid);
				}
			} ]
		});
		
		
	};
	var exportsFun = function() {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas+=item.id+",";
		});
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要导出的BOM','info');
		}else{
			//通过response导出，用户自己选择路径
			searchForm.idStr.value=datas;
	 		searchForm.action = ext.contextPath + '/material/materialbom/exportByResponse.do';
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
			url : ext.contextPath + '/material/materialbom/getMaterialBOMs.do',
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
/* 				{width : '80', title : '序号', field : 'ordernumber', sortable : true, align:'center', halign:'center'}, */
				{width : '200', title : '物料编码', field : 'materialcode', sortable : true, halign:'center'},
				{width : '380', title : '物料名称', field : 'materialname', sortable : true, halign:'center'},
				{width : '50', title : '单位', field : 'unit', sortable : true, align:'center', halign:'center'},
				{width : '80', title : '版本', field : 'version', sortable : true, align:'center', halign:'center'},
				{width : '280', title : '备注', field : 'remark', sortable : true, halign:'center'},
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/>';
							<%if (sessionManager.havePermission(session,"material/materialbom/edit.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="配置" onclick="editFun(\''+row.id+'\');"/>';
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
							<%if (sessionManager.havePermission(session,"material/materialbom/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<td><div class="datagrid-btn-separator"></div></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_save',plain:true" onclick="downFun();">模版下载</a></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_add',plain:true" onclick="importFun();">导入</a></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="exportsFun();">导出</a></td>
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