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

import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import com.acc.constants.YcommercewebservicesConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Web Service Controller for the ORDERS resource. All methods check orders of the current user. Methods require basic
 * authentication and are restricted to https channel.
 */
@Controller
@RequestMapping(value = "/v1/{baseSiteId}/orders")
public class OrderController extends BaseController
{

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	/**
	 * Web service for getting current user's order information by order code.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/orders/1234 <br>
	 * This method requires authentication.<br>
	 * Method type : <code>GET</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * @param code
	 *           - order code - must be given as path variable.
	 * 
	 * @return {@link OrderData} as response body.
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	@ResponseBody
	public OrderData getOrder(@PathVariable final String code)
	{
		return orderFacade.getOrderDetailsForCode(code);
	}

	/**
	 * Web service for getting current user's order history data.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/orders?statuses=COMPLETED,CANCELLED&pageSize=5&currentPage=0 <br>
	 * This method requires authentication.<br>
	 * Method type : <code>GET</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * @param statuses
	 *           - filter for order statuses- optional
	 * @param currentPage
	 *           - pagination parameter- optional
	 * @param pageSize
	 *           - {@link PaginationData} parameter - optional
	 * @param sort
	 *           - sort criterion
	 *
	 * @return {@link OrderData} as response body.
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public OrderHistoriesData getPagedOrdersForStatuses(@RequestParam(defaultValue = "all") final String statuses,
			@RequestParam(defaultValue = "-1") final int currentPage, @RequestParam(defaultValue = "-1") final int pageSize,
			@RequestParam(required = false) final String sort)
	{
		final PageableData pageableData = resolvePageableData(currentPage, pageSize, sort);

		if (pageableData == null)
		{
			final SearchPageData<OrderHistoryData> result = new SearchPageData<OrderHistoryData>();
			result.setResults(getAllOrdersForStatuses(statuses));
			return createOrderHistoriesData(result);
		}

		if ("all".equals(statuses))
		{
			return createOrderHistoriesData(orderFacade.getPagedOrderHistoryForStatuses(pageableData));
		}
		final Set<OrderStatus> statusSet = extractOrderStatuses(statuses);

		return createOrderHistoriesData(orderFacade.getPagedOrderHistoryForStatuses(pageableData,
				statusSet.toArray(new OrderStatus[statusSet.size()])));
	}

	protected List<OrderHistoryData> getAllOrdersForStatuses(final String statuses)
	{
		if ("all".equals(statuses))
		{
			return orderFacade.getOrderHistoryForStatuses();
		}
		final Set<OrderStatus> statusSet = extractOrderStatuses(statuses);
		return orderFacade.getOrderHistoryForStatuses(statusSet.toArray(new OrderStatus[statusSet.size()]));
	}

	protected PageableData resolvePageableData(final int currentPage, final int pageSize, final String sort)
	{
		if (pageSize == -1 || currentPage == -1)
		{
			return null;
		}
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(currentPage);
		pageableData.setPageSize(pageSize);
		pageableData.setSort(sort);
		return pageableData;
	}

	protected Set<OrderStatus> extractOrderStatuses(final String statuses)
	{
		final String statusesStrings[] = statuses.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<OrderStatus> statusesEnum = new HashSet<OrderStatus>();
		for (final String status : statusesStrings)
		{
			statusesEnum.add(OrderStatus.valueOf(status));
		}
		return statusesEnum;
	}

	protected OrderHistoriesData createOrderHistoriesData(final SearchPageData<OrderHistoryData> result)
	{
		final OrderHistoriesData orderHistoriesData = new OrderHistoriesData();

		orderHistoriesData.setOrders(result.getResults());
		orderHistoriesData.setSorts(result.getSorts());
		orderHistoriesData.setPagination(result.getPagination());

		return orderHistoriesData;
	}

}
