<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="bnc" tagdir="/WEB-INF/tags/desktop/bnc_csr" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<json:object>
	<json:property name="orders_list" escapeXml="false">
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
		<c:set var="orderListSizeNew" value="${fn:length(collectOrdersDataList)}"/>
		<c:if test="${orderListSizeNew!=param.size}">
			<script type="text/javascript">
				var audio = {};
				audio["walk"] = new Audio();
				audio["walk"].src = '${commonResourcePath}'+"/bnc_audio/bellring01.mp3"			
				audio["walk"].play();
				document.getElementById("bell_number").innerHTML = ${orderListSizeNew};
			</script>
		</c:if>
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
	</json:property>
</json:object>