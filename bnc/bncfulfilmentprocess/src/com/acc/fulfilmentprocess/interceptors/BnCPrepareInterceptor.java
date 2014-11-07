/**
 * 
 */
package com.acc.fulfilmentprocess.interceptors;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.acc.core.util.UCOIDUtility;

/**
 * @author swarnima.gupta
 * 
 */
public class BnCPrepareInterceptor implements PrepareInterceptor
{
	private static final String PICKUP_GROSS = "pickup-gross";
	private static final Logger LOG = Logger.getLogger(BnCPrepareInterceptor.class);

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		final OrderModel order = (OrderModel) model;
		LOG.info("## In BnCPrepareInterceptor ##");
		if (StringUtils.isEmpty(order.getUCOID()) && PICKUP_GROSS.equals(order.getDeliveryMode().getCode()))
		{
			try
			{
				order.setUCOID(new UCOIDUtility().getUCOID(order.getUser().getUid(), order.getCode()));
				LOG.info("## Modified Order Number "+order.getUCOID()+" added UCOID ##");
			}
			catch (NoSuchAlgorithmException | InvalidKeyException e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
	}
}
