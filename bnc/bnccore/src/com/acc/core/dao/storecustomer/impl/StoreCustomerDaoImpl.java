/**
 * 
 */
package com.acc.core.dao.storecustomer.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.acc.core.dao.storecustomer.StoreCustomerDao;
import com.acc.core.model.CSRCustomerDetailsModel;


/**
 * @author swapnil.a.pandey
 * 
 */
public class StoreCustomerDaoImpl extends AbstractItemDao implements StoreCustomerDao
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.storecustomer.StoreCustomerDao#getCollectOrderByCustomerName(java.lang.String)
	 */
	@Override
	public List<CSRCustomerDetailsModel> getCollectOrderByCustomerName(final String customerName)
	{

		final String query = "SELECT {pk} from {CSRCustomerDetails} WHERE {customerName}='" + customerName + "'";
		final SearchResult<CSRCustomerDetailsModel> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult();

	}

}
