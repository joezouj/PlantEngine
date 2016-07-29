<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%@ page import="com.sipai.entity.base.ServerObject"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<html>
<head>
<title><%= ServerObject.atttable.get("TOPTITLE")%></title>
<jsp:include page="inc.jsp"></jsp:include>
<LINK href="IMG/logo.ico" type="image/x-icon" rel="icon"/>
<LINK href="IMG/logo.ico" type="image/x-icon" rel="shortcut icon"/>
<script type="text/javascript">
	var mainMenu;
	var mainTabs;
	
	$(function() {
		
		$('#passwordDialog').show().dialog({
			modal : true,
			closable : true,
			iconCls : 'ext-icon-lock_edit',
			buttons : [ {
				text : '修改',
				handler : function() {
					if ($('#passwordDialog form').form('validate')) {
						$.post("Login/resetPwd.do", {"loadNewPassword" : $("#pwd").val()}, function(data) {
							if (data=="成功") {
								$.messager.alert('提示', '密码修改成功！', 'info');
								$('#passwordDialog').dialog('close');
							}else{
								$.messager.alert('提示', data, 'error');
							}
						});
					}
				}
			} ],
			onOpen : function() {
				$('#passwordDialog form :input').val('');
			}
		}).dialog('close');

	});
	
	//菜单栏选择后添加tab页面
	function addTab(id,name,location){
		$.post("user/isOnline.do");//用于session过期后重新进入登录页面
		
		var tabs = $('#mainTabs');
		var opts = {
			title : name,
			closable : true,
			content : '<iframe src="'+location+'" allowTransparency="true" style="border:0;width:100%;height:100%;" frameBorder="0" scrolling="auto"></iframe>',
			border : false,
			fit : true
		};
		if (tabs.tabs('exists', opts.title)) {
			tabs.tabs('select', opts.title);
		} else {
			tabs.tabs('add', opts);
		}
	}
	
	//消息
	var post = "";
	var msgflag = "yes";
	function querynewmsg(){
		$.post("msg/getUnreadMsgNum.do",function(data){
			var res = data;
			document.getElementById("msgcount").innerHTML = "("+res+")";
			if(msgflag=='yes' && res > 0){
				msgshow("您有"+res+"条新消息！'");
				msgflag="no";
			}
		});
	}
	function msgshow(a){
		$.messager.show({
            title:'消息',
            msg:a,
            showType:'show',
            timeout:5000,
            style:{
            	left:'',
                right:1,
                top:'',
                bottom:31
            }
        });
	}
	querynewmsg();
	//setInterval("querynewmsg();",5000);
</script>
</head>
<body id="mainLayout" class="easyui-layout">
	<div data-options="region:'north',href:'jsp/north.jsp'" class="logo" style="height: 70px; overflow: hidden;"></div>
	<div data-options="region:'west',href:'user/showMenuListByCu.do',split:true" title="${cu.caption}" style="width: 200px"></div>
	<div data-options="region:'center',href:'jsp/center.jsp'" style="overflow: hidden;"></div>
	<div data-options="region:'south',border:false" style="height: 30px; overflow: hidden;">
		<table style="float:right">
			<tr>
				<td style="padding:0px 5px 0px 5px">
					<%if (sessionManager.havePermission(session,"msg/getMsgrecv.do?scope=all")) {%>
							<a href="javascript:addTab('msgrecv','收消息','msg/showMsgrecv.do');" class="easyui-linkbutton" data-options="plain:true">
						<b id="newmsg">消息</b><b id="msgcount"></b>
					</a>
					<%}%>
				</td>
			</tr>
		</table>
	</div>

	<div id="passwordDialog" title="修改密码" style="display: none;width: 380px;">
		<form method="post" class="form" onsubmit="return false;">
			<table class="table">
				<tr>
					<th>新密码</th>
					<td><input id="pwd" name="loadNewPassword" type="password" class="easyui-validatebox" data-options="required:true,validType:'isBlank'" /></td>
				</tr>
				<tr>
					<th>重复密码</th>
					<td><input type="password" class="easyui-validatebox" data-options="required:true,validType:'eqPwd[\'#pwd\']'" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>