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
		var checkeditems = [];
		$.ajax({
				type:'post',
				url: ext.contextPath + '/process/taskmaterial/getchecklist.do?pid='+'${pid}'+'&taskname='+'${taskname}'+'&random='+Math.random(),
				data: '',
				success: function (result) {
					if(result.length>0){
						checkeditems=result.split(",");
					}
				}
		});
		
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/material/materialinfo/getMaterialInfos.do?typename=1',
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
				{width : '180', title : '物料名称', field : 'materialname', sortable : true, halign:'center'},
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
				{width : '100', title : '规格参数', field : 'spec', sortable : true, halign:'center'},
				{width : '80', title : '类型', field : 'materialtype', sortable : true, halign:'center', formatter : function(value, row){
					if(value!=null){
						return value.typename;
					}else{
						return "";
					}
					}}, 
				{width : '60', title : '单位', field : 'unit', sortable : true, halign:'center'}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
				if(data){
					$.each(data.rows, function(index, item){
						if(checkeditems.indexOf(item.id)>-1){
							$('#grid').datagrid('checkRow', index);
						}
					});
				}
			}
		});
	});
    function dosave(dialog, grid) {
    	
    	var midstr = "";
        var rows = $('#grid').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
        	midstr += rows[i].id+",";
        }
        if(midstr.length>0){
        	midstr = midstr.substring(0,midstr.length-1);
        }
        $.post(ext.contextPath + "/process/taskmaterial/save.do", {mids:midstr,pid:'${pid}',taskname:'${taskname}'}, function(data) {
			if (data.res != "") {
				top.$.messager.alert('提示', data.res, 'info', function() {
					grid.datagrid('reload');
					dialog.dialog('destroy');
				});
			}else{
				top.$.messager.alert('提示', "操作无效", 'info');
			}
		},'json');
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