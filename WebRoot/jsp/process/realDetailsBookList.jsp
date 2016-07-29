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
				title : '添加作业指导书',
				url : ext.contextPath + '/process/realDetailsBook/add.do?pid=${param.pid}',
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					}
				} ]
			});
		};
		var editFun = function(id) {
			var dialog = top.ext.modalDialog({
				title : '编辑作业指导书',
				url : ext.contextPath + '/process/realDetailsBook/edit.do?id=' + id+'&?pid=${param.pid}',
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					}
				} ]
			});
		};
		var viewFun = function(id) {
			var dialog = top.ext.modalDialog({
				title : '浏览作业指导书',
				url : ext.contextPath + '/document/showBookView.do?id=' + id+'&?pid=${param.pid}'
			});
		};
		var deleteFun = function(id) {
			top.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/process/realDetailsBook/delete.do', {id : id}, function(data) {
						if(data==1){
							top.$.messager.alert('提示','删除成功','info',function(){
								grid.datagrid('reload');
							});
						}else{
							top.$.messager.alert('提示','删除失败','info');
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
				top.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
					if (r) {
						$.post(ext.contextPath + '/process/realDetailsBook/deletes.do', {ids:datas} , function(data) {
							if(data>0){
								top.$.messager.alert('提示','成功删除'+data+'条记录','info',function(){
									grid.datagrid('reload');
									grid.datagrid('clearChecked');
								});
							}else{
								top.$.messager.alert('提示','删除失败','info');
							}
						});
					}
				});
			}
		};
		
		$(function() {
			grid = $('#grid').datagrid({
				title : '',
				url : ext.contextPath + '/process/realDetailsBook/getlist.do?pid=${param.pid}',
				striped : true,
				rownumbers : true,
				pagination : true,
				ctrlSelect:true,
				singleSelect: false,
				selectOnCheck: false,
				checkOnSelect: false,
				idField : 'id',
				pageSize : 50,
				pageList : [ 20, 50, 100],
				columns : [ [ 
					{checkbox:true , field : 'ck'}, 
					{width : '180', title : '作业指导书', field : 'docname', sortable : false, halign:'center',formatter:function(value,row){
						if(row.book!=null){
							return row.book.docname;
						}else{
							return "";
						}
					}}, 
					{width : '120', title : '编号', field : 'number', sortable : false, halign:'center',formatter:function(value,row){
						if(row.book!=null){
							return row.book.number;
						}else{
							return "";
						}
					}}, 
					{width : '100', title : '类型', field : 'doctype', sortable : false, halign:'center',formatter:function(value,row){
						if(row.book!=null&&row.book.doctype!=null){
							switch (row.book.doctype) {
							case 'A':
								return '工作指令';
							case 'B':
								return '图纸';
							case 'C':
								return '作业指导书';
							}
						}else{
							return "";
						}
					}}, 
					{width : '320', title : '地址', field : 'path', sortable : false, halign:'center',formatter:function(value,row){
						if(row.book!=null){
							return row.book.path;
						}else{
							return "";
						}
					}},
					{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
							var str = '';
								str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.book.id+'\');"/>';
// 								<%if (sessionManager.havePermission(session,"process/real/edit.do")) {%>
// 								str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
// 								<%}%>
								<%if (sessionManager.havePermission(session,"process/real/delete.do")) {%>
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
	<body>
		<table id="toolbar" style="width:100%">
			<tr>
				<td>
					<table>
						<tr>
							<%if (sessionManager.havePermission(session,"process/real/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"process/real/delete.do")) {%>
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
		<table id="grid" data-options="fit:true,border:false"></table>
	</body>
</html>