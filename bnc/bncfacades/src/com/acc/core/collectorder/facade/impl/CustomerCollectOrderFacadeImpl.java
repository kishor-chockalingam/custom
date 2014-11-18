/**
 * 
 */
package com.acc.core.collectorder.facade.impl;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.acc.core.collectorder.facade.CustomerCollectOrderFacade;
import com.acc.core.model.CollectOrderModel;
import com.acc.core.services.collectorder.CustomerCollectOrderService;
import com.acc.facades.collectOrder.data.CollectOrderData;


/**
 * @author swarnima.gupta
 * 
 */
public class CustomerCollectOrderFacadeImpl implements CustomerCollectOrderFacade
{

	private CustomerCollectOrderService customerCollectOrderService;

	@Resource(name = "collectOrderConverter")
	private Converter<CollectOrderModel, CollectOrderData> collectOrderConverter;

	@Resource(name = "orderConverter")
	private Converter<OrderModel, OrderData> orderConverter;

	/**
	 * @param customerCollectOrderService
	 *           the customerCollectOrderService to set
	 */
	public void setCustomerCollectOrderService(final CustomerCollectOrderService customerCollectOrderService)
	{
		this.customerCollectOrderService = customerCollectOrderService;
	}

	protected <S, T> SearchPageData<T> convertPageData(final SearchPageData<S> source, final Converter<S, T> converter)
	{
		final SearchPageData<T> result = new SearchPageData<T>();
		result.setPagination(source.getPagination());
		result.setSorts(source.getSorts());
		result.setResults(Converters.convertAll(source.getResults(), converter));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.collectorder.facade.CustomerCollectOrderFacade#getCollectOrders()
	 */
	@Override
	public SearchPageData<CollectOrderData> getCollectOrders(final PageableData pageableData)

	{
		final SearchPageData<CollectOrderModel> collectOrderModelsList = customerCollectOrderService.getCollectOrders(pageableData);
		return convertPageData(collectOrderModelsList, collectOrderConverter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.collectorder.facade.CustomerCollectOrderFacade#getCollectOrderByUCOID(java.lang.String)
	 */
	@Override
	public CollectOrderData getCollectOrderByUCOID(final String ucoid)
	{
		return collectOrderConverter.convert(customerCollectOrderService.getCollectOrderByUCOID(ucoid));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.collectorder.facade.CustomerCollectOrderFacade#getCollectOrderByOrderCode(java.lang.String)
	 */
	@Override
	public CollectOrderData getCollectOrderByOrderCode(final String orderCode)
	{
		return collectOrderConverter.convert(customerCollectOrderService.getCollectOrderByOrderCode(orderCode));
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.collectorder.facade.CustomerCollectOrderFacade#getCustomerListOrders(java.lang.String)
	 */
	@Override
	public List<CollectOrderData> getCustomerListOrders(final String customerID)
	{
		final List<CollectOrderModel> customerOrdersList = customerCollectOrderService.getCustomerListOrders(customerID);
		return convert(customerOrdersList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.acc.core.collectorder.facade.CustomerCollectOrderFacade#updateCollectOrder(com.acc.facades.collectOrder.data
	 * .CollectOrderData)
	 */
	@Override
	public void updateCollectOrder(final CollectOrderData collectOrderData)
	{
		customerCollectOrderService.updateCollectOrder(collectOrderData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.collectorder.facade.CustomerCollectOrderFacade#getOrderDetailsForCode(java.lang.String)
	 */
	@Override
	public OrderData getOrderDetailsForCode(final String orderCode)
	{
		return orderConverter.convert(customerCollectOrderService.getOrderDetailsForCode(orderCode));
	}

	/**
	 * @param collectOrderModelsList
	 * @return List<CollectOrderData>
	 */
	private List<CollectOrderData> convert(final List<CollectOrderModel> collectOrderModelsList)
	{
		final List<CollectOrderData> collectOrderDataList = new ArrayList<CollectOrderData>();
		if (CollectionUtils.isNotEmpty(collectOrderModelsList))
		{
			for (final CollectOrderModel collectOrderModel : collectOrderModelsList)
			{

				collectOrderDataList.add(collectOrderConverter.convert(collectOrderModel));
			}
		}
		return collectOrderDataList;
	}



}
