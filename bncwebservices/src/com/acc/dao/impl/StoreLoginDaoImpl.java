/**
 * 
 */
package com.acc.dao.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.acc.core.enums.CSRStoreStatus;
import com.acc.core.model.CSRCustomerDetailsModel;
import com.acc.dao.StoreLoginDao;


/**
 * @author prasun.a.kumar
 * 
 */
public class StoreLoginDaoImpl extends AbstractItemDao implements StoreLoginDao
{

	private static final Logger LOG = Logger.getLogger(StoreLoginDaoImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.dao.StoreLoginDao#isCustomerFound(java.lang.String)
	 */
	@Override
	public CustomerModel isCustomerFound(final String customerID)
	{

		CustomerModel customerModelResult = null;

		final FlexibleSearchQuery flexibleQuery = new FlexibleSearchQuery("select {pk} from {customer} where {uid} like '"
				+ customerID + "%'");

		final SearchResult<CustomerModel> result = getFlexibleSearchService().search(flexibleQuery);
		final List<CustomerModel> customerList = result.getResult();
		if (null != customerList && !customerList.isEmpty())
		{
			LOG.info("Customer UID :::::" + customerList.get(0));
			customerModelResult = customerList.get(0);
		}
		LOG.info("customerModelResult :::::" + customerModelResult);
		return customerModelResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.dao.StoreLoginDao#checkUserPresenceOnStore(de.hybris.platform.core.model.user.CustomerModel,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkUserPresenceOnStore(final CustomerModel customerModel, final String UUID, final String store)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String todayDate = sdf.format(new Date());
		final FlexibleSearchQuery flexibleQuery = new FlexibleSearchQuery(
				"Select {csrcd.pk} from {CSRCustomerDetails as csrcd JOIN CSRStoreStatus as csrss ON {csrss.pk}={csrcd.status}}"
						+ "where {csrss.code} in ('LoggedIn','InService') AND {csrcd.customerId}='" + customerModel.getUid()
						+ "' AND {csrcd.pointOfService}='" + store + "' AND {csrcd.UUID}='" + UUID
						+ "' AND {csrcd.creationtime} like '" + todayDate + "%'");

		final SearchResult<CSRCustomerDetailsModel> result = getFlexibleSearchService().search(flexibleQuery);

		final List<CSRCustomerDetailsModel> customerList = result.getResult();
		if (null != customerList && !customerList.isEmpty())
		{
			return true;
		}

		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.dao.StoreLoginDao#checkUserPresenceOnCSRDetails(de.hybris.platform.core.model.user.CustomerModel,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public CSRCustomerDetailsModel checkUserPresenceOnCSRDetails(final CustomerModel customerModel, final String uuID,
			final String storeID)
	{

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String todayDate = sdf.format(new Date());
		final FlexibleSearchQuery flexibleQuery = new FlexibleSearchQuery(
				"Select {csrcd.pk} from {CSRCustomerDetails as csrcd JOIN CSRStoreStatus as csrss ON {csrss.pk}={csrcd.status}}"
						+ "where {csrss.code} in ('LoggedIn','InService') AND {csrcd.customerId}='" + customerModel.getUid()
						+ "' AND {csrcd.creationtime} like '" + todayDate + "%'");

		final SearchResult<CSRCustomerDetailsModel> result = getFlexibleSearchService().search(flexibleQuery);

		final List<CSRCustomerDetailsModel> customerList = result.getResult();
		if (null != customerList && !customerList.isEmpty())
		{
			return customerList.get(0);
		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.dao.StoreLoginDao#saveCSRDetails(com.acc.core.model.CSRCustomerDetailsModel,
	 * de.hybris.platform.core.model.user.CustomerModel, java.lang.String, java.lang.String)
	 */
	@Override
	public void saveCSRDetails(final CSRCustomerDetailsModel csrModel, final CustomerModel customerModel, final String storeID,
			final String uuID)
	{
		csrModel.setCustomerId(customerModel.getUid());
		csrModel.setCustomerName(customerModel.getName());
		csrModel.setLoginTime(new Date());
		csrModel.setPointOfService(storeID);
		csrModel.setStatus(CSRStoreStatus.LOGGEDIN);
		csrModel.setUUID(uuID);
		getModelService().save(csrModel);
		LOG.info(":::::::::::::customer details saved successfully " + customerModel.getUid());
	}

}
