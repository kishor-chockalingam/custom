/**
 * 
 */
package com.acc.core.dao.collectorder;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import com.acc.core.model.CollectOrderModel;


/**
 * @author swarnima.gupta
 * 
 */
public interface CustomerCollectOrderDao
{
	public SearchPageData<CollectOrderModel> getCollectOrders(final PageableData pageableData);

	/**
	 * @param ucoid
	 * @return CollectOrderModel
	 */
	public CollectOrderModel getCollectOrderByUCOID(String ucoid);

	/**
	 * @param orderCode
	 * @return CollectOrderModel
	 */
	public CollectOrderModel getCollectOrderByOrderCode(String orderCode);

	/**
	 * 
	 * @param customerID
	 * @return
	 */
	public List<CollectOrderModel> getCustomerListOrders(String customerID);

	/**
	 * @param orderCode
	 * @return OrderModel
	 */
	public OrderModel getOrderDetailsForCode(String orderCode);

}
