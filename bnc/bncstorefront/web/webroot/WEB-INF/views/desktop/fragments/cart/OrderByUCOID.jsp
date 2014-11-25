<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<json:object>
	<json:property name="searchby_ucoid" escapeXml="false">
		
		<c:forEach items="${collectOrderDataByUcoid}" var="CollectOrderData" varStatus="counter">
			<c:url value="/orderslist/order/${CollectOrderData.orderId}" var="orderDetailsUrl"/>
			<c:set var="currentClass" value=''/>
			<c:if test="${counter.count==1}">
				<c:set var="currentClass" value='class="current"'/>
			</c:if>
			<li>
				<a onclick="javascript:OrderDetailsByOrderID('${CollectOrderData.orderId}');" ${currentClass}>
					${CollectOrderData.orderId}<br />
					<span>
						<fmt:formatDate type="time" value="${CollectOrderData.createdTS}" pattern="MM/dd/yyyy hh:mm aa"/><br />
					</span>
				</a>
			</li>
		</c:forEach>
	</json:property>
</json:object>
