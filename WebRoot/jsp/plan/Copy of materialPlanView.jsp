<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
/**
* EasyUI DataGrid根据字段动态合并单元格
* 参数 tableID 要合并table的id
* 参数 colList 要合并的列,用逗号分隔(例如："name,department,office");
*/
function mergeCellsByField(tableID, colList) {
    var ColArray = colList.split(",");
    var tTable = $("#" + tableID);
    var TableRowCnts = tTable.datagrid("getRows").length;
    var tmpA;
    var tmpB;
    var PerTxt = "";
    var CurTxt = "";
    var alertStr = "";
    for (j = ColArray.length - 1; j >= 0; j--) {
        PerTxt = "";
        tmpA = 1;
        tmpB = 0;

        for (i = 0; i <= TableRowCnts; i++) {
            if (i == TableRowCnts) {
                CurTxt = "";
            }
            else {
                CurTxt = tTable.datagrid("getRows")[i][ColArray[j]];
            }
            if (PerTxt == CurTxt) {
                tmpA += 1;
            }
            else {
                tmpB += tmpA;
                
                tTable.datagrid("mergeCells", {
                    index: i - tmpA,
                    field: ColArray[j],　　//合并字段
                    rowspan: tmpA,
                    colspan: null
                });
                tTable.datagrid("mergeCells", { //根据ColArray[j]进行合并
                    index: i - tmpA,
                    field: "Ideparture",
                    rowspan: tmpA,
                    colspan: null
                });
               
                tmpA = 1;
            }
            PerTxt = CurTxt;
        }
    }
}
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : ext.contextPath + '/plan/materialplan/getmaterialplan.do?planid=${dailyPlanSummary.id}',
			striped : true,
			select : false,
			selectOnCheck: false,
			checkOnSelect: false,
			idField : 'id',
			columns : [ [ 	 
					{width : '200', title : '工位名称',  field: 'workstationserial', sortable : true, align:'center' , formatter : function(value, row){
						if(value!=null){
							return value+"<br>（"+row.workstationname+"）";
						}else{
							return "";
						}
						}},
					{width : '180', title : '物料编码', field : 'materialcode', sortable : true, halign:'center' , formatter : function(value, row){
						if(value!=null){
							return value;
						}else{
							return "";
						}
						}},
					{width : '280', title : '物料名称', field : 'materialname', sortable : true, halign:'center' , formatter : function(value, row){
						if(value!=null){
							return value;
						}else{
							return "";
						}
						}},
					{width : '100', title : '数量', field : 'amount', sortable : true, halign:'center' , formatter : function(value, row){
						if(value!=null){
							if(value>0){
								return value+" "+row.unit;
							}
							return "";
							
						}else{
							return "";
						}
						}}
			] ],
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', ext.pixel_0);
				if (data.rows.length > 0) {
                    //调用mergeCellsByField()合并单元格
                    mergeCellsByField("grid", "workstationserial");
                }
			}
		});
	});
</script>
</head>
<body>
	<div style="text-align:center">
	<table class="table">				
		<tr>
			<th>计划名称</th>
			<td>
			${dailyPlanSummary.name }
			</td>
			<th>计划日期</th>
			<td>${dailyPlanSummary.plandt==null?"":(fn:substring(dailyPlanSummary.plandt,0,10))}</td>
		</tr>
	</table>
	<br/>
	
	</div>
	<table id="grid" class="easyui-table" data-options="border:true" ></table>
</body>
</html>
