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
			$.post(ext.contextPath + "/material/materialinfo/update.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						//刷新列表
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
	
	function selectDrawing(){
		var dialog = parent.ext.modalDialog({
				title : '选择图纸',
				width : 800,
				height : 480,
				closeOnEscape:true,
				url : ext.contextPath + '/document/selectDrawing.do?productcode=${productcode}',
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectOK();
						if(res!=null){
							dialog.dialog('destroy');
							$("#figurenumberid").val((res.id== 'undefined')?'':res.id);
							$("#figurenumber").textbox('setValue',(res.number == 'undefined')?'':res.number);
							$("#figurename").text((res.docname == 'undefined')?'':res.docname);
						}
					}
				}
				]
			});
	}
	
	$(function() {		
		$('#unit').combobox({ 
			url:ext.contextPath + '/material/materialunit/getJsonUnit.do', 
			valueField:'unit', 
			textField:'unit',
			editable:false,
			panelHeight:'auto'
		});
// 		$('#typeid').combobox({ 
// 			url:ext.contextPath + '/material/materialtype/getJsonType.do', 
// 			valueField:'id', 
// 			textField:'typename',
// 			editable:false,
// 			panelHeight:'auto',
// 			onSelect: function(type){			
//  				var materialcode = $('#materialcode').textbox('getValue');
// 				var oldmaterialcode=$("#mtypetemp").val();//删除旧的物料编码前缀
// 				if(oldmaterialcode!=null && oldmaterialcode!=""){
// 					materialcode=materialcode.substring(oldmaterialcode.length,materialcode.length);					
// 				}				
// 				materialcode=type.typecode+materialcode;
// 				$("#mtypetemp").val(type.typecode);
// 				$("#materialcode").textbox('setValue',materialcode);
// 			}			
// 		});	
		$('#tname').combotree({
			url : ext.contextPath + '/material/materialtype/getMenusJsonActive.do?random=' + Math.random(),
			parentField : 'pid',
			method:'get',
			required:true,
			onBeforeSelect:function(node){
				if(node.id=="-1"){
                    $("#tname").tree("unselect");
                }
            },
			onClick : function(node) {
				$("#typeid").val(node.id);	
// 				var materialcode=$("#materialcode").textbox('getValue');
// 				var oldmaterialcode=$("#mtypetemp").val();//删除旧的物料编码前缀
// 				if(oldmaterialcode!=null && oldmaterialcode!=""){
// 					materialcode=materialcode.substring(oldmaterialcode.length,materialcode.length);					
// 				}				
// 				materialcode=node.attributes.typecode+materialcode;
// 				$("#mtypetemp").val(node.attributes.typecode);
// 				$("#materialcode").textbox('setValue',materialcode);			
			},
			onLoadSuccess: function(node,data){//编辑界面初始化填充，解析树节点				
				var tid='${materialInfo.typeid }';
				var t = $('#tname').combotree('tree');
				var node = t.tree('find', tid);				
				$('#tname').combotree('setValue',node.text);	
			}
		});

		$("#figurenumber").textbox("textbox").bind("click",function(){
			selectDrawing();
		});
	});

</script>
</head>
<body>
		<form method="post" class="form">
		<input type="hidden" name="id" value="${materialInfo.id }"/>
		<input id="mtypetemp" name="mtypetemp" type="hidden" value="${typeCode}"/>
			<table class="table">
				<tr>
					<th>物料编码</th>
					<td><input id="materialcode" name="materialcode" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${materialInfo.materialcode }" /></td>
				<script type="text/javascript">
						$(function() {
						//避免用户：选择类型后，物料编码自动添加头，而又修改物料编码，又选择类型，导致截取。
							$("#materialcode").textbox('textbox').bind("click", function () { 
								$("#mtypetemp").val("");
							});
						});
				</script>
				</tr>				
				<tr>
					<th>物料名称</th>
					<td><input name="materialname" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${materialInfo.materialname }" /></td>
				</tr>
				<tr>
					<th>图号</th>
					<td>
						<input id="figurenumberid" name="figurenumberid" type="hidden" />
						<input id="figurenumber" class="easyui-textbox" data-options="editable:false" value="${materialInfo.figure.number }" />
					</td>
				</tr>
				<tr>
					<th>图名</th>
					<td><label id="figurename">${materialInfo.figure.docname }</label></td>
				</tr>
				<tr>
					<th>规格参数</th>
					<td><input name="spec" class="easyui-textbox" value="${materialInfo.spec }" /></td>
				</tr>
				<tr>
					<th>单位</th>
					<td><input id="unit" name="unit" class="easyui-combobox" value="${materialInfo.unit }" /></td>
				</tr>
				<tr>
					<th>类型</th>
					<td>
<!-- 					<input id="typeid" name="typeid" class="easyui-combobox" value="${materialInfo.typeid }" /> -->
						<select id="tname" name="tname" class="easyui-combotree" value="" ></select>
						<input id="typeid" name="typeid" type="hidden" value="${materialInfo.typeid }"/>
					</td>
				</tr>
				<tr>
					<th>配料类型</th>
					<td>
						<select name="delivertype" class="easyui-combobox" data-options="required:true,validType:'isBlank',editable:false,panelHeight:'auto',value:'${materialInfo.delivertype}'">
							<option value="0" selected>预约</option>
							<option value="1">申领</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td>
						<input name="remark" class="easyui-textbox" value="${materialInfo.remark }" data-options="multiline:true" style="width:100%;height:100px"
						 validtype="length[0,250]" invalidMessage="有效长度0-250"/>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
