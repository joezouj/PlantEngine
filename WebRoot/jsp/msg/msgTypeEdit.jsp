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
		if($('#pid').combobox('getValue')==""){			
 			alert("类型种类为必填项，不可为空");
			return 0;
		}	
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/msg/updateMsgType.do", $(".form").serialize(), function(data) {
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


function selectUserRoles(rolename,roleid){
	var dialog = parent.ext.modalDialog({
			title : '选择接收角色',
			width: 600,
			height:420,
			closeOnEscape:true,
			url : ext.contextPath + '/user/roleForSelect.do?roleids='+$("#"+roleid).val(),
			buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectOK();
					if(res!=null){
						dialog.dialog('destroy');						
						$("#"+rolename).textbox('setValue',res.split(")")[1]);//easyui textbox赋值jquery不一样
						$("#"+roleid).val(res.split(")")[0]);	
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
$(function(){
	$('#pid').combobox('setValue', '${msgType.pid}');
	$('#sendway').combobox('setValue', '${msgType.sendway}');
});
</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<tr>
						<th >类型名称</th>
						<td ><input id="name" name="name" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style="overflow:auto;width:100%;" value="${msgType.name}" /></td>				
				</tr>
				<tr>
						<th >ID</th>
						<td ><input id="id" name="id" class="easyui-textbox"	 data-options="required:true,validType:'isBlank',readonly:true" style="overflow:auto;width:100%;" value="${msgType.id}" /></td>				
				</tr>
				<tr>
						<th >类型种类</th>
						<td >
						<select id="pid" name="pid" class="easyui-combobox" style="width:152px;">
							<option value='' >请选择</option>
							<option value='personal' >个人消息</option>
							<option value='system' >系统消息</option>										
						</select>
						</td>				
				</tr>
				<tr>
						<th >接收角色</th>
						<td ><input id="msgrole-name" name="msgrole-name" class="easyui-textbox"	data-options="prompt:'内容空默认全部',multiline:true,readonly:true" style="overflow:auto;width:100%;height:80px;" value="${msgrolename}" />
						<input id="msgroleids" name="msgroleids" type="hidden" value="${msgroleids}">
						<script type="text/javascript">
						$(function() {							
							$("#msgrole-name").textbox('textbox').bind("click", function () { 
								selectUserRoles("msgrole-name","msgroleids");
							})
						})
					</script></td>				
				</tr>				
				<tr>
						<th >消息发送用户</th>
						<td ><input id="msguser-name" name="msguser-name" class="easyui-textbox"	data-options="prompt:'内容空默认全部',multiline:true,readonly:true" style="overflow:auto;width:100%;height:80px;" value="${msgusername}" />
						<input id="msguserids" name="msguserids" type="hidden" value="${msguserids}">
						<input id="msguseridname" name="msguseridname" type="hidden" value="${msguseridname}">
						<script type="text/javascript">
						$(function() {							
							$("#msguser-name").textbox('textbox').bind("click", function () { 
								selectUsers("msguser-name","msguserids","${param.iframeId}");
							})
						})
					</script></td>				
				</tr>
				<tr>
						<th >短信发送用户</th>
						<td ><input id="smsuser-name" name="smsuser-name" class="easyui-textbox"	data-options="prompt:'内容空默认全部',multiline:true,readonly:true" style="overflow:auto;width:100%;height:80px;" value="${smsusername}" />
						<input id="smsuserids" name="smsuserids" type="hidden" value="${smsuserids}">
						<input id="smsuseridname" name="smsuseridname" type="hidden" value="${smsuseridname}">
						<script type="text/javascript">
						$(function() {							
							$("#smsuser-name").textbox('textbox').bind("click", function () { 
								selectUsers("smsuser-name","smsuserids","${param.iframeId}");
							})
						})
					</script></td>										
				</tr>
				<tr>
					<th >发送方式</th>
					<td >
						<select id="sendway" name="sendway" class="easyui-combobox" style="width:152px;">
							<option value='msg'>仅消息发送</option>
							<option value='sms'>仅短信发送</option>
							<option value='both'>消息+短信发送</option>										
						</select>
						</td>				
				</tr>
				<tr>
						<th >备注</th>
						<td ><input id="remark" name="remark" class="easyui-textbox"  validtype="length[0,250]" invalidMessage="有效长度0-250"
						data-options="multiline:true" style="overflow:auto;width:100%;height:80px;" value="${ msgType.remark}" />
						</td>								
				</tr>	
			</table>
		</form>
</body>
</html>
