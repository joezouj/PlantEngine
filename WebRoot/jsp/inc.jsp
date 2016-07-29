<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>

<%String contextPath = request.getContextPath();%>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>

<%
Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
Cookie[] cookies = request.getCookies();
if (null != cookies) {
	for (Cookie cookie : cookies) {
		cookieMap.put(cookie.getName(), cookie);
	}
}
String easyuiTheme = "default";//指定如果用户未选择样式，那么初始化一个默认样式
if (cookieMap.containsKey("easyuiTheme")) {
	Cookie cookie = (Cookie) cookieMap.get("easyuiTheme");
	easyuiTheme = cookie.getValue();
}
%>

<script type="text/javascript">
var ext = ext || {};
ext.contextPath = '<%=contextPath%>';
ext.basePath = '<%=basePath%>';
ext.pixel_0 = '<%=contextPath%>/IMG/pixel_0.gif';//0像素的背景，一般用于占位
</script>

<%-- 引入jQuery --%>
<script type="text/javascript" src="<%=contextPath%>/JS/jquery-easyui-1.4.5/jquery.min.js" charset="utf-8"></script>
<%-- 引入jquery扩展 --%>
<script type="text/javascript" src="<%=contextPath%>/JS/extJquery.js"  charset="utf-8"></script>

<%-- 引入EasyUI --%>
<link id="easyuiTheme" rel="stylesheet" href="<%=contextPath%>/JS/jquery-easyui-1.4.5/themes/<%=easyuiTheme%>/easyui.css" type="text/css">
<link rel="stylesheet" href="<%=contextPath%>/JS/jquery-easyui-1.4.5/themes/icon.css" type="text/css">
<script type="text/javascript" src="<%=contextPath%>/JS/jquery-easyui-1.4.5/jquery.easyui.min.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=contextPath%>/JS/jquery-easyui-1.4.5/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
<%-- 引入easyui扩展 --%>
<script src="<%=contextPath%>/JS/extEasyUI.js" type="text/javascript" charset="utf-8"></script>

<%-- 引入easyui扩展detailview --%>
<script src="<%=contextPath%>/JS/jquery-easyui-datagridview/datagrid-detailview.js" type="text/javascript" charset="utf-8"></script>

<%-- 引入easyui扩展detail-dnd --%>
<script type="text/javascript" src="<%=contextPath%>/JS/jquery-easyui-datagrid-dnd/datagrid-dnd.js"></script>

<%-- 引入my97日期时间控件 --%>
<script type="text/javascript" src="<%=contextPath%>/JS/My97DatePicker/WdatePicker.js" charset="utf-8"></script>

<%-- 引入ueditor控件 --%>
<script type="text/javascript" charset="utf-8">window.UEDITOR_HOME_URL = '<%=contextPath%>/JS/ueditor1_4_2-utf8-jsp/';</script>
<script src="<%=contextPath%>/JS/ueditor1_4_2-utf8-jsp/ueditor.config.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=contextPath%>/JS/ueditor1_4_2-utf8-jsp/ueditor.all.min.js" type="text/javascript" charset="utf-8"></script>

<%-- 引入Highcharts --%>
<script src="<%=contextPath%>/JS/Highcharts-3.0.6/js/highcharts.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=contextPath%>/JS/Highcharts-3.0.6/js/modules/exporting.js" type="text/javascript" charset="utf-8"></script>

<%-- 引入webuploader-0.1.5 --%>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/webuploader-0.1.5/webuploader.css">
<script type="text/javascript" src="<%=contextPath%>/JS/webuploader-0.1.5/webuploader.js"></script>

<%-- 引入扩展图标 --%>
<link rel="stylesheet" href="<%=contextPath%>/CSS/extIcon.css" type="text/css">

<%-- 引入CSS --%>
<link rel="stylesheet" href="<%=contextPath%>/CSS/comm.css" type="text/css">

<%-- 引入js --%>
<script type="text/javascript" src="<%=contextPath%>/JS/comm.js" charset="utf-8"></script>

<%-- 引入二维码 --%>
<script type="text/javascript" src="<%=contextPath%>/JS/qrcode/jquery.qrcode.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=contextPath%>/JS/qrcode/qrcode.js" charset="utf-8"></script>
<%-- 引入一维码 --%>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/barcode/barcode.css" charset="utf-8">
<script type="text/javascript" src="<%=contextPath%>/JS/barcode/barcode.js" charset="utf-8"></script>