<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	function dosave(dialog,grid) {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas+=item.id+",";
		}); 
		//alert(datas);
		$.post(ext.contextPath + '/user/updateUserRole.do', {userstr:datas,roleid:"${roleid}"} , function(data) {
			if(data >= 0){
				top.$.messager.alert('提示','保存成功','info', function() {
					dialog.dialog('destroy');
				});
			}else{
				top.$.messager.alert('提示','保存失败','info');
			}
		});
	};
	
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/user/getUsers.do',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: false,
			ctrlSelect:true,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'}, 
				{width : '100', title : '登录名', field : 'name', sortable : true, halign:'center'}, 
				{width : '80', title : '姓名', field : 'caption', sortable : true, halign:'center'},
				{width : '180', title : '部门', field : '_pname', sortable : true, halign:'center'},
			    {width : '50', title : '性别', field : 'sex', sortable : true, halign:'center', align:'center', formatter : function(value, row, index) {
						switch (value) {
						case '0':
							return '女';
						case '1':
							return '男';
						}
					}
			    }
			] ],
			toolbar : '#toolbar',
			onLoadSuccess:function(data){
				if(data){
					$.each(data.rows, function(index, item){
						$.each(eval('${json}'), function(index1, item1){
							if(item.id==item1.empid){
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
	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<form id="searchForm">
						<table>
							<tr>
								<td>登录名</td>
								<td><input name="search_name" class="easyui-textbox" style="width: 180px;" /></td>
								<td>姓名</td>
								<td><input name="search_caption" class="easyui-textbox" style="width: 180px;" /></td>
								<td>性别</td>
								<td><select name="search_sex" class="easyui-combobox" style="width:100px;" data-options="panelHeight:'auto',editable:false">
									<option value="">请选择</option>
									<option value="1">男</option>
									<option value="0">女</option></select>
								</td>
								<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-search',plain:true" 
									onclick="grid.datagrid('load',ext.serializeObject($('#searchForm')));">搜索</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" 
									onclick="$('#searchForm').form('clear');grid.datagrid('load',{});">重置</a>
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
</body>
</html>