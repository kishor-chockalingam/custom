package com.acc.services.impl;

/**
 * 
 */


import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.acc.core.enums.CSRStoreStatus;
import com.acc.core.model.CSRCustomerDetailsModel;
import com.acc.services.StoreLoginService;


/**
 * @author surendra.a.singh
 * 
 */
public class StoreLoginServiceImpl implements StoreLoginService
{
	private static final Logger LOG = Logger.getLogger(StoreLoginServiceImpl.class);

	@Autowired
	private ModelService modelService;

	@Autowired
	private UserService userService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.ycommercewebservices.services.StoreLoginService#saveCustomerLoginDetails(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String saveCustomerLoginDetails(final String uuID, final String storeID, final String customerID)
	{
		String finalResult = "success";

		try
		{
			final CustomerModel customerModel = isCustomerFound(uuID, customerID);
			if (null != customerModel)
			{
				if (!checkUserPresenceOnStore(customerModel, uuID, storeID))
				{

					final PointOfServiceModel model = new PointOfServiceModel();
					model.setName(storeID);
					final PointOfServiceModel posModel = flexibleSearchService.getModelByExample(model);

					if (null != customerModel && null != posModel)
					{
						final CSRCustomerDetailsModel csrmodel = modelService.create(CSRCustomerDetailsModel.class);
						csrmodel.setCustomerId(customerModel.getUid());
						csrmodel.setCustomerName(customerModel.getName());
						csrmodel.setLoginTime(new Date());
						csrmodel.setPointOfService(storeID);
						csrmodel.setStatus(CSRStoreStatus.LOGGEDIN);
						modelService.save(csrmodel);
						LOG.info(":::::::::::::customer details saved successfully " + customerModel.getUid());
					}
				}
				else
				{
					finalResult = "User Record already exists for this store.";
				}
			}
			else
			{
				finalResult = "Please enter correct information.";
			}
		}

		catch (final ModelNotFoundException e)
		{
			finalResult = "Please enter correct UUID and Store name.";
		}
		catch (final ModelSavingException ex)
		{
			finalResult = "Error while saving the record model.";
		}
		catch (final Exception e)
		{
			finalResult = "Error while saving the records.";
		}
		return finalResult;
	}

	/**
	 * @param uuID
	 * @return
	 */
	private CustomerModel isCustomerFound(final String uuID, final String customerID)
	{

		CustomerModel customerModelResult = null;
		final FlexibleSearchQuery flexibleQuery = new FlexibleSearchQuery("select {pk} from {customer} where {uuid}='" + uuID
				+ "' OR {uid} like '" + customerID + "%'");

		final SearchResult<CustomerModel> result = flexibleSearchService.search(flexibleQuery);
		final List<CustomerModel> customerList = result.getResult();

		if (null != customerList && !customerList.isEmpty())
		{
			if (customerList.size() == 1)
			{
				customerModelResult = customerList.get(0);
			}
			else
			{
				for (final CustomerModel customerModel : customerList)
				{
					if (uuID.equals(customerModel.getUUID()) && customerID.equals(customerModel.getUid()))
					{
						customerModelResult = customerModel;
					}
				}
			}
		}
		return customerModelResult;
	}

	private boolean checkUserPresenceOnStore(final CustomerModel customerModel, final String UUID, final String store)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final String todayDate = sdf.format(new Date());
		final FlexibleSearchQuery flexibleQuery = new FlexibleSearchQuery(
				"Select {csrcd.pk} from {CSRCustomerDetails as csrcd JOIN CSRStoreStatus as csrss ON {csrss.pk}={csrcd.status}}"
						+ "where {csrss.code} in ('LoggedIn','InService') AND {csrcd.customerId}='" + customerModel.getUid()
						+ "' AND {csrcd.pointOfService}='" + store + "' AND {csrcd.creationtime} like '" + todayDate + "%'");

		final SearchResult<CSRCustomerDetailsModel> result = flexibleSearchService.search(flexibleQuery);

		final List<CSRCustomerDetailsModel> customerList = result.getResult();
		if (null != customerList && !customerList.isEmpty())
		{
			return true;
		}

		return false;
	}
}
