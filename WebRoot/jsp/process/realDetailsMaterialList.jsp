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
			var dialog = top.ext.modalDialog({
				title : '添加物料',
				url : ext.contextPath + '/process/realDetailsMaterial/add.do?pid=${param.pid}',
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					}
				} ]
			});
		};
		var editFun = function(id) {
			var dialog = top.ext.modalDialog({
				title : '编辑物料',
				url : ext.contextPath + '/process/realDetailsMaterial/edit.do?id=' + id+'&?pid=${param.pid}',
				buttons : [ {
					text : '保存',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					}
				} ]
			});
		};
		var deleteFun = function(id) {
			top.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/process/realDetailsMaterial/delete.do', {id : id}, function(data) {
						if(data==1){
							top.$.messager.alert('提示','删除成功','info',function(){
								grid.datagrid('reload');
							});
						}else{
							top.$.messager.alert('提示','删除失败','info');
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
			if(datas==""){
				top.$.messager.alert('提示', '请先选择要删除的记录','info');
			}else{
				top.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
					if (r) {
						$.post(ext.contextPath + '/process/realDetailsMaterial/deletes.do', {ids:datas} , function(data) {
							if(data>0){
								top.$.messager.alert('提示','成功删除'+data+'条记录','info',function(){
									grid.datagrid('reload');
									grid.datagrid('clearChecked');
								});
							}else{
								top.$.messager.alert('提示','删除失败','info');
							}
						});
					}
				});
			}
		};
		
		$(function() {
			grid = $('#grid').datagrid({
				title : '',
				url : ext.contextPath + '/process/realDetailsMaterial/getlist.do?pid=${param.pid}',
				striped : true,
				rownumbers : true,
				pagination : true,
				ctrlSelect:true,
				singleSelect: false,
				selectOnCheck: false,
				checkOnSelect: false,
				idField : 'id',
				pageSize : 50,
				pageList : [ 20, 50, 100],
				columns : [ [ 
					{checkbox:true , field : 'ck'}, 
					{width : '180', title : '物料编码', field : 'material', sortable : false, halign:'center',formatter:function(value,row){
						return value.materialcode;
					}}, 
					{width : '220', title : '物料名称', field : 'materialname', sortable : false, halign:'center',formatter:function(value,row){
						return row.material.materialname;
					}}, 
					{width : '150', title : '物料规格', field : 'materialspec', sortable : false, halign:'center',formatter:function(value,row){
						return row.material.spec;
					}}, 
 					{width : '100', title : '物料数量', field : 'amount', sortable : false, halign:'center',
						editor:{type:'numberbox',options:{precision:1 }}
					},
					{width : '100', title : '单位', field : 'unit', sortable : false, halign:'center',formatter:function(value,row){
 						return row.material.unit;
	 				}},
					{title : '操作', field : 'action', width : '70', halign:'center', align:'center', formatter : function(value, row,rowIndex) {
							var str = '';
// 								<%if (sessionManager.havePermission(session,"process/real/edit.do")) {%>
// 								str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
								str += '<img class="iconImg ext-icon-table_save" title="保存" onclick="saveFun(\''+row.id+'\',\''+rowIndex+'\');"/>';
// 								<%}%>
								<%if (sessionManager.havePermission(session,"process/real/delete.do")) {%>
								str += '<img class="iconImg ext-icon-table_delete" title="删除" onclick="deleteFun(\''+row.id+'\');"/>';
								<%}%>
							return str;
						}
					} 
				] ],
				onClickCell: function (rowIndex, field, value) {
       			 	beginEditing(rowIndex, field, value);
       			 	$('.iconImg').attr('src', ext.pixel_0);
   				},
				toolbar : '#toolbar',
				onLoadSuccess : function(data) {
					$('.iconImg').attr('src', ext.pixel_0);
				}
			});
		});
		var editIndex = undefined;
		var beginEditing = function (rowIndex, field, value) {
		    if (field != "amount" )
	        {
	        	if(editIndex!=undefined)
	        	{
	        		$('#grid').datagrid('endEdit', editIndex);
	        		editIndex = undefined;
	        	}
	        	return;
	        }
		
		    if (rowIndex != editIndex) {
		        if (endEditing()) {
		            $('#grid').datagrid('beginEdit', rowIndex);
		            editIndex = rowIndex;
		
		            var ed = $('#grid').datagrid('getEditor', { index: rowIndex, field: 'amount' });
		            $(ed.target).focus().bind('blur', function () {
		                endEditing();
		            });
		        } else {
		            $('#grid').datagrid('selectRow', editIndex);
		        }
		    }
		};
		var endEditing = function () {
		    if (editIndex == undefined)
		    { return true; }
		    if ($('#grid').datagrid('validateRow', editIndex)) {
		        var ed = $('#grid').datagrid('getEditor', { index: editIndex, field: 'amount' });
		        var number = $(ed.target).numberbox('getValue');
		        $('#grid').datagrid('getRows')[editIndex]['amount'] = number;
		        $('#grid').datagrid('endEdit', editIndex);
		        editIndex = undefined;
		        return true;
		    } else {
		        return false;
		    }
		};
		var updateFun = function() {
			var checkedItems = $('#grid').datagrid('getRows');
			var datas="";
			var amounts="";
			$.each(checkedItems, function(index, item){
				datas+=item.id+",";
				if(item.amount==null || item.amount==""){
					amounts+="0,";
				}else{
					var editor = $('#grid').datagrid('getEditor', {index:index,field:"amount"});
					if(editor!=null&&editor!=undefined){
						var editAmount = editor.target.val();
						amounts+=editAmount+",";
					}else{
						amounts+=item.amount+",";
					}
				}
			}); 
			$.post(ext.contextPath + '/process/realDetailsMaterial/updateamount.do', {ids:datas,amounts:amounts} , function(data) {
				if(data != ""){
	   				top.$.messager.alert("提示", data, "info", function() {
	   					grid.datagrid('reload');
	   				});
	    		}else{
	    			top.$.messager.alert("提示", "保存失败", "info", function() {
	   					grid.datagrid('reload');
	   				}); 
	        	}
			});
		};
		var saveFun = function(id,index) {
			var editor = $('#grid').datagrid('getEditor', {index:index,field:"amount"});
			if(editor!=null&&editor!=undefined){
				var amount = editor.target.val();
				$.post(ext.contextPath + '/process/realDetailsMaterial/saveamount.do', {id:id,amount:amount} , function(data) {
					if(data != ""){
		   				top.$.messager.alert("提示", data, "info", function() {
		   					grid.datagrid('reload');
		   				});
		    		}else{
		    			top.$.messager.alert("提示", "保存失败", "info", function() {
		   					grid.datagrid('reload');
		   				}); 
		        	}
				});
			}else{
				top.$.messager.alert("提示", "请输入要修改的数量", "info"); 
			}
		};
	</script>
	</head>
	<body>
		<table id="toolbar" style="width:100%">
			<tr>
				<td>
					<table>
						<tr>
							<%if (sessionManager.havePermission(session,"process/real/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"process/real/edit.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" 
									onclick="updateFun();">保存</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"process/real/delete.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deletesFun();">删除</a>
							</td>
							<%}%>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table id="grid" data-options="fit:true,border:false"></table>
	</body>
</html>