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
	function selectFun() {
		var checkedItems = $('#grid1').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas += '{"userid":"'+item.userid +'","usertype":"'+item.usertype +'","username":"'+item.username +'"},';
		}); 
		return $.parseJSON('{"result":['+datas.replace(/,$/g,"")+']}');
	}
	
	var grid;
	$(function() {
		grid = $('#grid').datagrid({
			title : '班组选择',
			url : ext.contextPath + '/work/group/getlist.do?querytype=select',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: true,
			idField : 'id',
			height:300,
			pageSize : 20,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{width : '100', title : '班组名称', field : 'name', sortable : true, halign:'center'}, 
				{width : '180', title : '所属车间', field : 'deptname', sortable : true, halign:'center'},
				{width : '180', title : '备注', field : 'remark', sortable : true, halign:'center'}
			] ],
			toolbar : '#toolbar',
			onSelect:function(index,row){
				loadMembers(row.id);
			}
		});
		
		$('#search_pid').combotree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			width:200,
			editable:false
		});
		
	});
	
	function loadMembers(groupid){
		$('#grid1').datagrid({
			title : '人员选择',
			url : ext.contextPath + '/work/group/getMemberListByGroupId.do?groupid='+groupid,
			height:400,
			striped : true,
			pagination : true,
			singleSelect: false,
			ctrlSelect:true,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			pageSize : 20,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '100', title : '人员类型', field : 'usertype', sortable : false, halign:'center',formatter:function(value){
					if(value=='leader'){
						return "组长";
					}else{
						return "组员";
					}
				}}, 
				{width : '180', title : '人员名称', field : 'username', sortable : false, halign:'center'}
			] ]
		});
	}
	
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false" style="padding:5px;">
	<table id="toolbar" style="display: none;width:100%">
		<tr>
			<td>
				<form id="searchForm">
					<table class="tooltable">
						<tr>
							<td>班组名称</td>
							<td><input name="search_name" class="easyui-textbox" /></td>
							<td>所属车间</td>
							<td><input id="search_pid" name="search_pid" class="easyui-combotree"/></td>
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
	<table id="grid"></table>
	<div style="height:10px"></div>
	<table id="grid1"></table>
</body>
</html>