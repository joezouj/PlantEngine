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
			title : '添加物料类型',
			url : ext.contextPath + '/material/materialtype/add.do',
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
			title : '查看物料类型',
			url : ext.contextPath + '/material/materialtype/view.do?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑物料类型',
			url : ext.contextPath + '/material/materialtype/edit.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/material/materialtype/delete.do', {id : id}, function(data) {
					if(data==1){
						parent.$.messager.alert('提示','删除成功','info');
						grid.treegrid('reload');
					}else{
						parent.$.messager.alert('提示','删除失败','info');
					}
				});
			}
		});
	};
	var deletesFun = function() {
		var datas = "";  
	    $("input:checked").each(function(){
	        var id = $(this).attr("id");
	        if(id.indexOf('check_type')== -1 && id.indexOf("check_")>-1){
				datas += id.replace("check_",'')+',';
			}  
	    });
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要删除的记录','info');
		}else{
			parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/material/materialtype/deletes.do', {ids:datas} , function(data) {
						if(data>0){
							parent.$.messager.alert('提示','成功删除'+data+'条记录','info');
							grid.treegrid('reload');
							grid.treegrid('clearChecked');
						}else{
							parent.$.messager.alert('提示','删除失败','info');
						}
					});
				}
			});
		}
	};

	function check(checkid){
      var s = '#check_'+checkid;
      /*选子节点*/
       var nodes = $("#grid").treegrid("getChildren",checkid);
       for(i=0;i<nodes.length;i++){
          $(('#check_'+nodes[i].id))[0].checked = $(s)[0].checked;
           
       }
       //选上级节点
       if(!$(s)[0].checked){
         var parent = $("#grid").treegrid("getParent",checkid);
         $(('#check_'+parent.id))[0].checked  = false;
         while(parent){
           parent = $("#grid").treegrid("getParent",parent.id);
            $(('#check_'+parent.id))[0].checked  = false;
         }
       }else{
         var parent = $("#grid").treegrid("getParent",checkid);
         var flag= true;
         var sons = parent.sondata.split(',');         
         for(j=0;j<sons.length;j++){
            if(!$(('#check_'+sons[j]))[0].checked){
            flag = false;
            break;
            }
         }
         if(flag)
         $(('#check_'+parent.id))[0].checked  = true;
         while(flag){
             parent = $("#grid").treegrid("getParent",parent.id);
            if(parent){
            sons = parent.sondata.split(',');
            for(j=0;j<sons.length;j++){
            if(!$(('#check_'+sons[j]))[0].checked){
            flag = false;
            break;
            }
           }
         }
          if(flag)
         $(('#check_'+parent.id))[0].checked  = true;
         }
       }
    }
	
	$(function() {
 		grid = $('#grid').treegrid({
 			title : '',
 			url : ext.contextPath + '/material/materialtype/getMaterialTypes.do',
 			striped : true,
 			rownumbers : true,
 			singleSelect: false,
			ctrlSelect: true,
			selectOnCheck: false,
			checkOnSelect: false,
 			idField : 'id',
 			treeField: 'typename',
 			pagination : true,
 			pageSize : 20,
 			pageList : [20, 50, 100],
 			columns : [ [
 				{ field : 'ck',formatter:function(val,row,index){
 					return '<input type="checkbox" onclick=check("'+row.id+'") id="check_'+row.id+'" (row.checked?"checked":"") />';
 					}
 				},
				{width : '150', title : '物料类型', field : 'typename', sortable : true, align:'left',halign:'center'}, 
 				{width : '100', title : '类型代码', field : 'typecode', sortable : true, align:'center',halign:'center'}, 
				{width : '100', title : '状态', field : 'status', sortable : true, align:'center',halign:'center', formatter : function(value, row, index) {
					switch (value) {
					case '0':
						return '禁用';
					case '1':
						return '启用';
					}
				}}, 
				{width : '500', title : '备注', field : 'remark', sortable : true, halign:'center'},
				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/>';
							<%if (sessionManager.havePermission(session,"material/materialtype/edit.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
							<%}%>
							<%if (sessionManager.havePermission(session,"material/materialtype/delete.do")) {%>
							str += '<img class="iconImg ext-icon-table_delete" title="删除" onclick="deleteFun(\''+row.id+'\');"/>';
							<%}%>
						return str;
					}
				} 
			] ],
			loadFilter: function (data){			  
                resultData = data;
                    $.each(data.rows, function(i) {  
                        var parentId = data.rows[i].parentid;                         
                        if(parentId!='-1'){  
                            data.rows[i]._parentId = parentId;  
                        }else{
                        	data.rows[i]._parentId =undefined;
                        }  
                    });   
                    return data; 
            },
            toolbar : '#toolbar',
			onLoadSuccess : function(row,data) {
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
					<table>
						<tr>
							<%if (sessionManager.havePermission(session,"material/materialtype/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"material/materialtype/delete.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deletesFun();">删除</a>
							</td>
							<%}%>
							<td><div class="datagrid-btn-separator"></div></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<form id="searchForm">
						<table class="tooltable">
							<tr>
								<td>物料类型</td>
								<td><input name="search_name" class="easyui-textbox" /></td>
								<td>类型代码</td>
								<td><input name="search_code" class="easyui-textbox" /></td>
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-search',plain:true"  
										onclick="grid.treegrid('load',ext.serializeObject($('#searchForm')));">搜索</a>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" 
										onclick="$('#searchForm').form('clear');grid.treegrid('load',{});">重置</a>
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