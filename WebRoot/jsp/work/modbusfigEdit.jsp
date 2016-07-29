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
			$.post(ext.contextPath + "/work/modbusfig/update.do", $(".form").serialize(), function(data) {
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
		
	});

</script>
</head>
<body>
		<form method="post" class="form">
			<input type="hidden" name="id" value="${modbusFig.id}"/>
			<table class="table">
				<tr>
					<th>服务器名称</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${modbusFig.name}" />
					</td>
					<th>启用</th>
					<td>
						<select name="flag" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,value:'${modbusFig.flag}'">
							<option value="启用">启用</option>
							<option value="禁用">禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>服务器IP</th>
					<td><input name="ipsever" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${modbusFig.ipsever}" />
					</td>
					<th>端口号</th>
					<td><input name="port" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${modbusFig.port}" />
					</td>
				</tr>
				<tr>
					<th>站地址</th>
					<td><input name="slaveid" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${modbusFig.slaveid}" />
					</td>
					<th>数据高低位存储方式</th>
					<td>
						<select name="order32" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,value:'${modbusFig.order32}'">
							<option  value="4321">FFH4 FFH3 FFH2 FFH1</option>
							<option  value="3412">FFH3 FFH4 FFH1 FFH2</option>
							<option  value="1234">FFH1 FFH2 FFH3 FFH4</option>
							<option  value="2143">FFH2 FFH1 FFH4 FFH3</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3">
						<textarea name="remarks" class="easyui-textbox" data-options="multiline:true"
							style="width:100%;height:100px" validtype="length[0,250]" invalidMessage="有效长度0-250">${modbusFig.remarks}</textarea>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
