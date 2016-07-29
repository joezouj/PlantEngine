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
	var addFun = function() {
		var dialog = parent.ext.modalDialog({
			title : '添加任务',
			url : ext.contextPath + '/plan/dailyplan/showlist_task.do',
			buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.dosave();
					if(res!=null){
						dialog.dialog('destroy');
						var str="";
						$.each(res.result,function(index,item){
							str+=item.productionorderno+",";
						});
						$.post(ext.contextPath + '/work/workorder/addbyplan.do', {ids:str.replace(/,$/g,"")} , function(data) {
							if(data>0){
								parent.$.messager.alert('提示','添加成功','info',function(){
									grid.datagrid('reload');
									grid.datagrid('clearChecked');
								});
							}else{
								parent.$.messager.alert('提示','添加失败','info');
							}
						});
					}
				}
			}]
		});
	};
	
	var editFun = function(id) {
		var dialog = parent.ext.modalDialog({
			title : '编辑任务',
			url : ext.contextPath + '/work/workorder/edit.do?id=' + id,
			buttons : [ {
				text : '保存',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog, grid);
				}
			} ]
		});
	};
	
	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/work/workorder/delete.do', {id : id}, function(data) {
					if(data==1){
						parent.$.messager.alert('提示','删除成功','info',function(){
							grid.datagrid('reload');
						});
					}else{
						parent.$.messager.alert('提示','删除失败','info');
					}
				});
			}
		});
	};
	
	var discardFun = function(id) {
		top.$.messager.confirm('提示', '您确定要作废此订单？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/work/workorder/discard.do', {id:id} , function(data) {
					if(data>0){
						parent.$.messager.alert('提示','作废成功','info',function(){
							grid.datagrid('reload');
							grid.datagrid('clearChecked');
						});
					}else{
						parent.$.messager.alert('提示','作废失败','info');
					}
				});
			}
		});
	}
	
	var deletesFun = function() {
		var checkedItems = $('#grid').datagrid('getChecked');
		var datas="";
		var eddts="";
		$.each(checkedItems, function(index, item){
			datas+=item.productionorderno+",";
			eddts+=item.eddt+",";
		}); 
		if(datas==""){
			top.$.messager.alert('提示', '请先选择要删除的记录','info');
		}else{
			parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/work/workorder/deletes.do', {ids:datas,eddts:eddts} , function(data) {
						if(data>0){
							parent.$.messager.alert('提示','删除成功','info',function(){
								grid.datagrid('reload');
								grid.datagrid('clearChecked');
							});
						}else{
							parent.$.messager.alert('提示','删除失败','info');
						}
					});
				}
			});
		}
	};
	
	var switchline = function(id){
		var dialog = parent.ext.modalDialog({
				title : '切换产线',
				url : ext.contextPath + '/work/workorder/showswitchlinelist.do?id='+id,
				width:600,
				height:400,
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						dialog.find('iframe').get(0).contentWindow.selectOK(dialog,grid);
					}
				}]
			});
	};
	
	var workstationsnaker = function(id){
		var dialog = parent.ext.modalDialog({
				title : '任务调度',
				url : ext.contextPath + '/work/worktaskworkstation/snaker.do?id='+id,
				width:1200,
				height:600,
				onClose:function(){
					var res=dialog.find('iframe').get(0).contentWindow.returnVal;
					if(res=="refresh"){
						$('#grid').datagrid("reload");
					}
				}
			});
	};
	
	var workstationsnakerview = function(id){
		var dialog = parent.ext.modalDialog({
				title : '任务调度',
				url : ext.contextPath + '/work/worktaskworkstation/snakerview.do?id='+id,
				width:1200,
				height:600,
				onClose:function(){
					var res=dialog.find('iframe').get(0).contentWindow.returnVal;
					if(res=="refresh"){
						$('#grid').datagrid("reload");
					}
				}
			});
	};
	
	var startProcess = function(id) {
		$.post(ext.contextPath + '/work/workorder/startorder.do?id=' + id,function(data){
			if(data=='1'){
				top.$.messager.alert('提示', '启动成功','info',function(){
					grid.datagrid('reload');
					grid.datagrid('clearChecked');
				});
			}else{
				top.$.messager.alert('提示', '启动失败','info');
			}
		});
	};

	$(function() {
		grid = $('#grid').datagrid({
			view: detailview,
            detailFormatter:function(index,row){
                return '<div style="padding:2px"><table class="ddv"></table></div>';
            },
			title : '',
			url : ext.contextPath + '/work/workorder/getlist.do',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect: true,
			ctrlSelect:true,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'productionorderno',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'},
				{width : '160', title : '生产订单号', field : 'productionorderno', sortable : true, halign:'center'}, 
				{width : '200', title : '产品编码', field : 'productno', sortable : true, halign:'center'}, 
				{width : '400', title : '产品名称', field : 'productname', sortable : true, halign:'center'},
				{width : '180', title : '交货日期', field : 'eddt', sortable : true, halign:'center', align:'center', formatter : function(value, row){
					if(value.length<20){
						return value;
					}else{
						return value.substring(0,19);
					}					
					}},
				{width : '120',title : '生产数量', field : '_productnum', halign:'center', align:'center'} 
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
				
				//展开所有的行
				for(var i=0;i<data.rows.length;i++){
					$('#grid').datagrid('expandRow',i);
				}
			},
			
            onExpandRow: function(index,row){
                var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
                ddv.datagrid({
                    url:ext.contextPath + '/work/workorder/getlist_detail.do?productionorderno='+row.productionorderno+'&eddt='+row.eddt,
                    fitColumns:false,
                    singleSelect:true,
                    rownumbers:true,
                    loadMsg:'',
                    height:'auto',
                    columns:[[
                        {width : '240', field:'productuid',title:'产品序列号',halign:'center'},
                        {width : '80', title : '订单状态', field : 'status', sortable : true, halign:'center',formatter : function(value, row){
	        						return value=='0'?"已作废":value=='1'?"进行中":value=='2'?"已完成":"未开始";
	        				}
	                    },
		                {width : '120', title : '当前产线', field : 'linename', sortable : true, halign:'center'},
                        {width : '140', title : '当前工序', field : 'pstatus', sortable : true, halign:'center',formatter : function(value, row){
	        					if(value==''){
	        						return "无";
	        					}else{
	        						return value;
	        					}
	        				}
                        },
                        {width : '220', title : '当前工位', field : 'activeWorkstations', halign:'center', formatter : function(value, row, index) {
	                        	if(value==''){
	        						return "无";
	        					}else{
	        						var res="";		
									for(var i=0;i<value.length;i++){
										res+="["+value[i].serial+"] "+value[i].name+", ";
									}
									return res.replace(/, $/g,"");
	        					}	
							}
						},
						{width : '120', title : '当前操作工', field : 'username', halign:'center',formatter : function(value, row){
	        					if(value==''){
	        						return "无";
	        					}else{
	        						return value.replace(/, $/g,"");
	        					}
	        				}
						},
                        {width : '130', title : '操作', field : 'action', align:'center', formatter : function(value, row) {
							var str = '';
								<%if (sessionManager.havePermission(session,"work/workorder/edit.do")) {%>
								if(row.status==''){
									//str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/>';
								}
								<%}%>
								<%if (sessionManager.havePermission(session,"work/workorder/edit.do")) {%>
								if(row.status!='0'){
								str += '<img class="iconImg ext-icon-bin" title="作废" onclick="discardFun(\''+row.id+'\');"/>';
								}
								<%}%>
								<%if (sessionManager.havePermission(session,"work/workorder/delete.do")) {%>
								str += '<img class="iconImg ext-icon-table_delete" title="删除" onclick="deleteFun(\''+row.id+'\');"/>';
								<%}%>
								<%if (sessionManager.havePermission(session,"work/worktaskworkstation/districtworkstation.do")) {%>
								str += '<img class="iconImg ext-icon-arrow_switch" title="切换产线" onclick="switchline(\''+row.id+'\');"/>';
								
								if(row.status==''){
									str += '<img class="iconImg ext-icon-chart_organisation" title="任务调度" onclick="workstationsnaker(\''+row.id+'\');"/>';
								}else{
									str += '<img class="iconImg ext-icon-chart_organisation" title="任务调度" onclick="workstationsnakerview(\''+row.id+'\');"/>';
								}
								<%}%>
								if(row.status == '' ){
									str += '<img class="iconImg ext-icon-control_play" title="任务开始" onclick="startProcess(\''+row.id+'\');"/>';
								}
							return str;
						}
					} 
                    ]],
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
                $('#grid').datagrid('fixDetailRowHeight',index);
            }
		});
		
	});
	
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<%if (sessionManager.havePermission(session,"work/workorder/add.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"work/workorder/delete.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deletesFun();">删除</a>
							</td>
							<%}%>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<form id="searchForm">
						<table class="tooltable">
							<tr>
								<td>生产订单号</td>
								<td><input name="search_taskno" class="easyui-textbox" /></td>
								<td>&nbsp;</td>
								<td>产品编码</td>
								<td><input name="search_productno" class="easyui-textbox" /></td>
								<td>&nbsp;</td>
								<td>到期时间</td>
								<td><input id="sdt" name="sdt" class="Wdate" value="${param.sdt }"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'sdt\')}'})"
									readonly></td>
								<td>到</td>
								<td><input id="edt" name="edt" class="Wdate" value="${param.edt }" 
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'edt\')}'})"
									readonly></td>
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