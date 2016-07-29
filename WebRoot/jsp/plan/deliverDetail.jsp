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
	var id = $("#id").val();
	var deliveramount = Number($("#deliveramount").val());
	var boxnumber = Number($("#boxnumber").val());
	var boxname = $("#boxname").val();
	var planamount = '${deliverDetail.planamount}';
	if(planamount>=deliveramount){
		$.post(ext.contextPath + "/plan/deliverdetail/deliverupdate.do", {id:id,deliveramount:deliveramount,boxnumber:boxnumber,boxname:boxname}, function(data) {
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
	}else{
		top.$.messager.alert('提示', "配送量需已超过计划量，请核实数据!",'info');
		$('#deliveramount').next('span').find('input').focus();
	}

}

function selectMaterialBox(){
	var dialog = parent.ext.modalDialog({
		title : '选择料盒',
		width : 600,
		height : 400,
		closeOnEscape:true,
		url : ext.contextPath + '/material/materialbox/selectMaterialBox.do?querytype=select',
		buttons : [{
			text : '确定',
			iconCls:'icon-ok',
			handler : function() {
				var res=dialog.find('iframe').get(0).contentWindow.selectOK();
				if(res!=null){
					dialog.dialog('destroy');
					$("#boxnumber").textbox('setValue',(res.boxnumber == 'undefined')?'':res.boxnumber);
					$("#boxname").html((res.boxname == 'undefined')?'':res.boxname);
				}
				
			}
		}
		]
	});
}

$(function() {
	$("#boxnumber").textbox("textbox").bind("click",function(){
		selectMaterialBox();
	});
});
</script>
</head>
<body>
		<form method="post" class="form">
			<input id="id" name="id" type="hidden" value="${deliverDetail.id }"/>
			<table class="table">
				<tr>
					<th>物料名称</th>
					<td>${deliverDetail.materialInfo.materialname }</td>
					<th>物料编码</th>
					<td>${deliverDetail.materialInfo.materialcode }</td>
				</tr>
				<tr>
					<th>计划配送量</th>
					<td>${deliverDetail.planamount}${deliverDetail.materialInfo.unit }</td>
					<th>配送量</th>
					<td>
					<input id="deliveramount" name="deliveramount" class="easyui-textbox" value="${deliverDetail.deliveramount}" autofocus/>
					${deliverDetail.materialInfo.unit }</td>
				</tr>
				<tr>
					<th>料盒编号</th>
					<td><input id="boxnumber" name="boxnumber" class="easyui-textbox"
						data-options="required:true,validType:'isBlank',editable:false" value="${deliverDetail.boxnumber }" /></td>
					<th>料盒名称</th>
					<td><span id="boxname">${deliverDetail.boxname }</span></td>
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3">
						<input id="remark" name="remark" class="easyui-textbox" value="${deliverDetail.remark }" 
						data-options="multiline:true" style="width:100%;height:100px"/>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
