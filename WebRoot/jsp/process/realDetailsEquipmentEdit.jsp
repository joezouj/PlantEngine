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
			$.post(ext.contextPath + "/process/realDetailsEquipment/update.do", $(".form").serialize(), function(data) {
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
	
	function selectEquipment(){
		var dialog = parent.ext.modalDialog({
				title : '选择材料',
				width : 800,
				height : 480,
				closeOnEscape:true,
				url : ext.contextPath + '/equipment/showEquipmentCardForSelect.do',
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectFun();
						if(res!=null){
							dialog.dialog('destroy');
							$("#equipmentid").val((res.id== 'undefined')?'':res.id);
							$("#equipmentcardid").textbox('setValue',(res.equipmentcardid == 'undefined')?'':res.equipmentcardid);
							
							$("#equipmentname").text((res.equipmentname == 'undefined')?'':res.equipmentname);
							$("#equipmentmodel").text((res.equipmentmodel == 'undefined')?'':res.equipmentmodel);
							$("#equipmentclass").text((res.equipmentclass == 'undefined')?'':res.equipmentclass);
						}
					}
				}
				]
			});
	}
	
	$(function(){
		$("#equipmentcardid").textbox("textbox").bind("click",function(){
			selectEquipment();
		});
	});
</script>
</head>
<body>
		<form method="post" class="form">
			<input type="hidden" name="id" value="${realDetailsEquipment.id}"/>
			<table class="table">
				<tr>
					<th>设备编号</th>
					<td>
						<input id="equipmentid" name="equipmentid" type="hidden" value="${realDetailsEquipment.equipmentid}"/>
						<input id="equipmentcardid" class="easyui-textbox" data-options="required:true,validType:'isBlank',editable:false" 
							value="${realDetailsEquipment.equipment.equipmentcardid}" />
					</td>
				</tr>
				<tr>
					<th>设备名称</th>
					<td>
						<label id="equipmentname">${realDetailsEquipment.equipment.equipmentname}</label>
					</td>
				</tr>
				<tr>
					<th>设备型号</th>
					<td>
						<label id="equipmentmodel">${realDetailsEquipment.equipment.equipmentmodel}</label>
					</td>
				</tr>
				<tr>
					<th>设备类型</th>
					<td>
						<label id="equipmentclass">${realDetailsEquipment.equipment.equipmentclassname}</label>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
