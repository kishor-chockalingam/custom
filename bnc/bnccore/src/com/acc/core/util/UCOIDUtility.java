/**
 * 
 */
package com.acc.core.util;

import java.util.Random;

import org.apache.log4j.Logger;

import com.acc.interfaces.BnCGenericUtility;


/**
 * @author swarnima.gupta
 * 
 */
public class UCOIDUtility implements BnCGenericUtility
{
	/**
	 * 
	 */

	private static final Logger LOG = Logger.getLogger(UCOIDUtility.class);

	@Override
	public String getUCOID(final String orderID)
	{


		final Random rand = new Random();


		final String GeneratedUCOID = orderID + rand.nextInt();



		LOG.info("String to be encrypted :: " + GeneratedUCOID);

		return GeneratedUCOID;
	}
}
