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

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.DeliveryModesData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.promotion.CommercePromotionRestrictionFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.promotion.CommercePromotionRestrictionException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import com.acc.conv.HttpRequestPaymentInfoPopulator;
import com.acc.error.data.ErrorData;
import com.acc.exceptions.InvalidPaymentInfoException;
import com.acc.exceptions.NoCheckoutCartException;
import com.acc.exceptions.PaymentAuthorizationException;
import com.acc.exceptions.UnsupportedDeliveryAddressException;
import com.acc.exceptions.UnsupportedDeliveryModeException;
import com.acc.populator.options.PaymentInfoOption;
import com.acc.validator.CCPaymentInfoValidator;
import com.acc.validator.CustomValidationException;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * @author KKW
 * 
 */
@Controller
@RequestMapping(value = "/v1/{baseSiteId}/cart")
public class CartController extends BaseController
{
	private final static Logger LOG = Logger.getLogger(CartController.class);
	@Resource(name = "commerceWebServicesCartFacade")
	private CartFacade cartFacade;
	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;
	@Resource(name = "cartRestorationConverter")
	private Converter<CommerceCartRestoration, CartRestorationData> cartRestorationConverter;
	@Resource(name = "commercePromotionRestrictionFacade")
	private CommercePromotionRestrictionFacade commercePromotionRestrictionFacade;
	@Resource(name = "voucherFacade")
	private VoucherFacade voucherFacade;

	public CartData getSessionCart()
	{
		return getSessionCart(false);
	}

	/**
	 * Web service for getting session cart. If there is no cart in the current session it will be restored if possible,
	 * otherwise new one will be created. <br>
	 * Sample call: http://localhost:9001/rest/v1/mysite/cart/ <br>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.<br>
	 * 
	 * @param restore
	 *           enables cart restoration (true by default)
	 * 
	 * @return {@link CartData} as response body.
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public CartData getSessionCart(@RequestParam(required = false, defaultValue = "true") final boolean restore)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getSessionCart");
		}

		if (!userFacade.isAnonymousUser() && !cartFacade.hasSessionCart() && restore)
		{
			try
			{
				commerceCartService.restoreCart(commerceCartService.getCartForGuidAndSiteAndUser(null,
						baseSiteService.getCurrentBaseSite(), userService.getCurrentUser()));
			}
			catch (final CommerceCartRestorationException e)
			{
				LOG.error("Couldn't restore cart: " + e.getMessage());
			}
		}
		return cartFacade.getSessionCart();
	}

	/**
	 * Web service handler for adding new products to the session cart.<br>
	 * Sample target URL : http://localhost:9001/rest/v1/cart/entry.<br>
	 * Client should provide product code and quantity (optional) as POST body.<br>
	 * For Content-Type=application/x-www-form-urlencoded;charset=UTF-8 a sample body is: (urlencoded) is:
	 * entryNumber=1&qty=2..<br>
	 * 
	 * Request Method = <code>POST<code>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.
	 * 
	 * @param code
	 * @param qty
	 * @return {@link CartModificationData} as response body.
	 * @throws CommerceCartModificationException
	 */

