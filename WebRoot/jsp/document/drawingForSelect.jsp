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
 			url : ext.contextPath + '/document/getDatasForSelect.do?doctype=B',
 			striped : true,
 			rownumbers : true,
 			singleSelect: false,
			//ctrlSelect: true,
			selectOnCheck: true,
			checkOnSelect: true,
 			idField : 'id',
 			treeField: 'docname',
 			pagination : true,
 			pageSize : 20,
 			pageList : [20, 50, 100],
 			columns : [ [
 				{checkbox:true , field : 'ck'},
				{width : '180', title : '名称', field : 'docname', sortable : true, align:'left',halign:'center'}, 
 				{width : '120', title : '编号', field : 'number', sortable : true, align:'center',halign:'center'}, 
				{width : '100', title : '类型', field : 'doctype', sortable : true, align:'center',halign:'center', formatter : function(value, row, index) {
					switch (value) {
					case 'A':
						return '工作指令';
					case 'B':
						return '图纸';
					case 'C':
						return '作业指导书';
					}
				}}, 
				{width : '320', title : '地址', field : 'path', sortable : true, halign:'center'} 
			] ],
			loadFilter: function (data){			  
                resultData = data;
                    $.each(data.rows, function(i) {  
                        var parentId = data.rows[i].pid;                         
                        if(parentId!='-1'){  
                            data.rows[i]._parentId = parentId;  
                        }else{
                        	data.rows[i]._parentId =undefined;
                        }  
                    });   
                    return data; 
            },
            toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
			}
 		});
 	});
    function selectOK() { 
    	//以json格式返回数据
        var jsonstr = "";
        var row = $('#grid').treegrid('getSelected');
        if (row){
        	jsonstr = '{"id":"'+row.id
        			+'","docname":"'+row.docname
        			+'","number":"'+row.number
        			+'","path":"'+row.path
        			+'"}';
        }
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
								<td>名称</td>
								<td><input name="search_name" class="easyui-textbox" /></td>
								<td>编号</td>
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