<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
 	$(function() {
 		grid = $('#grid').datagrid({
 			title : '',
 			url : ext.contextPath + '/material/salesorderproduct/getPDetails.do?pid=${salesorderproduct.id}',
 			rownumbers : true,
 			striped : true, 			
 			ctrlSelect:true,
 			singleSelect: false,
			selectOnCheck: false,
			checkOnSelect: false,
 			idField : 'id', 			
 			pageSize : 20,
 			pageList : [20, 50, 100],
 			columns : [ [				
				{width : '720', title : '产品序列号', field : 'productuid', halign:'center',align:'left'}
 			] ]
 		});
 	});
 	function dofinish(dialog, grid){
 		parent.$.messager.confirm('提示', '您确定生产订单已完成？操作后订单状态将无法更改。', function(r) {
			if (r) {
				if ($(".form").form('validate')) {
					$.post(ext.contextPath + "/material/salesorderproduct/dofinish.do", $(".form").serialize(), function(data) {
						if (data== '1') {
							top.$.messager.alert('提示', "状态更改成功，订单已完成", 'info', function() {
								//刷新列表
								grid.datagrid('reload');
								dialog.dialog('destroy');
							});
						}else if(data == '0'){
							top.$.messager.alert('提示', "保存失败", 'info');
						}else{
							top.$.messager.alert('提示', data.res, 'info');
						}
					},'json');
				}
			}
		});	
	}
</script>
</head>
<body>
		<form method="post" class="form">
		<div class="easyui-panel" title="销售订单信息" style="padding:10px;"
		data-options="tools:'#tt'">
			<table class="table">
			<input type="hidden" id="id" name="id" value="${salesorderproduct.id}"/>
			<input type="hidden" id="status" name="status" value="${salesorderproduct.status}"/>
				<tr>
					<th>销售订单编号</th>
					<td>${salesorderproduct.salesorderno}</td>
				</tr>
				<tr>
					<th>产品编码</th>
					<td>${salesorderproduct.product.materialcode}			
					</td>
				</tr>
				<tr>
					<th>产品名称</th>
					<td><span id="productname">${salesorderproduct.product.materialname}</span></td>
				</tr>
				<tr>
					<th>产品数量</th>
					<td>${salesorderproduct.productnum}
					</td>
				</tr>
				<tr>
					<th>客户名称</th>
					<td>${salesorderproduct.clients.name}
					</td>
				</tr>
				<tr>
					<th>订单创建时间</th>
					<td>${fn:substring(salesorderproduct.ordercreatedate,0,16)}						
					</td>
				</tr>
				<tr>
					<th>订单完成时间</th>
					<td>${fn:substring(salesorderproduct.orderfinishdate,0,16)}						
					</td>
				</tr>
				<tr>
					<th>发货时间</th>
					<td>${fn:substring(salesorderproduct.deliverydate,0,16)}					
					</td>
				</tr>
			</table>
		</div>
		</form>
		<br/>
		<div class="easyui-panel" title="产品明细" data-options="collapsible:true" width="100%">
			<div data-options="fit:true,border:false">
				<table id="grid" data-options="border:false"></table>
			</div>
		</div>
		
		
</body>
</html>
