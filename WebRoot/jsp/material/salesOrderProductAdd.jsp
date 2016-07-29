<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,grid) {
		var tb= document.getElementById('detailtable');
		var rowNum=tb.rows.length;	
		if(rowNum==1){
			createDetails();
		}
		if($("#salesorderno").val()!=""&&$("#productid").val()!=""){
			if($("#ordercreatedate").val()==''){
				top.$.messager.alert("系统提示","请选择订单创建时间","info");
				return false;
			}
			if($("#orderfinishdate").val()==''){
				top.$.messager.alert("系统提示","请选择订单完成时间","info");
				return false;
			}
			if($("#deliverydate").val()==''){
				top.$.messager.alert("系统提示","请选择发货时间","info");
				return false;
			}
		}
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/material/salesorderproduct/save.do", $(".form").serialize(), function(data) {
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
	$(function() {
		$('#clientsid').combobox({ 
			url:ext.contextPath + '/material/clients/getlistForSelect.do', 
			valueField:'id', 
			textField:'name',
			editable:false,
			panelHeight:'auto'
		});
		
		$("#productcode").textbox('textbox').bind("click", function () { 
			selectMaterial("productcode","productname","成品");
		});
								
		$("#productnum").numberspinner({
		    onChange:function(){
		        createDetails();
		  }
		});
	});
	function dodistribute(dialog,grid){
		$("#status").val("3");//下发
		dosave(dialog,grid);
	}
	function createDetails(){
		if($("#salesorderno").val()==""){
			top.$.messager.alert("系统提示","销售订单编号不可为空","info");
			return false;
		}
		if($("#productid").val()==""){
			top.$.messager.alert("系统提示","产品编码不可为空","info");
			return false;
		}
		var tb= document.getElementById('detailtable');
		var rowNum=tb.rows.length;		
		for(var j=1;j<rowNum;j++){
			tb.deleteRow(j);
			rowNum=rowNum-1;
			j=j-1;
		}
	//序号=订单编号+产品ID+数字?
		var num=$("#productnum").val();		
		for(var i=1;i<=num;i++){
			var numstr=i<100?(i<10?"00"+i:"0"+i):i;
			var valstr=$("#salesorderno").val()+"."+$("#productcode").val()+numstr;
			var htmlstr="<tr style='height:22px'><td style='text-align:center'>"+i+"</td><td>"+valstr+"<input type='hidden' id='product"+i+"' name='product"+i+"' value='"+valstr+"'  readonly/></td></tr>"
			$("#detailtable").append(htmlstr);
		}	
	}
	function selectMaterial(productcode,productname,typename){//	typename 中文用,隔开多个			
			var dialog = parent.ext.modalDialog({
			title : '选择物料',
			width: 780,
			height:480,			
			url : ext.contextPath + '/material/materialinfo/selectMaterial.do?typename='+encodeURI(encodeURI(typename)), 
				buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectOK();
					if(res!=null){
						dialog.dialog('destroy');
						$("#"+productcode).textbox('setValue',res.materialcode);//easyui textbox赋值jquery不一样
						$("#productid").val(res.materialid);
						var namestr=(res.materialname == 'undefined')?'':res.materialname;						
						$("#"+productname).html(namestr);	
					}
					
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
	
</script>
</head>
<body>
		<form method="post" class="form">
		<div class="easyui-panel" title="销售订单信息" style="padding:10px;"
		data-options="tools:'#tt'">
		<input id="status" name="status" type="hidden" value="0"/>		
			<table class="table">
				<tr>
					<th>销售订单编号</th>
					<td><input id="salesorderno" name="salesorderno" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="" /></td>
				</tr>
				<tr>
					<th>产品编码</th>
					<td>
						<input id="productcode" name="productcode" class="easyui-textbox" data-options="required:true,validType:'isBlank'" value="" />
						<input id="productid" name="productid" type="hidden"/>  
					</td>
				</tr>
				<tr>
					<th>产品名称</th>
					<td><span id="productname"></span></td>
				</tr>
				<tr>
					<th>产品数量</th>
					<td>
<!-- 						<input id="productnum" name="productnum" class="easyui-textbox" value="" /> -->
						<input id="productnum" name="productnum" class="easyui-numberspinner" data-options="min:1,max:999,required:true,validType:'isBlank',prompt:'1-999'" style="width:130px;" value='1' ></input>
<!-- 						<a href="#" class="easyui-linkbutton" onclick="createDetails();">生成序列号</a> -->
					</td>
				</tr>
				<tr>
					<th>所属客户</th>
					<td>
						<input id="clientsid" name="clientsid" class="easyui-combobox" value="" />
					</td>
					
				</tr>
				<tr>
					<th>订单创建时间</th>
					<td>
						<input id="ordercreatedate" name="ordercreatedate" class="Wdate" value=""
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
							readonly>
					</td>
				</tr>
				<tr>
					<th>订单完成时间</th>
					<td>
						<input id="orderfinishdate" name="orderfinishdate" class="Wdate" value=""
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
							readonly>
					</td>
				</tr>
				<tr>
					<th>发货时间</th>
					<td>
						<input id="deliverydate" name="deliverydate" class="Wdate" value=""
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
							readonly>
					</td>
				</tr>
			</table>
		</div>
		<br/>
		<div class="easyui-panel" title="产品明细">
			<table id="detailtable" class="table">
				<tr>
					<th style="width:3px;text-align:center">序号</th>
					<th style="text-align:center">产品序列号</th>
				</tr>
			</table>
		</div>
		</form>
		
</body>
</html>
