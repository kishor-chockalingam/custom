<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/mobile/nav/breadcrumb"%>

<template:page pageTitle="${pageTitle}">

	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
	</div>
	<div id="globalMessages">
		<common:globalMessages />
	</div>
	<nav:accountNav selected="customerpickuporders" />
	<div class="column accountContentPane clearfix orderList">
		<table class="orderListTable">
			<thead>
				<tr>
					<th id="header1"><spring:theme code="" text="Order ID" /></th>
					<th id="header2"><spring:theme code="" text="UCOID" /></th>
					<th id="header3"><spring:theme code="" text="Status" /></th>
					<th id="header4"><spring:theme code="" text="Comments" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${collectOrdersDataList}" var="CollectOrderData">
					<c:url value="/my-account/order/${CollectOrderData.orderId}"
						var="orderDetailsUrl" />

					<tr>
						<td headers="header1"><ycommerce:testId
								code="orderHistory_orderNumber_link">
								<a href="${orderDetailsUrl}">${CollectOrderData.orderId}</a>
							</ycommerce:testId></td>
						<td headers="header2"><ycommerce:testId
								code="orderHistory_orderStatus_label">
					${CollectOrderData.ucoid}
					</ycommerce:testId></td>
						<td headers="header3"><ycommerce:testId
								code="orderHistory_Total_links">
					${CollectOrderData.status}
					</ycommerce:testId></td>
						<td headers="header4">
						<c:choose>
								<c:when test="${CollectOrderData.status eq 'COMPLETED'}">
									<ycommerce:testId code="orderHistory_orderDate_label">
					<spring:theme code="text.customer.collect.order.completed.status" text=""/>
					</ycommerce:testId>
								</c:when>
								<c:otherwise>
								<spring:theme code="text.customer.collect.order.pending.status" text=""/>									
								</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>



</template:page>

