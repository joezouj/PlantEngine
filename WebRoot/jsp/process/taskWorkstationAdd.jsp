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
				url: ext.contextPath + '/process/taskworkstation/getchecklist.do?pid='+'${pid}'+'&taskname='+'${taskname}'+'&random='+Math.random(),
				data: '',
				success: function (result) {
					if(result.length>0){
						checkeditems=result.split(",");
					}
				}
		});
		
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/work/workstation/getlist.do',
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
				{width : '100', title : '工位编号', field : 'serial', sortable : true, halign:'center'}, 
				{width : '180', title : '工位名称', field : 'name', sortable : true, halign:'center'}, 
				{width : '80', title : '工位类型', field : 'typename', sortable : true, halign:'center'},
				{width : '80', title : '所属车间', field : 'deptname', sortable : true, halign:'center'},
				{width : '180', title : '工位概述', field : 'intro', halign:'center'}
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
    function dosave(dialog, grid) {
    	var widstr = "";
        var rows = $('#grid').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
        	widstr += rows[i].id+",";
        }
        if(widstr.length>0){
        	widstr = widstr.substring(0,widstr.length-1);
        }
        $.post(ext.contextPath + "/process/taskworkstation/save.do", {wids:widstr,pid:'${pid}',taskname:'${taskname}'}, function(data) {
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
								<td>工位名称</td>
								<td><input id="search_name" name="search_name" class="easyui-textbox" /></td>
								<td>所属车间</td>
								<td><input id="search_dept" name="search_dept" class="easyui-combobox"/></td>
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