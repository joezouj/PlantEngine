<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8">
	var logout1 = function() {
		$.messager.confirm('系统提示', '确定退出系统？', 
			function(r){
				if(r){    
					$.post("Login/logout.do",function(){
						delCookie("j_username");
						delCookie("j_password");
						location.replace(ext.contextPath);
					});
				}
		});
	};
	
	//读取cookies 
	function getCookie(name) { 
	    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	    if(arr=document.cookie.match(reg)) return unescape(arr[2]); 
	    else return null; 
	}
	
	//删除cookies 
	function delCookie(name) { 
	    var exp = new Date(); 
	    exp.setTime(exp.getTime() - 1); 
	    var cval=getCookie(name); 
	    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString(); 
	} 
</script>
<div style="position: absolute; right: 0px; bottom: 0px;">
	<a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_kzmbMenu',iconCls:'ext-icon-cog'">控制面板</a> 
</div>
<div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
	<div data-options="iconCls:'ext-icon-user_edit'" onclick="$('#passwordDialog').dialog('open');">修改密码</div>
	<div class="menu-sep"></div>
	<div data-options="iconCls:'ext-icon-door_out'" onclick="logout1();">退出系统</div>
</div>