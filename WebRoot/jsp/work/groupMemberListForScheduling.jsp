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
<script type="text/javascript" src="json2.js"></script> 
<script type="text/javascript">
	function selectFun() {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas += '{"userid":"'+item.userid +'","usertype":"'+item.usertype +'","username":"'+item.username +'"},';
		}); 
		return $.parseJSON('{"result":['+datas.replace(/,$/g,"")+']}');
	}
	
	var grid;
	var stationlist;
	var getworkstationFun = function() {
		$.post(ext.contextPath + '/work/group/getworkstation.do', {} , function(data) {
			stationlist=eval(data);
		});
	};
	$(function() {
		getworkstationFun();
		$('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/work/group/getMemberListByGroupId.do?groupid=${groupid}&userids=${userids}&workstationids=${workstationids}',
			striped : true,
			pagination : true,
			singleSelect: false,
			ctrlSelect:true,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			pageSize : 20,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '100', title : '人员类型', field : 'usertype', sortable : false, halign:'center',formatter:function(value){
					if(value=='leader'){
						return "组长";
					}else{
						return "组员";
					}
				}}, 
				{width : '180', title : '人员名称', field : 'username', sortable : false, halign:'center'},
				{width : '180', title : '人员工位', field : 'workstationid', sortable : false, halign:'center',
						formatter:function(value,row){
						var te;
						for(var i=0;i<stationlist.length;i++){
							if(value==stationlist[i].id){
								te=stationlist[i].name;
								break;
							}
						}
	                    return te;
		                },
						editor:{type:'combobox',
								options:{url:ext.contextPath + '/work/group/getworkstation.do',
										valueField:'id', 
										textField:'name',
										method:'get',
				                        required:false,
				                        }
		                        }
                 }
			] ],
			onClickCell: function (rowIndex, field, value) {
        			 beginEditing(rowIndex, field, value);
    			},
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
				if(data){
					var userids='${userids}';
					var userid;
					if(userids!=""){
						userid=userids.split(',');
					}
					$.each(data.rows, function(index, item){
						if(item._checkflag){
							$('#grid').datagrid('checkRow', index);
						}
					});
				}
				//if(grid.datagrid('getSelected')==null){
				//	grid.datagrid('selectRow',0);
				//}
			}
		});
		
	});
	
	var editIndex = undefined;
		var beginEditing = function (rowIndex, field, value) {
		    if (field != "workstationid" )
	        {
	        	if(editIndex!=undefined)
	        	{
	        		endEditing();
	        		$('#grid').datagrid('endEdit', editIndex);
	        		editIndex = undefined;
	        	}
	        	return;
	        }
		
		    if (rowIndex != editIndex) {
		        if (endEditing()) {
		            $('#grid').datagrid('beginEdit', rowIndex);
		            //alert("行号："+rowIndex);
		            editIndex = rowIndex;
		
		            var ed = $('#grid').datagrid('getEditor', { index: rowIndex, field: 'workstationid' });
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
		        var ed = $('#grid').datagrid('getEditor', { index: editIndex, field: 'workstationid' });
		        var number = $(ed.target).combobox('getValue');
		        //alert();
		        $('#grid').datagrid('getRows')[editIndex]['workstationid'] = number;
		        var numbern = $(ed.target).combobox('getText');
		        $('#grid').datagrid('getRows')[editIndex]['workstationname'] = numbern;
		        $('#grid').datagrid('endEdit', editIndex);
		        editIndex = undefined;
		        return true;
		    } else {
		        return false;
		    }
		};
	var dosave = function(dialog,grid) {
			//若用户未确认编辑状态，则自动完成编辑
			var row=$('#grid').datagrid('getSelected');
			var editIndex=$('#grid').datagrid('getRowIndex',row);
			var ed = $('#grid').datagrid('getEditor', { index: editIndex, field: 'workstationid' });
			if(ed!=null){
				var number = $(ed.target).combobox('getValue');
		        $('#grid').datagrid('getRows')[editIndex]['workstationid'] = number;
		        var numbern = $(ed.target).combobox('getText');
		        $('#grid').datagrid('getRows')[editIndex]['workstationname'] = numbern;
		        $('#grid').datagrid('endEdit', editIndex);
			}
	        
			var checkedItems = $('#grid').datagrid('getChecked');
			var datas="";
			var stationids="";
			var errorflag="";//标记是否勾选选人员未设置工位
			$.each(checkedItems, function(index, item){
				datas+=item.userid+",";
				stationids+=item.workstationid+",";
				if(item.workstationid==""){
					errorflag="error";
				}
			}); 
			if(datas==""){
				top.$.messager.alert('提示', '请选择至少一名成员','info');
			}else if(errorflag!=""){
				top.$.messager.alert('提示', '已选的人员中有人员未设置工位！','info');
			}else{
				var res=JSON.stringify(checkedItems);
				//alert(res);
				$.post(ext.contextPath + '/work/group/saveuserworkstation.do', {ids:datas,stationids:stationids} , function(data) {
					if(data==1){
						//top.$.messager.alert('提示','保存成功','info');
						dialog.dialog('destroy');
					}else{
						top.$.messager.alert('提示','信息提交失败','info');
					}
				});
				return res;
			}
		};
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false" >
	<div data-options="region:'center',fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
</body>
</html>