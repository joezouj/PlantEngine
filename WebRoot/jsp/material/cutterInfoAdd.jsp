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
			$.post(ext.contextPath + "/material/cutterinfo/save.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						//刷新列表
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
		$('#tname').combotree({
			url : ext.contextPath + '/material/cuttertype/getMenusJsonActive.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			required:true,
			onBeforeSelect:function(node){
				if(node.id=="-1"){
                    $("#tname").tree("unselect");
                }
            },
			onClick : function(node) {
					$("#typeid").val(node.id);
			}
		});
		
		$('#posname').combotree({
			url : ext.contextPath + '/material/cutterposition/getMenusJsonActive.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			required:true,
			onBeforeSelect:function(node){
				if(node.id=="-1"){
                    $("#posname").tree("unselect");
                }
            },
			onClick : function(node) {
					$("#positionid").val(node.id);
			}
		});
	});

</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th>刀具编码</th>
					<td><input id="cuttercode" name="cuttercode" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" /></td>
				</tr>
				<tr>
					<th>刀具名称</th>
					<td><input name="cuttername" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" /></td>
				</tr>
				<tr>
					<th>类型</th>
					<td>
					<select id="tname" name="tname" class="easyui-combotree" value="" ></select>
					<input id="typeid" name="typeid" type="hidden" value=""/>
					</td>
				</tr>
				<tr>
					<th>位置</th>
					<td>
					<select id="posname" name="posname" class="easyui-combotree" value="" ></select>
					<input id="positionid" name="positionid" type="hidden" value=""/>
					</td>
				</tr>
				<tr>
					<th>使用寿命</th>
					<td><input name="life" class="easyui-textbox" value="" /> h</td>
				</tr>
				<tr>
					<th>长度</th>
					<td>
						<input id="length" name="length" class="easyui-textbox" value="" /> mm
					</td>
				</tr>
				<tr>
					<th>宽度</th>
					<td>
						<input id="width" name="width" class="easyui-textbox" value="" /> mm
					</td>
				</tr>
				<tr>
					<th>厚度</th>
					<td>
						<input id="ply" name="ply" class="easyui-textbox" value="" /> mm
					</td>
				</tr>
				<tr>
					<th>供应商</th>
					<td>
						<input id="producer" name="producer" class="easyui-textbox" value="" />
					</td>
				</tr>
				<tr>
					<th>状态</th>
					<td>
						<select name="status" class="easyui-combobox" data-options="panelHeight:'auto',editable:false">
							<option value="1" selected >启用</option>
							<option value="0">禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>
						<input name="remark" class="easyui-textbox" style="width:100%;height:100px"  
						value=""data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
