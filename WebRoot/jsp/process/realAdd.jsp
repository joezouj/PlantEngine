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
			$.post(ext.contextPath + "/process/real/save.do", $(".form").serialize(), function(data) {
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
	
	function selectMaterial(){
		var dialog = parent.ext.modalDialog({
				title : '选择产品',
				width : 800,
				height : 400,
				closeOnEscape:true,
				url : ext.contextPath + '/material/materialinfo/selectMaterial.do?typename='+encodeURI(encodeURI("成品")),
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectOK();
						if(res!=null){
							dialog.dialog('destroy');
							$("#productid").val((res.materialid== 'undefined')?'':res.materialid);
							$("#productcode").textbox('setValue',(res.materialcode == 'undefined')?'':res.materialcode);
							$("#productname").text((res.materialname == 'undefined')?'':res.materialname);
							$("#productspec").text((res.spec == 'undefined')?'':res.spec);
							setName();
						}
					}
				}
				]
			});
	}
	
	function setName(){
		$("#name").textbox("setValue",$("#productname").text()+" "+$("#processid").combobox("getText"));
	}
	
	$(function(){
		$("#productcode").textbox("textbox").bind("click",function(){
			selectMaterial();
		});
		
		$("#processid").combobox({
			url:ext.contextPath + '/snaker/process/getListForSelect.do',
		    valueField:'id',
		    textField:'displayName',
		    onSelect:function(record){
		    	setName();
		    }
		});
		
		/* $("#lineid").combobox({
			url : ext.contextPath + '/work/line/getSelectList.do?random=' + Math.random(),
			valueField : 'id',
			textField : 'name',
			method:'get',
			width:250,
			panelHeight:'auto',
			editable:false
		}); */
	});
</script>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th>产品编码</th>
					<td>
						<input id="productid" name="productid" type="hidden" />
						<input id="productcode" name="productcode" class="easyui-textbox" data-options="required:true,validType:'isBlank',editable:false" value="" />
					</td>
				</tr>
				<tr>
					<th>产品名称</th>
					<td>
						<label id="productname"></label>
					</td>
				</tr>
				<tr>
					<th>产品规格</th>
					<td>
						<label id="productspec"></label>
					</td>
				</tr>
				<tr>
					<th>工艺路线</th>
					<td>
						<input id="processid" name="processid" class="easyui-combobox" data-options="required:true,validType:'isBlank',editable:false" value="" />
					</td>
				</tr>
				<tr>
					<th>产品工艺名称</th>
					<td>
						<input id="name" name="name" class="easyui-textbox" data-options="required:true,validType:'isBlank',width:400" value="" />
					</td>
				</tr>
				<!-- <tr>
					<th>所属产线</th>
					<td><input id="lineid" name="lineid" class="easyui-combotree"
						data-options="required:true,validType:'isBlank'" value="" />
					</td>
				</tr> -->
				<tr>
					<th>描述</th>
					<td>
						<input name="description" class="easyui-textbox" style="height:100px;width:300px"
						value=""data-options="multiline:true" validtype="length[0,250]" invalidMessage="有效长度0-250" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
