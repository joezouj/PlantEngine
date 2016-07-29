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
				<input id="status" name="status" type="hidden" value="1" />				
				<tr>
					<th >单号</th>
					<td >${materialrequisition.orderno}</td>				
				</tr>				
				<tr>
					<th >工位编号</th>
					<td >
					${materialrequisition.workstation.serial}					
					</td>				
				</tr>				
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
					${materialrequisition.materialinfo.materialcode}
					</td>				
				</tr>				
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
						${materialrequisition.quantity}
						<input id="requirequantity" name="requirequantity" type="hidden" value="${materialrequisition.quantity}"/>
					</td>				
				</tr>
				<tr>
					<th >要求发料日期</th>
					<td >${fn:substring(materialrequisition.requestsenddate,0,16)}</td>				
				</tr>
<!-- 				<tr> -->
<!-- 					<th >物料申领人</th> -->
<!-- 					<td > -->
<!-- 					<input id="applyusername" name="applyusername" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;" value="${cuid}" /> -->
<!-- 					<input id="applyuserid" name="applyuserid" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;" value="${cuid}" /> -->
<!-- 					</td>				 -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<th >备注</th> -->
<!-- 					<td ><input id="remark" name="remark" class="easyui-textbox"	data-options="multiline:true" style="overflow:auto;width:100%;height:80px;" value="" /></td>				 -->
<!-- 				</tr>	 -->
				
			</table>
			<br />
			<table class="table">
					<tr><th >发料人</th>
						<td>
							<input id="delivermanname" name="delivermanname" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;" value="${delivermanname}" /> 
							<input id="deliverman" name="deliverman" type="hidden"  value="${materialrequisition.deliverman}"/>
					</td></tr>
					<script type="text/javascript">
						$(function() {
							$("#delivermanname").textbox('textbox').bind("click", function () { 
								selectSingleUser("delivermanname","deliverman","${param.iframeId}");
							});
						});
					</script>
					<tr>
						<th >发料日期</th>
						<td ><input id="deliverddate" name="deliverdate" class="Wdate" value="${nowdate }"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
							readonly></td>				
					</tr>
					<tr><th >实发数量</th>
					<td><input id="deliverquantity" name="deliverquantity" class="easyui-numberspinner" data-options="min:0,max:9999,prompt:'0-9999',required:true,validType:'isBlank'" style="width:130px;" value='${materialrequisition.deliverquantity}'/></td></tr>
			</table>	
		
	</form>
</body>
</html>
