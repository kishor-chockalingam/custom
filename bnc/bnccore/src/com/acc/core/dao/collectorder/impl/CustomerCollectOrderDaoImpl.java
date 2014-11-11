/**
 * 
 */
package com.acc.core.dao.collectorder.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

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
public class CustomerCollectOrderDaoImpl implements CustomerCollectOrderDao
{

	private static final Logger LOG = Logger.getLogger(CustomerCollectOrderServiceImpl.class);

	private FlexibleSearchService flexibleSearchService;

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.dao.collectorder.CustomerCollectOrderDao#getCollectOrders()
	 */
	@Override
	public List<CollectOrderModel> getCollectOrders()
	{
		final String query = "SELECT {pk} from {collectOrder}";
		final SearchResult<CollectOrderModel> result = flexibleSearchService.search(query);
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
		final SearchResult<CollectOrderModel> result = flexibleSearchService.search(query);
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
		final SearchResult<CollectOrderModel> result = flexibleSearchService.search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult().get(0);
	}

}