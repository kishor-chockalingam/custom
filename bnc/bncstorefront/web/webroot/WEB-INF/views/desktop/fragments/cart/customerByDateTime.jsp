<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="bnc" tagdir="/WEB-INF/tags/desktop/bnc_csr" %>
<json:object>
	<json:property name="searchby_time" escapeXml="false">
	
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//run the first time; all subsequent calls will take care of themselves -->
			getCustomerDetails(document.getElementById("currentUserId").value);
		});
	</script>
	<div id="order_menu">
			<ul>
				
	
	<li class="search_padding"><input type="text" value=""
					placeholder="Search " name="q" class="search-text placeholder" id="customername" onblur="javascript:searchByCustomerName();">
				</li>
			
				
				<%-- <c:forEach items="${csrCustomerDataList}" var="customerByTime">
				
				<br/> ${customerByTime.customerName} <br/>
				</c:forEach> --%>
					 <c:forEach items="${csrCustomerDataList}" var="logedInUser" varStatus="counter">
		
					<c:set var="currentClass" value=""/>
					<c:if test="${counter.count==1}">
						<c:set var="currentClass" value='class="current"'/>
						<input type="hidden" id="currentUserId" value="${logedInUser.pk}"/>
					</c:if>
					<li>
						<a id="${logedInUser.pk}" onclick="javascript:getCustomerDetails('${logedInUser.pk}');" ${currentClass}>
							<span class="menuperson">
								<img src="${logedInUser.profilePictureURL}" />
							</span>
							${logedInUser.customerName}<br /> 
							<span>
								Logged in by ${logedInUser.loginTime}
							</span>
							<c:if test="${logedInUser.status=='INSERVICE' || logedInUser.status=='COMPLETED'}">
								<br>
								<span>
									${logedInUser.status} assisted by ${logedInUser.processedBy}
								</span>
							</c:if>
						</a>
					</li>
				</c:forEach> 
			</ul>
			
		</div>
				
			
					
	</ul>
	</div>
	</json:property>
	</json:object>
	