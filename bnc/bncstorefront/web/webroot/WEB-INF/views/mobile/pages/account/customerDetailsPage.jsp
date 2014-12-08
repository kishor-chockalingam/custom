<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/mobile/nav/breadcrumb"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bnc" tagdir="/WEB-INF/tags/mobile/bnc_csr" %>


<script type="text/javascript">
	<!--
	function preCheck(){
		if(document.getElementById('csrId').value == ""){
		document.getElementById('errorMsg').style.display='block';
		document.getElementById('errorMsg').style.color='red';
		return false;
		}
		else{
			document.getElementById('errorMsg').style.display='none';
			return true;
		}
	}

	function pop(div, profilePictureURL, customerName, waitingTime, loginTime, customerId) {
		document.getElementById(div).style.display = 'block';
		document.getElementById('customerNameID').innerText = customerName;
		document.getElementById('waitingTimeID').innerText = waitingTime;
		document.getElementById('LoggedinID').innerText = loginTime;
		document.getElementById('customerPicSrcID').src = profilePictureURL;
		document.getElementById('customerPK').value = customerId;
	}
	function hide(div) {
		document.getElementById(div).style.display = 'none';
		document.getElementById('customerNameID').innerText = '';
		document.getElementById('waitingTimeID').innerText = '';
		document.getElementById('LoggedinID').innerText = '';
		document.getElementById('customerPicSrcID').src = '#';
		document.getElementById('customerPK').value = '';
	}
	//To detect escape button
	document.onkeydown = function(evt) {
		evt = evt || window.event;
		if (evt.keyCode == 27) {
			hide('popDiv');
		}
	};
//-->
</script>
<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>  -->
<!-- <script type="text/javascript"> -->
<!-- // function executeQuery() { -->
<!-- //     $.ajax({ -->
<%-- //       url: '${contextPath}/customerlist/customerdeatils', --%>
<%-- //       data : "size="+${fn:length(customerLoggedInDataList)}, --%>
<!-- //       success: function(data) { -->
<%-- //        var size = '${param.size}'; --%>
<%-- //        var listSize = ${fn:length(customerLoggedInDataList)}; --%>
<!-- //        if(size!=0 && size!='' && size!=listSize) -->
<!-- //        { -->
<!-- //     	   var audio = {}; -->
<!-- // 			audio["walk"] = new Audio(); -->
<!-- // 			audio["walk"].src = '${commonResourcePath}'+"/bnc_audio/bellring01.mp3"			 -->
<!-- // 			audio["walk"].play(); -->
<!-- // 			alert("hey"); -->
<!-- //        } -->
<!-- //        window.location = "${contextPath}/customerlist/customerdeatils?size="+${fn:length(customerLoggedInDataList)}; -->
<!-- //       } -->
<!-- //     }); -->
<!-- //     setTimeout(executeQuery, 10000); -->
<!-- //   } -->

<!-- //   $(document).ready(function() { -->
<!-- //     // run the first time; all subsequent calls will take care of themselves -->
<!-- //     setTimeout(executeQuery, 10000); -->
<!-- //   }); -->
<!-- </script>  -->

<!-- 	<div id="breadcrumb" class="breadcrumb"></div> -->

<!-- 	<div id="globalMessages"></div> -->


