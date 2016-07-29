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
			$.post(ext.contextPath + "/process/realDetailsMaterial/save.do", $(".form").serialize(), function(data) {
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
	
	function selectMaterial(){
		var dialog = parent.ext.modalDialog({
				title : '选择材料',
				width : 800,
				height : 480,
				closeOnEscape:true,
				url : ext.contextPath + '/material/materialinfo/selectBomMaterial.do?productcode=${productcode}',
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectOK();
						if(res!=null){
							dialog.dialog('destroy');
							$("#materialid").val((res.materialid== 'undefined')?'':res.materialid);
							$("#materialcode").textbox('setValue',(res.materialcode == 'undefined')?'':res.materialcode);
							
							$("#materialname").text((res.materialname == 'undefined')?'':res.materialname);
							$("#materialspec").text((res.spec == 'undefined')?'':res.spec);
							$("#unit").text((res.unit == 'undefined')?'':res.unit);
						}
					}
				}
				]
			});
	}
	
	$(function(){
		$("#materialcode").textbox("textbox").bind("click",function(){
			selectMaterial();
		});
	});
</script>
</head>
<body>
		<form method="post" class="form">
			<input type="hidden" name="pid" value="${param.pid}"/>
			<input type="hidden" name="productcode" value="${productcode}"/>
			<table class="table">
				<tr>
					<th>物料编码</th>
					<td>
						<input id="materialid" name="materialid" type="hidden" />
						<input id="materialcode" class="easyui-textbox" data-options="required:true,validType:'isBlank',editable:false" value="" />
					</td>
				</tr>
				<tr>
					<th>物料名称</th>
					<td>
						<label id="materialname"></label>
					</td>
				</tr>
				<tr>
					<th>物料规格</th>
					<td>
						<label id="materialspec"></label>
					</td>
				</tr>
				<tr>
					<th>物料数量</th>
					<td>
						<input id="amount" name="amount" class="easyui-textbox"
						min="1" max="10000" precision="0" required="true" missingMessage="请填写数字"
						data-options="required:true,validType:'isBlank'" value="" autofocus/>
						<label id="unit"></label>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
