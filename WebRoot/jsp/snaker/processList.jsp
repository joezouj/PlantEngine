<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<!DOCTYPE html>
<html>
	<head>
		<title>流程定义</title>
		<jsp:include page="/jsp/inc.jsp"></jsp:include>
		<script type="text/javascript">
		var grid;
		var initFun = function() {
			parent.$.messager.confirm('提示', '您确定初始化流程文件吗？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/snaker/process/init.do',function() {
							grid.datagrid('reload');
					});
			}});
		};
		var startFun = function(id) {
			var dialog = parent.ext.modalDialog({
				title : '启用流程文件',
				url : ext.contextPath + '/snaker/process/start.do?id=' + id
			});
		};
		var designFun = function() {
			var dialog = parent.ext.modalDialog({
				id:'designer',
				title : '设计工艺流程',
				height:"80%",
				width:"80%",
				url : ext.contextPath + '/snaker/process/design.do?randomnum='+Math.random(),
				onClose : function(){
					grid.datagrid('reload');
				}
			});
		};

		var redesignFun = function(id) {
			var dialog = parent.ext.modalDialog({
				id:'designer',
				title : '修改工艺流程',
				height:"80%",
				width:"80%",
				url : ext.contextPath + '/snaker/process/redesign.do?id=' + id+'&randomnum='+Math.random(),
				onClose : function(){
					grid.datagrid('reload');
				}
			});
		};
		
		var reuseFun = function(id) {
			var dialog = parent.ext.modalDialog({
				id:'designer',
				title : '重用工艺流程',
				height:"80%",
				width:"80%",
				url : ext.contextPath + '/snaker/process/reuse.do?id=' + id+'&randomnum='+Math.random(),
				onClose : function(){
					grid.datagrid('reload');
				}
			});
		};
		
		var editFun = function(id) {
 			var dialog = parent.ext.modalDialog({
				title : '编辑工艺流程',
				url : ext.contextPath + '/snaker/process/edit.do?id=' + id+'&randomnum='+Math.random(),
				buttons : [{
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					}
				}]
 			});
		};
		var undeployFun = function(id) {
			parent.$.messager.confirm('提示', '您确定要禁用此工艺路线？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/snaker/process/undeploy.do', {id : id}, function(data) {
						if(data==1){
							parent.$.messager.alert('提示','禁用成功','info',function(){
								grid.datagrid('reload');
							});
						}else{
							parent.$.messager.alert('提示','禁用失败','info');
						}
					});
				}
			});
		};
		var enableFun = function(id) {
			parent.$.messager.confirm('提示', '您确定要启用此工艺路线？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/process/process/enable.do', {id : id}, function(data) {
						if(data==1){
							parent.$.messager.alert('提示','启用成功','info',function(){
								grid.datagrid('reload');
							});
						}else{
							parent.$.messager.alert('提示','启用失败','info');
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
				parent.$.messager.confirm('提示', '将级联删除流程定义和流程实例的数据，且不可恢复,您确定要执行本操作？', function(r) {
					if (r) {
						$.post(ext.contextPath + '/snaker/process/cascadeRemove.do', {id:datas} , function(data) {
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
				url : ext.contextPath + '/snaker/process/getProcess.do',
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
					{width : '20%', title : '工艺名称', field : 'name', sortable : true, halign:'center'}, 
					{width : '30%', title : '显示名称', field : 'displayName', sortable : true, halign:'center'},
					{width : '60', title : '工艺版本', field : 'version', sortable : true, align:'center'},
					{width : '60', title : '状态', field : 'state', sortable : true, halign:'center', align:'center', formatter : function(value, row, index) {
							switch (value) {
							case 0:
								return '禁用';
							case 1:
								return '启用';
							default:
								return '启用';
							}
						}
				    },
					{title : '操作', field : 'action', width : '180', halign:'center', align:'center', formatter : function(value, row) {
							var str = '';
<%-- 								<%if (sessionManager.havePermission(session,"snaker/process/start.do")) {%>
								str += '<img class="iconImg ext-icon-control_play_blue" title="启动流程" onclick="startFun(\''+row.id+'\');"/>';
								<%}%> --%>
<%-- 								<%if (sessionManager.havePermission(session,"snaker/process/edit.do")) {%>
								str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
								<%}%> --%>
								<%if (sessionManager.havePermission(session,"snaker/process/redesign.do")) {%>
								str += '<img class="iconImg ext-icon-control_equalizer_blue" title="修改" onclick="redesignFun(\''+row.id+'\');"/>';
								<%}%>
								<%if (sessionManager.havePermission(session,"snaker/process/reuse.do")) {%>
								str += '<img class="iconImg ext-icon-control_repeat_blue" title="重用" onclick="reuseFun(\''+row.id+'\');"/>';
								<%}%>
								<%if (sessionManager.havePermission(session,"snaker/process/undeploy.do")) {%>
								if(row.state=='1'){
									str += '<img class="iconImg ext-icon-control_stop_blue" title="禁用" onclick="undeployFun(\''+row.id+'\');"/>';
								}
								if(row.state=='0'){
									str += '<img class="iconImg ext-icon-control_play_blue" title="启用" onclick="enableFun(\''+row.id+'\');"/>';
								}
								<%}%>
								<%if (sessionManager.havePermission(session,"process/worktask/snakertasklist.do")) {%>
								str += '<img class="iconImg ext-icon-chart_organisation" title="工序配置" onclick="showdetailFun(\''+row.id+'\',\''+row.displayName+'\');"/>';
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

		var showdetailFun = function(id,name) {
			var dialog = parent.ext.modalDialog({
				title : name,
				url : ext.contextPath + '/process/worktask/snakertasklist.do?id=' + id+'&randomnum='+Math.random(),
				height:"80%",
				width:"80%"
			});
		};
	</script>
	</head>
	<body class="easyui-layout" data-options="fit:true,border:false" id="qwert">
		<div id="toolbar" style="display: none;">
			<table>
				<tr>
					<td>
						<table>
							<tr>
								<%-- <%if (sessionManager.havePermission(session,"snaker/process/init.do")) {%>
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-application_form_edit',plain:true" 
										onclick="initFun();">初始化</a>
								</td>
								<%}%> --%>
								<%if (sessionManager.havePermission(session,"snaker/process/design.do")) {%>
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
										onclick="designFun();">新增</a>
								</td>
								<%}%>
								<%if (sessionManager.havePermission(session,"snaker/process/cascadeRemove.do")) {%>
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
									<td>显示名称</td>
									<td><input name="displayName" class="easyui-textbox" /></td>
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
