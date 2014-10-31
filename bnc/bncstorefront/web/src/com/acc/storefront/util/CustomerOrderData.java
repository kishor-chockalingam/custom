/**
 * 
 */
package com.acc.storefront.util;

/**
 * @author prasun.a.kumar
 * 
 */
public class CustomerOrderData
{
	private String orderCode;
	private String placedDate;
	private String total;

	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	/**
	 * @return the placedDate
	 */
	public String getPlacedDate()
	{
		return placedDate;
	}

	/**
	 * @param placedDate
	 *           the placedDate to set
	 */
	public void setPlacedDate(final String placedDate)
	{
		this.placedDate = placedDate;
	}

	/**
	 * @return the total
	 */
	public String getTotal()
	{
		return total;
	}

	/**
	 * @param total
	 *           the total to set
	 */
	public void setTotal(final String total)
	{
		this.total = total;
	}


}