<%-- 		<div align="right"><a href="${contextPath}/customerlist/csrlogout">Exit</a></div> --%>
<!-- 	<div class="span-24"> -->
<!-- 			<div class="span-8"> -->
<!-- 			<div class="userRegister" id="refresh-this-div"> -->
<!-- 				<div class="headline">Waiting For Assistance</div> -->
<!-- 				<table id="loggedIn_customers" class="bnctable"> -->
<%-- 					<c:forEach items="${customerLoggedInDataList}" var="logedInUser"> --%>
<!-- 						<tr> -->
<!-- 							<td class="bnctd"><img -->
<%-- 								src="${logedInUser.profilePictureURL}" --%>
<%-- 								alt="${logedInUser.customerName}" height="42" width="42"></td> --%>
<%-- 							<td class="bnctd"><a href="${contextPath}/customerlist/assistcustomer?customerPK=${logedInUser.storeCustomerPK}">${logedInUser.customerName} </a><br> --%>
<%-- 								${logedInUser.waitingTime}<br> ${logedInUser.loginTime} --%>
<!-- 							</td> -->
<!-- 							<td class="bnctd"><a href="#" -->
<%-- 								onClick="pop('popDiv','${logedInUser.profilePictureURL}','${logedInUser.customerName}','${logedInUser.waitingTime}','${logedInUser.loginTime}','${logedInUser.storeCustomerPK}')"><img src="${contextPath}/_ui/desktop/common/images/Assist_Button.jpg" alt="Assist" height="25" width="70"></a> --%>
<%-- 								<br> <a href="${contextPath}/customerlist/statusUpdate?status=NoThanks&customerPK=${logedInUser.storeCustomerPK}"><img src="${contextPath}/_ui/desktop/common/images/No_Thanks_Button.jpg" alt="Assist" height="25" width="74"></a></td> --%>
<!-- 						</tr> -->
<%-- 					</c:forEach> --%>
<!-- 				</table> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="span-8"> -->
<!-- 			<div class="userLogin"> -->
<!-- 				<div class="headline">In Service</div> -->
<!-- 				<table id="order_history" class="bnctable"> -->
<%-- 					<c:forEach items="${customerInServiceDataList}" var="inServiceUser"> --%>
<!-- 					<tr> -->
<!-- 							<td class="bnctd"><img -->
<%-- 								src="${inServiceUser.profilePictureURL}" --%>
<%-- 								alt="${inServiceUser.customerName}" height="42" width="42"></td> --%>
<%-- 							<td class="bnctd"><a href="${contextPath}/customerlist/assistcustomer?customerPK=${inServiceUser.storeCustomerPK}">${inServiceUser.customerName}</a> <br> --%>
<%-- 								${inServiceUser.loginTime} --%>
<!-- 							</td> -->
<!-- 							<td class="bnctd"> -->
<%-- 								<br> <a href="${contextPath}/customerlist/statusUpdate?status=Completed&customerPK=${inServiceUser.storeCustomerPK}"><img src="${contextPath}/_ui/desktop/common/images/Complete_Button.jpg" alt="Assist" height="25" width="74"></a></td> --%>
<!-- 						</tr> -->
						
<%-- 					</c:forEach> --%>
<!-- 				</table> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="span-8 last"> -->
<!-- 			<div class="userGuest"> -->
<!-- 				<div class="headline">No, Thanks</div> -->
<!-- 				<table id="order_history" class="bnctable"> -->
<%-- 					<c:forEach items="${customerNoThanxDataList}" var="noThanxUser"> --%>
<!-- 						<tr> -->
<!-- 							<td class="bnctd"><img -->
<%-- 								src="${noThanxUser.profilePictureURL}" --%>
<%-- 								alt="${noThanxUser.customerName}" height="42" width="42"></td> --%>
<%-- 							<td class="bnctd"><a href="${contextPath}/customerlist/assistcustomer?customerPK=${noThanxUser.storeCustomerPK}">${noThanxUser.customerName} </a><br> --%>
<%-- 								${noThanxUser.loginTime} --%>
<!-- 							</td> -->
<!-- 							<td class="bnctd"> &nbsp; -->
<!-- 								<td> -->
<!-- 						</tr> -->
<%-- 					</c:forEach> --%>
<!-- 				</table> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- 	<div id="popDiv" class="ontop"> -->
<%-- 	<form action="${contextPath}/customerlist/assistcustomer" method="get" onsubmit='return preCheck();'> --%>
<!-- 	<table id="popup" style="width:40%;float:left;"> -->
<!-- 	<tr> -->
<!-- 	<td style="width:38%;float:left;" colspan="3">&nbsp;</td> -->
<!-- 	<td style="width:2%;float:left;"><a href="#" onClick="hide('popDiv')">Close</a></td> -->
<!-- 	</tr> -->
	
<!-- 	<tr> -->
<!-- 	<td style="width:2%;float:left;">&nbsp;</td> -->
<!-- 	<td style="width:22%;float:left;"> -->
<!-- 									<table> -->
<!-- 									<tr valign="top"> -->
<!-- 										<td id="customerPicID"><img id="customerPicSrcID" src="" -->
<!-- 											alt="" height="42" width="42"></td> -->
<!-- 										<td> -->
<!-- 											<table> -->
<!-- 												<tr> -->
<!-- 													<td id="customerNameID"></td> -->
<!-- 												</tr> -->
<!-- 												<tr> -->
<!-- 													<td id="waitingTimeID"></td> -->
<!-- 												</tr> -->
<!-- 												<tr> -->
<!-- 													<td id="LoggedinID"></td> -->
<!-- 												</tr> -->
<!-- 											</table> -->
<!-- 										</td> -->
<!-- 									</tr> -->

