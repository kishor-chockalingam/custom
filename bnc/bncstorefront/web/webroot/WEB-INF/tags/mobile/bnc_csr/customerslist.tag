<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
	function getCustomerDetails(customerPK)
	{
		$.ajax({
			type : 'GET',
			url : "${contextPath}/customerlist/assistcustomer",
			data : "customerPK="+customerPK,
			dataType : 'json',
			success : function(response) {
				$("#customer_details_block").html(response.customer_details);
			},
			error : function(e) {
				alert("Please enter correct UCOID");
			}
		});
	}
	
	$(document).ready(function() {
		//run the first time; all subsequent calls will take care of themselves -->
		getCustomerDetails(document.getElementById("currentUserId").value);
	});
</script>
<div id="order_menu">
	<ul>
		<li class="search_padding"><input type="text" value=""
			placeholder="Search " name="q" class="search-text placeholder">
		</li>
		<c:forEach items="${customerLoggedInDataList}" var="logedInUser" varStatus="counter">
			<c:set var="currentClass" value=""/>
			<c:if test="${counter.count==1}">
				<c:set var="currentClass" value='class="current"'/>
				<input type="hidden" id="currentUserId" value="${logedInUser.storeCustomerPK}"/>
			</c:if>
			<li>
				<a onclick="javascript:getCustomerDetails('${logedInUser.storeCustomerPK}');" ${currentClass}>
					<span class="menuperson">
						<img src="${logedInUser.profilePictureURL}" />
					</span>
					${logedInUser.customerName}<br /> 
					<span>
						${logedInUser.loginTime}
					</span>
					<c:if test="${param.status=='INSERVICE' || param.status=='COMPLETED'}">
						<span>
							processed by ${logedInUser.processedBy}
						</span>
					</c:if>
				</a>
			</li>
		</c:forEach>
	</ul>
</div>
