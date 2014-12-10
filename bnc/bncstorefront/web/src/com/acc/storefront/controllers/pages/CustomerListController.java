/**
 * 
 */
package com.acc.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.acc.core.enums.CSRStoreStatus;
import com.acc.core.model.CSRCustomerDetailsModel;
import com.acc.facades.CSRCustomerDetails.data.CSRCustomerDetailsData;
import com.acc.facades.storecustomer.StoreCustomerFacade;
import com.acc.facades.wishlist.data.Wishlist2Data;
import com.acc.storefront.controllers.ControllerConstants;
import com.acc.storefront.util.CustomerOrderData;
import com.acc.storefront.util.ProfileInformationDto;
import com.acc.storefront.util.StoreCustomerData;


/**
 * @author prasun.a.kumar
 * 
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/customerlist")
public class CustomerListController extends AbstractPageController
{
	private static final String REDIRECT_TO_CUSTOMER_DETAILS = REDIRECT_PREFIX + "/customerlist/customerdeatils";
	private static final String ACCOUNT_CMS_PAGE = "account";
	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);
	
	private static final Logger LOG = Logger.getLogger(CustomerListController.class);

	@Autowired
	private StoreCustomerFacade StoreCustomerFacade;
	@Autowired
	private UserService userService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private Wishlist2Service wishlistService;
	@Autowired
	private ProductFacade productFacade;
	@Autowired
	private Converter<Wishlist2Model, Wishlist2Data> Wishlist2Converter;


	@RequestMapping(value = "/assistcustomer", method = RequestMethod.GET, produces = "application/json")
	public String customerHistory(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		final ProfileInformationDto informationDto = new ProfileInformationDto();
		final CustomerData customerData = new CustomerData();
		final List<CustomerOrderData> customerOrderDataList = new ArrayList<CustomerOrderData>();
		final StoreCustomerData storecustomerData = new StoreCustomerData();
		if ((null != request.getParameter("csrId") && request.getParameter("csrId").trim().length() > 0)
				&& null != request.getParameter("customerPK"))
		{
			final CSRCustomerDetailsModel csrCustomerDetailsModel = StoreCustomerFacade.updateCSRCustomerDetail(
					request.getParameter("csrId"), request.getParameter("customerPK"), "");
			final UserModel csrUserModel = userService.getUserForUID(request.getParameter("csrId"));
			if (csrUserModel instanceof CustomerModel)
			{
				final CustomerModel csrCustomerModel = (CustomerModel) csrUserModel;
				customerData.setName(csrCustomerModel.getName());
				customerData.setUid(csrCustomerModel.getUid());
				customerData.setTitle(csrCustomerModel.getDescription());
			}
			assistCustomerRecord(csrCustomerDetailsModel, storecustomerData, informationDto, customerOrderDataList, model);
		}
		else if (null != request.getParameter("customerPK"))
		{
			final CSRCustomerDetailsModel csrCustomerDetailsModel = modelService.get(PK.parse(request.getParameter("customerPK")));
			assistCustomerRecord(csrCustomerDetailsModel, storecustomerData, informationDto, customerOrderDataList, model);
		}

		model.addAttribute("storecustomerData", storecustomerData);
		model.addAttribute("informationDto", informationDto);
		model.addAttribute("customerOrderDataList", customerOrderDataList);
		model.addAttribute("customerData", customerData);
		model.addAttribute("CSR_USER", sessionService.getAttribute("CSR_USER"));
		return ControllerConstants.Views.Fragments.Cart.CustomerDetailsFragment;

	}

	/**
	 * This helper method is used to return the Customer list of records like Order history, Contact Address and his/her
	 * details.
	 * 
	 * @param csrCustomerDetailsModel
	 * @param storecustomerData
	 *           Indicates Customer LoggedIn/NoThanks/Inservice status data.
	 * @param informationDto
	 *           Indicates his/her information.
	 * @param customerOrderDataList
	 *           Indicates their list of Orders.
	 */
	private void assistCustomerRecord(final CSRCustomerDetailsModel csrCustomerDetailsModel,
			final StoreCustomerData storecustomerData, final ProfileInformationDto informationDto,
			final List<CustomerOrderData> customerOrderDataList, final Model model)
	{
		final UserModel userModel = userService.getUserForUID(csrCustomerDetailsModel.getCustomerId());
		if (userModel instanceof CustomerModel)
		{
			final CustomerModel customerModel = (CustomerModel) userModel;
			//retrieving wishlist entries
			Wishlist2Model wishlistModel = null;
			if (!wishlistService.hasDefaultWishlist(userModel))
			{
				wishlistService.createDefaultWishlist(userModel, "wishlist", "add to wishlist functionality");
				wishlistModel = wishlistService.getDefaultWishlist(userModel);
			}
			else
			{
				wishlistModel = wishlistService.getDefaultWishlist(userModel);
			}
			Wishlist2Data wishlistData = null != wishlistModel.getEntries()?Wishlist2Converter.convert(wishlistModel):null;
			//end ** wishlist entries**
			//retrieving recently viewed products
			final List<ProductData> products = new ArrayList<ProductData>();
			if (null != customerModel.getRecentlyviewedproducts())
			{
				for (final ProductModel productModel : customerModel.getRecentlyviewedproducts())
				{
					products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
				}
			}
			Collections.reverse(products);
			model.addAttribute("wishlist", wishlistData);
			model.addAttribute("productData", products);
			//end ** recently viewed products**
			//CSR Customer data
			final String contextPath = "/bncstorefront/_ui/desktop/common/images/Dummy.jpg";
			storecustomerData.setCustomerName(csrCustomerDetailsModel.getCustomerName());
			final String time = returnLoginFromTime(csrCustomerDetailsModel.getLoginTime());
			final String loggedTime = returnLoggedInTime(csrCustomerDetailsModel.getLoginTime());
			storecustomerData.setWaitingTime(time);
			storecustomerData.setLoginTime(loggedTime);
			storecustomerData.setProfilePictureURL((null == customerModel.getProfilePicture() ? contextPath : customerModel
					.getProfilePicture().getURL2()));
			storecustomerData.setCustomerId(customerModel.getUid());
			storecustomerData.setProcessedBy((null == csrCustomerDetailsModel.getProcessedBy() ? "" : csrCustomerDetailsModel.getProcessedBy()));
			informationDto.setName(csrCustomerDetailsModel.getCustomerName());
			final Collection<AddressModel> addressList = customerModel.getAddresses();
			if (null != addressList)
			{
				for (final AddressModel addressModel : addressList)
				{
					if (addressModel.getContactAddress())
					{
						final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

						informationDto.setDob(null == addressModel.getDateOfBirth() ? "" : dateFormat.format(addressModel
								.getDateOfBirth()));
						informationDto.setLine1(addressModel.getLine1());
						informationDto.setLine2(addressModel.getLine2());
						informationDto.setApartment(addressModel.getAppartment());
						informationDto.setPostalCode(addressModel.getPostalcode());
					}
				}
			}

			final Collection<OrderModel> orderModel = customerModel.getOrders();
			if (null != orderModel)
			{
				for (final OrderModel orderModel2 : orderModel)
				{
					final CustomerOrderData customerOrderData = new CustomerOrderData();
					customerOrderData.setOrderCode(orderModel2.getCode());
					final DateFormat dateFormat = new SimpleDateFormat("dd MMM,yyyy");
					customerOrderData.setPlacedDate(dateFormat.format(orderModel2.getCreationtime()));
					if (null != orderModel2.getTotalPrice())
					{
						customerOrderData.setTotal(orderModel2.getTotalPrice().toString());
					}
					customerOrderDataList.add(customerOrderData);
				}
			}
		}
	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = "/customerdeatils", method = RequestMethod.GET)
	public String customerListDetails(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		final List<StoreCustomerData> customerStatusDataList = new ArrayList<StoreCustomerData>();
		final List<StoreCustomerData> customerInServiceDataList = new ArrayList<StoreCustomerData>();
		final List<StoreCustomerData> customerNoThanxDataList = new ArrayList<StoreCustomerData>();
		final List<CSRCustomerDetailsModel> csrCustomerDetailsList = StoreCustomerFacade.getCSRCustomerDetails();
		final String status = request.getParameter("status");
		final List<CSRCustomerDetailsModel> csrCustomerDetailsByStatusList = StoreCustomerFacade
				.getCSRCustomerDetailsByStatus(StringUtils.isEmpty(status) ? CSRStoreStatus.LOGGEDIN : CSRStoreStatus.valueOf(status));
		final String contextPath = "/bncstorefront/_ui/desktop/common/images/Dummy.jpg";
		try
		{
			if (null != csrCustomerDetailsByStatusList)
			{
				for (final CSRCustomerDetailsModel customerDetails : csrCustomerDetailsByStatusList)
				{
					final UserModel userModel = userService.getUserForUID(customerDetails.getCustomerId());
					String profilePictureURL = "";
					if (null != userModel && userModel instanceof CustomerModel)
					{
						final CustomerModel customerModel = (CustomerModel) userModel;
						profilePictureURL = (null == customerModel.getProfilePicture() ? contextPath : customerModel
								.getProfilePicture().getURL2());
					}
					final StoreCustomerData storecustomerData = new StoreCustomerData();
					final String time = returnLoginFromTime(customerDetails.getLoginTime());
					final String loggedTime = returnLoggedInTime(customerDetails.getLoginTime());
					storecustomerData.setCustomerId(customerDetails.getCustomerId());
					storecustomerData.setCustomerName(customerDetails.getCustomerName());
					storecustomerData.setStoreCustomerPK(customerDetails.getPk().getLongValueAsString());
					storecustomerData.setWaitingTime(time);
					storecustomerData.setLoginTime(loggedTime);
					storecustomerData.setProfilePictureURL(profilePictureURL);
					storecustomerData
							.setProcessedBy((null == customerDetails.getProcessedBy() ? "" : customerDetails.getProcessedBy()));
					customerStatusDataList.add(storecustomerData);
				}
			}
		}
		catch (final UnknownIdentifierException e)
		{
			//
		}
		model.addAttribute("customerLoggedInDataList", customerStatusDataList);
		model.addAttribute("csrCustomerDetailsByStatusList", csrCustomerDetailsByStatusList);
		model.addAttribute("Queued", getStatusCount(csrCustomerDetailsList, CSRStoreStatus.LOGGEDIN));
		model.addAttribute("Active", getStatusCount(csrCustomerDetailsList, CSRStoreStatus.INSERVICE));
		model.addAttribute("Serviced", getStatusCount(csrCustomerDetailsList, CSRStoreStatus.COMPLETED));
		model.addAttribute("CSR_USER", sessionService.getAttribute("CSR_USER"));

		return ControllerConstants.Views.Pages.Account.customerDetailsPage;

	}

	/**
	 * @param cSRCustomerDetailsModelsList
	 */
	private int getStatusCount(final List<CSRCustomerDetailsModel> cSRCustomerDetailsModelsList, final CSRStoreStatus status)
	{
		int statusCount = 0;
		if (CollectionUtils.isNotEmpty(cSRCustomerDetailsModelsList))
		{
			for (final CSRCustomerDetailsModel cSRCustomerDetailsModel : cSRCustomerDetailsModelsList)
			{
				if (status.equals(cSRCustomerDetailsModel.getStatus()))
				{
					statusCount++;
				}
			}
		}
		return statusCount;
	}




	@RequestMapping(value = "/csrlogout", method = RequestMethod.GET)
	public String csrlogout()
	{
		sessionService.removeAttribute("POINT_OF_SERVICE");
		sessionService.removeAttribute("CSR_USER");
		return REDIRECT_PREFIX + ROOT;
	}

	@RequestMapping(value = "/statusUpdate", method = RequestMethod.GET)
	public String customerStatusChanged(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		final String customerPK = request.getParameter("customerPK");
		final String status = request.getParameter("status");
		final String csrID = sessionService.getAttribute("CSR_USER");
		StoreCustomerFacade.updateCSRCustomerDetail(csrID, customerPK, status);
		return REDIRECT_TO_CUSTOMER_DETAILS;
	}


	/**
	 * @param loginTime
	 * @return Logged in by time.
	 */
	private String returnLoggedInTime(final Date loginTime)
	{
		final SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm a");
		final String formattedDate = "Logged In by " + formatDate.format(loginTime).toString();
		return formattedDate;
	}

	/**
	 * @param loginTime
	 * @return Waiting time of User
	 */
	private String returnLoginFromTime(final Date loginTime)
	{
		int waitTime = 0;
		String time = "";
		final Calendar start = Calendar.getInstance();
		start.setTime(loginTime);
		final Calendar end = Calendar.getInstance();
		end.setTime(new Date());
		final Calendar clone = (Calendar) start.clone();
		final int min = (int) (end.getTimeInMillis() - clone.getTimeInMillis()) / 60000;
		if ((min >= 60) && ((min % 60) > 0))
		{
			waitTime = min / 60;
			time = waitTime + " Hours " + min % 60 + " Minutes";
		}
		else
		{
			waitTime = min;
			time = waitTime + " Minutes";
		}
		return time;
	}
	@RequestMapping(value = "/customerName", method = RequestMethod.GET, produces = "application/json")
	public String SearchByCustomerName(@RequestParam("customername") final String customerName, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException

	{
		final List<CSRCustomerDetailsData> csrCustomerDetailsData = StoreCustomerFacade.getCollectOrderByCustomerName(customerName);
		if(null!=csrCustomerDetailsData)
		{
			LOG.info("customer data in csutomer list controller"+csrCustomerDetailsData);

		LOG.info("customer name in csutomer list controller"+csrCustomerDetailsData.get(0).getCustomerName());
		LOG.info("customer id in csutomer list controller"+csrCustomerDetailsData.get(0).getCustomerId());
		}
		model.addAttribute("collectOrderDataByCustomerName", csrCustomerDetailsData);
		LOG.info("In controller of SearchByCustomerName");
		return ControllerConstants.Views.Fragments.Cart.OrderByCustomerName;
	}

}
