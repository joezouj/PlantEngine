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
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/material/materialbox/getMaterialBoxes.do?querytype=select',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: true,
			ctrlSelect: false,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '200', title : '编号', field : 'boxnumber', sortable : true, align:'center',halign:'center'}, 
				{width : '200', title : '名称', field : 'name', sortable : true, align:'center',halign:'center'}, 
				{width : '100', title : '状态', field : 'status', sortable : true, align:'center',halign:'center',formatter : function(value, row) {
					switch (value) {
					case '0':
						return '可用';
					case '1':
						return '不可用';
					}
				}}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
			}
		});
	});
    function selectOK() {  
    	//以json格式返回数据
        var jsonstr = "";
        var row = $('#grid').datagrid('getSelected');
        if (row){
        	jsonstr = '{"boxid":"'+row.id
        			+'","boxnumber":"'+row.boxnumber
        			+'","boxname":"'+row.name
        			+'"}';
        	var dataset = $.parseJSON(jsonstr);
 	        return dataset;
        }else{
        	top.$.messager.alert('提示', "请选择要使用的数据！", 'info');
        }
   }
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<form id="searchForm">
						<table class="tooltable">
							<tr>
								<td>单位名称</td>
								<td><input name="search_name" class="easyui-textbox" /></td>
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