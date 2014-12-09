/**
 * 
 */
package com.acc.core.dao.storecustomer;

import com.acc.core.model.CSRCustomerDetailsModel;



/**
 * @author swapnil.a.pandey
 * 
 */
public interface StoreCustomerDao
{
	public CSRCustomerDetailsModel getCollectOrderByCustomerName(String customerName);
}
