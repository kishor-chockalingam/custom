/**
 * 
 */
package com.acc.core.service.storecustomer.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.acc.core.enums.CSRStoreStatus;
import com.acc.core.model.CSRCustomerDetailsModel;
import com.acc.core.service.storecustomer.StoreCustomerService;


/**
 * @author prasun.a.kumar
 * 
 */
public class StoreCustomerServiceImpl implements StoreCustomerService
{
	private static final Logger LOG = Logger.getLogger(StoreCustomerServiceImpl.class);

	@Autowired
	private SessionService sessionService;
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.service.storecustomer.StoreCustomerService#getCSRCustomerDetails()
	 */
	@Override
	public List<CSRCustomerDetailsModel> getCSRCustomerDetails()
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String todayDate = sdf.format(new Date());

		final String pos = sessionService.getAttribute("POINT_OF_SERVICE");
		LOG.info("Point of service [" + pos + "].");
		final FlexibleSearchQuery flexibleQuery = new FlexibleSearchQuery(
				"Select {csrcd.pk} from {CSRCustomerDetails as csrcd JOIN CSRStoreStatus as csrss ON {csrss.pk}={csrcd.status}}"
						+ "where "
						//+"{csrss.code} not in ('Completed') AND"
						+ " {csrcd.pointOfService}='" + pos + "' AND {csrcd.creationtime} like '" + todayDate + "%'");

		final SearchResult<CSRCustomerDetailsModel> result = flexibleSearchService.search(flexibleQuery);

		final List<CSRCustomerDetailsModel> customerList = result.getResult();
		if (null != customerList && !customerList.isEmpty())
		{
			return customerList;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.service.storecustomer.StoreCustomerService#updateCSRCustomerDetail(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public CSRCustomerDetailsModel updateCSRCustomerDetail(final String csrUID, final String customerUID,
			final String customerStatus)
	{
		LOG.info("Customer status [ " + customerStatus + " ].");
		final CSRCustomerDetailsModel csrCustomerDetailsModel = modelService.get(PK.parse(customerUID));
		LOG.info("CustomerID :::::::::: [" + csrCustomerDetailsModel.getCustomerId() + "].");
		if (customerStatus.equalsIgnoreCase(CSRStoreStatus.COMPLETED.getCode()))
		{
			csrCustomerDetailsModel.setStatus(CSRStoreStatus.COMPLETED);
		}
		else if (customerStatus.equalsIgnoreCase(CSRStoreStatus.NOTHANKS.getCode()))
		{
			csrCustomerDetailsModel.setStatus(CSRStoreStatus.NOTHANKS);
		}
		else
		{
			csrCustomerDetailsModel.setStatus(CSRStoreStatus.INSERVICE);
		}

		csrCustomerDetailsModel.setProcessedBy(csrUID);
		modelService.save(csrCustomerDetailsModel);
		return csrCustomerDetailsModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.service.storecustomer.StoreCustomerService#getCSRCustomerDetailsByStatus(com.acc.core.enums.
	 * CSRStoreStatus)
	 */
	@Override
	public List<CSRCustomerDetailsModel> getCSRCustomerDetailsByStatus(final CSRStoreStatus status)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String todayDate = sdf.format(new Date());
		final String pos = sessionService.getAttribute("POINT_OF_SERVICE");
		LOG.info("Point of service [" + pos + "].");
		final FlexibleSearchQuery flexibleQuery = new FlexibleSearchQuery(
				"Select {csrcd.pk} from {CSRCustomerDetails as csrcd JOIN CSRStoreStatus as csrss ON {csrss.pk}={csrcd.status}}"
						+ "where {csrss.code} in ('" + status.getCode() + "') AND {csrcd.pointOfService}='" + pos
						+ "' AND {csrcd.creationtime} like '" + todayDate + "%'");
		final SearchResult<CSRCustomerDetailsModel> result = flexibleSearchService.search(flexibleQuery);
		final List<CSRCustomerDetailsModel> customerList = result.getResult();
		return CollectionUtils.isNotEmpty(customerList) ? customerList : null;
	}
}
