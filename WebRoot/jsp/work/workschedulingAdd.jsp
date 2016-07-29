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
		var addFun = function() {
			var dialog = parent.ext.modalDialog({
				title : '添加工序',
				url : ext.contextPath + '/process/realDetails/add.do?pid=${param.pid}',
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					}
				} ]
			});
		};
		var editFun = function(id) {
			var dialog = parent.ext.modalDialog({
				title : '编辑工序',
				url : ext.contextPath + '/process/realDetails/edit.do?id=' + id,
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					}
				} ]
			});
		};
		var deleteFun = function(id) {
			parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/process/realDetails/delete.do', {id : id}, function(data) {
						if(data==1){
							top.$.messager.alert('提示','删除成功','info',function(){
								grid.datagrid('reload');
							});
						}else{
							top.$.messager.alert('提示','删除失败','info');
						}
					});
				}
			});
		};
		var deletesFun = function() {
			var checkedItems = $('#grid').datagrid('getChecked');
			var datas="";
			$.each(checkedItems, function(index, item){
				datas+=item.id+",";
			}); 
			if(datas==""){
				top.$.messager.alert('提示', '请先选择要删除的记录','info');
			}else{
				top.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
					if (r) {
						$.post(ext.contextPath + '/process/realDetails/deletes.do', {ids:datas} , function(data) {
							if(data>0){
								top.$.messager.alert('提示','成功删除'+data+'条记录','info',function(){
									grid.datagrid('reload');
									grid.datagrid('clearChecked');
								});
							}else{
								top.$.messager.alert('提示','删除失败','info');
							}
						});
					}
				});
			}
		};
		var dosave = function() {
			var checkedItems = $('#grid').datagrid('getChecked');
			var datas="";
			var realid="";
			var realdetailid=""
			$.each(checkedItems, function(index, item){
				datas+=item.workstationid+",";
				realid+=item.processRealid+",";
				realdetailid+=item.processRealdetailid+",";
			}); 
			if(datas==""){
				top.$.messager.alert('提示', '请选择工位','info');
			}else{
				$.post(ext.contextPath + '/work/worktaskworkstation/save.do', {ids:datas,taskid:'${param.taskid}',processrealid:realid,processrealdetailid:realdetailid} , function(data) {
					if(data==1){
						top.$.messager.alert('提示','保存成功','info');
					}else{
						top.$.messager.alert('提示','保存失败','info');
					}
				});
			}
		};
		function getNextFormatDate() {
		    var nowdate = new Date();
		    var seperator1 = "-";
		    var seperator2 = ":";
		    nowdate=nowdate.valueOf()+1000*60*60*24;
        	var date = new Date(nowdate);
		    var year = date.getFullYear();
		    var month = date.getMonth() + 1;
		    var strDate = date.getDate();
		    if (month >= 1 && month <= 9) {
		        month = "0" + month;
		    }
		    if (strDate >= 0 && strDate <= 9) {
		        strDate = "0" + strDate;
		    }
		    var nextdate = year + seperator1 + month + seperator1 + strDate;
		    return nextdate;
		}
		$(function() {
			grid = $('#grid').datagrid({
				title : '',
				url : ext.contextPath + '/work/workscheduling/getstationlist.do',
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
					{width : '100', title : '工位编号', field : 'serial', sortable : true, halign:'center'}, 
					{width : '100', title : '工位名称', field : 'name', sortable : true, halign:'center'}, 
					{width : '80', title : '工位类型', field : 'typename', sortable : true, halign:'center'},
					{width : '100', title : '所属车间', field : 'deptname', sortable : true, halign:'center'},
					{width : '100', title : '所属产线', field : 'linename', sortable : true, halign:'center'}
				] ],
				toolbar : '#toolbar',
				onLoadSuccess : function(data) {
					$('.iconImg').attr('src', ext.pixel_0);
					if(grid.datagrid('getSelected')==null){
						grid.datagrid('selectRow',0);
					}
				},
				onSelect:function(index,row){
					var date=$('#wdt').val();
					showdetailsFun(date,row.id);
				}
			});
			var nextdate=getNextFormatDate();
			$('#wdt').val(nextdate);
		});
		var showValue = function(workdate) {
			var row = $("#grid").datagrid("getSelected");
			showdetailsFun(workdate,row.id);
		};
		var showdetailsFun = function(workdate,workstationid) {
			//alert(workdate);
			$("#rightFrameScheduling").attr("src",ext.contextPath+"/work/workscheduling/showuserlist.do?workdate="+workdate+"&workstationid="+workstationid);
		};
	</script>
	</head>
	<body>
	<div id="datediv" data-options="region:'center'" title="排班日期" style="padding:5px;">
		<table id="datetable" class="tooltable">
			<tr>
				<td>排班日期</td>
				<td><input id="wdt" name="wdt" class="Wdate" value="${param.workdate }"
					onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"
					onPropertyChange="showValue(this.value)"></td>
			</tr>
		</table>
	</div>
	
	<div id="maindiv" class="easyui-layout" data-options="fit:true">
		<div id="leftdiv" data-options="region:'west',split:true" title="工位选择" style="width:400px;padding:0;">
			<table id="toolbar" style="width:100%">
				<tr>
					<td>
						<form id="searchForm">
							<table>
								<tr>
									<td>名称</td>
									<td><input name="search_name" class="easyui-textbox" style="width: 180px;" /></td>
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
				<div title="选择人员">   
			         <iframe id="rightFrameScheduling" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" 
						frameBorder="0" scrolling="auto"></iframe>
			    </div> 
			</div>  
		</div>
	</div>
	</body>
</html>