<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	//获取已选的工位id
	function getWids(){
		var wrows= $('#grid').datagrid("getRows");
		var wids="";
		$.each(wrows, function(index, item){
			wids+=item.id+",";
		});
		wids=wids.replace(/,$/gi,"");
		return wids;
	}
	
	function dosave(dialog,grid) {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/work/line/save.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					$.post(ext.contextPath + "/work/line/saveWorkstation.do",{lineid:data.id,wids:getWids()},function(data1){
						top.$.messager.alert('提示', "保存成功", 'info', function() {
							grid.datagrid('reload');
							dialog.dialog('destroy');
						});
					});
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	}
	
	function doadd(){
		$("#wids").val(getWids());
		
		var dialog = parent.ext.modalDialog({
			title : '添加工位',
			height:500,
			url : ext.contextPath + '/work/workstation/showlistForSelects.do?iframeId=lineAdd',
			buttons : [ {
				iconCls:'icon-ok',
				text : '确认',
				handler : function() {
					var data = dialog.find('iframe').get(0).contentWindow.selectFun(dialog, grid);
					if(data.res.length>0){
						for(var i=0;i<data.res.length;i++){
							grid.datagrid('appendRow',{
								id:data.res[i].id,
								serial:data.res[i].serial,
								name:data.res[i].name,
								typename:data.res[i].typename,
								deptname:data.res[i].deptname
							});
						}
					}
					dialog.dialog('destroy');
				}
			} ]
		});
	}
	
	function dodelete(){
		var rows = $('#grid').datagrid('getChecked');
    	if(rows.length==0){
    		top.$.messager.alert("提示","请选择需要删除的数据","info");
    	}else{
    		top.$.messager.confirm("提示", "确定删除这些数据？", function(r) {
    			if (r) {
		    		//复制rows，否则deleteRow的index会出错
			    	var copyRows = [];
			        for ( var j= 0; j < rows.length; j++) {
			        	copyRows.push(rows[j]);
			        }
			        
			    	$.each(copyRows, function(index, item){
			    		$('#grid').datagrid('deleteRow',$('#grid').datagrid('getRowIndex',copyRows[index]));
					});
    			}
    		});
    	}
	}
	
	$(function() {
		$('#p').panel({    
		  width:"100%",
		  height:"300",
		  title: '相关工位',
		  tools: [{    
		    iconCls:'icon-add',    
		    handler:function(){doadd();}    
		  },{    
		    iconCls:'icon-remove',    
		    handler:function(){dodelete();}    
		  }]    
		});  
		
		grid = $('#grid').datagrid({
			title : '',
			striped : true,
			rownumbers : true,
			idField : 'id',
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '160', title : '工位编号', field : 'serial', sortable : true, editor:'textbox',halign:'center'}, 
				{width : '240', title : '工位名称', field : 'name', sortable : true, editor:'textbox',halign:'center'},
				{width : '100', title : '工位类型', field : 'typename', sortable : true, editor:'textbox',halign:'center'},
				{width : '200', title : '所属车间', field : 'deptname', sortable : true, editor:'textbox',halign:'center'} 
			] ]
		});
	});
	
</script>
</head>
<body>
		<input id="wids" name="wids" type="hidden" value="" />
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th>产线编号</th>
					<td><input name="serial" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				<tr>
					<th>产线名称</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				<tr>
					<th>产能</th>
					<td><input id="capacity" name="capacity" class="easyui-numberbox" 
						min="1" max="10000" precision="0" missingMessage="请填写数字"
						data-options="required:true,validType:'isBlank'" value="" autofocus/>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>
						<input name="remark" class="easyui-textbox" style="width:100%;height:100px"  value=""
						data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250" />

					</td>
				</tr>
			</table>
		</form>
		<br/>
		<div id="p">
			<table id="grid" data-options="border:false"></table>
		</div>
</body>
</html>
