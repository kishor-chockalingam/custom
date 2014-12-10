<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="bnc" tagdir="/WEB-INF/tags/mobile/bnc_csr" %>
<json:object>
	<json:property name="searchby_time" escapeXml="false">
	<div id="order_menu">
	
	<ul>
	
	<li class="search_padding"><input type="text" value=""
					placeholder="Search " name="q" class="search-text placeholder" id="customername" onblur="javascript:searchByCustomerName();">
				</li>
			
				
				<c:forEach items="${csrCustomerDataList}" var="customerByTime">
				
				${customerByTime.customerName} </br></br>
				</c:forEach>
					
				
			
					
	</ul>
	</div>
	</json:property>
	</json:object>
	