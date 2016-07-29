<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$('#dataTree').tree({
			url : ext.contextPath + '/document/getDataJson.do?random=' + Math.random(),
			parentField : 'pid',
			onClick : function(node) {
				if(node.pid != ''){
					if(node.attributes.type=='A'){
						$("#mainFrame").attr("src",ext.contextPath+"/document/showWorkOrderEdit.do?id="+node.id);
					}else if(node.attributes.type=='B'){
						$("#mainFrame").attr("src",ext.contextPath+"/document/showDrawingEdit.do?id="+node.id);
					}else if(node.attributes.type=='C'){
						$("#mainFrame").attr("src",ext.contextPath+"/document/showBookEdit.do?id="+node.id);
					}
				}
			}
		});
	});
	
	
	function reloadTree(){
		$('#dataTree').tree('options').url = ext.contextPath+ '/document/getDataJson.do?random=' + Math.random();
		$('#dataTree').tree('reload');
		//$("#mainFrame").attr("src",ext.contextPath+"/document/showDataTree.do");
	}
	
	function getSelectedPid(){
		var t = $('#dataTree').tree('getSelected');
		var pid="";
		if(t!=null){
			pid=t.id;
		}
		return pid;
	}
	
	function addWorkOrder(){
		$("#mainFrame").attr("src",ext.contextPath+"/document/showWorkOrderAdd.do?pid="+getSelectedPid());
	}
	
	function addDrawing(){
		$("#mainFrame").attr("src",ext.contextPath+"/document/showDrawingAdd.do?pid="+getSelectedPid());
	}
	function addBook(){
		$("#mainFrame").attr("src",ext.contextPath+"/document/showBookAdd.do?pid="+getSelectedPid());
	}
</script>
</head>

<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id="leftdiv" data-options="region:'west',split:true" style="width:250px;position:relative">
			<div style="padding:2px;background-color:#eeeeee">
				<a class="easyui-linkbutton" href="#" data-options="iconCls:'icon-add',plain:true" onclick="addWorkOrder()">工作指令</a>
				<a class="easyui-linkbutton" href="#" data-options="iconCls:'icon-add',plain:true" onclick="addDrawing()">图纸</a>
				<a class="easyui-linkbutton" href="#" data-options="iconCls:'icon-add',plain:true" onclick="addBook()">作业指导书</a>
			</div>
			<ul id="dataTree" class="easyui-tree" data-options="method:'get',animate:true"></ul>
		</div>
		<div id="rightdiv" data-options="region:'center'" style="overflow: hidden;">
			<iframe id="mainFrame" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" frameBorder="0" scrolling="auto"></iframe>
		</div>
	</div>
</body>
</html>
