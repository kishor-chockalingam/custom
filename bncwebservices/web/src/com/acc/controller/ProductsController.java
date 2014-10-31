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
package com.acc.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.catalog.CatalogFacade;
import de.hybris.platform.commercefacades.product.ProductExportFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.*;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commercefacades.storefinder.StoreFinderStockFacade;
import de.hybris.platform.commercefacades.storefinder.data.StoreFinderStockSearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import com.acc.constants.YcommercewebservicesConstants;
import com.acc.expressupdate.data.ProductExpressUpdateElementData;
import com.acc.expressupdate.data.ProductExpressUpdateElementDataList;
import com.acc.expressupdate.impl.ProductExpressUpdateQueue;
import com.acc.formatters.WsDateFormatter;
import com.acc.product.data.ProductDataList;
import com.acc.product.data.SuggestionDataList;
import com.acc.util.ws.SearchQueryCodec;
import com.acc.validator.CustomValidationException;
import com.acc.validator.ReviewDataValidator;


/**
 * Web Services Controller to expose the functionality of the {@link ProductFacade} and SearchFacade.
 */
@Controller
@RequestMapping(value = "/v1/{baseSiteId}/products")
public class ProductsController extends BaseController
{
	private static final String BASIC_OPTION = "BASIC";
	private static final String MAX_INTEGER = "2147483647";
	private static final String DEFAULT_PAGE_VALUE = "0";
	private static final int CATALOG_ID_POS = 0;
	private static final int CATALOG_VERSION_POS = 1;
	private static final Logger LOG = Logger.getLogger(ProductsController.class);
	@Resource
	StoreFinderStockFacade storeFinderStockFacade;
	@Resource(name = "cwsProductFacade")
	private ProductFacade productFacade;
	@Resource(name = "cwsProductExportFacade")
	private ProductExportFacade productExportFacade;
	@Resource(name = "cwsSearchQueryCodec")
	private SearchQueryCodec<SolrSearchQueryData> searchQueryCodec;
	@Resource(name = "wsDateFormatter")
	private WsDateFormatter wsDateFormatter;
	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;
	@Resource(name = "solrSearchStateConverter")
	private Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter;
	@Resource(name = "httpRequestReviewDataPopulator")
	private Populator<HttpServletRequest, ReviewData> httpRequestReviewDataPopulator;
	@Resource(name = "reviewValidator")
	private Validator reviewValidator;
	@Resource(name = "productExpressUpdateQueue")
	private ProductExpressUpdateQueue productExpressUpdateQueue;
	@Resource(name = "catalogFacade")
	private CatalogFacade catalogFacade;

