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
			url : ext.contextPath + '/material/materialbom/getMaterialBOMs.do?querytype=select&bomid=${bomid}',
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
					{width : '150', title : '物料编码', field : 'materialcode', sortable : true, halign:'center'}, 
					{width : '400', title : '物料名称', field : 'materialname', sortable : true, halign:'center'},
					{width : '80', title : '单位', field : 'unit', sortable : true, halign:'center'},
					{width : '80', title : '版本', field : 'version', sortable : true, align:'center'}
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
    	var bomidstr = "";
        var rows = $('#grid').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
        	bomidstr += rows[i].id+",";
        }
        if(bomidstr.length>0){
        	bomidstr = bomidstr.substring(0,bomidstr.length-1);
        }
        jsonstr = '{"bomid":"'+bomidstr+'"}';
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