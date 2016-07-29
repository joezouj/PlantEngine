<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8">
$(function() {
	mainTabs = $('#mainTabs').tabs({
		fit : true,
		border : false,
		cache : false,
		tools : [ {
			text : '刷新',
			iconCls : 'ext-icon-arrow_refresh',
			cache : false,
			handler : function() {
				var panel = mainTabs.tabs('getSelected').panel('panel');
				var frame = panel.find('iframe');
				try {
					if (frame.length > 0) {
						for (var i = 0; i < frame.length; i++) {
							frame[i].contentWindow.document.write('');
							frame[i].contentWindow.close();
							frame[i].src = frame[i].src;
						}
						if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
							try {
								CollectGarbage();
							} catch (e) {
							}
						}
					}
				} catch (e) {
				}
			}
		}, {
			text : '关闭',
			iconCls : 'ext-icon-cross',
			handler : function() {
				parent.$.messager.confirm("提示","是否关闭全部？",function(r){
					if(r){
						$("#mainTabs span.tabs-title").each(function(){
				    		if($(this).text()!='首页'){
								$("#mainTabs").tabs('close',$(this).text());
							}
						});
					}
				});
			}
		} ]
	});
	
	$("iframe:first").attr("src","jsp/portal.jsp");
});
</script>
<div id="mainTabs" class="easyui-tabs" fit="true" border="false">
	<div title="首页">
		<!-- margin-bottom:-4px 不设置的话会出现滚动条 -->
		<iframe src="" allowTransparency="true" style="border: 0; width: 100%; height: 100%;margin-bottom:-4px" frameBorder="0" scrolling="no"></iframe>
	</div>
</div>