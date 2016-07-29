<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sipai.tools.SessionManager"%>
<%
	SessionManager sessionManager = new SessionManager();
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript" src="http://sandbox.runjs.cn/uploads/rs/233/bkf2ntm7/jquery.fullcalendar.js?v=0.7"></script>
<style scoped="scoped">
		.md{
			background-position:2px center;
			text-align:center;
			font-weight:bold;
			font-size:20px;
			padding:0 2px;
		}
</style>
<script type="text/javascript" src="json2.js"></script> 
<script type="text/javascript">
	var grid;
	var doSchedulingFun = function(dt,grouptypeid,grouptypename){
		var tdt = new Date(Date.parse(dt.replace("-","/")));
		var myDate=new Date();//取今天的时间
		var year=myDate.getFullYear();
		var month=myDate.getMonth()+1;
		var day=myDate.getDate();
		var ndt = new Date(Date.parse(year+"/"+month+"/"+day));
		var dialog = top.ext.modalDialog({
			title : '排班管理（'+dt+','+grouptypename+'）注：过期日期不能修改排班',
			width : 1000,
			height : 600,
			resizable:true,
			url : ext.contextPath + '/work/workscheduling/schedulingtogroup.do?dt='+dt+'&grouptypeid='+grouptypeid,
			buttons : [ {
				text : '保存',
				handler : function() {
					if(tdt<ndt){
						parent.$.messager.alert('提示','日期已过期，无法保存！','info');
					}else{
						dialog.find('iframe').get(0).contentWindow.dosave(dialog, window);
					}
				}
			} ]
		});
	};
	var addFun = function(){
		var dialog = top.ext.modalDialog({
			title : '选择排班人',
			width : 800,
			height : 600,
			resizable:true,
			url : ext.contextPath + '/work/group/showlistForSelect.do',
			buttons : [{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectFun();
					if(res!=null){
						dialog.dialog('destroy');
						var str="";
						$.each(res.result,function(index,item){
							str+=item.userid+",";
						});
						$.post(ext.contextPath + '/work/workscheduling/save.do', {ids:str.replace(/,$/g,""),taskid:'${param.taskid}',workstationid:'${param.workstationid}'} , function(data) {
							if(data>0){
								parent.$.messager.alert('提示','添加成功','info',function(){
									grid.datagrid('reload');
									grid.datagrid('clearChecked');
								});
							}else{
								parent.$.messager.alert('提示','添加失败','info');
							}
						});
					}
				}
			}]
		});
	};
	var grouptype = function(){
		var dialog = parent.ext.modalDialog({
				title : '工段维护',
				url : ext.contextPath + '/work/grouptype/showlist.do',
				width:1200,
				height:600
			});
	};
	var grouptypeid="";
	var grouptypename="";
	var dosheduling = function(d,dname){
		grouptypeid=d;
		grouptypename=dname;
	};
	function getNextFormatDate() {
		    var nowdate = new Date();
		    var seperator1 = "-";
		    var seperator2 = ":";
		    nowdate=nowdate.valueOf()+1000*60*60*24;
        	var date = new Date(nowdate);
		    var year = date.getFullYear();
		    var month = date.getMonth() + 1;
		    var strDate = date.getDate();
		    if (month >= 1 && month <= 9) {
		        month = "0" + month;
		    }
		    if (strDate >= 0 && strDate <= 9) {
		        strDate = "0" + strDate;
		    }
		    var nextdate = year + seperator1 + month + seperator1 + strDate;
		    return nextdate;
		}
	var refreshcalerdar=function(){
		var month=$('#cc').calendar('options')['month'];
		var year=$('#cc').calendar('options')['year'];
		var btext="";
		$.post(ext.contextPath + '/work/workscheduling/getGTScheduling.do',{year:year,month:month}, function(data) {
			var obj = JSON.parse(data);
			var obj_gt=obj.grouptype;
			var obj_ws=obj.workscheduling;
			$('#cc').calendar({
				formatter: function(date){
					btext="";
					var d = date.getDate();
					var m = date.getMonth()+1;
					
					for(var i=0;i<obj_gt.length;i++){
						var j=0;
						for(j=0;j<obj_ws.length;j++)
						{
							var workstdt=obj_ws[j].workstdt;
							//alert(workstdt);
							wsmonth=String(parseInt(workstdt.substring(5,7)));//去除数据库查询日期中的"0'
							wsday=String(parseInt(workstdt.substring(8,10)));
							if(m==wsmonth && d==wsday && obj_gt[i].id==obj_ws[j].grouptypeid){
								btext=btext+'&nbsp&nbsp'+'<a class="easyui-linkbutton"  style="font-weight:bold;color:blue;font-size:16px;" onmouseover="changeback1(this)" onmouseout="changeback2(this)" onclick="dosheduling(\''+obj_gt[i].id+'\',\''+obj_gt[i].name+'\')">'+obj_gt[i].name+ '</a>';
								break;
							}
						}
						if(j==obj_ws.length){
							btext=btext+'&nbsp&nbsp'+'<a class="easyui-linkbutton" style="color:black;font-size:16px;" onmouseover="changeback1(this)" onmouseout="changeback2(this)" onclick="dosheduling(\''+obj_gt[i].id+'\',\''+obj_gt[i].name+'\')">'+obj_gt[i].name+ '</a>';
						}
					}
					var bt='<div class="md" style="font-size:26px;" >'+d+'</div>';
					bt=bt+btext;
					return bt;
				},
				onSelect: function(date){
					var dt=date.getFullYear()+"-";
					if(date.getMonth()+1<10){
						dt=dt+"0";
					}
					dt=dt+(date.getMonth()+1)+"-";
					if(date.getDate()<10){
						dt=dt+"0";
					}
					dt=dt+date.getDate();
					
					if(grouptypeid!=""){
						doSchedulingFun(dt,grouptypeid,grouptypename);
					}
					grouptypeid="";
					grouptypename="";
				}
			});
		});
	};
	var lastcolor;
	var changeback1=function(obj){
	
		lastcolor=obj.style.color.toLowerCase();
		//alert(lastcolor);
		obj.style.color=obj.style.color.toLowerCase()=='red'?lastcolor:'red';
	};
	var changeback2=function(obj){
		obj.style.color=obj.style.color.toLowerCase()=='red'?lastcolor:'red';
	};
	$(function() {
		
		refreshcalerdar();
			
		$('#cc div.calendar-nav').click(function () {
              setTimeout("refreshcalerdar()",0);//setTimeout产生异步效果
           });
         $('#cc div.calendar-menu-month-inner').click(function () {
              setTimeout("refreshcalerdar()",0);
         });
		
         $(".calendar-title .calendar-text").css("fontSize","18px");
	});
	
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false" style="font-family:微软雅黑">
	<div class="easyui-panel"  data-options="fit:true" style="padding:2px;">
		<div id="cc" class="easyui-calendar" data-options="fit:true" >
		</div>
	</div>
	
</body>
</html>