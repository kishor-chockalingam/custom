<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bnc" tagdir="/WEB-INF/tags/desktop/bnc_csr" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<!DOCTYPE html>
<html lang="${currentLanguage.isocode}">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1">
		<title>CSR Dashboard</title>
		<link type="text/css" rel="stylesheet" href="${commonResourcePath}/bnc_css/style.css" />
		<script type="text/javascript">
			setTimeout(function () { 
				$.ajax({
					type : 'GET',
					url : "${contextPath}/orderslist/orders",
					data : "size="+${Queued}+"&status="+'${param.status}',
					dataType : 'json',
					success : function(response) {
						$("#main_content_blk").html(response.orders_list);
						$('#diagram-id-1').diagram({ 
							size: "60",
							borderWidth: "8",
							bgFill: "#efefef",
							frFill: "#13ccde",
							textSize: 20,
							textColor: '#626262'
						});
						
						$('#diagram-id-2').diagram({ 
							size: "60",
							borderWidth: "8",
							bgFill: "#efefef",
							frFill: "#13ccde",
							textSize: 20,
							textColor: '#626262'
						});
						
						$('#diagram-id-3').diagram({ 
							size: "60",
							borderWidth: "8",
							bgFill: "#efefef",
							frFill: "#13ccde",
							textSize: 20,
							textColor: '#626262'
						});
						
						$('#diagram-id-4').diagram({ 
							size: "60",
							borderWidth: "8",
							bgFill: "#efefef",
							frFill: "#ffd200",
							textSize: 20,
							textColor: '#626262'
						});
					},
					error : function(e) {
						//$("#invalidUCOID").append("Please enter the valid UCOID");
					}
				});
			}, 60000);
			
		</script> 
	</head>
	<body>
		<div id="main_wrapper" style="position:relative"> 
			<!--Header Starts here-->
			<bnc:csr_orders_header/>
			<!-- Header Ends here-->
			<div class="clearboth"></div>
			<!--Content Starts here-->
			<div id="main_content_blk">
				<div class="top_banner">
					<bnc:chart />
					<div class="clearboth"></div>
					<bnc:dateTime />
				</div>
				<div class="clearboth"></div>
				<div class="inner_content_blk" id="ordersDivId">
					<div class="left_block">
						<bnc:ordersList />
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
					<c:if test="${not empty orderData.code}">
					  <bnc:orderDetails />
					  </c:if>
					</div>
					<div class="clearboth"></div>
				</div>
			</div>
			<!--Content Ends here--> 
		</div>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> 
		<script src="${commonResourcePath}/bnc_js/jquery.diagram.js"></script> 
		<script src="${commonResourcePath}/bnc_js/script.js"></script>
		<bnc:csr_script />
	</body>
</html>