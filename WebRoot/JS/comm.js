/**
 * 去字符串空格
 */
function trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, '');
};
function ltrim(str) {
	return str.replace(/(^\s*)/g, '');
};
function rtrim(str) {
	return str.replace(/(\s*$)/g, '');
};

/**
 * 判断开始字符是否是XX
 */
function startWith(source, str) {
	var reg = new RegExp("^" + str);
	return reg.test(source);
};
/**
 * 判断结束字符是否是XX
 */
function endWith(source, str) {
	var reg = new RegExp(str + "$");
	return reg.test(source);
};

/**
 * iframe自适应高度
 * 
 * @param iframe
 */
function autoIframeHeight(iframe) {
	iframe.style.height = iframe.contentWindow.document.body.scrollHeight + "px";
};

/**
 * 设置iframe高度
 * 
 * @param iframe
 */
function setIframeHeight(iframe, height) {
	iframe.height = height;
};

var commfile = function(mappernamespace,id){
		this.showUpload = function () {		
			var dialog = top.ext.modalDialog({
				title : "上传文件",
				url : ext.contextPath + "/base/fileupload.do?mappernamespace="+mappernamespace+"&masterid="+id,
				width:600,
				height:400,
				onClose : function() {
					new commfile(mappernamespace,id).loadfile();
				}
			});
		};
		
		this.loadfile = function(){
			$.post(ext.contextPath + "/base/getFileList.do?mappernamespace="+mappernamespace+"&masterid="+id,function(data){
				var filehtml="";
				$.each(data,function(index,item){
					filehtml+="<div id='"+item.id+"'><img src='"+ext.contextPath + "/CSS/ext_icons/attach.png'/> " +
						"<a class='linkbutton' href='"+ext.contextPath + "/base/downloadfile.do?mappernamespace="+mappernamespace+"&id="+item.id+"'>"+item.filename+"</a> "+
						"<a class='linkbutton' style='color:red' href='javascript:new commfile(\""+mappernamespace+"\",\""+id+"\").deletefile(\""+item.id+"\")'>删除</a></div>";
				});
				$('#fileList').html(filehtml);
			},'json');
		};
		
		this.deletefile = function(itemid){
			top.$.messager.confirm('提示', '确定删除此文件？', function(r) {
				if (r) {
					$.post(ext.contextPath + "/base/deletefile.do?mappernamespace="+mappernamespace+"&id="+itemid,function(data){
						top.$.messager.alert("提示",data,"info",function(){
							if(data.indexOf("成功")>0){
								$("#"+itemid).remove();
							}
						});
					});
				}
			});
		};
		
};


/**
 * 选择人员
 * @param recvname 用户名称字段名
 * @param recvid 用户名称id字段名
 * @param iframeId 当前窗口Id，由父窗口传递过来
 */
function selectUsers(recvname,recvid,iframeId){
	var dialog = parent.ext.modalDialog({
			title : '选择人员',
			width: 600,
			height:480,
			closeOnEscape:true,
			url : ext.contextPath + '/user/userForSelect.do?iframeId='+iframeId+'&recvid='+recvid,				  
			buttons : [
			{
				text : '选中',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.addToSelectMulti();
				}
			},{
				text : '全选',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.addToSelectAll();
				}
			},{
				text : '清除',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.removeFromSelectAll();
				}
			},{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectOK();
					if(res!=null){
						dialog.dialog('destroy');						
						var recvuser=res.split(";");										
						var recvidstr="";
						var recvnamestr="";
						for(var i=0;i<(recvuser.length-1);i++){//leng-1去空格
							recvidstr+=recvuser[i].split(",")[0]+",";
							recvnamestr+=recvuser[i].split(",")[1]+",";
						}
						if(recvidstr.length>1){
							recvidstr = recvidstr.substring(0, recvidstr.length-1);
						}
						if(recvnamestr.length>1){
							recvnamestr = recvnamestr.substring(0, recvnamestr.length-1);
						}
						$("#"+recvname).textbox('setValue',recvnamestr);//easyui textbox赋值jquery不一样
						$("#"+recvid).val(recvidstr); 	
					 
					}
					
				}
			},{
				text : '取消',
				iconCls:'icon-cancel',
				handler : function() {
					dialog.dialog('destroy');
									
				}
			}]
		});
}
/**
 * 选择人员--单选
 * @param recvname 用户名称字段名
 * @param recvid 用户名称id字段名
 * @param iframeId 当前窗口Id，由父窗口传递过来
 */
function selectSingleUser(recvname,recvid,iframeId){
	var dialog = parent.ext.modalDialog({
			title : '选择人员',
			width: 440,
			height:480,
			closeOnEscape:true,
			url : ext.contextPath + '/user/userForSingleSelect.do?iframeId='+iframeId+'&recvid='+recvid,				  
			buttons : [
			{
				text : '确定',
				iconCls:'icon-ok',
				handler : function() {
					var res=dialog.find('iframe').get(0).contentWindow.selectOK();					
					if(res!=null){
						dialog.dialog('destroy');						
						var recvuser=res.split(",");
						$("#"+recvname).textbox('setValue',recvuser[1]);//easyui textbox赋值jquery不一样
						$("#"+recvid).val(recvuser[0]); 	
					 
					}
					
				}
			},{
				text : '取消',
				iconCls:'icon-cancel',
				handler : function() {
					dialog.dialog('destroy');
									
				}
			}]
		});
}
Date.prototype.Format = function(fmt){ 
	var o = {   
			"M+" : this.getMonth()+1,                 //月份   
			"d+" : this.getDate(),                    //日   
			"h+" : this.getHours(),                   //小时   
			"m+" : this.getMinutes(),                 //分   
			"s+" : this.getSeconds(),                 //秒   
			"q+" : Math.floor((this.getMonth()+3)/3), //季度   
			"S"  : this.getMilliseconds()             //毫秒   
	};   
	if(/(y+)/.test(fmt))   
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	for(var k in o)   
		if(new RegExp("("+ k +")").test(fmt))   
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
	return fmt;   
}

