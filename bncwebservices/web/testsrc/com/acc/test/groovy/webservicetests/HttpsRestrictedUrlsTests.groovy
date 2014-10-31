/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.acc.test.groovy.webservicetests

import static java.net.HttpURLConnection.*

import com.acc.test.groovy.webservicetests.markers.AvoidCollectingOutputFromTest

import org.junit.Test

/**
 *	 Negative test cases for http access for https restricted urls
 */
@org.junit.experimental.categories.Category(AvoidCollectingOutputFromTest.class)
public class HttpsRestrictedUrlsTests {
	def GET = "GET";
	def PUT = "PUT";
	def POST = "POST";
	def DELETE = "DELETE";
	def accept = "XML";

	def id = "someFakeId"


	@Test
	public void testCartAddress() {
		//	<intercept-url pattern="/**/cart/address/**" requires-channel="https"/>
		//@RequestMapping(value = "/address/delivery/{id}", method = RequestMethod.PUT)
		//@RequestMapping(value = "/address/delivery", method = RequestMethod.DELETE)
		TestUtil.getConnection("/cart/address/delivery/${id}", PUT, accept, HTTP_MOVED_TEMP)
		TestUtil.getConnection("/cart/address/delivery", DELETE, accept, HTTP_MOVED_TEMP)
	}

	@Test
	public void testCartDeliveryModes() {
		//	<intercept-url pattern="/**/cart/deliverymodes/**" requires-channel="https"/>
		//@RequestMapping(value = "/deliverymodes/{code}", method = RequestMethod.PUT)
		//@RequestMapping(value = "/deliverymodes", method = RequestMethod.DELETE)
		//	@RequestMapping(value = "/deliverymodes", method = RequestMethod.GET)
		TestUtil.getConnection("/cart/deliverymodes/${id}",PUT,accept, HTTP_MOVED_TEMP)
		TestUtil.getConnection("/cart/deliverymodes",DELETE,accept, HTTP_MOVED_TEMP)
		TestUtil.getConnection("/cart/deliverymodes",GET,accept, HTTP_MOVED_TEMP)

	}

	@Test
	public void testPlaceorder() {
		//	<intercept-url pattern="/**/cart/placeorder/**" requires-channel="https"/>
		//@RequestMapping(value = "/placeorder", method = RequestMethod.POST)
		TestUtil.getConnection("/cart/placeorder", POST, accept, HTTP_MOVED_TEMP)
	}

	@Test
	public void testPaymentInfo() {
		//	<intercept-url pattern="/**/cart/paymentinfo/**" requires-channel="https"/>
		//@RequestMapping(value = "/paymentinfo", method = RequestMethod.POST)
		//@RequestMapping(value = "/paymentinfo/{id}", method = RequestMethod.PUT)
		TestUtil.getConnection("/cart/paymentinfo", POST, accept, HTTP_MOVED_TEMP)
		TestUtil.getConnection("/cart/paymentinfo/${id}", PUT, accept, HTTP_MOVED_TEMP)
	}

	@Test
	public void testCartAuthorize() {
		// @RequestMapping(value = "/authorize", method = RequestMethod.POST)
		TestUtil.getConnection("/cart/authorize", POST, accept, HTTP_MOVED_TEMP)
	}

	@Test
	public void testOrders() {
		//	<intercept-url pattern="/**/orders/**" requires-channel="https"/>
		//@RequestMapping(value = "/{code}", method = RequestMethod.GET)
		TestUtil.getConnection("/orders/",GET, accept, HTTP_MOVED_TEMP)
		//@RequestMapping(method = RequestMethod.GET)
		TestUtil.getConnection("/orders/${id}",GET, accept, HTTP_MOVED_TEMP)
	}

	@Test
	public void testCustomers() {
		//	<intercept-url pattern="/**/customers/**" requires-channel="https"/>
		//@RequestMapping(method = RequestMethod.POST)
		TestUtil.getConnection("/customers/",POST,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/addresses/default/{id}", method = RequestMethod.PUT)
		TestUtil.getConnection("/customers/current/addresses/default/${id}",PUT,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/profile", method = RequestMethod.POST)
		TestUtil.getConnection("/customers/current/profile", POST,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/addresses", method = RequestMethod.GET)
		TestUtil.getConnection("/customers/current/addresses",GET,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/addresses", method = RequestMethod.POST)
		TestUtil.getConnection("/customers/current/addresses", POST, accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/addresses/{id}", method = RequestMethod.PUT)
		TestUtil.getConnection("/customers/current/addresses/${id}",PUT,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/addresses/{id}", method = RequestMethod.DELETE)
		TestUtil.getConnection("/customers/current/addresses/${id}", DELETE,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current", method = RequestMethod.GET)
		TestUtil.getConnection("/customers/current", GET,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/password", method = RequestMethod.POST)
		TestUtil.getConnection("/customers/current/password",POST,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/paymentinfos", method = RequestMethod.GET)
		TestUtil.getConnection("/customers/current/paymentinfos", GET,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/paymentinfos/{id}", method = RequestMethod.GET)
		TestUtil.getConnection("/customers/current/paymentinfos/${id}", GET,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/paymentinfos/{id}", method = RequestMethod.DELETE)
		TestUtil.getConnection("/customers/current/paymentinfos/{id}",DELETE,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/paymentinfos/{paymentInfoId}/address", method = RequestMethod.POST)
		TestUtil.getConnection("/customers/current/paymentinfos/${id}/address", POST,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/paymentinfos/{paymentInfoId}", method = RequestMethod.PUT)
		TestUtil.getConnection("/customers/current/paymentinfos/${id}",PUT ,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/current/customergroups", method = RequestMethod.GET)
		TestUtil.getConnection("/customers/current/customergroups",GET,accept, HTTP_MOVED_TEMP)
		//@RequestMapping(value = "/{uid}/customergroups", method = RequestMethod.GET)
		TestUtil.getConnection("/customers/${id}/customergroups",GET,accept, HTTP_MOVED_TEMP)
	}

	@Test
	public void testCustomergroups() {
		//	<intercept-url pattern="/**/customergroups/**" requires-channel="https"/>
		//@RequestMapping(method = RequestMethod.POST)
		//@RequestMapping(value = "/{uid}/members", method = RequestMethod.PUT)
		//@RequestMapping(value = "/{uid}/members/{userId:.*}", method = RequestMethod.DELETE)
		//@RequestMapping(method = RequestMethod.GET)
		//@RequestMapping(value = "/{uid}", method = RequestMethod.GET)
		TestUtil.getConnection("/customergroups", POST, accept, HTTP_MOVED_TEMP)
		TestUtil.getConnection("/customergroups/${id}/members",PUT, accept, HTTP_MOVED_TEMP)
		TestUtil.getConnection("/customergroups/${id}/members/${id}", DELETE, accept, HTTP_MOVED_TEMP)
		TestUtil.getConnection("/customergroups", GET, accept, HTTP_MOVED_TEMP)
		TestUtil.getConnection("/customergroups/${id}", GET, accept, HTTP_MOVED_TEMP)
	}
}
