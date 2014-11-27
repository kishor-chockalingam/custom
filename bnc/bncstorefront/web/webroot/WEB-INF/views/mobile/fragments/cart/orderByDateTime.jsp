<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="bnc" tagdir="/WEB-INF/tags/mobile/bnc_csr" %>
<json:object>
	<json:property name="searchby_ucoid" escapeXml="false">
		<div class="inner_content_blk">
			<div class="left_block">
				<div id="order_menu">
					<ul>
						<li class="search_padding">
							<input type="text" placeholder="Search " name="q" class="search-text placeholder" id="ucoid" onblur="javascript:doAjaxPost();">
						</li>
						<c:forEach items="${collectOrderDataByUcoid}" var="CollectOrderData" varStatus="counter">
							<c:url value="/orderslist/order/${CollectOrderData.orderId}" var="orderDetailsUrl"/>
							<c:set var="currentClass" value=''/>
							<c:if test="${counter.count==1}">
								<c:set var="currentClass" value='class="current"'/>
							</c:if>
							<li>
								<a onclick="javascript:OrderDetailsByOrderID('${CollectOrderData.orderId}');" ${currentClass}>
									${CollectOrderData.orderId}<br />
									<span>
										<fmt:formatDate type="time" value="${CollectOrderData.createdTS}" pattern="MM/dd/yyyy hh:mm aa"/><br />
									</span>
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<div class="right_block">
				<div class="tab_menu_block">
					<div class="tab_menu">
						<ul>
							<li id="orderDetailsTab"><a href="#" class="tabmenuselect">Order Details</a></li>
							<li class="divider"></li>
							<li id="personalDetails"><a onclick='javascript:PersonalDetailsByUserID("${orderData.user.uid}", "${orderData.code}");'>Personal Details</a></li>
						</ul>
					</div>
				  <div class="tab_button"></div>
				</div>
			  <bnc:orderDetails />
			</div>
			<div class="clearboth"></div>
		</div>
	</json:property>
</json:object>
