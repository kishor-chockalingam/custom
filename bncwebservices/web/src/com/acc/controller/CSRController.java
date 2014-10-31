/**
 * 
 */
package com.acc.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acc.services.StoreLoginService;


/**
 * @author surendra.a.singh
 * 
 */
@Controller
@RequestMapping(value = "/v1/{baseSiteId}/CustomerStoreLogin")
public class CSRController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(CSRController.class);
	@Autowired
	private StoreLoginService storeLoginService;

	@RequestMapping(value = "/{uuid}/{storeId}/{customerId}", method = RequestMethod.GET)
	@ResponseBody
	public String getCustomerLoginDetails(@PathVariable final String uuid, @PathVariable final String storeId,
			@PathVariable final String customerId)
	{

		LOG.info("in getCustomerLoginDetails GET request method :::::::uuid is :::::" + uuid + "::::::::storeId is :::::" + storeId
				+ "AND Customer ID::::" + customerId);
		return saveCustomerLoginDetails(uuid, storeId, customerId);
	}



	@RequestMapping(value = "/customerDetail", method = RequestMethod.POST)
	@ResponseBody
	public String saveCustomerLoginDetails(@RequestParam("uuid") final String uuidValue,
			@RequestParam("storeId") final String storeIdValue, @RequestParam("customerId") final String customerId)
	{
		LOG.info(":::::::::::in saveCustomerLoginDetails method::::::::: ");
		LOG.info(":::::::::::uuid is " + uuidValue + "::::::::::storeid is::::::::: " + storeIdValue + "AND Customer ID :::::::"
				+ customerId);

		return storeLoginService.saveCustomerLoginDetails(uuidValue, storeIdValue, customerId);


	}

}
