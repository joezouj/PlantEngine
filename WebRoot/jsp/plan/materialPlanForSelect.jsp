<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/plan/materialplan/getmaterialplan.do?pid=${pid}&planid=${planid}&workstationid=${workstationid}&random=Math.random()',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: true,
			ctrlSelect: true,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'materialid',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			idField : 'materialid',
			columns : [ [ 	
					{checkbox:true , field : 'ck'},
					{width : '180', title : '物料编码', field : 'materialcode', sortable : true, halign:'center' , formatter : function(value, row){
						if(value!=null){
							return value;
						}else{
							return "";
						}
						}},
					{width : '250', title : '物料名称', field : 'materialname', sortable : true, halign:'center' , formatter : function(value, row){
						if(value!=null){
							return value;
						}else{
							return "";
						}
						}},
					{width : '90', title : '总计划量', field : 'amount', halign:'center' , formatter : function(value, row){
						if(value!=null){
							if(value>0){
								return value+" "+row.unit;
							}
							return "";
							
						}else{
							return "";
						}
						}}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
				if(data){
					$.each(data.rows, function(index, item){
						if(item.materialcode==""||item.materialcode==undefined){
							//移除不合理数据
							$('#grid').datagrid('deleteRow', index);
						}
					});
				}
			}
		});
		
	});
	 function selectOK() { 
	    	//以json格式返回数据
	        var jsonstr = "";
	        var row = $('#grid').datagrid('getSelected');
	        if (row){
	        	jsonstr = '{"materialid":"'+row.materialid
	        			+'","materialcode":"'+row.materialcode
	        			+'","materialname":"'+row.materialname
	        			+'","materialamount":"'+row.amount
	        			+'","materialunit":"'+row.unit
	        			+'"}';
	        	var dataset = $.parseJSON(jsonstr);
	 	        return dataset;
	        }else{
	        	top.$.messager.alert('提示', "请选择要使用的数据！", 'info');
	        }
	       
	    }
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<form id="searchForm">
						<table class="tooltable">
							<tr>
								<td>物料编码</td>
								<td><input name="search_materialcode" class="easyui-textbox" /></td>
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
