<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="content_tabel" id="CSROrderDetails">
	<!--------Order Details Tabel Starts Here-------->
		<c:choose>
			<c:when test="${not empty orderData.code}">
				<div class="order_details_tabel">
	        	
	          <table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
	                  <tr>
	                    <td width="62%" class="order_number">${orderData.code}</td>
	                    <td width="27%"><span style="float:left">Status:</span>
	                      <div class="styled-select">
	                        <c:url value="/orderslist/order/${orderData.code}" var="orderDetailsURL"></c:url>
						<form:form action="${orderDetailsURL}" method="post" commandName="collectOrderData">
							<form:hidden path="pk"/>
							<form:select path="status" onchange="javascript:collectOrderData.submit();">
								<c:forEach items="${collectOrderStatusList}" var="stat">
									<form:option value="${stat}">${stat}</form:option>
								</c:forEach>
							</form:select>
						</form:form>
	                      </div></td>
	                    <td width="11%">&nbsp;</td>
	                  </tr>
	                  <tr>
	                    <td colspan="3">&nbsp;</td>
	                  </tr>
	                </table></td>
	            </tr>
	            <tr>
	              <td><table width="100%" border="0" cellspacing="0" cellpadding="0" class="orderitemtabel">
	              	<c:forEach items="${orderData.entries}" var="entry">
								<c:url value="${entry.product.url}" var="productUrl" />
								<tr>
			                      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
			                          <tr>
			                            <td width="8%"><product:productPrimaryImage	product="${entry.product}" format="thumbnail" /></td>
			                            <td width="2%"></td>
			                            <td width="25%">${entry.product.name}</td>
			                            <td width="12%">${entry.quantity}</td>
			                            <td width="11%"><format:price priceData="${entry.basePrice}"	displayFreeForZero="true" /></td>
			                            <td width="21%">$${entry.quantity * entry.basePrice.value}</td>
	<%-- 		                            <td width="11%" align="right"><img src="${commonResourcePath}/bnc_images/check_box.jpg" alt=""/></td> --%>
			                            <td width="3%">&nbsp;</td>
			                          </tr>
			                        </table></td>
			                    </tr>
			                    <tr>
			                      <td height="10"></td>
			                    </tr>
							</c:forEach>
	                </table></td>
	            </tr>
	            <tr>
	              <td>&nbsp;</td>
	            </tr>
	            <tr>
	              <td>&nbsp;</td>
	            </tr>
	            <tr>
	              <td class="order_number"><table width="100%" border="0" cellspacing="0" cellpadding="0">
	                  <tr>
	                    <td width="9%">Total</td>
	                    <td width="91%">$${orderData.totalPrice.value}</td>
	                  </tr>
	                </table></td>
	            </tr>
	            <tr>
	              <td>&nbsp;</td>
	            </tr>
	            <tr>
	              <td>&nbsp;</td>
	            </tr>
	          </table>
	          
	        </div>
			</c:when>
			<c:otherwise>
				<div class="order_details_tabel" style="color:red;">
					<c:out value="Please enter dates in proper format! Dates as DD.MM.YYYY and Time as HH:MM AM/PM!! FromDate should be before ToDate!!"></c:out>
				</div>
			</c:otherwise>
		</c:choose>
</div>