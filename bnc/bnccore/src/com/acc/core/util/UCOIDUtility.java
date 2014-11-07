/**
 * 
 */
package com.acc.core.util;

import de.hybris.platform.acceleratorservices.payment.cybersource.utils.DigestUtils;
import de.hybris.platform.util.Config;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

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
	private static final String ENCRYPTION_KEY_FOR_UCOID = "encryption.key.for.ucoid";
	private static final Logger LOG = Logger.getLogger(UCOIDUtility.class);

	@Override
	public String getUCOID(final String customerID, final String orderID) throws InvalidKeyException, NoSuchAlgorithmException
	{
		final String forEncryption = customerID + orderID + new Date().toString();
		LOG.info("String to be encrypted :: " + forEncryption);
		LOG.info("Encrypted String :: "
				+ DigestUtils.getPublicDigest(forEncryption, Config.getString(ENCRYPTION_KEY_FOR_UCOID, "ABCDEFGH")));
		return DigestUtils.getPublicDigest(forEncryption, Config.getString(ENCRYPTION_KEY_FOR_UCOID, "ABCDEFGH"));
	}
}
