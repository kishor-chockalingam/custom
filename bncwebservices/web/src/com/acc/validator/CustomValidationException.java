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
package com.acc.validator;

import org.springframework.validation.Errors;


/**
 * 
 */
public class CustomValidationException extends RuntimeException
{
	private final Errors errors;

	public CustomValidationException(final Errors errors)
	{
		super();
		this.errors = errors;
	}

	/**
	 * @param message
	 * @param errors
	 */
	public CustomValidationException(final String message, final Errors errors)
	{
		super(message);
		this.errors = errors;
	}

	/**
	 * @return the errors
	 */
	public Errors getErrors()
	{
		return errors;
	}

}
