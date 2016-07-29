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
	var selectedRows='';
	$(function() {
		var salesorderid='${salesorderid}';
		grid = $('#grid').datagrid({
			view: detailview,
            detailFormatter:function(index,row){
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
			title : '',
			url : ext.contextPath + '/material/orderproductdetail/getlistForSelect.do?flag=listpage&salesorderid='+salesorderid,
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: true,
			ctrlSelect:true,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [
// 				{checkbox:true , field : 'ck'}, 				
				{width : '100', title : '销售订单号',  field: 'salesorderproduct', sortable : true, halign:'center' , formatter : function(value, row){
							if(value !=null){
								return value.salesorderno;
							}else{
								return value;
							}
						 } },	 
						{width : '100', title : '生产订单号', field : 'productionorderno', sortable : true, halign:'center' }, 
		// 				{width : '100', title : '料号', field : 'bomid', sortable : true, halign:'center' },
						{width : '150', title : '产品信息', field : 'product', sortable : true, halign:'center' , formatter : function(value, row){
							if(value!=null){
								return value.materialname;
							}else{
								return "";
							}
						}}, 
						{width : '80', title : '该计划产品数量', field : 'productquantity', sortable : true, align:'center'}, 
						{width : '60', title : '完成数', field : 'finishedquantity', sortable : true, align:'center' },  
						{width : '130', title : '计划下达日期', field : 'stdt', sortable : true, halign:'center', formatter : function(value, row){
							if(value.substring(0,4)=="1900"){
								return "";
							}else{
								return value.substring(0,19);
							}					
							} },
						{width : '130', title : '计划完成日期', field : 'eddt', sortable : true, halign:'center', formatter : function(value, row){
							if(value.substring(0,4)=="1900"){
								return "";
							}else{
								return value.substring(0,19);
							}
							} },
						{width : '100', title : '计划状态', field : 'status', sortable : true, align:'center', formatter : function(value, row){
								switch(value){
									case "0": return "计划编制";	
									case "1": return "计划下发";	
									case "2": return "计划执行";	
									case "3": return "已作废";	
									case "4": return "已重排";	
									case "6": return "已完成";								
								}
							} }				 
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);				
				//展开所有的行
// 				for(var i=0;i<data.rows.length;i++){
// 					$('#grid').datagrid('expandRow',i);
// 				}
			},		
            onExpandRow: function(index,row){
            	var parentPONo=row.productionorderno;
                ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
                ddv.datagrid({                	
                    url:ext.contextPath + '/material/orderproductdetail/getlist_detail.do?productionorderno='+row.productionorderno,
                    fitColumns:true,
                    ctrlSelect:true,
					singleSelect: false,
					selectOnCheck: false,
					checkOnSelect: false,
					idField : 'id',
                    rownumbers:true,
                    //loadMsg:'',
                    height:'auto',
//                     rowStyler: function(index,row){
//                     	var rowstyler='';
// 		        		if (row.taskchangedstatus!=null){        		
// 		        			if(row.taskchangedstatus=='true' ||row.taskchangedstatus=='TRUE'){
// 		        				rowstyler+= 'font-weight:bold;';
// 		        			}
// 		        		}
// 		        		if(row.status=='3'){
// 		        			rowstyler+= ' background-color:#C0C0C0;color:#fff;';
// 		        		}
// 		        		return rowstyler;
// 		    		},
                    columns : [ [ 
						{checkbox:true , field : 'ck'}, 
						{width : '100', title : '产品序列号',  field: 'productuid', sortable : true, halign:'center' , formatter : function(value, row){
								return value;
						 } },	 
						{width : '100', title : '生产订单号', field : 'productionorderno', sortable : true, halign:'center' }, 
						{width : '100', title : '生产任务', field : 'workorder', sortable : true, align:'center' , formatter : function(value, row){							
							if(value!=null){
								return value.productionorderno;
							}else{
								return "未排任务";
							}
						}},
						{width : '150', title : '工艺路线', field : 'processrealid', sortable : true, align:'center' , formatter : function(value, row){
							if(value!=null){
								return "已设置";
							}else{
								return "未设置";
							}
						}}, 
						{width : '80', title : '状态', field : 'finishflag_wfo', sortable : true, align:'center' , formatter : function(value, row){
							if(value!=null&&row.productionorderno!=parentPONo){
								return "已重排";
							}else{
								if(value!=null){
									if(value=='0' && row.workorder!=null && row.workorder.wforderid!=null){
										return "已完成";
									}else{
										return "未完成";
									}								
								}else{
									return "未完成";
								}
							}							
						  },styler: function(value,row,index){
			        		if(value!=null){
								if(value=='0' && row.workorder!=null && row.workorder.wforderid!=null){
									return 'font-weight:bold;color:red;';
								}else{
									return null;
								}								
							}else{
								return null;
							}
	    		  		}}
						
					] ],
                    onResize:function(){
                        $('#grid').datagrid('fixDetailRowHeight',index);
                    },
                    onLoadSuccess:function(){
                   		 $('.iconImg').attr('src', ext.pixel_0);
                        setTimeout(function(){
                            $('#grid').datagrid('fixDetailRowHeight',index);
                        },0);
                        
                     },
                     onCheck:function(index,row){
                     	//alert(row.productuid);
                    	//selectedRows.push(row);
                    	//alert(row);
                    	var jsonstr ='';
                    	if(row.workorder!=null){
	                    	jsonstr = '{"id":"'+row.id
		        			+'","productuid":"'+row.productuid
		        			+'","productionorderno":"'+row.productionorderno
		        			+'","workorderid":"'+row.workorder.id		
		        			+'"}';
                    	}else{
                    		jsonstr = '{"id":"'+row.id
		        			+'","productuid":"'+row.productuid
		        			+'","productionorderno":"'+row.productionorderno
		        			+'","workorderid":"'		
		        			+'"}';
                    	}
                    	
	        			if(selectedRows!=''){
	        				selectedRows+=','+jsonstr;
	        			}else{
	        				selectedRows+=jsonstr;
	        			}
                     }
                });
            }
		});		
	});
    function selectOK() {
    //alert(selectedRows);    	
		  	return selectedRows;
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
								<td>产品序列号</td>
								<td><input name="search_name" class="easyui-textbox" /></td>								
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