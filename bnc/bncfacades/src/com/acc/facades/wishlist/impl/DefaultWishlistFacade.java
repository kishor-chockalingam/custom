/**
 * 
 */
package com.acc.facades.wishlist.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.acc.facades.wishlist.WishlistFacade;
import com.acc.facades.wishlist.data.Wishlist2Data;


/**
 * @author swapnil.a.pandey
 * 
 */
public class DefaultWishlistFacade implements WishlistFacade
{
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "wishlistService")
	private Wishlist2Service wishlistService;

	@Autowired
	private ModelService modelService;

	@Resource(name = "Wishlist2Converter")
	private Converter<Wishlist2Model, Wishlist2Data> Wishlist2Converter;


	/*
	 * @Resource(name = "wishlist2EntryConverter") private Converter<Wishlist2EntryModel, Wishlist2EntryData>
	 * wishlist2EntryConverter; private Wishlist2EntryPopulator wishlist2EntryPopulator;
	 */
	/*
	 * @Override public Wishlist2Model CreateWishlist(final ProductModel model) { final UserModel userModel =
	 * userService.getCurrentUser();
	 * 
	 * final Wishlist2Model wishlistEntry = wishlist2Service.createWishlist(userModel, "swapnil",
	 * "she is very good girl"); return wishlistEntry;
	 * 
	 * }
	 */

	@Override
	public Wishlist2Model CreateDefaultWishlist()
	{


		final UserModel userModel = userService.getCurrentUser();
		Wishlist2Model wishlistEntry = null;

		if (wishlistService.hasDefaultWishlist(userModel) == false)
		{
			wishlistEntry = wishlistService.createDefaultWishlist(userModel, "wishlist", "add to wishlist fuunctionality");

			System.out.println("inside create deafultwishlist");
		}

		return wishlistEntry;
	}


	@Override
	public Wishlist2Data addWishlistEntry(final ProductModel model)
	{
		boolean flag = false;
		final UserModel userModel = userService.getCurrentUser();

		@SuppressWarnings("deprecation")
		final ProductModel productModel = productService.getProductForCode(model.getCode());
		final Wishlist2Model wishLists = wishlistService.getDefaultWishlist(userModel);

		/*
		 * final List<Wishlist2EntryModel> wishLists1 = null;
		 */System.out.println("inside add deafultwishlist1");

		final List<Wishlist2EntryModel> entries = wishLists.getEntries();
		for (final Wishlist2EntryModel entry : entries)
		{
			System.out.println("inside for loop" + entries);
			if (entry.getProduct().getCode().equals(model.getCode()))
			{
				System.out.println("inside if loop" + wishLists);
				flag = true;
				break;
			}
		}


		if (!flag)
		{
			final Wishlist2EntryModel productEnteries = new Wishlist2EntryModel();

			productEnteries.setProduct(productModel);
			productEnteries.setPriority(Wishlist2EntryPriority.LOW);
			productEnteries.setAddedDate(new Date());

			wishlistService.addWishlistEntry(wishLists, productEnteries);
			System.out.println("inside if loop" + wishLists.getEntries());
		}

		return Wishlist2Converter.convert(wishLists);
	}

	@Override
	public Wishlist2Data RemoveWishlistData(final ProductModel model)
	{
		final UserModel userModel = userService.getCurrentUser();
		final Wishlist2Model wishLists = wishlistService.getDefaultWishlist(userModel);
		final Wishlist2EntryModel productEnteries = new Wishlist2EntryModel();
		final ProductModel productModel = productService.getProductForCode(model.getCode());
		productEnteries.setProduct(productModel);
		productEnteries.setPriority(Wishlist2EntryPriority.LOW);
		productEnteries.setAddedDate(new Date());
		wishlistService.removeWishlistEntryForProduct(productModel, wishLists);
		//wishlistService.removeWishlistEntry(wishLists, productEnteries);
		return Wishlist2Converter.convert(wishLists);
	}

	/*
	 * public Wishlist2EntryData addWishlistEntry() { final Wishlist2Model wishLists =
	 * wishlistService.getDefaultWishlist();
	 * 
	 * final List<Wishlist2EntryModel> entrymodel = wishLists.getEntries(); final Wishlist2EntryData wishlist2EntryData =
	 * new Wishlist2EntryData(); for (final Wishlist2EntryModel wishlistModel : entrymodel) {
	 * 
	 * 
	 * 
	 * wishlist2EntryPopulator.populate(wishlistModel, wishlist2EntryData); }
	 * 
	 * 
	 * 
	 * 
	 * return wishlist2EntryData; }
	 */
}
