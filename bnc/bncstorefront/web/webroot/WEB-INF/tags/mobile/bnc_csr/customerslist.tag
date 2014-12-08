<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="order_menu">
	<ul>
		<li class="search_padding"><input type="text" value=""
			placeholder="Search " name="q" class="search-text placeholder">
		</li>
		<c:forEach items="${customerLoggedInDataList}" var="logedInUser" varStatus="counter">
			<c:set var="currentClass" value=""/>
			<c:if test="${counter.count==1}">
				<c:set var="currentClass" value='class="current"'/>
			</c:if>
			<li>
				<a href="#" ${currentClass}>
					<span class="menuperson">
						<img src="${logedInUser.profilePictureURL}" />
					</span>
					${logedInUser.customerName}<br /> 
					<span>
						${logedInUser.loginTime}
					</span>
				</a>
			</li>
		</c:forEach>
	</ul>
</div>
