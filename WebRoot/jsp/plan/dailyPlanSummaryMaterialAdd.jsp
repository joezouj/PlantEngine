<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,grid) {
		var rowsdata=$('#grid').datagrid('getRows');
		if (rowsdata.length>0) {
			var dailyplansummaryid='${param.id}';
			var materialid="";
			var workstationid="";
			var amount="";
			var remark="";//暂时没做输入，后续完善
			$.each(rowsdata, function(index, item){
				if(item.materialid==null || item.materialid==""){
					materialid+="0,";
				}else{
					materialid+=item.materialid+",";
				}
				if(item.workstationid==null || item.workstationid==""){
					workstationid+="0,";
				}else{
					workstationid+=item.workstationid+",";
				}
				
				var editorR = $('#grid').datagrid('getEditor', {index:index,field:"remark"});
				if(editorR!=null&&editorR!=undefined){
					var editRemark = editorR.target.val();
					remark+=editRemark+",";
				}else{
					remark+=" ,";
				}
								
				if(item.amount==null || item.amount==""){
					amount+="0,";
				}else{
					var editor = $('#grid').datagrid('getEditor', {index:index,field:"amount"});
					if(editor!=null&&editor!=undefined){
						var editAmount = editor.target.val();
						amount+=editAmount+",";
					}else{
						amount+=item.amount+",";
					}
				}
			}); 
			$.post(ext.contextPath + "/plan/dailyplansummarymaterial/save.do", {dailyplansummaryid:dailyplansummaryid,materialid:materialid,workstationid:workstationid,amount:amount,remark:remark}, function(data) {					
	   				top.$.messager.alert("提示", data.res, "info", function() {
	   				dialog.dialog('destroy');
	   				});
	    		
			},'json');
		}else{
			return;
		}

	}
$(function(){
	 grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath +  '/plan/materialplan/getmaterialplan.do',
			queryParams: {
				planid:'${param.id}'
			},
			rownumbers : true,
			striped : true, 			
			ctrlSelect:true,
			singleSelect: false,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id', 			
			pageSize : 20,
			pageList : [20, 50, 100],
			columns : [ [ 	
							{width : '100', title : '工位编号',  field: 'workstationserial', sortable : true, align:'center' , formatter : function(value, row){
								if(value!=null){
									return value;
								}else{
									return "";
								}
								}},
							{width : '150', title : '工位名称',  field: 'workstationname', sortable : true, align:'center' , formatter : function(value, row){
								if(value!=null){
									return value;
								}else{
									return "";
								}
								}},
							{width : '150', title : '物料编码', field : 'materialcode', sortable : true, halign:'center' , formatter : function(value, row){
								if(value!=null){
									return value;
								}else{
									return "";
								}
								}},
							{width : '260', title : '物料名称', field : 'materialname', sortable : true, halign:'center' , formatter : function(value, row){
								if(value!=null){
									return value;
								}else{
									return "";
								}
								}},
							{width : '90', title : '总计划量', field : 'amount', halign:'center' ,editor:{type:'numberbox',options:{precision:1 }}},
							{width : '90', title : '单位',  field: 'unit', sortable : true, align:'center' , formatter : function(value, row){
									if(value!=null){
										return value;
									}else{
										return "";
									}
							}}
					] ],
					onClickCell: function (rowIndex, field, value) {
						beginEditing(rowIndex, field, value);
	       			 	$('.iconImg').attr('src', ext.pixel_0);
	    			}
		});
});
var editIndex = undefined;
var beginEditing = function (rowIndex, field, value) {	
    if (field!= "amount" ){
    	if(editIndex!=undefined)
    	{
    		$('#grid').datagrid('endEdit', editIndex);
    		editIndex = undefined;
    	}
    	return;
    }
    if (rowIndex != editIndex) {
        if (endEditing()) {
            $('#grid').datagrid('beginEdit', rowIndex);
            editIndex = rowIndex;

            var ed = $('#grid').datagrid('getEditor', { index: rowIndex, field: 'amount' });
            $(ed.target).focus().bind('blur', function () {
                endEditing();
            });
        } else {
            $('#grid').datagrid('selectRow', editIndex);
            $('.iconImg').attr('src', ext.pixel_0);
        }
    }
};
var endEditing = function () {
    if (editIndex == undefined){ 
    	return true; 
    }
    if ($('#grid').datagrid('validateRow', editIndex)) {
        var ed = $('#grid').datagrid('getEditor', { index: editIndex, field: 'amount' });
        var number = $(ed.target).numberbox('getValue');
        $('#grid').datagrid('getRows')[editIndex]['amount'] = number;
        $('#grid').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
};
</script>
</head>
<body>
	<div id="cc" class="easyui-layout" style="width:800px;height:600px;" data-options="fit:true,border:false">
		<div data-options="region:'center',title:'MRP自动计算结果【日计划单:&nbsp;${param.name }】'" style="padding:0px;">
			<table id="grid" data-options="fit:true,border:false"></table>
		</div>			
	    <div data-options="region:'east',iconCls:'icon-reload',title:'呆滞物料',split:true" style="width:30%" >呆滞物料</div>
	</div>
</body>
</html>
