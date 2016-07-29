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
	function selectFun() {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas = '{"id":"'+item.id+'"}';
		}); 
		//alert(datas);
		return $.parseJSON('{"result":['+datas.replace(/,$/g,"")+']}');
	}
	var grouptype = function(){
		var dialog = parent.ext.modalDialog({
				title : '工段维护',
				url : ext.contextPath + '/work/grouptype/showlist.do',
				width:1200,
				height:600
			});
	};
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/work/grouptype/getlistForSelect.do?querytype=select',
			striped : true,
			rownumbers : true,
			pagination : false,
			singleSelect: true,
			ctrlSelect:true,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			columns : [ [ 
				{width : '100', title : '名称', field : 'name', sortable : true, halign:'center'},
				{width : '100', title : '开始时间', field : 'sdt', sortable : true, halign:'center',align:'center', formatter : function(value, row) {
							var str = '';
							if(value.length>6){
								str = value.substring(0,5);
							}else{
								return value;
							}
							return str;
						}
				}, 
				{width : '100', title : '结束时间', field : 'edt', sortable : true, halign:'center',align:'center', formatter : function(value, row) {
							var str = '';
							if(value.length>6){
								str = value.substring(0,5);
							}else{
								return value;
							}
							return str;
						}
				},
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
				if(grid.datagrid('getSelected')==null){
					grid.datagrid('selectRow',0);
				}
			}
		});
		
	});
	
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',fit:true,border:false">
		<input id="userids" name="userids" type="hidden"	/>
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
</body>
</html>