/**
 * 
 */
package com.acc.dao;

import de.hybris.platform.core.model.user.CustomerModel;

import com.acc.core.model.CSRCustomerDetailsModel;


/**
 * @author prasun.a.kumar
 * 
 */
public interface StoreLoginDao
{
	/**
	 * This helper method returns whether customer exist in hybris system or not.
	 * 
	 * @return return custom if exist otherwise null.
	 */
	public CustomerModel isCustomerFound(final String customerID);

	/**
	 * This helper method is used to check user presence on store or not.
	 * 
	 * @param customerModel
	 *           Indicates customer ID.
	 * @param UUID
	 *           Indicates becan ID.
	 * @param store
	 *           Indicates current store.
	 * @return returns TRUE or FALSE based on User presence on Store.
	 */
	public boolean checkUserPresenceOnStore(final CustomerModel customerModel, final String UUID, final String store);

	/**
	 * @param customerModel
	 * @param uuID
	 * @param storeID
	 * @return returns CSR model
	 */
	public CSRCustomerDetailsModel checkUserPresenceOnCSRDetails(final CustomerModel customerModel, final String uuID,
			final String storeID);

	/**
	 * This helper method is used to save the CSR details in queue.
	 * 
	 * @param csrModel
	 *           Indicates CSR model.
	 * @param customerModel
	 *           Indicates Customer model.
	 * @param storeID
	 *           Indicates Store.
	 * @param uuID
	 *           Ind.icates becan ID
	 */
	public void saveCSRDetails(final CSRCustomerDetailsModel csrModel, final CustomerModel customerModel, final String storeID,
			final String uuID);
}
