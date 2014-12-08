/**
 * 
 */
package com.acc.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.acc.core.enums.CSRStoreStatus;
import com.acc.core.model.CSRCustomerDetailsModel;
import com.acc.facades.storecustomer.StoreCustomerFacade;
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
	@Autowired
	private StoreCustomerFacade StoreCustomerFacade;
	@Autowired
	private UserService userService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private SessionService sessionService;


	@RequestMapping(value = "/assistcustomer", method = RequestMethod.GET)
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
			assistCustomerRecord(csrCustomerDetailsModel, storecustomerData, informationDto, customerOrderDataList);
		}
		else if (null != request.getParameter("customerPK"))
		{
			final CSRCustomerDetailsModel csrCustomerDetailsModel = modelService.get(PK.parse(request.getParameter("customerPK")));
			assistCustomerRecord(csrCustomerDetailsModel, storecustomerData, informationDto, customerOrderDataList);
		}

		model.addAttribute("storecustomerData", storecustomerData);
		model.addAttribute("informationDto", informationDto);
		model.addAttribute("customerOrderDataList", customerOrderDataList);
		model.addAttribute("customerData", customerData);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		return ControllerConstants.Views.Pages.Account.assistcustomerPage;

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
			final List<CustomerOrderData> customerOrderDataList)
	{
		final UserModel userModel = userService.getUserForUID(csrCustomerDetailsModel.getCustomerId());
		if (userModel instanceof CustomerModel)
		{
			final CustomerModel customerModel = (CustomerModel) userModel;

			//CSR Customer data
			final String contextPath = "/bncstorefront/_ui/desktop/common/images/Dummy.jpg";
			storecustomerData.setCustomerName(csrCustomerDetailsModel.getCustomerName());
			final String time = returnLoginFromTime(csrCustomerDetailsModel.getLoginTime());
			final String loggedTime = returnLoggedInTime(csrCustomerDetailsModel.getLoginTime());
			storecustomerData.setWaitingTime(time);
			storecustomerData.setLoginTime(loggedTime);
			storecustomerData.setProfilePictureURL((null == customerModel.getProfilePicture() ? contextPath : customerModel
					.getProfilePicture().getURL2()));
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
		String status = request.getParameter("status");
		final List<CSRCustomerDetailsModel> csrCustomerDetailsByStatusList = StoreCustomerFacade.getCSRCustomerDetailsByStatus(StringUtils.isEmpty(status)?CSRStoreStatus.LOGGEDIN:CSRStoreStatus.valueOf(status));
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
					storecustomerData.setProcessedBy((null == customerDetails.getProcessedBy() ? "" : customerDetails
							.getProcessedBy()));
					customerStatusDataList.add(storecustomerData);
				}
			}
		}
		catch (final UnknownIdentifierException e)
		{
			//
		}
		model.addAttribute("customerLoggedInDataList", customerStatusDataList);
		model.addAttribute("csrCustomerDetailsByStatusList",csrCustomerDetailsByStatusList);
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
		if(CollectionUtils.isNotEmpty(cSRCustomerDetailsModelsList))
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
}
