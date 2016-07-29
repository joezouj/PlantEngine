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
		var returnVal = "";//用于传递是否需要刷新页面refresh
	
		var dosave = function() {
			var checkedItems = $('#grid').datagrid('getChecked');
			var datas="";
			var realid=checkedItems[0].processRealid;
			var realdetailid=checkedItems[0].processRealdetailid;
			$.each(checkedItems, function(index, item){
				datas+=item.workstationid+",";
			});
			if(datas==""){
				top.$.messager.alert('提示', '请选择工位','info');
			}else{
				$.post(ext.contextPath + '/work/worktaskworkstation/save.do', {ids:datas,taskid:'${param.taskid}',processrealid:realid,processrealdetailid:realdetailid} , function(data) {
					if(data.res==1){
						top.$.messager.alert('提示','保存成功','info',function(){
							grid.datagrid('selectRecord',grid.datagrid('getSelected'));
							returnVal=data.refresh;
						});
					}else{
						top.$.messager.alert('提示','保存失败','info');
					}
				},'json');
			}
		};
		
		var grid;
		$(function() {
			grid = $('#grid').datagrid({
				title : '',
				url : ext.contextPath + '/work/worktaskworkstation/getworkstationlist.do?taskname=${param.taskname}&taskid=${param.taskid}',
				striped : true,
				rownumbers : true,
				pagination : true,
				singleSelect: true,
				selectOnCheck: true,
				checkOnSelect: false,
				idField : 'id',
				pageSize : 50,
				pageList : [ 20, 50, 100],
				columns : [ [ 
					{checkbox:true , field : 'ck'}, 
					{width : '100', title : '工位编号', field : 'workstationserial', sortable : true, halign:'center'},
					{width : '100', title : '工位', field : 'workstationname', sortable : true, halign:'center'},
					{width : '100', title : '车间', field : 'deptname', sortable : true, halign:'center'}
				] ],
				toolbar : '#toolbar',
				onLoadSuccess : function(data) {
					$('.iconImg').attr('src', ext.pixel_0);
					
					
					if(data){
						$.each(data.rows, function(index, item){
							if(item._checked){
								$('#grid').datagrid('checkRow', index);
							}
						});
					}
					if(grid.datagrid('getSelected')==null){
						grid.datagrid('selectRow',0);
					}
					
					$("#name_search").combobox("select",data.lineid);
				},
				onSelect:function(index,row){
					showdetailsFun(row.processRealdetailid,'${param.taskid}',row.workstationid);
				}
			});
			
			$("#name_search").combobox({
				url : ext.contextPath + '/process/real/getLineListByRealid.do?realid=${workorder.processrealid}&random=' + Math.random(),
				valueField : 'id',
				textField : 'name',
				method:'get',
				panelHeight:'auto'
			});
		});
		
		var showdetailsFun = function(id,taskid,workstationid) {
			//$("#rightFrameScheduling").attr("src",ext.contextPath+"/work/workscheduling/showlist.do?processrealdetailid="+id+"&taskid="+taskid+"&workstationid="+workstationid);
			$("#rightFrameMaterial").attr("src",ext.contextPath+"/work/workTaskMaterial/showList.do?processrealdetailid="+id+"&taskid="+taskid+"&workstationid="+workstationid);
			$("#rightFrameEquipment").attr("src",ext.contextPath+"/work/workTaskEquipment/showList.do?processrealdetailid="+id+"&taskid="+taskid+"&workstationid="+workstationid);
		};
	</script>
	</head>
	<body>
	<div id="maindiv" class="easyui-layout" data-options="fit:true">
		<div id="leftdiv" data-options="region:'west',split:true" title="工位分配 (当前产线：${linename})" style="width:400px;padding:0;">
			<table id="toolbar" style="width:100%">
				<tr>
					<td>
						<%if (sessionManager.havePermission(session,"work/worktaskworkstation/save.do")) {%>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" 
									onclick="dosave();">分配</a>
						<%}%>
					</td>
				</tr>
				<tr>
					<td>
						<form id="searchForm">
							<table>
								<tr>
									<td>产线</td>
									<td><input id="name_search" name="name_search" class="easyui-combobox"/></td>
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
			<table id="grid" data-options="fit:true,border:false"></table>
		</div>
		<div id="rightdiv" data-options="region:'center'">
			<div id="tt" class="easyui-tabs" data-options="fit:true">
				<!-- <div title="排班管理">   
			         <iframe id="rightFrameScheduling" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
						frameBorder="0" scrolling="auto"></iframe>
			    </div> --> 
				<div title="相关物料">   
			        <iframe id="rightFrameMaterial" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
						frameBorder="0" scrolling="auto"></iframe>
			    </div>   
			    <div title="相关设备">   
			         <iframe id="rightFrameEquipment" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
						frameBorder="0" scrolling="auto"></iframe>
			    </div>   
			</div>  
		</div>
	</div>
	</body>
</html>