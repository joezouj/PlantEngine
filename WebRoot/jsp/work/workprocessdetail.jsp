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
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/work/worktask/delete.do', {id : id}, function(data) {
					if(data==1){
						parent.$.messager.alert('提示','删除成功','info',function(){
							grid.datagrid('reload');
						});
					}else{
						parent.$.messager.alert('提示','删除失败','info');
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
		//alert(datas);
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要删除的记录','info');
		}else{
			parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/work/worktask/deletes.do', {ids:datas} , function(data) {
						if(data>0){
							parent.$.messager.alert('提示','成功删除'+data+'条记录','info',function(){
								grid.datagrid('reload');
								grid.datagrid('clearChecked');
							});
						}else{
							parent.$.messager.alert('提示','删除失败','info');
						}
					});
				}
			});
		}
	};
	var starttask=function(){
		var taskid='${taskid}';
		var processdetailid='${processdetailid}';
		var processid='${processid}';
		var processorderid='${processorderid}';
		var workstationid=$('#workstation').combobox('getValue');
		$.post(ext.contextPath + '/work/workprocess/starttask.do', {taskid : taskid,workstationid:workstationid,
		processdetailid:processdetailid,processid:processid,processorderid:processorderid}, function(data) {
			if(data==1){
				parent.$.messager.alert('提示','任务成功接受','info',function(){
					setbutton('1');
					$('#workstation').combobox('reload');  
				});
			}else{
				parent.$.messager.alert('提示','任务接受失败','info');
			}
		});
	};
	var endtask=function(){
		var taskid='${taskid}';
		var processdetailid='${processdetailid}';
		var processid='${processid}';
		var workstationid=$('#workstation').combobox('getValue');
		$.post(ext.contextPath + '/work/workprocess/endtask.do', {taskid : taskid,workstationid:workstationid,
		processdetailid:processdetailid,processid:processid}, function(data) {
			if(data==1){
				parent.$.messager.alert('提示','任务已保存','info',function(){
					setbutton('2');
					$('#workstation').combobox('reload');  
				});
			}else{
				parent.$.messager.alert('提示','任务保存失败','info');
			}
		});
	};
	var nexttask=function(){
		var taskid='${taskid}';
		var processdetailid=$('#workprocess').combobox('getValue');
		var processdetailname=$('#workprocess').combobox('getText');
		var processid='${processid}';
		//alert(taskid+"，"+processdetailid+","+processid);
		parent.$.messager.confirm('提示', '您确定要进入\"'+processdetailname+'\"流程？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/work/workprocess/nexttask.do', {taskid : taskid,
				processdetailid:processdetailid,processid:processid}, function(data) {
					if(data==1){
						parent.$.messager.alert('提示','流程已进入下一步，感谢您的操作！','info',function(){
							//setbutton('2');
						});
					}else{
						parent.$.messager.alert('提示','流程更新失败','info');
					}
				});
			}
		});
		
	};
	var setbutton =function(status){
		switch(status){
		case '0':
			$('#startbutton').show();
			$('#endbutton').hide();
			$('#nextprocess').hide();
			$('#nextbutton').hide();
			break;
		case '1':
			$('#startbutton').hide();
			$('#endbutton').show();
			$('#nextprocess').hide();
			$('#nextbutton').hide();
		    break;
		case '2':
			$('#startbutton').hide();
			$('#endbutton').hide();
			$('#nextprocess').show();
			$('#nextbutton').show();
		
		}
	};
	$(function() {
		$('#startbutton').hide();
		$('#endbutton').hide();
		$('#nextprocess').show();
		$('#nextbutton').show();
		$('#workstation').combobox({
			url:ext.contextPath + '/work/workprocess/getworkstation.do?taskid=${taskid}&processdetailid=${processdetailid}&processid=${processid}&processorderid=${processorderid}',
			valueField:'workstationid',
			textField:'workstationname',
			onSelect: function(rec){  
				setbutton(rec._taskstatue);
	        }

		});
		$('#workprocess').combobox({
			url:ext.contextPath + '/work/workprocess/getprocess.do?taskid=${taskid}',
			valueField:'id',
			textField:'name',
		});
	});
	
	function selecttask(workstationid){
		$('#workstation').combobox('select',workstationid);
	};
</script>
</head>
<body  text-align="west">
		<div  class="easyui-panel" title="任务操作" style="padding:5px;height:50%">
			<form method="post" class="form" >
				<table class="table">
					<tr>
						<th>操作工位</th>
						<td>
							<select id="workstation" name="workstation" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,value:''">
							</select>
						</td>
					</tr>
					
				</table>
			</form>
			<div>操作内容</div>
			<div style="padding:5px;" >
				<a id="startbutton" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"   
	        		onclick="starttask()">开始</a>  
	       		<a id="endbutton" class="easyui-linkbutton" data-options="iconCls:'icon-no'"   
	       			onclick="endtask()">完成</a>  
			</div>
		</div>
		<br/>
		<div class="easyui-panel" title="工序走向" style="padding:5px;height:50%" >
			<form method="post" class="form" >
				<table class="table">
					<tr id="nextprocess">
						<th>下一步流程</th>
						<td>
							<select id="workprocess" name="workprocess" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,value:''">
							</select>
						</td>
					</tr>
				</table>
			</form>
			<div>&nbsp</div>
			<div>
	       		<a id="nextbutton" class="easyui-linkbutton" data-options="iconCls:'icon-save'"   
	       			onclick="nexttask()">执行</a>  
     		</div>
		</div>
</body>
</html>