/**
 * 
 */
package com.acc.core.services.collectorder.impl;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.acc.core.dao.collectorder.CustomerCollectOrderDao;
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
	
	private CustomerCollectOrderDao customerCollectOrderDao;

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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.services.collectorder.CustomerCollectOrderService#getCollectOrders()
	 */
	@Override
	public List<CollectOrderModel> getCollectOrders()
	{
		LOG.info("## In CustomerCollectOrderServiceImpl getCollectOrders ##");
		return customerCollectOrderDao.getCollectOrders();
	}

	/**
	 * @param customerCollectOrderDao
	 *           the customerCollectOrderDao to set
	 */
	public void setCustomerCollectOrderDao(final CustomerCollectOrderDao customerCollectOrderDao)
	{
		this.customerCollectOrderDao = customerCollectOrderDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.services.collectorder.CustomerCollectOrderService#getCollectOrderByUCOID(java.lang.String)
	 */
	@Override
	public CollectOrderModel getCollectOrderByUCOID(final String ucoid)
	{
		return customerCollectOrderDao.getCollectOrderByUCOID(ucoid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acc.core.services.collectorder.CustomerCollectOrderService#getCollectOrderByOrderCode(java.lang.String)
	 */
	@Override
	public CollectOrderModel getCollectOrderByOrderCode(final String orderCode)
	{
		return customerCollectOrderDao.getCollectOrderByOrderCode(orderCode);
	}
}
