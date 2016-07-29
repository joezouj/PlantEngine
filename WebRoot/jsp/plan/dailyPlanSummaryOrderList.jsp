<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="com.sipai.tools.SessionManager"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
SessionManager sessionManager = new SessionManager();
%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE HTML>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	var gridAll;
	var invalidategrid;
	var ddv;
	var ddvall;
	var ddvpid='${dailyplansummary.id }';
	var order_n0;
	var order_n;
	var index_n0;
	var index_n;
	var allowDndflag=0;
	var addFun = function() {
		var dialog = parent.ext.modalDialog({
			iframeId:'add',
			title : '新增日计划',		
			url : ext.contextPath + '/plan/dailyplan/add.do?iframeId=add&date=${param.sdt}&plandate=${dailyplansummary.plandt}&pid=${dailyplansummary.id }',
			buttons : [
			{
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			}, 
			{
				text : '提交',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosubmit(dialog, grid);
				}
			}
			
			 ]
		});
	};
	var viewFun = function(id) {
		//grid.datagrid('clearChecked');
		var dialog = parent.ext.modalDialog({
			width: 800,
			height:600,
			title : '查看日计划',
			url : ext.contextPath + '/plan/dailyplan/view.do?id=' + id,
			buttons : [ {
				text : '确认完成',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dofinish(dialog, grid);
				}
			}]
		});
		grid.datagrid('reload');
		gridAll.datagrid('reload');		
	};
	var doedit = function(id) {
		var dialog = parent.ext.modalDialog({			
			width: 480,
			height:320,
			title : '编辑日计划信息',
			url : ext.contextPath + '/plan/dailyplansummary/edit.do?id='+id+'&iframeId=${param.iframeId}',
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	var doaudit = function() {
		var dialog = parent.ext.modalDialog({
			iframeId:'audit',
			width: 480,
			height:320,
			title : '审核日计划信息',
			url : ext.contextPath + '/plan/dailyplansummary/audit.do?id=${dailyplansummary.id }'+'&iframeId=${param.iframeId}'+'&iframeId=audit',
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog);
				}
			} ]
		});
	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			iframeId:'edit',
			width: 800,
			height:620,
			title : '编辑日计划',
			url : ext.contextPath + '/plan/dailyplan/edit.do?id=' + id+'&iframeId=edit',
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			}, 
			{
				text : '提交',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosubmit(dialog, grid);
				}
			} ]
		});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？计划删除后工单将自动作废。', function(r) {
			if (r) {
				$.post(ext.contextPath + '/plan/dailyplan/delete.do', {id : id}, function(data) {
					if(data==1){
						top.$.messager.alert('提示','删除成功','info');
						grid.datagrid('reload');
					}else{
						top.$.messager.alert('提示','删除失败','info');
					}
				});
			}
		});
	};
	var deletesFun = function() {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		$.each(checkedItems, function(index, item){
			datas+=item.id+",";
		});
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要删除的记录','info');
		}else{
			top.$.messager.confirm('提示', '您确定要删除多条记录？计划删除后工单将自动作废。', function(r) {
				if (r) {
					$.post(ext.contextPath + '/plan/dailyplan/deletes.do', {ids:datas} , function(data) {
						if(data>0){
							top.$.messager.alert('提示','成功删除'+data+'条记录','info');
							grid.datagrid('reload');
							grid.datagrid('clearChecked');
						}else{
							top.$.messager.alert('提示','删除失败','info');
						}
					});
				}
			});
		}
	};

	var flag=0;
	$(function() {		
          grid = $('#grid').datagrid({
   			title : '',
   			url : ext.contextPath + '/plan/dailyplansummary/getlist.do',
   			queryParams: {
   				sdt:'${wsdt}',
   				edt:'${wedt}',
   				nostatus:2
   			},
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
  				{width : '180', title : '日计划单号', field : 'name', halign:'center',align:'left'},
  				{width : '180', title : '日计划日期', field : 'plandt', halign:'center',align:'left',formatter : function(value, row){
  					return value.substring(0,10);
  				}},
  				{width : '180', title : '状态', field : 'status', halign:'center',align:'left',formatter : function(value, row){
  					switch(value){
						case "0": return "未审核";break;
						case "1": return "已完成";break;
						case "3": return "已审核";break;
						default: return '';						
					}
  				}},
  				{width : '180', title : '备注', field : 'remark', halign:'center',align:'left'},
  				{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
					var str = '';
						str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewPlanFun(\''+row.plandt+'\',\'${param.sdt}\',\'${param.edt}\',\''+row.id+'\');"/> ';
						<%if (sessionManager.havePermission(session,"plan/dailyplan/edit.do")) {%>
							str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="doedit(\''+row.id+'\');"/> ';
						<%}%>
						return str;
  					}
  				}
   			] ],
   			toolbar : '#toolbar',
 			onDblClickRow:function(index,row){
 				viewPlanFun(row.plandt,'${param.sdt}','${param.edt}',row.id);
 			},
 			onLoadSuccess:function(){
          		 $('.iconImg').attr('src', ext.pixel_0);
 			}
   		});	
		$('#tt').tabs({
			onSelect: function(title){//点到tab才加载
				if(title=='已作废计划单'){
					invalidategrid = $('#invalidategrid').datagrid({
			 			title : '',
			 			url : ext.contextPath + '/plan/dailyplansummary/getinvalidDPS.do?sdt=${wsdt}&edt=${wedt}',
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
							{width : '150', title : '单号', field : 'name', align:'center'},
							{width : '100', title : '计划日期', field : 'plandt', halign:'center',align:'center', formatter : function(value, row){
								if(value!=null){
									return value.substring(0,10);
								}																
							}},
							{width : '180', title : '备注', field : 'remark', halign:'center',align:'left'},
							{width : '100', title : '作废人', field : '_modifiername', halign:'center',align:'center'},
							{width : '150', title : '作废日期', field : 'modifydt', halign:'center',align:'center'}
			 			] ],
			 			toolbar : '#toolbarinvalid'
			 		});
				}else if(title=='全部计划'){
					gridAll = $('#gridAll').datagrid({
						view: detailview,
			            detailFormatter:function(index,row){
			                return '<div style="padding:2px"><table class="ddv"></table></div>';
			            },
						title : '',
						url : ext.contextPath + '/plan/dailyplan/getlist.do',
						queryParams: {
	                    	search_name:$('#search_nameAll').textbox('getValue'),
	                		sdt: $("#sdtAll").val(),
	                		edt: $("#edtAll").val(),
	                		search_status:$('#search_statusAll').combobox('getValue')
	                	},
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
							{checkbox:true , field : 'ck'}, 				
							{width : '250', title : '销售订单号', field : 'salesorderno', sortable : true, halign:'center'},	
							{width : '290', title : '产品名称', field : 'product', halign:'center',formatter : function(value,row){
								if(value!=null){
			                       		return value.materialname;
			                       }else{
			                       		return value;
			                       }
							},sortable:false},
							{width : '250', title : '订单产品数量', field : 'productnum',align:'center',sortable:false},
							{width : '250', title : '未排计划产品数', field : 'orderproductdetail',align:'center',formatter : function(value,row){
								if(value.length!=0){                       		
			                       		var unplanednum=0;
			                       		for(var i=0;i<value.length;i++){
			                       			if(value[i].productionorderno==null || value[i].productionorderno==""){
			                       				unplanednum++;
			                       			}
			                       		}
			                       		return unplanednum;
			                       }else{
			                       		return 0;
			                       }
							},sortable:false}				 
						] ],
						toolbar : '#toolbarAll',
						onLoadSuccess : function(data) {
							$('.iconImg').attr('src', ext.pixel_0);
							//if(flag==0){
								//flag=1;	
								//grid.datagrid('load',ext.serializeObject($('#searchForm')));//搜索					
							//}else{
								//展开所有的行
								for(var i=0;i<data.rows.length;i++){
									$('#gridAll').datagrid('expandRow',i);
								}
							//}			
							
						},		
			            onExpandRow: function(index,row){
			                var ddvallT = $(this).datagrid('getRowDetail',index).find('table.ddv');
			                ddvallT.datagrid({                	
			                    url:ext.contextPath + '/plan/dailyplan/getlist_detail.do',
			                    queryParams: {
			                    	salesOrderID: row.id,
			                    	search_name: $('#search_nameAll').textbox('getValue'),
			                		sdt: $("#sdtAll").val(),
			                		edt: $("#edtAll").val(),
			                		search_status: $('#search_statusAll').combobox('getValue')
			                	},
			                    fitColumns:true,
			                    ctrlSelect:true,
								singleSelect: false,
								selectOnCheck: false,
								checkOnSelect: false,
								idField : 'id',
			                    rownumbers:true,
			                    //loadMsg:'',
			                    height:'auto',
			                    rowStyler: function(index,row){
			                    	var rowstyler='';
					        		if (row.taskchangedstatus!=null){        		
					        			if(row.taskchangedstatus=='true' ||row.taskchangedstatus=='TRUE'){
					        				rowstyler+= 'font-weight:bold;';
					        			}
					        		}
					        		if(row.status=='3' || row.status=='4'){
					        			rowstyler+= ' background-color:#C0C0C0;color:#fff;';
					        		}else if(row.status=='6'){
					        			rowstyler+= ' font-style:ltalic;';
					        		}
					        		return rowstyler;
					    		},
			                    columns : [ [ 						
									 
									{width : '100', title : '生产订单号', field : 'productionorderno',  halign:'center' ,sortable:true}, 
					// 				{width : '100', title : '料号', field : 'bomid', sortable : true, halign:'center' },						 
									{width : '80', title : '计划生产数', field : 'productquantity',  halign:'center' ,sortable:false}, 
									{width : '60', title : '完成数', field : 'finishedquantity',  halign:'center' ,sortable:false}, 
									{width : '180', title : '工艺路线', field : 'processrealname',halign:'center' ,sortable:false},  
									{width : '120', title : '计划下达日期', field : 'stdt', align:'center', formatter : function(value, row){
										if(value.substring(0,4)=="1900"){
											return "";
										}else{
											return value.substring(0,19);
										}					
										} ,sortable:false},
									{width : '120', title : '计划完成日期', field : 'eddt',align:'center', formatter : function(value, row){
										if(value.substring(0,4)=="1900"){
											return "";
										}else{
											return value.substring(0,19);
										}
										} ,sortable:false},
									{width : '100', title : '计划状态', field : 'status', align:'center', formatter : function(value, row){
											switch(value){
												case "0": return "待提交";
												case "1": return "待审核";
												case "5": return "待下发";	
												case "2": return "已下发";										
												case "3": return "已作废";	
												case "4": return "已重排";
												case "6": return "已完成";							
											}
										} ,sortable:false},			    		    
									{title : '操作', field : 'action', width : '120', halign:'center', align:'center', formatter : function(value, row) {
											var str = '';
												str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\');"/> ';
												<%if (sessionManager.havePermission(session,"plan/dailyplan/edit.do")) {%>
												if(row.status=='0'){
													str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/> ';
												}	
												<%}%>
												<%if (sessionManager.havePermission(session,"plan/dailyplan/delete.do")) {%>
												str += '<img class="iconImg ext-icon-table_delete" title="删除" onclick="deleteFun(\''+row.id+'\');"/> ';
												<%}%>									
												<%if (sessionManager.havePermission(session,"plan/dailyplan/edit.do")) {%>
												if(row.status!='0'){
												str += '<img class="iconImg ext-icon-bin" title="计划作废" onclick="invalidateFun(\''+row.id+'\');"/> ';
												}
												<%}%>						
											return str;
										}
									},
									{width : '80', title : '提示', field : 'taskchangedstatus',align:'center', formatter : function(value, row){
											if(value=="true" ||value=="TRUE"){
												return '已生成工单<img class="iconImg ext-icon-new" title="任务新变动" onclick="viewFun(\''+row.id+'\');"/>';
											}else{												
												return "";
											}											
										} ,sortable:false} 
								] ],
			                    onResize:function(){
			                        $('#gridAll').datagrid('fixDetailRowHeight',index);
			                    },
			                    onLoadSuccess:function(){
			                   		 $('.iconImg').attr('src', ext.pixel_0);
			                        setTimeout(function(){
			                            $('#gridAll').datagrid('fixDetailRowHeight',index);
			                        },0);
			                        
			                     }
			                });
			            }
					});	
				}else if(title=='当前计划'){
					grid.datagrid('reload');//全部计划里面刷新grid会显示出错，这里再刷新一遍
				}
			  }
			});

	});
	var viewPlanFun=function(date,sdt,edt,id){
		var nowdate = new Date();
		var thisdate=new Date(date);
		var olddayflag=0;
		if(thisdate<nowdate &&thisdate.getDate()!=nowdate.getDate()){
			olddayflag=1;
		}
		switch('${param.pageflag}'){//页面选择
			case 'operate':var dialog = parent.ext.modalDialog({
				id:'viewplan3',
				iframeId:'viewp3',
				title : '查看计划',	
				height:"85%",
				width:"85%",
				url : ext.contextPath + '/plan/dailyplansummary/showlistoperate.do?&sdt='+sdt+'&edt='+edt+'&iframeId=viewp3&olddayflag='+olddayflag+'&id='+id
				
			});break;
			case 'materialplan':var dialog = parent.ext.modalDialog({
				id:'viewplan2',
				iframeId:'viewp2',
				title : '查看物料计划',	
				height:"85%",
				width:"85%",
				url : ext.contextPath + '/plan/materialplan/showplan.do?&planid='+id
				
			});break;
			default:var dialog = parent.ext.modalDialog({
				id:'viewplan1',
				iframeId:'viewp1',
				title : '查看计划',	
				height:"85%",
				width:"85%",
				url : ext.contextPath + '/plan/dailyplansummary/showlist.do?&sdt='+sdt+'&edt='+edt+'&iframeId=viewp1&olddayflag='+olddayflag+'&id='+id
				
			});
		}		
		
	};
	
	var invalidateFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要此记录作废？计划作废后将无法撤销。', function(r) {
			if (r) {
				$.post(ext.contextPath + '/plan/dailyplan/invalidate.do', {id : id}, function(data) {
					if(data==1){
						top.$.messager.alert('提示','作废成功','info');
						grid.datagrid('reload');
					}else{
						top.$.messager.alert('提示','作废失败','info');
					}
				});
			}
		});
	};
	var invalidateday = function() {
		parent.$.messager.confirm('提示', '您确定要作废全天计划？计划作废后将无法撤销。', function(r) {
			if (r) {
				var id='${dailyplansummary.id }';
				$.post(ext.contextPath + '/plan/dailyplansummary/invalidateday.do', {id : id}, function(data) {
					if(data==1){
						top.$.messager.alert('提示','作废成功','info');
						parent.$("#viewDialog").dialog('destroy');
					}else{
						top.$.messager.alert('提示','作废失败','info');
					}
				});
				
			}
		});
	};
	var distributeday = function() {
		parent.$.messager.confirm('提示', '您确定要下发全天计划？', function(r) {
			if (r) {
				var id='${dailyplansummary.id }';
				$.post(ext.contextPath + '/plan/dailyplansummary/distributeday.do', {id : id}, function(data) {
					if(data.res=='1'){
						top.$.messager.alert('提示','下发成功','info');
						grid.datagrid('reload');
					}else if(data.res=='0'){
						top.$.messager.alert('提示','无可下发计划','info');
					}else{
						top.$.messager.alert('提示',data.res,'info');
					}
				},'json');
				
			}
		});
	};
	var submitday = function() {
		parent.$.messager.confirm('提示', '您确定要提交全天待提交计划？', function(r) {
			if (r) {
				var id='${dailyplansummary.id }';
				$.post(ext.contextPath + '/plan/dailyplansummary/submitday.do', {id : id}, function(data) {
					if(data.res=='1'){
						top.$.messager.alert('提示','提交成功','info');
						grid.datagrid('reload');
					}else if(data.res=='0'){
						top.$.messager.alert('提示','无可提交计划','info');
					}else{
						top.$.messager.alert('提示','提交失败','info');
					}
				},'json');
				
			}
		});
	};
	function showYesterday(){
		var yesterday=getDateStartEnd("yesterday");		
		$("#sdt").val(yesterday[0]);
		$("#edt").val(yesterday[1]);
		grid.datagrid('load',ext.serializeObject($('#searchForm')));//搜索
	}
	function showToday(){
		var today=getDateStartEnd("today");		
		$("#sdt").val(today[0]);
		$("#edt").val(today[1]);
		grid.datagrid('load',ext.serializeObject($('#searchForm')));//搜索
	}
	function showThisWeek(){
		var thisweek=getDateStartEnd("week");		
		$("#sdt").val(thisweek[0]);
		$("#edt").val(thisweek[1]);
		grid.datagrid('load',ext.serializeObject($('#searchForm')));//搜索
	}
	function reset(){
		$('#searchForm').form('clear');
		grid.datagrid('load',{sdt:'${wsdt}',edt:'${wedt}'});
		$('#sdt').val('${wsdt}');
		$('#edt').val('${wedt}');
	}
	function allowDnd(){
		grid.datagrid('enableDnd');
		var nad =document.getElementById("notallowDndbtn");
		var ad =document.getElementById("allowDndbtn");
		nad.style.display="";
		ad.style.display="none";
		showTips('列表拖拽已启用', 1);
		//grid.datagrid('reload');
	}
	function notallowDnd(){
		grid.datagrid('reload');
		var nad =document.getElementById("notallowDndbtn");
		var ad =document.getElementById("allowDndbtn");
		ad.style.display="";
		nad.style.display="none";
		showTips('列表拖拽已禁用', 1);
	}	
	function ToTop(id,pid,porder){
		$.post(ext.contextPath + '/plan/dailyplan/drag.do', {order_n0:porder,order_n:0,pid:pid,id:id} , function(data) {
     	 	if(data==0){
     	 		return;
     	 	}else if(data>0){
     	 			//allowDndflag=1;//允许拖拽
					grid.datagrid('reload');	      							
				}else{
					top.$.messager.alert('提示','操作失败','info');
				}
			});
	}
	$(function(){//物料计划进入，隐藏全部计划
		if('${param.pageflag}'=='materialplan'){
			$('#tt').tabs('close', 2); 
		}
	});
	var dpsaddFun = function(date,sdt,edt){
		var dialog = parent.ext.modalDialog({
			title : '新增日计划单信息',		
			url : ext.contextPath + '/plan/dailyplansummary/add.do?date='+date+'&sdt='+sdt+'&edt='+edt,
			buttons : [
			{
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
					grid.datagrid('reload');
				}
			}
			
			 ]
		});
	};
