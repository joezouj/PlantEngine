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
			title : '物料配送任务',
			url : ext.contextPath + '/plan/materialdeliver/addMaterialMission.do',
			onClose : function(){
				grid.datagrid('reload');
			}
		});
	};
	var viewFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '查看物料配送任务',
			url : ext.contextPath + '/plan/materialdeliver/view.do?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑物料配送任务',
			url : ext.contextPath + '/plan/materialdeliver/edit.do?id=' + id,
			onClose : function(){
				grid.datagrid('reload');
			}
		});
	};
	var distributeFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '分配物料配送任务',
			url : ext.contextPath + '/plan/materialdeliver/distribute.do?id=' + id,
			onClose : function(){
				grid.datagrid('reload');
			}
		});
	};
	var deliverFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要下发此任务？下发后将不可修改任务信息', function(r) {
			if (r) {
				$.post(ext.contextPath + '/plan/materialdeliver/deliver.do', {id : id}, function(data) {
					if(data.result==1){
						parent.$.messager.alert('提示','下发成功','info');
						grid.datagrid('reload');
					}else{
						parent.$.messager.alert('提示',data.feedback,'info');
					}
				},'json');
			}
		});
	};
	var revokeFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要撤回此任务？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/plan/materialdeliver/revoke.do', {id : id}, function(data) {
					if(data==1){
						parent.$.messager.alert('提示','撤回成功','info');
						grid.datagrid('reload');
					}else{
						parent.$.messager.alert('提示','撤回失败','info');
					}
				});
			}
		});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/plan/materialdeliver/delete.do', {id : id}, function(data) {
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
					$.post(ext.contextPath + '/plan/materialdeliver/deletes.do', {ids:datas} , function(data) {
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
			url : ext.contextPath + '/plan/materialdeliver/getMaterialDelivers.do',
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
				{width : '180', title : '计划名称', field : 'planname', halign:'center', formatter : function(value, row){
					if(row.dailyPlanSummary!=null){
						return row.dailyPlanSummary.name;
					}else{
						return "";
					}
				}},
				{width : '100', title : '计划日期', field : 'plandt', align:'center', formatter : function(value, row){
					if(row.dailyPlanSummary!=null){
						return row.dailyPlanSummary.plandt.length>10?row.dailyPlanSummary.plandt.substring(0,10):"";
					}else{
						return "";
					}
				}},
				{width : '180', title : '归属工位', field : 'workstationname', halign:'center', formatter : function(value, row){
					if(row.workstation!=null){
						return row.workstation.name;
					}else{
						return "";
					}
				}},
				{width : '120', title : '工位编号', field : 'workstationserial', halign:'center', formatter : function(value, row){
					if(row.workstation!=null){
						return row.workstation.serial;
					}else{
						return "";
					}
				}},
				{width : '120', title : '开始时间', field : 'starttime', align:'center', formatter : function(value, row){
					if(value!=null){
						return value.length>10?value.substring(0,16):"";
					}else{
						return "";
					}
				}},
				{width : '120', title : '结束时间', field : 'endtime', align:'center', formatter : function(value, row){
					if(value!=null){
						return value.length>10?value.substring(0,16):"";
					}else{
						return "";
					}
				}},
				{width : '120', title : '状态', field : 'status', sortable : true, align:'center', formatter : function(value, row){
					switch (value) {
					case '0':
						return '制定中';
					case '1':
						return '已下发';
					case '2':
						return '配送中';
					case '3':
						return '已完成';
					default:
						return '';
					}
				}},
				
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/>';
							if(row.status=='0'){
								<%if (sessionManager.havePermission(session,"plan/materialdeliver/edit.do")) {%>
								str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
								<%}%>
								<%if (sessionManager.havePermission(session,"plan/materialdeliver/distribute.do")) {%>
								str += '<img class="iconImg ext-icon-table_link" title="分配" onclick="distributeFun(\''+row.id+'\');"/>';
								<%}%>
								<%if (sessionManager.havePermission(session,"plan/materialdeliver/deliver.do")) {%>
								str += '<img class="iconImg ext-icon-table_go" title="下发" onclick="deliverFun(\''+row.id+'\');"/> ';
								<%}%>
								<%if (sessionManager.havePermission(session,"plan/materialdeliver/delete.do")) {%>
								str += '<img class="iconImg ext-icon-table_delete" title="删除" onclick="deleteFun(\''+row.id+'\');"/>';
								<%}%>
							}
							if(row.status=='1'||row.status=='2'){
								<%if (sessionManager.havePermission(session,"plan/materialdeliver/revoke.do")) {%>
								str += '<img class="iconImg ext-icon-arrow_rotate_clockwise" title="撤回" onclick="revokeFun(\''+row.id+'\');"/> ';
								<%}%>
							}
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
								<td>工位名称</td>
								<td><input name="search_workstationname" class="easyui-textbox" /></td>
								<td>工位编号</td>
								<td><input name="search_workstationserial" class="easyui-textbox" /></td>
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