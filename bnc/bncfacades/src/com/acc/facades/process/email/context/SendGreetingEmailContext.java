/**
 * 
 */
package com.acc.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.SendGreetingProcessModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;


/**
 * @author swarnima.gupta
 * 
 */
public class SendGreetingEmailContext extends CustomerEmailContext
{
	private String loginTime;
	private String token;

	/**
	 * @return the loginTime
	 */
	public String getLoginTime()
	{
		return loginTime;
	}

	/**
	 * @param loginTime
	 *           the loginTime to set
	 */
	public void setLoginTime(final String loginTime)
	{
		this.loginTime = loginTime;
	}

	/**
	 * @return the token
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @param token
	 *           the token to set
	 */
	public void setToken(final String token)
	{
		this.token = token;
	}

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		if (storeFrontCustomerProcessModel instanceof SendGreetingProcessModel)
		{
			setToken(((SendGreetingProcessModel) storeFrontCustomerProcessModel).getToken());
		}
	}

}
