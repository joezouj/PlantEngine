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
				title : '添加工序',
				url : ext.contextPath + '/process/realDetails/add.do?pid=${param.pid}',
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					}
				} ]
			});
		};
		var editFun = function(id) {
			var dialog = parent.ext.modalDialog({
				title : '编辑工序',
				url : ext.contextPath + '/process/realDetails/edit.do?id=' + id,
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
					$.post(ext.contextPath + '/process/realDetails/delete.do', {id : id}, function(data) {
						if(data==1){
							top.$.messager.alert('提示','删除成功','info',function(){
								grid.datagrid('reload');
								
								selectFirst();//刷新tabs
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
						$.post(ext.contextPath + '/process/realDetails/deletes.do', {ids:datas} , function(data) {
							if(data>0){
								top.$.messager.alert('提示','成功删除'+data+'条记录','info',function(){
									grid.datagrid('reload');
									grid.datagrid('clearChecked');
									
									selectFirst();//刷新tabs
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
				url : ext.contextPath + '/process/realDetails/getlist.do?pid=${param.pid}',
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
					{width : '30', title : '序号', field : 'number', sortable : true, halign:'center'},
					{width : '100', title : '名称', field : 'name', sortable : true, halign:'center'}, 
					{width : '60', title : '标准工时', field : 'worktime', sortable : true, halign:'center'},
					{width : '100', title : '描述', field : 'description', sortable : true, halign:'center'},
					{title : '操作', field : 'action', width : '70', halign:'center', align:'center', formatter : function(value, row) {
							var str = '';
								<%if (sessionManager.havePermission(session,"process/real/edit.do")) {%>
								str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
								<%}%>
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
					
					selectFirst();
				},
				onSelect:function(index,row){
					showdetailsFun(row.id);
				}
			});
		});
		
		function selectFirst(){
			if(grid.datagrid('getData').total>0){
				grid.datagrid('unselectAll');
				grid.datagrid('selectRow',0);
			}else{
				showdetailsFun("-1");
			}
		}
		
		var oldId = "";//用于避免重复提交同一行数据，导致页面卡顿
		var showdetailsFun = function(id) {
			if(id != oldId){
				$("#rightFrameStep").attr("src",ext.contextPath+"/process/realDetailsStep/showList.do?pid="+id);
				$("#rightFrameWorkstation").attr("src",ext.contextPath+"/process/realDetailsWorkstation/showList.do?pid="+id);
				$("#rightFrameJob").attr("src",ext.contextPath+"/process/realDetailsJob/showList.do?pid="+id);
				$("#rightFrameMaterial").attr("src",ext.contextPath+"/process/realDetailsMaterial/showList.do?pid="+id);
				$("#rightFrameEquipment").attr("src",ext.contextPath+"/process/realDetailsEquipment/showList.do?pid="+id);
			}
			oldId = id;
		};
	</script>
	</head>
	<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id="leftdiv" data-options="region:'west',split:true" title="工序" style="width:440px;padding:0;">
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
				<tr>
					<td>
						<form id="searchForm">
							<table>
								<tr>
									<td>名称</td>
									<td><input name="search_name" class="easyui-textbox" style="width: 180px;" /></td>
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
			<table id="grid" data-options="fit:true,border:false"></table>
		</div>
		<div id="rightdiv" data-options="region:'center'">
			<div id="tt" class="easyui-tabs" data-options="fit:true">
				<div title="工序步骤">   
			         <iframe id="rightFrameStep" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
						frameBorder="0" scrolling="auto"></iframe>
			    </div> 
			    <div title="相关工位">   
			         <iframe id="rightFrameWorkstation" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
						frameBorder="0" scrolling="auto"></iframe>
			    </div> 
			    <div title="相关工种">   
			         <iframe id="rightFrameJob" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
						frameBorder="0" scrolling="auto"></iframe>
			    </div>
			    <div title="相关物料">   
			        <iframe id="rightFrameMaterial" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
						frameBorder="0" scrolling="auto"></iframe>
			    </div>   
			    <div title="相关设备">   
			         <iframe id="rightFrameEquipment" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
						frameBorder="0" scrolling="auto"></iframe>
			    </div>   
			</div>  
		</div>
	</div>
	</body>
</html>