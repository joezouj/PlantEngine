<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	var editIndex = undefined;//当前编辑行索引
	function endEditing(){//清除editor
        if (editIndex == undefined){return true;}
        if ($('#grid').datagrid('validateRow', editIndex)){
            $('#grid').datagrid('endEdit', editIndex);
            editIndex = undefined;
            return true;
       	} else {
            return false;
        }
    }
	//新增行
	var addFun = function() {
		if(!endEditing()){
			return false;
		}
		
		editIndex = 0;//设置当前编辑行
		if($('#grid').datagrid('getRows')[editIndex]==undefined){//如果一行都不存在
			$('#grid').datagrid('insertRow', {index: editIndex,row: {}});
		}else{
			if($('#grid').datagrid('getRows')[editIndex]['id']!=undefined){//判断是否已经存在未保存的新增行
				$('#grid').datagrid('insertRow', {index: editIndex,row: {}});
			}
		}
		
		$('#grid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
		
		var ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'name'});
        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
		
	};
	//编辑行
	var editFun = function (index, field){
		if(!endEditing()){
			return false;
		}
		
		editIndex = index;
       	$('#grid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
       
       	var ed = $('#grid').datagrid('getEditor', {index:editIndex,field:field});
       	($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
    };
    //保存行
	var saveFun = function() {
		if($('#grid').datagrid('getSelected')==null){
			top.$.messager.alert('提示','请先选择要保存的记录','info');
		}else{
			var id = $('#grid').datagrid('getSelected').id;
			var url;
			if(id==undefined){//新增
				editIndex = $('#grid').datagrid('getRowIndex');
				url = ext.contextPath + '/user/saveRole.do';
			}else{//编辑
				editIndex = $('#grid').datagrid('getRowIndex',id);
				url = ext.contextPath + '/user/updateRole.do';
			}
			//alert(editIndex);
			
			$('#grid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
			
			var name_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'name'});
			var name = $(name_ed.target).textbox('getValue');
			var description_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'description'});
			var description = $(description_ed.target).textbox('getValue');
			var opt=$('#grid').datagrid('selectRow', editIndex).datagrid('options');
			var morder = (opt.pageNumber-1) * opt.pageSize + editIndex + 1;
			
			$.post(url,{id:id,name:name,description:description,morder:morder},function(data){
				if(data.res == '1'){
					top.$.messager.alert('提示','保存成功','info',function (){
						$('#grid').datagrid('getRows')[editIndex]['id'] = data.id;//设置行id
						$('#grid').datagrid('acceptChanges');
						$('#grid').datagrid('unselectAll');
					});
				}else if(data.res == '0'){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	};
	//删除行
	var deleteFun = function() {
		if($('#grid').datagrid('getSelected')==null){
			top.$.messager.alert('提示','请先选择要删除的记录','info');
		}else{
			var id = $('#grid').datagrid('getSelected').id;
			if(id==undefined){//未经保存的数据
				top.$.messager.confirm('提示', '确定删除此记录？', function(r) {
					if (r) {
						editIndex = $('#grid').datagrid('getRowIndex');
						$('#grid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
					}
				});
			}else{
				top.$.messager.confirm('提示', '确定删除此记录？', function(r) {
					if (r) {
						$.post(ext.contextPath + '/user/deleteRole.do', {id : id}, function(data) {
							if(data==1){
								top.$.messager.alert('提示','删除成功','info',function(){
									$('#grid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
								});
							}else{
								top.$.messager.alert('提示','删除失败','info');
							}
						});
					}
				});
			}
		}
	};
	
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/user/getListRole.do',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			onClickCell: editFun,
			idField : 'id',
			pageSize : 20,
			pageList : [20, 50, 100],
			columns : [ [ 
				{width : '100', title : '名称', field : 'name', sortable : true, editor:'textbox',halign:'center'}, 
				{width : '280', title : '描述', field : 'description', sortable : true, editor:'textbox',halign:'center'} , 
				{width : '90', title : '操作', field : 'action', halign:'center', align:'center', formatter : function(value, row, index) {
					var str = '';
						str += '<img class="iconImg" src="../CSS/ext_icons/table/table_edit.png" title="菜单配置" onclick="setMenu(\''+row.id+'\');"/> ';
						str += '<img class="iconImg" src="../CSS/ext_icons/user/user_edit.png" title="人员配置" onclick="setUser(\''+row.id+'\');"/> ';
					return str;
				}
			} 
			] ],
			toolbar : '#toolbar'
		});
	});
	
	function setMenu(id){
		var dialog = parent.ext.modalDialog({
			title : '菜单配置',
			url : ext.contextPath + '/user/showRoleMenu.do?roleid=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	}
	
	function setUser(id){
		var dialog = parent.ext.modalDialog({
			title : '用户配置',
			url : ext.contextPath + '/user/showRoleUser.do?roleid=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	}
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deleteFun();">删除</a>
							</td>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" 
									onclick="saveFun();">保存</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
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