<!-- 								</table> -->
<!-- 	</td> -->
<!-- 	<td style="width:14%;float:right;" colspan="2">&nbsp;</td> -->
<!-- 	<td style="width:2%;float:right;" colspan="2">&nbsp;</td> -->
<!-- 	</tr> -->
		
	
<!-- 	<tr> -->
<!-- 	<td style="width:2%;float:left;">&nbsp;</td> -->
<!-- 	<td style="width:38%;float:left;" colspan="3"> -->
<!-- 								<table> -->
<!-- 									<tr> -->
<!-- 										<td>Enter CSR Code to proceed with Customer Assistance</td> -->
<!-- 									</tr> -->
<!-- 									<tr> -->
<!-- 										<td><input type="text" id="csrId" name="csrId"/></td> -->
<!-- 									</tr> -->
<!-- 									<tr><td><div id="errorMsg" style="display:none;">CSR code cannot be blank</div></td></tr> -->
<!-- 								</table> -->
<!-- 	</td> -->
<!-- 	</tr> -->
	
	
<!-- 	<tr> -->
<!-- 	<td style="width:24%;float:left;" colspan="2">&nbsp;</td> -->
<%-- 	<td style="width:14%;align:right;" ><input type="hidden" id="customerPK" name="customerPK" value=""><img src="${contextPath}/_ui/desktop/common/images/Assist_Customer.jpg" alt="CSR Agent" height="42" width="42"> <input type="submit" value="Assist Customer"></input></td> --%>
<!-- 	<td style="width:2%;float:right;" >&nbsp;</td> -->
<!-- 	</tr> -->
	
<!-- 	</table> -->

<%-- 	</form> --%>
<!-- 	</div> -->
	
<!-- 	<div id="inStoreOrders"> -->
<!-- 	<br> -->
<!-- 	<br> -->
<!-- 	<br> -->
<%-- 		<c:url value="/orderslist/vieworders" var="viewOrdersURL"></c:url> --%>
<%-- 		<form action="${viewOrdersURL}" method="get"> --%>
<!-- 			<input type="submit" value="View In-Store Orders"></input> -->
<%-- 		</form> --%>
<!-- 	</div> -->

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1">
<title>Send Greeting</title>
<link type="text/css" rel="stylesheet"
	href="${commonResourcePath}/bnc_css/style.css" />
