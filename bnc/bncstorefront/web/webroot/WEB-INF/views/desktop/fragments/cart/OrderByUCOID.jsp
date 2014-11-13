<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<json:object>
	<json:property name="searchby_ucoid" escapeXml="false">
		<c:url value="/orderslist/order/${collectOrderDataByUcoid.orderId}" var="orderDetailsUrl"/>
		<hr>
		<div align="left"><a href="${contextPath}/orderslist/vieworders">Back</a></div>
				<div class="column accountContentPane clearfix orderList">
					<table class="orderListTable">
						<thead>
							<tr>
								<th id="header1"><spring:theme code="" text="UCOID" />vfdvf</th>
								<th id="header2"><spring:theme code="" text="Customer ID" /></th>
								<th id="header3"><spring:theme code="" text="Order ID" /></th>
								<th id="header4"><spring:theme code="" text="Status" /></th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${empty collectOrderDataByUcoid.ucoid}">
									<tr>
										<td headers="header1"><ycommerce:testId
												code="orderHistory_orderNumber_link">
							<font color ="red"><b>	<c:out value="No results found, Please enter the valid UCOID"></c:out></b></font>
								</ycommerce:testId>
								</td>
								</tr>
								</c:when>
								<c:otherwise>
									<c:url value="/orderslist/order/${collectOrderDataByUcoid.orderId}" var="orderDetailsUrl"/>
									<tr>
										<td headers="header1"><ycommerce:testId
												code="orderHistory_orderNumber_link">
												<a href="${orderDetailsUrl}">${collectOrderDataByUcoid.ucoid}</a>
											</ycommerce:testId></td>
										<td headers="header2"><ycommerce:testId
												code="orderHistory_orderStatus_label">
									${collectOrderDataByUcoid.customerId}
									</ycommerce:testId></td>
										<td headers="header3"><ycommerce:testId
												code="orderHistory_Total_links">
									${collectOrderDataByUcoid.orderId}
									</ycommerce:testId></td>
										<td headers="header4"><ycommerce:testId code="orderHistory_orderDate_label">
										${collectOrderDataByUcoid.status}
									</ycommerce:testId>
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
							
						</tbody>
					</table>
					
				</div>	
			
	</json:property>
</json:object>
