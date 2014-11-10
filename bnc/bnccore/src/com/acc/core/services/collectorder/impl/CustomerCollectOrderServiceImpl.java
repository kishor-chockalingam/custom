/**
 * 
 */
package com.acc.core.services.collectorder.impl;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.acc.core.model.CollectOrderModel;
import com.acc.core.services.collectorder.CustomerCollectOrderService;



/**
 * @author prasun.a.kumar
 * 
 */
public class CustomerCollectOrderServiceImpl implements CustomerCollectOrderService
{
	private static final Logger LOG = Logger.getLogger(CustomerCollectOrderServiceImpl.class);
	@Autowired
	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.services.collectorder.CustomerCollectOrderService#saveCustomerColectOrder(com.acc.core.model.
	 * CollectOrderModel)
	 */
	@Override
	public void saveCustomerColectOrder(final CollectOrderModel collectOrderModel)
	{
		try
		{
			modelService.save(collectOrderModel);
			LOG.info("Customer Collect Order data saved successfully.");
		}
		catch (final ModelSavingException ex)
		{
			LOG.error("Customer Collect Order data saving error [" + ex.getMessage() + "].");
		}

	}
}
