<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	var addFun = function() {
		var dialog = parent.ext.modalDialog({
			title : '添加用户信息',
			url : ext.contextPath + '/user/addUser.do',
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	var viewFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '查看用户信息',
			url : ext.contextPath + '/user/viewUser.do?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑用户信息',
			url : ext.contextPath + '/user/editUser.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	var pwdFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要重置密码？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/user/resetPwd.do', {id : id}, function(data) {
					if(data.res==1){
						parent.$.messager.alert('提示','重置成功','info');
						grid.datagrid('reload');
					}else{
						parent.$.messager.alert('提示','重置失败','info');
					}
				},'json');
			}
		});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/user/deleteUser.do', {id : id}, function(data) {
					if(data==1){
						parent.$.messager.alert('提示','删除成功','info');
						grid.datagrid('reload');
					}else{
						parent.$.messager.alert('提示','删除失败','info');
					}
				});
			}
		});
	};
	var deletesFun = function() {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas+=item.id+",";
		}); 
		//alert(datas);
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要删除的记录','info');
		}else{
			parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/user/deleteUsers.do', {ids:datas} , function(data) {
						if(data>0){
							parent.$.messager.alert('提示','成功删除'+data+'条记录','info');
							grid.datagrid('reload');
							grid.datagrid('clearChecked');
						}else{
							parent.$.messager.alert('提示','删除失败','info');
						}
					});
				}
			});
		}
	};
	
	var downFun = function() {
		searchForm.action = ext.contextPath + '/user/downtemplate.do';
		
		searchForm.submit();
	};
	
	var importFun = function() {

		var dialog = parent.ext.modalDialog({
			title : '选择文件',
			width: 600,
			height:300,
			closeOnEscape:true,
			url : ext.contextPath + '/user/doimport.do',
			buttons : [ {
				text : '导入数据',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.doimport(dialog, grid);
				}
			} ]
		});
		
		
	};
	
	var exportFun = function() {
		//通过response导出，用户自己选择路径
 		searchForm.action = ext.contextPath + '/user/exportUsersByResponse.do';
		searchForm.submit();
 		var win = $.messager.progress({
			title:'提示',
			msg:'文件正在导出，请稍后...'
		});
		setTimeout(function(){
			$.messager.progress('close');
		},3000);
		
/*		//后台输出
		$.post(ext.contextPath + '/user/exportUsers.do',function(data) {
			$.messager.progress('close');
			if(data!=''){
				$.messager.alert('提示',data,'info');
			}else{
				$.messager.alert('提示','导出失败','info');
			}
		}); */
		
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
				{width : '180', title : '部门', field : '_pname', halign:'center'},
				{width : '100', title : '职位', field : 'jobs', halign:'center',formatter : function(value, row, index) {
						var res="";		
						for(var i=0;i<value.length;i++){
							res+=value[i].name+", ";
						}
						return res.replace(/, $/g,"");
					}
				},
			    {width : '50', title : '性别', field : 'sex', sortable : true, halign:'center', align:'center', formatter : function(value, row, index) {
						switch (value) {
						case '0':
							return '女';
						case '1':
							return '男';
						}
					}
			    }, 
			    {width : '100', title : '在线小时', field : 'totaltime', sortable : true, halign:'center',formatter : function(value, row, index) {
			    		return value.toFixed(2);
			    	}
			    },
			    {width : '150', title : '角色权限', field : 'roles', halign:'center', formatter : function(value, row, index) {
						var res="";	
				    	for(var i=0;i<value.length;i++){
				    		res+="<a class='linkbutton' href='javascript:void(0)' onclick='showMenu(\""+value[i].id+"\",\""+value[i].name+"\")'>"+value[i].name+"</a>"+", ";
				    	}
						return res.replace(/, $/g,"");
					}
			    }, 
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/>';
							<%if (sessionManager.havePermission(session,"user/editUser.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
							<%}%>
							<%if (sessionManager.havePermission(session,"user/deleteUser.do")) {%>
							str += '<img class="iconImg ext-icon-table_delete" title="删除" onclick="deleteFun(\''+row.id+'\');"/>';
							<%}%>
							<%if (sessionManager.havePermission(session,"user/resetPwd.do")) {%>
							str += '<img class="iconImg ext-icon-table_key" title="重置密码" onclick="pwdFun(\''+row.id+'\');"/>';
							<%}%>
						return str;
					}
				} 
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
			}
		});
		
		$('#search_pid').combotree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			panelHeight:'auto',
			width:200,
			onBeforeSelect:function(node){
				if(node.id=="-1"){
                    $("#search_pid").tree("search_pid");
                }
            },
			editable:false
		});
	});
	
	function showMenu(id,name){
		parent.ext.modalDialog({
			title : name,
			url : ext.contextPath + '/user/showRoleMenu.do?roleid=' + id
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
							<%if (sessionManager.havePermission(session,"user/addUser.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"user/deleteUser.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deletesFun();">删除</a>
							</td>
							<%}%>
							<td><div class="datagrid-btn-separator"></div></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_save',plain:true" onclick="downFun();">模版下载</a></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_add',plain:true" onclick="importFun();">导入</a></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="exportFun();">导出</a></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<form id="searchForm">
						<table class="tooltable">
							<tr>
								<td>登录名</td>
								<td><input name="search_name" class="easyui-textbox" /></td>
								<td>姓名</td>
								<td><input name="search_caption" class="easyui-textbox" /></td>
								<td>部门</td>
								<td><input id="search_pid" name="search_pid" class="easyui-combotree"/></td>
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