<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="${currentLanguage.isocode}">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1">
		<c:url value="/orderslist/vieworders" var="viewOrdersURL"></c:url>
		<meta http-equiv="Refresh" content="30;url=${viewOrdersURL}">
		<title>CSR Dashboard</title>
		<link type="text/css" rel="stylesheet" href="${commonResourcePath}/bnc_css/style.css" />
	</head>
	<body>
		<div id="main_wrapper" style="position:relative"> 
			<!--Header Starts here-->
			<div id="head">
				<div class="contain_block">
					<div id="menu_top"><a href="javascript:void(0);" class="mClose">Menu</a></div>
					<div class="sitename">CSR Dashboard</div>
					<div class="username_blk">
						<div class="reminder">
							<div class="bell"> <span class="remind_round">1</span> </div>
						</div>
						<div class="divider"></div>
						<div class="userblock">
							<div class="user_photo"><img src="${commonResourcePath}/bnc_images/user_photo.png" alt=""/></div>
							<div class="username">Agent Name</div>
						</div>
					</div>
					<div class="mobile_leftnav" style="position:absolute;left:-250px; height:100%; top:0; background:#333333">
						<nav class="main-nav">
							<ol>
								<li>
									<a href="#" style="border-top:1px solid #494949" class="select">
										<img src="${commonResourcePath}/bnc_images/dashboard_icon.png" alt=""/>
										<span>Customers<br>Dashboard</span>
									</a>
								</li>
								<li>
									<a href="#">
										<img src="${commonResourcePath}/bnc_images/dashboard_icon.png" alt=""/>
										<span>Orders<br>Dashboard</span>
									</a>
								</li>
								<li>
									<a href="#">
										<img src="${commonResourcePath}/bnc_images/search_icon.png" alt=""/>
										<span class="singleline">Search</span>
									</a>
								</li>
								<li>
									<a href="#">
										<img src="${commonResourcePath}/bnc_images/notification_icon.png" alt=""/>
										<span class="singleline">Notifications</span>
									</a>
								</li>
								<li>
									<a href="#">
										<img src="${commonResourcePath}/bnc_images/myteam_icon.png" alt=""/>
										<span class="singleline">My Team</span>
									</a>
								</li>
								<li>
									<a href="#">
										<img src="${commonResourcePath}/bnc_images/mytargets_icon.png" alt=""/>
										<span>My Targets <br>&amp; Performance</span>
									</a>
								</li>
							</ol>
						</nav>
					</div>
				</div>
			</div>
			<!-- Header Ends here-->
			<div class="clearboth"></div>
			<!--Content Starts here-->
			<div id="main_content_blk">
				<div class="top_banner">
					<div class="chart_block">
						<div class="diagram_block diagramcurrent">
							<div id="diagram-id-1" class="diagram" data-percent="34"></div>
							<div class="digram_txt">Queued</div>
						</div>
						<div class="diagram_block">
							<div id="diagram-id-2" class="diagram" data-percent="14"></div>
							<div class="digram_txt">Active</div>
						</div>
						<div class="diagram_block">
							<div id="diagram-id-3" class="diagram" data-percent="9"></div>
							<div class="digram_txt">Serviced</div>
						</div>
						<div class="diagram_block">
							<div id="diagram-id-4" class="diagram" data-percent="40%"></div>
							<div class="digram_txt">Target</div>
						</div>
					</div>
					<div class="clearboth"></div>
					<div class="datetime_block">
						<div class="date_block">
							<ul>
								<li><a href="#"><img src="${commonResourcePath}/bnc_images/calender_icon.png" alt="calender" border="0"/></a></li>
								<li>From :</li>
								<li>
									<input type="text" value="5.11.2014">
								</li>
								<li>To :</li>
								<li>
									<input type="text" disabled value="5.11.2014" class="disable">
								</li>
							</ul>
						</div>
						<div class="time_block">
							<ul>
								<li><a href="#"><img src="${commonResourcePath}/bnc_images/clock_icon.png" alt="clock" border="0"/></a></li>
								<li>From :</li>
								<li>
									<input type="text" value="03:00PM">
								</li>
								<li>To :</li>
								<li>
									<input type="text" value="04:00PM">
								</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="clearboth"></div>
				<div class="inner_content_blk">
					<div class="left_block">
						<div id="order_menu">
							<ul>
								<li class="search_padding">
									<input type="text" placeholder="Search " name="q" class="search-text placeholder" id="ucoid" onblur="javascript:doAjaxPost();">
								</li>
								<c:forEach items="${searchPageData.results}" var="CollectOrderData" varStatus=counter>
									<c:url value="/orderslist/order/${CollectOrderData.orderId}" var="orderDetailsUrl"/>
									<c:set var="currentClass" value=''/>
									<c:if test="${counter.count==1}">
										<c:set var="currentClass" value='class="current"'/>
									</c:if>
									<li>
										<a href="${orderDetailsUrl}" ${currentClass}>
											{CollectOrderData.orderId}<br />
											<span>HH:MM AM/PM</span>
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
									<li><a href="#" class="tabmenuselect">Order Details</a></li>
									<li class="divider"></li>
									<li><a href="#">Personal Details</a></li>
								</ul>
							</div>
						  <div class="tab_button"></div>
						</div>
					  <div><img src="${commonResourcePath}/bnc_images/right_blk.png"/></div>
					</div>
					<div class="clearboth"></div>
				</div>
			</div>
			<!--Content Ends here--> 
		</div>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> 
		<script src="${commonResourcePath}/bnc_js/jquery.diagram.js"></script> 
		<script>
			$(function() {
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
		
				$('#menu_top a').click(function() {
				    if($(this).hasClass("mOpen")) {
				        $( ".mobile_leftnav" ).animate({
				            left: "-250px"
				        }, "slow" );
				        $(this).addClass("mClose");
				        $(this).removeClass("mOpen");
				        $( "#main_wrapper" ).animate({
				            left: "0"
				        }, "slow" );        
				    } else if($(this).hasClass("mClose")) {console.log("asd");
				        $( ".mobile_leftnav" ).animate({
				            left: "-250"
				        }, "slow" );
				        $( "#main_wrapper" ).animate({
				            left: "250"
				        }, "slow" );        
				        $(this).addClass("mOpen");
				        $(this).removeClass("mClose");
				    }
				});
				$.event.add(window,"resize",$.alignMiddle);
			});
			
			function doAjaxPost() {
				var ucoid = document.getElementById('ucoid').value;
				 if (document.getElementById('ucoid').value =='' ) { 
				alert("Please enter the UCOID!");
					document.getElementById('ucoid').focus();
					return false;
				}
				//alert("Adding "+ucoid);
				$.ajax({
					type : 'GET',
					url : "${contextPath}/orderslist/ucoid",
					data : "ucoid=" + ucoid,
					dataType : 'json',
					success : function(response) {
					$("#order_menu").html(response.searchby_ucoid);
					},
					error : function(e) {
						//$("#invalidUCOID").append("Please enter the valid UCOID");
					}
				});
			}
		</script>
	</body>
</html>