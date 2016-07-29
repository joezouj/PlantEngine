<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,grid) {		
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/material/materialrequisition/update.do", $(".form").serialize(), function(data) {
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
function selectWorkstation(workstationno,workstationid){  
	var dialog = parent.ext.modalDialog({
			title : '选择工位',
			width: 600,
			height:420,
			closeOnEscape:true,
			url : ext.contextPath + '/work/workstation/showlistForSelect.do',
			toolbar:'',
			buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectFun();
					if(res!=null){
						dialog.dialog('destroy');
						$("#"+workstationno).textbox("setValue",res.serial);
						$("#"+workstationid).val(res.id);
						$("#workstationname").html(res.name);
						$("#workstationshop").html(res.deptname);
						$("#workstationline").html(res.linename);
					}
					
				}
			},
			{
				text : '取消',
				iconCls:'icon-cancel',
				handler : function() {
//					dialog.find('iframe').get(0).contentWindow.selectCancel();
					dialog.dialog('destroy');
									
				}
			}
			]
		});
}
function selectMaterial(materialcode,materialid){ 	  
	var dialog = parent.ext.modalDialog({
			title : '选择物料',
			width: 600,
			height:420,
			closeOnEscape:true,
			url : ext.contextPath + '/material/materialinfo/selectMaterial.do',
			toolbar:'',
			buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectOK();
					if(res!=null){
						dialog.dialog('destroy');
						$("#"+materialcode).textbox("setValue",res.materialcode);
						$("#"+materialid).val(res.materialid);
						$("#materialname").html(res.materialname);
						$("#materialspec").html(res.spec);
						$("#materialunit").html(res.unit);
					}
					
				}
			},
			{
				text : '取消',
				iconCls:'icon-cancel',
				handler : function() {
					//dialog.find('iframe').get(0).contentWindow.selectCancel();
					dialog.dialog('destroy');
									
				}
			}
			]
		});
}
</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<input id="id" name="id" type="hidden" value="${materialrequisition.id}" />				
				<tr>
					<th >单号</th>
					<td ><input id="orderno" name="orderno" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;" value="${materialrequisition.orderno}" /></td>				
				</tr>				
				<tr>
					<th >工位编号</th>
					<td >
					<input id="workstationno" name="workstationno" class="easyui-textbox" data-options="required:true,validType:'isBlank',prompt:'单击选择'" value="${materialrequisition.workstation.serial}" readonly/>
					<input id="workstationid" name="workstationid" type="hidden" value="${materialrequisition.workstation.id}" />
					</td>				
				</tr>
				<script type="text/javascript">
						$(function() {							
							$("#workstationno").textbox('textbox').bind("click", function () { 
								selectWorkstation("workstationno","workstationid");
							})
						})
				</script>
				<tr>
					<th >工位名称</th>
					<td ><span id="workstationname">${materialrequisition.workstation.name}</span></td>
				</tr>
				<tr>
					<th >所属车间</th>
					<td ><span id="workstationshop">${materialrequisition.workstation.linename}</span></td>
				</tr>
				<tr>
					<th >所属产线</th>
					<td ><span id="workstationline">${materialrequisition.workstation.deptname}</span></td>
				</tr>
				<tr>
					<th >物料编码</th>
					<td >
					<input id="materialcode" name="materialcode" class="easyui-textbox" data-options="required:true,validType:'isBlank',prompt:'单击选择'" value="${materialrequisition.materialinfo.materialcode}" readonly/>
					<input id="materialid" name="materialid" type="hidden" value="${materialrequisition.materialinfo.id}" />
					</td>				
				</tr>
				<script type="text/javascript">
						$(function() {							
							$("#materialcode").textbox('textbox').bind("click", function () { 
								selectMaterial("materialcode","materialid");
							})
						})
				</script>
				<tr>
					<th >物料名称</th>
					<td ><span id="materialname">${materialrequisition.materialinfo.materialname}</span></td>				
				</tr>				
				<tr>
					<th >物料规格</th>
					<td ><span id="materialspec">${materialrequisition.materialinfo.spec}</span></td>
				</tr>
				<tr>
					<th >物料单位</th>
					<td ><span id="materialunit">${materialrequisition.materialinfo.unit}</span></td>
				</tr>
				<tr>
					<th >申领物料数量</th>
					<td >
						<input id="quantity" name="quantity" class="easyui-numberspinner" data-options="min:1,max:999,required:true,validType:'isBlank',prompt:'1-999'" style="width:130px;" value='${materialrequisition.quantity}'/>
					</td>				
				</tr>
				<tr>
					<th >要求发料日期</th>
					<td ><input id="requestsenddate" name="requestsenddate" class="Wdate" data-options="required:true,validType:'isBlank'"
						value="${fn:substring(materialrequisition.requestsenddate,0,16)}"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d'})"
							readonly></td>				
				</tr>
			</table>				
		
	</form>
</body>
</html>
