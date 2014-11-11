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
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
.column
{
	float: left;
	width:200px;
	text-align: center;
}
</style>
<template:page pageTitle="${pageTitle}">

	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
	</div>
	<div id="globalMessages">
		<common:globalMessages/>
	</div>
	<div class="column accountContentPane clearfix">
		<div class="headline">Order Details</div>
		<div class="span-19">
			<div class="span-7">
				<spring:theme code="text.account.order.orderNumber" arguments="${orderData.code}"/><br />
				<spring:theme code="text.account.order.orderPlaced" arguments="${orderData.created}"/><br />
				<c:if test="${not empty orderData.statusDisplay}">
					<spring:theme code="text.account.order.status.display.${orderData.statusDisplay}" var="orderStatus"/>
					<spring:theme code="text.account.order.orderStatus" arguments="${orderStatus}"/><br />
				</c:if>	
			</div>	
	
			<div class="span-5">&nbsp;
				<order:receivedPromotions order="${orderData}"/>
			</div>
			<div class="span-6 last order-totals">
				<order:orderTotalsItem order="${orderData}"/>
			</div>
		</div>
		
		<div class="orderBoxes clearfix">
			<order:deliveryAddressItem order="${orderData}"/>
			<order:deliveryMethodItem order="${orderData}"/>
			<div class="orderBox billing">
				<order:billingAddressItem order="${orderData}"/>
			</div>
			<c:if test="${not empty orderData.paymentInfo}">
				<div class="orderBox payment">
					<order:paymentDetailsItem order="${orderData}"/>
				</div>
			</c:if>
		</div>
		
		<c:if test="${not empty orderData.unconsignedEntries}" >
			<order:orderUnconsignedEntries order="${orderData}"/>				
		</c:if>
	
		<c:set var="headingWasShown" value="false"/>
		<c:forEach items="${orderData.consignments}" var="consignment">
			<c:if test="${consignment.status.code eq 'WAITING' or consignment.status.code eq 'PICKPACK' or consignment.status.code eq 'READY'}">
					<c:if test="${not headingWasShown}">
					<c:set var="headingWasShown" value="true"/>
					<h2><spring:theme code="text.account.order.title.inProgressItems"/></h2>
				</c:if>
				<div class="productItemListHolder fulfilment-states-${consignment.status.code}">
					<order:accountOrderDetailsItem order="${orderData}" consignment="${consignment}" inProgress="true"/>
				</div>
			</c:if>
		</c:forEach>	
		
		<c:forEach items="${orderData.consignments}" var="consignment">
			<c:if test="${consignment.status.code ne 'WAITING' and consignment.status.code ne 'PICKPACK' and consignment.status.code ne 'READY'}">
				<div class="productItemListHolder fulfilment-states-${consignment.status.code}">
					<order:accountOrderDetailsItem order="${orderData}" consignment="${consignment}" />
				</div>
			</c:if>
		</c:forEach>	
	</div>
	
	<div class="column accountContentPane clearfix">
		<div class="headline"><spring:theme code="text.account.profile" text="Profile"/></div>
			<table class="account-profile-data">
				<tr>
					<td><spring:theme code="profile.title" text="Title"/>: </td>
					<td>${fn:escapeXml(title.name)}</td>
				</tr>
				<tr>
					<td><spring:theme code="profile.firstName" text="First name"/>: </td>
					<td>${fn:escapeXml(customerData.firstName)}</td>
				</tr>
				<tr>
					<td><spring:theme code="profile.lastName" text="Last name"/>: </td>
					<td>${fn:escapeXml(customerData.lastName)}</td>
				</tr>
				<tr>
					<td><spring:theme code="profile.email" text="E-mail"/>: </td>
					<td>${fn:escapeXml(customerData.displayUid)}</td>
				</tr>
			</table>
			
			<a class="button" href="update-password"><spring:theme code="text.account.profile.changePassword" text="Change password"/></a>
			<a class="button" href="update-profile"><spring:theme code="text.account.profile.updatePersonalDetails" text="Update personal details"/></a>
			<a class="button" href="update-email"><spring:theme code="text.account.profile.updateEmail" text="Update email"/></a>

	</div>
</template:page>