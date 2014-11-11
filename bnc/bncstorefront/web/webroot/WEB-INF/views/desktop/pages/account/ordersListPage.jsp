<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<c:url value="/orderslist/vieworders" var="viewOrdersURL"></c:url>
<meta http-equiv="Refresh" content="30;url=${viewOrdersURL}">
<style>
.column
{
	float: left;
	width:200px;
	text-align: center;
}
</style>
<template:page pageTitle="${pageTitle}">

	<div id="breadcrumb" class="breadcrumb"></div>

	<div id="globalMessages"></div>
	<div class="span-24">
		<input type="button" value="Refresh" onclick="javascript:document.location.reload();"/>
		<br/>
		<br/>
		<h3><b>In-Store Orders</b></h3>
		<hr>
			<div>
				<div class="column"><h5>UCOID</h5></div>
				<div class="column"><h5>Customer ID</h5></div>
				<div class="column"><h5>Order ID</h5></div>
				<div class="column"><h5>Status</h5></div>
			</div>
			<hr>
			<c:forEach items="${collectOrdersDataList}" var="CollectOrderData">
				<c:url value="/orderslist/order/${CollectOrderData.orderId}" var="orderDetailsUrl"/>
				<div>
					<div class="column">
						<a href="${orderDetailsUrl}">${CollectOrderData.ucoid}</a>
					</div>
					<div class="column">${CollectOrderData.customerId}</div>
					<div class="column">${CollectOrderData.orderId}</div>
					<div class="column">${CollectOrderData.status}</div>
				</div>
				<br/>
			</c:forEach>
		<br/>
		<hr>
		<br/>
		<input type="button" value="Refresh" onclick="javascript:document.location.reload();"/>
	</div>
	
</template:page>