<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	function dosave(dialog,grid) {
		if ($(".form").form('validate')) {
			$.post(ext.contextPath + "/material/clients/update.do", $(".form").serialize(), function(data) {
				if (data.res == 1) {
					top.$.messager.alert('提示', "保存成功", 'info', function() {
						grid.datagrid('reload');
						dialog.dialog('destroy');
					});
				}else if(data.res == 0){
					top.$.messager.alert('提示', "保存失败", 'info');
				}else{
					top.$.messager.alert('提示', data.res, 'info');
				}
			},'json');
		}
	}
	
	$(function() {
	});
//信息验证
  $.extend($.fn.validatebox.defaults.rules, {
  phoneRex: {//电话号码自定义验证
    validator: function(value){
    var rex=/^1[3-8]+\d{9}$/;
    //var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
    //区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}
    //电话号码：7-8位数字： \d{7,8
    //分机号：一般都是3位数字： \d{3,}
     //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/		 
    var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
    if(rex.test(value)||rex2.test(value))
    {
      // alert('t'+value);
      return true;
    }else
    {
     //alert('false '+value);
       return false;
    }
      
    },
    message: '请输入正确电话或手机格式'
  },
  name: {// 验证姓名，中文或英文，必须输入全是中文或全是英文
    validator: function (value) {
    return /^[\Α-\￥]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
    },
    message: '请输入姓名'
    },
faxno: {// 验证传真
    validator: function (value) {
     //            return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
    return /^((\d{2,3})|(\d{3}\-))?(0\d{2,3}|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
    },
    message: '传真号码不正确'
    }
});
	
</script>
</head>
<body>
		<form method="post" class="form">
		<input name="id" type="hidden" value="${clients.id}"/>
			<table class="table">
				<tr>
					<th>客户名称</th>
					<td><input name="name" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${clients.name}" />
					</td>
				</tr>
				<tr>
					<th>联系电话</th>
					<td><input name="tel" class="easyui-textbox"
						data-options="required:true,validType:'isBlank'" value="${clients.tel}"  type="text" validtype="phoneRex" invalidMessage="请输入正确的电话号码"/>
					</td>
				</tr>
				<tr>
					<th>联系传真</th>
					<td><input name="fax" class="easyui-textbox"
						data-options="required:false" value="${clients.fax}" validtype="faxno" invalidMessage="请输入正确传真号"/>
					</td>
				</tr>
				<tr>
					<th>E-mail</th>
					<td><input name="email" class="easyui-textbox"
						data-options="required:false" value="${clients.email}" validtype="email" required="true" missingMessage="不能为空" invalidMessage="请输入正确的邮箱格式"/>
					</td>
				</tr>
				<tr>
					<th>联系人</th>
					<td><input name="contactname" class="easyui-textbox"
						data-options="required:false" value="${clients.contactname}" validtype="name" invalidMessage="请输入正确名称"/>
					</td>
				</tr>
				<tr>
					<th>联系人电话</th>
					<td><input name="contacttel" class="easyui-textbox"
						data-options="required:false" value="${clients.contacttel}"  validtype="phoneRex" invalidMessage="请输入正确的电话号码"/>
					</td>
				</tr>
				<tr>
					<th>地址</th>
					<td><input name="address" class="easyui-textbox"
						data-options="required:false" value="${clients.address}" validtype="length[0,250]" invalidMessage="有效长度0-250"/>
					</td>
				</tr>
				<tr>
					<th>备注</th>
					<td><input id="remark" name="remark" class="easyui-textbox" 
						data-options="multiline:true"  validtype="length[0,250]" invalidMessage="有效长度0-250"
						style="overflow:auto;height:80px;width:100%" value="${clients.remark}" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
