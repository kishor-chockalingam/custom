/**
 * 
 */
package com.acc.facade.csrCustomerDetails.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import com.acc.core.model.CSRCustomerDetailsModel;
import com.acc.facades.CSRCustomerDetails.data.CSRCustomerDetailsData;



/**
 * @author swapnil.a.pandey
 * 
 */
public class CSRCustomerDetailsPopulator implements Populator<CSRCustomerDetailsModel, CSRCustomerDetailsData>
{

	final String contextPath = "/bncstorefront/_ui/desktop/common/images/Dummy.jpg";
	@Autowired
	private UserService userService;

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

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
			final UserModel userModel = userService.getUserForUID(source.getCustomerId());
			if (userModel instanceof CustomerModel)
			{
				final CustomerModel customerModel = (CustomerModel) userModel;
				target.setProfilePictureURL((null == customerModel.getProfilePicture() ? contextPath : customerModel
						.getProfilePicture().getURL2()));
			}

		}

	}
}
