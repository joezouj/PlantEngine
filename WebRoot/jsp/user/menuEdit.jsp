<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave() {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/user/updateMenu.do", $(".form").serialize(), function(result) {
				if (result == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						//刷新树
						parent.reloadTree();
					});
				} else {
					top.$.messager.alert('提示', "保存失败", 'info');
				}
			});
		}
	}
	
	function saveDefault() {
		if($("#location").val()==''){
			top.$.messager.alert('提示','请先填写菜单地址','info');
		}else{
			if ($(".form").form('validate')) {
				$.post(ext.contextPath + "/user/saveDefaultFunc.do", $(".form").serialize(), function(result) {
					if (result > 0) {
						top.$.messager.alert('提示', "保存成功", 'info', function() {
							$('#grid').datagrid('reload');
						});
					} else {
						top.$.messager.alert('提示', "保存失败", 'info');
					}
				});
			}
		}
	}
	
	function dodel() {
		top.$.messager.confirm('提示', '确定删除此菜单？', function(r) {
			if (r) {
				$.post(ext.contextPath + "/user/deleteMenu.do", $(".form").serialize(), function(result) {
					if (result.res > 0) {
						top.$.messager.alert('提示', "删除成功", 'info', function() {
							//刷新树
							parent.reloadTree();
							parent.$("#mainFrame").attr("src",ext.contextPath+"/user/showMenuAdd.do?pid="+$("#pid").val());
						});
					} else {
						top.$.messager.alert('提示', result.msg, 'info');
					}
				},'json');
			}
		});
	}
	
	$(function() {
		$('#pname').combotree({
			url : ext.contextPath + "/user/getMenusJson.do?random=" + Math.random(),
			parentField : 'pid',
			method:'get',
			required:true,
			onClick : function(node) {
				$("#pid").val(node.id);
			}
		});
	});
</script>

<script type="text/javascript">
	var grid;
	var editIndex = undefined;//当前编辑行索引
	
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/user/getFuncJson.do?id=${menu.id}',
			striped : true,
			rownumbers : true,
			singleSelect : true,
			onClickCell: editFun,
			idField : 'id',
			pageSize : 20,
			pageList : [20, 50, 100],
			columns : [ [ 
				{width : '100', title : '名称', field : 'name', sortable : true, editor:'textbox',halign:'center',align:'center'}, 
				{width : '280', title : '路径', field : 'location', sortable : true, editor:'textbox',halign:'center'} ,
				{width : '280', title : '所属菜单', field : 'pid', sortable : true, halign:'center',align:'center', 
					formatter: function(value,row,index){
						return row._pname;
					},
					editor:{
						type:'combotree',
						options:{
							required:true,
							parentField : 'pid',
							valueField:'id',
							textField:'text',
							method:'get',
							url: ext.contextPath + "/user/getMenusJson.do?random=" + Math.random()
					}
				}} ,
				{width : '100', title : '启用', field : 'active', sortable : true, halign:'center',align:'center',
					editor: { 
						type: 'combobox', 
						options: { 
							required: true,
							panelHeight:'auto',
							valueField:'value',
							textField:'text',
							data:[{value:"启用",text:"启用"},{value:"禁用",text:"禁用"}]
					}
				}} 
			] ],
			toolbar : '#toolbar'
		});
	});
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
			$('#grid').datagrid('insertRow', {index: editIndex,row: {pid:'${menu.id}',active:'启用'}});
		}else{
			if($('#grid').datagrid('getRows')[editIndex]['id']!=undefined){//判断是否已经存在未保存的新增行
				$('#grid').datagrid('insertRow', {index: editIndex,row: {pid:'${menu.id}',active:'启用'}});
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
				url = ext.contextPath + '/user/saveFunc.do';
			}else{//编辑
				editIndex = $('#grid').datagrid('getRowIndex',id);
				url = ext.contextPath + '/user/updateFunc.do';
			}
			//判断必输项
			if(!$('#grid').datagrid('validateRow', editIndex)){
				top.$.messager.alert('提示','请输入必输项','info');
				return false;
			}
			//获取数据
			var name_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'name'});
			var name = $(name_ed.target).textbox('getValue');
			var location_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'location'});
			var location = $(location_ed.target).textbox('getValue');
			var active_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'active'});
			var active = $(active_ed.target).textbox('getValue');
			var pid_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'pid'});
			var pid = $(pid_ed.target).textbox('getValue');
			var opt=$('#grid').datagrid('selectRow', editIndex).datagrid('options');
			var morder = (opt.pageNumber-1) * opt.pageSize + editIndex;
			
			$.post(url,{id:id,name:name,location:location,morder:morder,pid:pid,active:active},function(data){
				if(data.res == 1){
					top.$.messager.alert('提示','保存成功','info',function (){
						$('#grid').datagrid('reload');
					});
				}else{
					top.$.messager.alert('提示','保存失败','info');
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
						$.post(ext.contextPath + '/user/deleteMenu.do', {id : id}, function(data) {
							if(data.res==1){
								top.$.messager.alert('提示','删除成功','info',function(){
									$('#grid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
								});
							}else{
								top.$.messager.alert('提示','删除失败','info');
							}
						},'json');
					}
				});
			}
		}
	};
</script>
</head>
<body>
	<div id="tt">
		<a href="javascript:void(0)" class="icon-save" title="保存" onclick="dosave()"></a>
		<a href="javascript:void(0)" class="icon-remove" title="删除" onclick="dodel()"></a>
	</div>
	<div class="easyui-panel" title="编辑菜单" style="padding:10px;"
		data-options="tools:'#tt'">
		<form method="post" class="form">
			<input type="hidden" name="id" value="${menu.id}"/>
			<input type="hidden" name="type" value="menu"/>
			<table class="table">
				<tr>
					<th>名称</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${menu.name}" /></td>
					<th>上级菜单</th>
					<td>
						<input id="pname" name="pname" class="easyui-combotree" value="${menu._pname}" />
						<input id="pid" name="pid" type="hidden" value="${menu.pid}"/>
					</td>
				</tr>
				<tr>
					<th>地址</th>
					<td colspan="3"><input id="location" name="location" class="easyui-textbox"
						style="width:500px" value="${menu.location}" /></td>
				</tr>
				<tr>
					<th>顺序</th>
					<td><input name="morder" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${menu.morder}"/></td>
					<th>启用</th>
					<td>
						<select name="active" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,value:'${menu.active}'">
							<option value="启用">启用</option>
							<option value="禁用">禁用</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<br/>
	<div class="easyui-panel" title="功能设置">
	<div id="toolbar">
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
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="saveDefault();">默认权限</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="border:false">
		<table id="grid" data-options="border:false"></table>
	</div>
	</div>
	
</body>
</html>
