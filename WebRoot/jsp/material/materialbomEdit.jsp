<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	var editIndex = undefined;//当前编辑行索引
	
	$(function() {
		$('#unit').combobox({ 
			url:ext.contextPath + '/material/materialunit/getJsonUnit.do', 
			valueField:'unit', 
			textField:'unit',
			editable:false,
			panelHeight:'auto'
		});
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/material/materialbom/getBOMDetailsJson.do?pid=${materialBOM.id}',
			striped : true,
			rownumbers : true,
			singleSelect : true,
			onClickCell: editFun,
			idField : 'id',
			pageSize : 20,
			pageList : [20, 50, 100],
			columns : [ [ 
// 				{width : '60', title : '序号', field : 'ordernumber', sortable : true, editor:'textbox',halign:'center',align:'center'},
				{width : '280', title : '物料编码', field : 'materialcode', sortable : true, halign:'center',align:'left'},
				{width : '300', title : '物料名称', field : 'materialname', sortable : true, halign:'center',align:'left'},
				{width : '60', title : '数量', field : 'num', sortable : true, editor:'textbox',halign:'center',align:'center'},
				{width : '50', title : '单位', field : 'unit', sortable : true, editor:'textbox',halign:'center',align:'center'},
				{width : '40', title : '版本', field : 'version', sortable : true,halign:'center',align:'center'}
			] ],
			toolbar : '#toolbar'
		});
	});
	
	function endEditing(){//清除editor
        if (editIndex == undefined){return true;}
        if ($('#grid').datagrid('validateRow', editIndex)){
            $('#grid').datagrid('endEdit', editIndex);
            editIndex = undefined;
            return true;
       	} else {
            return false;
        }
    }
	//新增行
	var addFun = function() {
		if(!endEditing()){
			return false;
		}

		editIndex = 0;//设置当前编辑行
		
		if($('#grid').datagrid('getRows')[editIndex]==undefined){//如果一行都不存在
			$('#grid').datagrid('insertRow', {index: editIndex,row: {materialcode:'onClick:function (){alert("test")}',materialpcode:'${materialInfo.materialcode}'}});
		}else{
			if($('#grid').datagrid('getRows')[editIndex]['id']!=undefined){//判断是否已经存在未保存的新增行
				$('#grid').datagrid('insertRow', {index: editIndex,row: {materialpcode:'${materialInfo.materialcode}'}});
			}
		}
		
		$('#grid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
		
		var ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'name'});
        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
		
	};
	//编辑行
	var editFun = function (index, field){
		if(!endEditing()){
			return false;
		}
		
		editIndex = index;
       	$('#grid').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
       
       	var ed = $('#grid').datagrid('getEditor', {index:editIndex,field:field});
       	($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
    };
    //保存行
	var saveFun = function() {

		if($('#grid').datagrid('getSelected')==null){
			top.$.messager.alert('提示','请先选择要保存的记录','info');
		}else{
			var id = $('#grid').datagrid('getSelected').id;
			var url;
			if(id==undefined){//新增
				editIndex = $('#grid').datagrid('getRowIndex');
				url = ext.contextPath + '/material/materialbom/save.do';
			}else{//编辑
				editIndex = $('#grid').datagrid('getRowIndex',id);
				url = ext.contextPath + '/material/materialbom/update.do';
			}
			//判断必输项
			if(!$('#grid').datagrid('validateRow', editIndex)){
				top.$.messager.alert('提示','请输入必输项','info');
				return false;
			}
			//获取数据
			//var ordernumber_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'ordernumber'});
 			//var ordernumber = $(ordernumber_ed.target).textbox('getValue');
			//var materialcode_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'materialcode'});
			//var materialcode = $(materialcode_ed.target).textbox('getValue');
			var num_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'num'});
			var num = $(num_ed.target).textbox('getValue');
			
			var unit_ed = $('#grid').datagrid('getEditor', {index:editIndex,field:'unit'});
			var unit = $(unit_ed.target).textbox('getValue');
			
			$.post(url,{id:id,num:num,unit:unit},function(data){
				if(data.res == '1'){
					top.$.messager.alert('提示','保存成功','info',function (){
						$('#grid').datagrid('reload');
					});
				}else{
					top.$.messager.alert('提示','保存失败','info');
				}
			},'json');
		}
	};
	//删除行
	var deleteFun = function() {
		if($('#grid').datagrid('getSelected')==null){
			top.$.messager.alert('提示','请先选择要删除的记录','info');
		}else{
			var id = $('#grid').datagrid('getSelected').id;
			if(id==undefined){//未经保存的数据
				top.$.messager.confirm('提示', '确定删除此记录？', function(r) {
					if (r) {
						editIndex = $('#grid').datagrid('getRowIndex');
						$('#grid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
					}
				});
			}else{
				top.$.messager.confirm('提示', '确定删除此记录？', function(r) {
					if (r) {
						$.post(ext.contextPath + '/material/materialbom/delete.do', {id : id}, function(data) {
							if(data==1){
								top.$.messager.alert('提示','删除成功','info',function(){
									$('#grid').datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
								});
							}else{
								top.$.messager.alert('提示','删除失败','info');
							}
						});
					}
				});
			}
		}
	};
	function selectMaterials(){
		var dialog = parent.ext.modalDialog({
				title : '选择材料',
				width : 800,
				height : 400,
				closeOnEscape:true,
				url : ext.contextPath + '/material/materialinfo/selectMaterials.do',
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectOK();
						if(res!=null){
							dialog.dialog('destroy');
							var materialcode = (res.materialcode == 'undefined')?'':res.materialcode;
							var pid = '${materialBOM.id}';
							
							$.post(ext.contextPath + '/material/materialbom/initsave.do',{pid:pid,materialcode:materialcode},function(data){
								if(data.res > 0){
									top.$.messager.alert('提示','保存成功','info',function (){
										$('#grid').datagrid('reload');
									});
								}else{
									top.$.messager.alert('提示','保存失败','info');
								}
							},'json');
						}
						
					}
				}
				]
			});
	}
	
	function selectBOMs(){
		var dialog = parent.ext.modalDialog({
				title : '选择成品',
				width : 800,
				height : 400,
				closeOnEscape:true,
				url : ext.contextPath + '/material/materialbom/selectBOMs.do?id=${materialBOM.id}',
				buttons : [{
					text : '确定',
					iconCls:'icon-ok',
					handler : function() {
						var res=dialog.find('iframe').get(0).contentWindow.selectOK();
						if(res!=null){
							dialog.dialog('destroy');
							var bomid = (res.bomid == 'undefined')?'':res.bomid;
							var pid = '${materialBOM.id}';
							
							$.post(ext.contextPath + '/material/materialbom/initbomsave.do',{pid:pid,bomid:bomid},function(data){
								if(data.res > 0){
									top.$.messager.alert('提示','保存成功','info',function (){
										$('#grid').datagrid('reload');
									});
								}else{
									top.$.messager.alert('提示','保存失败','info');
								}
							},'json');
						}
						
					}
				}
				]
			});
	}
	
	function doupdate() {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/material/materialbom/update.do", $(".form").serialize(), function(result) {
				if(result.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info');
				}else if(result.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', result.res, 'info');
				}
			},'json');
		}
	}
