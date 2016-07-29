<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
SessionManager sessionManager = new SessionManager();
%>
<!DOCTYPE HTML>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	var addFun = function() {
		var dialog = parent.ext.modalDialog({
			title : '新增日计划',		
			url : ext.contextPath + '/plan/dailyplan/add.do',
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	var viewFun = function(id) {
		grid.datagrid('clearChecked');
		var dialog = parent.ext.modalDialog({
			title : '查看日计划',
			url : ext.contextPath + '/plan/dailyplan/view.do?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑日计划',
			url : ext.contextPath + '/plan/dailyplan/edit.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	var dosave = function(id) {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas += '{"itemid":"'+item.id +'","productionorderno":"'+item.productionorderno +'"},';
		}); 
		return $.parseJSON('{"result":['+datas.replace(/,$/g,"")+']}');
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
			top.$.messager.confirm('提示', '您确定要删除此记录？计划删除后将自动作废。', function(r) {
				if (r) {
					$.post(ext.contextPath + '/plan/dailyplan/deletes.do', {ids:datas} , function(data) {
						if(data>0){
							top.$.messager.alert('提示','成功删除'+data+'条记录','info');
							grid.datagrid('reload');
							grid.datagrid('clearChecked');
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
			url : ext.contextPath + '/plan/dailyplan/getdailyplans_task.do',
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
				{width : '100', title : '销售订单号',  field: 'salesorderproduct', sortable : true, halign:'center' , formatter : function(value, row){
					if(value !=null){
						return value.salesorderno;
					}else{
						return value;
					}
				 } },	 
				{width : '100', title : '生产订单号', field : 'productionorderno', sortable : true, halign:'center' }, 
// 				{width : '100', title : '料号', field : 'bomid', sortable : true, halign:'center' },
				{width : '150', title : '产品信息', field : 'product', sortable : true, halign:'center' , formatter : function(value, row){
							if(value!=null){
								return value.materialname;
							}else{
								return "";
							}
						}}, 
				{width : '80', title : '计划生产数', field : 'productquantity', sortable : true, halign:'center' }, 
				{width : '60', title : '完成数', field : 'finishedquantity', sortable : true, halign:'center' },  
				{width : '120', title : '任务下达日期', field : 'stdt', sortable : true, halign:'center', formatter : function(value, row){
					if(value.substring(0,4)=="1900"){
						return "";
					}else{
						return value.substring(0,19);
					}					
					} },
				{width : '120', title : '任务完成日期', field : 'eddt', sortable : true, halign:'center', formatter : function(value, row){
					if(value.substring(0,4)=="1900"){
						return "";
					}else{
						return value.substring(0,19);
					}
					} },
				{width : '100', title : '计划状态', field : 'status', sortable : true, align:'center', formatter : function(value, row){
						switch(value){
							case "0": return "计划编制";	
							case "1": return "计划下发";	
							case "2": return "计划执行";	
							case "3": return "计划作废";								
						}
					} }			    		    
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
			}
		});
	});
	var invalidateFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要此记录作废？计划作废后将无法撤销。', function(r) {
			if (r) {
				$.post(ext.contextPath + '/plan/dailyplan/invalidate.do', {id : id}, function(data) {
					if(data==1){
						top.$.messager.alert('提示','作废成功','info');
						grid.datagrid('reload');
					}else{
						top.$.messager.alert('提示','作废失败','info');
					}
				});
			}
		});
	};
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
							
								<td>生产订单号</td>
								<td><input name="search_name" class="easyui-textbox" data-options="prompt:'请输入关键字'" style="width: 150px;" value=""/></td>
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
 
