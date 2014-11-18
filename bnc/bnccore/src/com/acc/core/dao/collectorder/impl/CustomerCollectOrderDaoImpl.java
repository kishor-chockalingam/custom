/**
 * 
 */
package com.acc.core.dao.collectorder.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
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
	private PagedFlexibleSearchService pagedFlexibleSearchService;
	@Autowired
	private UserService userService;

	protected PagedFlexibleSearchService getPagedFlexibleSearchService()
	{
		return pagedFlexibleSearchService;
	}

	@Required
	public void setPagedFlexibleSearchService(final PagedFlexibleSearchService pagedFlexibleSearchService)
	{
		this.pagedFlexibleSearchService = pagedFlexibleSearchService;
	}

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
	public SearchPageData<CollectOrderModel> getCollectOrders(final PageableData pageableData)
	{

		FlexibleSearchQuery query = null;
		query = new FlexibleSearchQuery("SELECT {pk} from {collectOrder}");
		//final SearchResult<CollectOrderModel> result = flexibleSearchService.search(query);
		query.setUser(userService.getAdminUser());
		query.setNeedTotal(true);

		query.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());

		query.setCount(pageableData.getPageSize());


		return getPagedFlexibleSearchService().search(query, pageableData);
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
		final SearchResult<CollectOrderModel> result = flexibleSearchService.search(query);
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
		final SearchResult<OrderModel> result = flexibleSearchService.search(query);
		return CollectionUtils.isEmpty(result.getResult()) ? null : result.getResult().get(0);
	}

}
