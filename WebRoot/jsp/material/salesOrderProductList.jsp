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
			title : '新增销售订单',			
			url : ext.contextPath + '/material/salesorderproduct/add.do',
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			},
			{
				text : '下发',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dodistribute(dialog, grid);
				}
			} ]
		});
	};
	var viewFun = function(id) {
		grid.datagrid('clearChecked');
		var dialog = parent.ext.modalDialog({
			title : '查看销售订单',
			url : ext.contextPath + '/material/salesorderproduct/view.do?id=' + id,
			buttons : [ {
				text : '确认完成',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dofinish(dialog, grid);
				}
			}]
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑销售订单',
			url : ext.contextPath + '/material/salesorderproduct/edit.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			},
			{
				text : '下发',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dodistribute(dialog, grid);
				}
			} ]
		});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '删除订单将删除下发的日计划，确认删除？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/material/salesorderproduct/delete.do', {id : id}, function(data) {
					if(data==1){
						top.$.messager.alert('提示','删除成功','info');
						grid.datagrid('reload');
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
			top.$.messager.confirm('提示', '删除订单将删除下发的日计划，确认删除？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/material/salesorderproduct/deletes.do', {ids:datas} , function(data) {
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
			url : ext.contextPath + '/material/salesorderproduct/getSalesOrders.do',
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
				{width : '13%', title : '销售订单编号',  field: 'salesorderno', sortable : true, halign:'center' },
				{width : '13%', title : '产品编码',  field: 'materialcode', sortable : true, halign:'center' , 
					formatter : function(value, row) {
						if(row.product!=null){
							return row.product.materialcode==null?"":row.product.materialcode;
						}else{
							return "";
						}
			        }
				},
				{width : '27%', title : '产品名称', field : 'product', sortable : true, halign:'center' , formatter : function(value, row) {
						if(value!=null){
				       		return value.materialname==null?"":value.materialname;
				       }else{
				       		return "";
				       }
				}}, 
				{width : '13%', title : '客户名称',  field: 'clientname', sortable : true, halign:'center' },
				{width : '10%', title : '产品数量', field : 'productnum', sortable : true, align:'center'},	
				{width : '10%', title : '订单状态', field : 'status', sortable : true, align:'center', formatter : function(value, row){
					switch(value){
							case "0": return "编辑";	
							case "1": return "已完成";	
							case "2": return "已作废";
							case "3": return "已下发";																
						}
				}},			    		    
				{title : '操作', field : 'action', width : '10%', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/> ';
							if(row.status=='0'){
								<%if (sessionManager.havePermission(session,"material/salesorderproduct/edit.do")) {%>
								str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/> ';
								<%}%>
							}
							<%if (sessionManager.havePermission(session,"material/salesorderproduct/delete.do")) {%>
							str += '<img class="iconImg ext-icon-table_delete" title="删除" onclick="deleteFun(\''+row.id+'\');"/>';
							<%}%>
							if(row.status!='2'){
								<%if (sessionManager.havePermission(session,"material/salesorderproduct/delete.do")) {%>
								str += '<img class="iconImg ext-icon-bin" title="订单作废" onclick="invalidateFun(\''+row.id+'\');"/> ';
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
	var invalidateFun = function(id) {
		parent.$.messager.confirm('提示', '订单作废后计划也作废，确定要作废此订单？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/material/salesorderproduct/invalidate.do', {id : id}, function(data) {
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
					<table>
						<tr>
							<%if (sessionManager.havePermission(session,"material/salesorderproduct/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"material/salesorderproduct/delete.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deletesFun();">删除</a>
							</td>
							<%}%>
							<!-- <td><div class="datagrid-btn-separator"></div></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_add',plain:true" onclick="">导入</a></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="">导出</a></td> -->
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<form id="searchForm">
						<table>
							<tr>
							
								<td>销售订单编号</td>
								<td><input name="search_name" class="easyui-textbox" data-options="prompt:'请输入关键字'" style="width: 150px;" value=""/></td>
								<td >订单状态</td>
								<td colspan='4'><select id="search_status" name="search_status" class="easyui-combobox" style="width:152px;">
										<option value='all' select>全部</option>
										<option value='edit'>编制中</option>
										<option value='deliverd'>已下发</option>
										<option value='completed'>已完成</option>
										<option value='invalid'>已作废</option>											
									</select>
								</td>
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
 
