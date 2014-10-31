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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


/**
 * 
 */
@Ignore("ACC-2747 Ignored test because it tries to open a web connection to the hybris instance, which isn't possible during a unit text execution.")
@UnitTest
public class ProductWebServiceTest
{

	private static final String PASSWD = "nimda";

	private static final String USER = "admin";

	private static final String URL = "http://localhost:9001/rest/v1/products";

	private static final Logger LOG = Logger.getLogger(ProductWebServiceTest.class.getName());

	private RestTemplate template;

	private List<HttpMessageConverter<?>> converters;

	@Before
	public void before()
	{
		LOG.setLevel(Level.DEBUG);
		final HttpClient client = new HttpClient();
		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(USER, PASSWD);
		client.getState().setCredentials(new AuthScope(AuthScope.ANY), credentials);

		final CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client);


		final HttpMessageConverter<?> jsonConverter = new org.springframework.http.converter.xml.MarshallingHttpMessageConverter(
				new org.springframework.oxm.xstream.XStreamMarshaller());
		((org.springframework.http.converter.xml.MarshallingHttpMessageConverter) jsonConverter).setSupportedMediaTypes(Arrays
				.asList(org.springframework.http.MediaType.APPLICATION_JSON));

		final HttpMessageConverter<?> xmlConverter = new org.springframework.http.converter.xml.MarshallingHttpMessageConverter(
				new org.springframework.oxm.xstream.XStreamMarshaller());
		((org.springframework.http.converter.xml.MarshallingHttpMessageConverter) jsonConverter).setSupportedMediaTypes(Arrays
				.asList(org.springframework.http.MediaType.APPLICATION_XML));

		converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(jsonConverter);
		converters.add(xmlConverter);