	/**
	 * Web service handler for search. Implementation has to catch up once the SearchFacade exists.
	 * 
	 * @param query
	 *           serialized query in format: freeTextSearch:sort:facetKey1:facetValue1:facetKey2:facetValue2
	 * @param currentPage
	 *           the current result page requested
	 * @param pageSize
	 *           the number of results returned per page
	 * @param sort
	 *           sorting method applied to the display search results
	 * @return {@link FacetSearchPageData}
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ProductSearchPageData<SearchStateData, ProductData> searchProducts(@RequestParam(required = false) final String query,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_VALUE) final int currentPage,
			@RequestParam(required = false, defaultValue = "20") final int pageSize,
			@RequestParam(required = false) final String sort)
	{
		final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
		final PageableData pageable = new PageableData();
		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		pageable.setSort(sort);

		return productSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
	}

	/**
	 * Web service handler for product export. If no 'options' query parameter is defined, it will assume BASIC. The
	 * options are turned into a Set<ProductOption> and passed on to the facade. <br>
	 * Sample Call: http://localhost:9001/rest/v1/{SITE}/products/export/full
	 * 
	 * @param currentPage
	 *           - index position of the first Product, which will be included in the returned List
	 * @param pageSize
	 *           - number of Products which will be returned in each page
	 * @param options
	 *           - a String enumerating the detail level, values are BASIC, PROMOTIONS, STOCK, REVIEW, CLASSIFICATION,
	 *           REFERENCES. Combine by using a ',', which needs to be encoded as part of a URI using URLEncoding: %2C
	 * @return {@link ProductDataList}
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/export/full", method = RequestMethod.GET)
	@ResponseBody
	public ProductDataList exportProducts(
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_VALUE) final int currentPage,
			@RequestParam(required = false, defaultValue = MAX_INTEGER) final int pageSize,
			@RequestParam(required = false, defaultValue = BASIC_OPTION) final String options,
			@RequestParam(required = false) final String catalog, @RequestParam(required = false) final String version)
	{
		final Set<ProductOption> opts = extractOptions(options);

		final ProductResultData products = productExportFacade.getAllProductsForOptions(catalog, version, opts, currentPage,
				pageSize);

		//addUrlsToResult(catalog, version, products);
		final ProductDataList result = convertResultset(currentPage, pageSize, catalog, version, products);

		return result;
	}

	/**
	 * Web service handler for incremental product export. Timestamp specifies which product to export. If no 'options'
	 * query parameter is defined, it will assume BASIC. The options are turned into a Set<ProductOption> and passed on
	 * to the facade. <br>
	 * Sample Call: http://localhost:9001/rest/v1/{SITE}/products/export/incremental
	 * 
	 * @param currentPage
	 *           - index position of the first Product, which will be included in the returned List
	 * @param pageSize
	 *           - number of Products which will be returned in each page
	 * @param options
	 *           - a String enumerating the detail level, values are BASIC, PROMOTIONS, STOCK, REVIEW, CLASSIFICATION,
	 *           REFERENCES. Combine by using a ',', which needs to be encoded as part of a URI using URLEncoding: %2C
	 * @param catalog
	 *           catalog from which get products
	 * @param version
	 *           version of catalog
	 * @param timestamp
	 *           time in ISO-8601 format
	 * @return {@link ProductDataList}
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/export/incremental", method = RequestMethod.GET)
	@ResponseBody
	public ProductDataList exportProducts(
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_VALUE) final int currentPage,
			@RequestParam(required = false, defaultValue = MAX_INTEGER) final int pageSize,
			@RequestParam(required = false, defaultValue = BASIC_OPTION) final String options,
			@RequestParam(required = false) final String catalog, @RequestParam(required = false) final String version,
			@RequestParam final String timestamp) throws DateParseException
	{
		final Set<ProductOption> opts = extractOptions(options);
		final Date timestampDate = wsDateFormatter.toDate(timestamp);

		final ProductResultData modifiedProducts = productExportFacade.getOnlyModifiedProductsForOptions(catalog, version,
				timestampDate, opts, currentPage, pageSize);

		return convertResultset(currentPage, pageSize, catalog, version, modifiedProducts);
	}

	/**
	 * Web service handler for export product references. Reference type specifies which references to return. If no
	 * 'options' query parameter is defined, it will assume BASIC. The options are turned into a Set<ProductOption> and
	 * passed on to the facade. Sample Call:
	 * http://localhost:9001/rest/v1/{SITE}/products/export/references/{code}?referenceType
	 * =CROSSELLING&catalog=hwcatalog&version=Online
	 * 
	 * @param code
	 *           - product code
	 * @param referenceType
	 *           - reference type according to enum ProductReferenceTypeEnum
	 * @param pageSize
	 *           - number of Products which will be returned in each page
	 * @param options
	 *           - a String enumerating the detail level, values are BASIC, PROMOTIONS, STOCK, REVIEW, CLASSIFICATION,
	 *           REFERENCES. Combine by using a ',', which needs to be encoded as part of a URI using URLEncoding: %2C
	 * @return collection of {@link ProductReferenceData}
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/export/references/{code}", method = RequestMethod.GET)
	@ResponseBody
	public ProductReferencesData exportProductReferences(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = MAX_INTEGER) final int pageSize,
			@RequestParam(required = false, defaultValue = BASIC_OPTION) final String options,
			@RequestParam final String referenceType) throws DateParseException
	{
		final List<ProductOption> opts = Lists.newArrayList(extractOptions(options));
		final ProductReferenceTypeEnum referenceTypeEnum = ProductReferenceTypeEnum.valueOf(referenceType);

		final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(code, referenceTypeEnum,
				opts, Integer.valueOf(pageSize));

		final ProductReferencesData productReferencesData = new ProductReferencesData();
		productReferencesData.setReferences(productReferences);

		return productReferencesData;
	}

	/**
	 * Web service handler for product express update. Returns only elements newer than timestamp. Sample Call:
	 * http://localhost:9001/rest/v1/{SITE}/products/expressUpdate<br>
	 * This method requires trusted client authentication.<br>
	 * Method type : <code>GET</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * @param timestamp
	 *           - time in ISO-8601 format
	 * @param catalog
	 *           - the product catalog to return queue for. If not set all products from all catalogs in queue will be
	 *           returned. Format: catalogId:catalogVersion
	 * @return {@link ProductExpressUpdateElementDataList}
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/expressUpdate", method = RequestMethod.GET)
	@ResponseBody
	public ProductExpressUpdateElementDataList expressUpdate(@RequestParam final String timestamp,
			@RequestParam(required = false) final String catalog)
	{
		final Date timestampDate = wsDateFormatter.toDate(timestamp);
		final ProductExpressUpdateElementDataList productExpressUpdateDataList = new ProductExpressUpdateElementDataList();
		productExpressUpdateDataList.setProductExpressUpdateElements(productExpressUpdateQueue.getItems(timestampDate));
		filterExpressUpdateQueue(productExpressUpdateDataList, validateAndSplitCatalog(catalog));
		return productExpressUpdateDataList;
	}

	private void filterExpressUpdateQueue(final ProductExpressUpdateElementDataList productExpressUpdateDataList,
			final List<String> catalogInfo)
	{
		if (catalogInfo.size() == 2 && StringUtils.isNotEmpty(catalogInfo.get(CATALOG_ID_POS))
				&& StringUtils.isNotEmpty(catalogInfo.get(CATALOG_VERSION_POS))
				&& CollectionUtils.isNotEmpty(productExpressUpdateDataList.getProductExpressUpdateElements()))
		{
			final Iterator<ProductExpressUpdateElementData> dataIterator = productExpressUpdateDataList
					.getProductExpressUpdateElements().iterator();
			while (dataIterator.hasNext())
			{
				final ProductExpressUpdateElementData productExpressUpdateElementData = dataIterator.next();
				if (!catalogInfo.get(CATALOG_ID_POS).equals(productExpressUpdateElementData.getCatalogId())
						|| !catalogInfo.get(CATALOG_VERSION_POS).equals(productExpressUpdateElementData.getCatalogVersion()))
				{
					dataIterator.remove();
				}
			}
		}
	}

	protected List<String> validateAndSplitCatalog(final String catalog) throws IllegalArgumentException
	{
		final List<String> catalogInfo = new ArrayList<>();
		if (StringUtils.isNotEmpty(catalog))
		{
			catalogInfo.addAll(Lists.newArrayList(Splitter.on(':').trimResults().omitEmptyStrings().split(catalog)));
			if (catalogInfo.size() == 2)
			{
				catalogFacade.getProductCatalogVersionForTheCurrentSite(catalogInfo.get(CATALOG_ID_POS),
						catalogInfo.get(CATALOG_VERSION_POS), Collections.EMPTY_SET);
			}
			else if (!catalogInfo.isEmpty())
			{
				throw new IllegalArgumentException("You have to provide both catalog and catalogVersion parameters or none of them.");
			}
		}
		return catalogInfo;
	}

	private ProductDataList convertResultset(final int page, final int pageSize, final String catalog, final String version,
			final ProductResultData modifiedProducts)
	{
		final ProductDataList result = new ProductDataList();
		result.setProducts(modifiedProducts.getProducts());
		if (pageSize > 0)
		{
			result.setTotalPageCount((modifiedProducts.getTotalCount() % pageSize == 0) ? modifiedProducts.getTotalCount()
					/ pageSize : modifiedProducts.getTotalCount() / pageSize + 1);
		}
		result.setCurrentPage(page);
		result.setTotalProductCount(modifiedProducts.getTotalCount());
		result.setCatalog(catalog);
		result.setVersion(version);
		return result;
	}

	protected Set<ProductOption> extractOptions(final String options)
	{
		final String optionsStrings[] = options.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<ProductOption> opts = new HashSet<ProductOption>();
		for (final String option : optionsStrings)
		{
			opts.add(ProductOption.valueOf(option));
		}
		return opts;
	}

	/**
	 * Web service handler for the getProductByCode call. If no 'options' query parameter is defined, it will assume
	 * BASIC. The options are turned into a Set<ProductOption> and passed on to the facade. Sample Call:
	 * http://localhost:9001/rest/v1/{SITE}/products/{CODE}?options=BASIC%2CPROMOTIONS Keep in mind ',' needs to be
	 * encoded as %2C
	 * 
	 * @param code
	 *           - the unique code used to identify a product
	 * @param options
	 *           - a String enumerating the detail level, values are BASIC, PROMOTIONS, STOCK, REVIEW, CLASSIFICATION,
	 *           REFERENCES. Combine by using a ',', which needs to be encoded as part of a URI using URLEncoding: %2C
	 * @return the ProdcutData DTO which will be marshaled to JSON or XML based on Accept-Header
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	@ResponseBody
	public ProductData getProductByCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = BASIC_OPTION) final String options)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductByCode: code=" + code + " | options=" + options);
		}

		final Set<ProductOption> opts = extractOptions(options);

		return productFacade.getProductForCodeAndOptions(code, opts);
	}

	/**
	 * Web service handler for giving the auto complete suggestions as List<String>
	 * 
	 * @param term
	 *           - the term that user inputs for search
	 * @param max
	 *           - the limit of the suggestions
	 * @return the list of auto suggestions
	 */
	@RequestMapping(value = "/suggest", method = RequestMethod.GET)
	@ResponseBody
	public SuggestionDataList getSuggestions(@RequestParam(required = true, defaultValue = " ") final String term,
			@RequestParam(required = true, defaultValue = "10") final int max)
	{
		final List<SuggestionData> suggestions = new ArrayList<SuggestionData>();
		final List<AutocompleteSuggestionData> autoSuggestions;
		if (max < productSearchFacade.getAutocompleteSuggestions(term).size())
		{
			autoSuggestions = productSearchFacade.getAutocompleteSuggestions(term).subList(0, max);
		}
		else
		{
			autoSuggestions = productSearchFacade.getAutocompleteSuggestions(term);
		}
		for (final AutocompleteSuggestionData autoSuggestion : autoSuggestions)
		{
			final SuggestionData suggestionData = new SuggestionData();
			suggestionData.setValue(autoSuggestion.getTerm());
			suggestions.add(suggestionData);
		}
		final SuggestionDataList suggestionDataList = new SuggestionDataList();
		suggestionDataList.setSuggestions(suggestions);
		return suggestionDataList;
	}