</script>
</head>
<body>
	<div id="tt">
		<a href="javascript:void(0)" class="icon-save" title="保存" onclick="doupdate()"></a>
	</div>
	<div class="easyui-panel" title="BOM信息" style="padding:10px;"
		data-options="tools:'#tt'">
<form method="post" class="form">
		<input type="hidden" name="id" value="${materialBOM.id }"/>
		<input type="hidden" id="num" name="num" value="${materialBOM.num }" />
			<table class="table">
				<tr>
					<th>物料编码</th>
					<td>${materialBOM.materialcode }</td>
					<input id="materialcode" name="materialcode" type="hidden"
						value="${materialBOM.materialcode }" />
					<th>物料名称</th>
					<td><input id="materialname" name="materialname" class="easyui-textbox"
						 value="${materialBOM.materialname }" /></td>
				</tr>
				<tr>
					<th>序号</th>
					<td><input id="ordernumber" name="ordernumber" class="easyui-numberbox"
						data-options="required:true,validType:'isBlank'" value="${materialBOM.ordernumber }" /></td>
					<th>版本</th>
					<td><input id="version" name="version" class="easyui-numberbox"
						data-options="" value="${materialBOM.version }" /></td>
				</tr>
				<tr>
					<th>单位</th>
					<td colspan="3"><input id="unit" name="unit" class="easyui-combobox" value="${materialBOM.unit }" /></td>
				</tr>
				<tr>
					<th>备注</th>
					<td colspan="3">
					<input name="remark" class="easyui-textbox" value="${materialBOM.remark }" validtype="length[0,250]" invalidMessage="有效长度0-250"
					 data-options="multiline:true" style="width:100%;height:100px"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<br/>
	<div class="easyui-panel" title="物料清单">
	<div id="toolbar">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="selectBOMs();">添加成品</a>
							</td>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" 
									onclick="selectMaterials();">添加材料</a>
							</td>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" 
									onclick="deleteFun();">删除</a>
							</td>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" 
									onclick="saveFun();">保存</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="border:false">
		<table id="grid" data-options="border:false"></table>
	</div>
	</div>
	
</body>
</html>
