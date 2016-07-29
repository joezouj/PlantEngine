<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<style type="text/css">
.menuli {
	padding:2px;
}

.menuli a {
	color: #138daa;
	text-align:left;
	outline:none;
}
</style>
<div class="easyui-accordion" fit="true" border="false">
	<c:forEach items="${list}" var="cumcl1">
		<c:if test="${cumcl1.pid=='-1' }">
			<div title="${cumcl1.name}" selected="true">
				<c:forEach items="${list}" var="cumcl2">
					<c:if test="${cumcl1.id==cumcl2.pid}">
						<div style="padding:5px;background-color:#E0EEEE">
							${cumcl2.name}</div>
						<ul>
							<c:forEach items="${list}" var="cumcl3">
								<c:if test="${cumcl2.id==cumcl3.pid}">
									<li class="menuli"><a class="easyui-linkbutton"
										data-options="toggle:true,group:'menugp',plain:true,fit:true" hidefocus
										href="javascript:addTab('${cumcl3.id}','${cumcl3.name}','${cumcl3.location}');">
											${cumcl3.name} </a></li>
								</c:if>
							</c:forEach>
						</ul>
					</c:if>
				</c:forEach>
			</div>
		</c:if>
	</c:forEach>
</div>