	/**
	 * Web service handler for the postReview call. Review will be posted as anonymous principal. Method uses
	 * {@link com.acc.populator.HttpRequestReviewDataPopulator} to populate review data
	 * from request parameters.
	 * <p/>
	 * There is no default validation for the posted value!
	 * <p/>
	 * Request Method: <code>POST<code>
	 * Sample Call: http://localhost:9001/rest/v1/{SITE}/products/{CODE}/review
	 * Request parameters:
	 * <ul>
	 * <li>rating (required)</li>
	 * <li>headline</li>
	 * <li>comment</li>
	 * <li>alias</li>
	 * </ul>
	 * 
	 * @param code
	 *           - the unique code used to identify a product
	 * @param request
	 * @return the ReviewData DTO which will be marshaled to JSON or XML based on Accept-Header
	 */
	@RequestMapping(value = "/{code}/reviews", method = RequestMethod.POST)
	@ResponseBody
	public ReviewData createReview(@PathVariable final String code, final HttpServletRequest request)
			throws CustomValidationException
	{
		final ReviewData reviewData = new ReviewData();
		httpRequestReviewDataPopulator.populate(request, reviewData);
		final Errors errors = new BeanPropertyBindingResult(reviewData, "reviewData");

		reviewValidator.validate(reviewData, errors);

		if (errors.hasErrors())
		{
			throw new CustomValidationException("Review validation error", errors);
		}

		return productFacade.postReview(code, reviewData);
	}

