<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;	
	function selectOK() {  
        var rolenames="";
        var roleids="";  
        var checkedItems = $('#grid').datagrid('getChecked');		
		$.each(checkedItems, function(index, item){
			roleids+=item.id+",";
			rolenames+=item.name+",";
		});
		if(roleids.length>1){
			roleids = roleids.substring(0, roleids.length-1);
		}
		if(rolenames.length>1){
			rolenames = rolenames.substring(0, rolenames.length-1);
		}
        var retn= roleids+")"+rolenames; 
        return retn;
        }
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/user/getRolesForSelect.do',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: false,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'}, 
				{width : '100', title : '名称', field : 'name', sortable : true, editor:'textbox',halign:'center'}, 
				{width : '280', title : '描述', field : 'description', sortable : true, editor:'textbox',halign:'center'} 
			] ],
			toolbar : '#toolbar',
			onLoadSuccess:function(data){
				if(data){
					$.each(data.rows, function(index, item){
						//$.each(eval('${json}'), function(index1, item1){
						if('${roleids}'!=null){
							var roleidstr='${roleids}';
							var roleidarr=roleidstr.split(",");
							for(var i=0;i<roleidarr.length;i++){
								if(item.id==roleidarr[i]){
									$('#grid').datagrid('checkRow', index);
								}
							}
						}
						//});
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
								<td>权限名称</td>
								<td><input name="search_name" class="easyui-textbox" style="width: 180px;" /></td>
															
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