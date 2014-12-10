<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="bnc" tagdir="/WEB-INF/tags/desktop/bnc_csr" %>
<json:object>
	<json:property name="searchby_time" escapeXml="false">
	
	<div class="inner_content_blk">
			<div class="left_block">
				<div id="order_menu">
				
				<c:forEach items="${csrCustomerDataList}" var="customerByTime">
				
				${customerByTime.customerName} </br>
				</c:forEach>
					
					</div>
					</div>
	</div>
	</json:property>
	</json:object>
	