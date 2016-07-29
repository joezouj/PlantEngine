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
<script type="text/javascript" src="json2.js"></script> 
<script type="text/javascript">
	
	var grid;
	var editFun = function(id,index) {
	//alert(index);
		var row=$('#grid').datagrid('getRows')[index];
		var userids="";
		var workstationids="";
		$.each(row.groupmembers, function(index, item){
			userids=userids+item.userid+",";
			workstationids=workstationids+item.workstationid+",";
		}); 
		var dialog = parent.ext.modalDialog({
			title : '组员指派',
			url : ext.contextPath + '/work/group/edit4scheduling.do?id=' + id+"&userids="+userids+"&workstationids="+workstationids,
			buttons : [ {
				text : '确认',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.dosave(dialog,grid);
					changerow(res);
				}
			} ]
		});
	};
	var changerow =function( res){
	//alert(index);
		var row=$('#grid').datagrid('getSelected');
		var editIndex=$('#grid').datagrid('getRowIndex',row);
		var members=JSON.parse(res);
		$('#grid').datagrid('getRows')[editIndex]['groupmembers'] = members;
		$('#grid').datagrid('refreshRow',editIndex);    
		$('.iconImg').attr('src', ext.pixel_0);
	};
	
	$(function() {
		grid = $('#grid').datagrid({
			url : ext.contextPath + '/work/workscheduling/getlist.do?querytype=select&dt=${dt}&grouptypeid=${grouptypeid}',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: false,
			ctrlSelect:true,
			selectOnCheck: true,
			checkOnSelect: false,
			nowrap:false,
			idField : 'id',
			pageSize : 20,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '100', title : '班组名称', field : 'name', sortable : true, halign:'center'}, 
				{width : '180', title : '所属车间', field : 'deptname', sortable : true, halign:'center'},
				{width : '380', title : '班组当班人员', field : 'groupmembers', sortable : false, halign:'center',formatter:function(value, row){
					var res="";
					for(var i=0;i<value.length;i++){
						if(value[i].usertype=='leader'){
							res+=value[i].username+"（"+value[i].workstationname+"）, ";
						}
					}
					var res1="";
					for(var i=0;i<value.length;i++){
						if(value[i].usertype=='member'){
							res1+=value[i].username+"（"+value[i].workstationname+"）, ";
						}
					}
					return "<b>"+res+"</b>"+res1;
				}},
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row,index) {
						var str = '';
							str += '<img class="iconImg ext-icon-table_edit" title="组员指派" onclick="editFun(\''+row.id+'\',\''+index+'\');"/>';
						return str;
					}
				} 
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
				if(data){
					$.each(data.rows, function(index, item){
						if(item.schedulingflag){
							$('#grid').datagrid('checkRow', index);
						}
					});
				}
			}
		});
		
		$('#search_pid').combotree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			width:200,
			editable:false
		});
		
	});
	
	var dosave = function(dialog,parentWin) {
			var checkedItems = $('#grid').datagrid('getChecked');
			if(checkedItems==""){
				top.$.messager.alert('提示', '请选择班组','info');
			}else{
				var errflag="";
				$.each(checkedItems, function(index, item){
					var groupmembers=item.groupmembers;
					$.each(groupmembers, function(index, item_m){
						if(item_m.workstationid==null||item_m.workstationid==""){
							top.$.messager.alert('提示',item.name+"中的“"+item_m.username+"”未安排工位！",'info');
							errflag="1";
							return false;
						}
					});
					if(errflag!=""){
						return false;
					}
				}); 
				if(errflag!=""){
					return false;
				}
				var res=JSON.stringify(checkedItems);
				$.post(ext.contextPath + '/work/workscheduling/save.do', {res:res,dt:'${dt}',grouptypeid:'${grouptypeid}'} , function(data) {
					if(data==1){
						top.$.messager.alert('提示','保存成功','info');
						parentWin.refreshcalerdar();//更新父窗体
						dialog.dialog('destroy');
					}else{
						top.$.messager.alert('提示','保存失败','info');
					}
				});
			}
		};
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false" >
	<table id="toolbar" style="display: none;width:100%">
		<tr>
			<td>
				<form id="searchForm">
					<table class="tooltable">
						<tr>
							<td>班组名称</td>
							<td><input name="search_name" class="easyui-textbox" /></td>
							<td>所属车间</td>
							<td><input id="search_pid" name="search_pid" class="easyui-combotree"/></td>
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
	<div class="easyui-panel" data-options="fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
	<!-- <div id="ft" style="padding:5px;">
        选择需排班的班组，点击保存，完成排班。
    </div> -->
</body>
</html>