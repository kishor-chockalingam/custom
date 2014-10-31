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
package com.acc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import com.acc.product.data.ReviewDataList;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * 
 */
@Ignore("ACC-2747 Ignored test because it tries to open a web connection to the hybris instance, which isn't possible during a unit text execution.")
@UnitTest
public class UserWebServiceTest
{

	private static final Logger LOG = Logger.getLogger(UserWebServiceTest.class.getName());


	private RestTemplate template;

	@Before
	public void before()
	{
		LOG.setLevel(Level.DEBUG);
		final HttpClient client = new HttpClient();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(TestConstants.USERNAME,
				TestConstants.PASSWORD);
		client.getState().setCredentials(new AuthScope(AuthScope.ANY), credentials);

		final CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client);

		template = new RestTemplate(commons);
	}

	@Test()
	public void testGetUserReviews_Success_XML()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<String> response = template.exchange("http://localhost:9001/rest/v1/users/{userid}/reviews",
				HttpMethod.GET, requestEntity, String.class, TestConstants.USERNAME);
		assertEquals("application/xml;charset=UTF-8", response.getHeaders().getContentType().toString());
	}

	@Test()
	public void testGetUserReviews_Success_JSON()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getJSONHeaders());
		final ResponseEntity<String> response = template.exchange("http://localhost:9001/rest/v1/users/{userid}/reviews",
				HttpMethod.GET, requestEntity, String.class, TestConstants.USERNAME);
		assertEquals("application/json;charset=UTF-8", response.getHeaders().getContentType().toString());
	}

	@Test()
	public void testGetUserReviews_Success_JSON_Deep()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getJSONHeaders());
		final ResponseEntity<ReviewDataList> response = template.exchange("http://localhost:9001/rest/v1/users/{userid}/reviews",
				HttpMethod.GET, requestEntity, ReviewDataList.class, TestConstants.USERNAME);
		assertEquals("application/json;charset=UTF-8", response.getHeaders().getContentType().toString());

		final ReviewDataList reviews = response.getBody();
		assertEquals(5, reviews.getReviews().size());
	}

	@Test()
	public void testGetUser_Success_JSON_Deep()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getJSONHeaders());
		final ResponseEntity<CustomerData> response = template.exchange("http://localhost:9001/rest/v1/users/{userid}",
				HttpMethod.GET, requestEntity, CustomerData.class, TestConstants.USERNAME);
		assertEquals("application/json;charset=UTF-8", response.getHeaders().getContentType().toString());

		final CustomerData customer = response.getBody();
		assertEquals("Klaus Demokunde", customer.getName());
		assertEquals("demo", customer.getUid());
		assertNotNull(customer.getCurrency());
		assertNotNull(customer.getLanguage());
		assertNotNull(customer.getDefaultBillingAddress());
		assertNotNull(customer.getDefaultShippingAddress());
	}








	protected HttpHeaders getXMLHeaders()
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/xml");
		return headers;
	}

	protected HttpHeaders getJSONHeaders()
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		return headers;
	}
}
