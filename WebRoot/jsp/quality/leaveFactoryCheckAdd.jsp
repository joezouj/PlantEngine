<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html xmlns:v="urn:schemas-microsoft-com:vml" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40">
 <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="ProgId" content="Excel.Sheet"/>
  <meta name="Generator" content="WPS Office ET"/> 
  <style>
<!-- @page
	{margin:0.39in 0.28in 0.39in 0.32in;
	mso-header-margin:0.51in;
	mso-footer-margin:0.51in;}
 -->  </style>
  <!--[if gte mso 9]>
   <xml>
    <x:WorksheetOptions>
     <x:DefaultRowHeight>285</x:DefaultRowHeight>
     <x:Selected/>
     <x:Panes>
      <x:Pane>
       <x:Number>3</x:Number>
       <x:ActiveCol>4</x:ActiveCol>
       <x:ActiveRow>15</x:ActiveRow>
       <x:RangeSelection>E16:M16</x:RangeSelection>
      </x:Pane>
     </x:Panes>
     <x:ProtectContents>False</x:ProtectContents>
     <x:ProtectObjects>False</x:ProtectObjects>
     <x:ProtectScenarios>False</x:ProtectScenarios>
     <x:PageBreakZoom>100</x:PageBreakZoom>
     <x:Print>
      <x:ValidPrinterInfo/>
      <x:PaperSizeIndex>9</x:PaperSizeIndex>
      <x:HorizontalResolution>600</x:HorizontalResolution>
     </x:Print>
    </x:WorksheetOptions>
   </xml>
  <![endif]-->
  <jsp:include page="../inc.jsp"></jsp:include>
  <link rel="Stylesheet" href="${ctx}/CSS/LeaveFactoryCheck.css"/>
  <script language="JavaScript">
