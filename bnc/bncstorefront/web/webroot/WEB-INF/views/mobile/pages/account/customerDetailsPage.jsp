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
	<script type="text/javascript">
	function loadOrderDetails(orderCode)
	{
		$.ajax({
			type : 'GET',
			url : "${contextPath}/customerlist/order/orderCode",
			data: "orderCode="+orderCode,
			dataType : 'json',
			success : function(response) {
			$("#orderDetails_"+orderCode).html(response.customer_dashboard_order_details_for_accordion);
			},
			error : function(e) {
			}
		});
	}
	function getCustomerDetails(customerPK)
	{
		$.ajax({
			type : 'GET',
			url : "${contextPath}/customerlist/assistcustomer",
			data : "customerPK="+customerPK,
			dataType : 'json',
			success : function(response) {
				$("a").removeClass("current");
				$("#"+customerPK).addClass("current");
				$("#customer_details_block").html(response.customer_details);
			},
			error : function(e) {
				alert("No customer has logged in yet");
			}
		});
	}
	
	function searchByCustomerName() {
		
		var customername = document.getElementById('customername').value;
		 if (document.getElementById('customername').value =='' ) { 
		alert("Please enter the customername!");
			document.getElementById('customername').focus();
			return false;
		}
		$.ajax({
			type : 'GET',
			url : "${contextPath}/customerlist/customerName",
			data : "customername=" + customername,
			dataType : 'json',
			success : function(response) {
			$("#customer_list_block").html(response.searchby_customername);
			},
			error : function(e) {
				alert("Please enter correct customername");
			}
		});
	}
	
	function getCustomersByFromDate()
	{
		
		
		var fdate = document.getElementById('searchTimeBarFromDate').value;
		var tdate = document.getElementById('searchTimeBarToDate').value;
		var ftime = document.getElementById('searchTimeBarFromTime').value;
		var ttime = document.getElementById('searchTimeBarToTime').value;
		if (fdate =='' ) 
		{ 
			alert("Please enter the from Date!");
			document.getElementById('searchTimeBarFromDate').focus();
			return false;
		}
		if (tdate =='' ) 
		{ 
			alert("Please enter the to Date!");
			document.getElementById('searchTimeBarToDate').focus();
			return false;
		}
		if (ftime =='' ) 
		{ 
			alert("Please enter the from Time!");
			document.getElementById('searchTimeBarFromTime').focus();
			return false;
		}
		if (ttime =='' ) 
		{ 
			alert("Please enter the to Time!");
			document.getElementById('searchTimeBarToTime').focus();
			return false;
		}
		$.ajax({
			type : 'GET',
			url : "${contextPath}/customerlist/datetime",
			data : "fdate=" + fdate+"&tdate="+tdate+"&ftime="+ftime+"&ttime="+ttime,
			dataType : 'json',
			success : function(response) {
				$("#customer_list_block").html(response.searchby_time);
			},
			error : function(e) {
				alert("Please enter dates in proper format! Dates as DD.MM.YYYY and Time as HH:MM AM/PM!!\n\n FromDate should be before ToDate!!");
			}
		});
	}
	</script>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1">
		<title>Customers Dashboard</title>
		<link type="text/css" rel="stylesheet" href="${commonResourcePath}/bnc_css/style.css" />
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
					<bnc:cust_dateTime/>
				</div>
				
				<div class="clearboth"></div>
				<div class="inner_content_blk">
					<div class="left_block bigger_menu" id="customer_list_block">
						<bnc:customerslist/>
					</div>
					<div class="right_block smaller_blovk" id="customer_details_block">
							<!--------Personal Details Table Will go here-------->
							
					</div>
					<div class="clearboth"></div>
					<div id ="customerByTimeDivId"></div>
				</div>
			</div>
			
			<!--Content Ends here-->
	
		</div>
		
		
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script src="${commonResourcePath}/bnc_js/jquery.diagram.js"></script>
		<script src="${commonResourcePath}/bnc_js/script.js"></script>
	</body>
</html>