<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
SessionManager sessionManager = new SessionManager();
%>
<!DOCTYPE HTML>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/material/salesorderproduct/getSalesOrderProducts.do?flag=select',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: true,
			selectOnCheck: true,
			checkOnSelect: true,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			rowStyler: function(index,row){
            	var rowstyler='';
				if(row.status=='1'){
	    			rowstyler+= ' background-color:#C0C0C0;color:#fff;';
	    		}
				return rowstyler;
    		},
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '80', title : '销售订单编号',  field: 'salesorderno', sortable : true, halign:'center' },	 
				{width : '180', title : '产品名称', field : 'product', sortable : true, halign:'center' , formatter : function(value, row) {
						if(value!=null){
                       		return value.materialname;
                       }else{
                       		return value;
                       }
				}}, 
				{width : '150', title : '物料编码', field : 'materialcode', sortable : true, halign:'center' , formatter : function(value, row) {
						var value=row.product;
						if(value!=null){							
                       		return value.materialcode;
                       }else{
                       		return value;
                       }
				}},
				{width : '65', title : '交货时间', field : 'deliverydate', sortable : true, halign:'center',formatter : function(value, row){
					return value.substring(0,19);
				},
					styler: function(value,row,index){					
	        		if (value!=null){
		        		var dt1 = new Date(value.substring(0,19).replace(/-/g,"/"));
		        		var dt2 = new Date();
						dt2.setDate(dt2.getDate()+7);//一周时间						
		        		if(dt1<=dt2){
		        			return 'font-weight:bold;color:red;';
		        		}else{
		        			return null;
		        		}       		
	            		 
	        		}
	    		  }
				
				},
				{width : '60', title : '产品数量', field : 'orderproductdetail', sortable : true, halign:'center',formatter : function(value, row){
					return value.length;
				}},
				{width : '80', title : '已排计划数量', field : 'planednum', sortable : true, halign:'center',formatter : function(value, row,index){
					var pdetaillist=row.orderproductdetail;
					var planednum=0;
					for(var i=0;i<pdetaillist.length;i++){
						if(pdetaillist[i].productionorderno!=null&&pdetaillist[i].productionorderno!=''){
							planednum++;
						}
					}
					return planednum;
				}},
				{width : '70', title : '已完成数量', field : 'finishednum', sortable : true, halign:'center',formatter : function(value, row,index){
					var pdetaillist=row.orderproductdetail;
					var finishednum=0;
					for(var i=0;i<pdetaillist.length;i++){
						//if(pdetaillist[i].productionorderno!=null&&pdetaillist[i].productionorderno!=''){//已排计划
							if(pdetaillist[i].workorderid!=null&&pdetaillist[i].workorderid!=''){//已排任务
								if(pdetaillist[i].finishflag_wfo=='0'){
									finishednum++;
								}//产品已完成							
							}
						//}
					}
					return finishednum;
				}},
				{width : '80', title : '剩余未排数量', field : 'remainednum', sortable : true, halign:'center',formatter : function(value, row,index){
					var pdetaillist=row.orderproductdetail;
					var planednum=0;
					for(var i=0;i<pdetaillist.length;i++){
						if(pdetaillist[i].productionorderno!=null&&pdetaillist[i].productionorderno!=''){
							planednum++;
						}
					}
					return pdetaillist.length-planednum;
				}},
				{width : '80', title : '订单产品状态', field : 'status', sortable : true, halign:'center',formatter : function(value, row,index){
					if(value=='1'){
						return "已完成";
					}else {
						return "未完成";
					}					
				}}
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
			}
		});
	});
 function selectOK() { 		
		var row_pdtotalnum=0;
		var row_pdplanednum=0;
		var row_remainednum=0;
		var row_finishednum=0;	    	
        var jsonstr = "";
        var row = $('#grid').datagrid('getSelected');                
        if (row){        
	        //计算        
	        var pdetaillist=row.orderproductdetail;
	        row_pdtotalnum=pdetaillist.length;	        
			for(var i=0;i<row_pdtotalnum;i++){
				if(pdetaillist[i].productionorderno!=null&&pdetaillist[i].productionorderno!=''){//已排计划
					row_pdplanednum++;					
				}
				if(pdetaillist[i].workorderid!=null&&pdetaillist[i].workorderid!=''){//已排任务
					if(pdetaillist[i].finishflag_wfo=='0'){//产品已完成
						row_finishednum++;
					}							
				}
			}
			row_remainednum=row_pdtotalnum-row_pdplanednum;			
	        //以json格式返回数据      
        	jsonstr = '{"salesorderid":"'+row.id
        			+'","salesorderno":"'+row.salesorderno
        			+'","materialname":"'+row.product.materialname
        			+'","pdtotalnum":"'+row_pdtotalnum
        			+'","pdplanednum":"'+row_pdplanednum
        			+'","remainednum":"'+row_remainednum
        			+'","finishednum":"'+row_finishednum  
        			+'","productid":"'+row.product.id  			
        			+'"}';
	        var dataset = $.parseJSON(jsonstr);
	        return dataset;
        }else{
        	return 0;
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
						<table>
							<tr >
							
								<td>销售订单编号</td>
								<td><input name="search_name" class="easyui-textbox" data-options="prompt:'请输入关键字'" style="width: 150px;" value=""/></td>
								<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-search',plain:true" 
									onclick="grid.datagrid('load',ext.serializeObject($('#searchForm')));">搜索</a>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" 
									onclick="$('#searchForm').form('clear');grid.datagrid('load',{});">重置</a>
								</td>
								<td><div class="datagrid-btn-separator"></div></td>
								<td >
 									显示已完成订单&nbsp;&nbsp;<input id="mm" class="easyui-switchbutton" style="height:22px;width:50px;" data-options="onText:'是',offText:'否'" unchecked></td>
 									<input id="displayfinished" name="displayfinished" type="hidden" value="" />
 									<script type="text/javascript"> 
									  $(function(){ 
									    $('#mm').switchbutton({ 
									      onChange: function(checked){
									    	  if(checked==true){
									    		  $('#displayfinished').val("1");
									    	  }else{
									    		  $('#displayfinished').val("");
									    	  }									    	  
									        //alert($('#mm').switchbutton('options').checked); 
									    	  grid.datagrid('load',ext.serializeObject($('#searchForm')));//搜索
									      } 
									    }) 
									  }) 
									</script>
 									
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
 
