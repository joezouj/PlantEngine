<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%String contextPath = request.getContextPath();%>
<!DOCTYPE html>
<html>
<head>
<title>文件选择</title>
<jsp:include page="/jsp/inc.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/webuploader-0.1.5/CSS/bootstrap-theme.min.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/webuploader-0.1.5/CSS/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/webuploader-0.1.5/CSS/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/webuploader-0.1.5/CSS/syntax.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/JS/webuploader-0.1.5/CSS/styleWebuploader.css">
<style type="text/css">

html, body {
	height: 100%;
}
.wu-example {
	width: 100%;
    position: relative;
    padding: 45px 15px 15px;
    margin: 0;
    background-color: white;
    box-shadow: inset 0 3px 6px rgba(0, 0, 0, .05);
    border-color: #e5e5e5 #eee #eee;
    border-style: solid;
    border-width: 1px 0;
}

.uploader-list {
    width: 100%;
    height: 260px;
    overflow: auto;
}

.btns {
	position:relative;
	top:5px;
	text-align:center;
}
</style>

<script>
var $list ;
var uploader;
$(function() {
	var $ = jQuery,
    $btn = $('#ctlBtn'),
    state = 'pending';
	
	$list = $('#thelist');
	uploader = WebUploader.create({
	    // swf文件路径
	    swf: ext.contextPath+'/JS/webuploader-0.1.5/Uploader.swf',
	    // 文件接收服务端。
	    server: ext.contextPath+'/base/uploadfile.do?mappernamespace=${mappernamespace}&masterid=${masterid}',
	    // 选择文件的按钮。可选。
	    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	    pick: '#picker',
	    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
	    resize: false
	});
	
	//当有文件被添加进队列的时候
	uploader.on( 'fileQueued', function( file ) {
	    $list.append( '<div id="' + file.id + '" class="item">' +
	        '<h4 class="info">' + file.name + '</h4>' +
	        '<p class="state">等待上传...</p>' +
	    '</div>' );
	});
	//文件上传过程中创建进度条实时显示。
	uploader.on( 'uploadProgress', function( file, percentage ) {
	    var $li = $( '#'+file.id ),
	        $percent = $li.find('.progress .progress-bar');
	
	    // 避免重复创建
	    if ( !$percent.length ) {
	        $percent = $('<div class="progress progress-striped active">' +
	          '<div class="progress-bar" role="progressbar" style="width: 0%">' +
	          '</div>' +
	        '</div>').appendTo( $li ).find('.progress-bar');
	    }
	
	    $li.find('p.state').text('上传中');
	
	    $percent.css( 'width', percentage * 100 + '%' );
	});
	uploader.on( 'uploadSuccess', function( file, response) {
		if (response.feedback.indexOf("成功") >= 0){
	    	$( '#'+file.id ).find('p.state').text('已上传');
 		} else{
 			$( '#'+file.id ).find('p.state').text(response.feedback);
 		}
	});
	
	uploader.on( 'uploadError', function( file ) {
	    $( '#'+file.id ).find('p.state').text('上传出错');
	});
	
	uploader.on( 'uploadComplete', function( file ) {
	    $( '#'+file.id ).find('.progress').remove();
	});
	
	uploader.on( 'all', function( type ) {
	    if ( type === 'startUpload' ) {
	        state = 'uploading';
	    } else if ( type === 'stopUpload' ) {
	        state = 'paused';
	    } else if ( type === 'uploadFinished' ) {
	        state = 'done';
	    }
	
	    if ( state === 'uploading' ) {
	        $btn.text('暂停上传');
	    } else {
	        $btn.text('开始上传');
	    }
	});
	
	$btn.on( 'click', function() {
	    if ( state === 'uploading' ) {
	        uploader.stop();
	    } else {
	        uploader.upload();
	    }
	});
});
</script>
</head>
<body>
<div id="uploader" class="wu-example">
 	<!--用来存放文件信息-->
    <div id="thelist" class="uploader-list"></div>
    
</div>
<div class="btns" >
	<div id="picker">选择文件</div>
	<button id="ctlBtn" class="btn btn-default">开始上传</button>
</div>       
</body> 
</html>