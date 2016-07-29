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
			$.post(ext.contextPath + "/process/realDetailsWorkstation/update.do", $(".form").serialize(), function(data) {
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
	
	function selectWorkStation(){
		var dialog = parent.ext.modalDialog({
				title : '选择材料',
				width : 800,
				height : 480,
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
							
							$("#workstationserial").textbox('setValue',(res.serial == 'undefined')?'':res.serial);
							
							$("#workstationname").text((res.name == 'undefined')?'':res.name);
							$("#typename").text((res.typename == 'undefined')?'':res.typename);
							$("#deptname").text((res.deptname == 'undefined')?'':res.deptname);
							$("#linename").text((res.linename == 'undefined')?'':res.linename);
						}
					}
				}
				]
			});
	}
	
	$(function(){
		$("#workstationserial").textbox("textbox").bind("click",function(){
			selectWorkStation();
		});
	});
</script>
</head>
<body>
		<form method="post" class="form">
			<input type="hidden" name="id" value="${realDetailsWorkstation.id}"/>
			<table class="table">
				<tr>
					<th>工位编号</th>
					<td>
						<input id="workstationid" name="workstationid" type="hidden" value="${realDetailsWorkstation.id}" />
						<input id="workstationserial" name="workstationserial" class="easyui-textbox" data-options="required:true,validType:'isBlank',editable:false" value="${realDetailsWorkstation.workstation.serial}" />
					</td>
				</tr>
				<tr>
					<th>工位名称</th>
					<td>
						<label id="workstationname">${realDetailsWorkstation.workstation.name}</label>
					</td>
				</tr>
				<tr>
					<th>工位类型</th>
					<td>
						<label id="typename">${realDetailsWorkstation.workstation.typename}</label>
					</td>
				</tr>
				<tr>
					<th>所属车间</th>
					<td>
						<label id="deptname">${realDetailsWorkstation.workstation.deptname}</label>
					</td>
				</tr>
				<tr>
					<th>所属产线</th>
					<td>
						<label id="linename">${realDetailsWorkstation.workstation.linename}</label>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
