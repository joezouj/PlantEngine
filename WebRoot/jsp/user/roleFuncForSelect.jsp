<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,parent) {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas+=item.id+",";
		}); 
		//alert(datas);
		$.post(ext.contextPath + '/user/updateFuncByRoleMenu.do', {funcstr:datas,roleid:"${param.roleid}",menuid:"${param.menuid}"} , function(data) {
			if(data >= 0){
				top.$.messager.alert('提示','保存成功','info', function() {
					parent.reloadTree();
					
					dialog.dialog('destroy');
				});
			}else{
				top.$.messager.alert('提示','保存失败','info');
			}
		});
	}
	
	$(function() {
		$('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/user/getFuncByMenuid.do?menuid=${param.menuid}',
			striped : true,
			rownumbers : true,
			singleSelect: false,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			columns : [ [ 
				{checkbox:true , field : 'ck'}, 
				{width : '100', title : '功能', field : 'name', sortable : true, halign:'center'},
				{width : '200', title : '地址', field : 'location', sortable : true, halign:'center'}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess:function(data){
				if(data){
					$.each(data.rows, function(index, item){
						$.each(eval('${json}'), function(index1, item1){
							if(item.id==item1.menuid){
								$('#grid').datagrid('checkRow', index);
							}
						});
					});
				}
			} 
		});
	});
	
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
</body>
</html>