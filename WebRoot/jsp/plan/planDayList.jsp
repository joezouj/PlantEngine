<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
	var ddv;
	var addFun = function() {
		var dialog = parent.ext.modalDialog({
			title : '新增日计划',		
			url : ext.contextPath + '/plan/dailyplan/add.do',
			buttons : [
			{
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			}, 
			{
				text : '下发',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dodistribute(dialog, grid);
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
		ddv.datagrid('reload');

	};
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			width: 800,
			height:620,
			title : '编辑日计划',
			url : ext.contextPath + '/plan/dailyplan/edit.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			},
			{
				text : '下发',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dodistribute(dialog, grid);
				}
			},
			{
				text : '作废',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.invalidate(dialog, grid);
				}
			} ]
		});
	};
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？计划删除后将自动作废。', function(r) {
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
			top.$.messager.confirm('提示', '您确定要删除多条记录？即将删除所选单号下所有计划，计划删除后将自动作废。', function(r) {
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
			view: detailview,
            detailFormatter:function(index,row){
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
			title : '',
			url : ext.contextPath + '/plan/dailyplan/getlist.do?flag=listpage',
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
				{width : '290', title : '产品名称', field : 'product', sortable : true, halign:'center',formatter : function(value,row){
					if(value!=null){
                       		return value.materialname;
                       }else{
                       		return value;
                       }
				}},
				{width : '250', title : '订单产品数量', field : 'productnum', sortable : true, halign:'center'},
				{width : '250', title : '未排计划产品数', field : 'orderproductdetail', sortable : true, halign:'center',formatter : function(value,row){
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
				}}				 
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
				if(flag==0){
					flag=1;	
					showToday();					
				}	
				
				//展开所有的行
				for(var i=0;i<data.rows.length;i++){
					$('#grid').datagrid('expandRow',i);
				}
			},		
            onExpandRow: function(index,row){
                ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
                ddv.datagrid({                	
                    url:ext.contextPath + '/plan/dailyplan/getlist_detail.do?salesOrderID='+row.id+'&search_name='+$('#search_name').textbox('getValue')+'&sdt='+$("#sdt").val()+'&edt='+$("#edt").val()+'&search_status='+$('#search_status').combobox('getValue'),
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
		        		if(row.status=='3'){
		        			rowstyler+= ' background-color:#C0C0C0;color:#fff;';
		        		}else if(row.status=='6'){
		        			rowstyler+= ' font-style:ltalic;';
		        		}
		        		return rowstyler;
		    		},
                    columns : [ [ 
						
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
						{width : '80', title : '计划生产数', field : 'productquantity', sortable : true, halign:'center' }, 
						{width : '60', title : '完成数', field : 'finishedquantity', sortable : true, halign:'center' },  
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
							} },			    		    
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
									str += '<img class="iconImg ext-icon-bin" title="计划作废" onclick="invalidateFun(\''+row.id+'\');"/> ';
									<%}%>						
								return str;
							}
						},
						{width : '80', title : '提示', field : 'taskchangedstatus', sortable : true, align:'center', formatter : function(value, row){
								if(value=="true" ||value=="TRUE"){
									return '新进度<img class="iconImg ext-icon-new" title="任务新变动" onclick="viewFun(\''+row.id+'\');"/>';
								}else{
									return "";
								}											
							} } 
					] ],
                    onResize:function(){
                        $('#grid').datagrid('fixDetailRowHeight',index);
                    },
                    onLoadSuccess:function(){
                   		 $('.iconImg').attr('src', ext.pixel_0);
                        setTimeout(function(){
                            $('#grid').datagrid('fixDetailRowHeight',index);
                        },0);
                        
                     }
                });
            }
		});		
	});
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
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
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
					<form id="searchForm">
						<table>
							<tr>
							
								<td>生产订单号</td>
								<td><input name="search_name" id="search_name" class="easyui-textbox" data-options="prompt:'请输入关键字'" style="width: 150px;" value=""/></td>
								<td >计划状态</td>
								<td colspan='4'><select id="search_status" name="search_status" class="easyui-combobox" style="width:152px;">
										<option value='all' select>全部</option>
										<option value='edit'>编制中</option>
										<option value='inprogress'>进行中</option>
										<option value='completed'>已完成</option>										
									</select>
								</td>
							</tr>
							<tr>
								<td>计划下达日期</td>
								<td><input id="sdt" name="sdt" class="Wdate"
									value="${wsdt}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'edt\')}'})"
									readonly></td>
								<td>到</td>
								<td><input id="edt" name="edt" class="Wdate"
									value="${wedt}"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'sdt\')}'})"
									readonly></td>
								<td><a href="#" class="easyui-linkbutton"   
        				onclick="showYesterday();">昨日</a>&nbsp;<a href="#" class="easyui-linkbutton"   
        				onclick="showToday();">今日</a>&nbsp;<a href="#" class="easyui-linkbutton"   
        				onclick="showThisWeek();">本周</a></td>
        						<td><div class="datagrid-btn-separator"></div></td>
								<td><a href="javascript:void(0);" class="easyui-linkbutton"
									data-options="iconCls:'ext-icon-search',plain:true"
									onclick="grid.datagrid('load',ext.serializeObject($('#searchForm')));">搜索</a>
									<a href="javascript:void(0);" class="easyui-linkbutton"
									data-options="iconCls:'icon-reload',plain:true"
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
 
