/**
 * 
 */
package com.acc.facades.wishlist;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import com.acc.facades.wishlist.data.Wishlist2Data;


/**
 * @author swapnil.a.pandey
 * 
 */
public interface WishlistFacade
{
	public Wishlist2Model CreateDefaultWishlist();

	public Wishlist2Data addWishlistEntry(final ProductModel model);

	public Wishlist2Data RemoveWishlistData(final ProductModel model);

	/**
	 * @param model
	 * @return
	 */

	/**
	 * @param model
	 * @return
	 */

}
