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
			title : '添加物料回收情况',
			url : ext.contextPath + '/plan/materialrecover/add.do',
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
			title : '查看物料回收情况',
			url : ext.contextPath + '/plan/materialrecover/view.do?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑物料回收情况',
			url : ext.contextPath + '/plan/materialrecover/edit.do?id=' + id,
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
				$.post(ext.contextPath + '/plan/materialrecover/delete.do', {id : id}, function(data) {
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
					$.post(ext.contextPath + '/plan/materialrecover/deletes.do', {ids:datas} , function(data) {
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
			url : ext.contextPath + '/plan/materialrecover/getMaterialRecovers.do',
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
				{width : '120', title : '计划名称', field : 'planname', halign:'center', formatter : function(value, row){
					if(row.dailyPlanSummary!=null){
						return row.dailyPlanSummary.name;
					}else{
						return "";
					}
				}},
				{width : '80', title : '计划日期', field : 'plandt', align:'center', formatter : function(value, row){
					if(row.dailyPlanSummary!=null){
						return row.dailyPlanSummary.plandt.length>10?row.dailyPlanSummary.plandt.substring(0,10):"";
					}else{
						return "";
					}
				}},
				{width : '120', title : '物料编码', field : 'materialcode', align:'center', formatter : function(value, row){
					if(row.materialInfo!=null){
						return row.materialInfo.materialcode;
					}else{
						return "";
					}
				}},
				{width : '120', title : '物料名称', field : 'materialname', align:'center', formatter : function(value, row){
					if(row.materialInfo!=null){
						return row.materialInfo.materialname;
					}else{
						return "";
					}
				}},
				{width : '120', title : '归属工位', field : 'workstationname', align:'center', formatter : function(value, row){
					if(row.workstation!=null){
						return row.workstation.name;
					}else{
						return "";
					}
				}},
				{width : '120', title : '工位编号', field : 'workstationserial', align:'center', formatter : function(value, row){
					if(row.workstation!=null){
						return row.workstation.serial;
					}else{
						return "";
					}
				}},
				{width : '120', title : '回收量', field : 'recovernum', sortable : true, align:'center', formatter : function(value, row){
					if(value!=null){
						var unit="";
						if(row.materialInfo!=null){
							unit = row.materialInfo.unit;
						}
						return value+" "+unit;
					}else{
						return "";
					}
				}},
				{width : '80', title : '回收人', field : 'recover', align:'center', formatter : function(value, row){
					if(row.recover!=null){
						return row.recover.name;
					}else{
						return "";
					}
				}},
				{width : '100', title : '回收时间', field : 'recovertime', sortable : true, align:'center', formatter : function(value, row){
					if(value!=null){
						return value.length>16?value.substring(0,16):value;
					}else{
						return "";
					}
				}},
				
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/>';
							<%if (sessionManager.havePermission(session,"plan/materialrecover/edit.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
							<%}%>
							<%if (sessionManager.havePermission(session,"plan/materialrecover/delete.do")) {%>
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
							<%if (sessionManager.havePermission(session,"plan/materialrecover/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"plan/materialrecover/delete.do")) {%>
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
								<td>工位编号</td>
								<td><input name="search_workstationserial" class="easyui-textbox" /></td>
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
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
</body>
</html>