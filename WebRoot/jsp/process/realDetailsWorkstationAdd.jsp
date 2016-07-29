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
			url : ext.contextPath + '/process/realDetailsWorkstation/getTaskWorkstation.do?pid=${param.pid}',
			striped : true,
			pagination : true,
			rownumbers : true,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '180', title : '工位编号', field : 'serial', sortable : true, halign:'center'}, 
				{width : '180', title : '工位名称', field : 'name', sortable : true, halign:'center'}, 
				{width : '80', title : '工位类型', field : 'typename', sortable : true, halign:'center'},
				{width : '120', title : '所属车间', field : 'deptname', sortable : true, halign:'center'}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				if(data){
					$.post(ext.contextPath + '/process/realDetailsWorkstation/getlist1.do?pid=${param.pid}',
						function(result){
							$.each(result.rows,function(i,obj){
								$.each(data.rows, function(index, item){
									if(item.workstationid==obj.workstationid){
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
    	var widstr = "";
        var rows = $('#grid').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
        	widstr += rows[i].workstationid+",";
        }
        if(widstr.length>0){
        	widstr = widstr.substring(0,widstr.length-1);
        }
        $.post(ext.contextPath + "/process/realDetailsWorkstation/save.do", {wids:widstr,pid:'${param.pid}'}, function(data, textStatus) {
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
								<td>工位名称</td>
								<td><input id="search_name" name="search_name" class="easyui-textbox" /></td>
								<td>工位编号</td>
								<td><input name="search_code" class="easyui-textbox" /></td>
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
	<div id="grid" data-options="fit:true,border:false"></div>
</body>
</html>