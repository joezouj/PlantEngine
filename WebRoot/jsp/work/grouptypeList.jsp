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
		var dialog = top.ext.modalDialog({
			title : '添加班次',
			width : 600,
			height : 400,
			resizable:true,
			url : ext.contextPath + '/work/grouptype/add.do',
			buttons : [{
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			}]
		});
	}
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑班次',
			url : ext.contextPath + '/work/grouptype/edit.do?id=' + id,
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
				$.post(ext.contextPath + '/work/grouptype/delete.do', {id : id}, function(data) {
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
					$.post(ext.contextPath + '/work/grouptype/deletes.do', {ids:datas} , function(data) {
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
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/work/grouptype/getlist.do',
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
				{width : '100', title : '名称', field : 'name', sortable : true, halign:'center'},
				{width : '100', title : '开始时间', field : 'sdt', sortable : true, halign:'center',align:'center', formatter : function(value, row) {
							var str = '';
							if(value.length>6){
								str = value.substring(0,5);
							}else{
								return value;
							}
							return str;
						}
				}, 
				{width : '100', title : '结束时间', field : 'edt', sortable : true, halign:'center',align:'center', formatter : function(value, row) {
							var str = '';
							if(value.length>6){
								str = value.substring(0,5);
							}else{
								return value;
							}
							return str;
						}
				}, 
				{width : '300', title : '备注', field : 'remark', sortable : true, halign:'center'}, 
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
							<%if (sessionManager.havePermission(session,"work/grouptype/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"work/grouptype/delete.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deletesFun();">删除</a>
							</td>
							<%}%>
						</tr>
						
					</table>
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