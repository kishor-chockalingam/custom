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

 <script type="text/javascript">  
		function doAjaxPost() {
			var ucoid = document.getElementById('ucoid').value;
			 if (document.getElementById('ucoid').value =='' ) { 
			alert("Please enter the UCOID!");
				document.getElementById('ucoid').focus();
				return false;
			}
			//alert("Adding "+ucoid);
			$.ajax({
				type : 'GET',
				url : "${contextPath}/orderslist/ucoid",
				data : "ucoid=" + ucoid,
				dataType : 'json',
				success : function(response) {
				$("#newdiv").html(response.searchby_ucoid);
				},
				error : function(e) {
					//$("#invalidUCOID").append("Please enter the valid UCOID");
				}
			});
		}
	</script> 
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
	<input type="button" value="Refresh" onclick="javascript:document.location.reload();"/>
	<br/>
	<br/>
	<h3><b><spring:theme code="" text="In-Store Orders" /></b></h3>
	<font color="green"><b><spring:theme code="" text="Enter UCOID : " /><input type="text" id="ucoid" /></b></font>
	
	<input type="button" name="ucoid"  value="search" onclick="javascript:doAjaxPost();"/> 
	
	<br/>
	<br/>
	<div class="span-24"  id="newdiv">
	<c:if test="${not empty searchPageData.results}">
		<div class="column accountContentPane clearfix orderList">
		
		<nav:pagination top="true"  supportShowPaged="${isShowPageAllowed}"  supportShowAll="${isShowAllAllowed}"  searchPageData="${searchPageData}" searchUrl="/orderslist/vieworders?sort=${searchPageData.pagination.sort}" msgKey="text.csr.view.orders.page"  numberPagesShown="${numberPagesShown}"/>
			<table class="orderListTable">
				<thead>
					<tr>
						<th id="header1"><spring:theme code="" text="UCOID" /></th>
						<th id="header2"><spring:theme code="" text="Customer ID" /></th>
						<th id="header3"><spring:theme code="" text="Order ID" /></th>
						<th id="header4"><spring:theme code="" text="Status" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${searchPageData.results}" var="CollectOrderData">
						<c:url value="/orderslist/order/${CollectOrderData.orderId}" var="orderDetailsUrl"/>
	
						<tr>
							<td headers="header1"><ycommerce:testId
									code="orderHistory_orderNumber_link">
									<a href="${orderDetailsUrl}">${CollectOrderData.ucoid}</a>
								</ycommerce:testId></td>
							<td headers="header2"><ycommerce:testId
								code="orderHistory_orderStatus_label">
								${CollectOrderData.customerId}
								</ycommerce:testId></td>
							<td headers="header3"><ycommerce:testId
											code="orderHistory_Total_links">
								${CollectOrderData.orderId}
								</ycommerce:testId></td>
							<td headers="header4">
								<ycommerce:testId code="orderHistory_orderDate_label">
									<form action="${viewOrdersURL}" method="GET">
										<input type="hidden" name="pk" value="${CollectOrderData.pk}"/>
										<select name="status" onchange="this.form.submit();">
											<c:forEach items="${collectOrderStatusList}" var="stat">
											<c:set var="selected" value="" />
											<c:if test="${stat eq  CollectOrderData.status}">
												<c:set var="selected" value="selected='true'" />
											</c:if>
												<option value="${stat}" ${selected}>${stat}</option>
											</c:forEach>
										</select>
									</form>
								</ycommerce:testId>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
						<nav:pagination top="false" supportShowPaged="${isShowPageAllowed}"  supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="/orderslist/vieworders?sort=${searchPageData.pagination.sort}" msgKey="text.csr.view.orders.page"  numberPagesShown="${numberPagesShown}"/>
			
			
			
		</div>
		</c:if>
	</div>

</template:page>