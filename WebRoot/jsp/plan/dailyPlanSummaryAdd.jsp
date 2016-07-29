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
			$.post(ext.contextPath + "/plan/dailyplansummary/save.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
						top.$.messager.alert('提示', "保存成功", 'info', function() {	
								doLook('${param.date}','${param.sdt}','${param.edt}',data.id);
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
	var doLook = function(date,sdt,edt,id){
		var nowdate = new Date();
		var thisdate=new Date(date);
		var olddayflag=0;
		if(thisdate<nowdate &&thisdate.getDate()!=nowdate.getDate()){
			olddayflag=1;
		}
		var dialog = parent.ext.modalDialog({
 			id:'viewDialog1',
 			iframeId:'view',
 			title : '查看计划',	
 			height:"90%",
 			width:"90%",
 			url : ext.contextPath + '/plan/dailyplansummary/showlist.do?sdt='+sdt+'&edt='+edt+'&iframeId=view&olddayflag='+olddayflag+'&id='+id
 			
 		});
	};
</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th >日计划单号</th>
					<td ><input id="name" name="name" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;" value="${autoname }" /></td>				
				</tr>				
				<tr>
					<th >日计划日期</th>
					<td ><input id="plandt" name="plandt"  class="easyui-textbox" value="${param.date }"
							readonly></td>				
				</tr>
				<tr>
					<th >备注</th>
					<td ><input  id="remark" name="remark" class="easyui-textbox" style="overflow:auto;width:100%;height:80px;" 
					 value="" data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250" />
					 </td>				
				</tr>	
				
			</table>
		
	</form>
</body>
</html>
