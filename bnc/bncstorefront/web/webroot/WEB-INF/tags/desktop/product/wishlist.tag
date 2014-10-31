<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product"  type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/desktop/storepickup" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<spring:theme code="text.addToCart" var="addToCartText"/>

<c:forEach var="WishlistEntries" items="${wishlist2Model1.entries}">
<c:url value="${WishlistEntries.product.url}" var="productUrl"/>

<c:set value="${not empty WishlistEntries.product.potentialPromotions}" var="hasPromotion"/>

<div class="productListItem${hasPromotion ? ' productListItemPromotion' : ''}">
	<ycommerce:testId code="test_searchPage_wholeProduct">
		<a href="${productUrl}" title="${WishlistEntries.product.name}" class="productMainLink">
			<div class="thumb">
					<product:productPrimaryImage product="${WishlistEntries.product}" format="product"/>
				<c:if test="${not empty product.potentialPromotions and not empty product.potentialPromotions[0].productBanner}">
					<img class="promo" src="${WishlistEntries.product.potentialPromotions[0].productBanner.url}" alt="${WishlistEntries.product.potentialPromotions[0].description}" title="${WishlistEntries.product.potentialPromotions[0].description}"/>
				</c:if>
			</div>
			
			<ycommerce:testId code="searchPage_price_label_${WishlistEntries.product.code}">
				<div  class="price"><format:price priceData="${WishlistEntries.product.price}"/></div>
			</ycommerce:testId>
			
		
				<ycommerce:testId code="searchPage_productName_link_${WishlistEntries.product.code}">
					<div class="head">${WishlistEntries.product.name}</div>
				</ycommerce:testId>
				<c:if test="${not empty WishlistEntries.product.averageRating}">
					<product:productStars rating="${WishlistEntries.product.averageRating}" />
				</c:if>
				<c:if test="${not empty WishlistEntries.product.summary}">
					<div class="details">${WishlistEntries.product.summary}</div>
				</c:if>			
				<product:productListerClassifications product="${WishlistEntries.product}"/>
				
				
	
				<ycommerce:testId code="searchPage_addToCart_button_${WishlistEntries.product.code}">
					<c:set var="buttonType">submit</c:set>
					<c:choose>
						<c:when test="${WishlistEntries.product.stock.stockLevelStatus.code eq 'outOfStock' }">
							<c:set var="buttonType">button</c:set>
							<spring:theme code="text.addToCart.outOfStock" var="addToCartText"/>
						</c:when>
						<c:when test="${WishlistEntries.product.stock.stockLevelStatus.code eq 'lowStock' }">
							<div class='lowStock'><spring:theme code="product.variants.only.left" arguments="${WishlistEntries.product.stock.stockLevel}"/></div>
						</c:when>
					</c:choose>
					</ycommerce:testId>
			
		</a>
		
		
		
		<%-- <c:url value="/wishlist/add/?productCodePost=${product.code}" var="favouriteFormAction" />

<a href="${favouriteFormAction}"> Add to wishlist</a> --%>

		
	
		<%-- <!--code added for wishlist  -->
		
		<c:url value="/wishlist/add" var="addToWishlistUrl"/>			
		<ycommerce:testId code="wishlist">
		
		<spring:theme code="text.addToWishlist" var="addToWishlistText"/>
		
		<a href="${addToWishlistUrl}" >Add to wishlist</a>	<form:form id="addToWishlistForm${product.code}" action="${addToWishlistUrl}" method="post" class="add_to_Wishlist_form">
					<input type="hidden" name="productCodePost" value="${product.code}"/>
					<button type="button" class="positive" >${addToWishlistText}</button>				
					
				</form:form>
				
			</ycommerce:testId>
		 --%>
			
		<div class="cart clearfix">
				<c:url value="/cart/add" var="addToCartUrl"/>

				<form:form id="addToCartForm${WishlistEntries.product.code}" action="${addToCartUrl}" method="post" class="add_to_cart_form">
					<input type="hidden" name="productCodePost" value="${WishlistEntries.product.code}"/>
					<button type="${buttonType}" class="addToCartButton <c:if test="${WishlistEntries.product.stock.stockLevelStatus.code eq 'outOfStock' }">out-of-stock</c:if>" <c:if test="${WishlistEntries.product.stock.stockLevelStatus.code eq 'outOfStock' }"> disabled="disabled" aria-disabled="true"</c:if>>${addToCartText}</button>				
				</form:form>
			<ycommerce:testId code="pickup">
			<c:if test="${WishlistEntries.product.availableForPickup}">
				<storepickup:clickPickupInStore product="${WishlistEntries.product}" entryNumber="0" cartPage="false"/>
			</c:if>
		
		</ycommerce:testId>	
		</div>


<c:url value="/wishlist/modify?productCodePost=${WishlistEntries.product.code}&wl=rem" var="removeWishlistUrl"/>			
		<ycommerce:testId code="Removewishlist">
		
		
		
		<a href="${removeWishlistUrl}" >Removewishlist</a> 
		
		</ycommerce:testId>
		
</ycommerce:testId>

</div>

</c:forEach>



		

