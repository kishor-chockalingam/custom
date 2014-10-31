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

import de.hybris.platform.jalo.JaloSession;
import com.acc.auth.data.LogoutResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/v1/customers")
public class LogoutController
{
	/**
	 * Spring security logout successful redirect.
	 */
	@RequestMapping(value = "/current/logout", method = RequestMethod.POST)
	@ResponseBody
	public LogoutResponse logout(final HttpServletRequest request)
	{
		JaloSession.getCurrentSession().close();
		request.getSession().invalidate();
		final LogoutResponse logoutResponse = new LogoutResponse();
		logoutResponse.setSuccess(true);
		return logoutResponse;
	}
}
