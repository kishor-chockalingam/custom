/**
 * 
 */
package com.acc.storefront.controllers.misc;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.acc.facades.wishlist.WishlistFacade;
import com.acc.facades.wishlist.data.Wishlist2Data;
import com.acc.storefront.controllers.ControllerConstants;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
/**
 * @author swapnil.a.pandey
 * 
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/wishlist")
public class AddToWishlistController extends AbstractPageController
{
	/**
	 * @return the productService
	 */
	@Resource(name = "productService")
	ProductService productService;
	@Resource(name = "wishlistFacade")
	WishlistFacade wishlistFacade;
	private static final String ADD_EDIT_ADDRESS_CMS_PAGE = "add-edit-address";
	@Resource(name = "wishlist2Populator")
	private Populator<Wishlist2Model, Wishlist2Data> wishlist2Populator;







	protected static final Logger LOG = Logger.getLogger(AddToWishlistController.class);


	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String addToWishlist(@RequestParam("productCodePost") final String code, @RequestParam("wl") final String addOrRemove,
			final Model model
	/* final BindingResult bindingResult */) throws CMSItemNotFoundException
	{


		System.out.println("inside add to wishlist controller");

		final ProductModel productModel = productService.getProductForCode(code);
		final Wishlist2Data wishlist2Entries;

		if ("add".equalsIgnoreCase(addOrRemove))
		{

			final Wishlist2Model wishlist2Model = wishlistFacade.CreateDefaultWishlist();

			wishlist2Entries = wishlistFacade.addWishlistEntry(productModel);

			model.addAttribute("wishlist2Model", wishlist2Model);
			//model.addAttribute("wishlist2Model1", wishlist2Entries);

		}

		else
		{
			wishlist2Entries = wishlistFacade.RemoveWishlistData(productModel);


			//model.addAttribute("Removewishlist", wishlist2Entries);
			//model.addAttribute("wishlist2Model1", wishlist2Entries);


		}


		model.addAttribute("wishlist2Model1", wishlist2Entries);


		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));





		return ControllerConstants.Views.Fragments.Cart.WishlistPopup;
	}

	/*
	 * @RequestMapping(value = "/remove", method = RequestMethod.GET) public String
	 * RemoveWishlist(@RequestParam("productCodePost") final String code, final Model model) throws
	 * CMSItemNotFoundException { final ProductModel productModel = productService.getProductForCode(code);
	 * 
	 * 
	 * storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
	 * setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return ControllerConstants.Views.Fragments.Cart.WishlistPopup;
	 * 
	 * 
	 * }
	 */

}