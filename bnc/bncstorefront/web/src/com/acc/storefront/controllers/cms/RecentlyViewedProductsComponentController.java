/**
 * 
 */
package com.acc.storefront.controllers.cms;

import de.hybris.platform.cms2lib.model.components.RecentlyViewedProductsComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acc.core.model.CSRCustomerDetailsModel;


/**
 * @author jayanth.kotha
 * 
 */
@Controller("RecentlyViewedProductsComponentController")
@Scope("tenant")
@RequestMapping(value = "/view/RecentlyViewedProductsComponentController")
public class RecentlyViewedProductsComponentController extends
		AbstractCMSComponentController<RecentlyViewedProductsComponentModel>
{
	private static final Logger LOG = Logger.getLogger(RecentlyViewedProductsComponentController.class);
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "productFacade")
	private ProductFacade productFacade;
	@Autowired
	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.yacceleratorstorefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet
	 * .http.HttpServletRequest, org.springframework.ui.Model,
	 * de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final RecentlyViewedProductsComponentModel component)
	{
		LOG.info("::::::::::::::: Inside RecentlyViewedProductsComponentController fill model :::::::::::::::::");

		final CSRCustomerDetailsModel csrCustomerDetailsModel = modelService.get(PK.parse(request.getParameter("customerPK")));
		final UserModel userModel = userService.getUserForUID(csrCustomerDetailsModel.getCustomerId());
		final List<ProductData> products = new ArrayList<ProductData>();
		if (userModel instanceof CustomerModel)
		{
			final CustomerModel customerModel = (CustomerModel) userModel;
			if (null != customerModel.getRecentlyviewedproducts())
			{
				for (final ProductModel productModel : customerModel.getRecentlyviewedproducts())
				{
					products.add(productFacade.getProductForOptions(productModel, PRODUCT_OPTIONS));
				}
			}
		}
		Collections.reverse(products);
		//model.addAttribute("title", "Recently viewed products");
		model.addAttribute("productData", products);
		//
	}

}