</head>
<body>
	<div id="main_wrapper" style="position: relative">
		<bnc:csr_header />
		<div class="clearboth"></div>
		<!--Content Starts here-->
		<div id="main_content_blk">
			<div class="top_banner">
				<bnc:cust_chart/>
				<div class="clearboth"></div>
				<bnc:dateTime/>
			</div>
			<div class="clearboth"></div>
			<div class="inner_content_blk">
				<div class="left_block bigger_menu">
					<div id="order_menu">
						<ul>
							<li class="search_padding"><input type="text" value=""
								placeholder="Search " name="q" class="search-text placeholder">
							</li>
							<c:forEach items="${customerLoggedInDataList}" var="logedInUser">
								<li>
									<a href="#" class="current">
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
				</div>
				<div class="right_block smaller_blovk">
					<div class="tab_menu_block">
						<div class="tab_menu_profile">
							<ul>
								<li class="space"><a href="#" class="send_greeting">Send
										Greeting</a></li>
								<li class="divider space"></li>
								<li class="space"><a href="#" class="start_conversation">Start
										Conversation</a></li>
								<li class="divider space"></li>
								<li class="space"><a href="#" class="process_order1">Process
										Order</a></li>
							</ul>
						</div>
					</div>
					<div class="tab_menu_block">
						<div class="tab_menu">
							<ul>
								<li><a href="#">Personal Data</a></li>
							</ul>
						</div>
					</div>
					<div class="content_tabel">
						<!--------Personal Details Tabel Starts Here-------->
						<div class="personal_details_tabel">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="43%" class="bluetext">Custome Name</td>
									<td width="57%">&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2"><table width="100%" border="0"
											cellspacing="0" cellpadding="0">
											<tr>
												<td width="43%" class="graytext">Date Of Birth</td>
												<td width="57%" class="graytext">Address</td>
											</tr>
											<tr>
												<td class="bluetext">27.11.1988</td>
												<td class="bluetext">Lorem Ipsum</td>
											</tr>
										</table></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2"><table width="100%" border="0"
											cellspacing="0" cellpadding="0">
											<tr>
												<td width="43%" class="graytext">Lorem Ipsum</td>
												<td width="57%" class="graytext">Lorem Ipsum</td>
											</tr>
											<tr>
												<td class="bluetext">Lorem Ipsum</td>
												<td class="bluetext">Lorem Ipsum</td>
											</tr>
										</table></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2"><table width="100%" border="0"
											cellspacing="0" cellpadding="0">
											<tr>
												<td width="43%" class="graytext">Lorem Ipsum</td>
												<td width="57%" class="graytext">Lorem Ipsum</td>
											</tr>
											<tr>
												<td class="bluetext">Lorem Ipsum</td>
												<td class="bluetext">Lorem Ipsum</td>
											</tr>
										</table></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2"><table width="100%" border="0"
											cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="2" class="grayheading">Shopping Trend</td>
											</tr>
											<tr>
												<td width="16%">&nbsp;</td>
												<td width="84%">&nbsp;</td>
											</tr>
											<tr>
												<td class="smallgraytxt">Electronics</td>
												<td><div class="meter orange nostripes">
														<span style="width: 160px; height: 6px"></span>
													</div></td>
											</tr>
											<tr>
												<td colspan="2" height="10"></td>
											</tr>
											<tr>
												<td class="smallgraytxt">Groceries</td>
												<td><div class="meter orange nostripes">
														<span style="width: 250px; height: 6px"></span>
													</div></td>
											</tr>
											<tr>
												<td colspan="2" height="10"></td>
											</tr>
											<tr>
												<td class="smallgraytxt">Stationary</td>
												<td><div class="meter orange nostripes">
														<span style="width: 40px; height: 6px"></span>
													</div></td>
											</tr>
											<tr>
												<td colspan="2" height="10"></td>
											</tr>
											<tr>
												<td class="smallgraytxt">Home Decor</td>
												<td><div class="meter orange nostripes">
														<span style="width: 140px; height: 6px"></span>
													</div></td>
											</tr>
											<tr>
												<td>&nbsp;</td>
												<td>&nbsp;</td>
											</tr>
										</table></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2" class="grayheading">Recently Viewed</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2"><div id="caurosel_block">
											<div class="left_arrow">
												<a href="#"><img
													src="${commonResourcePath}/bnc_images/arrow_left.png"
													alt="" border="0" /></a>
											</div>
											<div class="caurosel_center_block caurosel_center_small">
												<div class="caurosel_widget caurosel_widget1">
													<div class="caurosel_img">
														<img
															src="${commonResourcePath}/bnc_images/order_item_img/img3.jpg"
															alt="" />
													</div>
													<div class="caurosel_text">
														Product Name<br> Description Lorem Ipsum
													</div>
												</div>
												<div class="caurosel_widget caurosel_widget1">
													<div class="caurosel_img">
														<img
															src="${commonResourcePath}/bnc_images/order_item_img/img2.jpg"
															alt="" />
													</div>
													<div class="caurosel_text">
														Product Name<br> Description Lorem Ipsum
													</div>
												</div>
												<div class="caurosel_widget caurosel_widget1">
													<div class="caurosel_img">
														<img
															src="${commonResourcePath}/bnc_images/order_item_img/img1.jpg"
															alt="" />
													</div>
													<div class="caurosel_text">
														Product Name<br> Description Lorem Ipsum
													</div>
												</div>
											</div>
											<div class="right_arrow">
												<a href="#"><img
													src="${commonResourcePath}/bnc_images/arrow_right.png"
													alt="" border="0" /></a>
											</div>
										</div></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2" class="grayheading">Wishlist</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2"><div id="caurosel_block">
											<div class="left_arrow">
												<a href="#"><img
													src="${commonResourcePath}/bnc_images/arrow_left.png"
													alt="" border="0" /></a>
											</div>
											<div class="caurosel_center_block caurosel_center_small">
												<div class="caurosel_widget caurosel_widget1">
													<div class="caurosel_img">
														<img
															src="${commonResourcePath}/bnc_images/order_item_img/img3.jpg"
															alt="" />
													</div>
													<div class="caurosel_text">
														Product Name<br> Description Lorem Ipsum
													</div>
												</div>
												<div class="caurosel_widget caurosel_widget1">
													<div class="caurosel_img">
														<img
															src="${commonResourcePath}/bnc_images/order_item_img/img2.jpg"
															alt="" />
													</div>
													<div class="caurosel_text">
														Product Name<br> Description Lorem Ipsum
													</div>
												</div>
												<div class="caurosel_widget caurosel_widget1">
													<div class="caurosel_img">
														<img
															src="${commonResourcePath}/bnc_images/order_item_img/img1.jpg"
															alt="" />
													</div>
													<div class="caurosel_text">
														Product Name<br> Description Lorem Ipsum
													</div>
												</div>
											</div>
											<div class="right_arrow">
												<a href="#"><img
													src="${commonResourcePath}/bnc_images/arrow_right.png"
													alt="" border="0" /></a>
											</div>
										</div></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
							</table>
						</div>
						<!--------Personal Details Tabel Starts Here-------->

					</div>
				</div>
				<div class="clearboth"></div>
			</div>
		</div>
		<!--Content Ends here-->

	</div>
	<script
		src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script src="${commonResourcePath}/bnc_js/jquery.diagram.js"></script>
	<script src="${commonResourcePath}/bnc_js/script.js"></script>
</body>
</html>