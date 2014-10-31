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

import java.io.IOException;
import java.io.Writer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import com.acc.error.data.ErrorData;


/**
 * Main Controller for Vouchers
 */
@Controller
@RequestMapping(value = "/v1/{baseSiteId}/vouchers")
public class VouchersController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(VouchersController.class);
	@Resource(name = "voucherFacade")
	private VoucherFacade voucherFacade;

	/**
	 * Web service for getting voucher information by voucher code.<br>
	 * Sample call: https://localhost:9002/rest/v1/mysite/vouchers/abc-9PSW-EDH2-RXKA <br>
	 * This method requires authentication.<br>
	 * Method type : <code>GET</code>.<br>
	 * Method is restricted for <code>HTTPS</code> channel.
	 * 
	 * @param code
	 *           - voucher code - must be given as path variable.
	 * 
	 * @return {@link VoucherData} which will be marshaled to JSON or XML based on Accept-Header
	 * @throws {@link VoucherOperationException} if voucher cannot be found
	 */
	@Secured("ROLE_TRUSTED_CLIENT")
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	@ResponseBody
	public VoucherData getVoucherByCode(@PathVariable final String code) throws VoucherOperationException
	{
		return voucherFacade.getVoucher(code);
	}

	/**
	 * Handles Vouchers controller specific exceptions
	 * 
	 * @param excp
	 *           to support basic exception marshaling to JSON and XML. It will determine the format based on the
	 *           request's Accept header.
	 * @param request
	 * @param writer
	 * @throws java.io.IOException
	 * 
	 * @return {@link com.acc.error.data.ErrorData} as a response body
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(
	{ VoucherOperationException.class })
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
