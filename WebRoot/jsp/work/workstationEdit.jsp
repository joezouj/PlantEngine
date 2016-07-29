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
			$.post(ext.contextPath + "/work/workstation/update.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						grid.datagrid('reload');
						dialog.dialog('destroy');
					});
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	}
	
	$(function() {
		$('#deptid').combotree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			onBeforeSelect:function(node){
				if(node.id=="-1"){
                    $("#deptid").combotree("unselect");
                }
            },
            onLoadSuccess:function(){
				$('#deptid').combotree('setValue','${workstation.deptid}');
			}
		});
		
		$('#typeid').combobox({
			url : ext.contextPath + '/work/workstationType/getselectlist.do?random=' + Math.random(),
			valueField:'id', 
			textField:'name',
			method:'get',
			onLoadSuccess:function(){
				$('#typeid').combobox('setValue','${workstation.typeid}');
			}
		});
	});

</script>
</head>
<body>
		<form method="post" class="form">
		<input name="id" type="hidden" value="${workstation.id}"/>
			<table class="table">
				<tr>
					<th>工位编号</th>
					<td><input name="serial" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${workstation.serial}" />
					</td>
				</tr>
				<tr>
					<th>工位名称</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${workstation.name}" />
					</td>
				</tr>
				<tr>
					<th>工位类型</th>
					<td><input id="typeid" name="typeid" class="easyui-combobox"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				<tr>
					<th>所属车间</th>
					<td><input id="deptid" name="deptid" class="easyui-combobox"
						data-options="required:true,validType:'isBlank'" />
					</td>
				</tr>
				<tr>
					<th>工位概述</th>
					<td>
						<textarea name="intro" class="easyui-textbox" data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250"
							style="width:100%;height:100px">${workstation.intro}</textarea>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
