<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> 
<script src="${commonResourcePath}/bnc_js/jquery.diagram.js"></script> 
<script>
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
			alert("Please enter correct UCOID");
		}
	});
}

function OrderDetailsByOrderID(orderId) {
	
	alert(orderId);
		$.ajax({
			type : 'GET',
			url : "${contextPath}/orderslist/order/orderCode",
			
			
			data: "orderCode="+orderId,
			dataType : 'json',
			success : function(response) {
				alert("success");
				
				$('#CSROrderDetails').html(response.CSROrder_Details);
			},
			error : function(e) {
			}
		});
	}

function getOrdersByFromDate()
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
		url : "${contextPath}/orderslist/datetime",
		data : "fdate=" + fdate+"&tdate="+tdate+"&ftime="+ftime+"&ttime="+ttime,
		dataType : 'json',
		success : function(response) {
			$("#order_menu").html(response.searchby_ucoid);
		},
		error : function(e) {
			alert("Please enter dates in proper format! Dates as DD.MM.YYYY and Time as HH:MM AM/PM!!");
		}
	});
}
	
	
	

</script>