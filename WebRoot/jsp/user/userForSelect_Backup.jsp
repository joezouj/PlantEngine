<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
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
       // if(data.resultcode=='SUCCESS'){  
            if(data.rows.length > 0)  
            {     
                for(var i = 0;i<data.rows.length;i++){  
                    //alert(data.object[i].usercode);  
                    var userid = data.rows[i].id;  
                    //var username = data.object[i].username+"("+data.object[i].usercode+")";  
                    var username = data.rows[i].name;  
                      
                    $("#a_Users").append("<option value='"+userid+"'>"+username+"</option>");   
                }  
            }else{  
                document.getElementById("prompt").style.visibility="visible";//显示  
            }  
              
        //}else{  
        //    document.getElementById("prompt").style.visibility="visible";//显示  
        //}  
    }  
      
  
    function addToSelect(){    
      
      //  getAllselect('a_Users');    
          
    	//removeFromSelect();  
        $("#a_selectUsers").append("<option value='"+$("#a_Users  option:selected").val()+"'>"+$("#a_Users  option:selected").text()+"</option>");   
    }    
      
      
    function removeFromSelect(){  
    var selOpt = $("#a_selectUsers option:selected");    
    selOpt.remove();    
    }  
      
      
    function removeFromSelectAll(){  
           $("#a_selectUsers").empty();  
    }  
      
      
    function addToSelectAll(){   
        //$("#a_selectUsers").empty();  
        for(var i=0;i<$("#a_Users option").length;i++){  
            $("#a_selectUsers").append("<option value='"+$("#a_Users").get(0).options[i].value+"'>"+$("#a_Users").get(0).options[i].text+"</option>");   
         }      
    }  
    function addToSelectMulti(){
    	for(var i=0;i<$("#a_Users option").length;i++){  
    		if($("#a_Users").get(0).options[i].selected){
    			$("#a_selectUsers").append("<option value='"+$("#a_Users").get(0).options[i].value+"'>"+$("#a_Users").get(0).options[i].text+"</option>");
    		} 
    	}
    }       
    function getUser(){
        document.getElementById("prompt").style.visibility="hidden";  
        var username = $('#queryusername').val();
        $.getJSON(ext.contextPath +"/user/queryUsers.do?queryusername="+encodeURI(encodeURI(username)), callback );
    }  
      
    function selectOK() {  
        var username="",userid="";  
        for(var i=0;i<$("#a_selectUsers option").length;i++){  
            userid+=$("#a_selectUsers").get(0).options[i].value+","; 
            username+=$("#a_selectUsers").get(0).options[i].text+",";  
         }
        var retn= userid+")"+username;     
        //alert(retn);
        return retn;
        }
    /* function selectCancel() {
    	alert('提示', "取消操作", 'info');
    	
    }  */     
    $(function() {
		$('#unitTree').tree({
			url : ext.contextPath + '/user/getUnitsJson.do?random=' + Math.random(),
			parentField : 'pid',
			onClick : function(node) {
				if(node.pid != ''){
// 					if(node.attributes.type=='D'){
						doSearch(node.id,node.attributes.type);
// 					}else{
// 						doSearch(node.id);
// 					}
				}
			}
		});
		$.each(eval('${json}'), function(index, item){
				$("#a_selectUsers").append("<option value='"+item.id+"'>"+item.caption+"</option>");
		});
	});
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="divadd" style="display:block;">
		<div>
			<div >
				<table width="100%" class="easyui-datagrid" toolbar="#tb" border="0">					
				</table>
				<div id="tb" >
					<span style="font-size:12px">&nbsp;&nbsp;姓名查询</span>
					<input	class="easyui-searchbox" type="text" style=" width: 180px;" data-options="prompt:'请输入关键字',searcher:getUser" id="queryusername"
						name="queryusername" /> 
<!-- 					<a class="easyui-linkbutton" data-options="iconCls:'icon-search'" href="#" onclick="getUser();" -->
<!-- 						style="width:80px;height:24px">查询</a>  -->
					<span id="prompt" name="prompt" style="color: red;visibility:hidden">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;无用户</span>
				</div>
				<table width="100%" cellspacing="0">
					<tr>
						<th width="40%">
							<div
								style="width: 100%; height: 340px;overflow: auto; overflow-x:hidden; text-align: left;font-Weight:normal">
								<div region="west" style="padding: 4px;width: 200px;" 
									border="false" >
									<%-- <fsdp:tree id="tt"
										url="${ctx}/jsp/user/unitTree"
										onclick="clickTree"></fsdp:tree> --%>
								<ul id="unitTree" class="easyui-tree" data-options="method:'get',animate:true"></ul>
								</div>
							</div>
						</th>
						<td id="usertd1" style="width:170px;"><span style="font-size:12px">人员(双击选择)： </span><br /> <select
							name="a_Users" id="a_Users" style="width:170px; height:340px;margin:-2px"
							ondblclick="addToSelect()" multiple="multiple" border="1px solid #FFF;"></select>
						</td>
						<td id="usertd2" style="width:160px;"><span style="font-size:12px">已选(双击移除)： </span><br/>
							<select name="a_selectUsers" id="a_selectUsers"
							ondblclick="removeFromSelect();"
							style="width:160px; height:340px;margin:-2px" multiple="multiple" border="1px solid #FFF;"></select>
						</td>
					</tr>
					<!-- <tr style="height: 10px;"></tr> -->
					<!-- <tr>
						<td class="nobg" colspan="3" align="center"
							style="text-align:center;">
							<a class="easyui-linkbutton"	data-options="iconCls:'icon-ok'" href="javascript:void(0)"
							onclick="addToSelect();" style="width:80px">添加</a>
							 <a	class="easyui-linkbutton" data-options="iconCls:'icon-no'"
							href="javascript:void(0)" onclick="removeFromSelect();"
							style="width:80px">移出</a> 
							 <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="addToSelectAll();"style="width:80px">全部添加</a>  
                           <a class="easyui-linkbutton" data-options="iconCls:'icon-reload'" href="javascript:void(0)"  onclick="removeFromSelectAll();" style="width:80px">全部移出</a>
							<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'"
							href="javascript:void(0)" onclick="selectOK();"
							style="width:80px">确定</a>
						</td>
					</tr> -->
				</table>
			</div>
		</div>
	</div>
</body>
</html>