/**
 * 
 */
package com.acc.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.acc.core.collectorder.facade.CustomerCollectOrderFacade;
import com.acc.core.util.BnCGenericUtil;
import com.acc.facades.collectOrder.CollectOrderStatus;
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
	private static final String ORDERS = "/orders";
	private static final Logger LOG = Logger.getLogger(PickInStoreOrdersListPageController.class);
	private static final String ACCOUNT_CMS_PAGE = "account";
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";
	private static final String VIEWORDERS = "/vieworders";
	private static final String REDIRECT_TO_ORDER_DETAILS_PAGE = REDIRECT_PREFIX + "/orderslist" + VIEWORDERS;
	private static final String CUSTOMERPICKUPORDERS = "/customerpickuporders";
	private static final String UCOID = "/ucoid";
	private static final String ORDER = "/order/";

	@Autowired
	private CustomerCollectOrderFacade customerCollectOrderFacade;

	@Autowired
	private UserService userService;

	@Autowired
	private Converter<UserModel, CustomerData> customerConverter;
	@Autowired
	private SessionService sessionService;

	@SuppressWarnings("boxing")
	@RequestMapping(value = VIEWORDERS, method = RequestMethod.GET)
	public String getOrdersList(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		//		if(StringUtils.isNotEmpty(request.getParameter("pk")) && StringUtils.isNotEmpty(request.getParameter("status")))
		//		{
		//			final CollectOrderData collectOrderData = new CollectOrderData();
		//			collectOrderData.setPk(request.getParameter("pk"));
		//			collectOrderData.setStatus(CollectOrderStatus.valueOf(request.getParameter("status")));
		//			customerCollectOrderFacade.updateCollectOrder(collectOrderData);
		//		}
		final List<CollectOrderData> collectOrderDataList = customerCollectOrderFacade.getCollectOrders();
		model.addAttribute("Queued", getStatusCount(collectOrderDataList, CollectOrderStatus.PENDING));
		model.addAttribute("Active", getStatusCount(collectOrderDataList, CollectOrderStatus.COMPLETED));
		model.addAttribute("Serviced", getStatusCount(collectOrderDataList, CollectOrderStatus.COLLECTED));
		model.addAttribute("CSR_USER", sessionService.getAttribute("CSR_USER"));
		model.addAttribute("collectOrdersDataList", collectOrderDataList);
		return ControllerConstants.Views.Pages.Account.ordersListPage;
	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = ORDERS, method = RequestMethod.GET, produces = "application/json")
	public String getAjaxOrdersList(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		final List<CollectOrderData> collectOrderDataList = customerCollectOrderFacade.getCollectOrders();
		model.addAttribute("Queued", getStatusCount(collectOrderDataList, CollectOrderStatus.PENDING));
		model.addAttribute("Active", getStatusCount(collectOrderDataList, CollectOrderStatus.COMPLETED));
		model.addAttribute("Serviced", getStatusCount(collectOrderDataList, CollectOrderStatus.COLLECTED));
		model.addAttribute("collectOrdersDataList", collectOrderDataList);
		return ControllerConstants.Views.Fragments.Cart.OrdersListFragmentPage;
	}


	@RequestMapping(value = ORDER + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET,produces = "application/json")
	public String postOrderDetails(@RequestParam("orderCode") final String orderCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		final OrderData orderData = customerCollectOrderFacade.getOrderDetailsForCode(orderCode);
		final CustomerData customerData = customerConverter.convert(userService.getUserForUID(orderData.getUser().getUid()));
		model.addAttribute("orderData", orderData);
		model.addAttribute("customerData", customerData);
		model.addAttribute("collectOrderData", customerCollectOrderFacade.getCollectOrderByOrderCode(orderCode));
		model.addAttribute("collectOrderStatusList", BnCGenericUtil.getStatusList());
		return ControllerConstants.Views.Fragments.Cart.csrOrderDetails;
	}


	@RequestMapping(value = CUSTOMERPICKUPORDERS, method = RequestMethod.GET)
	public String getCustomerPickupOrdersList(final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		LOG.info("Calling methos to fetch Customer list of Collected Orders whose customer ID is ["
				+ userService.getCurrentUser().getUid());
		model.addAttribute("collectOrdersDataList",
				customerCollectOrderFacade.getCustomerListOrders(userService.getCurrentUser().getUid()));
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		return ControllerConstants.Views.Pages.Account.CustomerCollectOrderPage;
	}

	@RequestMapping(value = UCOID, method = RequestMethod.GET, produces = "application/json")
	public String SearchByUCOID(@RequestParam("ucoid") final String ucoid, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException

	{
		LOG.info("In controller of ucoid1");
		final CollectOrderData collectOrderData = customerCollectOrderFacade.getCollectOrderByUCOID(ucoid);
		model.addAttribute("collectOrderDataByUcoid", collectOrderData);
		LOG.info("In controller of ucoid");
		return ControllerConstants.Views.Fragments.Cart.OrderByUCOID;
	}

	@RequestMapping(value = ORDER + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	public String postOrderDetails(final CollectOrderData collectOrderData, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		customerCollectOrderFacade.updateCollectOrder(collectOrderData);
		return REDIRECT_TO_ORDER_DETAILS_PAGE;
	}

	/**
	 * @param collectOrderDataList
	 */
	private int getStatusCount(final List<CollectOrderData> collectOrderDataList, final CollectOrderStatus status)
	{
		int statusCount = 0;
		for (final CollectOrderData collectOrderData : collectOrderDataList)
		{
			if (status.equals(collectOrderData.getStatus()))
			{
				statusCount++;
			}
		}
		return statusCount;
	}
}
