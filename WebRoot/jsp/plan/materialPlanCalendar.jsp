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
<script type="text/javascript" src="json2.js"></script> 
	<style scoped="scoped">
			.md{
				background-position:2px center;
				text-align:center;
				font-weight:bold;
				font-size:20px;
				padding:0 2px;
			}
	</style>
	<script>
	var grid;
	var configDeliver = function(planid) {
		var dialog = parent.ext.modalDialog({
			title : '配料计划管理',		
			url : ext.contextPath + '/plan/materialdeliver/showlist.do?planid='+planid
		});
	};
	var showPlan = function(sdt,edt) {
		var dialog = parent.ext.modalDialog({
			title : '查看物料计划',	
			height:"90%",
			width:"90%",
			//url : ext.contextPath + '/plan/materialplan/showplan.do?&planid='+planid
			url : ext.contextPath + '/plan/dailyplansummary/showorderlist.do?&sdt='+sdt+'&edt='+edt+'&pageflag=materialplan',
		});
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
		$.post(ext.contextPath + '/plan/materialplan/getCalendarData.do',{year:year,month:month}, function(data) {
			var obj = JSON.parse(data);
			var materialPlan=obj.materialPlan;
			$('#cc').calendar({
				formatter: function(date){
					btext="";
					var d = date.getDate();
					var m = date.getMonth()+1;					
					var y = date.getFullYear();
					var tempm="";
					var tempd="";
				 	if (m >= 1 && m <= 9) {
				 		tempm = "0";
				    }
				    if (d >= 0 && d <= 9) {
				    	tempd = "0";
				    }
					var sdt = y+"-"+tempm+m+"-"+tempd+d+" 00:00:00";
					var edt = y+"-"+tempm+m+"-"+tempd+d+" 23:59:59";
					var flag = true;
					for(var j=0;j<materialPlan.length;j++)
					{
						var plandt=materialPlan[j].plandt;
						var wsmonth=String(parseInt(plandt.substring(5,7)));
						var wsday=String(parseInt(plandt.substring(8,10)));
						if(m==wsmonth && d==wsday){
							//btext+='<a class="easyui-linkbutton" style="font-weight:bold;color:blue;font-size:16px;" onmouseover="changeback1(this)" onmouseout="changeback2(this)" onclick="configDeliver(\''+materialPlan[j].id+'\')">配置</a>';
							btext+='&nbsp&nbsp'+'<a class="easyui-linkbutton" style="font-weight:bold;color:blue;font-size:16px;" onmouseover="changeback1(this)" onmouseout="changeback2(this)" onclick="showPlan(\''+sdt+'\',\''+edt+'\')">查看</a>';
							flag = false;
							break;
						}
					}
					if(flag){
						/* btext+='<a class="easyui-linkbutton" style="color:gray;font-size:16px;" onclick="alert(\'无对应日计划\')">配置</a>'; */
						btext+='&nbsp&nbsp'+'<a class="easyui-linkbutton" style="color:gray;font-size:16px;"  onclick="alert(\'无对应日计划\')">查看</a>';						
					}
					
					var bt='<div class="md" style="font-size:26px;" >'+d+'</div>';
					bt=bt+btext;
					return bt;
				}
			});
		});
	};
	var lastcolor;
	var changeback1=function(obj){
		lastcolor=obj.style.color.toLowerCase();
		obj.style.color=obj.style.color.toLowerCase()=='red'?lastcolor:'red';
	};
	var changeback2=function(obj){
		obj.style.color=obj.style.color.toLowerCase()=='red'?lastcolor:'red';
	};
	$(function() {
		refreshcalerdar();
			
		$('#cc div.calendar-nav').click(function () {
			//setTimeout产生异步效果
            setTimeout("refreshcalerdar()",0);
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
		<div id="cc" class="easyui-calendar" data-options="fit:true" ></div>
	</div>
</body>
</html>