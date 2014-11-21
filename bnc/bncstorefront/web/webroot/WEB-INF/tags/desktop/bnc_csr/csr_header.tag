<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
				<div class="username">${fn:toUpperCase(CSR_USER)}</div>
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