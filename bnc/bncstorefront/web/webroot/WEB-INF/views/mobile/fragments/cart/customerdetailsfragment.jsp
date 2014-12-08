<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<json:object>
	<json:property name="customer_details" escapeXml="false">
		<div class="tab_menu_block">
			<div class="tab_menu_profile">
				<ul>
					<li class="space"><input name="" class="assist_btn"
						type="button" value="Assist"></li>
					<li class="divider space"></li>
					<li class="space"><input name="" class="assist_btn"
						type="button" value="No Thanks"></li>
					<li class="divider space"></li>
					<li class="space"><a href="#" class="send_greeting">Send
							Greeting</a></li>
				</ul>
			</div>
		</div>
		<div class="tab_menu_block">
			<div class="tab_menu">
				<ul>
					<li><a href="#">Personal Data</a></li>
				</ul>
			</div>
		</div>
		<div class="content_tabel">
			<!--------Personal Details Tabel Starts Here-------->
			<div class="personal_details_tabel">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="43%"><img src="${storecustomerData.profilePictureURL}" alt=""/></td>
						<td width="57%">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td width="43%" class="bluetext">${storecustomerData.customerName}</td>
						<td width="57%">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2"><table width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td width="43%" class="graytext">Date of Birth</td>
									<td width="57%" class="graytext">Address</td>
								</tr>
								<tr>
									<td class="bluetext">${informationDto.dob}</td>
									<td class="bluetext">${informationDto.line1}&nbsp;${informationDto.line2}&nbsp;${informationDto.apartment}&nbsp;${informationDto.postalCode}</td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2"><table width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td width="43%" class="graytext">UID</td>
									<td width="57%">&nbsp;</td>
								</tr>
								<tr>
									<td class="bluetext">${storecustomerData.customerId}</td>
									<td width="57%">&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td width="43%" class="graytext">Email ID</td>
									<td width="57%">&nbsp;</td>
								</tr>
								<tr>
									<td class="bluetext">${storecustomerData.customerId}</td>
									<td width="57%">&nbsp;</td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2"><table width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td colspan="2" class="grayheading">Shopping Trend</td>
								</tr>
								<tr>
									<td width="16%">&nbsp;</td>
									<td width="84%">&nbsp;</td>
								</tr>
								<tr>
									<td class="smallgraytxt">Electronics</td>
									<td><div class="meter orange nostripes">
											<span style="width: 160px; height: 6px"></span>
										</div></td>
								</tr>
								<tr>
									<td colspan="2" height="10"></td>
								</tr>
								<tr>
									<td class="smallgraytxt">Groceries</td>
									<td><div class="meter orange nostripes">
											<span style="width: 250px; height: 6px"></span>
										</div></td>
								</tr>
								<tr>
									<td colspan="2" height="10"></td>
								</tr>
								<tr>
									<td class="smallgraytxt">Stationary</td>
									<td><div class="meter orange nostripes">
											<span style="width: 40px; height: 6px"></span>
										</div></td>
								</tr>
								<tr>
									<td colspan="2" height="10"></td>
								</tr>
								<tr>
									<td class="smallgraytxt">Home Decor</td>
									<td><div class="meter orange nostripes">
											<span style="width: 140px; height: 6px"></span>
										</div></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<c:if test="${not empty productData}">
						<tr>
							<td colspan="2" class="grayheading">Recently Viewed</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="caurosel_block">
									<div class="left_arrow">
										<a href="#">
											<img src="${commonResourcePath}/bnc_images/arrow_left.png" alt="" border="0"/>
										</a>
									</div>
									<div class="caurosel_center_block">
										<c:forEach items="${productData}" var="product">
											<c:url value="${product.url}" var="productQuickViewUrl" />
											<div class="caurosel_widget">
											  <div class="caurosel_img">
													<div class="thumb">
														<product:productPrimaryImage product="${product}" format="thumbnail"/>
													</div>
											  </div>
											  <div class="caurosel_text">${product.name}<br>
											    <format:fromPrice priceData="${product.price}"/>
											   </div>
											</div>
										</c:forEach>
									</div>
									<div class="right_arrow"><a href="#"><img src="${commonResourcePath}/bnc_images/arrow_right.png" alt=""  border="0"/></a></div>
								 </div>
	               	</td>
						</tr>
					</c:if>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<c:if test="${not empty wishlist.entries}">
						<tr>
							<td colspan="2" class="grayheading">Wishlist</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="caurosel_block">
	                  		<div class="left_arrow">
	                  			<a href="#"><img src="${commonResourcePath}/bnc_images/arrow_left.png" alt="" border="0"/></a>
	                  		</div>
	                  		<div class="caurosel_center_block">
	                  			<c:forEach items="${wishlist.entries}" var="wishlist">
											<div class="caurosel_widget">
		                      			<div class="caurosel_img">
													<div class="thumb">
														<product:productPrimaryImage product="${wishlist.product}" format="thumbnail"/>
													</div>
												</div>
		                      			<div class="caurosel_text">${fn:substring(wishlist.product.name,0,20)}<br>
		                        			<format:fromPrice priceData="${wishlist.product.price}"/></div>
		                    				</div>
										</c:forEach>
	                  		</div>
	                  		<div class="right_arrow">
	                  			<a href="#"><img src="${commonResourcePath}/bnc_images/arrow_right.png" alt=""  border="0"/></a>
	                  		</div>
	                		</div>
							</td>
						</tr>
					</c:if>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>
			</div>
		</div>
	</json:property>
</json:object>