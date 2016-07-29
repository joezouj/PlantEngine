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
	function selectFun(dialog, grid) {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas = '{"id":"'+item.id +'","serial":"'+item.serial +'",'+
				'"name":"'+item.name +'","typename":"'+item.typename +'",'+
				'"deptname":"'+item.deptname +'","linename":"'+item.linename +'"}';
		}); 
		return $.parseJSON(datas);
	}
	
	var grid;
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/work/workstation/getlist.do?querytype=select',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '160', title : '工位编号', field : 'serial', sortable : true, halign:'center'}, 
				{width : '240', title : '工位名称', field : 'name', sortable : true, halign:'center'}, 
				{width : '80', title : '工位类型', field : 'typename', sortable : true, halign:'center'},
				{width : '180', title : '所属车间', field : 'deptname', sortable : true, halign:'center'}
			] ],
			toolbar : '#toolbar'
		});
		
		$('#search_dept').combotree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			onBeforeSelect:function(node){
				if(node.id=="-1"){
                    $("#deptid").tree("unselect");
                }
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
					<form id="searchForm">
						<table class="tooltable">
							<tr>
								<td>工位名称</td>
								<td><input id="search_name" name="search_name" class="easyui-textbox" /></td>
								<td>所属车间</td>
								<td><input id="search_dept" name="search_dept" class="easyui-combotree"/></td>
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-search',plain:true" 
										onclick="grid.datagrid('load',ext.serializeObject($('#searchForm')));">搜索</a>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" 
										onclick="$('#search_name').textbox('setValue','');$('#search_line').combobox('setValue','');grid.datagrid('load',{});">重置</a>
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