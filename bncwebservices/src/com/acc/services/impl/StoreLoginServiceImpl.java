package com.acc.services.impl;

/**
 * 
 */


import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.acc.core.model.CSRCustomerDetailsModel;
import com.acc.dao.StoreLoginDao;
import com.acc.services.StoreLoginService;


/**
 * @author prasun.a.kumar
 * 
 */
public class StoreLoginServiceImpl implements StoreLoginService
{
	private static final Logger LOG = Logger.getLogger(StoreLoginServiceImpl.class);

	@Autowired
	private ModelService modelService;
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private StoreLoginDao storeLoginDao;

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
			final CustomerModel customerModel = storeLoginDao.isCustomerFound(customerID);
			if (null != customerModel)
			{
				if (!storeLoginDao.checkUserPresenceOnStore(customerModel, uuID, storeID))
				{
					final CSRCustomerDetailsModel csrCustDetail = storeLoginDao.checkUserPresenceOnCSRDetails(customerModel, uuID,
							storeID);
					final PointOfServiceModel model = new PointOfServiceModel();
					model.setName(storeID);
					final PointOfServiceModel posModel = flexibleSearchService.getModelByExample(model);
					if (null != posModel)
					{
						if (null != csrCustDetail)
						{

							storeLoginDao.saveCSRDetails(csrCustDetail, customerModel, storeID, uuID);
							finalResult = "User Record updated successfully.";
						}
						else
						{

							final CSRCustomerDetailsModel csrModel = modelService.create(CSRCustomerDetailsModel.class);
							storeLoginDao.saveCSRDetails(csrModel, customerModel, storeID, uuID);

						}
					}
				}
				else
				{
					finalResult = "User Record already exists for this store.";
				}
			}
			else
			{
				finalResult = "User doesnot exist.";
			}
		}

		catch (final ModelNotFoundException e)
		{
			finalResult = "Please enter correct Store name.";
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

}
