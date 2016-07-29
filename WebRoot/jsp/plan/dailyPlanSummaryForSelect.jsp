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
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/plan/dailyplansummary/getlistForSelect.do?querytype=select',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: true,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],			
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '180', title : '名称',  field: 'name', sortable : true, halign:'center' },	 
				{width : '180', title : '计划日期', field : 'plandt', sortable : true, halign:'center' , formatter : function(value, row) {
						if(value!=null){
                       		return value.substring(0,10);
                       }else{
                       		return value;
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
        var jsonstr = "";
        var row = $('#grid').datagrid('getSelected');                
        if (row){
	        //以json格式返回数据      
        	jsonstr = '{"id":"'+row.id
        			+'","name":"'+row.name
        			+'","plandt":"'+row.plandt		
        			+'"}';
	        var dataset = $.parseJSON(jsonstr);
	        return dataset;
        }else{
        	return 0;
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
						<table>
							<tr>
							
								<td>名称</td>
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
 
