/**
 * 
 */
package com.acc.core.interceptors;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.acc.core.model.CollectOrderModel;
import com.acc.core.services.collectorder.CustomerCollectOrderService;
import com.acc.core.util.UCOIDUtility;
import com.acc.facades.collectOrder.CollectOrderStatus;


/**
 * @author swarnima.gupta
 * 
 */
public class BnCPrepareInterceptor implements PrepareInterceptor
{
	private static final String PICKUP_GROSS = "pickup-gross";
	private static final Logger LOG = Logger.getLogger(BnCPrepareInterceptor.class);
	
	@Autowired
	private CustomerCollectOrderService customerCollectOrderService;

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		final OrderModel order = (OrderModel) model;
		LOG.info("## In BnCPrepareInterceptor ##");
		if (StringUtils.isEmpty(order.getUCOID()) && PICKUP_GROSS.equals(order.getDeliveryMode().getCode()))
		{
			order.setUCOID(new UCOIDUtility().getUCOID(order.getCode()));
			LOG.info("## Modified Order Number " + order.getUCOID() + " added UCOID ##");

			saveColelctOrder(order, ctx);
		}

	}

	/**
	 * This helper method is used to save the Customer collect order if he/she choose delivery mode as
	 * "Pickup from next counter".
	 * 
	 * @param order
	 *           Indicates Order Model.
	 * @param ctx
	 *           Indicates Interceptor context.
	 */
	private void saveColelctOrder(final OrderModel order, final InterceptorContext ctx)
	{
		final CollectOrderModel collectOrderModel = ctx.getModelService().create(CollectOrderModel.class);
		collectOrderModel.setCID(order.getUser().getUid());
		collectOrderModel.setOID(order.getCode());
		collectOrderModel.setUCOID(order.getUCOID());
		collectOrderModel.setStatus(CollectOrderStatus.PENDING.toString());
		customerCollectOrderService.saveCustomerColectOrder(collectOrderModel);
		LOG.info("Customer Collect Order data saved successfully from.");

	}
}
