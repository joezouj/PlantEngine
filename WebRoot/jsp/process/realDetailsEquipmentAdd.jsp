<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
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
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/process/realDetailsEquipment/getTaskEquipment.do?pid=${param.pid}',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: false,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			selectOnCheck: true,
			checkOnSelect: true,
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '100', title : '设备编号', field : 'equipmentcardid', sortable : true, halign:'center'},  
				{width : '120', title : '设备名称', field : 'equipmentname', sortable : true, halign:'center'},
				{width : '120', title : '设备型号',  field: 'equipmentmodel', sortable : true, halign:'center'},
				{width : '100', title : '设备类型',  field: 'equipmentclass', sortable : true, halign:'center'},
				{width : '100', title : '存放位置',  field: 'geographyarea', sortable : true, halign:'center'},
				{width : '80', title : '管理状态',  field: 'currentmanageflag', sortable : true, align:'center'},
				{width : '100', title : '备注', field : 'remark', sortable : true, halign:'center'}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				if(data){
					$.post(ext.contextPath + '/process/realDetailsEquipment/getchecklist.do?pid=${param.pid}',
						function(result){
							$.each(result.rows,function(i,obj){
								$.each(data.rows, function(index, item){
									if(item.equipmentid==obj.equipmentid){
										$('#grid').datagrid('checkRow', index);
									}
								});
							});
					},'json');
				}
				$('.iconImg').attr('src', ext.pixel_0);
			}
		});
	});
	function dosave(dialog, grid) {
    	var eidstr = "";
    	var rows = $('#grid').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
        	eidstr += rows[i].equipmentid+",";
        }
        if(eidstr.length>0){
        	eidstr = eidstr.substring(0,eidstr.length-1);
        }
        $.post(ext.contextPath + "/process/realDetailsEquipment/save.do", {eids:eidstr,pid:'${param.pid}'}, function(data, textStatus) {
        	if(data != ""){
   				top.$.messager.alert("提示", data, "info", function() {
   					grid.datagrid('reload');
   					dialog.dialog('destroy');
   				});
    		}else{
    			top.$.messager.alert("提示", "保存失败", "info", function() {
   					grid.datagrid('reload');
   					dialog.dialog('destroy');
   				}); 
        	}
		});
    }
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<form id="searchForm">
						<table>
							<tr>
								<td>设备名称</td>
								<td><input name="search_name" class="easyui-textbox" style="width: 180px;" /></td>
								<td>设备编号</td>
								<td><input name="search_code" class="easyui-textbox" style="width: 180px;" /></td>
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