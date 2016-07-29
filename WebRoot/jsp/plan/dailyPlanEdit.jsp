<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	var selectedProducts='';
	var manualnum=0;
	function dosave(dialog,grid) {
		if($("#status").val()==""){
			$("#status").val('0');
		}	
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/plan/dailyplan/update.do", $(".form").serialize()+"&selectedProducts="+selectedProducts, function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						//刷新列表
						grid.datagrid('reload');
						dialog.dialog('destroy');
					});
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}		
	}	
	function selectSalesOrderProduct(salesorderid,salesordernum){
		var dialog = parent.ext.modalDialog({
			title : '选择销售订单',
			width: 800,
			height:550,				
			url : ext.contextPath + '/material/salesorderproduct/select.do', 
				buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectOK();
					if(res!=null){
							$("#"+salesordernum).textbox('setValue',res.salesorderno);//easyui textbox赋值jquery不一样
 							$("#"+salesorderid).val(res.salesorderid);
							$("#pdtotalnumber").html(res.pdtotalnum);
 							$("#pdplanednumber").html(res.pdplanednum);
 							$("#remainednumber").html(res.remainednum);
 							$("#productquantity").numberspinner('setValue',res.remainednum); //自动填写本次生产数量 
 							$("#finishednumber").html(res.finishednum);
 							$("#pdtotalnum").val(res.pdtotalnum);
 							$("#pdplanednum").val(res.pdplanednum);
 							$("#productinfo").html(res.materialname);
 							$("#processrealid").combobox({
 								url:ext.contextPath + '/process/real/getlistByProductid.do?productid='+res.productid,
 							    valueField:'id',
 							    textField:'name',
 							    onSelect: function(record){ 							    	
 							    	$("#lineid").combobox({
  										url : ext.contextPath + '/process/real/getLineListByRealid.do?realid='+$("#processrealid").combobox("getValue")+'&random=' + Math.random(),
  										valueField : 'id',
  										textField : 'name',
  										method:'get',
  										panelHeight:'auto',
  										onSelect: function(record){
  											$("#capacity").val(record.capacity);
  		 									$("#linecapacity").html(record.capacity);
  										}
  									});
  							    },
  							    onLoadSuccess:function(){
  							    	if($("#processrealid").combobox('getData').length==1){
  							    		$("#processrealid").combobox('select',$("#processrealid").combobox('getData')[0].id);
  							    	}
  							    }
 							});  				
							manualnum=0;
 							displayNum();
					}
					dialog.dialog('destroy');
					
				}
			},
			{
				text : '取消',
				iconCls:'icon-cancel',
				handler : function() {
					dialog.dialog('destroy');
									
				}
			}
			]
		});
	}
	function selectDailyPlanSummary(pid,dailyplansummary){
		var dialog = parent.ext.modalDialog({
			title : '选择日计划归属日期',
			width: 800,
			height:550,			
			url : ext.contextPath + '/plan/dailyplansummary/select.do', 
				buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectOK();
					if(res!=null){
							$("#"+dailyplansummary).textbox('setValue',res.plandt.substring(0,10));//easyui textbox赋值jquery不一样
 							$("#"+pid).val(res.id);
					}
					dialog.dialog('destroy');
					
				}
			},
			{
				text : '取消',
				iconCls:'icon-cancel',
				handler : function() {
					dialog.dialog('destroy');
									
				}
			}
			]
		});
	}
	function invalidate(dialog, grid){
		parent.$.messager.confirm('提示', '您确定要作废计划？下发计划被作废后，将无法撤回。', function(r) {
			if(r){
				$("#status").val('3');
				dosave(dialog,grid);				
			}else{
				return ;
			}
		});
	}
	function dodistribute(dialog, grid){
		parent.$.messager.confirm('提示', '您确定要下发计划？下发计划生成工单后，将无法修改，只能作废。', function(r) {
			if(r){
				$("#status").val('1');
				//dosave							
				if ($(".form").form('validate')) {
					$.post(ext.contextPath + "/plan/dailyplan/update.do", $(".form").serialize()+"&selectedProducts="+selectedProducts, function(data) {
						if (data.res == 1) {
								top.$.messager.alert('提示', "保存成功", 'info', function() {
								//distribute
								var ids=$("#productionorderno").textbox('getValue');								
								$.post(ext.contextPath + '/work/workorder/addbyplan.do', {ids:ids} , function(data) {
											if(data>0){
												top.$.messager.alert('提示','下发计划成功','info',function(){
													grid.datagrid('reload');
													dialog.dialog('destroy');
												});
											}else{
												top.$.messager.alert('提示','下发计划失败','info');
											}
										});
							});
						}else if(data.res == 0){
								top.$.messager.alert('提示', "保存失败", 'info');
						}else{
								top.$.messager.alert('提示', data.res, 'info');
							}
						},'json');
				}
				
			}else{
				return ;
			}
		});
	}
	function dosubmit(dialog, grid){
		parent.$.messager.confirm('提示', '您确定要提交计划？提交计划等待审核，将无法修改。', function(r) {
			if(r){
				$("#status").val('1');
				//dosave							
				if ($(".form").form('validate')) {
					$.post(ext.contextPath + "/plan/dailyplan/update.do", $(".form").serialize()+"&selectedProducts="+selectedProducts, function(data) {
						if (data.res == 1) {
								top.$.messager.alert('提示', "提交成功", 'info', function() {
									refreshbutton();
									grid.datagrid('reload');
									dialog.dialog('destroy');
							});
						}else if(data.res == 0){
								top.$.messager.alert('提示', "提交失败", 'info');
						}else{
								top.$.messager.alert('提示', data.res, 'info');
							}
						},'json');
				}
				
			}else{
				return ;
			}
		}) ;
	}
	function refreshbutton(){		
		parent.$('#viewp1')[0].contentWindow.$('#edittd').hide();
		parent.$('#viewp1')[0].contentWindow.$('#addtd').hide();
	}
	$(function() {
 		var grid = $('#grid').datagrid({
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
 			columns : [ [				
				{width : '510', title : '产品序列号', field : 'productuid', halign:'center',align:'left'},
				{width : '120', title : '生产订单号', field : 'productionorderno', halign:'center',align:'left'},
				{width : '90', title : '工单状态', field : 'workorderid', halign:'center',align:'center', formatter : function(value, row){
					if(row.productionorderno!=null &&row.productionorderno!='${dailyplan.productionorderno}'){
							return "已重排";
					}else{
						if(value==null ||value=="") {
							return "未排工单";
						}else{
							return "已排工单";
						}
					}
				}}
 			] ]
 		});
 	});
	
	function selectOrderProductDetail(){
		var salesorderid=$("#salesorderid").val();
		var totalnum=$("#productquantity").numberspinner('getValue');
		if(salesorderid==null||salesorderid==""){
			top.$.messager.alert('提示', "请先选择销售订单", 'info');
			return;
		}
		var dialog = parent.ext.modalDialog({
			title : '产品列表（本计划生产总数：<span style=\"color:red\">'+totalnum+'</span>）',
			width: 950,
			height:550,			
			url : ext.contextPath + '/material/orderproductdetail/showlistForSelectAll.do?salesorderid='+salesorderid+'&totalnum='+totalnum+'&iframeId=${param.iframeId}&plandate=${plandt}&planid=${dailyplan.id}', 
				buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					parent.$.messager.confirm('提示', '已排计划的产品重新排计划将更改原计划状态为重排，作废原工单。已完成产品重排计划无效。', function(r) {
						if (r) {				
					       var res=dialog.find('iframe').get(0).contentWindow.selectOK();
							if(res!=null){//获得已排计划产品信息，待保存时再作废计划和任务		
									selectedProducts=res;		
									dialog.dialog('destroy');
									$("#div-view").panel('close');
									$("#div_product").panel('open');
									//document.getElementById("producttable").style.display="";							
									var selectedarr=(selectedProducts+",").split('}');//去掉了右括号
									manualnum=selectedarr.length-1;//显示个数		
									displayNum();
									var tb= document.getElementById('producttable');//删除旧的table
									var rowNum=tb.rows.length;		
									for(var j=1;j<rowNum;j++){
										tb.deleteRow(j);
										rowNum=rowNum-1;
										j=j-1;
									}
									for(var i=1;i<selectedarr.length;i++){
										var dataset;
										if(i==1){
											dataset = $.parseJSON(selectedarr[i-1]+"}");//加上右括号
										}else{
											dataset = $.parseJSON(selectedarr[i-1].substring(1,selectedarr[i-1].length)+"}");//加上右括号	
										}																		
										var htmlstr="<tr><td style='text-align:center'>"+i+"</td><td>"+dataset.productuid+"</td><td>"+dataset.productionorderno+"</td></tr>";
										$("#producttable").append(htmlstr);
									}																			
													
							}	
						}
					});				
					
				}
			},
			{
				text : '取消',
				iconCls:'icon-cancel',
				handler : function() {
					dialog.dialog('destroy');
									
				}
			}
			]
		});
	}
	function displayNum(){	
		var total=$("#productquantity").numberspinner('getValue');
// 		if(total<manualnum){
// 			$("#productquantity").numberspinner('setValue',manualnum);
// 			total=manualnum;
// 		}
		if(total>manualnum&& selectedProducts!=''){
			top.$.messager.alert('提示', "勾选数量不等于计划生产数量！已自动修改计划生产数量。", 'info');
			$("#productquantity").numberspinner('setValue',manualnum);
			total=manualnum;
		} 
		$("#sys").html(total-manualnum);
		$("#sysarrangenum").val(total-manualnum);
		$("#man").html(manualnum);
	}
	
	$(function() {							
		$("#productquantity").numberspinner({
		    onChange:function(){
		        displayNum();
		  }
		});
		
		$("#salesordernum").textbox('textbox').bind("click", function () { 
			selectSalesOrderProduct("salesorderid","salesordernum");
		});
		
		$("#dailyplansummary").textbox('textbox').bind("click", function () { 
			selectDailyPlanSummary("pid","dailyplansummary");
		});
		
		$("#processrealid").combobox({
			url:ext.contextPath + "/process/real/getlistByProductid.do?productid=${dailyplan.product.id}",
			valueField:'id',
			textField:'name',
			onSelect: function(record){ 							    	
		    	$("#lineid").combobox({
					url : ext.contextPath + '/process/real/getLineListByRealid.do?realid='+$("#processrealid").combobox("getValue")+'&random=' + Math.random(),
					valueField : 'id',
					textField : 'name',
					method:'get',
					panelHeight:'auto',
					onSelect: function(record){
						$("#capacity").val(record.capacity);
						$("#linecapacity").html(record.capacity);
					}
				});
		    },
			onLoadSuccess:function(){
				$("#processrealid").combobox("select","${dailyplan.processrealid}");
				
				$("#lineid").combobox({
					url : ext.contextPath + '/process/real/getLineListByRealid.do?realid=${dailyplan.processrealid}&random=' + Math.random(),
					valueField : 'id',
					textField : 'name',
					method:'get',
					panelHeight:'auto',
					onSelect: function(record){
						$("#capacity").val(record.capacity);
						$("#linecapacity").html(record.capacity);
					}
				});
				$("#lineid").combobox("select","${dailyplan.lineid}");
			}
		});
	});
