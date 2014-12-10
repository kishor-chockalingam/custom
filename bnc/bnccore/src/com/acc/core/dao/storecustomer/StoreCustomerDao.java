/**
 * 
 */
package com.acc.core.dao.storecustomer;

import java.util.List;

import com.acc.core.model.CSRCustomerDetailsModel;



/**
 * @author swapnil.a.pandey
 * 
 */
public interface StoreCustomerDao
{
	public List<CSRCustomerDetailsModel> getCollectOrderByCustomerName(String customerName);

	public List<CSRCustomerDetailsModel> getCustomerDetailsByDateAndTime(final String fromDate, final String toDate,
			final String fromTime, final String toTime);
}