	@RequestMapping(value = "/entry", method = RequestMethod.POST)
	@ResponseBody
	public CartModificationData addToCart(@RequestParam(required = true) final String code,
			@RequestParam(required = false, defaultValue = "1") final long qty) throws CommerceCartModificationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("addToCart : code = " + code + ", qty = " + qty);
		}
		return cartFacade.addToCart(code, qty);
	}

	/**
	 * Web service for modifying cart entry quantity.<br>
	 * Client should provide cart entry number as path variable and new quantity as url request parameter.<br>
	 * Sample target URL : http://localhost:9001/rest/v1/cart/entry/0?qty=2 <br>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.<br>
	 * Request Method = <code>PUT<code>
	 * 
	 * @param entryNumber
	 * @param qty
	 * @return {@link CartModificationData} as response body.
	 * @throws CommerceCartModificationException
	 */
	@RequestMapping(value = "/entry/{entryNumber}", method = RequestMethod.PUT)
	@ResponseBody
	public CartModificationData updateCartEntry(@PathVariable final long entryNumber, @RequestParam(required = true) final long qty)
			throws CommerceCartModificationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateCartEntry : entryNumber = " + entryNumber + ", qty = " + qty);
		}
		return cartFacade.updateCartEntry(entryNumber, qty);
	}

	/**
	 * Web service for deleting cart entry.<br>
	 * Client should provide cart entry number as path variable.<br>
	 * Sample target URL : http://localhost:9001/rest/v1/cart/entry/0<br>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.<br>
	 * Request Method = <code>DELETE<code>
	 * 
	 * @param entryNumber
	 * @return {@link CartModificationData} as response body.
	 * @throws CommerceCartModificationException
	 */
	@RequestMapping(value = "/entry/{entryNumber}", method = RequestMethod.DELETE)
	@ResponseBody
	public CartModificationData deleteCartEntry(@PathVariable final long entryNumber) throws CommerceCartModificationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("deleteCartEntry : entryNumber = " + entryNumber);
		}
		return cartFacade.updateCartEntry(entryNumber, 0);
	}

	/**
	 * Web service for setting cart's delivery address by address id.<br>
	 * Address id must be given as path variable.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/address/delivery/1234 <br>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.<br>
	 * This method requires authentication.<br>
	 * Method type : <code>PUT</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * 
	 * 
	 * @return true if carts delivery address was changed.
	 * @throws UnsupportedDeliveryAddressException
	 * @throws NoCheckoutCartException
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/address/delivery/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public CartData setCartDeliveryAddress(@PathVariable final String id) throws UnsupportedDeliveryAddressException,
			NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("setCartDeliveryAddress : id = " + id);
		}
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot set delivery address. There was no checkout cart created yet!");
		}
		final AddressData address = new AddressData();
		address.setId(id);
		if (checkoutFacade.setDeliveryAddress(address))
		{
			return getSessionCart();
		}
		throw new UnsupportedDeliveryAddressException(id);
	}

	/**
	 * Web service for removing delivery address from current cart.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/address/delivery <br>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.<br>
	 * This method requires authentication.<br>
	 * Method type : <code>DELETE</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * 
	 * @return true if carts delivery address was removed.
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/address/delivery", method = RequestMethod.DELETE)
	@ResponseBody
	public CartData removeDeliveryAddress()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("removeDeliveryAddress");
		}
		checkoutFacade.removeDeliveryAddress();
		return getSessionCart();
	}

	/**
	 * Web service for setting cart's delivery mode by delivery mode code.<br>
	 * Delivery mode code must be given as path variable.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/deliverymode/expressDelivery <br>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.<br>
	 * This method requires authentication.<br>
	 * Method type : <code>PUT</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * 
	 * @return true if carts delivery mode was changed.
	 * @throws UnsupportedDeliveryModeException
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/deliverymodes/{code}", method = RequestMethod.PUT)
	@ResponseBody
	public CartData setCartDeliveryMode(@PathVariable final String code) throws UnsupportedDeliveryModeException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("setCartDeliveryMode : code = " + code);
		}

		if (checkoutFacade.setDeliveryMode(code))
		{
			return getSessionCart();
		}
		throw new UnsupportedDeliveryModeException(code);
	}

	/**
	 * Web service for removing delivery mode from current cart.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/deliverymode <br>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.<br>
	 * This method requires authentication.<br>
	 * Method type : <code>DELETE</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * 
	 * @return true if cart's delivery mode was removed.
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/deliverymodes", method = RequestMethod.DELETE)
	@ResponseBody
	public CartData removeDeliveryMode()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("removeDeliveryMode");
		}
		checkoutFacade.removeDeliveryMode();
		return getSessionCart();
	}

	/**
	 * Web service for placing order from current session cart.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/placeorder. <br>
	 * This method requires authentication.<br>
	 * Method type : <code>POST</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * @return {@link OrderData} as response body
	 * @throws InvalidCartException
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/placeorder", method = RequestMethod.POST)
	@ResponseBody
	public OrderData placeOrder() throws InvalidCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("placeOrder");
		}
		return checkoutFacade.placeOrder();
	}

	/**
	 * Web service for creating a credit card payment subscription.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/paymentinfo <br>
	 * CCPaymentInfoData parameters need to be send as post body.<br>
	 * Method uses dedicated populator - {@link HttpRequestPaymentInfoPopulator} - to populate the
	 * {@link CCPaymentInfoData} from request parameters.<br>
	 * Method uses dedicated validator - {@link CCPaymentInfoValidator} - to validate request parameters.<br>
	 * This method requires authentication and is restricted for <code>HTTPS</code> channel.<br>
	 * Method type : <code>POST</code>.<br>
	 * 
	 * @param request
	 *           - incoming HttpServletRequest. As there are many potential query parameters to handle they are not
	 *           mapped using annotations.
	 * 
	 * @return {@link CartData} as response body
	 * @throws CustomValidationException
	 * @throws InvalidPaymentInfoException
	 * @throws NoCheckoutCartException
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/paymentinfo", method = RequestMethod.POST)
	@ResponseBody
	public CartData addPaymentInfo(final HttpServletRequest request) throws CustomValidationException,
			InvalidPaymentInfoException, NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("addPaymentInfo");
		}
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot add PaymentInfo. There was no checkout cart created yet!");
		}


		final CCPaymentInfoData paymentInfoData = new CCPaymentInfoData();
		final Errors errors = new BeanPropertyBindingResult(paymentInfoData, "paymentInfoData");

		final Collection<PaymentInfoOption> options = new ArrayList<PaymentInfoOption>();
		options.add(PaymentInfoOption.BASIC);
		options.add(PaymentInfoOption.BILLING_ADDRESS);

		getCCPaymentInfoPopulator().populate(request, paymentInfoData, options);
		getCCPaymentInfoValidator().validate(paymentInfoData, errors);

		if (errors.hasErrors())
		{
			throw new CustomValidationException("Payment Data Validation Error", errors);
		}

		final CCPaymentInfoData createdPaymentInfoData = checkoutFacade.createPaymentSubscription(paymentInfoData);
		if (paymentInfoData.isDefaultPaymentInfo())
		{
			userFacade.setDefaultPaymentInfo(createdPaymentInfoData);
		}
		if (checkoutFacade.setPaymentDetails(createdPaymentInfoData.getId()))
		{
			return getSessionCart();
		}
		throw new InvalidPaymentInfoException(createdPaymentInfoData.getId());

	}

	/**
	 * Web service for assigning given payment (by payment id) to the checkout cart.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/paymentinfo/1234 <br>
	 * This method requires authentication and is restricted for <code>HTTPS</code> channel.<br>
	 * Method type : <code>PUT</code>.
	 * 
	 * @return <code>true</code> if paymentInfo was assigned to the session cart.
	 * @throws InvalidPaymentInfoException
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/paymentinfo/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public CartData setPaymentDetails(@PathVariable final String id) throws InvalidPaymentInfoException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("setPaymentDetails : id = " + id);
		}
		if (checkoutFacade.setPaymentDetails(id))
		{
			return getSessionCart();
		}
		throw new InvalidPaymentInfoException(id);
	}

	/**
	 * Web service for getting all supported delivery modes for the session cart.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/deliverymodes <br>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.<br>
	 * This method requires authentication and is restricted to <code>HTTPS<code> channel only.<br>
	 * Method type : <code>GET</code>.
	 * 
	 * 
	 * @return List of {@link DeliveryModeData} as response body.
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/deliverymodes", method = RequestMethod.GET)
	@ResponseBody
	public DeliveryModesData getSupportedDeliveryModes()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getSupportedDeliveryModes");
		}
		final DeliveryModesData deliveryModesData = new DeliveryModesData();
		deliveryModesData.setDeliveryModes(checkoutFacade.getSupportedDeliveryModes());
		return deliveryModesData;
	}

	/**
	 * Web service for authorizing cart's credit cart payment.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/authorizePayment <br>
	 * authorization security code - ccv - must be sent as a post body.<br>
	 * Response contains a set-cookie header with the jsessionId associated with the cart.<br>
	 * This method requires authentication and is restricted to <code>HTTPS<code> channel only.<br>
	 * Method type : <code>POST</code>.
	 * 
	 * @return true if the payment was authorized
	 * @throws PaymentAuthorizationException
	 */
	@Secured("ROLE_CUSTOMERGROUP")
	@RequestMapping(value = "/authorize", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.ACCEPTED)
	public CartData authorizePayment(@RequestParam(required = true) final String securityCode)
			throws PaymentAuthorizationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("authorizePayment");
		}
		if (checkoutFacade.authorizePayment(securityCode))
		{
			return getSessionCart();
		}
		throw new PaymentAuthorizationException();
	}

	/**
	 * Web service for restoring anonymous cart by guid.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/restore <br>
	 * This method requires authentication and is restricted to <code>HTTPS<code> channel only.<br>
	 * Method type : <code>GET</code>.
	 * 
	 * @param guid
	 * 
	 * @return {@link CartRestorationData}
	 * @throws CommerceCartRestorationException
	 */
	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/restore", method = RequestMethod.GET)
	@ResponseBody
	public CartRestorationData restoreCart(@RequestParam final String guid) throws CommerceCartRestorationException
	{
		final CartModel cartModel = commerceCartService.getCartForGuidAndSiteAndUser(guid, baseSiteService.getCurrentBaseSite(),
				userService.getAnonymousUser());

		if (cartModel == null)
		{
			throw new CommerceCartRestorationException("Cannot find cart for a given guid: " + guid);
		}

		return cartRestorationConverter.convert(commerceCartService.restoreCart(cartModel));
	}

	/**
	 * Web service for enabling order promotions.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/promotion/{promotionCode}<br>
	 * This method requires authentication and is restricted to <code>HTTPS<code> channel only.<br>
	 * Method type : <code>POST</code>.
	 * 
	 * @param promotionCode
	 *           promotion code
	 * @return {@link CartData}
	 * @throws {@link CommercePromotionRestrictionException}
	 */
	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/promotion/{promotionCode}", method = RequestMethod.POST)
	@ResponseBody
	public CartData applyPromotion(@PathVariable final String promotionCode) throws CommercePromotionRestrictionException
	{
		commercePromotionRestrictionFacade.enablePromotionForCurrentCart(promotionCode);
		return getSessionCart();
	}

	/**
	 * Web service for disabling order promotions.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/promotion/{promotionCode}<br>
	 * This method requires authentication and is restricted to <code>HTTPS<code> channel only.<br>
	 * Method type : <code>DELETE</code>.
	 * 
	 * @param promotionCode
	 *           promotion code
	 * @return {@link CartData}
	 * @throws {@link CommercePromotionRestrictionException}
	 */
	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/promotion/{promotionCode}", method = RequestMethod.DELETE)
	@ResponseBody
	public CartData removePromotion(@PathVariable final String promotionCode) throws CommercePromotionRestrictionException
	{
		commercePromotionRestrictionFacade.disablePromotionForCurrentCart(promotionCode);
		return getSessionCart();
	}

	/**
	 * Web service for applying voucher to cart.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/voucher/abc-9PSW-EDH2-RXKA <br>
	 * This method requires authentication.<br>
	 * Method type : <code>POST</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * 
	 * 
	 * @return cart data with applied voucher.
	 * @throws NoCheckoutCartException
	 * @throws VoucherOperationException
	 */
	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/voucher/{voucherCode}", method = RequestMethod.POST)
	@ResponseBody
	public CartData applyVoucherForCart(@PathVariable final String voucherCode) throws NoCheckoutCartException,
			VoucherOperationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("apply voucher : voucherCode = " + voucherCode);
		}
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot apply voucher. There was no checkout cart created yet!");
		}

		voucherFacade.applyVoucher(voucherCode);
		return getSessionCart();
	}

	/**
	 * Web service for removing voucher from cart.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/cart/voucher/abc-9PSW-EDH2-RXKA <br>
	 * This method requires authentication.<br>
	 * Method type : <code>DELETE</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * @return updated cart data.
	 * @throws NoCheckoutCartException
	 * @throws VoucherOperationException
	 */
	@Secured(
	{ "ROLE_CLIENT", "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/voucher/{voucherCode}", method = RequestMethod.DELETE)
	@ResponseBody
	public CartData releaseVoucherFromCart(@PathVariable final String voucherCode) throws NoCheckoutCartException,
			VoucherOperationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("release voucher : voucherCode = " + voucherCode);
		}
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot realese voucher. There was no checkout cart created yet!");
		}
		voucherFacade.releaseVoucher(voucherCode);
		return getSessionCart();
	}

	/**
	 * Handles Cart controller specific exceptions
	 * 
	 * @param excp
	 *           to support basic exception marshaling to JSON and XML. It will determine the format based on the
	 *           request's Accept header.
	 * @param request
	 * @param writer
	 * @throws IOException
	 * 
	 * @return {@link ErrorData} as a response body
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(
	{ CommerceCartModificationException.class, InvalidCartException.class, UnsupportedDeliveryAddressException.class,
			UnsupportedDeliveryModeException.class, InvalidPaymentInfoException.class, PaymentAuthorizationException.class,
			NoCheckoutCartException.class, CommerceCartRestorationException.class, CommercePromotionRestrictionException.class,
			VoucherOperationException.class })
	public ErrorData handleCartException(final Exception excp, final HttpServletRequest request, final Writer writer)
			throws IOException
	{
		LOG.info("Handling Exception for this request - " + excp.getClass().getSimpleName() + " - " + excp.getMessage());
		if (LOG.isDebugEnabled())
		{
			LOG.debug(excp);
		}
		final ErrorData errorData = handleErrorInternal(excp.getClass().getSimpleName(), excp.getMessage());
		return errorData;
	}

}
