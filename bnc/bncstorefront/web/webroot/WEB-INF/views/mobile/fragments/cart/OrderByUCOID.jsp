<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<json:object>
	<json:property name="searchby_ucoid" escapeXml="false">
		<c:url value="/orderslist/order/${collectOrderDataByUcoid.orderId}" var="orderDetailsUrl"/>
		<ul>
			<li class="search_padding">
				<input type="text" placeholder="Search " name="q" class="search-text placeholder" id="ucoid" onblur="javascript:doAjaxPost();">
			</li>
			<li>
				<a href="${orderDetailsUrl}" class="current">
					${collectOrderDataByUcoid.orderId}<br />
					<span>HH:MM AM/PM</span>
				</a>
			</li>
		</ul>
	</json:property>
</json:object>
