<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<title></title>
<jsp:include page="../inc.jsp"></jsp:include>
<style>
	body{ text-align:center}
</style>
</head>
<body>
		<form method="post" class="form">
			<table class="table">
				<tr>
					<th>日计划名称</th>
					<td>${materialRecover.dailyPlanSummary==null?"":materialRecover.dailyPlanSummary.name }</td>
					<th>计划日期</th>
					<td>${materialRecover.dailyPlanSummary==null?"":fn:substring(materialRecover.dailyPlanSummary.plandt,0,10)}</td>
				</tr>
				<tr>
					<th>物料名称</th>
					<td>${materialRecover.materialInfo==null?"":materialRecover.materialInfo.materialname }</td>
					<th>物料编码</th>
					<td>${materialRecover.materialInfo==null?"":materialRecover.materialInfo.materialcode }</td>
				</tr>
				<tr>
					<th>所属工位</th>
					<td>${materialRecover.workstation==null?"":materialRecover.workstation.name }</td>
					<th>工位编码</th>
					<td>${materialRecover.workstation==null?"":materialRecover.workstation.serial }</td>
				</tr>
				<tr>
					<th>回收人</th>
					<td>${materialRecover.recover==null?"":materialRecover.recover.name }</td>
					<th>回收时间</th>
					<td>${materialRecover.recovertime==null?"":fn:substring(materialRecover.recovertime,0,16)}</td>
				</tr>
				<tr>
					<th>回收量</th>
					<td>${materialRecover.recovernum} ${materialRecover.materialInfo==null?"":materialRecover.materialInfo.unit }</td>
					<th>备注</th>
					<td>${materialRecover.memo }</td>
				</tr>				
			</table>
		</form>
</body>
</html>
