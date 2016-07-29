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
 			url : ext.contextPath + '/document/getDatasForSelect.do?doctype=C',
 			striped : true,
 			rownumbers : true,
 			singleSelect: false,
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
				if(data){
					$.post(ext.contextPath + '/process/realDetailsBook/getchecklist.do?pid=${param.pid}',
						function(result){
							$.each(result.rows,function(i,obj){
								$.each(data.rows, function(index, item){
									if(item.id==obj.bookid){
										$('#grid').datagrid('checkRow', index);
									}
								});
							});
					},'json');
				}
			}
		});
	});
	
    function dosave(dialog, grid) {
    	var bidstr = "";
        var rows = $('#grid').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
        	bidstr += rows[i].id+",";
        }
        if(bidstr.length>0){
        	bidstr = bidstr.substring(0,bidstr.length-1);
        }
        $.post(ext.contextPath + "/process/realDetailsBook/save.do", {bids:bidstr,pid:'${param.pid}'}, function(data, textStatus) {
        	if(data != ""){
   				top.$.messager.alert("提示", data, "info", function() {
   					grid.datagrid('reload');
   					dialog.dialog('destroy');
   				});
    		}else{
    			top.$.messager.alert("提示", "保存失败", "info", function() {
   					grid.datagrid('reload');
   					dialog.dialog('destroy');
   				}); 
        	}
		});
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