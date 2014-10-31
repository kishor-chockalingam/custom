/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.acc.controller;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customergroups.CustomerGroupFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoDatas;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commercefacades.user.data.UserGroupDataList;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import com.acc.conv.HttpRequestPaymentInfoPopulator;
import com.acc.populator.HttpRequestAddressDataPopulator;
import com.acc.populator.HttpRequestCustomerDataPopulator;
import com.acc.populator.options.PaymentInfoOption;
import com.acc.user.data.AddressDataList;
import com.acc.user.data.LoginChangeResponse;
import com.acc.user.data.PasswordRestoreResponse;
import com.acc.validator.CustomValidationException;

import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Main Controller for CustomerFacade WebServices
 */
@Controller
@RequestMapping(value = "/v1/{baseSiteId}/customers")
public class CustomersController extends BaseController
{
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "customerGroupFacade")
	private CustomerGroupFacade customerGroupFacade;

	@Resource(name = "httpRequestCustomerDataPopulator")
	private HttpRequestCustomerDataPopulator httpRequestCustomerDataPopulator;

	@Resource(name = "httpRequestAddressDataPopulator")
	private Populator<HttpServletRequest, AddressData> httpRequestAddressDataPopulator;

	@Resource(name = "addressValidator")
	private Validator addressValidator;

	private static final Logger LOG = Logger.getLogger(CustomersController.class);

	/**
	 * Client should pass customer's data as POST Body. Content-Type needs to be set to
	 * application/x-www-form-urlencoded; charset=UTF-8 and sample body (urlencoded) is: old=1234&new=1111<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/customers <br>
	 * Method requires authentication and is restricted <code>HTTPS<code> channel.<br> 
	 * Method type : <code>POST</code> Register data need to be sent as post body..<br>
	 * 
	 * @param login
	 *           - login to be created
	 * @param password
	 *           - customer password
	 * @param firstName
	 *           - customer first name
	 * @param lastName
	 *           - customer last name
	 * @param titleCode
	 *           - customer's title
	 * @throws DuplicateUidException
	 *            in case the requested login already exists
	 */
	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED, reason = "Customer created.")
	public void registerUser(@RequestParam final String login, @RequestParam final String password,
			@RequestParam(required = false) final String titleCode, @RequestParam final String firstName,
			@RequestParam final String lastName) throws DuplicateUidException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("registerUser: login=" + login);
		}

		if (!EmailValidator.getInstance().isValid(login))
		{
			throw new IllegalArgumentException("Login is not a valid e-mail address! (Login=" + login + ")");
		}

		final RegisterData registration = new RegisterData();
		registration.setFirstName(firstName);
		registration.setLastName(lastName);
		registration.setLogin(login);
		registration.setPassword(password);
		registration.setTitleCode(titleCode);
		customerFacade.register(registration);
	}

	/**
	 * Update customer's default address
	 * 
	 * @param id
	 *           - Address id to be set as default address
	 * @throws DuplicateUidException
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/addresses/default/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK, reason = "Default address updated.")
	public void updateDefaultAddress(@PathVariable final String id) throws DuplicateUidException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateDefaultAddress: id=" + id);
		}
		final AddressData address = userFacade.getAddressForCode(id);
		userFacade.setDefaultAddress(address);
	}

	/**
	 * Update customer's profile
	 * 
	 * @param request
	 *           - http request
	 * @return updated profile
	 * @throws DuplicateUidException
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/profile", method =
	{ RequestMethod.PUT, RequestMethod.POST })
	@ResponseBody
	public CustomerData updateProfile(final HttpServletRequest request) throws DuplicateUidException
	{
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateCustomer: userId=" + customer.getUid());
		}
		httpRequestCustomerDataPopulator.populate(request, customer);
		customerFacade.updateFullProfile(customer);
		return customerFacade.getCurrentCustomer();
	}

	/**
	 * Get all customer's addresses
	 * 
	 * @return List of customer addresses
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/addresses", method = RequestMethod.GET)
	@ResponseBody
	public AddressDataList getAddresses()
	{
		final AddressDataList addressDataList = new AddressDataList();
		addressDataList.setAddresses(userFacade.getAddressBook());
		return addressDataList;
	}

	/**
	 * Create new address for current customer
	 * 
	 * @param request
	 * @return address created
	 * @throws DuplicateUidException
	 * @throws CustomValidationException
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/addresses", method = RequestMethod.POST)
	@ResponseBody
	public AddressData createAddress(final HttpServletRequest request) throws DuplicateUidException, CustomValidationException
	{
		final AddressData addressData = new AddressData();
		final Errors errors = new BeanPropertyBindingResult(addressData, "addressData");

		httpRequestAddressDataPopulator.populate(request, addressData);
		addressValidator.validate(addressData, errors);

		if (errors.hasErrors())
		{
			throw new CustomValidationException("Address validation error", errors);
		}

		addressData.setShippingAddress(true);
		addressData.setVisibleInAddressBook(true);
		userFacade.addAddress(addressData);
		return addressData;
	}

	/**
	 * Edit address from current customer
	 * 
	 * @param id
	 *           - id of address to be edited
	 * @return modified address
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/addresses/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public AddressData editAddress(@PathVariable final String id, final HttpServletRequest request)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("editAddress: id=" + id);
		}
		final AddressData address = userFacade.getAddressForCode(id);
		final Errors errors = new BeanPropertyBindingResult(address, "addressData");

		httpRequestAddressDataPopulator.populate(request, address);
		addressValidator.validate(address, errors);

		if (errors.hasErrors())
		{
			throw new CustomValidationException("Address validation error", errors);
		}

		if (address.getId().equals(userFacade.getDefaultAddress().getId()))
		{
			address.setDefaultAddress(true);
			address.setVisibleInAddressBook(true);
		}
		userFacade.editAddress(address);
		return address;
	}

	/**
	 * Remove address from current customer
	 * 
	 * @param id
	 *           - id of address to be removed
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/addresses/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteAddress(@PathVariable final String id)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("deleteAddress: id=" + id);
		}
		final AddressData address = userFacade.getAddressForCode(id);
		userFacade.removeAddress(address);
	}

	/**
	 * Get customer data
	 * 
	 * @return CustomerData object containing customer information
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	@ResponseBody
	public CustomerData getCurrentCustomer()
	{
		return customerFacade.getCurrentCustomer();
	}

	/**
	 * Client should pass old and new password in Body. Content-Type needs to be set to
	 * application/x-www-form-urlencoded; charset=UTF-8 and sample body (urlencoded) is: old=1234&new=1111
	 * 
	 * @param old
	 *           - old password
	 * @param newPassword
	 *           - new password
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/password", method =
	{ RequestMethod.PUT, RequestMethod.POST })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.ACCEPTED, reason = "Password changed.")
	public void changePassword(@RequestParam final String old, @RequestParam(value = "new") final String newPassword)
	{
		customerFacade.changePassword(old, newPassword);
	}

	/**
	 * Web service for getting current user's credit card payment infos.<br>
	 * Sample call: http://localhost:9001/rest/v1/mysite/customers/paymentinfos?saved=true <br>
	 * Method requires authentication and is restricted <code>HTTPS<code> channel.<br> 
	 * Method type : <code>GET</code>.<br>
	 * 
	 * @param saved
	 *           - <code>true</code> to retrieve only saved payment infos. <code>false</code> by default
	 * 
	 * @return List of {@link CCPaymentInfoData} as response body
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/paymentinfos", method = RequestMethod.GET)
	@ResponseBody
	public CCPaymentInfoDatas getPaymentInfos(@RequestParam(required = false, defaultValue = "false") final boolean saved)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getPaymentInfos");
		}

		final CCPaymentInfoDatas data = new CCPaymentInfoDatas();
		data.setPaymentInfos(userFacade.getCCPaymentInfos(saved));
		return data;
	}


	/**
	 * Web service for getting current user's credit card payment info by id.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/customers/paymentinfos/123 <br>
	 * Method requires authentication and is restricted <code>HTTPS<code> channel.<br> 
	 * Method type : <code>GET</code>.<br>
	 * 
	 * 
	 * @return {@link CCPaymentInfoData} as response body
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/paymentinfos/{id}", method = RequestMethod.GET)
	@ResponseBody
	public CCPaymentInfoData getPaymentInfo(@PathVariable final String id)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getPaymentInfo : id = " + id);
		}
		return userFacade.getCCPaymentInfoForCode(id);
	}

	/**
	 * Web service for deleting current user's credit card payment info by id.<br>
	 * Sample call: http://localhost:9001/rest/v1/mysite/customers/paymentinfos/123<br>
	 * Method requires authentication and is restricted <code>HTTPS<code> channel.<br> 
	 * Method type : <code>DELETE</code>.<br>
	 * 
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/paymentinfos/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deletePaymentInfo(@PathVariable final String id)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("deletePaymentInfo: id = " + id);
		}
		userFacade.removeCCPaymentInfo(id);
	}

	/**
	 * Web service for modifying billing address data for the specific payment info.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/paymentinfos/123/address<br>
	 * Method requires authentication and is restricted <code>HTTPS<code> channel.<br> 
	 * Method type : <code>POST</code>. Address data need to be sent as post body.<br>
	 * Method uses {@link HttpRequestAddressDataPopulator} to populate address data from request parameters.
	 * 
	 * @param paymentInfoId
	 * @param request
	 * 
	 */
	@RequestMapping(value = "/current/paymentinfos/{paymentInfoId}/address", method = RequestMethod.POST)
	@ResponseBody
	@Secured("ROLE_CUSTOMERGROUP")
	public void updatePaymentInfoAddress(@PathVariable final String paymentInfoId, final HttpServletRequest request)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updatePaymentInfoAddress: id = " + paymentInfoId);
		}
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentInfoId);

		if (paymentInfoData != null)
		{
			final AddressData billingAddressData = paymentInfoData.getBillingAddress();
			httpRequestAddressDataPopulator.populate(request, billingAddressData);
			paymentInfoData.setBillingAddress(billingAddressData);
			userFacade.updateCCPaymentInfo(paymentInfoData);
		}
		else
		{
			throw new UnknownIdentifierException("Payment info [" + paymentInfoId + "] not found.");
		}
	}

	/**
	 * Web service for modifying existing payment info.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/paymentinfos/123<br>
	 * Method requires authentication and is restricted <code>HTTPS<code> channel.<br> 
	 * Method type : <code>PUT</code>. PaymentInfo data need to be sent in body.<br>
	 * Method uses {@link HttpRequestPaymentInfoPopulator} to populate payment info data from request parameters.
	 * 
	 * @param paymentInfoId
	 * @param request
	 * 
	 */
	@RequestMapping(value = "/current/paymentinfos/{paymentInfoId}", method = RequestMethod.PUT)
	@ResponseBody
	@Secured("ROLE_CUSTOMERGROUP")
	public void updatePaymentInfo(@PathVariable final String paymentInfoId, final HttpServletRequest request)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updatePaymentInfo: id = " + paymentInfoId);
		}
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentInfoId);

		if (paymentInfoData != null)
		{
			getCCPaymentInfoPopulator().populate(request, paymentInfoData, Collections.singletonList(PaymentInfoOption.BASIC));
			userFacade.updateCCPaymentInfo(paymentInfoData);
		}
		else
		{
			throw new UnknownIdentifierException("Payment info [" + paymentInfoId + "] not found.");
		}
	}

	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/customergroups", method = RequestMethod.GET)
	@ResponseBody
	public UserGroupDataList getAllCustomerGroupsForCurrentCustomer()
	{
		final UserGroupDataList userGroupDataList = new UserGroupDataList();
		userGroupDataList.setUserGroups(customerGroupFacade.getCustomerGroupsForCurrentUser());
		return userGroupDataList;
	}

	/**
	 * Web service for getting customer groups for current user.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/customers/{uid}/customergroups<br>
	 * Method requires authentication and is restricted <code>HTTPS<code> channel.<br> 
	 * Method type : <code>GET</code>.
	 * 
	 * @return {@link UserGroupDataList} as response body
	 */
	@Secured("ROLE_CUSTOMERMANAGERGROUP")
	@RequestMapping(value = "/{uid}/customergroups", method = RequestMethod.GET)
	@ResponseBody
	public UserGroupDataList getAllCustomerGroupsForCustomer(@PathVariable final String uid)
	{
		final UserGroupDataList userGroupDataList = new UserGroupDataList();
		userGroupDataList.setUserGroups(customerGroupFacade.getCustomerGroupsForUser(uid));
		return userGroupDataList;
	}

	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/current/login", method =
	{ RequestMethod.PUT, RequestMethod.POST })
	@ResponseBody
	public LoginChangeResponse changeLogin(@RequestParam final String newLogin, @RequestParam final String password)
			throws DuplicateUidException, PasswordMismatchException
	{
		if (!EmailValidator.getInstance().isValid(newLogin))
		{
			throw new IllegalArgumentException("Login is not a valid e-mail address! (Login=" + newLogin + ")");
		}
		customerFacade.changeUid(newLogin, password);
		final LoginChangeResponse loginChangeResponse = new LoginChangeResponse();
		loginChangeResponse.setSuccess(true);
		return loginChangeResponse;
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/current/forgottenpassword", method = RequestMethod.POST)
	@ResponseBody
	public PasswordRestoreResponse restorePassword(@RequestParam final String login)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("restorePassword: uid=" + login);
		}
		customerFacade.forgottenPassword(login);
		final PasswordRestoreResponse passwordRestoreResponse = new PasswordRestoreResponse();
		passwordRestoreResponse.setSuccess(true);
		return passwordRestoreResponse;
	}

	/**
	 * Client should pass old and new password in Body. Content-Type needs to be set to
	 * application/x-www-form-urlencoded; charset=UTF-8 and sample body (urlencoded) is: new=1111
	 * 
	 * @param newPassword
	 *           - new password
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/{customerId}/password", method =
	{ RequestMethod.PUT, RequestMethod.POST })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.ACCEPTED, reason = "Password changed.")
	public void changeCustomerPassword(@PathVariable final String customerId, @RequestParam(value = "new") final String newPassword)
	{
		userService.setPassword(customerId, newPassword);
	}

	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	public void setCustomerGroupFacade(final CustomerGroupFacade customerGroupFacade)
	{
		this.customerGroupFacade = customerGroupFacade;
	}

	public void setAddressValidators(final Validator addressValidator)
	{
		this.addressValidator = addressValidator;
	}
}