/**
 * @param scope 范围 today yesterday week prevweek month prevmonth
 * @param datestr 如果为null，则直接取当前时间
 * @return result (Array)
 */
function getDateStartEnd(scope,datestr){
	datestr = datestr==null?new Date():datestr;
	
	var nowdate = new Date(datestr);
	//今天时间
    var year = nowdate.getFullYear();
    var month = nowdate.getMonth()+1;
    var day = nowdate.getDate();
    var today = nowdate.Format("yyyy-MM-dd");

    
    //昨天时间
    var dd = new Date(nowdate);
    dd.setDate(dd.getDate()-1);
    var yesterday = dd.Format("yyyy-MM-dd");

    
    // 获得本周时间 (周一到当天的时间)
    var thisWeekStart; //本周周一的时间
    if(nowdate.getDay()==0){  //周天的情况
    	thisWeekStart = (new Date(today)).getTime()-((nowdate.getDay())+6) * 86400000;
    }else{
        thisWeekStart = (new Date(today)).getTime()-((nowdate.getDay())-1) * 86400000;
    }
    var weekStartDate=new Date(thisWeekStart).Format("yyyy-MM-dd");
    var weekEndDate=nowdate.Format("yyyy-MM-dd");


    //获得上周时间
    var prevWeekStart = thisWeekStart - 7 * 86400000;//上周周一的时间
    var prevWeekEnd = thisWeekStart - 1 * 86400000;//上周周日的时间
    var prevweekStartDate=new Date(prevWeekStart).Format("yyyy-MM-dd");
    var prevweekEndDate=new Date(prevWeekEnd).Format("yyyy-MM-dd");  
         
         
    //获得本月时间
    var currentYear=nowdate.getFullYear();
    var currentMonth=nowdate.getMonth();     
    var monthStartDate= new Date(currentYear,currentMonth,1).Format("yyyy-MM-dd");    
    var monthEndDate=weekEndDate;

         
    //获得上月时间
    var currentYear=nowdate.getFullYear();
    var currentMonth=nowdate.getMonth();
    var prevCurrentYear=0,prevCurrentMonth=0;
    if(currentMonth==1){
        prevCurrentYear=currentYear-1;
        prevCurrentMonth=12;
    }else{
        prevCurrentYear=currentYear;
        prevCurrentMonth=currentMonth-1;
    }
    var prevmonthLastday=(new Date(currentYear,currentMonth,1)).getTime()-86400000;
    var prevmonthStartDate= new Date(prevCurrentYear,prevCurrentMonth,1).Format("yyyy-MM-dd");  
    var prevmonthEndDate=new Date(prevmonthLastday).Format("yyyy-MM-dd");
    
    
    var result = new Array();
    if(scope=="today"){
    	result[0] = today+" 00:00:00";
    	result[1] = today+" 23:59:59";
    }else if(scope=="yesterday"){
    	result[0] = yesterday+" 00:00:00";
    	result[1] = yesterday+" 23:59:59";
    }else if(scope=="week"){
    	result[0] = weekStartDate+" 00:00:00";
    	result[1] = weekEndDate+" 23:59:59";
    }else if(scope=="prevweek"){
    	result[0] = prevweekStartDate+" 00:00:00";
    	result[1] = prevweekEndDate+" 23:59:59";
    }else if(scope=="month"){
    	result[0] = monthStartDate+" 00:00:00";
    	result[1] = monthEndDate+" 23:59:59";
    }else if(scope=="prevmonth"){
    	result[0] = prevmonthStartDate+" 00:00:00";
    	result[1] = prevmonthEndDate+" 23:59:59";
    }
    
    return result;
} 
/**
 * @param tips 提示内容
 * @param time 秒
 */
function showTips( tips, time ){
	var windowWidth = document.documentElement.clientWidth; 
	var height = $(document).height(); 
	var loadingDiv = "<div id='loadingDiv' style='position:absolute;top:0;left:0;width:100%;height:"+height+"px;background:#ccc;opacity:0.4;display:none'></div>"; 
	var tipsDiv = '<div class="tipsClass">' + tips + '</div>'; 
	$( 'body' ).append( loadingDiv+tipsDiv);
	$( '#loadingDiv' ).show();
	$( 'div.tipsClass' ).css({ 
	'top' :  height  / 2+ 'px', 
	'left' : ( windowWidth / 2 ) - ( tips.length * 13 / 2 ) + 'px', 
	'position' : 'absolute', 
	'padding' : '10px', 
	'background': '#fff', 
	'font-size' : 12 + 'px', 
	'margin' : '0 auto', 
	'text-align': 'center', 
	'width' : 'auto', 
	'border':'2px solid #95B8E7'
	}).show(); 
	
	setTimeout( function(){
		$('#loadingDiv').fadeOut();
		$('div.tipsClass').fadeOut();
	}, ( time * 1000 ) ); 
} 
