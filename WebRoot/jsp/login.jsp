<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.entity.base.ServerObject"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title><%= ServerObject.atttable.get("TOPTITLE")%></title>
<jsp:include page="inc.jsp"></jsp:include>
<link id="easyuiTheme" rel="stylesheet" href="<%=contextPath%>/JS/jquery-easyui-1.4.5/themes/metro/easyui.css" type="text/css">
<LINK href="IMG/logo.ico" type="image/x-icon" rel="icon"/>
<LINK href="IMG/logo.ico" type="image/x-icon" rel="shortcut icon"/>
<style>
	.table tr{
		height:32px
	}
	.table input{
		height:22px
	}
</style>
<script type="text/javascript">
	function init(){
		$("#j_username").val(getCookie("j_username"));
		$("#j_password").val(getCookie("j_password"));
		
		$('.loginbg').height($(window).height());
		$('input').css({"width":"194px","border":"1px solid #d5d5d5"});
	}
	
	function validateForm(loginTabIndex){
		if(loginTabIndex == 0){
			if($("#j_username").val()==''){
				$.messager.alert("提示","请输入用户名","info",function(){
					$("#j_username").focus();
					return false;
				});
			}else if($("#j_password").val()==''){
				$.messager.alert("提示","请输入密码","info",function(){
					$("#j_password").focus();
					return false;
				});
			}else{
				return true;
			}
		}else if(loginTabIndex == 1){
			if($("#j_cardid").val()==''){
				$.messager.alert("提示","请刷卡登录","info",function(){
					$("#j_cardid").focus();
					return false;
				});
			}else{
				return true;
			}
		}
	}
	
	var loginFun;
	$(function() {
		init();
		
		loginFun = function() {
			var loginTabs = $('#loginTabs').tabs('getSelected');//当前选中的tab
			var loginTabIndex = $('#loginTabs').tabs('getTabIndex',loginTabs);//当前tab序号
			var $form = loginTabs.find('form');//选中的tab里面的form
			if (validateForm(loginTabIndex)) {//判断不为空
				$.post(ext.contextPath+"/Login/validate.do",$form.serialize(), function(result) {
					delCookie("j_username");
					delCookie("j_password");
					
					if (result==true) {
						if(loginTabIndex==0){
							setCookie("j_username",$("#j_username").val());
							setCookie("j_password",$("#j_password").val());
						}
						location.replace(ext.contextPath);
					} else {
						if(loginTabIndex==0){
							$.messager.alert('提示',"用户名或密码错误",  'error');
						}else if(loginTabIndex==1){
							$.messager.alert('提示',"卡号错误",  'error');
						}
					}
				}, "json");
			}
		};

		$('#loginDialog').show().dialog({
			modal : false,
			closable : false,
			shadow:false,
			title:"系统登录",
			buttons : [ {
				id : 'loginBtn',
				text : ' 登 录 ',
				width:80,
				handler : function() {
					loginFun();
				}
			} ],
			onOpen : function() {
				$('form :input:first').focus();
				$('form :input').keyup(function(event) {
					if ($(this).val() !='' && event.keyCode == 13) {
						loginFun();
					}
				});
			}
		});
		
		$('#loginTabs').tabs({
			onSelect:function(title,index){
				var loginTabs = $('#loginTabs').tabs('getSelected');
				loginTabs.find('form :input:first').focus();
		    }
		}); 
		
	});
	
	//写cookies 
	function setCookie(name,value) { 
	    var Days = 365; 
	    var exp = new Date(); 
	    exp.setTime(exp.getTime() + Days*24*60*60*1000); 
	    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString(); 
	} 

	//读取cookies 
	function getCookie(name) { 
	    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	    if(arr=document.cookie.match(reg)) return unescape(arr[2]); 
	    else return null; 
	} 

	//删除cookies 
	function delCookie(name) { 
	    var exp = new Date(); 
	    exp.setTime(exp.getTime() - 1*24*60*60*1000); 
	    var cval=getCookie(name); 
	    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString(); 
	} 
</script>
</head>
<body class="loginbg">
	<div style="background: url('<%=contextPath%>/IMG/login/sipai.png') no-repeat; background-position:center;background-color:#ffffff;height:140px;width:100%;">
	</div>
	<div style="background: url('<%=contextPath%>/IMG/login/title.png') no-repeat; background-position:center;height:180px;width:100%;">
	</div>
	<div style="position:absolute;bottom:0px;text-align:center;background-color:#ffffff;width:100%;line-height:100px;height:100px;">
		<label style="font-family:'微软雅黑';font-size:16px;">请使用IE11以上版本浏览器</label>
	</div>
	
	<div id="loginDialog" style="width: 320px; height: 180px; overflow: hidden;">
		<div id="loginTabs" class="easyui-tabs" data-options="fit:true,border:false">
			<div title="用户登录" style="overflow: hidden; padding: 5px;">
				<form method="post" class="form" onSubmit="return false;">
					<table class="table" style="width: 100%">
						<tr>
							<th style="text-align:center;width:80px">用户名</th>
							<td style="text-align:center">
								<input id="j_username" name="j_username"/>
							</td>
						</tr>
						<tr>
							<th style="text-align:center">密&nbsp;码</th>
							<td style="text-align:center">
								<input id="j_password" name="j_password" type="password"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div title="卡号登录" style="overflow: hidden; padding: 5px;">
				<form method="post" class="form" onSubmit="return false;">
					<table class="table" style="width: 100%">
						<tr>
							<th style="text-align:center;width:80px">卡号</th>
							<td style="text-align:center">
								<input id="j_cardid" name="j_cardid" type="password"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>