<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	function assistCustomer()
	{
		alert('${param.customerPK}');
		window.location = "${contextPath}/customerlist/statusUpdate?status=InService&customerPK=${param.customerPK}";
	}
	function noThanksUpdate()
	{
		alert('${param.customerPK}');
		window.location = "${contextPath}/customerlist/statusUpdate?status=NoThanks&customerPK=${param.customerPK}";
	}
	function servicedCustomer()
	{
		alert('${param.customerPK}');
		window.location = "${contextPath}/customerlist/statusUpdate?status=Completed&customerPK=${param.customerPK}";
	}
		function showSendGreetingBlock()
	{
		$("#mail_messages").removeClass("mail_text");
		$("#mail_messages").html("");
		if($("#send_greeting_block_id").css("display")=="none")
		{
			$("#send_greeting_id").addClass("send_greeting_selected");
			$("#send_greeting_block_id").css("display","block");
		}
		else
		{
			$("#send_greeting_id").removeClass("send_greeting_selected");
			$("#send_greeting_block_id").css("display","none");
		}
		
	}
	function sendGreeting()
	{
		$.ajax({
			type : 'GET',
			url : "${contextPath}/customerlist/greeting",
			data : "customerPK=" + '${param.customerPK}',
			dataType : 'json',
			success : function(response) {
				$("#mail_messages").addClass("mail_text");
				$("#mail_messages").css("color","green");
				$("#mail_messages").html("Main sent successfully");
			},
			error : function(e) {
				$("#mail_messages").addClass("mail_text");
				$("#mail_messages").css("color","red");
				$("#mail_messages").html("Error sending mail.");
			}
		});
	}
</script>
<div class="tab_menu_block">
	<div class="tab_menu_profile">
		<ul>
			<c:choose>
				<c:when test="${storecustomerData.custStatus=='InService' && not empty storecustomerData.processedBy && storecustomerData.processedBy==CSR_USER}">
					<li class="space"><input name="" class="assist_btn"	type="button" value="Serviced" onclick="javascript:servicedCustomer();"></li>
					<li class="divider space"></li>
				</c:when>
				<c:when test="${storecustomerData.custStatus=='Completed'}">
				</c:when>
				<c:otherwise>
					<li class="space"><input name="" class="assist_btn"	type="button" value="Assist" onclick="javascript:assistCustomer();"></li>
					<li class="divider space"></li>
					<li class="space"><input name="" class="assist_btn" type="button" value="No Thanks" onclick="javascript:noThanksUpdate();"></li>
					<li class="divider space"></li>
				</c:otherwise>
			</c:choose>
			<li class="space"><a href="#" id="send_greeting_id" class="send_greeting" onclick="javascript:showSendGreetingBlock();">Send	Greeting</a>
			</li>
		</ul>
	</div>
</div>
<div class="clearboth"></div>
<div id="send_greeting_block_id" class="send_greeting_block" style="display: none;">
	<div id="mail_messages"></div>
	<div class="mail_text">Hi ${storecustomerData.customerName},<br>
		<br>
		Welcome to our store Chiba. Please proceed with your selections. Our customer service representative will be with you shortly. Thanks! 
	</div>
	<div class="send_greeting_buttons">
		<input name="" class="assist_btn" type="button" value="Cancel" onclick="javascript:showSendGreetingBlock();">
		<input name="" class="assist_btn" type="button" value="Send" onclick="javascript:sendGreeting();">
	</div>
</div>
<div class="tab_menu_block">
	<div class="tab_menu">
		<ul>
			<li><a href="#">Personal Data</a></li>
		</ul>
	</div>
</div>