// 	if (window.name!="frSheet")
// 		window.location.replace("../质检.htm");
// 	else
// 		parent.fnUpdateTabs(0);
	function dosave(dialog,grid) {
		if($('#inspector').val()==""){		
 			alert("检验员编号为必填项，不可为空");
 			$('#inspector').focus();
			return ;
		}
		if($('#inspectdate').val()==""){		
				alert("检验日期为必填项，不可为空");
				$('#inspectdate').focus();
			return ;
		}
		if ($("#form").form('validate')) {
			$.post(ext.contextPath + "/quality/leavefactorycheck/save.do", $("#form").serialize(), function(data) {
				if (data.res == 1) {
						top.$.messager.alert('提示', "保存成功", 'info', function() {
								//刷新列表
								grid.datagrid('reload');
								dialog.dialog('destroy');
							});				
					
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else {
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	}	
</script>
 </head>
 <body link="blue" vlink="purple">
 <form method="post" id="form" >
  <table width="878" border="0" cellpadding="0" cellspacing="0" style='width:658.50pt;border-collapse:collapse;table-layout:fixed;'>
   <col width="31" style='mso-width-source:userset;mso-width-alt:992;'/>
   <col width="47" style='mso-width-source:userset;mso-width-alt:1504;'/>
   <col width="54" style='mso-width-source:userset;mso-width-alt:1728;'/>
   <col width="34" style='mso-width-source:userset;mso-width-alt:1088;'/>
   <col width="72" span="3" style='width:54.00pt;'/>
   <col width="65" style='mso-width-source:userset;mso-width-alt:2080;'/>
   <col width="31" style='mso-width-source:userset;mso-width-alt:992;'/>
   <col width="28" span="2" style='mso-width-source:userset;mso-width-alt:896;'/>
   <col width="17" style='mso-width-source:userset;mso-width-alt:544;'/>
   <col width="33" style='mso-width-source:userset;mso-width-alt:1056;'/>
   <col width="50" style='mso-width-source:userset;mso-width-alt:1600;'/>
   <col width="54" style='mso-width-source:userset;mso-width-alt:1728;'/>
   <col width="46" style='mso-width-source:userset;mso-width-alt:1472;'/>
   <col width="72" span="2" style='width:54.00pt;'/>
   <tr height="19" style='height:14.25pt;'>
    <td class="xl65" height="19" width="78" colspan="2" style='height:14.25pt;width:58.50pt;mso-ignore:colspan;' x:str>Q/JBSC9389</td>
    <td class="xl66" width="54" style='width:40.50pt;'></td>
    <td class="xl66" width="34" style='width:25.50pt;'></td>
    <td class="xl66" width="72" style='width:54.00pt;'></td>
    <td class="xl66" width="72" style='width:54.00pt;'></td>
    <td class="xl66" width="72" style='width:54.00pt;'></td>
    <td class="xl66" width="65" style='width:48.75pt;'></td>
    <td class="xl66" width="31" style='width:23.25pt;'></td>
    <td class="xl66" width="28" style='width:21.00pt;'></td>
    <td class="xl66" width="28" style='width:21.00pt;'></td>
    <td class="xl66" width="17" style='width:12.75pt;'></td>
    <td class="xl66" width="33" style='width:24.75pt;'></td>
    <td class="xl66" width="50" style='width:37.50pt;'></td>
    <td class="xl66" width="54" style='width:40.50pt;'></td>
    <td class="xl66" width="46" style='width:34.50pt;'></td>
    <td width="72" style='width:54.00pt;'></td>
    <td width="72" style='width:54.00pt;'></td>
   </tr>
   <tr height="19" style='height:14.25pt;'>
    <td class="xl67" height="38" colspan="16" rowspan="2" style='height:28.50pt;border-right:none;border-bottom:none;' x:str><input id="equipmentname" name="equipmentname" class="easyui-textbox"	data-options="required:true,validType:'isBlank',prompt:'设备型号及名称'" value=""/> 出厂检验记录单</td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="19" style='height:14.25pt;'>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="27" style='height:20.25pt;'>
    <td class="xl65" height="27" style='height:20.25pt;'></td>
    <td class="xl66" colspan="3" style='mso-ignore:colspan;'></td>
    <td class="xl66" colspan="3" style='mso-ignore:colspan;'></td>
    <td class="xl66" colspan="3" style='mso-ignore:colspan;'></td>
    <td class="xl68" colspan="3" style='border-right:none;border-bottom:none;' x:str>出厂编号</td>
    <td class="xl88" colspan="3" style='mso-ignore:colspan;border-right:none;border-bottom:none;' x:str><input id="leavefactoryno" name="leavefactoryno" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" value=""/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr  height="19" style='height:14.25pt;'>
    <td class="xl65" height="19" colspan="3" style='height:14.25pt;mso-ignore:colspan;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>主要技术参数</td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="19" style='height:14.25pt;'>
    <td class="xl65" height="19" colspan="2" style='height:14.25pt;mso-ignore:colspan;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>额定电压</td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66" x:str><input id="ratedvoltage" name="ratedvoltage" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" value=""/></td>
    <td class="xl65"></td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str>电操机构电压</td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str>
    	<input id="poweroperationv" name="poweroperationv" class="easyui-textbox"	data-options="required:true,validType:'isBlank',prompt:'无数值请输入‘-’'" value=""/>
	</td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="19" style='height:14.25pt;'>
    <td class="xl65" height="19" colspan="2" style='height:14.25pt;mso-ignore:colspan;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>额定电流</td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66" x:str><input id="ratedcurrent" name="ratedcurrent" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" value=""/></td>
    <td class="xl65"></td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str>分励脱扣器电压</td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str>
    	<input id="shuntreleasev" name="shuntreleasev" class="easyui-textbox"	data-options="required:true,validType:'isBlank',prompt:'无数值请输入‘-’'" value=""/>
    </td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="19" style='height:14.25pt;'>
    <td class="xl65" height="19" colspan="2" style='height:14.25pt;mso-ignore:colspan;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>安装型式</td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str><input id="installationtype" name="installationtype" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" value=""/></td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str>合闸电磁铁电压</td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str>
    	<input id="closingemv" name="closingemv" class="easyui-textbox"	data-options="required:true,validType:'isBlank',prompt:'无数值请输入‘-’'" value=""/>
    </td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="19" style='height:14.25pt;'>
    <td class="xl65" height="19" colspan="3" style='height:14.25pt;mso-ignore:colspan;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>智能脱扣器型号及规格</td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str>
    	<input id="releasemodel" name="releasemodel" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" value=""/>
	</td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str>欠压脱扣器电压</td>
    <td class="xl66"></td>
    <td class="xl66" x:str>
    	<input id="undervreleasev" name="undervreleasev" class="easyui-textbox"	data-options="required:true,validType:'isBlank',prompt:'无数值请输入‘-’'" value=""/>
    </td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="19" style='height:14.25pt;'>
    <td class="xl65" height="19" colspan="2" style='height:14.25pt;mso-ignore:colspan;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>外部附件</td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66" x:str>
    	<input id="appendix" name="appendix" class="easyui-textbox"	data-options="required:true,validType:'isBlank',prompt:'无数值请输入‘-’'" value=""/>
	</td>
    <td class="xl65"></td>
    <td class="xl66"></td>
    <td class="xl66" x:str>备注</td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66" colspan="2" style='mso-ignore:colspan;' x:str>
    	<input id="remarkoverall" name="remarkoverall" class="easyui-textbox"	data-options="required:true,validType:'isBlank',prompt:'无数值请输入‘-’'" value=""/>
	</td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td class="xl66"></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="24" style='height:18.00pt;mso-height-source:userset;mso-height-alt:360;'>
    <td class="xl66" height="24" colspan="4" style='height:18.00pt;mso-ignore:colspan;'></td>
    <td class="xl66" colspan="3" style='mso-ignore:colspan;'></td>
    <td class="xl66" colspan="9" style='mso-ignore:colspan;'></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="56" style='height:42.00pt;mso-height-source:userset;mso-height-alt:840;'>
    <td class="xl69" height="56" style='height:42.00pt;' x:str>序号</td>
    <td class="xl70" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>检验项目<br/>Inspection Item</td>
    <td class="xl70" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>技术要求<br/>Technical Requirement</td>
    <td class="xl70" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>实测结果<br/>Result</td>
    <td class="xl70" x:str>备注<br/>Remark</td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="60" style='height:45.00pt;mso-height-source:userset;mso-height-alt:900;'>
    <td class="xl73" height="60" style='height:45.00pt;' x:num>1</td>
    <td class="xl74" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>一般检查<font class="font4"><br/>Check Appearance</font></td>
    <td class="xl76" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str><span style='mso-spacerun:yes;'>&nbsp;</span><font class="font1">金属件、铭牌、二次回路、装配、接地、电气间隙、爬电距离、抽屉座等</font><font class="font4"><br/><span style='mso-spacerun:yes;'>&nbsp;</span>Nameplate,secondary loop,Assembly,Earth Marking,<br/><span style='mso-spacerun:yes;'>&nbsp;</span>clearance,creepage distance,draw-out and so on</font></td>
    <td class="xl74" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>合格<font class="font4"><br/>Qualification</font></td>
    <td class="xl74"><input id="remark1" name="remark1" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	value="" validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="89.33" style='height:67.00pt;mso-height-source:userset;mso-height-alt:1340;'>
    <td class="xl73" height="89.33" style='height:67.00pt;' x:num>2</td>
    <td class="xl74" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>机械操作特性检查<font class="font4"><br/>Check Mechcanical Operation Performance</font></td>
    <td class="xl78" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>手动操作断路器：两次闭合，两次断开；两次自由脱扣操作。电动操作断路<font class="font4"> </font><font class="font1">器：在额定控制电源的</font><font class="font4">110%</font><font class="font1">和</font><font class="font4">85%</font><font class="font1">时两次闭合，断开操作；两次自由脱扣操作</font><font class="font4"><br/><span style='mso-spacerun:yes;'>&nbsp;</span>Manual operation: close and break the breaker twice;make free trip twice.Power operation:close and break the breaker at 110% and 85% rated control power;make free trip twice.</font></td>
    <td class="xl74" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>合格<font class="font4"><br/>Qualification</font></td>
    <td class="xl74"><input id="remark2" name="remark2" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	 value=""validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="70.67" style='height:53.00pt;mso-height-source:userset;mso-height-alt:1060;'>
    <td class="xl73" height="70.67" style='height:53.00pt;' x:num>3</td>
    <td class="xl74" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>长延时<font class="font4">Ir1<br/>Long-Delay Ir1</font></td>
    <td class="xl76" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>1.05Ir1&gt;2h<font class="font1">不动作</font><font class="font4">, 1.05Ir1&lt;1h</font><font class="font1">动作</font><font class="font4"><br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>1.05Ir1&gt;2h</font><font class="font1"> Without Action</font><font class="font4">, 1.05Ir1&lt;1h Action</font></td>
    <td class="xl74" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>A<font class="font27">相</font><font class="font4">
    	<input id="phasea" name="phasea" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style='width:50px' value=""/>
    </font><font class="font27">合格</font><font class="font4"><br/>B</font><font class="font27">相</font><font class="font4">
		<input id="phaseb" name="phaseb" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style='width:50px' value=""/>
	</font><font class="font27">合格</font><font class="font4"><br/>C</font><font class="font27">相</font><font class="font4">
		<input id="phasec" name="phasec" class="easyui-textbox"	data-options="required:true,validType:'isBlank'" style='width:50px' value=""/>
	</font><font class="font27">合格</font><font class="font4"><br/>Qualification</font></td>
    <td class="xl74"><input id="remark3" name="remark3" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	 value=""validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="56" style='height:42.00pt;mso-height-source:userset;mso-height-alt:840;'>
    <td class="xl73" height="56" style='height:42.00pt;' x:num>4</td>
    <td class="xl74" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>短延时<font class="font4">Ir2<br/>Short-Delay Ir2</font></td>
    <td class="xl76" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>0.9Ir2<font class="font1">不动作</font><font class="font4">, 1.1Ir2</font><font class="font1">定时限动作</font><font class="font4"><br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>0.9Ir2 Without Action, 1.1Ir2 Delay Action</font></td>
    <td class="xl74" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>合格<font class="font4"><br/>Qualification</font></td>
    <td class="xl74"><input id="remark4" name="remark4" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	 value=""validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="56" style='height:42.00pt;mso-height-source:userset;mso-height-alt:840;'>
    <td class="xl73" height="56" style='height:42.00pt;' x:num>5</td>
    <td class="xl79" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>瞬时<font class="font4">Ir3<br/>Instantaneous Ir3</font></td>
    <td class="xl76" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>0.85Ir3<font class="font1">不动作</font><font class="font4">, 1.15Ir3</font><font class="font1">瞬时动作</font><font class="font4"><br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>0.85Ir3 Without Action, 1.15Ir3 Instantaneous Action(t&lt;100ms)</font></td>
    <td class="xl74" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>合格<font class="font4"><br/>Qualification</font></td>
    <td class="xl74"><input id="remark5" name="remark5" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	 value=""validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="66.67" style='height:50.00pt;mso-height-source:userset;mso-height-alt:1000;'>
    <td class="xl73" height="66.67" style='height:50.00pt;' x:num>6</td>
    <td class="xl74" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>欠压脱扣器<font class="font4"><br/>Under voltage Release</font></td>
    <td class="xl80" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str><span style='mso-spacerun:yes;'>&nbsp;</span>≧<font class="font4">85%Ue </font><font class="font1">可靠吸合</font><font class="font4">,(35% -70%)Ue </font><font class="font1">瞬时或延时断开</font><font class="font4">,&lt;35%Ue</font><font class="font1">不合闸</font><font class="font4"><br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span></font><font class="font1">≧</font><font class="font4">85%Ue Reliable Closing<br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>(35% -70%)Ue Instantaneous or Delay Break<br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>&lt;35%Ue Refuse to Close</font></td>
    <td class="xl74" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>-</td>
    <td class="xl74"><input id="remark6" name="remark6" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	 value=""validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="56" style='height:42.00pt;mso-height-source:userset;mso-height-alt:840;'>
    <td class="xl73" height="56" style='height:42.00pt;' x:num>7</td>
    <td class="xl74" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>分励脱扣器<font class="font4"><br/>Shunt Release</font></td>
    <td class="xl76" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>(70%-100%)Ue <font class="font1">应可靠动作</font><font class="font4"><br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>(70%-110%)Ue Reliable Action</font></td>
    <td class="xl79" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>合格<font class="font4"><br/>Qualification</font></td>
    <td class="xl74"><input id="remark7" name="remark7" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	 value=""validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="56" style='height:42.00pt;mso-height-source:userset;mso-height-alt:840;'>
    <td class="xl73" height="56" style='height:42.00pt;' x:num>8</td>
    <td class="xl74" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>释能脱扣器<font class="font4"><br/>Energy Releasing Release</font></td>
    <td class="xl76" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>(85%-100%)Ue <font class="font1">应可靠动作</font><font class="font4"><br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>(85%-110%)Ue Reliable Action</font></td>
    <td class="xl74" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>合格<font class="font4"><br/>Qualification</font></td>
    <td class="xl74"><input id="remark8" name="remark8" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	 value=""validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="56" style='height:42.00pt;mso-height-source:userset;mso-height-alt:840;'>
    <td class="xl73" height="56" style='height:42.00pt;' x:num>9</td>
    <td class="xl74" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>耐压试验<font class="font4"><br/>Voltage Releasing Test</font></td>
    <td class="xl78" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str><span style='mso-spacerun:yes;'>&nbsp;</span>主回路2500V/1s,控制回路1890V/1s,不允许有闪烙、击穿现象<font class="font4"><br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>Major Loop 2500V/1s,Control Loop 1890V/1s.<br/><span style='mso-spacerun:yes;'>&nbsp;&nbsp;</span>No flashover and destructive phenomenon should be allowed.</font></td>
    <td class="xl74" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>合格<font class="font4"><br/>Qualification</font></td>
    <td class="xl74"><input id="remark9" name="remark9" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	 value=""validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="56" style='height:42.00pt;mso-height-source:userset;mso-height-alt:840;'>
    <td class="xl81" height="56" style='height:42.00pt;' x:num>10</td>
    <td class="xl82" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:none;' x:str>包装检查<font class="font4"><br/>Packing inspection</font></td>
    <td class="xl84" colspan="9" style='border-right:.5pt solid windowtext;border-bottom:none;' x:str>断路器实物必须与箱内外标识的规格一致，箱内附件必须齐全<font class="font4">(</font><font class="font1">对照附件表</font><font class="font4">)<br/>Products must be consistent with the specifications of identity, accessories in the boxes must be complete(according to the upfile).</font></td>
    <td class="xl82" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:none;' x:str>合格<font class="font4"><br/>Qualification</font></td>
    <td class="xl82"><input id="remark10" name="remark10" class="easyui-textbox"  style='width:60px;overflow:auto;height:40px;' data-options="multiline:true"	 value=""validtype="length[0,250]" invalidMessage="有效长度0-250"/></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="40" style='height:30.00pt;mso-height-source:userset;mso-height-alt:600;'>
    <td class="xl74" height="40" colspan="4" style='height:30.00pt;border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>结论<font class="font4"><br/>Conclusion</font></td>
    <td class="xl74" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>合格<font class="font4"><br/>Qualification</font></td>
    <td class="xl74" x:str>检验员<font class="font4"><br/>Inspector</font></td>
    <td class="xl91" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>检<font class="font4">
    	<input id="inspector" name="inspector" style='width:40px' class="easyui-textbox,prompt:'输入检验员序号，如‘77’'"	data-options="required:true,validType:'isBlank'" value=""/>
	</font></td>
    <td class="xl74" colspan="2" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:str>日期<font class="font4"><br/>Date</font></td>
    <td class="xl94" colspan="3" style='border-right:.5pt solid windowtext;border-bottom:.5pt solid windowtext;' x:num="42319.">
    	<input name="inspectdate" id="inspectdate" class="Wdate" value="${nowdate }" style='width:100px' onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" readonly>
    </td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="19" style='height:14.25pt;'>
    <td height="19" colspan="4" style='height:14.25pt;mso-ignore:colspan;'></td>
    <td colspan="3" style='mso-ignore:colspan;'></td>
    <td colspan="9" style='mso-ignore:colspan;'></td>
    <td colspan="2" style='mso-ignore:colspan;'></td>
   </tr>
   <tr height="19" style='height:14.25pt;'>
<!--     <td class="xl86" height="19" colspan="4" style='height:14.25pt;border-right:none;border-bottom:none;' x:str>2015.11.11 11:11:11AM</td>
    <td colspan="3" style='mso-ignore:colspan;'></td>
    <td colspan="9" style='mso-ignore:colspan;'></td>
    <td colspan="2" style='mso-ignore:colspan;'></td> -->
   </tr>
   <![if supportMisalignedColumns]>
    <tr width="0" style='display:none;'>
     <td width="31" style='width:23;'></td>
     <td width="47" style='width:35;'></td>
     <td width="54" style='width:41;'></td>
     <td width="34" style='width:26;'></td>
     <td width="65" style='width:49;'></td>
     <td width="31" style='width:23;'></td>
     <td width="28" style='width:21;'></td>
     <td width="17" style='width:13;'></td>
     <td width="33" style='width:25;'></td>
     <td width="50" style='width:38;'></td>
     <td width="54" style='width:41;'></td>
     <td width="46" style='width:35;'></td>
    </tr>
   <![endif]>
  </table>
  </form>
 </body>
</html>
