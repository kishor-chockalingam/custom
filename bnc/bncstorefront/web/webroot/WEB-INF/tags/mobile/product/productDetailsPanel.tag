<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="galleryImages" required="true" type="java.util.List" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<spring:theme code="text.addToCart" var="addToCartText"/>

<div class="productDetailsPanel">

	<product:productImagePanel product="${product}" galleryImages="${galleryImages}"/>

	<div class="span-10 productDescription last">
		<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
			<product:productPricePanel product="${product}"/>
		</ycommerce:testId>
		<ycommerce:testId code="productDetails_productNamePrice_label_${product.code}">
			<h1>
					${product.name}
			</h1>
		</ycommerce:testId>

		<product:productReviewSummary product="${product}"/>


		<div class="summary">
			${product.summary}
		</div>

		<product:productPromotionSection product="${product}"/>

		<cms:pageSlot position="VariantSelector" var="component" element="div">
			<cms:component component="${component}"/>
		</cms:pageSlot>

		<cms:pageSlot position="AddToCart" var="component" element="div" class="span-8">
			<cms:component component="${component}"/>
		</cms:pageSlot>
		
		
		

		<product:productShareTag/>
	</div>

	<cms:pageSlot position="Section2" var="feature" element="div" class="span-8 section2 cms_disp-img_slot last">
		<cms:component component="${feature}"/>
	</cms:pageSlot>
	<!--code added for wishlist  -->
		
		<c:url value="/wishlist/modify?productCodePost=${product.code}&wl=add" var="addToWishlistUrl"/>			
		<ycommerce:testId code="wishlist">
		
		<spring:theme code="text.addToWishlist" var="addToWishlistText"/>
		<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
			<a href="${addToWishlistUrl}" >Add to wishlist</a> 	 <%-- <form:form id="addToWishlistForm${product.code}" action="${addToWishlistUrl}" method="post" class="add_to_Wishlist_form">
						<input type="hidden" name="productCodePost" value="${product.code}"/>
						<button type="button" class="positive" >${addToWishlistText}</button>				
						
					</form:form>  --%>
		</sec:authorize>		
			</ycommerce:testId>
</div>
