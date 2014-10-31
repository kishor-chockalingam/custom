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
package com.acc.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.acc.storefront.controllers.ControllerConstants;
import com.acc.storefront.forms.CsrLoginForm;


/**
 * Login Controller. Handles login and register for the account flow.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/login")
public class LoginPageController extends AbstractLoginPageController
{
	private HttpSessionRequestCache httpSessionRequestCache;
	private static final String ACCOUNT_CMS_PAGE = "account";
	private static final String REDIRECT_TO_CUSTOMER_DETAILS = REDIRECT_PREFIX + "/customerlist/customerdeatils";
	@Autowired
	private UserService userService;
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private SessionService sessionService;
	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Account.AccountLoginPage;
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (httpSessionRequestCache.getRequest(request, response) != null)
		{
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}
		return "/my-account";
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("login");
	}


	@Resource(name = "httpSessionRequestCache")
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doLogin(@RequestHeader(value = "referer", required = false) final String referer,
			@RequestParam(value = "error", defaultValue = "false") final boolean loginError, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final HttpSession session)
			throws CMSItemNotFoundException
	{
		if (!loginError)
		{
			storeReferer(referer, request, response);
		}
		return getDefaultLoginPage(loginError, session, model);
	}

	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(referer) && !StringUtils.endsWith(referer, "/login"))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doRegister(@RequestHeader(value = "referer", required = false) final String referer, final RegisterForm form,
			final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		getRegistrationValidator().validate(form, bindingResult);
		return processRegisterUserRequest(referer, form, bindingResult, model, request, response, redirectModel);
	}

	@RequestMapping(value = "/csrLogin", method = RequestMethod.GET)
	public String csrLogin(final CsrLoginForm form, final Model model) throws CMSItemNotFoundException
	{
		if (null != sessionService.getAttribute("CSR_USER") && null != sessionService.getAttribute("POINT_OF_SERVICE"))
		{
			return REDIRECT_TO_CUSTOMER_DETAILS;
		}

		model.addAttribute("csrLoginForm", form);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		return ControllerConstants.Views.Pages.Account.csrLoginPage;
	}

	@RequestMapping(value = "/csrLogin", method = RequestMethod.POST)
	public String csrLogin(final CsrLoginForm form, final Model model, final BindingResult bindingResult)
			throws CMSItemNotFoundException
	{
		String returnAction = ControllerConstants.Views.Pages.Account.csrLoginPage;
		try
		{
			final PointOfServiceModel tempPointOfService = new PointOfServiceModel();
			tempPointOfService.setName(form.getPos());
			final PointOfServiceModel pointOfServiceModel = flexibleSearchService.getModelByExample(tempPointOfService);
			System.out.println("Point of service [ " + pointOfServiceModel + " ].");
			final UserModel userModel = userService.getUserForUID(form.getUsername());
			final UserGroupModel userGroupModel = userService.getUserGroupForUID("csrGroup");
			if ((null != userGroupModel) && (userService.isMemberOfGroup(userModel, userGroupModel)))
			{
				final String pwd = userService.getPassword(userModel);
				if (null != pwd && pwd.equals(form.getPassword()))
				{
					sessionService.setAttribute("POINT_OF_SERVICE", form.getPos());
					sessionService.setAttribute("CSR_USER", form.getUsername());
					return REDIRECT_TO_CUSTOMER_DETAILS;
				}
				else
				{

					bindingResult.rejectValue("password", "login.error.account.not.found.title", new Object[] {},
							"login.error.account.not.found.title");
				}
			}
			else
			{
				bindingResult.rejectValue("username", "login.error.group.not.found", new Object[] {}, "login.error.group.not.found");
			}
		}
		catch (final UnknownIdentifierException e)
		{
			bindingResult.rejectValue("username", "login.error.user.not.found", new Object[] {}, "login.error.user.not.found");
		}
		catch (final ModelNotFoundException e)
		{
			bindingResult.rejectValue("pos", "login.error.pos.not.found", new Object[] {}, "login.error.pos.not.found");
		}
		if (bindingResult.hasErrors())
		{
			returnAction = csrPageError(model);
		}
		else
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		}
		model.addAttribute("csrLoginForm", form);
		return returnAction;
	}

	protected String csrPageError(final Model model) throws CMSItemNotFoundException
	{
		final String returnAction;
		GlobalMessages.addErrorMessage(model, "form.global.error");
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("CSR Login"));
		returnAction = ControllerConstants.Views.Pages.Account.csrLoginPage;
		return returnAction;
	}
}
