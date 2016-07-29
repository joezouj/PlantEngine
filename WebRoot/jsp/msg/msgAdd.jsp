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
			$.post(ext.contextPath + "/msg/saveMsgYM.do", $(".form").serialize(), function(data) {			
				if(data.res.indexOf("成功")==-1){
					top.$.messager.alert('提示', data.res, 'info');
				}else {
					top.$.messager.alert('提示', data.res, 'info', function(){
								//刷新列表
								grid.datagrid('reload');
								dialog.dialog('destroy');
							});				
					
				}
			},'json');
		}
	}

function selectMsgType(msgtype,mtypename){ 	  
	var dialog = parent.ext.modalDialog({
			title : '选择消息类型',
			width: 600,
			height:420,
			closeOnEscape:true,
			url : ext.contextPath + '/msg/msgTypeForSelect.do',
			toolbar:'',
			buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectOK();
					if(res!=null){
						dialog.dialog('destroy');
						for (var i = 0; i < $("#"+msgtype+" option").length; i++) {
							if($("#"+msgtype).get(0).options[i].value==res.split(")")[0]){
								$("#"+msgtype).get(0).options[i].remove();
							}
 						}
						//$("#"+msgtype).append("<option value='"+res.split(")")[0]+"' selected>"+res.split(")")[1]+"</option>");  						
						$("#"+mtypename).textbox('setValue',res.split(")")[1]);//easyui textbox赋值jquery不一样
						$("#"+msgtype).val(res.split(")")[0]);
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
				<tr>
					<th >消息类型</th>
					<td >
<!-- 					<select id="mtype" name="mtype" >							 -->
<!-- 							<option value="P">个人信息</option>													 -->
<!-- 						</select>&nbsp; <a href="javascript:void(0)" onclick="selectMsgType('mtype')">更多</a> -->
					<input id="mtypename" name="mtypename" class="easyui-textbox" data-options="required:true,validType:'isBlank',prompt:'单击选择'" value="个人信息" readonly/>
					<input id="mtype" name="mtype" type="hidden" value="P" />	
					</td>
					<script type="text/javascript">
						$(function() {							
							$("#mtypename").textbox('textbox').bind("click", function () { 
								selectMsgType("mtype","mtypename");
							})
						})
				</script>				
				</tr>					
				<tr>
					<th>接收人</th>
					<td>					 
					<input id="recvname" name="recvname" class="easyui-textbox"	data-options="required:true,validType:'isBlank',multiline:true,iconCls:'icon-man',readonly:true" 
						style="overflow:auto;width:100%;height:80px;" value="" />
					<input id="recvid" name="recvid" type="hidden"	/>
					<script type="text/javascript">
						$(function() {
							$("#recvname").textbox('textbox').bind("click", function () { 
								selectUsers("recvname","recvid","${param.iframeId}");
							});
						});
					</script>
					</td>
				</tr>							
				<tr>
					<th >内容</th>
					<td ><input name="content"   class="easyui-textbox"	data-options="multiline:true" style="overflow:auto;width:100%;height:200px;" value=""validtype="length[0,250]" invalidMessage="有效长度0-250" ></td>					
				</tr>
<!-- 				<tr>
					<th >发送方式</th>
					<td ><select id="sendway" name="sendway" >							
							<option value="msg">仅消息发送</option>
							<option value="sms">仅短信发送</option>							
							<option value="both">消息+短信发送</option>
						</select></td>				
				</tr> -->
				
			</table>
		
	</form>
</body>
</html>
