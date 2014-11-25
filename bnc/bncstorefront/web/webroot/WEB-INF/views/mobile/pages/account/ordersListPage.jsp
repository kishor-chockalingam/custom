<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="bnc" tagdir="/WEB-INF/tags/mobile/bnc_csr" %>
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
					data : "size="+${fn:length(collectOrdersDataList)},
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
			}, 30000);
			
		</script> 
	</head>
	<body>
		<div id="main_wrapper" style="position:relative"> 
			<!--Header Starts here-->
			<bnc:csr_header />
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
				<div class="inner_content_blk">
					<div class="left_block">
						<bnc:ordersList />
					</div>
					<div class="right_block">
						<div class="tab_menu_block">
							<div class="tab_menu">
								<ul>
									<li><a href="#" class="tabmenuselect">Order Details</a></li>
									<li class="divider"></li>
									<li><a href="#">Personal Details</a></li>
								</ul>
							</div>
						  <div class="tab_button"></div>
						</div>
					  <div id="CSROrderDetails"><img src="${commonResourcePath}/bnc_images/right_blk.png"/></div>
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