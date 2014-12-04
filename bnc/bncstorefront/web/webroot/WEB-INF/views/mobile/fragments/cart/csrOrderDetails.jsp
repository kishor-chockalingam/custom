<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/mobile/nav/breadcrumb"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/mobile/order" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<json:object>
	<json:property name="CSROrder_Details" escapeXml="false">
	  <script type="text/javascript">
	  	document.getElementById("personalDetails").innerHTML = 
		  "<a onclick='javascript:PersonalDetailsByUserID(\"${orderData.user.uid}\", \"${orderData.code}\");'>Personal Details</a>"; 
		  document.getElementById("orderDetailsTab").innerHTML = 
			  "<a href='#' class='tabmenuselect'>Order Details</a>";
	  </script>
 		<!--------Order Details Tabel Starts Here-------->
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
          <!--------Order Details Tabel Ends Here-------->      
	</json:property>
						</json:object>
						