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
	var salesorderid='${salesorderid}';
	var totalnum='${totalnum}';		
	$(function() {
		var plandetailjson=parent.$('#'+'${param.iframeId}')[0].contentWindow.$("#grid").datagrid("getData");			
		var selectedp=parent.$('#'+'${param.iframeId}')[0].contentWindow.selectedProducts;
		//changeSysArrangeList(totalnum,salesorderid);
		grid = $('#grid').datagrid({	               	
                    url:ext.contextPath + '/material/orderproductdetail/getlistall_detailOfSalesOrder.do?salesorderid='+salesorderid+'&plandate=${param.plandate}&planid=${param.planid}',
                    striped : true,
					rownumbers : true,
					pagination : true,
					singleSelect: false,
					selectOnCheck: true,
					checkOnSelect: true,
					idField : 'id',
					pageSize : 50,
					pageList : [ 20, 50, 100],
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
						{width : '220', title : '产品序列号',  field: 'productuid', sortable : true, halign:'center' , formatter : function(value, row){
								return value;
						 } },	 
						{width : '100', title : '生产订单号', field : 'productionorderno', sortable : true, halign:'center' }, 
						{width : '100', title : '工单状态', field : 'workorder', sortable : true, align:'center' , formatter : function(value, row){							
							if(value!=null){
								return "已排工单";
							}else{
								return "未排工单";
							}
						}},
						{width : '80', title : '工艺路线', field : 'processrealid', sortable : true, align:'center' , formatter : function(value, row){
							if(value!=null){
								return "已设置";
							}else{
								return "未设置";
							}
						}}, 
						{width : '80', title : '状态', field : 'finishflag_wfo', sortable : true, align:'center' , formatter : function(value, row){
// 							if(value!=null&&row.productionorderno!=parentPONo){
// 								return "已重排";
// 							}else{
								if(value!=null){
									if(value=='0' && row.workorder!=null && row.workorder.wforderid!=null){
										return "已完成";
									}else if(value=='8'){
										return "计划已重排";
									}else{
										return "未完成";
									}								
								}else{
									return "未完成";
								}
							//}							
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
                    onLoadSuccess:function(data){
                   		 $('.iconImg').attr('src', ext.pixel_0);
                        setTimeout(function(){
                            $('#grid').datagrid('fixDetailRowHeight',index);
                        },0);                        
                        if(data){
								$.each(data.rows, function(index, item){									
									if(selectedp!=null && selectedp!=''){
										var selectedpstr=selectedp;
										var sparr=selectedpstr.split("}");
										var dataset;
										for(var i=0;i<sparr.length-1;i++){
											if(i==0){
												dataset = $.parseJSON(sparr[i]+"}");												 
											}else{
												dataset = $.parseJSON(sparr[i].substring(1,sparr[i].length)+"}");
											}
											if(item.id==dataset.id){
												$('#grid').datagrid('checkRow', index);												
											}								
										}
									}
									else if(selectedp==''){//第一次打开页面勾选
										for(var i=0;i<plandetailjson.rows.length;i++){
											if(item.id==plandetailjson.rows[i].id){
												$('#grid').datagrid('checkRow', index);												
											}
										}
									}									
								});
							}						
                     },
                     onCheck:function(index,row){
                     	//alert(row.productuid);
                    	//selectedRows.push(row);
                    	//alert(row);
//                     	var jsonstr ='';
//                     	if(row.workorder!=null){
// 	                    	jsonstr = '{"id":"'+row.id
// 		        			+'","productuid":"'+row.productuid
// 		        			+'","productionorderno":"'+row.productionorderno
// 		        			+'","workorderid":"'+row.workorder.id		
// 		        			+'"}';
//                     	}else{
//                     		jsonstr = '{"id":"'+row.id
// 		        			+'","productuid":"'+row.productuid
// 		        			+'","productionorderno":"'+row.productionorderno
// 		        			+'","workorderid":"'		
// 		        			+'"}';
//                     	}
                    	
// 	        			if(selectedRows!=''){
// 	        				selectedRows+=','+jsonstr;
// 	        			}else{
// 	        				selectedRows+=jsonstr;
// 	        			}
	        			var checkedItems = $('#grid').datagrid('getChecked');
	        			checkNumber(checkedItems.length,row);
                     },
                     onUncheck:function(index,row){
                     	var checkedItems = $('#grid').datagrid('getChecked');
	        			checkNumber(checkedItems.length,row);

                     }
                });
    });
    function selectOK() {
    	var checkedItems = $('#grid').datagrid('getChecked');
    	if(checkedItems==null || checkedItems==""){
    		top.$.messager.alert('提示', "勾选数量（计划生产数量）不能为0", 'info');
    		return;
    	}
		var sl='';
		$.each(checkedItems, function(index, item){
			jsonstr = '{"id":"'+item.id
      			+'","productuid":"'+item.productuid
      			+'","productionorderno":"'+item.productionorderno
      			+'","workorderid":"'+item.workorderid	
      			+'"}';
      			if(sl!=''){
      				sl+=','+jsonstr;
      			}else{
      				sl+=jsonstr;
      			}
		});    	
		return sl;
    }
    function checkNumber(checkedlength,row){
//     	if(checkedlength==0){
//     		top.$.messager.alert('提示', "勾选数量（计划生产数量）不能为0", 'info');
//     		$('#grid').datagrid('checkRow',$('#grid').datagrid('getRowIndex',row));//撤销不勾选
//     		return;
//     	}
    	if(checkedlength>totalnum){
    		top.$.messager.alert('提示', "勾选数量超过计划总数", 'info');
    		$('#grid').datagrid('uncheckRow',$('#grid').datagrid('getRowIndex',row));//撤销勾选
    		return;
    	}
    	$("#man").html(checkedlength);
	    $("#sys").html(totalnum-checkedlength);
	    //changeSysArrangeList(totalnum-checkedlength,salesorderid);
    }
    function changeSysArrangeList(sysnum,salesorderid){
    	$.post(ext.contextPath + "/material/orderproductdetail/getSysArrangeList.do", {sysnum:sysnum,salesorderid:salesorderid}, function(data) {
    		var tb= document.getElementById('producttable');
			var rowNum=tb.rows.length;		
			for(var j=1;j<rowNum;j++){
				tb.deleteRow(j);
				rowNum=rowNum-1;
				j=j-1;
			}
			for(var i=1;i<=data.rows.length;i++){
				var htmlstr="<tr><td style='text-align:center'>"+i+"</td><td>"+data.rows[i-1].productuid+"</td></tr>"
				$("#producttable").append(htmlstr);
			}
			var sysar= $("#sys").html();
			document.getElementById("prompt").style.visibility="hidden";//隐藏  
			if(sysar>data.rows.length){
				document.getElementById("prompt").style.visibility="visible";//显示  
				$("#promptnum").html(data.rows.length);
			}
  		},'json');
    }
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="a1" class="easyui-layout" data-options="fit:true,border:false">
		<%-- <div data-options="region:'west',title:'系统分配产品（分配数量：<span id=\'sys\' style=\'color:red\'>${totalnum}</span>）',split:false"
			style="width:30%;">		
				<table id="producttable" title="产品明细" class="table" data-options="border:false,region:'center'" >
				<tr><th style="width:20%; text-align:center">序号</th>
				<th style="width:80%; text-align:center">产品序列号</th></tr>
				</table>				
			<span id="prompt"  style="visibility:hidden;">最大可分配数目&nbsp;&nbsp;<span id="promptnum"  style="color:red;"></span></span>			
		</div> --%>
		<div data-options="region:'center',title:'该订单产品所有明细（勾选数量：<span id=\'man\' style=\'color:red\'>0</span>）'" style="padding:0px;">
			<div id="a2" class="easyui-layout" data-options="fit:true,border:false">
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
			</div>
		</div>
	</div>

</body>
</html>