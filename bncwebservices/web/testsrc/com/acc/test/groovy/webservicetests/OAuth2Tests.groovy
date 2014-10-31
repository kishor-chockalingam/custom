package com.acc.test.groovy.webservicetests
import org.junit.Test

import groovy.json.*

class OAuth2Tests extends BaseWSTest {

	@Test
	void testGetAccessToken() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUserJSON()
		assert TestUtil.getAccessToken(uid, CustomerTests.password) : 'Unable to obtain access_token!'
	}

	@Test
	void testGetRefreshToken() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUserJSON()
		def tokenMap = TestUtil.getAccessTokenMap(uid, CustomerTests.password)
		assert TestUtil.refreshToken(tokenMap.refresh_token) : 'Could not obtian new access token'
	}

	@Test
	void testGetClientCredentialsToken() {
		assert TestUtil.getClientCredentialsToken('mobile_android', 'secret')
	}
}