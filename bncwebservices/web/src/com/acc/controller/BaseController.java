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

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import com.acc.error.data.ErrorData;
import com.acc.error.data.ValidationErrorData;
import com.acc.populator.options.PaymentInfoOption;
import com.acc.validator.CustomValidationException;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Base Controller. It defines the exception handler to be used by all controllers. Extending controllers can add or
 * overwrite the exception handler if needed.
 */
@Controller
public class BaseController
{

	private static final Logger LOG = Logger.getLogger(BaseController.class);

	@Resource(name = "httpRequestPaymentInfoPopulator")
	private ConfigurablePopulator<HttpServletRequest, CCPaymentInfoData, PaymentInfoOption> httpRequestPaymentInfoPopulator;

	@Resource(name = "ccPaymentInfoValidator")
	private Validator ccPaymentInfoValidator;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "commerceCommonI18NService")
	CommerceCommonI18NService commerceCommonI18NService;

	/**
	 * 
	 * @param excp
	 *           to support basic exception marshaling to JSON and XML. It will determine the format based on the
	 *           request's Accept header.
	 * @param request
	 * @param writer
	 * @throws IOException
	 * @return {@link ErrorData} as a response body
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(
	{ IllegalArgumentException.class, UnknownIdentifierException.class, AmbiguousIdentifierException.class,
			ModelSavingException.class, JaloInvalidParameterException.class, DuplicateUidException.class,
			PasswordMismatchException.class, ModelNotFoundException.class, ConversionException.class })
	public ErrorData handleException(final Exception excp, final HttpServletRequest request, final Writer writer)
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

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(CustomValidationException.class)
	public ValidationErrorData handleValidationException(final Exception excp, final HttpServletRequest request,
			final Writer writer)
	{
		LOG.info("Handling Exception for this request - " + excp.getClass().getSimpleName() + " - " + excp.getMessage());
		if (LOG.isDebugEnabled())
		{
			LOG.debug(excp);
		}
		final ValidationErrorData errorData = handleValidationErrorInternal(excp.getClass().getSimpleName(), excp.getMessage(),
				((CustomValidationException) excp).getErrors());
		return errorData;
	}

	protected ErrorData handleErrorInternal(final String errorClass, final String errorMsg)
	{
		final ErrorData errorData = new ErrorData();
		errorData.setClassName(errorClass);
		errorData.setMessage(errorMsg);
		return errorData;
	}

	protected ValidationErrorData handleValidationErrorInternal(final String errorClass, final String errorMsg, final Errors errors)
	{
		final Locale currentLocale = commerceCommonI18NService.getCurrentLocale();
		final ValidationErrorData validationErrorData = new ValidationErrorData();
		validationErrorData.setClassName(errorClass);
		validationErrorData.setMessage(errorMsg);
		final List<String> validationErrors = new ArrayList<String>();
		for (final ObjectError error : errors.getGlobalErrors())
		{
			final String validationError = getMessage(error.getCode(), error.getArguments(), currentLocale);
			validationErrors.add(validationError);
		}
		for (final FieldError error : errors.getFieldErrors())
		{
			final String validationError = createValidationError(error.getField(),
					getMessage(error.getCode(), error.getArguments(), currentLocale));
			validationErrors.add(validationError);
		}
		validationErrorData.setValidationErrors(validationErrors);
		return validationErrorData;
	}

	protected String createValidationError(final String field, final String message)
	{
		return field + " : " + message;
	}

	protected String getMessage(final String code, final Locale locale)
	{
		return messageSource.getMessage(code, new Object[0], locale);
	}

	protected String getMessage(final String code, final Object[] args, final Locale locale)
	{
		return messageSource.getMessage(code, args, locale);
	}

	protected Validator getCCPaymentInfoValidator()
	{
		return ccPaymentInfoValidator;
	}

	protected ConfigurablePopulator<HttpServletRequest, CCPaymentInfoData, PaymentInfoOption> getCCPaymentInfoPopulator()
	{
		return httpRequestPaymentInfoPopulator;
	}
}
