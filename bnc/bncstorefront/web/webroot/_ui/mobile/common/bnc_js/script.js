// JavaScript Document
$(function() {
		$('#diagram-id-1').diagram({ 
			size: "60",
			borderWidth: "7",
			bgFill: "#efefef",
			frFill: "#13ccde",
			textSize: 20,
			textColor: '#626262'
		});
		
		$('#diagram-id-2').diagram({ 
			size: "60",
			borderWidth: "7",
			bgFill: "#efefef",
			frFill: "#13ccde",
			textSize: 20,
			textColor: '#626262'
		});
		
		$('#diagram-id-3').diagram({ 
			size: "60",
			borderWidth: "7",
			bgFill: "#efefef",
			frFill: "#13ccde",
			textSize: 20,
			textColor: '#626262'
		});
		
		$('#diagram-id-4').diagram({ 
			size: "60",
			borderWidth: "7",
			bgFill: "#efefef",
			frFill: "#ffd200",
			textSize: 20,
			textColor: '#626262'
		});

$('#menu_top a').click(function() {
    if($(this).hasClass("mOpen")) {
        $( ".mobile_leftnav" ).animate({
            left: "-1000px"
        }, "fast" );
        $(this).addClass("mClose");
        $(this).removeClass("mOpen");
        $( "#main_wrapper" ).animate({
            left: "0"
        }, "fast" );        
    } else if($(this).hasClass("mClose")) {console.log("asd");
        $( ".mobile_leftnav" ).animate({
            left: "0"
        }, "fast" );
        $( "#main_wrapper" ).animate({
            left: "0"
        }, "fast" );        
        $(this).addClass("mOpen");
        $(this).removeClass("mClose");
    }

});


$.event.add(window,"resize",$.alignMiddle);
	});
	