<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$('#mainMenu').tree({
			url : ext.contextPath + '/user/getMenusJson.do?random=' + Math.random(),
			parentField : 'pid',
			onClick : function(node) {
				if(node.pid==''){
					doadd();
				}else{
					$("#mainFrame").attr("src",ext.contextPath+"/user/showMenuEdit.do?id="+node.id);
				}
			}
		});
		
		doadd();
	});
	
	 function reloadTree(){
		$('#mainMenu').tree('options').url = ext.contextPath+ '/user/getMenusJson.do?random=' + Math.random();
		$('#mainMenu').tree('reload');
	 }
	 
	 function getSelectedPid(){
			var t = $('#mainMenu').tree('getSelected');
			var pid="";
			if(t!=null){
				pid=t.id;
			}
			return pid;
		}
	 
	 function doadd() {
		$("#mainFrame").attr("src",ext.contextPath+"/user/showMenuAdd.do?pid="+getSelectedPid());
	}
</script>
</head>

<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id="leftdiv" data-options="region:'west',split:true" style="width:250px;position:relative">
			<div style="padding:2px;background-color:#eeeeee">
				<a class="easyui-linkbutton" href="#" data-options="iconCls:'icon-add',plain:true" onclick="doadd()">新增</a>
			</div>
			<ul id="mainMenu" class="easyui-tree" data-options="method:'get',animate:true"></ul>
		</div>
		<div id="rightdiv" data-options="region:'center'" style="overflow: hidden;">
			<iframe id="mainFrame" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" frameBorder="0" scrolling="auto"></iframe>
		</div>
	</div>

</body>
</html>
