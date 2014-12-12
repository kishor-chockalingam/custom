<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		<c:forEach items="${customerLoggedInDataList}" var="logedInUser" varStatus="counter">
			<c:set var="currentClass" value=""/>
			<c:if test="${counter.count==1}">
				<c:set var="currentClass" value='class="current"'/>
				<input type="hidden" id="currentUserId" value="${logedInUser.storeCustomerPK}"/>
			</c:if>
			<li>
				<a id="${logedInUser.storeCustomerPK}" onclick="javascript:getCustomerDetails('${logedInUser.storeCustomerPK}');" ${currentClass}>
					<span class="menuperson">
						<img src="${logedInUser.profilePictureURL}" />
					</span>
					${logedInUser.customerName}<br /> 
					<span>
						${logedInUser.loginTime}
					</span>
					<c:if test="${param.status=='INSERVICE' || param.status=='COMPLETED'}">
						<span>
							assisted by ${logedInUser.processedBy}
						</span>
					</c:if>
				</a>
			</li>
		</c:forEach>
	</ul>
</div>
