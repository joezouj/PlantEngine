<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html>
<html>
	<head>
		<title>流程实例</title>
		<jsp:include page="/jsp/inc.jsp"></jsp:include>
		<script type="text/javascript">
		var grid;
		$(function() {
			grid = $('#grid').datagrid({
				title : '',
				url : ext.contextPath + '/work/workorder/gettasklist.do',
				striped : true,
				rownumbers : true,
				pagination : true,
				singleSelect: false,
				ctrlSelect:true,
				selectOnCheck: false,
				checkOnSelect: false,
				idField : 'id',
				pageSize : 50,
				pageList : [ 20, 50, 100],
				columns : [ [
					{checkbox:true , field : 'ck'},
					{width : '120', title : '生产订单号', field : 'productionorderno', sortable : true, halign:'center'}, 
					{width : '200', title : '产品序列号', field : 'productuid', sortable : true, halign:'center'},
					{width : '150', title : '产品编码', field : 'productno', sortable : true, halign:'center'}, 
					{width : '200', title : '产品名称', field : 'productname', sortable : true, halign:'center'}, 
					{width : '150', title : '当前工序', field : 'wftaskdisplay', sortable : true, halign:'center'},
					{width : '100', title : '当前工位', field : 'activeWorkstations', sortable : true, halign:'center', formatter : function(value, row, index) {
							var res="";		
							for(var i=0;i<value.length;i++){
								res+=value[i].serial+", ";
							}
							return res.replace(/, $/g,"");
						}
					},
					{width : '400', title : '相关工位', field : 'workstationlist', sortable : true, halign:'center', formatter : function(value, row, index) {
						var res="";		
						for(var i=0;i<value.length;i++){
							res+=value[i].workstationserial+", ";
						}
						return res.replace(/, $/g,"");
					}
				},
					{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
							var str = '';
								<%if (sessionManager.havePermission(session,"snaker/task/excute.do")) {%>
								//str += '<img class="iconImg ext-icon-application_go" title="执行" onclick="excuteFun(\''+row.wftaskid+'\');"/>';
								<%}%>
								str += '<img class="iconImg ext-icon-application_go" title="执行" onclick="excuteURL(\''+row.wftaskurl+'&taskId='+row.wftaskid+'\');"/>';
							return str;
						}
					} 
				] ],
				toolbar : '#toolbar',
				onLoadSuccess : function(data) {
					$('.iconImg').attr('src', ext.pixel_0);
				}
			});
			
		});
		
		var excuteFun = function(id) {
			var dialog = parent.ext.modalDialog({
				title : '测试查看',
				url : ext.contextPath + '/snaker/task/showlist.do?id=' + id,
				onClose : function(){
					grid.datagrid('reload');
				}
			});
		};
		
		var excuteURL = function(urlStr) {
			var dialog = parent.ext.modalDialog({
				title : '任务执行',
				url : ext.contextPath + urlStr,
				onClose : function(){
					grid.datagrid('reload');
				}
			});
		};
	</script>
	</head>
	<body class="easyui-layout" data-options="fit:true,border:false">
		<div id="toolbar" style="display: none;">
			<table>
				<tr>
					<td>
						<table>
							<tr>
<!-- 								<td><div class="datagrid-btn-separator"></div></td> -->
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<form id="searchForm">
							<table class="tooltable">
								<tr>
								<td>任务名称</td>
								<td><input name="search_name" class="easyui-textbox" /></td>
								<td>任务显示名称</td>
								<td><input name="search_displayname" class="easyui-textbox" /></td>
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
