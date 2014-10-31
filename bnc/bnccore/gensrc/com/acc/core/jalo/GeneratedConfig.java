/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Oct 15, 2014 12:56:39 PM                    ---
 * ----------------------------------------------------------------
 */
package com.acc.core.jalo;

import com.acc.core.constants.BncCoreConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem Config}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedConfig extends GenericItem
{
	/** Qualifier of the <code>Config.ValueHolder</code> attribute **/
	public static final String VALUEHOLDER = "ValueHolder";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(VALUEHOLDER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Config.ValueHolder</code> attribute.
	 * @return the ValueHolder
	 */
	public Integer getValueHolder(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, VALUEHOLDER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Config.ValueHolder</code> attribute.
	 * @return the ValueHolder
	 */
	public Integer getValueHolder()
	{
		return getValueHolder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Config.ValueHolder</code> attribute. 
	 * @return the ValueHolder
	 */
	public int getValueHolderAsPrimitive(final SessionContext ctx)
	{
		Integer value = getValueHolder( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Config.ValueHolder</code> attribute. 
	 * @return the ValueHolder
	 */
	public int getValueHolderAsPrimitive()
	{
		return getValueHolderAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Config.ValueHolder</code> attribute. 
	 * @param value the ValueHolder
	 */
	public void setValueHolder(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, VALUEHOLDER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Config.ValueHolder</code> attribute. 
	 * @param value the ValueHolder
	 */
	public void setValueHolder(final Integer value)
	{
		setValueHolder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Config.ValueHolder</code> attribute. 
	 * @param value the ValueHolder
	 */
	public void setValueHolder(final SessionContext ctx, final int value)
	{
		setValueHolder( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Config.ValueHolder</code> attribute. 
	 * @param value the ValueHolder
	 */
	public void setValueHolder(final int value)
	{
		setValueHolder( getSession().getSessionContext(), value );
	}
	
}
