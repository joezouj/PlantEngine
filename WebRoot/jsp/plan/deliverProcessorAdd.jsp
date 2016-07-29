<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script type="text/javascript">

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
        var selecttext=$("#a_Users  option:selected").text()
        for (var i = 0; i < $("#a_selectUsers option").length; i++) {
				if($("#a_selectUsers").get(0).options[i].value==selectid){
					return;
				}
 		}
        $("#a_selectUsers").append("<option value='"+selectid+"'>"+selecttext+"</option>");   
    }    
      
    function removeFromSelect(){  
    	var selOpt = $("#a_selectUsers option:selected");    
    	selOpt.remove();    
    }  
      
    function removeFromSelectAll(){  
    	top.$.messager.confirm("提示","是否清除全部已选人员？",function(res){
    		if(res){
    			$("#a_selectUsers").empty();
    		}	
    	});
    }  
      
    function addToSelectAll(){   
        for(var i=0;i<$("#a_Users option").length;i++){
        	var flag=0;
        	var selectid=$("#a_Users").get(0).options[i].value;
        	var selecttext=$("#a_Users").get(0).options[i].text;
        	for (var j = 0; j < $("#a_selectUsers option").length; j++) {
				if($("#a_selectUsers").get(0).options[j].value==selectid){
					flag=1;
				}
 			} 
 			if(flag==0) {
 				$("#a_selectUsers").append("<option value='"+selectid+"'>"+selecttext+"</option>");
 			}
               
         }
    } 
    
    function addToSelectMulti(){
    	for(var i=0;i<$("#a_Users option").length;i++){  
    		if($("#a_Users").get(0).options[i].selected){
    			var flag=0;
        		var selectid=$("#a_Users").get(0).options[i].value;
        		var selecttext=$("#a_Users").get(0).options[i].text;
        		for (var j = 0; j < $("#a_selectUsers option").length; j++) {
					if($("#a_selectUsers").get(0).options[j].value==selectid){
						flag=1;
					}
 				} 
 				if(flag==0) {
 					$("#a_selectUsers").append("<option value='"+selectid+"'>"+selecttext+"</option>");
 				}
    			
    		} 
    	}
    }     
    
    function getUser(){
        document.getElementById("prompt").style.visibility="hidden";  
        var username = $('#queryusername').val();
        $.getJSON(ext.contextPath +"/user/queryUsers.do?queryusername="+encodeURI(encodeURI(username)), callback );
    }  
      
    function selectOK(dialog, grid) {  
        var userids="";  
        for(var i=0;i<$("#a_selectUsers option").length;i++){  
        	userids+=$("#a_selectUsers").get(0).options[i].value+",";
        }
        if(userids.length>1){
        	userids = userids.substring(0, userids.length-1);
        }
        $.post(ext.contextPath + "/plan/deliverprocessor/save.do",{pid:'${pid}',processorids:userids}, function(data) {
        	if (data.res != "") {
				top.$.messager.alert('提示', data.res, 'info', function() {
					grid.datagrid('reload');
					dialog.dialog('destroy');
				});
			}else{
				top.$.messager.alert('提示', "操作无效", 'info');
			}
		},'json');
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
		var recvid='${processorids}';
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
	<input id="processorids" name="processorids" type="hidden"	/>
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
					人员 (双击选择)
				</div>
				<select name="a_Users" id="a_Users" ondblclick="addToSelect()" 
					style="width:160px; height:350px;border:0px;padding:2px;" multiple="multiple"></select>
			</td>
			<td valign="top" style="border:1px solid #e4e4e4">
				<div style="background-color:#f4f4f4;padding:2px 5px 2px 5px;border-bottom:1px solid #e4e4e4">
					已选 (双击移除)
				</div>
				<select name="a_selectUsers" id="a_selectUsers" ondblclick="removeFromSelect();" 
					style="width:160px; height:350px;border:0px;padding:2px;" multiple="multiple"></select>
			</td>
		</tr>
	</table>
</body>
</html>