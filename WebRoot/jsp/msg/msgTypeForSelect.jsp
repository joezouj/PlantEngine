<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;	
	function selectOK() {  
        var name="";
        var id="";  
        var checkedItem = $('#grid').datagrid('getSelected');
     	id=checkedItem.id;
		name=checkedItem.name;
        var retn= id+")"+name; 
        return retn;
        }
	$(function() {	
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/msg/getMsgTypeForSelect.do?querytype=select',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: true,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'}, 
				{width : '100', title : '类型名称', field : 'name', sortable : true, halign:'center' }, 
				{width : '100', title : 'ID', field : 'id', sortable : true, halign:'center' },
			    {width : '80', title : '类型种类',  field: 'pid', sortable : true, halign:'center' }		    
				
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {			
				$('.iconImg').attr('src', ext.pixel_0);
			
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
								<td>名称</td>
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