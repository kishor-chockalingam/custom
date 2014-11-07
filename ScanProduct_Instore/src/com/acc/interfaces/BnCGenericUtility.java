package com.acc.interfaces;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface BnCGenericUtility {
	
	public String getUCOID(final String customerID, final String orderID) throws InvalidKeyException,
	NoSuchAlgorithmException;

}
