<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="chart_block">
	<c:choose>
		<c:when test="${param.status=='COMPLETED'}">
			<c:set var="queuedCurrent" value=""/>
			<c:set var="activeCurrent" value=" diagramcurrent"/>
			<c:set var="servicedCurrent" value=""/>
		</c:when>
		<c:when test="${param.status=='COLLECTED'}">
			<c:set var="queuedCurrent" value=""/>
			<c:set var="activeCurrent" value=""/>
			<c:set var="servicedCurrent" value=" diagramcurrent"/>
		</c:when>
		<c:otherwise>
			<c:set var="queuedCurrent" value=" diagramcurrent"/>
			<c:set var="activeCurrent" value=""/>
			<c:set var="servicedCurrent" value=""/>
		</c:otherwise>
	</c:choose>
	<div class="diagram_block${queuedCurrent}">
		<div id="diagram-id-1" class="diagram" data-percent="${Queued}"></div>
		<div class="digram_txt">
			<c:url value="/orderslist/vieworders" var="queuedURL">
				<c:param name="status" value="PENDING"></c:param>
			</c:url>
			<a href="${queuedURL}"  style="text-decoration: none;color:#6f6f6f;">Queued</a>
		</div>
	</div>
	<div class="diagram_block${activeCurrent}">
		<div id="diagram-id-2" class="diagram" data-percent="${Active}"></div>
		<div class="digram_txt">
			<c:url value="/orderslist/vieworders" var="activeURL">
				<c:param name="status" value="COMPLETED"></c:param>
			</c:url>
			<a href="${activeURL}" style="text-decoration: none;color:#6f6f6f;">Active</a>
		</div>
	</div>
	<div class="diagram_block${servicedCurrent}">
		<div id="diagram-id-3" class="diagram" data-percent="${Serviced}"></div>
		<div class="digram_txt">
			<c:url value="/orderslist/vieworders" var="servicedURL">
				<c:param name="status" value="COLLECTED"></c:param>
			</c:url>
			<a href="${servicedURL}"  style="text-decoration: none;color:#6f6f6f;">Serviced</a>
		</div>
	</div>
	<div class="diagram_block">
		<c:set var="Total" value="${fn:length(collectOrdersDataList)}"/>
		<fmt:formatNumber var="target" value="${(Serviced/Total)*100}" maxFractionDigits="0"/>
		<c:if test="${Serviced==0 or  Total==0}">
			<fmt:formatNumber var="target" value="0" maxFractionDigits="0"/>
		</c:if>
		<div id="diagram-id-4" class="diagram" data-percent="${target}%"></div>
		<div class="digram_txt">Target</div>
	</div>
</div>