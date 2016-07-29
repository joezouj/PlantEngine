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
	function dosave(dialog) {
		if($("#auditor").val()!=null && $("#auditor").val()!=""){
			$("#status").val("3");
		}
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/plan/dailyplansummary/auditsave.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
						top.$.messager.alert('提示', "审核成功", 'info', function() {
								//刷新
								parent.$('#viewp3')[0].contentWindow.location.reload();
								refreshbutton();
								dialog.dialog('destroy');
							});				
					
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
					dialog.dialog('destroy');
				}
			},'json');
		}
	}
	function refreshbutton(){		
		parent.$('#viewp3')[0].contentWindow.$('#audittd').hide();
		parent.$('#viewp3')[0].contentWindow.$('#disttd').show();
	}
</script>
</head>
<body>
		<form method="post" class="form">
		<input id="id" name="id" type="hidden" value="${dailyplansummary.id }"/>
		<input id="status" name="status" type="hidden" value="${dailyplansummary.status }"/>
			<table class="table">
				<tr>
					<th >审核人</th>
					<td ><input id="auditorname" name="auditorname" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;" value="${cuname }" readonly/>
					<input id="auditor" name="auditor" type="hidden" value="${cuid}"/>
					</td>
					<script type="text/javascript">
// 						$(function() {
// 							$("#auditorname").textbox('textbox').bind("click", function () { 
// 								selectSingleUser("auditorname","auditor","${param.iframeId}");
// 							});
// 						});
					</script>				
				</tr>				
				<tr>
					<th >审核日期</th>
					<td ><input id="auditdate" name="auditdate" class="Wdate" value="<c:if test='${dailyplansummary.auditdate!=null }'>${fn:substring(dailyplansummary.auditdate,0,19)}</c:if><c:if test='${dailyplansummary.auditdate==null }'>${nowdate }</c:if>"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
							readonly></td>				
				</tr>
				<tr>
					<th >备注</th>
					<td ><input id="remark" name="remark" class="easyui-textbox"	data-options="multiline:true" style="overflow:auto;width:100%;height:80px;" value="${dailyplansummary.remark}" /></td>				
				</tr>	
				
			</table>
		
	</form>
</body>
</html>
