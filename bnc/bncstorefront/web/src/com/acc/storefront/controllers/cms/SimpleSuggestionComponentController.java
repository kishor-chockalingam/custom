/**
 * 
 */
package com.acc.storefront.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.SimpleSuggestionComponentModel;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
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
import java.util.Iterator;
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
@Controller("SimpleSuggestionComponentController")
@Scope("tenant")
@RequestMapping(value = "view/SimpleSuggestionComponentController")
public class SimpleSuggestionComponentController extends AbstractCMSComponentController<SimpleSuggestionComponentModel>
{
	private static final Logger LOG = Logger.getLogger(SimpleSuggestionComponentController.class);
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
	protected void fillModel(final HttpServletRequest request, final Model model, final SimpleSuggestionComponentModel component)
	{

		final List<ProductData> products = new ArrayList<ProductData>();
		LOG.info("inside SimpleSuggestionComponentController's fill model of simple controller");

		final CSRCustomerDetailsModel csrCustomerDetailsModel = modelService.get(PK.parse(request.getParameter("customerPK")));
		final UserModel userModel = userService.getUserForUID(csrCustomerDetailsModel.getCustomerId());
		if (userModel instanceof CustomerModel)
		{
			final CustomerModel customerModel = (CustomerModel) userModel;
			if (null != customerModel.getRecentlyviewedproducts())
			{

				final List<ProductModel> productslist = (List<ProductModel>) customerModel.getRecentlyviewedproducts();

				for (final ProductModel productModel : productslist)
				{
					final Iterator<ProductReferenceModel> iterator = productModel.getProductReferences().iterator();
					while (iterator.hasNext())
					{
						final ProductReferenceModel productReferenceModel = iterator.next();
						final ProductReferenceTypeEnum productReferenceTypeEnum = productReferenceModel.getReferenceType();
						final ProductData productData = productFacade.getProductForOptions(productReferenceModel.getTarget(),
								PRODUCT_OPTIONS);
						if (productReferenceTypeEnum.equals(ProductReferenceTypeEnum.SIMILAR) && !(productslist.contains(productData)))
						{
							products.add(productData);
							break;
						}

					}

				}
			}
		}

		//model.addAttribute("title", "Recommended products");
		model.addAttribute("productData", products);


	}
}
