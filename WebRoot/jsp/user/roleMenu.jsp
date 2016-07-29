<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,grid) {
		var checkedtree = $('#mainMenu').tree('getChecked');
		var datas="";
		$.each(checkedtree,function(index,item){
			datas+=item.id+",";
		});
		 $.post(ext.contextPath + "/user/updateRoleMenu.do", {menustr:datas,roleid:"${roleid}"}, function(result) {
			if (result >= 0) {
				top.$.messager.alert('提示', "保存成功", 'info', function() {
					dialog.dialog('destroy');
				});
			}else{
				top.$.messager.alert('提示', "保存失败", 'info');
			}
		}); 
	}
	
	var tree;
	$(function(){
		tree=$('#mainMenu').tree({
			url : ext.contextPath + '/user/getMenusJsonWithFuncByRoleID.do?roleid=${roleid}&random=' + Math.random(),
			parentField : 'pid',
			checkbox:true,
			animate:true,
			onLoadSuccess:function(){ 
				$.each(eval('${json}'),function(index,item){
					 var node = $('#mainMenu').tree('find',item.menuid);  
	                 $('#mainMenu').tree('check',node.target); 
				});
			},
			onClick:function(node,data){
				var dialog = parent.ext.modalDialog({
					width:600,
					height:400,
					title : '功能权限配置',
					url : ext.contextPath + '/user/roleFuncForSelect.do?menuid='+node.id+'&roleid=${roleid}',
					buttons : [ {
						text : '保存',
						handler : function() {
							dialog.find('iframe').get(0).contentWindow.dosave(dialog,window);
						}
					} ]
				});
			}
		});
	});
	
	function reloadTree(){
		tree.tree('options').url = ext.contextPath + '/user/getMenusJsonWithFuncByRoleID.do?roleid=${roleid}&random=' + Math.random();
		tree.tree('reload');
	 }
</script>
</head>
<body>
		<ul id="mainMenu" class="easyui-tree" data-options="method:'get',animate:true"></ul>
</body>
</html>
