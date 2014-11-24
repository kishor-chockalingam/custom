<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="chart_block">
	<div class="diagram_block diagramcurrent">
		<div id="diagram-id-1" class="diagram" data-percent="${Queued}"></div>
		<div class="digram_txt">Queued</div>
	</div>
	<div class="diagram_block">
		<div id="diagram-id-2" class="diagram" data-percent="${Active}"></div>
		<div class="digram_txt">Active</div>
	</div>
	<div class="diagram_block">
		<div id="diagram-id-3" class="diagram" data-percent="${Serviced}"></div>
		<div class="digram_txt">Serviced</div>
	</div>
	<div class="diagram_block">
		<c:set var="Total" value="${fn:length(collectOrdersDataList)}"/>
		<fmt:formatNumber var="target" value="${(Serviced/Total)*100}" maxFractionDigits="0"/>
		<div id="diagram-id-4" class="diagram" data-percent="${target}%"></div>
		<div class="digram_txt">Target</div>
	</div>
</div>