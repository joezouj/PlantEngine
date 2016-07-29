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
			$.post(ext.contextPath + "/plan/materialrecover/save.do", $(".form").serialize(), function(data) {
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
	
	function selectDailyplan(){
		var dialog = parent.ext.modalDialog({
				title : '选择日计划',
				width : 800,
				height : 480,
				closeOnEscape:true,
				url : ext.contextPath + '/plan/dailyplansummary/select.do',
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectOK();
						if(res!=null){
								$("#dailyplanid").val((res.id == 'undefined')?'':res.id);
								$("#dailyplan").textbox('setValue',(res.name == 'undefined')?'':res.name);
								$("#dailyplandt").html((res.plandt == 'undefined')?'':res.plandt.substring(0,10));
						}
						dialog.dialog('destroy');
					}
				}
				]
			});
	}
	
	function selectMaterial(){
		var dialog = parent.ext.modalDialog({
			title : '选择材料',
			width : 800,
			height : 400,
			closeOnEscape:true,
			url : ext.contextPath + '/material/materialinfo/selectMaterial.do?typename=1',
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
					}
					
				}
			}
			]
		});
	}
	
	function selectWorkstation(){
		var dialog = parent.ext.modalDialog({
			title : '选择工位',
			width : 800,
			height : 480,
			resizable:true,
			closeOnEscape:true,
			url : ext.contextPath + '/work/workstation/showlistForSelect.do',
			buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectFun();
					if(res!=null){
						dialog.dialog('destroy');
						$("#workstationid").val((res.id== 'undefined')?'':res.id);
						$("#workstationname").textbox('setValue',(res.name == 'undefined')?'':res.name);
						$("#workstationserial").html((res.serial == 'undefined')?'':res.serial);
					}
				}
			}
			]
		});
	}
	
	$(function() {
		$("#dailyplan").textbox("textbox").bind("click",function(){
			selectDailyplan();
		});
		$("#materialcode").textbox("textbox").bind("click",function(){
			selectMaterial();
		});
		$("#workstationname").textbox("textbox").bind("click",function(){
			selectWorkstation();
		});
	});

</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th>计划信息</th>
					<td>
						<input id="dailyplanid" name="dailyplanid" type="hidden" value=""/>
						<input id="dailyplan" name="dailyplan" class="easyui-textbox"
						data-options="required:true,validType:'isBlank',editable:false" value="" /></td>
				</tr>
				<tr>
					<th>计划日期</th>
					<td><span id="dailyplandt"></span></td>
				</tr>
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
					<th>所属工位</th>
					<td>
						<input id="workstationid" name="workstationid" type="hidden" value=""/>
						<input id="workstationname" name="workstationname" class="easyui-textbox" 
						data-options="required:true,validType:'isBlank',editable:false" value="" />
					</td>
				</tr>
				<tr>
					<th>工位编号</th>
					<td><span id="workstationserial"></span></td>
				</tr>
				<tr>
					<th>回收量</th>
					<td>
						<input id="recovernum" name="recovernum" class="easyui-numberbox" 
						min="1" max="10000" precision="0" required="true" missingMessage="请填写数字"
						data-options="required:true,validType:'isBlank'" value="" autofocus/>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td >
						<input id="memo" name="memo"class="easyui-textbox" style="width:100%;height:100px" 
						 value=""data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
