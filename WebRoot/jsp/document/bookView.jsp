<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		var ue = UE.getEditor('details');
		$('#pname').combotree({
			url : ext.contextPath + '/document/getDataJson.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			required:true,
			onClick : function(node) {
				$("#pid").val(node.id);
			}
		});
		
		//附件加载
		new commfile('document.DocFileMapper','${data.id}').loadfile();
	});
</script>
</head>
<body>
	<div >
		<form method="post" class="form">
			<input type="hidden" name="id" value="${data.id}"/>
			<table class="table">
				<tr>
					<th>名称</th>
					<td>${data.docname}</td>
					<th>上级</th>
					<td>${pname}</td>
				</tr>
				<tr>
					<th>编号</th>
					<td>${data.number}</td>
					<th>类型</th>
					<td>作业指导书</td>
				</tr>
				<tr>
					<th>地址</th>
					<td colspan="3">${data.path}</td>
				</tr>
				<tr>
					<th>
						附件列表
					</th>
					<td colspan="3">
						<div id="fileList"></div>
					</td>				
				</tr>
				<tr>
					<th>状态</th>
					<td colspan="3">
					${data.st=="1"?"启用":"禁用"}
					</td>
				</tr>
				<tr>
					<th>内容</th>
					<td colspan="3"><script id="details"  name="details"  style="width:100%;height:260px;">${data.details}</script></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
