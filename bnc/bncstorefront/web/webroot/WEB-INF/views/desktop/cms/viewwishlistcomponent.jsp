<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>

<c:choose>
	<c:when test="${not empty wishlist}">
		<div class="scroller">
			<div class="title">${title}</div>
<ul class="carousel jcarousel-skin">
						<c:forEach items="${wishlist.entries}" var="wishlist">

						<c:url value="${wishlist.product.url}" var="productQuickViewUrl" />
							<li>
								<a href="${productQuickViewUrl}" class="scrollerProduct"> 
									<div class="thumb">
										<product:productPrimaryImage product="${wishlist.product}" format="product"/>
									</div>
									
									<div class="priceContainer">
										<format:fromPrice priceData="${wishlist.product.price}"/>
									</div>
									<div class="details">
											${wishlist.product.name}
									</div>
								</a> 
							</li>
						</c:forEach>
			</ul>
		</div>
	</c:when>

	<c:otherwise>
		<component:emptyComponent/>
	</c:otherwise>
</c:choose>
