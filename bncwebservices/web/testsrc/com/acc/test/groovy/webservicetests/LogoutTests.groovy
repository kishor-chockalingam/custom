package com.acc.test.groovy.webservicetests

import static com.acc.test.groovy.webservicetests.TestUtil.*

import com.acc.test.groovy.webservicetests.markers.CollectOutputFromTest

import org.junit.Test
import org.junit.experimental.categories.Category

@Category(CollectOutputFromTest.class)
class LogoutTests extends BaseWSTest {

	final password = "test"

	@Test
	void testLogout() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		//get customer profile
		def con = TestUtil.getSecureConnection("/customers/current", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)
		def response = TestUtil.verifiedXMLSlurper(con)
		assert response

		//get cookie
		def cookie = con.getHeaderField('Set-Cookie')
		assert cookie : 'No cookie present!'
		def cookieNoPath = cookie.split(';')[0]

		//logout
		con = TestUtil.getSecureConnection(TestUtil.LOGOUT_URL, 'POST', 'XML', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		response = TestUtil.verifiedXMLSlurper(con)

		assert response.success == true

		//after logout, using the same cookie than before must fail!
		con = TestUtil.getSecureConnection("/customers/current", 'GET', 'XML', HttpURLConnection.HTTP_UNAUTHORIZED, null, cookieNoPath)
	}

	@Test
	void testLogoutJSON() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		//get customer profile
		def con = TestUtil.getSecureConnection("/customers/current", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		def response = TestUtil.verifiedJSONSlurper(con)
		assert response

		//get cookie
		def cookie = con.getHeaderField('Set-Cookie')
		assert cookie : 'No cookie present!'
		def cookieNoPath = cookie.split(';')[0]

		//logout
		con = TestUtil.getSecureConnection(TestUtil.LOGOUT_URL, 'POST', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		response = TestUtil.verifiedJSONSlurper(con)

		assert response.success == true

		//after logout, using the same cookie than before must fail!
		con = TestUtil.getSecureConnection("/customers/current", 'GET', 'JSON', HttpURLConnection.HTTP_UNAUTHORIZED, null, cookieNoPath)
	}
}