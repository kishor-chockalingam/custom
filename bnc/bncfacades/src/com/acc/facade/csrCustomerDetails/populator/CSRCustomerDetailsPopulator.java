/**
 * 
 */
package com.acc.facade.csrCustomerDetails.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.acc.core.model.CSRCustomerDetailsModel;
import com.acc.facades.CSRCustomerDetails.data.CSRCustomerDetailsData;



/**
 * @author swapnil.a.pandey
 * 
 */
public class CSRCustomerDetailsPopulator implements Populator<CSRCustomerDetailsModel, CSRCustomerDetailsData>
{



	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final CSRCustomerDetailsModel source, final CSRCustomerDetailsData target) throws ConversionException
	{

		if (source != null)
		{
			target.setCustomerId(source.getCustomerId());
			target.setCustomerName(source.getCustomerName());
			target.setLoginTime(source.getLoginTime());
			target.setPk(String.valueOf(source.getPk().getLongValue()));
			target.setPointOfService(source.getPointOfService());
			target.setStatus(source.getStatus());
			target.setUUID(source.getUUID());
			target.setProcessedBy(source.getProcessedBy());


		}

	}
}
