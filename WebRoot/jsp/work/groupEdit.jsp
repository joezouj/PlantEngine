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
			$.post(ext.contextPath + "/work/group/update.do", $(".form").serialize(), function(data) {
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
	
	$(function() {
		$('#deptid').combotree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			onBeforeSelect:function(node){
				if(node.id=="-1"){
                    $("#deptid").tree("unselect");
                }
            },
			onLoadSuccess:function(){
				$('#deptid').combotree('setValue','${group.deptid}');
			}
		});
		
		var leadernames="",leaderids="",membernames="",memberids="";
		<c:forEach var="obj" items="${group.groupmembers}">
			if("${obj.usertype}"=="leader"){
				leadernames+="${obj.username}"+",";
				leaderids+="${obj.userid}"+",";
			}
			if("${obj.usertype}"=="member"){
				membernames+="${obj.username}"+",";
				memberids+="${obj.userid}"+",";
			}
		</c:forEach>
		leadernames=leadernames.substring(0,leadernames.length-1);
		membernames=membernames.substring(0,membernames.length-1);
		$('#leadername').textbox({
			required:true,
			editable:false,
			value:leadernames
		});
		$('#leadername').textbox('textbox').bind("click",function(){
			selectUsers("leadername","leaderid","${param.iframeId}");
		});
		$('#leaderid').val(leaderids);
		
		$('#membername').textbox({
			required:true,
			editable:false,
			value:membernames
		});
		$('#membername').textbox('textbox').bind("click",function(){
			selectUsers("membername","memberid","${param.iframeId}");
		});
		$('#memberid').val(memberids);
	});

</script>
</head>
<body>
		<form method="post" class="form">
		<input name="id" type="hidden" value="${group.id}"/>
			<table class="table">
				<tr>
					<th>班组名称</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${group.name}" />
					</td>
				</tr>
				<tr>
					<th>所属车间</th>
					<td><input id="deptid" name="deptid" class="easyui-combobox"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr>
				<tr>
					<th>组长</th>
					<td>
						<input id="leadername" name="leadername" class="easyui-textbox"/>
						<input id="leaderid" name="leaderid" type="hidden"/>
					</td>
				</tr>
				<tr>
					<th>组员</th>
					<td>
						<textarea id="membername" name="membername" class="easyui-textbox" data-options="multiline:true" 
							style="height:100px;width:100%"></textarea>
						<input id="memberid" name="memberid" type="hidden"/>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>
						<textarea name="remark" class="easyui-textbox" data-options="multiline:true" style="height:100px;width:100%" 
						 validtype="length[0,250]" invalidMessage="有效长度0-250">${group.remark}</textarea>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
