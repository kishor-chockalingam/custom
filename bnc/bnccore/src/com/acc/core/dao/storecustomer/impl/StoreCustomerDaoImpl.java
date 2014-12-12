/**
 * 
 */
package com.acc.core.dao.storecustomer.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.util.CollectionUtils;

import com.acc.core.dao.storecustomer.StoreCustomerDao;
import com.acc.core.model.CSRCustomerDetailsModel;


/**
 * @author swapnil.a.pandey
 * 
 */
public class StoreCustomerDaoImpl extends AbstractItemDao implements StoreCustomerDao
{
	private static final Logger LOG = Logger.getLogger(StoreCustomerDaoImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.storecustomer.StoreCustomerDao#getCollectOrderByCustomerName(java.lang.String)
	 */
	@Override
	public List<CSRCustomerDetailsModel> getCollectOrderByCustomerName(final String customerName)
	{

		final String query = "SELECT {pk} from {CSRCustomerDetails} WHERE {customerName} LIKE '%" + customerName + "%'";
		final SearchResult<CSRCustomerDetailsModel> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.storecustomer.StoreCustomerDao#getCustomerDetailsByDateAndTime(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<CSRCustomerDetailsModel> getCustomerDetailsByDateAndTime(final String fromDate, final String toDate,
			final String fromTime, final String toTime)
	{


		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");
		String fDate = null;
		String tDate = null;
		SearchResult<CSRCustomerDetailsModel> result = null;
		try
		{


			fDate = sdf.format(sdf1.parse(fromDate + " " + fromTime));
			tDate = sdf.format(sdf1.parse(toDate + " " + toTime));
			final DateTime fromDateTime = new DateTime(sdf1.parse(fromDate + " " + fromTime));
			final DateTime toDateTime = new DateTime(sdf1.parse(toDate + " " + toTime));
			if (toDateTime.isBefore(fromDateTime))
			{
				throw new Exception("From Date should be before To Date!!");
			}
			final String query = "SELECT {pk} from {CSRCustomerDetails} WHERE {creationtime} BETWEEN '" + fDate + "' AND '" + tDate
					+ "'";
			result = getFlexibleSearchService().search(query);
		}
		catch (final ParseException e)
		{
			LOG.error(e.getMessage(), e);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return result == null ? null : result.getResult();
	}


}
