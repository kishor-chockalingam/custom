/**
 * 
 */
package com.acc.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.commerceservices.model.process.SendGreetingProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author swarnima.gupta
 * 
 */
public class SendGreetingEventListener extends AbstractSiteEventListener<SendGreetingEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	protected void onSiteEvent(final SendGreetingEvent event)
	{
		final SendGreetingProcessModel sendGreetingProcessModel = (SendGreetingProcessModel) getBusinessProcessService()
				.createProcess("sendGreeting-" + event.getCustomer().getUid() + "-" + System.currentTimeMillis(),
						"sendGreetingEmailProcess");
		sendGreetingProcessModel.setSite(event.getSite());
		sendGreetingProcessModel.setCustomer(event.getCustomer());
		sendGreetingProcessModel.setToken(event.getToken());
		sendGreetingProcessModel.setLanguage(event.getLanguage());
		sendGreetingProcessModel.setCurrency(event.getCurrency());
		sendGreetingProcessModel.setStore(event.getBaseStore());
		getModelService().save(sendGreetingProcessModel);
		getBusinessProcessService().startProcess(sendGreetingProcessModel);
	}

	@Override
	protected boolean shouldHandleEvent(final SendGreetingEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}
}
