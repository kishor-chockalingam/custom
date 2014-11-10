/**
 * 
 */
package com.acc.core.services.collectorder;

import com.acc.core.model.CollectOrderModel;


/**
 * @author prasun.a.kumar
 * 
 */
public interface CustomerCollectOrderService
{
	/**
	 * It is through this table the queue for order (that needs to be collected) will be managed by CSR agent, hence it
	 * needs to be updated every time with latest values.
	 * 
	 * @param collectOrderModel
	 *           Indicates Customer collect Order details.
	 */
	public void saveCustomerColectOrder(CollectOrderModel collectOrderModel);
}
