/**
 * 
 */
package com.acc.storefront.util;

/**
 * @author prasun.a.kumar
 * 
 */
public class ProfileInformationDto
{
	private String name;
	private String dob;
	private String line1;
	private String line2;
	private String apartment;
	private String postalCode;

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the dob
	 */
	public String getDob()
	{
		return dob;
	}

	/**
	 * @param dob
	 *           the dob to set
	 */
	public void setDob(final String dob)
	{
		this.dob = dob;
	}

	/**
	 * @return the line1
	 */
	public String getLine1()
	{
		return line1;
	}

	/**
	 * @param line1
	 *           the line1 to set
	 */
	public void setLine1(final String line1)
	{
		this.line1 = line1;
	}

	/**
	 * @return the line2
	 */
	public String getLine2()
	{
		return line2;
	}

	/**
	 * @param line2
	 *           the line2 to set
	 */
	public void setLine2(final String line2)
	{
		this.line2 = line2;
	}

	/**
	 * @return the apartment
	 */
	public String getApartment()
	{
		return apartment;
	}

	/**
	 * @param apartment
	 *           the apartment to set
	 */
	public void setApartment(final String apartment)
	{
		this.apartment = apartment;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * @param postalCode
	 *           the postalCode to set
	 */
	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}

}
