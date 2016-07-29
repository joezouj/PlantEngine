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
			url : ext.contextPath + '/process/realDetailsMaterial/getTaskMaterial.do?pid=${param.pid}',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: false,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '150', title : '物料编码', field : 'materialcode', sortable : true, halign:'center'}, 
				{width : '250', title : '物料名称', field : 'materialname', sortable : true, halign:'center'},
				{width : '150', title : '规格参数', field : 'spec', sortable : true, halign:'center'},
				{width : '80', title : '类型', field : 'typename', sortable : true, halign:'center',align:'center'}, 
				{width : '80', title : '单位', field : 'unit', sortable : true, halign:'center',align:'center'}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				if(data){
					$.post(ext.contextPath + '/process/realDetailsMaterial/getchecklist.do?pid=${param.pid}',
						function(result){
							$.each(result.rows,function(i,obj){
								$.each(data.rows, function(index, item){
									if(item.materialid==obj.materialid){
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
    	var midstr = "";
    	var rows = $('#grid').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
        	midstr += rows[i].materialid+",";
        }
        if(midstr.length>0){
        	midstr = midstr.substring(0,midstr.length-1);
        }
        $.post(ext.contextPath + "/process/realDetailsMaterial/save.do", {mids:midstr,pid:'${param.pid}'}, function(data, textStatus) {
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
					<input type="hidden" name="productcode" value="${productcode}"/>
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