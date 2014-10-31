/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Oct 15, 2014 12:56:39 PM                    ---
 * ----------------------------------------------------------------
 */
package com.acc.core.jalo;

import com.acc.core.constants.BncCoreConstants;
import com.acc.core.jalo.ApparelProduct;
import com.acc.core.jalo.ApparelSizeVariantProduct;
import com.acc.core.jalo.ApparelStyleVariantProduct;
import com.acc.core.jalo.CSRCustomerDetails;
import com.acc.core.jalo.Config;
import com.acc.core.jalo.ElectronicsColorVariantProduct;
import de.hybris.platform.cms2lib.components.RecentlyViewedProductsComponent;
import de.hybris.platform.cms2lib.components.ViewWishlistComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>BncCoreManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedBncCoreManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("recentlyviewedproducts", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.user.User", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("UUID", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.user.Customer", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public ApparelProduct createApparelProduct(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( BncCoreConstants.TC.APPARELPRODUCT );
			return (ApparelProduct)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ApparelProduct : "+e.getMessage(), 0 );
		}
	}
	
	public ApparelProduct createApparelProduct(final Map attributeValues)
	{
		return createApparelProduct( getSession().getSessionContext(), attributeValues );
	}
	
	public ApparelSizeVariantProduct createApparelSizeVariantProduct(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( BncCoreConstants.TC.APPARELSIZEVARIANTPRODUCT );
			return (ApparelSizeVariantProduct)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ApparelSizeVariantProduct : "+e.getMessage(), 0 );
		}
	}
	
	public ApparelSizeVariantProduct createApparelSizeVariantProduct(final Map attributeValues)
	{
		return createApparelSizeVariantProduct( getSession().getSessionContext(), attributeValues );
	}
	
	public ApparelStyleVariantProduct createApparelStyleVariantProduct(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( BncCoreConstants.TC.APPARELSTYLEVARIANTPRODUCT );
			return (ApparelStyleVariantProduct)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ApparelStyleVariantProduct : "+e.getMessage(), 0 );
		}
	}
	
	public ApparelStyleVariantProduct createApparelStyleVariantProduct(final Map attributeValues)
	{
		return createApparelStyleVariantProduct( getSession().getSessionContext(), attributeValues );
	}
	
	public Config createConfig(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( BncCoreConstants.TC.CONFIG );
			return (Config)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating Config : "+e.getMessage(), 0 );
		}
	}
	
	public Config createConfig(final Map attributeValues)
	{
		return createConfig( getSession().getSessionContext(), attributeValues );
	}
	
	public CSRCustomerDetails createCSRCustomerDetails(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( BncCoreConstants.TC.CSRCUSTOMERDETAILS );
			return (CSRCustomerDetails)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating CSRCustomerDetails : "+e.getMessage(), 0 );
		}
	}
	
	public CSRCustomerDetails createCSRCustomerDetails(final Map attributeValues)
	{
		return createCSRCustomerDetails( getSession().getSessionContext(), attributeValues );
	}
	
	public ElectronicsColorVariantProduct createElectronicsColorVariantProduct(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( BncCoreConstants.TC.ELECTRONICSCOLORVARIANTPRODUCT );
			return (ElectronicsColorVariantProduct)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ElectronicsColorVariantProduct : "+e.getMessage(), 0 );
		}
	}
	
	public ElectronicsColorVariantProduct createElectronicsColorVariantProduct(final Map attributeValues)
	{
		return createElectronicsColorVariantProduct( getSession().getSessionContext(), attributeValues );
	}
	
	public RecentlyViewedProductsComponent createRecentlyViewedProductsComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( BncCoreConstants.TC.RECENTLYVIEWEDPRODUCTSCOMPONENT );
			return (RecentlyViewedProductsComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating RecentlyViewedProductsComponent : "+e.getMessage(), 0 );
		}
	}
	
	public RecentlyViewedProductsComponent createRecentlyViewedProductsComponent(final Map attributeValues)
	{
		return createRecentlyViewedProductsComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public ViewWishlistComponent createViewWishlistComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( BncCoreConstants.TC.VIEWWISHLISTCOMPONENT );
			return (ViewWishlistComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ViewWishlistComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ViewWishlistComponent createViewWishlistComponent(final Map attributeValues)
	{
		return createViewWishlistComponent( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return BncCoreConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.recentlyviewedproducts</code> attribute.
	 * @return the recentlyviewedproducts
	 */
	public Collection<Product> getRecentlyviewedproducts(final SessionContext ctx, final User item)
	{
		Collection<Product> coll = (Collection<Product>)item.getProperty( ctx, BncCoreConstants.Attributes.User.RECENTLYVIEWEDPRODUCTS);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>User.recentlyviewedproducts</code> attribute.
	 * @return the recentlyviewedproducts
	 */
	public Collection<Product> getRecentlyviewedproducts(final User item)
	{
		return getRecentlyviewedproducts( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.recentlyviewedproducts</code> attribute. 
	 * @param value the recentlyviewedproducts
	 */
	public void setRecentlyviewedproducts(final SessionContext ctx, final User item, final Collection<Product> value)
	{
		item.setProperty(ctx, BncCoreConstants.Attributes.User.RECENTLYVIEWEDPRODUCTS,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>User.recentlyviewedproducts</code> attribute. 
	 * @param value the recentlyviewedproducts
	 */
	public void setRecentlyviewedproducts(final User item, final Collection<Product> value)
	{
		setRecentlyviewedproducts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.UUID</code> attribute.
	 * @return the UUID - Customer universally unique id for a device
	 */
	public String getUUID(final SessionContext ctx, final Customer item)
	{
		return (String)item.getProperty( ctx, BncCoreConstants.Attributes.Customer.UUID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Customer.UUID</code> attribute.
	 * @return the UUID - Customer universally unique id for a device
	 */
	public String getUUID(final Customer item)
	{
		return getUUID( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.UUID</code> attribute. 
	 * @param value the UUID - Customer universally unique id for a device
	 */
	public void setUUID(final SessionContext ctx, final Customer item, final String value)
	{
		item.setProperty(ctx, BncCoreConstants.Attributes.Customer.UUID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Customer.UUID</code> attribute. 
	 * @param value the UUID - Customer universally unique id for a device
	 */
	public void setUUID(final Customer item, final String value)
	{
		setUUID( getSession().getSessionContext(), item, value );
	}
	
}
