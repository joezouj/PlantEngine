<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,grid) {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/material/materialbom/save.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					$.messager.alert('提示', "保存成功", 'info', function() {
						//刷新列表
						grid.datagrid('reload');
						dialog.dialog('destroy');
					});
				}else if(data.res == 0){
					$.messager.alert('提示', "保存失败", 'info');
				}else{
					$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	}

	function selectMaterial(){
		var dialog = parent.ext.modalDialog({
				title : '选择材料',
				width : 800,
				height : 400,
				closeOnEscape:true,
				url : ext.contextPath + '/material/materialinfo/selectMaterial.do?typename='+encodeURI(encodeURI('成品')),
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectOK();
						if(res!=null){
							dialog.dialog('destroy');
							$("#materialcode").textbox('setValue',(res.materialcode == 'undefined')?'':res.materialcode);
							$("#materialname").textbox('setValue',(res.materialname == 'undefined')?'':res.materialname);
						}
						
					}
				}
				]
			});
	}
	$(function() {
		$("#materialcode").textbox('textbox').bind("click", function () { 
			selectMaterial();
		});
		$('#unit').combobox({ 
			url:ext.contextPath + '/material/materialunit/getJsonUnit.do', 
			valueField:'unit', 
			textField:'unit',
			editable:false,
			panelHeight:'auto'
		});
	});
</script>
</head>
<body>
		<form method="post" class="form">
			<input type="hidden" id="pid" name="pid" value="-1" />
			<input type="hidden" id="num" name="num" value="1" />
			<table class="table">
				<tr>
					<th>物料编码</th>
					<td><input id="materialcode" name="materialcode" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="" /></td>
				</tr>
				<tr>
					<th>物料名称</th>
					<td><input id="materialname" name="materialname" class="easyui-textbox"
						readonly="readonly" value="" /></td>
				</tr>
				<tr>
					<th>单位</th>
					<td>
						<input id="unit" name="unit" class="easyui-combobox" value="" />
					</td>
					
				</tr>
				<tr>
					<th>序号</th>
					<td><input id="ordernumber" name="ordernumber" class="easyui-numberbox"
						data-options="required:true,validType:'isBlank'" value="0" /></td>
				</tr>
				<tr>
					<th>版本</th>
					<td><input id="version" name="version" class="easyui-numberbox"
						data-options="" value="0" /></td>
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
