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
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/process/realDetailsBook/save.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
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
	
	function selectBook(){
		var dialog = parent.ext.modalDialog({
				title : '选择作业指导书',
				width : 800,
				height : 480,
				closeOnEscape:true,
				url : ext.contextPath + '/document/selectBook.do?productcode=${productcode}',
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectOK();
						if(res!=null){
							dialog.dialog('destroy');
							$("#bookid").val((res.id== 'undefined')?'':res.id);
							$("#bookname").textbox('setValue',(res.docname == 'undefined')?'':res.docname);
							$("#booknum").text((res.number == 'undefined')?'':res.number);
							$("#bookpath").text((res.path == 'undefined')?'':res.path);
						}
					}
				}
				]
			});
	}
	
	$(function(){
		$("#bookname").textbox("textbox").bind("click",function(){
			selectBook();
		});
	});
</script>
</head>
<body>
		<form method="post" class="form">
			<input type="hidden" name="pid" value="${param.pid}"/>
			<input type="hidden" name="productcode" value="${productcode}"/>
			<table class="table">
				<tr>
					<th>作业指导书</th>
					<td>
						<input id="bookid" name="bookid" type="hidden" />
						<input id="bookname" class="easyui-textbox" data-options="required:true,validType:'isBlank',editable:false" value="" />
					</td>
				</tr>
				<tr>
					<th>编号</th>
					<td>
						<label id="booknum"></label>
					</td>
				</tr>
				<tr>
					<th>地址</th>
					<td>
						<label id="bookpath"></label>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
