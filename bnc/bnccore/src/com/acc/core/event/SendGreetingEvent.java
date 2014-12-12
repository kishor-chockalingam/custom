/**
 * 
 */
package com.acc.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * @author swarnima.gupta
 * 
 */
public class SendGreetingEvent extends AbstractCommerceUserEvent
{
	private String token;

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
}
