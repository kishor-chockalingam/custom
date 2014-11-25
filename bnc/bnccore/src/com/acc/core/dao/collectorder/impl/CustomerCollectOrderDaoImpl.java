/**
 * 
 */
package com.acc.core.dao.collectorder.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.acc.core.dao.collectorder.CustomerCollectOrderDao;
import com.acc.core.model.CollectOrderModel;
import com.acc.core.services.collectorder.impl.CustomerCollectOrderServiceImpl;


/**
 * @author swarnima.gupta
 * 
 */
public class CustomerCollectOrderDaoImpl extends AbstractItemDao implements CustomerCollectOrderDao
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CustomerCollectOrderServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.collectorder.CustomerCollectOrderDao#getCollectOrders()
	 */
	@Override
	public List<CollectOrderModel> getCollectOrders()
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String todayDate = sdf.format(new Date());
		final String query = "SELECT {pk} from {collectOrder} WHERE {creationtime} like '" + todayDate + "%'";
		final SearchResult<CollectOrderModel> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.collectorder.CustomerCollectOrderDao#getCollectOrderByUCOID(java.lang.String)
	 */
	@Override
	public CollectOrderModel getCollectOrderByUCOID(final String ucoid)
	{
		final String query = "SELECT {pk} from {collectOrder} where {ucoid}='" + ucoid + "'";
		final SearchResult<CollectOrderModel> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult().get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.collectorder.CustomerCollectOrderDao#getCollectOrderByOrderCode(java.lang.String)
	 */
	@Override
	public CollectOrderModel getCollectOrderByOrderCode(final String orderCode)
	{
		final String query = "SELECT {pk} from {collectOrder} where {oid}='" + orderCode + "'";
		final SearchResult<CollectOrderModel> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult().get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.collectorder.CustomerCollectOrderDao#getCustomerListOrders(java.lang.String)
	 */
	@Override
	public List<CollectOrderModel> getCustomerListOrders(final String customerID)
	{
		final String query = "SELECT {PK} from {collectOrder} where {cid}='" + customerID + "'";
		//+ "' AND {status} IN ('PENDING','COMPLETED')";
		final SearchResult<CollectOrderModel> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.collectorder.CustomerCollectOrderDao#getOrderDetailsForCode(java.lang.String)
	 */
	@Override
	public OrderModel getOrderDetailsForCode(final String orderCode)
	{
		final String query = "SELECT {pk} from {order} where {code}='" + orderCode + "'";
		final SearchResult<OrderModel> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult().get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.collectorder.CustomerCollectOrderDao#getCollectOrderByDateAndTime(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<CollectOrderModel> getCollectOrderByDateAndTime(final String fromDate, final String toDate, final String fromTime,
			final String toTime)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy hh:mm aa");
		String fDate = null;
		String tDate = null;
		try
		{
			fDate = sdf.format(sdf1.parse(fromDate + " " + fromTime));
			tDate = sdf.format(sdf1.parse(toDate + " " + toTime));
		}
		catch (final ParseException e)
		{
			e.printStackTrace();
		}

		final String query = "SELECT {pk} from {collectOrder} WHERE {creationtime} BETWEEN '" + fDate + "' AND '" + tDate + "'";
		final SearchResult<CollectOrderModel> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.collectorder.CustomerCollectOrderDao#getCollectOrdersByStatus(java.lang.String)
	 */
	@Override
	public List<CollectOrderModel> getCollectOrdersByStatus(final String status)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String todayDate = sdf.format(new Date());
		final String query = "SELECT {pk} from {collectOrder} WHERE {status} IN ('" + status + "') AND {creationtime} like '"
				+ todayDate + "%'";
		final SearchResult<CollectOrderModel> result = getFlexibleSearchService().search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult();
	}

}
