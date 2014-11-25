<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<json:object>
	<json:property name="searchby_ucoid" escapeXml="false">
		<ul>
			<li class="search_padding">
				<input type="text" placeholder="Search " name="q" class="search-text placeholder" id="ucoid" onblur="javascript:doAjaxPost();">
			</li>
			<c:url value="/orderslist/order/${collectOrderDataByUcoid.orderId}" var="orderDetailsUrl"/>
			<li>
				<a onclick="javascript:OrderDetailsByOrderID('${collectOrderDataByUcoid.orderId}');" class="current">
					${collectOrderDataByUcoid.orderId}<br />
					<span>
						<fmt:formatDate type="time" value="${collectOrderDataByUcoid.createdTS}" pattern="MM/dd/yyyy hh:mm aa"/><br />
					</span>
				</a>
			</li>
		</ul>
	</json:property>
</json:object>
