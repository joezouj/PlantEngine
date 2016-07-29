<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function doview(dialog, grid,morder,send){
		//grid.datagrid('reload');		
		var id=$('#id').val();
		var sdt=$('#sdtt').val();				
		$.post(ext.contextPath + '/msg/viewMsgFast.do',{id:id,morder:morder,sdt:sdt,send:send},function(data){
		if(data.rows==""){
			top.$.messager.alert('提示',data.res,'info');
		}				
		$('#suser').html(data.suser);
		$('#recv').html(data.recv);
		$('#sdt').html(data.rows[0].sdt.substring(0,19));		
		$('#content').html(data.rows[0].content);
		$('#id').val(data.rows[0].id);	
		$('#sdtt').val(data.rows[0].sdt.substring(0,19));		
		},'json');		
	}
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
</script>
</head>
<body>
		<form method="post" class="form">
		<input type="hidden" id="id" name="id" value="${msg.id }"/>	
		<input type="hidden" id="sdtt" name="sdtt" value="${msg.sdt}"/>	
			<table class="table" id="table1">
				<tr>
					<th>发送人</th>
					<td id="suser">${suser}</td>
				</tr>
				<tr>
					<th>发送时间</th>
					<td id="sdt">${fn:substring(msg.sdt,0,19)}</td>
				</tr>
				<tr>
					<th>接收人</th>
					<td id="recv">${recv}</td>
				</tr>
				
				<tr>
					<th>内容</th>
					<td id="content">${msg.content}</td>
				</tr>
			</table>
		</form>
</body>
</html>
