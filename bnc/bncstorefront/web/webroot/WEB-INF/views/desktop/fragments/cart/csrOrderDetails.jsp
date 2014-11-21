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
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<json:object>
	<json:property name="CSROrder_Details" escapeXml="false">
	<div class="orderList">
<%-- 	<c:set var="headingWasShown" value="false"/>
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
		</c:forEach> --%>

		<table class="orderListTable">
			<c:forEach items="${orderData.consignments}" var="consignment">

				<c:forEach items="${consignment.entries}" var="entry">

					<c:url value="${entry.orderEntry.product.url}" var="productUrl" />

					<tr>
						<td class="product_image"><a href="${productUrl}"> <product:productPrimaryImage
									product="${entry.orderEntry.product}" format="thumbnail" />
						</a></td>

						<td><ycommerce:testId
								code="orderDetails_productQuantity_label">${entry.quantity}</ycommerce:testId>
						</td>

						<td><ycommerce:testId
								code="orderDetails_productItemPrice_label">
								<format:price priceData="${entry.orderEntry.basePrice}"
									displayFreeForZero="true" />
							</ycommerce:testId></td>

						<td><ycommerce:testId
								code="orderDetails_productTotalPrice_label">${entry.quantity * entry.orderEntry.basePrice.value}</ycommerce:testId>
						</td>

					</tr>

				</c:forEach>
			</c:forEach>

		</table>
</div>
	</json:property>
						</json:object>
						