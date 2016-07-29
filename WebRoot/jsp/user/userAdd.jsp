<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,grid) {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/user/saveUser.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					var resmsg="";
					//保存职位
					var jobval = String($('#_jobs').combobox('getValues'));
					$.post(ext.contextPath + "/user/updateJobByUserid.do",{jobstr:jobval,userid:data.id,unitid:$('#pid').val()},function(data1){
						if(data1<0){
							resmsg="职位保存失败 ";
						}
					});
					//保存权限
					var val = String($('#_roles').combobox('getValues'));
					$.post(ext.contextPath + "/user/updateRoleUser.do",{rolestr:val,userid:data.id},function(data1){
						if(data1<0){
							resmsg+="权限保存失败";
						}
					});
					if(resmsg.indexOf("失败")>0){
						top.$.messager.alert('提示', resmsg, 'info');
					}else{
						top.$.messager.alert('提示', "保存成功", 'info',function(){
							grid.datagrid('reload');
							dialog.dialog('destroy');
						});
					}
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	}
	
	$(function() {
		$('#pname').combotree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			onBeforeSelect:function(node){
				if(node.id=="-1"){
                    $("#pname").tree("unselect");
                }
            },
			onClick : function(node) {
				$("#pid").val(node.id);
			}
		});
		
		$('#_jobs').combobox({ 
			url:ext.contextPath + '/user/getJsonJob.do', 
			valueField:'id', 
			textField:'name',
			multiple:true,
			editable:false
		});
		
		$('#_roles').combobox({ 
			url:ext.contextPath + '/user/getJsonRole.do', 
			valueField:'id', 
			textField:'name',
			required:true,
			multiple:true,
			editable:false
		}); 
	});

</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th>登录名</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" /></td>
				</tr>
				<tr>
					<th>姓名</th>
					<td><input name="caption" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value=""/></td>
				</tr>
				<tr>
					<th>工号</th>
					<td><input name="serial" class="easyui-textbox" value="" /></td>
				</tr>
				<tr>
					<th>卡号</th>
					<td><input name="cardid" class="easyui-textbox" value="" /></td>
				</tr>
				<tr>
					<th>性别</th>
					<td>
						<select name="sex" class="easyui-combobox" data-options="panelHeight:'auto',editable:false">
							<option value="">请选择</option>
							<option value="1">男</option>
							<option value="0">女</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>部门</th>
					<td>
						<input id="pname" name="pname" class="easyui-combotree" value="" />
						<input id="pid" name="pid" type="hidden" value=""/>
					</td>
				</tr>
				<tr>
					<th>职位</th>
					<td>
						<input id="_jobs" name="_jobs" class="easyui-combobox" value="" />
					</td>
				</tr>
				<tr>
					<th>权限</th>
					<td>
						<input id="_roles" name="_roles" class="easyui-combobox" style="width:300px" value=""/>
					</td> 
				</tr>
				<tr>
					<th>用户状态</th>
					<td>
						<select name="active" class="easyui-combobox" data-options="panelHeight:'auto',editable:false">
							<option value="1">启用</option>
							<option value="0">禁用</option>
							<option value="3">离职</option>
							<option value="2">退休</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>顺序</th>
					<td><input name="morder" class="easyui-textbox" value="0" /></td>
				</tr>
			</table>
		</form>
</body>
</html>
