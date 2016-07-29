<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="com.sipai.tools.SessionManager"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
	var addFun = function() {
		var dialog = parent.ext.modalDialog({
			iframeId:'send',
			title : '新建消息',
			url : ext.contextPath + '/msg/addMsg.do?iframeId=send',
			buttons : [ {
				text : '发送',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.dosave(dialog,grid);
				}
			} ]
		});
	};
	var viewFun = function(id,issms) {
		if(issms=="sms"){
			//查看短信			
			grid.datagrid('clearChecked');
			var dialog = parent.ext.modalDialog({
				title : '查看短信',
				url : ext.contextPath + '/msg/viewSms.do?id=' + id	,
				onClose:function(){
     				grid.datagrid('reload');
    		    }		
			});

		}else{
			grid.datagrid('clearChecked');
			var dialog = parent.ext.modalDialog({
				title : '查看信息',
				url : ext.contextPath + '/msg/viewMsgSend.do?id=' + id+'&send=send',
				buttons : [ {
					text : '上一条',
					handler : function() {
						var morder="pre";
						var send="true";//判断是否为发送，还是接收，接收要更改未读状态
						dialog.find('iframe').get(0).contentWindow.doview(dialog, grid,morder,send);
						grid.datagrid('reload');
					}
				},
				{
					text : '下一条',
					handler : function() {
						var morder="next";
						var send="true";//判断是否为发送，还是接收，接收要更改未读状态
						dialog.find('iframe').get(0).contentWindow.doview(dialog, grid,morder,send);
						grid.datagrid('reload');
					}
				}]		
			});
		}
	};

	var deleteFun = function(id) {
		parent.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(ext.contextPath + '/msg/deleteMsg.do', {id : id}, function(data) {
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
			top.$.messager.confirm('提示', '您确定要删除此记录？', function(r) {
				if (r) {
					$.post(ext.contextPath + '/msg/deleteMsgs.do', {ids:datas} , function(data) {
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
	
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/msg/getMsgsend.do',
			striped : true,
			rownumbers : true,
			pagination : true,
			ctrlSelect:true,
			singleSelect: false,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			pageSize : 50,
			pageList : [ 20, 50, 100],
			columns : [ [ 
				{checkbox:true , field : 'ck'}, 
				{width : '80', title : '类型', field : 'typename', sortable : true, halign:'center',formatter:function(value){  
                         if(value!=null){
                       		return value.name;
                       }else{
                       		return value;
                       }
                     } 
                 },
                 {width : '80', title : '方式', field : 'issms', sortable : true, halign:'center',formatter:function(value){
                 	if(value=="sms"){
                 		return "短信";
                 	}else{
                 		return "消息";
                 	}    
                   } 
                 }, 
				{width : '280', title : '内容', field : 'content', sortable : true, halign:'center'},
			    {width : '80', title : '发送人',  field: 'susername', sortable : true, halign:'center',formatter:function(value){  
                         if(value!=null){
                       		return value.caption;
                       }else{
                       		return value;
                       }
                         } 
                  },
			    {width : '160', title : '发送时间', field : 'sdt', sortable : true, halign:'center',align:'center',formatter:function(value){  
                        return value.substring(0,19);
                         }  
                 }, 
				{title : '操作', field : 'action', width : '160', halign:'center', align:'center', formatter : function(value, row) {
						var str = '';
							str += '<img class="iconImg ext-icon-table" title="查看" onclick="viewFun(\''+row.id+'\',\''+row.issms+'\');"/> ';
							//str += '<img class="iconImg ext-icon-table_edit" title="编辑" onclick="editFun(\''+row.id+'\');"/> ';
							<%if (sessionManager.havePermission(session,"msg/deleteMsg.do")) {%>
							str += '<img class="iconImg ext-icon-table_delete" title="删除" onclick="deleteFun(\''+row.id+'\');"/>';
							<%}%>
							if(row.issms!='sms'){
								//str+=' <a href="#" onclick="viewstatus(\''+row.id+'\');">浏览情况</a>';
								str+=' <img class="iconImg ext-icon-eye" title="浏览情况" onclick="viewstatus(\''+row.id+'\');"/>';
							}else{
								str+='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
								
							}														
						return str;
					}
				} 
			] ],
			toolbar : '#toolbar',
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
			}
		});
	});
	
	function formattime(val) {
		var year = parseInt(val.year) + 1900;
		var month = (parseInt(val.month) + 1);
		month = month > 9 ? month : ('0' + month);
		var date = parseInt(val.date);
		date = date > 9 ? date : ('0' + date);
		var hours = parseInt(val.hours);
		hours = hours > 9 ? hours : ('0' + hours);
		var minutes = parseInt(val.minutes);
		minutes = minutes > 9 ? minutes : ('0' + minutes);
		var seconds = parseInt(val.seconds);
		seconds = seconds > 9 ? seconds : ('0' + seconds);
		var time = year + '-' + month + '-' + date + ' ' + hours + ':'
				+ minutes + ':' + seconds;
		return time;
	}
	function viewstatus(id){
		grid.datagrid('clearChecked');
		var dialog = parent.ext.modalDialog({
			title : '浏览情况',
			url : ext.contextPath + '/msg/viewMsgStatus.do?id=' + id
		});
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
							<%if (sessionManager.havePermission(session,"msg/addMsg.do")) {%>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="addFun();">添加</a>
							</td>
							<%}%>
							<%if (sessionManager.havePermission(session,"msg/deleteMsg.do")) {%>
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
							
								<td>发送人</td>
								<td><input name="search_susername" class="easyui-textbox" data-options="prompt:'请输入关键字'" style="width: 150px;" value=""/></td>
								<td>内容</td>
								<td><input name="search_content" class="easyui-textbox" data-options="prompt:'请输入关键字'" style="width: 150px;" value=""/></td>	
								<td></td>
							</tr>
							<tr>															
								<td>发送时间</td>
								<td><input id="sdt" name="sdt" class="Wdate" value="${param.sdt }"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'edt\')}'})"
							readonly></td>
							<td>到</td>
							<td><input id="edt" name="edt" class="Wdate" value="${param.edt }" 
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'sdt\')}'})"
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
 
