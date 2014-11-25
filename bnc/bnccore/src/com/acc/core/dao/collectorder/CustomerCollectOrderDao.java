/**
 * 
 */
package com.acc.core.dao.collectorder;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import com.acc.core.model.CollectOrderModel;


/**
 * @author swarnima.gupta
 * 
 */
public interface CustomerCollectOrderDao
{
	public List<CollectOrderModel> getCollectOrders();

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

	/**
	 * @param fromDate
	 * @param toDate
	 * @param fromTime
	 * @param toTime
	 * @return List<CollectOrderModel>
	 */
	public List<CollectOrderModel> getCollectOrderByDateAndTime(String fromDate, String toDate, String fromTime, String toTime);

	/**
	 * @param status
	 * @return List<CollectOrderModel>
	 */
	public List<CollectOrderModel> getCollectOrdersByStatus(String status);

}
