<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="datetime_block">
	<c:set var="now" value="<%=new java.util.Date()%>" />
	<div class="date_block">
		<fmt:formatDate var="nowDate" value="${now}" pattern="dd.MM.yyyy"/>
		<ul>
			<li><a href="#"><img src="${commonResourcePath}/bnc_images/calender_icon.png" alt="calender" border="0"/></a></li>
			<li>From :</li>
			<li>
				<input type="text" value="${nowDate}" id="searchTimeBarFromDate" onblur="javascript:getCustomersByFromDate();">
			</li>
			<c:choose>
			
		<c:when test="${param.status=='PENDING'}"> 
		
			<li>To :</li>
			<li>
				<input type="text" disabled value="${nowDate}" id="searchTimeBarToDate" class="disable" onblur="javascript:getCustomersByFromDate();">
			</li>
			</c:when>
			
			<c:otherwise> 
			<li>To :</li>
			<li>
				<input type="text"  value="${nowDate}" id="searchTimeBarToDate"  onblur="javascript:getCustomersByFromDate();">
			</li> 
			
			</c:otherwise>
			</c:choose> 
		
		</ul>
	</div>
	<div class="time_block">
		<ul>
			<li><a href="#"><img src="${commonResourcePath}/bnc_images/clock_icon.png" alt="clock" border="0"/></a></li>
			<li>From :</li>
			<li>
				<input type="text" value="00:00 AM" id="searchTimeBarFromTime" onblur="javascript:getCustomersByFromDate();">
			</li>
			<li>To :</li>
			<li>
				<fmt:formatDate var="nowTime" value="${now}" pattern="hh:mm aa"/>
				<input type="text" value="${nowTime}" id="searchTimeBarToTime" onblur="javascript:getCustomersByFromDate();">
			</li>
		</ul>
	</div>
</div>