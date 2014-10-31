<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>

<template:page pageTitle="${pageTitle}">
	<div id="breadcrumb" class="breadcrumb">
		<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}"/>
	</div>
	<div id="globalMessages">
		<common:globalMessages/>
	</div>
	<cms:pageSlot position="SideContent" var="feature" element="div" class="span-4 side-content-slot cms_disp-img_slot">
		<cms:component component="${feature}"/>
	</cms:pageSlot> 
<div class="item_container_holder">
	<div class="title_holder">
		<div class="title">
			<div class="title-top">
				<span></span>
			</div>
		</div>
		<h2>
			<spring:theme code="CSR Login" />
		</h2>
	</div>
	<div class="item_container">
		<p class="required">
			<spring:theme code="login.required.message" />
		</p>
	<table style="background-color:white;width:50%;float:left;">
		<form:form id="testId" action="${contextPath}/login/csrLogin" method="post" commandName="csrLoginForm">
		<tr><td><formElement:formInputBox idKey="username" labelKey="csr.login.username" path="username" inputCSS="text" mandatory="true"/></td><td>&nbsp;</td></tr>
		<tr><td><formElement:formPasswordBox idKey="password" labelKey="csr.login.password" path="password" inputCSS="text password" mandatory="true"/></td><td>&nbsp;</td></tr>
		<tr><td><formElement:formInputBox idKey="pos" labelKey="point.of.service" path="pos" inputCSS="text" mandatory="true"/></td><td>&nbsp;</td></tr>
		<tr><td><span style="display: block; clear: both;"> <ycommerce:testId
					code="login_Login_button">
					<button type="submit" class="form">
						<spring:theme code="Login" />
					</button>
				</ycommerce:testId>
			</span></td><td>&nbsp;</td></tr>	
		</form:form>
	
	</table>
	</div>
</div>
</template:page>

