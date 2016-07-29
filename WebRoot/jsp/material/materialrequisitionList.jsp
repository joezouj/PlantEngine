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
	var addFun = function() {
		var dialog = parent.ext.modalDialog({
			iframeId:'send',
			title : '添加物料申领信息',
			url : ext.contextPath + '/material/materialrequisition/add.do',
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
			title : '查看物料申领信息',
			url : ext.contextPath + '/material/materialrequisition/view.do?id=' + id
		});
		dialog.dialog({
				onClose:function(){
     				grid.datagrid('reload');
    		    }
			});
	};
	var editFun = function(id) {
		grid.datagrid('clearChecked');
		var dialog = parent.ext.modalDialog({
			title : '编辑物料申领信息',
			url : ext.contextPath + '/material/materialrequisition/edit.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
		dialog.dialog({
				onClose:function(){
     				grid.datagrid('reload');
    		    }
			});
	};
	var deliverFun = function(id) {
		grid.datagrid('clearChecked');
		var dialog = parent.ext.modalDialog({
			iframeId:'send',
			title : '编辑物料申领信息',
			url : ext.contextPath + '/material/materialrequisition/editDelivery.do?iframeId=send&id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
		dialog.dialog({
				onClose:function(){
     				grid.datagrid('reload');
    		    }
			});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/material/materialrequisition/delete.do', {id : id}, function(data) {
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
			top.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/material/materialrequisition/deletes.do', {ids:datas} , function(data) {
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
			url : ext.contextPath + '/material/materialrequisition/getlist.do',
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
			selectOnCheck: true,
			columns : [ [ 
				{checkbox:true , field : 'ck'}, 
				{width : '100', title : '单号', field : 'orderno', sortable : true, halign:'center'},				
                {width : '120', title : '物料编码', field : 'materialinfo', sortable : true, halign:'center',
					formatter:function(value,row,index){ 
						var value = row.materialinfo;
						if(value!=null){
							return value.materialcode;
						}
						return '';
                    } 
                 },
                 {width : '150', title : '物料名称', field : 'materialname', sortable : true, halign:'center',
                	 formatter:function(value,row,index){ 
                		var value = row.materialinfo;
 						if(value!=null){
 							return value.materialname;
 						}
 						return '';
                     }  
                 },
			    {width : '50', title : '数量',  field: 'quantity', sortable : true, halign:'center',align:'center'},
			    {width : '50', title : '单位', field : 'unit', sortable : true, halign:'center',
			    	formatter:function(value,row,index){ 
			    		var value = row.materialinfo;
 						if(value!=null){
 							return value.unit;
 						}
 						return '';
                    }  
                 },
                 {width : '90', title : '规格', field : 'spec', sortable : true, halign:'center',
                	 formatter:function(value,row,index){
                		var value = row.materialinfo;
  						if(value!=null){
  							return value.spec;
  						}
  						return '';
                     }  
                 },
			    {width : '90', title : '物料申领人', field : 'applyuser', sortable : true, halign:'center',
                	 formatter:function(value){  
                        return value.caption;
                     }  
                 },
                 {width : '120', title : '物料申领工位', field : 'workstation', sortable : true, halign:'center',
                	 formatter:function(value){
                		if(value!=null){
   							return value.name;
   						}
   						return '';
                     }  
                 },
			    {width : '120', title : '要求发料时间', field : 'requestsenddate', sortable : true, halign:'center',
                	 formatter:function(value){  
                		 if(value.length<20){
								return value;
						}else{
								return value.substring(0,19);
						}
                     }  
                 },
                 {width : '100', title : '状态', field : 'status', sortable : true, align:'center', formatter : function(value, row){
								switch(value){
									case "0": return "待发料";	
									case "1": return "部分发料";	
									case "2": return "发料完成";								
								}
							} },
				{title : '操作', field : 'action', width : '130', halign:'center', align:'center', 
                	 formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/> ';
							if(row.status=='0'){
								<%if (sessionManager.havePermission(session,"material/materialrequisition/edit.do")) {%>
								str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/> ';
								<%}%>
							}
							if(row.status!='2'){							
								<%if (sessionManager.havePermission(session,"material/materialrequisition/editDelivery.do")) {%>
								str += '<img class="iconImg ext-icon-table_go" title="发料确认" onclick="deliverFun(\''+row.id+'\');"/> ';
								<%}%>
							}
							<%if (sessionManager.havePermission(session,"material/materialrequisition/delete.do")) {%>
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
	
	function formattime(val) {
		var year = parseInt(val.year) + 1900;
		var month = (parseInt(val.month) + 1);
		month = month > 9 ? month : ('0' + month);
		var date = parseInt(val.date);
		date = date > 9 ? date : ('0' + date);
		var hours = parseInt(val.hours);
		hours = hours > 9 ? hours : ('0' + hours);
		var minutes = parseInt(val.minutes);
		minutes = minutes > 9 ? minutes : ('0' + minutes);
		var seconds = parseInt(val.seconds);
		seconds = seconds > 9 ? seconds : ('0' + seconds);
		var time = year + '-' + month + '-' + date + ' ' + hours + ':'
				+ minutes + ':' + seconds;
		return time;
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>
							<%if (sessionManager.havePermission(session,"material/materialrequisition/add.do")) {%>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							<%}%>
							</td>
							<td>
							<%if (sessionManager.havePermission(session,"material/materialrequisition/delete.do")) {%>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deletesFun();">删除</a>
							<%}%>
							</td>
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
								<td>物料申领人</td>
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
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
	</body>
</html>