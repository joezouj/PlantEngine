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
		var undeliveramount = Number($("#undeliveramount").val());
		var planamount = Number($("#planamount").val());
		if(planamount>undeliveramount){
			top.$.messager.alert('提示', "计划配送量已超过当日可未分配量,请修改数据!",'info');
			$('#planamount').focus();
		}else if(planamount<=0){
			top.$.messager.alert('提示', "计划配送量需大于0!",'info');
			$('#planamount').next('span').find('input').focus();
		}else{
			if ($(".form").form('validate')) {
				$.post(ext.contextPath + "/plan/deliverdetail/save.do", $(".form").serialize(), function(data) {
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
		
	}
	function selectMaterial(){
		var dialog = parent.ext.modalDialog({
			title : '选择材料',
			width : 600,
			height : 400,
			closeOnEscape:true,
			url : ext.contextPath + '/plan/materialplan/materialPlanForSelect.do?pid=${pid}&planid=${planid}&workstationid=${workstationid}',
			buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectOK();
					if(res!=null){
						dialog.dialog('destroy');
						$("#materialid").val((res.materialid == 'undefined')?'':res.materialid);
						$("#materialcode").textbox('setValue',(res.materialcode == 'undefined')?'':res.materialcode);
						$("#materialname").html((res.materialname == 'undefined')?'':res.materialname);
						$("#materialunit").html((res.materialunit == 'undefined')?'':res.materialunit);
						
						$.ajax({
							type:'post',
							url: ext.contextPath + '/plan/deliverdetail/getPlanedAmount.do?planid=${planid}&materialid='+$("#materialid").val()+'&random='+Math.random(),
							data:'',
							success: function (data) {
								if(data!=null){
									var obj = $.parseJSON(data);
									var planamount = obj.deliverDetail[0].planamount;
									var undeliveramount = (res.materialamount == 'undefined')?(0-planamount):(res.materialamount-planamount);
									$("#undeliveramount").val(undeliveramount);
									$("#undeliverInfo").html("尚未分配物料数量："+undeliveramount+$("#materialunit").val());
								}
							}
						});
					}
					
				}
			}
			]
		});
	}

	$(function() {
		$("#materialcode").textbox("textbox").bind("click",function(){
			selectMaterial();
		});
		
	});

</script>
</head>
<body>
		<form method="post" class="form">
			<input id="pid" name="pid" type="hidden" value="${pid }"/>
			<table class="table">
				<tr>
					<th>物料编码</th>
					<td>
						<input id="materialid" name="materialid" type="hidden" value=""/>
						<input id="materialcode" name="materialcode" class="easyui-textbox"
						data-options="required:true,validType:'isBlank',editable:false" value="" /></td>
				</tr>
				<tr>
					<th>物料名称</th>
					<td><span id="materialname"></span></td>
				</tr>
				<tr>
					<th>计划配送量</th>
					<td>
						<input id="planamount" name="planamount" class="easyui-numberbox" 
						min="0.01" max="10000" precision="2" required="true" missingMessage="必须填写数字"
						data-options="required:true,validType:'isBlank'" value="" autofocus/>
						<input id="undeliveramount" name="undeliveramount" type="hidden" value="" />
						<span id="materialunit"></span>
						<span id="undeliverInfo"  style="float:none"></span>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td >
						<input name="remark" class="easyui-textbox" style="width:100%;height:100px"  
						value=""data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
