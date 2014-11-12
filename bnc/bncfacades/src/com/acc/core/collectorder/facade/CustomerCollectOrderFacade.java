/**
 * 
 */
package com.acc.core.collectorder.facade;

import de.hybris.platform.commercefacades.order.data.OrderData;

import java.util.List;

import com.acc.facades.collectOrder.data.CollectOrderData;


/**
 * @author swarnima.gupta
 * 
 */
public interface CustomerCollectOrderFacade
{
	public List<CollectOrderData> getCollectOrders();

	/**
	 * @param ucoid
	 * @return CollectOrderData
	 */
	public CollectOrderData getCollectOrderByUCOID(String ucoid);

	/**
	 * @param orderCode
	 * @return CollectOrderData
	 */
	public CollectOrderData getCollectOrderByOrderCode(String orderCode);

	/**
	 * 
	 * @param customerID
	 * @return
	 */
	public List<CollectOrderData> getCustomerListOrders(final String customerID);

	/**
	 * @param collectOrderData
	 */
	public void updateCollectOrder(CollectOrderData collectOrderData);

	/**
	 * @param orderCode
	 * @return OrderData
	 */
	public OrderData getOrderDetailsForCode(String orderCode);

}
