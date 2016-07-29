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
			url : ext.contextPath + '/material/materialinfo/getMaterialInfos.do?querytype=select&typename=1',
			striped : true,
			rownumbers : true,
			pagination : true,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '100', title : '物料编码', field : 'materialcode', sortable : true, halign:'center'}, 
				{width : '80', title : '物料名称', field : 'materialname', sortable : true, halign:'center'},
				{width : '100', title : '图号', field : 'D.number', sortable : true, halign:'center', formatter : function(value, row){
					if(row.figure!=null){
						return row.figure.number;
					}else{
						return "";
					}
					}},
				{width : '80', title : '图名', field : 'D.docname', sortable : true, halign:'center', formatter : function(value, row){
					if(row.figure!=null){
						return row.figure.docname;
					}else{
						return "";
					}
					}},
				{width : '180', title : '规格参数', field : 'spec', sortable : true, halign:'center'},
				{width : '100', title : '类型', field : 'materialtype', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value.typename;
					}else{
						return "";
					}
					}}, 
				{width : '80', title : '单位', field : 'unit', sortable : true, halign:'center'}
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
    	var mcodestr = "";
        var rows = $('#grid').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
        	mcodestr += rows[i].materialcode+",";
        }
        if(mcodestr.length>0){
        	mcodestr = mcodestr.substring(0,mcodestr.length-1);
        }
        jsonstr = '{"materialcode":"'+mcodestr+'"}';
        var dataset = $.parseJSON(jsonstr);
        return dataset;
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
								<td>物料名称</td>
								<td><input name="search_name" class="easyui-textbox" /></td>
								<td>物料编码</td>
								<td><input name="search_code" class="easyui-textbox" /></td>
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