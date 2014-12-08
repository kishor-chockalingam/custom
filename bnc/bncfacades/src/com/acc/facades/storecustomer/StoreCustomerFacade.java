/**
 * 
 */
package com.acc.facades.storecustomer;

import java.util.List;

import com.acc.core.enums.CSRStoreStatus;
import com.acc.core.model.CSRCustomerDetailsModel;


/**
 * @author prasun.a.kumar
 * 
 */
public interface StoreCustomerFacade
{

	/**
	 * This method returns the CSR customer details list.
	 */
	public List<CSRCustomerDetailsModel> getCSRCustomerDetails();

	/**
	 * This method is used to update the customer details.
	 * 
	 * @param status
	 *           YTODO
	 */
	public CSRCustomerDetailsModel updateCSRCustomerDetail(final String csrUID, final String customerUID, String status);

	/**
	 * @param CSRStoreStatus
	 * @return List<CSRCustomerDetailsModel>
	 */
	public List<CSRCustomerDetailsModel> getCSRCustomerDetailsByStatus(CSRStoreStatus status);
}
