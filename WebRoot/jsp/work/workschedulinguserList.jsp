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
	var addFun = function(){
		var workdate= '${param.workdate}';
		if(workdate==""){
			parent.$.messager.alert('提示','请先选择需要排班的日期','info',function(){
			});
		}else{
			var dialog = top.ext.modalDialog({
					title : '选择工段',
					width : 400,
					height : 300,
					resizable:true,
					url : ext.contextPath + '/work/grouptype/showlistforselect.do',
					buttons : [{
						text : '确定',
						iconCls:'icon-ok',
						handler : function() {
							var res=dialog.find('iframe').get(0).contentWindow.selectFun();
							if(res!=null){
								dialog.dialog('destroy');
								var str="";
								$.each(res.result,function(index,item){
									str=item.id;
								});
								selectFun(str);
							}
						}
					}]
			});
		}
		
	};
	var selectFun = function(grouptypeid) {
		var dialog = top.ext.modalDialog({
				title : '选择排班人',
				width : 800,
				height : 600,
				resizable:true,
				url : ext.contextPath + '/work/group/showlistForSelect.do',
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectFun();
						if(res!=null){
							dialog.dialog('destroy');
							var str="";
							$.each(res.result,function(index,item){
								str+=item.userid+",";
							});
							$.post(ext.contextPath + '/work/workscheduling/save.do', 
							{ids:str.replace(/,$/g,""),grouptypeid:grouptypeid,workdate:'${param.workdate}',
							workstationid:'${param.workstationid}'} , function(data) {
								if(data>0){
									parent.$.messager.alert('提示','添加成功','info',function(){
										grid.datagrid('reload');
										grid.datagrid('clearChecked');
									});
								}else{
									parent.$.messager.alert('提示','添加失败','info');
								}
							});
						}
					}
				}]
			});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑任务',
			url : ext.contextPath + '/work/worktask/edit.do?id=' + id,
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
			title : '查看用户信息',
			url : ext.contextPath + '/work/workstation/view.do?id=' + id
		});
	};
	
	var refreshFun = function() {
			$.post(ext.contextPath + '/work/worktask/getnewtask.do', {}, function(data) {
					grid.datagrid('reload');
				});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/work/workscheduling/delete.do', {id : id}, function(data) {
					if(data==1){
						parent.$.messager.alert('提示','删除成功','info',function(){
							grid.datagrid('reload');
						});
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
		//alert(datas);
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要删除的记录','info');
		}else{
			parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/work/workscheduling/deletes.do', {ids:datas} , function(data) {
						if(data>0){
							parent.$.messager.alert('提示','成功删除'+data+'条记录','info',function(){
								grid.datagrid('reload');
								grid.datagrid('clearChecked');
							});
						}else{
							parent.$.messager.alert('提示','删除失败','info');
						}
					});
				}
			});
		}
	};
	var grouptype = function(){
		var dialog = parent.ext.modalDialog({
				title : '工段维护',
				url : ext.contextPath + '/work/grouptype/showlist.do',
				width:1200,
				height:600
			});
	};
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/work/workscheduling/getlist.do?wdt=${param.workdate}&workstationid=${param.workstationid}',
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
				{width : '100', title : '姓名', field : '_username', sortable : true, halign:'center'},
				{width : '100', title : '工段', field : '_grouptypename', sortable : true, halign:'center'},
				{width : '100', title : '班组', field : '_groupname', sortable : true, halign:'center'}, 
				{width : '100', title : '工种', field : '_jobname', sortable : true, halign:'center'}, 
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							<%if (sessionManager.havePermission(session,"work/workscheduling/edit.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
							<%}%>
							<%if (sessionManager.havePermission(session,"work/workscheduling/delete.do")) {%>
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
							<%if (sessionManager.havePermission(session,"work/workscheduling/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"work/workscheduling/delete.do")) {%>
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
						<table class="tooltable">
							<tr>
								<td>人员姓名</td>
								<td><input name="search_username" class="easyui-textbox" /></td>
								<td>&nbsp;</td>
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
		<input id="userids" name="userids" type="hidden"	/>
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
</body>
</html>