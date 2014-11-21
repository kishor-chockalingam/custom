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