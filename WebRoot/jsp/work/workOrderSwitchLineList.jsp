<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html>
<html>
	<head>
		<title>切换产线</title>
		<jsp:include page="/jsp/inc.jsp"></jsp:include>
		<script type="text/javascript">
		var grid;
		$(function() {
			grid = $('#grid').datagrid({
				title : '',
				url : ext.contextPath + '/process/real/getLineListByRealid.do?realid=${workOrder.processrealid}',
				striped : true,
				rownumbers : true,
				singleSelect: true,
				checkOnSelect:true,
				idField : 'id',
				columns : [ [
					{checkbox:true , field : 'ck'},
					{width : '200', title : '产线编号', field : 'serial', sortable : true, halign:'center'},
					{width : '140', title : '部门名称', field : 'deptname', sortable : true, halign:'center'},
					{width : '180', title : '产线名称', field : 'name', sortable : true, halign:'center'}
				] ],
				onLoadSuccess:function(data){
					$('#grid').datagrid("selectRecord","${workOrder.lineid}");
				}
			});
			
		});
		
		function selectOK(dialog,grid) {
			var selectLineid=$('#grid').datagrid("getChecked")[0].id;
			if(selectLineid != "${workOrder.lineid}"){//当数据有变化时才执行
				top.$.messager.confirm('提示', '切换产线后，未执行的工序需要重新配置工位，是否继续？', function(r) {
					if(r){
						$.post(ext.contextPath + '/work/workorder/updateswitchline.do?id=${param.id}&lineid='+selectLineid ,function(data){
							if(data.res=='1'){
								top.$.messager.alert('提示', '切换成功','info',function(){
									grid.datagrid('reload');
									dialog.dialog('destroy');
								});
							}else{
								top.$.messager.alert('提示', '切换失败','info');
							}
						},'json');
					}
				});
			}else{
				dialog.dialog('destroy');
			}
	    }
	</script>
	</head>
	<body>
		<table id="grid" data-options="fit:true,border:false"></table>
	</body>
</html>