		template = new RestTemplate(commons);
		template.setMessageConverters(converters);

	}



	@Test()
	public void testGetProductByCode_Success_XML_Deep()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<ProductData> response = template.exchange(URL + "/{code}", HttpMethod.GET, requestEntity,
				ProductData.class, TestConstants.PRODUCT_CODE);
		final ProductData productData = response.getBody();



		assertEquals(TestConstants.PRODUCT_CODE, productData.getCode());
		assertEquals("EASYSHARE V1253, Black", productData.getName());
		assertEquals(5, productData.getImages().size());
	}



	@Test()
	public void testGetProductByCode_Success_XML_Options_Description()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<ProductData> response = template.exchange(URL + "/{code}?options=DESCRIPTION", HttpMethod.GET,
				requestEntity, ProductData.class, TestConstants.PRODUCT_CODE);
		final ProductData productData = response.getBody();

		assertEquals(TestConstants.PRODUCT_CODE, productData.getCode());
		assertEquals("EASYSHARE V1253, Black", productData.getName());
		assertEquals(5, productData.getImages().size());

		assertTrue("Description may not be null!", productData.getDescription() != null);
	}

	@Test()
	public void testGetProductByCode_Success_XML_Options_Gallery()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<ProductData> response = template.exchange(URL + "/{code}?options=GALLERY", HttpMethod.GET,
				requestEntity, ProductData.class, TestConstants.PRODUCT_CODE);
		final ProductData productData = response.getBody();

		assertEquals(TestConstants.PRODUCT_CODE, productData.getCode());
		assertEquals("EASYSHARE V1253, Black", productData.getName());
		assertEquals(10, productData.getImages().size());

	}

	@Test()
	public void testGetProductByCode_Success_XML_Options_Categories()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<ProductData> response = template.exchange(URL + "/{code}?options=CATEGORIES", HttpMethod.GET,
				requestEntity, ProductData.class, TestConstants.PRODUCT_CODE);
		final ProductData productData = response.getBody();

		assertEquals(TestConstants.PRODUCT_CODE, productData.getCode());
		assertEquals("EASYSHARE V1253, Black", productData.getName());
		assertEquals(2, productData.getCategories().size());

	}

	@Test()
	public void testGetProductByCode_Success_XML_Options_Promotions()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<ProductData> response = template.exchange(URL + "/{code}?options=PROMOTIONS", HttpMethod.GET,
				requestEntity, ProductData.class, TestConstants.PRODUCT_CODE);
		final ProductData productData = response.getBody();

		assertEquals(TestConstants.PRODUCT_CODE, productData.getCode());
		assertEquals("EASYSHARE V1253, Black", productData.getName());
	}

	@Test()
	public void testGetProductByCode_Success_XML_Options_Stock()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<ProductData> response = template.exchange(URL + "/{code}?options=STOCK", HttpMethod.GET,
				requestEntity, ProductData.class, TestConstants.PRODUCT_CODE);
		final ProductData productData = response.getBody();

		assertEquals(TestConstants.PRODUCT_CODE, productData.getCode());
		assertEquals("EASYSHARE V1253, Black", productData.getName());

	}

	@Test()
	public void testGetProductByCode_Success_XML_Options_Review()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<ProductData> response = template.exchange(URL + "/{code}?options=REVIEW", HttpMethod.GET,
				requestEntity, ProductData.class, TestConstants.PRODUCT_CODE);
		final ProductData productData = response.getBody();

		assertEquals(TestConstants.PRODUCT_CODE, productData.getCode());
		assertEquals("EASYSHARE V1253, Black", productData.getName());
		assertEquals(0, productData.getReviews().size());

	}

	@Test()
	public void testPutProductByCode_Success_XML_Options_Review()
	{

		final PrincipalData principal = new PrincipalData();
		principal.setName(USER);
		principal.setUid(USER);

		final ReviewData review = new ReviewData();
		review.setAlias("alias message");
		review.setComment("comment here ");
		review.setHeadline("head line new");
		review.setPrincipal(principal);
		review.setDate(new Date());
		review.setRating(Double.valueOf(1));

		final HttpEntity<ReviewData> requestEntity = new HttpEntity<ReviewData>(review, getXMLHeaders());

		final ResponseEntity<ReviewData> response = template.exchange(URL + "/{code}/reviews", HttpMethod.PUT, requestEntity,
				ReviewData.class, TestConstants.PRODUCT_CODE);
		final ReviewData reviewData = response.getBody();

		Assert.assertNotNull(reviewData);

	}

	@Test()
	public void testGetProductByCode_Success_XML_Options_Classifications()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<ProductData> response = template.exchange(URL + "/{code}?options=CLASSIFICATION", HttpMethod.GET,
				requestEntity, ProductData.class, TestConstants.PRODUCT_CODE);
		final ProductData productData = response.getBody();

		assertEquals(TestConstants.PRODUCT_CODE, productData.getCode());
		assertEquals("EASYSHARE V1253, Black", productData.getName());
		assertEquals(23, productData.getClassifications().size());

	}

	@Test(expected = HttpClientErrorException.class)
	public void testGetProductByCode_Failure_XML()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<String> response = template.exchange(URL + "/DOESNOTEXIST{code}", HttpMethod.GET, requestEntity,
				String.class, TestConstants.PRODUCT_CODE);
		assertEquals("application/xml;charset=UTF-8", response.getHeaders().getContentType().toString());
	}

	@Test()
	public void testGetProductByCode_Success_JSON()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getJSONHeaders());
		final ResponseEntity<String> response = template.exchange(URL + "/{code}", HttpMethod.GET, requestEntity, String.class,
				TestConstants.PRODUCT_CODE);
		assertEquals("application/json;charset=UTF-8", response.getHeaders().getContentType().toString());
	}

	@Test()
	public void testGetProductByCode_Success_JSON_Deep()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getJSONHeaders());
		final ResponseEntity<ProductData> response = template.exchange(URL + "/{code}", HttpMethod.GET, requestEntity,
				ProductData.class, TestConstants.PRODUCT_CODE);
		assertEquals(TestConstants.PRODUCT_CODE, response.getBody().getCode());
	}

	@Test(expected = HttpClientErrorException.class)
	public void testGetProductByCode_Failure_JSON()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getJSONHeaders());
		final ResponseEntity<String> response = template.exchange(URL + "/DOESNOTEXIST{code}", HttpMethod.GET, requestEntity,
				String.class, TestConstants.PRODUCT_CODE);
		assertEquals("application/json;charset=UTF-8", response.getHeaders().getContentType().toString());
	}

	@Test()
	public void testETagExists()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getJSONHeaders());
		final ResponseEntity<String> response = template.exchange(URL + "/{code}", HttpMethod.GET, requestEntity, String.class,
				TestConstants.PRODUCT_CODE);
		assertTrue(response.getHeaders().containsKey("ETag"));

	}

	@Test
	public void testBasicSearch()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<String> response = template.exchange(URL + "?query={query}", HttpMethod.GET, requestEntity,
				String.class, "a");
		assertTrue(response.getBody().length() > 0);
		assertEquals("application/xml;charset=UTF-8", response.getHeaders().getContentType().toString());
	}

	@Test
	public void testBasicSearch2()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<FacetSearchPageData> response = template.exchange(URL + "?query={query}", HttpMethod.GET,
				requestEntity, FacetSearchPageData.class, "a");

		final FacetSearchPageData<SolrSearchQueryData, ProductData> facetSearchPageData = response.getBody();
		assertTrue(facetSearchPageData != null);

		assertEquals("a", facetSearchPageData.getCurrentQuery().getFreeTextSearch());
		assertEquals(0, facetSearchPageData.getBreadcrumbs().size());
		assertTrue(facetSearchPageData.getFacets() != null);
		assertEquals(20, facetSearchPageData.getResults().size());
		assertEquals(6, facetSearchPageData.getSorts().size());

		for (final ProductData p : facetSearchPageData.getResults())
		{
			assertTrue("Each product should have a price", p.getPrice() != null);
			assertTrue("Each product should have a name", p.getName() != null);

		}

	}

	@Test
	public void testSearchRefine()
	{
		final HttpEntity<String> requestEntity = new HttpEntity<String>(getXMLHeaders());
		final ResponseEntity<FacetSearchPageData> response = template.exchange(URL + "?query={query}", HttpMethod.GET,
				requestEntity, FacetSearchPageData.class, "a");

		final FacetSearchPageData<SolrSearchQueryData, ProductData> facetSearchPageData = response.getBody();

		final FacetData<SolrSearchQueryData> facet = facetSearchPageData.getFacets().get(0);
		assertEquals("Category", facet.getName());
		assertEquals(21, facet.getValues().size());
		final FacetValueData<SolrSearchQueryData> value = facet.getValues().get(0);
		assertEquals("Battery Chargers", value.getName());
		assertEquals(4l, value.getCount());

		final ResponseEntity<FacetSearchPageData> response2 = template.exchange(URL + "?query={query}", HttpMethod.GET,
				requestEntity, FacetSearchPageData.class, "a:relevance:brand:brand_10");
		assertTrue(response2 != null);

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
