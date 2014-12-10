/**
 * 
 */
package com.acc.facades.storecustomer.impl;

import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.acc.core.enums.CSRStoreStatus;
import com.acc.core.model.CSRCustomerDetailsModel;
import com.acc.core.service.storecustomer.StoreCustomerService;
import com.acc.facades.CSRCustomerDetails.data.CSRCustomerDetailsData;
import com.acc.facades.storecustomer.StoreCustomerFacade;


/**
 * @author prasun.a.kumar
 * 
 */
public class StoreCustomerFacadeImpl implements StoreCustomerFacade
{
	private static final Logger LOG = Logger.getLogger(StoreCustomerFacadeImpl.class);

	@Autowired
	private StoreCustomerService storeCustomerService;



	@Resource(name = "csrCustomerDetailsConverter")
	private Converter<CSRCustomerDetailsModel, CSRCustomerDetailsData> csrCustomerDetailsConverter;

	/**
	 * @return the storeCustomerService
	 */
	public StoreCustomerService getStoreCustomerService()
	{
		return storeCustomerService;
	}

	/**
	 * @param storeCustomerService
	 *           the storeCustomerService to set
	 */
	public void setStoreCustomerService(final StoreCustomerService storeCustomerService)
	{
		this.storeCustomerService = storeCustomerService;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.facades.storecustomer.StoreCustomerFacade#getCSRCustomerDetails()
	 */
	@Override
	public List<CSRCustomerDetailsModel> getCSRCustomerDetails()
	{
		LOG.info("Calling getCSRCustomerDetails from facade");
		return storeCustomerService.getCSRCustomerDetails();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.facades.storecustomer.StoreCustomerFacade#updateCSRCustomerDetail(java.lang.String, java.lang.String)
	 */
	@Override
	public CSRCustomerDetailsModel updateCSRCustomerDetail(final String csrUID, final String customerUID, final String status)
	{
		LOG.info("Calling updateCSRCustomerDetail from facade with CSR ID [" + csrUID + "], And Customer ID [" + customerUID + "].");
		return storeCustomerService.updateCSRCustomerDetail(csrUID, customerUID, status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.acc.facades.storecustomer.StoreCustomerFacade#getCSRCustomerDetailsByStatus(com.acc.core.enums.CSRStoreStatus)
	 */
	@Override
	public List<CSRCustomerDetailsModel> getCSRCustomerDetailsByStatus(final CSRStoreStatus status)
	{
		return storeCustomerService.getCSRCustomerDetailsByStatus(status);
	}


	private List<CSRCustomerDetailsData> convert(final List<CSRCustomerDetailsModel> CSRCustomerDetailsList)
	{
		final List<CSRCustomerDetailsData> CSRCustomerDetailsData = new ArrayList<CSRCustomerDetailsData>();

		if (CollectionUtils.isNotEmpty(CSRCustomerDetailsList))
		{
			for (final CSRCustomerDetailsModel customerDetailsModel : CSRCustomerDetailsList)
			{
				CSRCustomerDetailsData.add(csrCustomerDetailsConverter.convert(customerDetailsModel));

			}
		}
		return CSRCustomerDetailsData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.collectorder.facade.CustomerCollectOrderFacade#getCollectOrderByCustomerName(java.lang.String)
	 */
	@Override
	public List<CSRCustomerDetailsData> getCollectOrderByCustomerName(final String customerName)
	{
		final List<CSRCustomerDetailsModel> CSRCustomerDetails = storeCustomerService.getCollectOrderByCustomerName(customerName);
		return convert(CSRCustomerDetails);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.facades.storecustomer.StoreCustomerFacade#getCustomerDetailsByDateAndTime(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<CSRCustomerDetailsData> getCustomerDetailsByDateAndTime(final String fromDate, final String toDate,
			final String fromTime, final String toTime)
	{
		return convert(storeCustomerService.getCustomerDetailsByDateAndTime(fromDate, toDate, fromTime, toTime));
	}

}
