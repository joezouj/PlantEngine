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
	var grid_process;
	var addFun = function() {
		var dialog = parent.ext.modalDialog({
			title : '添加任务',
			url : ext.contextPath + '/work/worktask/add.do',
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
			title : '编辑任务',
			url : ext.contextPath + '/work/worktask/edit.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	var viewFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '查看用户信息',
			url : ext.contextPath + '/work/workstation/view.do?id=' + id
		});
	};
	
	var refreshFun = function() {
			$.post(ext.contextPath + '/work/worktask/getnewtask.do', {}, function(data) {
					grid.datagrid('reload');
				});
	};
	var dodel = function() {
		var checkedItems = $('#grid_process').datagrid('getSelections');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas+=item.id+",";
		}); 
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要删除的记录','info');
		}else{
			parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/work/workprocess/delprocess.do', {ids:datas} , function(data) {
						if(data>0){
							parent.$.messager.alert('提示','成功删除'+data+'条记录','info',function(){
								grid_process.datagrid('reload');
							});
						}else{
							parent.$.messager.alert('提示','删除失败','info');
						}
					});
				}
			});
		}
	};
	
	$(function() {
		grid_process = $('#grid_process').datagrid({
			title : '',
			url : ext.contextPath + '/work/workprocess/getlist_process.do?taskid='+'${taskid}',
			striped : true,
			singleSelect: false,
			rownumbers : true,
			ctrlSelect:true,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			columns : [ [ 
				{width : '250', title : '流程名称', field : 'name', sortable : true, halign:'center',
				formatter : function(value, row) {
					return row.realDetails.name;
				}}, 
				{width : '150', title : '流程状态', field : 'status', sortable : true, halign:'center'}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				var datagridLength = grid_process.datagrid('getRows').length;
				$('.iconImg').attr('src', ext.pixel_0);
				if(grid_process.datagrid('getSelected')==null){
					grid_process.datagrid('selectRow',datagridLength-1);
				}
			},
			onSelect:function(index,row){
				showdetailsFun('${taskid}',row.processRealdetailid,row.processRealid,row.id);
			}
		});
		grid_agency = $('#grid_agency').datagrid({
			title : '',
			url : ext.contextPath + '/work/workprocess/getlist_agency.do?taskid='+'${taskid}',
			striped : true,
			singleSelect: false,
			rownumbers : true,
			ctrlSelect:true,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			columns : [ [ 
				{width : '250', title : '流程名称', field : '_processdetailname', sortable : true, halign:'center'}, 
				{width : '150', title : '对应工位', field : 'workstationname', sortable : true, halign:'center'}, 
				{width : '150', title : '操作人员', field : 'name', sortable : true, halign:'center',
					formatter : function(value, row) {
						if(row.user==null){
						}else{
							return row.user.caption;
						}
					}
				},
				{width : '150', title : '任务进展', field : 'status', sortable : true, halign:'center',
				formatter : function(value, row) {
						if(row._taskstdt==''){
							return '该工序尚未开始';
						}else if(row._taskedt==''){
							return '该工序进行中';
						}else{
							return '该工序已完成';
						}
				}}
			] ],
				 
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
				if(grid_agency.datagrid('getSelected')==null){
					grid_agency.datagrid('selectRow',0);
				}
			},
			onSelect:function(index,row){
				var processdetailid=row.processRealdetailid;
				var rows=$('#grid_process').datagrid('getData').rows;
				var i=0;
				for(i=0;i<rows.length;i++){
					if(rows[i].processRealdetailid==processdetailid){
						$('#grid_process').datagrid('unselectAll');
						$('#grid_process').datagrid('selectRow',i);
						break;
					}
				}
				if(i==rows.length){
					top.$.messager.alert('提示', '该任务尚未执行到属于您的流程！','info');
				}else{
					setTimeout(function () {
					    $('#belowFrame')[0].contentWindow.selecttask(row.workstationid);
					  }, 1000);
				}
			}
		});
		
	});
	var showdetailsFun = function(taskid,processdetailid,processid,processorderid) {
	//alert(taskid+","+processdetailid+","+processid)
			$("#belowFrame").attr("src",ext.contextPath+"/work/workprocess/showlist_detail.do?taskid="+taskid+
			"&processdetailid="+processdetailid+"&processid="+processid+"&processorderid="+processorderid);
		};
</script>
</head>
<body style="padding:5px;height:650px;">
	<div id="tt">
		<a href="javascript:void(0)" class="icon-remove" title="删除" onclick="dodel()"></a>
	</div>
	<div class="easyui-panel" title="待办条目" style="width:950px;height:150px;">
		<table id="grid_agency" data-options="border:false"></table>
	</div>
	<br/>
	<div class="easyui-panel" title="流程信息" data-options="tools:'#tt'" style="width:950px;height:150px;">
		<table id="grid_process" data-options="border:false"></table>
	</div>
	<br/>
	<div id="belowdiv" title="流程详情" class="easyui-panel" style="width:950px;height:450px;"> 
        <iframe id="belowFrame" name="belowFrame" src="" allowTransparency="true"  width=100% height=100%
			frameBorder="0" scrolling="no" ></iframe>
	</div>
</body>
</html>