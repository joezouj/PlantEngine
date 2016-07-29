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
			$.post(ext.contextPath + "/plan/materialdeliver/save.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						parent.$("#mainFrame").attr("src",ext.contextPath+"/plan/materialdeliver/distribute.do?id="+data.id);
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
		$("#workstationname").textbox("textbox").bind("click",function(){
			selectWorkstation();
		});
	});

</script>
</head>
<body>
	<div id="tt">
		<a href="javascript:void(0)" class="icon-save" title="保存" onclick="dosave()"></a>
	</div>
	<div class="easyui-panel" title="新增配送任务" style="padding:10px;"
		data-options="tools:'#tt'">
		<form method="post" class="form">
			<input id="status" name="status" type="hidden" value="0"/>
			<table class="table">
				<tr>
					<th>计划信息</th>
					<td>
						<input id="dailyplanid" name="dailyplanid" type="hidden" value=""/>
						<input id="dailyplan" name="dailyplan" class="easyui-textbox"
						data-options="required:true,validType:'isBlank',editable:false" value="" /></td>
					<th>计划日期</th>
					<td><span id="dailyplandt"></span></td>
				</tr>
				<tr>
					<th>所属工位</th>
					<td>
						<input id="workstationid" name="workstationid" type="hidden" value=""/>
						<input id="workstationname" name="workstationname" class="easyui-textbox" 
						data-options="required:true,validType:'isBlank',editable:false" value="" />
					</td>
					<th>工位编号</th>
					<td><span id="workstationserial"></span></td>
				</tr>
				<tr>
					<th >配送开始时间</th>
					<td ><input id="starttime" name="starttime" class="Wdate" value="${fn:substring(nowdate,0,16) }"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d'})"
							readonly></td>	
					<th >配送截至时间</th>
					<td ><input id="endtime" name="endtime" class="Wdate" value="${fn:substring(nowdate,0,16) }"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'starttime\')}'})"
							readonly></td>				
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3">
						<input id="remark" name="remark" class="easyui-textbox" value="" 
						validtype="length[0,250]" invalidMessage="有效长度0-250" 
						data-options="multiline:true" style="width:100%;height:100px"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