</script>
</head>
<body >
	<div id="tt" class="easyui-tabs" data-options="fit:true,border:false">
		<div title="计划单" style="padding:0px;">
		<div id="toolbar" style="display: none;">
								<table>
									<tr>
										<td>
											<table>
												<tr>
													<c:if test="${param.olddayflag!=1 }">
													<%if (sessionManager.havePermission(session,"plan/dailyplan/add.do")) {%>
													<td>
														<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
															onclick="dpsaddFun('${param.date}','${wsdt}','${wsdt}');">添加</a>
													</td>
													<%}%>
													</c:if>
<!-- 													<%if (sessionManager.havePermission(session,"plan/dailyplan/delete.do")) {%> -->
<!-- 													<td> -->
<!-- 														<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true"  -->
<!-- 															onclick="deletesFun();">删除</a> -->
<!-- 													</td> -->
<!-- 													<%}%> -->
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<form id="searchForm">
											<input id="pid" name="pid" type="hidden" value="${dailyplansummary.id }" />
												<table>
													<tr>													
														<td>日计划单号</td>
														<td><input name="search_name" id="search_name" class="easyui-textbox" data-options="prompt:'请输入关键字'" style="width: 150px;" value=""/></td>
														<td >状态</td>
														<td colspan='4'><select id="search_status" name="search_status" class="easyui-combobox" style="width:152px;">
																<option value='all' select>全部</option>
																<option value='unaudit'>未审核</option>
																<option value='finished'>已完成</option>
																<option value='audited'>已审核</option>
															</select>
														</td>	