</script>
</head>
<body>
		<form method="post" class="form">
		<input type="hidden" id="id" name="id" value="${dailyplan.id}"/>
		<input type="hidden" id="pdtotalnum" name="pdtotalnum" value="${ pdtotalnum}"/>
		<input type="hidden" id="pdplanednum" name="pdplanednum" value="${ pdplanednum}"/>
		<input id="status"  name="status" type="hidden" value="0"/>
		<div class="easyui-panel" title="日计划信息" style="padding:10px;" width="100%" 
		data-options="tools:'#tt'">
			<table class="table">				
				<tr>
					<th>销售订单号</th>
					<td><input id="salesordernum" name="salesordernum" class="easyui-textbox" data-options="required:true,validType:'isBlank',prompt:'单击选择'" value="${dailyplan.salesorderproduct.salesorderno }" />
					<input id="salesorderid" name="salesorderid" type="hidden" value="${dailyplan.salesorderid }" />					
					</td>
				</tr>
				<tr>
					<th>该订单产品名称</th>
					<td><span id="productinfo">${dailyplan.product.materialname}</span>
					</td>
				</tr>				
				<tr>
					<th>产品数量</th>
					<td><span id="pdtotalnumber">${ pdtotalnum}</span></td>
				</tr>
				<tr>
					<th>已排计划数量</th>
					<td><span id="pdplanednumber">${ pdplanednum}</span></td>
				</tr>
				<tr>
					<th>已完成数量</th>
					<td><span id="finishednumber">${ finishednum}</span></td>
				</tr>
				<tr>
					<th>剩余未排数量</th>
					<td><span id="remainednumber">${ remainednum}</span></td>
				</tr>
				</table>
				<br />
				<table class="table">
				<tr>
					<th>计划日期</th>
					<td><input id="dailyplansummary" name="dailyplansummary" class="easyui-textbox" data-options="required:true,validType:'isBlank',prompt:'单击选择'" value="${fn:substring(plandt,0,10)}" readonly/>
					<input id="pid" name="pid" type="hidden" value="${ dailyplan.pid}" />					
					</td>
				</tr>
				<tr>
					<th>生产订单号</th>
					<td><input id="productionorderno" name="productionorderno" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="${dailyplan.productionorderno }" /></td>
				</tr>	
				<tr>
					<th>产品工艺路线</th>
					<td><input id="processrealid" name="processrealid" class="easyui-combobox" data-options="required:true,validType:'isBlank'" value="" /></td>
				</tr>
				<tr>
					<th>产线</th>
					<td><input id="lineid" name="lineid" class="easyui-combobox" data-options="required:true,validType:'isBlank'"/></td>
				</tr>
				<tr>
					<th>产线产能</th>
					<td><span id="linecapacity">${dailyplan.line.capacity}</span>
					<input id="capacity" name="capacity" type="hidden" value="${dailyplan.line.capacity}"/></td>
				</tr>
				
				<tr>
					<th>本计划生产数量</th>
					<td>
						<input id="productquantity" name="productquantity" class="easyui-numberspinner" data-options="min:0,max:9999,required:true,validType:'isBlank',prompt:'1-9999'" style="width:130px;" value='${dailyplan.productquantity}'></input>
