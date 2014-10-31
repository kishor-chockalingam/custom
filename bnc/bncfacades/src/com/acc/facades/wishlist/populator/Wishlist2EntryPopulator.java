/**
 * 
 */
package com.acc.facades.wishlist.populator;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import com.acc.facades.wishlist.data.Wishlist2EntryData;


/**
 * @author swapnil.a.pandey
 * 
 */
public class Wishlist2EntryPopulator implements Populator<Wishlist2EntryModel, Wishlist2EntryData>
{
	@Resource(name = "productFacade")
	ProductFacade productFacade;

	@Resource(name = "productService")
	ProductService productService;


	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final Wishlist2EntryModel source, final Wishlist2EntryData target) throws ConversionException
	{
		// YTODO Auto-generated method stu
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");


		final ProductModel productModel = productService.getProductForCode(source.getProduct().getCode());




		final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
				ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
				ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW, ProductOption.DELIVERY_MODE_AVAILABILITY,
				ProductOption.GALLERY, ProductOption.IMAGES, ProductOption.URL));

		target.setProduct(productData);


	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */

}
