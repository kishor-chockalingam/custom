/**
 * 
 */
package com.acc.facades.wishlist.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import javax.annotation.Resource;

import org.springframework.util.Assert;

import com.acc.facades.wishlist.data.Wishlist2Data;
import com.acc.facades.wishlist.data.Wishlist2EntryData;


/**
 * @author swapnil.a.pandey
 * 
 */
public class Wishlist2Populator implements Populator<Wishlist2Model, Wishlist2Data>
{

	@Resource(name = "Wishlist2EntryConverter")
	Converter<Wishlist2EntryModel, Wishlist2EntryData> Wishlist2EntryConverter;


	@Override
	public void populate(final Wishlist2Model source, final Wishlist2Data target) throws ConversionException
	{

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setEntries(Converters.convertAll(source.getEntries(), Wishlist2EntryConverter));



	}


}
