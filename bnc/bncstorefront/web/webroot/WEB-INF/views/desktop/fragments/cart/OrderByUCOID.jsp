<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<json:object>
<json:property name="searchby_ucoid" escapeXml="false">

<h3><b>In-Store Orders</b></h3>
<c:url value="/orderslist/order/${collectOrderDataByUcoid.orderId}" var="orderDetailsUrl"/>

<%--   <c:set var="UCOIDOrder" value="${collectOrderDataByUcoid}" />
  <c:when test="${not empty UCOIDOrder}">  --%>
<hr>
			<div>
				<div class="column"><h5>UCOID</h5></div>
				<div class="column"><h5>Customer ID</h5></div>
				<div class="column"><h5>Order ID</h5></div>
				<div class="column"><h5>Status</h5></div>
			</div>
			
			<hr>
			
			
		
				<div>
					<div class="column">
						<a href="${orderDetailsUrl}">${collectOrderDataByUcoid.ucoid}</a>
					</div>
					<div class="column">${collectOrderDataByUcoid.customerId}</div>
					<div class="column">${collectOrderDataByUcoid.orderId}</div>
					<div class="column">${collectOrderDataByUcoid.status}</div>
				</div>
				<br/>
		<%-- 		</c:when> --%>
			</json:property>
</json:object>
