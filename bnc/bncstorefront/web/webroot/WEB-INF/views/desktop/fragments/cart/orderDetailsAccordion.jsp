<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<json:object>
	<json:property name="customer_dashboard_order_details_for_accordion" escapeXml="false">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="orderitemtabel">
			<c:forEach items="${orderData.entries}" var="entry">
				<c:url value="${entry.product.url}" var="productUrl" />
				<tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="8%"><product:productPrimaryImage	product="${entry.product}" format="thumbnail" /></td>
				            <td width="2%"></td>
				            <td width="25%">${entry.product.name}</td>
				            <td width="12%">${entry.quantity}</td>
				            <td width="11%"><format:price priceData="${entry.basePrice}"	displayFreeForZero="true" /></td>
				            <td width="21%">$${entry.quantity * entry.basePrice.value}</td>
				            <td width="3%">&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="10"></td>
				</tr>
			</c:forEach>
		</table>
	</json:property>
</json:object>