/**
 * 
 */
package com.acc.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.acc.core.collectorder.facade.CustomerCollectOrderFacade;
import com.acc.facades.collectOrder.data.CollectOrderData;
import com.acc.storefront.controllers.ControllerConstants;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;


/**
 * @author swarnima.gupta
 * 
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/orderslist")
public class PickInStoreOrdersListPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(PickInStoreOrdersListPageController.class);

	private static final String ACCOUNT_CMS_PAGE = "account";
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";

	@Autowired
	private CustomerCollectOrderFacade customerCollectOrderFacade;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/vieworders", method = RequestMethod.GET)
	public String getOrdersList(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		model.addAttribute("collectOrdersDataList", customerCollectOrderFacade.getCollectOrders());
		model.addAttribute("collectOrderDataForm", new CollectOrderData());
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		return ControllerConstants.Views.Pages.Account.ordersListPage;
	}

	@RequestMapping(value = "/order/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String postOrderDetails(@PathVariable("orderCode") final String orderCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		System.out.println("orderCode-------------->" + orderCode);
		final OrderData orderData = orderFacade.getOrderDetailsForCode(orderCode);
		final UserModel userModel = userService.getUserForUID(orderData.getUser().getUid());
		model.addAttribute("orderData", orderData);
		model.addAttribute("userModel", userModel);
		model.addAttribute("collectOrderData", customerCollectOrderFacade.getCollectOrderByOrderCode(orderCode));
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		return ControllerConstants.Views.Pages.Account.orderDetailsPage;
	}
	
	@RequestMapping(value = "/customerpickuporders", method = RequestMethod.GET)
	public String getCustomerPickupOrdersList(final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		LOG.info("Calling methos to fetch Customer list of Collected Orders whose customer ID is ["
				+ userService.getCurrentUser().getUid());
		model.addAttribute("collectOrdersDataList",
				customerCollectOrderFacade.getCustomerListOrders(userService.getCurrentUser().getUid()));
		model.addAttribute("collectOrderDataForm", new CollectOrderData());
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		return ControllerConstants.Views.Pages.Account.CustomerCollectOrderPage;
	}

	@RequestMapping(value = "/ucoid", method = RequestMethod.GET ,produces = "application/json")
	public String SearchByUCOID(@RequestParam("ucoid") final String ucoid, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException

	{
		
		System.out.println("In controller of ucoid1");
		final CollectOrderData collectOrderData = customerCollectOrderFacade.getCollectOrderByUCOID(ucoid);
		model.addAttribute("collectOrderDataByUcoid", collectOrderData);
		System.out.println("In controller of ucoid");
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		return ControllerConstants.Views.Fragments.Cart.OrderByUCOID;

	}

}
