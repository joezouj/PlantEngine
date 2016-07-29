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
 			url : ext.contextPath + '/plan/dailyplan/getPDetails.do?pid=${dailyplan.id}',
 			rownumbers : true,
 			striped : true, 			
 			ctrlSelect:true,
 			singleSelect: false,
			selectOnCheck: false,
			checkOnSelect: false,
 			idField : 'id', 			
 			pageSize : 20,
 			pageList : [20, 50, 100],
 			 rowStyler: function(index,row){
             	var rowstyler='';
	        		if (row.productionorderno!=null){        		
	        			if(row.productionorderno!=null &&row.productionorderno!='${dailyplan.productionorderno}'){
	        				rowstyler+= ' background-color:#C0C0C0;color:#fff;';
	        			}
	        		}
	        		return rowstyler;
	    		},
 			columns : [ [				
				{width : '535', title : '产品序列号', field : 'productuid', halign:'center',align:'left'},
				{width : '100', title : '工单号', field : 'workorder', halign:'center',align:'left', formatter : function(value, row){
					if(value!=null && value!=""){
						return value.productionorderno;
					}else{
						return "";
					}
				}},
				{width : '100', title : '工单状态', field : 'workorderstatus', halign:'center',align:'center', formatter : function(value, row){
					if(row.productionorderno!=null &&row.productionorderno!='${dailyplan.productionorderno}'){
							return "已重排";
					}else{
						if(row.workorder==null ||row.workorder=="") {
							return "未排工单";
						}else{
							return "已排工单";
						}
					}
					
				}}
 			] ]
 		});
 	});
 	$(function(){
		$("#processrealid").combobox({
			url:ext.contextPath + "/process/real/getlistByProductid.do?",
			valueField:'id',
			textField:'name',
			onLoadSuccess:function(){
				$("#processrealid").combobox("setValue","${dailyplan.processrealid}");
			}
		});
	});
	function dofinish(dialog, grid){
		parent.$.messager.confirm('提示', '您确定计划已完成？操作后计划状态将无法更改。', function(r) {
			if (r) {
				if ($(".form").form('validate')) {
					$.post(ext.contextPath + "/plan/dailyplan/dofinish.do", $(".form").serialize(), function(data) {
						if (data== '1') {
							top.$.messager.alert('提示', "状态更改成功，计划已完成", 'info', function() {
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
		<input type="hidden" id="id" name="id" value="${dailyplan.id}"/>
		<input type="hidden" id="status" name="status" value="${dailyplan.status}"/>
		<input type="hidden" id="pdtotalnum" name="pdtotalnum" value="${ pdtotalnum}"/>
		<input type="hidden" id="pdplanednum" name="pdplanednum" value="${ pdplanednum}"/>
		<div class="easyui-panel" title="日计划信息" style="padding:10px;" width="100%" 
		data-options="tools:'#tt'">
			<table class="table">				
				<tr>
					<th>销售订单号</th>
					<td>${dailyplan.salesorderproduct.salesorderno }
					</td>
				</tr>
				<tr>
					<th>该订单产品名称</th>
					<td>${dailyplan.product.materialname}</td>					
				</tr>
				<tr>
					<th>产品数量</th>
					<td>${ pdtotalnum}</td>					
				</tr>
				<tr>
					<th>已排计划数量</th>
					<td>${ pdplanednum}</td>					
				</tr>
				<tr>
					<th>已完成数量</th>
					<td>${ finishednum}</td>
				</tr>
				<tr>
					<th>剩余未排数量</th>
					<td>${ remainednum}</td>
				</tr>
				</table>
				<br />
				<table class="table">
				<tr>
					<th>计划日期</th>
					<td>${fn:substring(plandt,0,10)}
					</td>
				</tr>
				<tr>
					<th>生产订单号</th>
					<td>${dailyplan.productionorderno }</td>
				</tr>
				<tr>
					<th>产品工艺路线</th>
					<td>${processrealname}</td>
				</tr>
				<tr>
					<th>产线</th>
					<td>${dailyplan.line.name}</td>
				</tr>
				<tr>
					<th>产线产能</th>
					<td>${dailyplan.line.capacity}</td>
				</tr>
				<tr>
					<th>本计划生产数量</th>
					<td>${dailyplan.productquantity}
					</td>					
				</tr>		
				
				<tr>
					<th>计划下达日期</th>
					<td>${fn:substring(dailyplan.stdt,0,19)}
					</td>
				</tr>
				<tr>
					<th>计划完成日期</th>
					<td>${fn:substring(dailyplan.eddt,0,19)}
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>${dailyplan.remark}</td>
				</tr>
			</table>
			</div>
		</form>
			<br/>
			<div class="easyui-panel" title="产品明细" data-options="collapsible:true"   width="100%" >
				<div data-options="fit:true,border:false">
					<table id="grid" data-options="border:false"></table>
				</div>
			</div>		
</body>
</html>
