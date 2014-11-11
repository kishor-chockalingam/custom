/**
 * 
 */
package com.acc.core.collectorder.facade.impl;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.collectorder.facade.CustomerCollectOrderFacade#getCollectOrders()
	 */
	@Override
	public List<CollectOrderData> getCollectOrders()
	{
		final List<CollectOrderModel> collectOrderModelsList = customerCollectOrderService.getCollectOrders();
		final List<CollectOrderData> collectOrderDataList = new ArrayList<CollectOrderData>();
		for (final CollectOrderModel collectOrderModel : collectOrderModelsList)
		{

			collectOrderDataList.add(collectOrderConverter.convert(collectOrderModel));
		}
		return collectOrderDataList;
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

}
