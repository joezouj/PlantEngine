<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">
	returnstr="";
    function doSearch(orgids,type){        
        document.getElementById("prompt").style.visibility="hidden";  
        $.getJSON(ext.contextPath +"/user/queryUsers.do?orgid="+orgids+"&type="+type+"",callback);  
    }   
      
    function callback(data){  
        $("#a_Users").empty(); 
        if(data.rows.length > 0)  
        {     
            for(var i = 0;i<data.rows.length;i++){  
                var userid = data.rows[i].id;  
                var username = data.rows[i].caption;  
                  
                $("#a_Users").append("<option value='"+userid+"'>"+username+"</option>");   
            }  
        }else{  
            document.getElementById("prompt").style.visibility="visible";//显示  
        }  
    }  
      
  
    function addToSelect(){
    	var selectid=$("#a_Users  option:selected").val();
        var selecttext=$("#a_Users  option:selected").text();
		returnstr=selectid+","+selecttext;	
		
    }  
    
    function getUser(){
        document.getElementById("prompt").style.visibility="hidden";  
        var username = $('#queryusername').val();
        $.getJSON(ext.contextPath +"/user/queryUsers.do?queryusername="+encodeURI(encodeURI(username)), callback );
    }  
      
    function selectOK() {
    	addToSelect();
    	return returnstr;      
    }
    
    $(function() {
		$('#unitTree').tree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			onClick : function(node) {
				if(node.pid != ''){						
						doSearch(node.id,node.attributes.type);
				}
			}
		});	
		var recvid=parent.$('#'+'${param.iframeId}')[0].contentWindow.$("#"+'${param.recvid}').val();
		$.getJSON(ext.contextPath +"/user/getRecvusersJson.do?recvid="+recvid, function(data){
			 $("#a_selectUsers").empty();
			 for(var i = 0;i<data.rows.length;i++){  
                var userid = data.rows[i].id;  
                var username = data.rows[i].caption;
                $("#a_selectUsers").append("<option value='"+userid+"'>"+username+"</option>");   
            }  
		});

	});
</script>
</head>
<body style="margin:0px">
	<input id="recvid" name="recvid" type="hidden"	/>
	<div style="background-color:#f4f4f4;padding-top:2px;padding-bottom:2px;border-bottom:1px solid #e4e4e4;font-size:12px;">
		<span style="padding:2px 5px 2px 5px">姓名</span>
		<input id="queryusername" name="queryusername" class="easyui-searchbox" style="width: 180px;" 
			data-options="prompt:'请输入关键字',searcher:getUser" />
		<span id="prompt" name="prompt" style="color:red;visibility:hidden;padding:2px 5px 2px 5px">无用户</span>
	</div>
	<table cellspacing="2" cellpadding="0" style="font-size:12px;">
		<tr>
			<td valign="top" style="border:1px solid #e4e4e4">
				<div style="width: 250px;height: 370px;overflow:auto">
					<ul id="unitTree" class="easyui-tree" data-options="method:'get',animate:true"></ul>
				</div>
			</td>
			<td valign="top" style="border:1px solid #e4e4e4">
				<div style="background-color:#f4f4f4;padding:2px 5px 2px 5px;border-bottom:1px solid #e4e4e4">
					人员 (单击选择)
				</div>
				<select name="a_Users" id="a_Users"  onclick="addToSelect()" 
					style="width:160px; height:350px;border:0px;padding:2px;" multiple="multiple"></select>
			</td>
		</tr>
	</table>
</body>
</html>