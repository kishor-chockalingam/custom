/**
 * 
 */
package com.acc.facade.collectorder.populator;


import de.hybris.platform.converters.Populator;

import org.apache.log4j.Logger;

import com.acc.core.model.CollectOrderModel;
import com.acc.facades.collectOrder.CollectOrderStatus;
import com.acc.facades.collectOrder.data.CollectOrderData;


/**
 * @author swarnima.gupta
 * 
 */
public class CollectOrderPopulator implements Populator<CollectOrderModel, CollectOrderData>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CollectOrderPopulator.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.commons.conversion.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final CollectOrderModel source, final CollectOrderData target)
	{
		if (source != null)
		{
			target.setPk(String.valueOf(source.getPk().getLongValue()));
			target.setUcoid(source.getUCOID());
			target.setOrderId(source.getOID());
			target.setCustomerId(source.getCID());
			target.setPId(source.getPID());
			target.setStatus(CollectOrderStatus.valueOf(source.getStatus()));
		}
	}

}
