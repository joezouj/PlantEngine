<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%String contextPath = request.getContextPath();%>
<html>
<head>
	<jsp:include page="inc.jsp"></jsp:include>
	<%-- 引入EasyUI Portal插件 --%>
	<link rel="stylesheet" href="<%=contextPath%>/JS/jquery-easyui-portal/portal.css" type="text/css"/>
	<script type="text/javascript" src="<%=contextPath%>/JS/jquery-easyui-portal/jquery.portal.js" charset="utf-8"></script>
	<script type="text/javascript">
        var panels = [
			{id:'p1',title:'项目',height:610,collapsible:true,href:''},
            {id:'p2',title:'监视',height:329,collapsible:true,href:''},
            {id:'p3',title:'消息',height:271,collapsible:true,href:''}
        ];
        function getCookie(name){
            var cookies = document.cookie.split(';');
            if (!cookies.length) return '';
            for(var i=0; i<cookies.length; i++){
                var pair = cookies[i].split('=');
                if ($.trim(pair[0]) == name){
                    return $.trim(pair[1]);
                }
            }
            return '';
        }
        function getPanelOptions(id){
            for(var i=0; i<panels.length; i++){
                if (panels[i].id == id){
                    return panels[i];
                }
            }
            return undefined;
        }
        function getPortalState(){
            var aa = [];
            for(var columnIndex=0; columnIndex<2; columnIndex++){
                var cc = [];
                var panels = $('#pp').portal('getPanels', columnIndex);
                for(var i=0; i<panels.length; i++){
                    cc.push(panels[i].attr('id'));
                }
                aa.push(cc.join(','));
            }
            return aa.join(':');
        }
        function addPanels(portalState){
            var columns = portalState.split(':');
            for(var columnIndex=0; columnIndex<columns.length; columnIndex++){
                var cc = columns[columnIndex].split(',');
                for(var j=0; j<cc.length; j++){
                    var options = getPanelOptions(cc[j]);
                    if (options){
                        var p = $('<div/>').attr('id',options.id).appendTo('body');
                        p.panel(options);
                        $('#pp').portal('add',{
                            panel:p,
                            columnIndex:columnIndex
                        });
                    }
                }
            }
            
        }
        
        $(document).ready(function(){
            $('#pp').portal({
                onStateChange:function(){
                    var state = getPortalState();
                    var date = new Date();
                    date.setTime(date.getTime() + 24*3600*1000);
                    document.cookie = 'portal-state='+state+';expires='+date.toGMTString();
                }
            });
            var state = getCookie('portal-state');
            if (!state){
                state = 'p1:p2,p3';    // the default portal state
            }
            addPanels(state);
            
            $('#pp').portal('resize');
            
        });
        
    </script>
    
    <script type="text/javascript">
		function doviewop(o){
			top.addTab("","收消息","msgaction.do?method=doview&id="+o);
		}
        function dofirstviewop(o){
			top.addTab("","收消息","msgaction.do?method=dofirstview&id="+o);
        }	        	
	</script>
</head>
<body style="overflow-x:hidden">
<div id="showdiv"></div>
    <div id="pp" style="position:relative;border:1px solid #ffffff;">
        <div style="width:70%"></div>
        <div style="width:30%"></div>
    </div>
</body>
</html>