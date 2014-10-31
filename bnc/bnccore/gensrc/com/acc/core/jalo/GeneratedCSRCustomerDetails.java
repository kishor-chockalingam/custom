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
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem CSRCustomerDetails}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedCSRCustomerDetails extends GenericItem
{
	/** Qualifier of the <code>CSRCustomerDetails.loginTime</code> attribute **/
	public static final String LOGINTIME = "loginTime";
	/** Qualifier of the <code>CSRCustomerDetails.processedBy</code> attribute **/
	public static final String PROCESSEDBY = "processedBy";
	/** Qualifier of the <code>CSRCustomerDetails.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>CSRCustomerDetails.pointOfService</code> attribute **/
	public static final String POINTOFSERVICE = "pointOfService";
	/** Qualifier of the <code>CSRCustomerDetails.customerName</code> attribute **/
	public static final String CUSTOMERNAME = "customerName";
	/** Qualifier of the <code>CSRCustomerDetails.customerId</code> attribute **/
	public static final String CUSTOMERID = "customerId";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(LOGINTIME, AttributeMode.INITIAL);
		tmp.put(PROCESSEDBY, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		tmp.put(POINTOFSERVICE, AttributeMode.INITIAL);
		tmp.put(CUSTOMERNAME, AttributeMode.INITIAL);
		tmp.put(CUSTOMERID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.customerId</code> attribute.
	 * @return the customerId - Customer universally unique id for a device
	 */
	public String getCustomerId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.customerId</code> attribute.
	 * @return the customerId - Customer universally unique id for a device
	 */
	public String getCustomerId()
	{
		return getCustomerId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.customerId</code> attribute. 
	 * @param value the customerId - Customer universally unique id for a device
	 */
	public void setCustomerId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMERID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.customerId</code> attribute. 
	 * @param value the customerId - Customer universally unique id for a device
	 */
	public void setCustomerId(final String value)
	{
		setCustomerId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.customerName</code> attribute.
	 * @return the customerName
	 */
	public String getCustomerName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CUSTOMERNAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.customerName</code> attribute.
	 * @return the customerName
	 */
	public String getCustomerName()
	{
		return getCustomerName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.customerName</code> attribute. 
	 * @param value the customerName
	 */
	public void setCustomerName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CUSTOMERNAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.customerName</code> attribute. 
	 * @param value the customerName
	 */
	public void setCustomerName(final String value)
	{
		setCustomerName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.loginTime</code> attribute.
	 * @return the loginTime
	 */
	public Date getLoginTime(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, LOGINTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.loginTime</code> attribute.
	 * @return the loginTime
	 */
	public Date getLoginTime()
	{
		return getLoginTime( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.loginTime</code> attribute. 
	 * @param value the loginTime
	 */
	public void setLoginTime(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, LOGINTIME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.loginTime</code> attribute. 
	 * @param value the loginTime
	 */
	public void setLoginTime(final Date value)
	{
		setLoginTime( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.pointOfService</code> attribute.
	 * @return the pointOfService
	 */
	public String getPointOfService(final SessionContext ctx)
	{
		return (String)getProperty( ctx, POINTOFSERVICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.pointOfService</code> attribute.
	 * @return the pointOfService
	 */
	public String getPointOfService()
	{
		return getPointOfService( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.pointOfService</code> attribute. 
	 * @param value the pointOfService
	 */
	public void setPointOfService(final SessionContext ctx, final String value)
	{
		setProperty(ctx, POINTOFSERVICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.pointOfService</code> attribute. 
	 * @param value the pointOfService
	 */
	public void setPointOfService(final String value)
	{
		setPointOfService( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.processedBy</code> attribute.
	 * @return the processedBy
	 */
	public String getProcessedBy(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROCESSEDBY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.processedBy</code> attribute.
	 * @return the processedBy
	 */
	public String getProcessedBy()
	{
		return getProcessedBy( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.processedBy</code> attribute. 
	 * @param value the processedBy
	 */
	public void setProcessedBy(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PROCESSEDBY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.processedBy</code> attribute. 
	 * @param value the processedBy
	 */
	public void setProcessedBy(final String value)
	{
		setProcessedBy( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, STATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CSRCustomerDetails.status</code> attribute.
	 * @return the status
	 */
	public EnumerationValue getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, STATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CSRCustomerDetails.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final EnumerationValue value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
}
