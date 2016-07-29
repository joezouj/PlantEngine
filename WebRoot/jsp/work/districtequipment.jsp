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
				title : '添加设备',
				url : ext.contextPath + '/process/realDetailsEquipment/add.do?processrealdetailid=${param.processrealdetailid}',
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
				title : '编辑设备',
				url : ext.contextPath + '/process/realDetailsEquipment/edit.do?id=' + id,
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					}
				} ]
			});
		};
		var deleteFun = function(id) {
			top.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/process/realDetailsEquipment/delete.do', {id : id}, function(data) {
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
						$.post(ext.contextPath + '/process/realDetailsEquipment/deletes.do', {ids:datas} , function(data) {
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
		var dosave = function() {
			var checkedItems = $('#grid').datagrid('getChecked');
			var datas="";
			$.each(checkedItems, function(index, item){
				datas+=item.equipmentid+",";
			}); 
			//if(datas==""){
				//top.$.messager.alert('提示', '请选择设备','info');
			//}else{
				$.post(ext.contextPath + '/work/workTaskEquipment/save.do', {ids:datas,taskid:'${param.taskid}',processrealdetailid:'${param.processrealdetailid}',workstationid:'${param.workstationid}'} , function(data) {
					if(data==1){
						top.$.messager.alert('提示','保存成功','info');
					}else{
						top.$.messager.alert('提示','保存失败','info');
					}
				});
			//}
		};
		$(function() {
			grid = $('#grid').datagrid({
				title : '',
				url : ext.contextPath + '/work/workTaskEquipment/getlist.do?processrealdetailid=${param.processrealdetailid}&taskid=${param.taskid}&workstationid=${param.workstationid}',
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
					{width : '100', title : '设备编码', field : 'equipment', sortable : false, halign:'center',formatter:function(value,row){
						return value.equipmentcardid;
					}}, 
					{width : '150', title : '设备名称', field : 'equipmentname', sortable : false, halign:'center',formatter:function(value,row){
						return row.equipment.equipmentname;
					}}, 
					{width : '150', title : '设备型号', field : 'equipmentmodel', sortable : false, halign:'center',formatter:function(value,row){
						return row.equipment.equipmentmodel;
					}}, 
					{width : '100', title : '设备类型', field : 'equipmentclass', sortable : false, halign:'center',formatter:function(value,row){
						return row.equipment.equipmentclassname;
					}}, 
				] ],
				toolbar : '#toolbar',
				onLoadSuccess : function(data) {
					$('.iconImg').attr('src', ext.pixel_0);
					if(data){
						$.each(data.rows, function(index, item){
							if(item._checked){
								$('#grid').datagrid('checkRow', index);
							}
						});
					}
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
							<%if (sessionManager.havePermission(session,"work/workTaskEquipment/save.do")) {%>
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" 
										onclick="dosave();">分配</a>
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