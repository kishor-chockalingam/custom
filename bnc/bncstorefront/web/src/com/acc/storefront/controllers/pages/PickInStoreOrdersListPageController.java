/**
 * 
 */
package com.acc.storefront.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.acc.core.collectorder.facade.CustomerCollectOrderFacade;
import com.acc.core.util.BnCGenericUtil;
import com.acc.facades.collectOrder.CollectOrderStatus;
import com.acc.facades.collectOrder.data.CollectOrderData;
import com.acc.facades.storecustomer.StoreCustomerFacade;
import com.acc.facades.wishlist.data.Wishlist2Data;
import com.acc.storefront.controllers.ControllerConstants;
import com.acc.storefront.util.StoreCustomerData;
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
	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	@Autowired
	private CustomerCollectOrderFacade customerCollectOrderFacade;

	@Autowired
	private UserService userService;
	@Autowired
	private Wishlist2Service wishlistService;
	@Autowired
	private ProductFacade productFacade;
	@Autowired
	private Converter<Wishlist2Model, Wishlist2Data> Wishlist2Converter;

	@Autowired
	private StoreCustomerFacade storeCustomerFacade;

	@Autowired
	private Converter<UserModel, CustomerData> customerConverter;
	@Autowired
	private SessionService sessionService;

	@SuppressWarnings("boxing")
	@RequestMapping(value = VIEWORDERS, method = RequestMethod.GET)
	public String getOrdersList(final Model model, final HttpServletRequest request,	final HttpServletResponse response) throws CMSItemNotFoundException
	{
		String status = request.getParameter("status");
		final List<CollectOrderData> collectOrderDataForStatusList = customerCollectOrderFacade.getCollectOrdersByStatus(StringUtils.isNotEmpty(status) ? status : CollectOrderStatus.PENDING.toString());
		final List<CollectOrderData> collectOrderDataList = customerCollectOrderFacade.getCollectOrders();
		model.addAttribute("Queued", getStatusCount(collectOrderDataList, CollectOrderStatus.PENDING));
		model.addAttribute("Active", getStatusCount(collectOrderDataList, CollectOrderStatus.COMPLETED));
		model.addAttribute("Serviced", getStatusCount(collectOrderDataList, CollectOrderStatus.COLLECTED));
		model.addAttribute("CSR_USER", sessionService.getAttribute("CSR_USER"));
		model.addAttribute("collectOrdersDataList", collectOrderDataForStatusList);
		final OrderData orderData = CollectionUtils.isEmpty(collectOrderDataForStatusList)?new OrderData():customerCollectOrderFacade.getOrderDetailsForCode(collectOrderDataForStatusList.get(0).getOrderId());
		model.addAttribute("orderData", orderData);
		model.addAttribute("collectOrderStatusList", BnCGenericUtil.getStatusList());
		model.addAttribute("collectOrderData", CollectionUtils.isEmpty(collectOrderDataForStatusList)? new CollectOrderData() : customerCollectOrderFacade.getCollectOrderByOrderCode(collectOrderDataForStatusList.get(0).getOrderId()));
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
		String status = request.getParameter("status");
		final List<CollectOrderData> collectOrderDataForStatusList = customerCollectOrderFacade.getCollectOrdersByStatus(StringUtils.isNotEmpty(status) ? status : CollectOrderStatus.PENDING.toString());
		model.addAttribute("collectOrdersDataList", collectOrderDataForStatusList);
		final OrderData orderData = CollectionUtils.isEmpty(collectOrderDataForStatusList)?new OrderData():customerCollectOrderFacade.getOrderDetailsForCode(collectOrderDataForStatusList.get(0).getOrderId());
		model.addAttribute("orderData", orderData);
		model.addAttribute("collectOrderStatusList", BnCGenericUtil.getStatusList());
		model.addAttribute("collectOrderData", CollectionUtils.isEmpty(collectOrderDataForStatusList)? new CollectOrderData() : customerCollectOrderFacade.getCollectOrderByOrderCode(collectOrderDataForStatusList.get(0).getOrderId()));
		return ControllerConstants.Views.Fragments.Cart.OrdersListFragmentPage;
	}


	@RequestMapping(value = ORDER + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET, produces = "application/json")
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
		
		final OrderData orderData = customerCollectOrderFacade.getOrderDetailsForCode(collectOrderData.getOrderId());
		model.addAttribute("orderData", orderData);
		model.addAttribute("collectOrderStatusList", BnCGenericUtil.getStatusList());
		model.addAttribute("collectOrderData", collectOrderData);
		return ControllerConstants.Views.Fragments.Cart.OrderByUCOID;
	}

	@RequestMapping(value = ORDER + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	public String postOrderDetails(final CollectOrderData collectOrderData, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		customerCollectOrderFacade.updateCollectOrder(collectOrderData);
		return REDIRECT_TO_ORDER_DETAILS_PAGE;
	}

	@RequestMapping(value = "/datetime", method = RequestMethod.GET, produces = "application/json")
	public String searchByDateTime(@RequestParam("fdate") final String fromDate, @RequestParam("tdate") final String toDate,
			@RequestParam("ftime") final String fromTime, @RequestParam("ttime") final String toTime, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException

	{
		final List<CollectOrderData> collectOrderDataList = customerCollectOrderFacade.getCollectOrderByDateAndTime(fromDate,
				toDate, fromTime, toTime);
		model.addAttribute("collectOrderDataByUcoid", collectOrderDataList);
		final OrderData orderData = CollectionUtils.isEmpty(collectOrderDataList)?new OrderData()
				:customerCollectOrderFacade.getOrderDetailsForCode(collectOrderDataList.get(0).getOrderId());
		model.addAttribute("orderData", orderData);
		model.addAttribute("collectOrderStatusList", BnCGenericUtil.getStatusList());
		model.addAttribute("collectOrderData", CollectionUtils.isEmpty(collectOrderDataList)?
					new CollectOrderData() : customerCollectOrderFacade.getCollectOrderByOrderCode(collectOrderDataList.get(0).getOrderId()));
		return ControllerConstants.Views.Fragments.Cart.OrderByDateTime;
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

	@RequestMapping(value = "/personaldetails", method = RequestMethod.GET, produces = "application/json")
	public String getPersonalDetails(@RequestParam("code") final String orderCode, @RequestParam("uid") final String uid,
			final Model model, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{

		System.out.println("inside getcustomer details");
		//final List<CSRCustomerDetailsModel> csrCustomerDetailsList = storeCustomerFacade.getCSRCustomerDetails();
		String profilePictureURL = "";
		final UserModel userModel = userService.getUserForUID(uid);
		final Wishlist2Model wishlistModel = wishlistService.getDefaultWishlist(userModel);
		final List<Wishlist2EntryModel> wishlistEnteries = wishlistModel.getEntries();
		Wishlist2Data wishlistData = null;
		final String profileImage = "/bncstorefront/_ui/desktop/common/bnc_images/personal_photos/person1.jpg";
		final CustomerModel customerModel = (CustomerModel) userModel;
		if (null != userModel && userModel instanceof CustomerModel)
		{
			profilePictureURL = (null == customerModel.getProfilePicture() ? profileImage : customerModel.getProfilePicture()
					.getURL2());
		}
		final StoreCustomerData storecustomerData = new StoreCustomerData();
		storecustomerData.setProfilePictureURL((null == customerModel.getProfilePicture() ? profileImage : customerModel
				.getProfilePicture().getURL2()));
		storecustomerData.setCustomerId(customerModel.getUid());
		storecustomerData.setCustomerName(customerModel.getDisplayName());
		storecustomerData.setStoreCustomerPK(customerModel.getPk().getLongValueAsString());
		customerModel.getUid();
		final Collection<AddressModel> address = customerModel.getAddresses();
		for (final AddressModel userAddress : address)
		{
			userAddress.getContactAddress();
			userAddress.getDateOfBirth();
			userAddress.getEmail();
			userAddress.getLine1();
			userAddress.getLine2();
			userAddress.getTown();
			userAddress.getCountry();
			userAddress.getPostalcode();
			model.addAttribute("useraddress", userAddress);
			System.out.println("useraddress" + userAddress);
			if (null != userAddress)
			{
				System.out.println("######################useraddress#########" + userAddress.getFirstname());
			}
		}
		storecustomerData.setProfilePictureURL(profilePictureURL);
		System.out.println("inside getcustomer details" + storecustomerData);
		if (null != storecustomerData)
		{
			System.out.println("profile picture" + storecustomerData.getProfilePictureURL());
			System.out.println("profile picture" + storecustomerData.getCustomerName());

		}
		System.out.println("########customer model" + customerModel);
		model.addAttribute("storeCustomerData", storecustomerData);
		model.addAttribute("customerModel", customerModel);

		final List<ProductData> products = new ArrayList<ProductData>();
		if (userModel instanceof CustomerModel)
		{
			if (null != wishlistEnteries)
			{
				wishlistData = Wishlist2Converter.convert(wishlistModel);

			}
			if (null != customerModel.getRecentlyviewedproducts())
			{
				for (final ProductModel productModel : customerModel.getRecentlyviewedproducts())
				{
					products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
				}
			}
		}
		Collections.reverse(products);
		model.addAttribute("wishlist", wishlistData);
		System.out.println("#############wishlist data" + wishlistData.getEntries());
		model.addAttribute("productData", products);
		System.out.println("#############recetly viewed data" + products);
		model.addAttribute("orderCode", orderCode);
		return ControllerConstants.Views.Fragments.Cart.PersonalDetails;
	}

}
