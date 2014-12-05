<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


 <script type="text/javascript">
    function redirectToOrdersDashboard()
    {
    	window.location.href="/bncstorefront/electronics/en/orderslist/vieworders";
    }
    function redirectToCustomersDashboard()
    {
    	window.location.href="/bncstorefront/electronics/en/customerlist/customerdeatils";
    }
    </script>
<div id="head">
	<div class="contain_block">
		<div id="menu_top"><a href="javascript:void(0);" class="mClose">Menu</a></div>
		<div class="sitename">CSR Dashboard</div>
		<div class="username_blk">
			<div class="reminder">
				<div class="bell"> <span class="remind_round" id="bell_number">${Queued}</span> </div>
			</div>
			<div class="divider"></div>
			<div class="userblock">
				<div class="user_photo"><img src="${commonResourcePath}/bnc_images/user_photo.png" alt=""/></div>
				<c:if test="${CSR_USER eq null or CSR_USER eq ''}">
					<c:redirect url="/login/csrLogin"/>
				</c:if>
				<div class="username">${fn:toUpperCase(CSR_USER)}</div>
			</div>
		</div>
	</div>
	<div class="hide_menu_block"></div>
	<div class="mobile_leftnav">
		<nav class="main-nav">
			<ol>
				<li>
					<a href="#" onClick="redirectToCustomersDashboard()" style="border-top:1px solid #494949"   >
						<img src="${commonResourcePath}/bnc_images/dashboard_icon.png" alt=""/>
						<span>Customers<br>Dashboard</span>
					</a>
				</li>
				<li>
					<a href="#" onClick="redirectToOrdersDashboard()" class="select" >
						<img src="${commonResourcePath}/bnc_images/dashboard_icon.png" alt="" />
						<span>Orders<br>Dashboard</span>
					</a>
				</li>
				<div class="rest_nav" style="opacity:.4;display:block; ">
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
				</div>
			</ol>
		</nav>
	</div>
</div>