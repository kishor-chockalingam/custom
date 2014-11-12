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

 <script type="text/javascript">  
		function doAjaxPost() {
			var ucoid = document.getElementById('ucoid').value;
			if (document.getElementById('ucoid').value == '') {
				alert("Please Provide Details!");
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
					//alert('Error: ' + e);   
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
<!-- 	<div class="span-24"> -->
	<input type="button" value="Refresh" onclick="javascript:document.location.reload();"/>
	<br/>
	<br/>
	<h3><b><spring:theme code="" text="In-Store Orders" /></b></h3>
	<font color="green"><b><spring:theme code="" text="Enter UCOID : " /><input type="text" id="ucoid" /></b></font>
	
	<input type="button" name="ucoid"  value="search" onclick="javascript:doAjaxPost();"/> 
	
	<br/>
	<br/>
	<div class="span-24"  id="newdiv">
		<%-- <hr>
			<div>
				<div class="column"><h5><spring:theme code="" text="UCOID" /></h5></div>
				<div class="column"><h5><spring:theme code="" text="Customer ID" /></h5></div>
				<div class="column"><h5><spring:theme code="" text="Order ID" /></h5></div>
				<div class="column"><h5><spring:theme code="" text="Status" /></h5></div>
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
		<br/><br/>
		<hr>
		<br/> --%>
		<div class="column accountContentPane clearfix orderList">
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
					<c:forEach items="${collectOrdersDataList}" var="CollectOrderData">
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
							<td headers="header4"><ycommerce:testId code="orderHistory_orderDate_label">
							${CollectOrderData.status}
						</ycommerce:testId>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
		</div>
	</div>

</template:page>