/**
 * 
 */
package com.acc.storefront.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;


/**
 * @author prasun.a.kumar
 * 
 */
public class CsrLoginForm
{
	@NotNull(message = "{general.required}")
	@NotBlank(message = "User name cannot be blank.")
	private String username;
	@NotNull(message = "{general.required}")
	@NotBlank(message = "Password cannot be blank.")
	private String password;
	@NotNull(message = "{general.required}")
	@NotBlank(message = "Point of service cannot be blank.")
	private String pos;

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username
	 *           the username to set
	 */
	public void setUsername(final String username)
	{
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *           the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * @return the pos
	 */
	public String getPos()
	{
		return pos;
	}

	/**
	 * @param pos
	 *           the pos to set
	 */
	public void setPos(final String pos)
	{
		this.pos = pos;
	}



}
