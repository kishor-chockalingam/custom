/**
 * 
 */
package com.acc.core.collectorder.facade;

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

}
