<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>导入人员</title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<script>
var uploader,state;
$(function() {
	var $ = jQuery,
    $list = $('#thelist'),
    state = 'pending';
	
	uploader = WebUploader.create({
	    // swf文件路径
	    swf: ext.contextPath+'/JS/webuploader-0.1.5/Uploader.swf',
	    // 文件接收服务端。
	    server: ext.contextPath+'/material/materialinfo/importMaterialInfo.do',
	    // 选择文件的按钮。可选。
	    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	    pick: {
	    	id: '#picker',
	    	multiple: false
	    },
	    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
	    resize: false,
	    accept: {
	    	title: 'Excel',
	        extensions: 'xls,xlsx',
	        mimeTypes: 'excel/*'
	    }
	});
	//当有文件被添加进队列的时候
	uploader.on( 'fileQueued', function( file ) {
	    $list.append( '<div id="' + file.id + '" class="item">' +
	        '<h4 class="info">' + file.name + '</h4>' +
	        '<p class="state">等待上传...</p>' +
	    '</div>' );
	});
});

var doimport = function(dialog, grid){
	//执行上传功能
	if ( state === 'uploading' ) {
		alert("文件正在上传中，请稍等");
    } else {
    	uploader.upload();
    }

	uploader.on( 'uploadError', function( file ) {
	    $( '#'+file.id ).find('p.state').text('上传出错');
	});
	
	uploader.on( 'uploadSuccess', function( file,response ) {
	    $( '#'+file.id ).find('p.state').text('已上传');
	    if(response.feedback!=null&&response.feedback!=""){
	    	alert(response.feedback);
	    	grid.datagrid('reload');
			dialog.dialog('destroy');
	    }else{
	    	alert("导入未完成！");
	    	dialog.dialog('destroy');
	    }
	});
	
}
</script>
</head>
<body>
<div id="uploader" class="wu-example">    
    <div class="btns">
        <div id="picker">选择文件</div>
    </div>
    <!--用来存放文件信息-->
    <div id="thelist" class="uploader-list"></div>
</div>
</body> 
</html>