<!-- 													</tr> -->
<!-- 													<tr> -->
<!-- 														<td>计划下达日期</td> -->
														<td><input id="sdt" name="sdt" class="Wdate" type="hidden"
															value="${wsdt}"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'edt\')}'})"
															readonly></td>
<!-- 														<td>到</td> -->
														<td><input id="edt" name="edt" class="Wdate" type="hidden"
															value="${wedt}"
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'sdt\')}'})"
															readonly></td>
														<td><a href="javascript:void(0);" class="easyui-linkbutton"
															data-options="iconCls:'ext-icon-search',plain:true"
															onclick="$('#pid').val('${dailyplansummary.id }');grid.datagrid('load',ext.serializeObject($('#searchForm')));">搜索</a>
															<a href="javascript:void(0);" class="easyui-linkbutton"
															data-options="iconCls:'icon-reload',plain:true"
															onclick="reset();">重置</a>
<!-- 															<a href="javascript:void(0);" class="easyui-linkbutton" -->
<!-- 															data-options="iconCls:'ext-icon-search',plain:true" -->
<!-- 															onclick="$('#searchForm').form('clear');ddvpid='';grid.datagrid('load',{});">全部计划</a> -->
														</td>								
													</tr>
												</table>
											</form>
										</td>
									</tr>
								</table>
							</div>
			<div id="cc" class="easyui-layout" data-options="fit:true,border:false">  
				 <div data-options="fit:true,border:false">
					<table id="grid" data-options="border:false"></table>
				</div>  
			</div> 		
		</div>		
		<div title="已作废计划单" data-options="iconCls:''" style="padding:0px;">
		<div id="toolbarinvalid" style="display: none;">
								<table>									
									<tr>
										<td>
											<form id="searchFormiv">
												<table>
													<tr>													
														<td>日计划单号</td>
														<td><input name="search_nameiv" id="search_nameiv" class="easyui-textbox" data-options="prompt:'请输入关键字'" style="width: 150px;" value=""/></td>
							
														<td><a href="javascript:void(0);" class="easyui-linkbutton"
															data-options="iconCls:'ext-icon-search',plain:true"
															onclick="invalidategrid.datagrid('load',ext.serializeObject($('#searchFormiv')));">搜索</a>
															<a href="javascript:void(0);" class="easyui-linkbutton"
															data-options="iconCls:'icon-reload',plain:true"
															onclick="$('#searchFormiv').form('clear');invalidategrid.datagrid('load',{sdt:'${wsdt}',edt:'${wedt}'});">重置</a>

														</td>								
													</tr>
												</table>
											</form>
										</td>
									</tr>
								</table>
							</div>
			<div class="easyui-panel" title="作废的日计划单" data-options="fit:true,border:false">
				<table id="invalidategrid" data-options="fit:true,border:false"></table>
			</div>
		</div>
		<div title="全部计划" data-options="iconCls:''" style="padding:0px;">
			<div class="easyui-panel" title="全部日计划" data-options="fit:true,border:false">				
				<div  class="easyui-layout" data-options="fit:true,border:false">
			    		  <div id="toolbarAll" style="display: none;">
								<table>
									<tr>
										<td>
											<table>
												<tr>
													<%if (sessionManager.havePermission(session,"plan/dailyplan/add.do")) {%>
													<td>
														<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
															onclick="addFun();">添加</a>
													</td>
													<%}%>
													<%if (sessionManager.havePermission(session,"plan/dailyplan/delete.do")) {%>
													<td>
														<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
															onclick="deletesFun();">删除</a>
													</td>
													<%}%>
													<!-- <td><div class="datagrid-btn-separator"></div></td>
													<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_add',plain:true" onclick="">导入</a></td>
													<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="">导出</a></td> -->
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<form id="searchFormAll">
												<table>
													<tr>
													
														<td>生产订单号</td>
														<td><input name="search_nameAll" id="search_nameAll" class="easyui-textbox" data-options="prompt:'请输入关键字'" style="width: 150px;" value=""/></td>
														<td >计划状态</td>
														<td colspan='4'><select id="search_statusAll" name="search_statusAll" class="easyui-combobox" style="width:152px;">
																<option value='all' select>全部</option>
																<option value='edit'>待提交</option>
																<option value='submit'>待审核</option>
																<option value='audited'>待下发</option>
																<option value='inprogress'>已下发</option>
																<option value='completed'>已完成</option>
																<option value='rearrenged'>已重排</option>
																<option value='invalid'>已作废</option>										
															</select>
														</td>
