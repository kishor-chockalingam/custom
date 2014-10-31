<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>



<template:page pageTitle="${pageTitle}">
	<div class="item_container_holder">
		<div class="title_holder">
			<div class="title">
				<div class="title-top">
					<span></span>
				</div>
			</div>
		</div>
		<div id="breadcrumb" class="breadcrumb"></div>

		<div id="globalMessages"></div>


		<div class="span-72">

			<table style="background-color: white; width: 100%; float: left;">
				<tr>
					<td style="width: 50%; float: left;">
						<div class="userRegister">

							<table id="loggedIn_customers" class="bnctable">
								<tr>
									<td class="bnctd"><img
										src="${storecustomerData.profilePictureURL}"
										alt="${storecustomerData.customerName}" height="42" width="42"></td>
									<td class="bnctd">${storecustomerData.customerName} <br>
										${storecustomerData.waitingTime}<br>
										${storecustomerData.loginTime}
									</td>
								</tr>
							</table>
						</div>

					</td>
					<td style="width: 50%; float: left;">
						<div class="userRegister">

							<table id="loggedIn_customers" class="bnctable">

								<tr>

									<td class="bnctd">${customerData.name}<br>${customerData.title}<br>${customerData.uid}
									</td>
									<td><img
										src="${contextPath}/_ui/desktop/common/images/CSR_Sample_Photo.jpg"
										alt="CSR Agent" height="42" width="42"></td>
								</tr>

							</table>
						</div>
					</td>
				</tr>

				<tr>
					<td style="width: 50%; float: left;">&nbsp;</td>
					<td style="width: 50%; float: left;">&nbsp;</td>
				</tr>

				<tr>
					<td style="width: 50%; float: left;">
						<div style="background-color: Orange;">

							<table id="loggedIn_customers">

								<tr>
									<td><img
										src="${contextPath}/_ui/desktop/common/images/Basic_Profile_Info.jpg"
										alt="CSR Agent" height="42" width="42"></td>
									<td><B>Basic Profile information</B></td>
								</tr>

							</table>
						</div>

					</td>
					<td style="width: 50%; float: left;">
						<div style="background-color: lightBlue;">

							<table id="loggedIn_customers">

								<tr>
									<td><img
										src="${contextPath}/_ui/desktop/common/images/Order_History.jpg"
										alt="CSR Agent" height="42" width="42"></td>
									<td><B>Order History</B></td>
								</tr>

							</table>
						</div>
					</td>
				</tr>

				<tr>
					<td style="width: 50%; float: left;">
						<div class="userRegister">
							<table id="loggedIn_customers" class="bnctable">

								<tr>
									<td>&nbsp;</td>
									<td class="bnctd">${informationDto.name}
										<br>Date of Birth : ${informationDto.dob}
										<br> ${informationDto.line1}
										<br> ${informationDto.line2}
										<br> ${informationDto.apartment} 
										<br> ${informationDto.postalCode}
									</td>
								</tr>

							</table>

						</div>

					</td>
					<td style="width: 50%; float: left;">
						<div class="userRegister">
							<table id="loggedIn_customers" style="width: 100%">
								<tr>
									<td><B>ORDER NO.</B></td>
									<td><B>DATE PLACED</B></td>
									<td><B>TOTAL</B></td>
								</tr>
								<c:forEach items="${customerOrderDataList}"
									var="customerOrderData">
									<tr>
										<td class="bnctd"><a href="#">${customerOrderData.orderCode}</a></td>
										<td class="bnctd">${customerOrderData.placedDate}</td>
										<td class="bnctd">$${customerOrderData.total}</td>
									</tr>
								</c:forEach>

							</table>


						</div>

					</td>
				</tr>

				<tr>
					<td style="width: 50%; float: left;">&nbsp;</td>
					<td style="width: 50%; float: left;">&nbsp;</td>
				</tr>

				<tr>
					<td style="width: 50%; float: left;">
						<div style="background-color: pink;">

							<table id="loggedIn_customers">

								<tr>
									<td><img
										src="${contextPath}/_ui/desktop/common/images/Recommended_Products.jpg"
										alt="CSR Agent" height="42" width="42"></td>
									<td><B>Recommended Products</B></td>
								</tr>

							</table>
						</div>
					</td>
					<td style="width: 25%; float: left;">
						<div style="background-color: lightgreen;">

							<table id="loggedIn_customers">

								<tr>
									<td><img
										src="${contextPath}/_ui/desktop/common/images/Recently_Viewed.jpg"
										alt="CSR Agent" height="42" width="42"></td>
									<td><B>Recently Viewed Products</B></td>
								</tr>

							</table>
						</div>
					</td>
					
				</tr>
				

				<tr>
					<td style="width: 50%; float: left;">
						<div class="userRegister">

							<table id="loggedIn_customers">

								<tr>
									<td colspan="2"> <cms:pageSlot position="recommendedProducts" var="similar"
											element="div" class="span-24 span-bnc-recommended section5 cms_disp-img_slot">
											<cms:component component="${similar}" />
										</cms:pageSlot></td>
								</tr>

							</table>
						</div>

					</td>
					<td style="width: 50%; float: left;">
						<div class="userRegister">

							<table id="loggedIn_customers">

								<tr>
									<td colspan="2"> <cms:pageSlot position="recentlyViewed" var="feature"
											element="div" class="span-24 span-bnc section5 cms_disp-img_slot">
											<cms:component component="${feature}" />
										</cms:pageSlot></td>
								</tr>

							</table>
						</div>

					</td>
					
						
				</tr>
				
				<tr>
					<td style="width: 50%; float: left;">&nbsp;</td>
					<td style="width: 50%; float: left;">&nbsp;</td>
				</tr>
				<tr>
				<td style="width:10%; float: left;">
						<div style="background-color: pink;">

							<table id="loggedIn_customers">

								<tr>
									<td><img
										src="${contextPath}/_ui/desktop/common/images/Recently_Viewed.jpg"
										alt="CSR Agent" height="42" width="42"></td>
									<td><B>Wishlist Items</B></td>
								</tr>

							</table>
						</div>
					</td>
			
					
				</tr>
				
				<tr>
				<td style="width: 10%; float: left; ">
						<div class="userRegister">

							<table id="loggedIn_customers">

								<tr>
									<td colspan="2"> <cms:pageSlot position="wishlist" var="view"
											element="div" class="span-24 span-bnc-wishlist section5 cms_disp-img_slot" style="width:500px;">
											<cms:component component="${view}" />
											
										</cms:pageSlot></td>
								</tr>

							</table>
						</div>

					</td>
			
					<td style="width: 50%; float: left;">&nbsp;</td>
				</tr>
				
					
				
				<tr>
					<td style="width: 50%; float: left;">&nbsp;</td>
					<td style="width: 50%; float: left;"><div align="right">
							<a href="${contextPath}/customerlist/customerdeatils">Back</a>
						</div></td>
				</tr>
				
				

			</table>


		</div>






	</div>

</template:page>

