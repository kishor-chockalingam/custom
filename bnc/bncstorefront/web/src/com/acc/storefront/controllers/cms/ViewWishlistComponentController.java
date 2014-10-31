/**
 * 
 */

package com.acc.storefront.controllers.cms;

import de.hybris.platform.cms2lib.model.components.ViewWishlistComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.Arrays;
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
import com.acc.facades.wishlist.data.Wishlist2Data;


/**
 * @author swapnil.a.pandey
 * 
 */
@Controller("ViewWishlistComponentController")
@Scope("tenant")
@RequestMapping(value = "/view/ViewWishlistComponentController")
public class ViewWishlistComponentController extends AbstractCMSComponentController<ViewWishlistComponentModel>
{
	private static final Logger LOG = Logger.getLogger(ViewWishlistComponentController.class);
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "productFacade")
	private ProductFacade productFacade;
	@Autowired
	private ModelService modelService;
	@Resource(name = "wishlistService")
	private Wishlist2Service wishlistService;
	@Resource(name = "Wishlist2Converter")
	private Converter<Wishlist2Model, Wishlist2Data> Wishlist2Converter;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final ViewWishlistComponentModel component)
	{
		LOG.info("::::::::::::::: Inside ViewWishlistComponentController fill model :::::::::::::::::");

		final CSRCustomerDetailsModel csrCustomerDetailsModel = modelService.get(PK.parse(request.getParameter("customerPK")));
		final UserModel userModel = userService.getUserForUID(csrCustomerDetailsModel.getCustomerId());

		final Wishlist2Model wishlistModel = wishlistService.getDefaultWishlist(userModel);
		final List<Wishlist2EntryModel> wishlistEnteries = wishlistModel.getEntries();
		Wishlist2Data wishlistData = null;
		//final List<ProductData> products = new ArrayList<ProductData>();
		if (userModel instanceof CustomerModel)
		{

			if (null != wishlistEnteries)
			{
				//for (final Wishlist2EntryModel wishlist : wishlistEnteries)
				//	{

				wishlistData = Wishlist2Converter.convert(wishlistModel);



				//

				//}
			}
		}
		//Collections.reverse(wishlistData);
		//model.addAttribute("title", "Recently viewed products");
		model.addAttribute("wishlist", wishlistData);

	}
}