	/**
	 * Web service handler for searching product's stock level sorted by distance from specific location passed by the
	 * free-text parameter. Sample Call: http://localhost:9001/rest/v1/{SITE}/products/{CODE}/nearLocation
	 * 
	 * @param code
	 *           - the unique code used to identify a product
	 * @param location
	 *           - free-text location
	 * @return the StoreFinderStockSearchPageData of ProductData objects sorted by distance from location ascending
	 */
	@RequestMapping(value = "/{code}/nearLocation", method = RequestMethod.GET)
	@ResponseBody
	public StoreFinderStockSearchPageData<ProductData> searchProductStockByLocation(@PathVariable final String code,
			@RequestParam(required = true) final String location,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_VALUE) final int currentPage,
			@RequestParam(required = false, defaultValue = MAX_INTEGER) final int pageSize)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductStockByLocation: code=" + code + " | location=" + location);
		}

		final Set<ProductOption> opts = extractOptions(BASIC_OPTION);

		return this.storeFinderStockFacade.productSearch(location, productFacade.getProductForCodeAndOptions(code, opts),
				createPageableData(currentPage, pageSize));

	}

	/**
	 * Web service handler for searching product's stock level sorted by distance from specific location passed by the
	 * free-text parameter. Sample Call: http://localhost:9001/rest/v1/{SITE}/products/{CODE}/nearLatLong
	 * 
	 * @param code
	 *           - the unique code used to identify a product
	 * @param latitude
	 *           - location's latitude
	 * @param longitude
	 *           - location's longitude
	 * @return the StoreFinderStockSearchPageData of ProductData objects sorted by distance from location ascending
	 */
	@RequestMapping(value = "/{code}/nearLatLong", method = RequestMethod.GET)
	@ResponseBody
	public StoreFinderStockSearchPageData<ProductData> searchProductStockByLocationGeoCode(@PathVariable final String code,
			@RequestParam(required = true) final Double latitude, @RequestParam(required = true) final Double longitude,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_VALUE) final int currentPage,
			@RequestParam(required = false, defaultValue = MAX_INTEGER) final int pageSize)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getProductStockByLocationGeoCode: code=" + code + " | latitude=" + latitude + " | longitude=" + longitude);
		}
		final Set<ProductOption> opts = extractOptions(BASIC_OPTION);

		return this.storeFinderStockFacade.productSearch(createGeoPoint(latitude, longitude),
				productFacade.getProductForCodeAndOptions(code, opts), createPageableData(currentPage, pageSize));
	}

	private PageableData createPageableData(final int currentPage, final int pageSize)
	{
		final PageableData pageable = new PageableData();

		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		return pageable;
	}

	private GeoPoint createGeoPoint(final Double latitude, final Double longitude)
	{
		final GeoPoint point = new GeoPoint();
		point.setLatitude(latitude);
		point.setLongitude(longitude);

		return point;
	}

	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	public void setSearchQueryCodec(final SearchQueryCodec<SolrSearchQueryData> searchQueryCodec)
	{
		this.searchQueryCodec = searchQueryCodec;
	}

	public void setProductExportFacade(final ProductExportFacade productExportFacade)
	{
		this.productExportFacade = productExportFacade;
	}

	public void setProductSearchFacade(final ProductSearchFacade<ProductData> productSearchFacade)
	{
		this.productSearchFacade = productSearchFacade;
	}

	public void setWsDateFormatter(final WsDateFormatter wsDateFormatter)
	{
		this.wsDateFormatter = wsDateFormatter;
	}

	public void setReviewValidator(final ReviewDataValidator reviewValidator)
	{
		this.reviewValidator = reviewValidator;
	}

	public void setCatalogFacade(final CatalogFacade catalogFacade)
	{
		this.catalogFacade = catalogFacade;
	}
}
