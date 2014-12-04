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
		$("#ordersDivId").html(response.searchby_ucoid);
		},
		error : function(e) {
			alert("Please enter correct UCOID");
		}
	});
}

function OrderDetailsByOrderID(orderId) {
		$.ajax({
			type : 'GET',
			url : "${contextPath}/orderslist/order/orderCode",
			data: "orderCode="+orderId,
			dataType : 'json',
			success : function(response) {
				$("a").removeClass("current");
				$("#"+orderId).addClass("current");
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
			$("#ordersDivId").html(response.searchby_ucoid);
		},
		error : function(e) {
			alert("Please enter dates in proper format! Dates as DD.MM.YYYY and Time as HH:MM AM/PM!!\n\n FromDate should be before ToDate!!");
		}
	});
}
	
function PersonalDetailsByUserID(uid, orderCode) {
	 $.ajax({
		type : 'GET',
		url : "${contextPath}/orderslist/personaldetails",
		data: "uid="+uid+"&code="+orderCode,
		dataType : 'json',
		success : function(response) {
			$('#CSROrderDetails').html(response.Personal_Details);
		},
		error : function(e) {
			alert("error"+e);
		}
	}); 
}	
	

</script>