<!-- 													</tr> -->
<!-- 													<tr> -->
														<td>计划下达日期</td>
														<td><input id="sdtAll" name="sdtAll" class="Wdate"
															value=""
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'edtAll\')}'})"
															readonly></td>
														<td>到</td>
														<td><input id="edtAll" name="edtAll" class="Wdate"
															value=""
															onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'sdtAll\')}'})"
															readonly></td>
						<!-- 								<td><a href="#" class="easyui-linkbutton"    -->
						<!--         				onclick="showYesterday();">昨日</a>&nbsp;<a href="#" class="easyui-linkbutton"    -->
						<!--         				onclick="showToday();">今日</a>&nbsp;<a href="#" class="easyui-linkbutton"    -->
						<!--         				onclick="showThisWeek();">本周</a></td> -->
						<!--         						<td><div class="datagrid-btn-separator"></div></td> -->
														<td><a href="javascript:void(0);" class="easyui-linkbutton"
															data-options="iconCls:'ext-icon-search',plain:true"
															onclick="gridAll.datagrid('load',ext.serializeObject($('#searchFormAll')));">搜索</a>
															<a href="javascript:void(0);" class="easyui-linkbutton"
															data-options="iconCls:'icon-reload',plain:true"
															onclick="$('#searchFormAll').form('clear');gridAll.datagrid('load',{});">重置</a>															
														</td>								
													</tr>
												</table>
											</form>
										</td>
									</tr>
								</table>
							</div>
							<div data-options="region:'center',fit:true,border:false">
								<table id="gridAll" data-options="fit:true,border:false"></table>
							</div> 
			    	</div>
			</div>
		</div>		
	</div>
</body>
</html>
 
