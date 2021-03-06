<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
  <head>
  <title></title>
  <jsp:include page="../inc.jsp"></jsp:include>
  <script type="text/javascript">
  $(function(){ 
  <c:forEach items="${listuserrecv}" var="obj">
  	 $("#selectUsers").append("<option value='"+'${obj.id}'+"'>"+'${obj.caption}'+"</option>");  	 
  </c:forEach>
  <c:forEach items="${viewlist}" var="viewlistobj">
  	var longstr='<div style="float:right;width:400px;"><br><table class="table" ><tr><td  height="50px">'+'${viewlistobj.content}'+'</td>'
				+'<th>'+'${suser.caption}'+'<br>'+'${viewlistobj.sdt.substring(0,19)}'+'</th></tr></table></div>';
	$("#screen").append(longstr);
	$("#parentid").val('${viewlistobj.id}');	
  </c:forEach>  
  });
  function sendnew(){  
  	var content = $("#content").val();
  	var parentid = $("#parentid").val();  	
  	if(content==null ||content==""){
  		alert("短信内容不能为空");
  		return;
  	}
  	if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/msg/sendnewSms.do", {recvid:"${recvid}",typeid:"${msg.typeid}",content:content,parentid:parentid} , function(data) {
				if (data.res== 1) {
						top.$.messager.alert('提示', "发送成功", 'info');	
						//刷新页面								
 								$("#parentid").val(data.parentid);															
								var longstr='<div style="float:right;width:400px;"><br><table class="table" ><tr><td  height="50px">'+data.rows[0].content+'</td>'
								+'<th>'+data.susername+'<br>'+data.rows[0].sdt.substring(0,19)+'</th></tr></table></div>';
								$("#screen").append(longstr);			
					
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
  }
  </script>
  </head> 
  <body>
  <input type="hidden" id="recvid" name="recvid" value="${recvid}"/>
  <input type="hidden" id="typeid" name="typeid" value="${msg.typeid}"/>
  <input type="hidden" id="parentid" name="parentid" value="${msg.id}"/>
	<div class="easyui-layout" style="width:750px;height:450px;">
		<!-- <div data-options="region:'north'" style="height:50px"></div>-->
		<!-- <div data-options="region:'west',split:true" title="West"	style="width:100px;"></div> -->
		<div data-options="region:'east',split:true,iconCls:'icon-man'" title="收信人" style="width:20%;padding:0;">
			<select name="selectUsers" id="selectUsers"	ondblclick="" style="width:100%; height:100%;margin:-2px" multiple="multiple"></select>
		</div>
		<div data-options="region:'center',title:'内容',iconCls:'icon-ok'">
			<div class="easyui-layout" fit="true"> 
				<div region="center" border="false" id="screen"> 
								
				</div>
				<div region="south" split="true" border="false" style="height:20%;" >
                	<input class="easyui-textbox" data-options="buttonText:'发送',prompt:'输入短信内容',multiline:true,onClickButton:sendnew" style="width:100%;height:100%;" id="content" name="content">       
                </div>
			</div>
		</div>
	</div>

</body>
</html>
