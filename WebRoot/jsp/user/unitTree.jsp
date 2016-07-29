<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$('#unitTree').tree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			dnd:true,
			onClick : function(node) {
				if(node.pid != ''){
					if(node.attributes.type=='C'){
						$("#mainFrame").attr("src",ext.contextPath+"/user/showCompanyEdit.do?id="+node.id);
					}else{
						$("#mainFrame").attr("src",ext.contextPath+"/user/showDeptEdit.do?id="+node.id);
					}
				}
			},
			onDrop:function(target,source,point){
				var next = $('#unitTree').tree('getNode',target);
				/* var parentNode;
				if(point=='append'){
					parentNode = next;
				}else{
					parentNode = $('#unitTree').tree('getParent',next.target);
				}
				var childrenNodes= $('#unitTree').tree('getChildren',parentNode.target);
				alert(childrenNodes.length);
				
				var datas  = {};  
				for(var i=0;i<childrenNodes.length;i++) {
					datas["children["+i+"].id"]=childrenNodes[i].id;
					datas["children["+i+"].name"]=childrenNodes[i].text;
					datas["children["+i+"].pid"]=childrenNodes[i].pid;
				} */
				$.ajax({
				     url : ext.contextPath + '/user/saveUnitOrder.do',
				     type: 'post', 
				     data : {
				    	 target:next.id,
				    	 source:source.id,
				    	 point:point
				     },
				     dataType : 'json',
				     success : function(r){
				    	 if(r>0){
				    		 top.$.messager.alert('提示','顺序保存成功','info');
				    	 }else{
				    		 top.$.messager.alert('提示','顺序保存失败','info');
				    	 }
				     }
				});
			}
		});
		
	});
	
	
	function reloadTree(){
		$('#unitTree').tree('options').url = ext.contextPath+ '/user/getUnitsJson.do?random=' + Math.random();
		$('#unitTree').tree('reload');
	}
	
	function getSelectedPid(){
		var t = $('#unitTree').tree('getSelected');
		var pid="";
		if(t!=null){
			pid=t.id;
		}
		return pid;
	}
	
	function addComp(){
		$("#mainFrame").attr("src",ext.contextPath+"/user/showCompanyAdd.do?pid="+getSelectedPid());
	}
	
	function addDept(){
		$("#mainFrame").attr("src",ext.contextPath+"/user/showDeptAdd.do?pid="+getSelectedPid());
	}
</script>
</head>

<body>
	<div class="easyui-layout" data-options="fit:true">
		<div id="leftdiv" data-options="region:'west',split:true" style="width:250px;position:relative">
			<div style="padding:2px;background-color:#eeeeee">
				<a class="easyui-linkbutton" href="#" data-options="iconCls:'icon-add',plain:true" onclick="addComp()">公司</a>
				<a class="easyui-linkbutton" href="#" data-options="iconCls:'icon-add',plain:true" onclick="addDept()">部门</a>
			</div>
			<ul id="unitTree" class="easyui-tree" data-options="method:'get',animate:true"></ul>
		</div>
		<div id="rightdiv" data-options="region:'center'" style="overflow: hidden;">
			<iframe id="mainFrame" src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;" frameBorder="0" scrolling="auto"></iframe>
		</div>
	</div>
</body>
</html>
