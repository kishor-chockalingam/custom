/**
 * 
 */
package com.acc.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.acc.facades.collectOrder.CollectOrderStatus;
import com.acc.interfaces.BnCGenericUtility;


/**
 * @author swarnima.gupta
 * 
 */
public class BnCGenericUtil implements BnCGenericUtility
{
	/**
	 * 
	 */

	private static final Logger LOG = Logger.getLogger(BnCGenericUtil.class);

	@Override
	public String getUCOID(final String orderID)
	{
		final Random rand = new Random();
		final String GeneratedUCOID = orderID + rand.nextInt();
		LOG.info("String to be encrypted :: " + GeneratedUCOID);
		return GeneratedUCOID;
	}

	public static List<CollectOrderStatus> getStatusList()
	{
		final List<CollectOrderStatus> statusList = new ArrayList<CollectOrderStatus>();
		statusList.add(CollectOrderStatus.PENDING);
		statusList.add(CollectOrderStatus.COMPLETED);
		statusList.add(CollectOrderStatus.COLLECTED);
		return statusList;
	}
}