<!-- 						【包括：系统分配：<span id='sys'>-</span><input type="hidden" id="sysarrangenum" name="sysarrangenum" value="${dailyplan.productquantity}">,手动选择：<span id='man'>-</span>】 -->
						<input type="hidden" id="sysarrangenum" name="sysarrangenum" value="${dailyplan.productquantity}"/>
						<a href="#" class="easyui-linkbutton" onclick="selectOrderProductDetail();" >产品列表</a>					
					</td>
				</tr>
				<tr>
					<th>计划下达日期</th>
					<td>
						<input id="stdt" name="stdt" class="Wdate" value="<c:if test="${fn:substring(dailyplan.stdt,0,4)=='1900' }"></c:if><c:if test="${fn:substring(dailyplan.stdt,0,4)!='1900' }">${fn:substring(dailyplan.stdt,0,19)}</c:if>"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" readonly/>
					</td>
				</tr>
				<tr>
					<th>计划完成日期</th>
					<td>
						<input id="eddt" name="eddt" class="Wdate" value="<c:if test="${fn:substring(dailyplan.eddt,0,4)=='1900' }"></c:if><c:if test="${fn:substring(dailyplan.eddt,0,4)!='1900' }">${fn:substring(dailyplan.eddt,0,19)}</c:if>"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" readonly/>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td><input id="remark" name="remark" class="easyui-textbox" data-options="multiline:true" style="overflow:auto;width:100%;height:80px;" value="${dailyplan.remark}" validtype="length[0,250]" invalidMessage="有效长度0-250" />
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<th>计划状态</th> -->
<!-- 					<td><select id="status" name="status" class="easyui-combobox" data-options="panelHeight:'auto'"> -->
<!-- 								<option value="0" selected>计划编制</option> -->
<!-- 								<option value="1" >计划下发</option> -->
<!-- 								<option value="3">计划作废</option>																		 -->
<!-- 						</select> -->
<!-- 					</td> -->
<!-- 				</tr> -->
			</table>
			</div>
		</form>
			<br/>
			<div id="div-view" class="easyui-panel" title="产品明细" data-options="collapsible:true" width="100%" >
				<div data-options="fit:true,border:false">
					<table id="grid" data-options="border:false"></table>
				</div>
			</div>
			<div id="div_product" class="easyui-panel" title="产品明细" data-options="collapsible:true,closed:true" >
				<table id="producttable" title="产品明细" class="table" data-options="border:false,region:'center'" >
				<tr><th style="width:10%; text-align:center">序号</th>
				<th style="width:50%; text-align:center"">产品序列号</th>
				<th style="width:40%; text-align:center"">原生产订单号</th></tr>
				</table>
			</div>		
</body>
</html>
