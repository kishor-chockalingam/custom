package com.acc.test.groovy.webservicetests
import com.acc.test.groovy.webservicetests.markers.AvoidCollectingOutputFromTest
import com.acc.test.groovy.webservicetests.markers.CollectOutputFromTest

import org.junit.Test
import org.junit.experimental.categories.Category

import groovy.json.JsonSlurper

@Category(CollectOutputFromTest.class)
class CustomerTests extends BaseWSTest {

	final firstName = "Sven"
	final lastName = "Haiges"
	final titleCode = "dr"
	final title = "Dr."
	final public static password = "test"
	final line1 = "Nymphenburger Str. 86 - Maillingerstrasse"
	final town = "Muenchen"
	final town2 = "Hamburg"
	final postalCode = "80331"
	final countryIsoCode = "DE"

	/**
	 * helper method to register user
	 * @return generated userId
	 */
	def registerUser(useSecureConnection=true, status=HttpURLConnection.HTTP_CREATED) {
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def randomUID = System.currentTimeMillis()
		def body = "login=${randomUID}@sven.de&password=${password}&firstName=${firstName}&lastName=${lastName}&titleCode=${titleCode}"

		def con = null
		if(useSecureConnection) {
			con = TestUtil.getSecureConnection("/customers", 'POST', 'XML', status, body, null, client_credentials_token)
		}
		else {
			con = TestUtil.getConnection("/customers", 'POST', 'XML', status, body, null, client_credentials_token)
		}

		return status == HttpURLConnection.HTTP_CREATED ? "${randomUID}@sven.de" : null
	}


	def registerUserJSON() {
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def randomUID = System.currentTimeMillis()
		def body = "login=${randomUID}@sven.de&password=${password}&firstName=${firstName}&lastName=${lastName}&titleCode=${titleCode}"
		def con = TestUtil.getSecureConnection("/customers", 'POST', 'JSON', HttpURLConnection.HTTP_CREATED, body, null, client_credentials_token)
		return "${randomUID}@sven.de"
	}

	/**
	 * helper method to create address
	 * @return generated addressId
	 */
	def createAddress(access_token, town = "Muenchen") {
		def postBody = "titleCode=${titleCode}&firstName=${firstName}&lastName=${lastName}&line1=${line1}&town=${town}&postalCode=${postalCode}&country.isocode=${countryIsoCode}"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)

		def response = TestUtil.verifiedXMLSlurper(con)
		assert response.line1 == "${line1}"
		assert response.town == "${town}"
		assert response.country.isocode == "${countryIsoCode}"

		return response.id
	}

	def createAddressJSON(access_token, town="Muenchen") {
		def postBody = "titleCode=${titleCode}&firstName=${firstName}&lastName=${lastName}&line1=${line1}&town=${town}&postalCode=${postalCode}&country.isocode=${countryIsoCode}"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'JSON', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		def response = TestUtil.verifiedJSONSlurper(con)
		assert response.line1 == "${line1}"
		assert response.town == "${town}"
		assert response.country.isocode == "${countryIsoCode}"

		return response.id
	}

	def addToCardAndGetCookie(code) {
		def postBody = "code=${code}"
		def con = TestUtil.getConnection('/cart/entry', 'POST', 'JSON', HttpURLConnection.HTTP_OK, postBody)
		def response = TestUtil.verifiedJSONSlurper(con)

		assert response.statusCode == 'success'
		assert response.quantityAdded == 1
		assert response.entry.entryNumber == 0

		def cookie = con.getHeaderField('Set-Cookie')
		assert cookie : 'No cookie present, cannot keep session'
		return cookie.split(';')[0]
	}

	def createPaymentInfo() {
		def  customerTests = new CustomerTests()
		def uid = customerTests.registerUserJSON()
		def access_token = TestUtil.getAccessToken(uid, password)
		def aid = customerTests.createAddressJSON(access_token)
		//add something to a cart
		def cookieNoPath = addToCardAndGetCookie("3429337")

		def postBody = "accountHolderName=Sven+Haiges&cardNumber=4111111111111111&cardType=visa&expiryMonth=01&expiryYear=2013&saved=true&defaultPaymentInfo=true&billingAddress.titleCode=mr&billingAddress.firstName=sven&billingAddress.lastName=haiges&billingAddress.line1=test1&billingAddress.line2=test2&billingAddress.postalCode=12345&billingAddress.town=somecity&billingAddress.country.isocode=DE"
		def con = TestUtil.getSecureConnection("/cart/paymentinfo", 'POST', 'XML', HttpURLConnection.HTTP_OK, postBody, cookieNoPath, access_token)

		def response = TestUtil.verifiedXMLSlurper(con)
		def paymentInfoId = response.paymentInfo.id

		assert response.paymentInfo.id
		assert response.paymentInfo.accountHolderName == 'Sven Haiges'
		assert response.paymentInfo.cardType.code == 'visa'
		assert response.paymentInfo.cardType.name == 'Visa'
		assert response.paymentInfo.expiryMonth == '01'
		assert response.paymentInfo.expiryYear == '2013'

		return [
			uid,
			access_token,
			cookieNoPath,
			paymentInfoId
		]
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testRegisterUserJSON() {
		def uid = registerUserJSON()
		assert uid
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testTryRegisterUserWithoutHttps() {
		def uid = registerUser(false, HttpURLConnection.HTTP_MOVED_TEMP)
		assert uid == null
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testTryRegisterUserWithHttps() {

		def uid = registerUser(true)
		println uid
		assert uid != null
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testRegisterDuplicateUID() {
		def uid = registerUser()

		//try to register another user with same UID
		def postBody = "login=${uid}&password=${password}&firstName=${firstName}&lastName=${lastName}&titleCode=${titleCode}"
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def con = TestUtil.getSecureConnection("/customers", 'POST', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, client_credentials_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'DuplicateUidException'
		assert response.message == "${uid}"
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testRegisterDuplicateUIDJSON() {
		def uid = registerUserJSON()

		//try to register another user with same UID
		def postBody = "login=${uid}&password=${password}&firstName=${firstName}&lastName=${lastName}&titleCode=${titleCode}"
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def con = TestUtil.getSecureConnection("/customers", 'POST', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, client_credentials_token)
		def response = new JsonSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'DuplicateUidException'
		assert response.message == "${uid}"
	}

	@Test
	void testGetCurrentUser() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		//check customer profile
		def con = TestUtil.getSecureConnection("/customers/current", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)

		def response = TestUtil.verifiedXMLSlurper(con)
		assert response.uid == "${uid}"
		assert response.firstName == "${firstName}"
		assert response.lastName == "${lastName}"
		assert response.titleCode == "${titleCode}"
		assert response.title == "${title}"
	}

	@Test
	void testGetCurrentUserParamAccessToken() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		//check customer profile
		def con = TestUtil.getSecureConnection("/customers/current?access_token=${access_token}", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, null)

		def response = TestUtil.verifiedXMLSlurper(con)
		assert response.uid == "${uid}"
		assert response.firstName == "${firstName}"
		assert response.lastName == "${lastName}"
		assert response.titleCode == "${titleCode}"
	}

	@Test
	void testGetCurrentUserJSON() {
		def uid = registerUserJSON()
		def access_token = TestUtil.getAccessToken(uid, password);

		//check customer profile
		def con = TestUtil.getSecureConnection("/customers/current", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		def response = TestUtil.verifiedJSONSlurper(con)

		assert response.uid == "${uid}"
		assert response.firstName == "${firstName}"
		assert response.lastName == "${lastName}"
		assert response.titleCode == "${titleCode}"
		assert response.title == "${title}"
		assert response.name == "$firstName $lastName"

		assert response.currency.isocode == 'USD'
		assert response.language.isocode == 'en'

	}

	@Test
	void testChangePassword() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		//change password
		def postBody = "old=${password}&new=newpassword"
		def con = TestUtil.getSecureConnection("/customers/current/password", 'PUT', 'XML', HttpURLConnection.HTTP_ACCEPTED, postBody, null, access_token)

	}

	@Test
	void testForceChangePassword() {
		def uid = registerUser()
		def trusted_client_access_token = TestUtil.getTrustedClientCredentialsToken();

		//change password
		def postBody = "new=newpassword"
		def con = TestUtil.getSecureConnection("/customers/" + uid + "/password", 'PUT', 'XML', HttpURLConnection.HTTP_ACCEPTED, postBody, null, trusted_client_access_token)

		// test if password correct
		def access_token = TestUtil.getAccessToken(uid, 'newpassword');
	}

	@Test
	void testForceChangePasswordFailWhenClientCredentialsProvided() {
		def uid = registerUser()
		def client_access_token = TestUtil.getClientCredentialsToken();

		//change password
		def postBody = "new=newpassword"
		def con = TestUtil.getSecureConnection("/customers/" + uid + "/password", 'PUT', 'XML', HttpURLConnection.HTTP_FORBIDDEN, postBody, null, client_access_token)
	}

	@Test
	void testForceChangePasswordFailWhenCustomerCredentialsProvided() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		//change password
		def postBody = "new=newpassword"
		def con = TestUtil.getSecureConnection("/customers/" + uid + "/password", 'PUT', 'XML', HttpURLConnection.HTTP_FORBIDDEN, postBody, null, access_token)
	}

	@Test
	void testForceChangePasswordFailIfCustomerNotExists() {
		def uid = registerUser()
		def trusted_client_access_token = TestUtil.getTrustedClientCredentialsToken();

		//change password
		def postBody = "new=newpassword"
		def con = TestUtil.getSecureConnection("/customers/" + "nonexisting@hybris.com" + "/password", 'PUT', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, trusted_client_access_token)

		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'UnknownIdentifierException'
	}

	@Test
	void testChangePasswordJSON() {
		def uid = registerUserJSON()
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "old=${password}&new=newpassword"
		def con = TestUtil.getSecureConnection("/customers/current/password", 'PUT', 'JSON', HttpURLConnection.HTTP_ACCEPTED, postBody, null, access_token)
	}

	@Test
	void testAddressBook() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);
		def aid = createAddress(access_token, town)

		//set default address to aid2
		//check if address is listed on address book
		def con = TestUtil.getSecureConnection("/customers/current/addresses/default/${aid}", 'PUT', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)
		//no response, that's ok


		//check if address is listed on address book
		con = TestUtil.getSecureConnection("/customers/current/addresses", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)
		def response = TestUtil.verifiedXMLSlurper(con)
		assert response.address[0].line1 == "${line1}"
		assert response.address[0].town == "${town}"
		assert response.address[0].country.isocode == "${countryIsoCode}"

		//add another address

		def postBody = "titleCode=${titleCode}&firstName=${firstName}&lastName=${lastName}&line1=Bayerstr.+10a&town=${town2}&country.isocode=DE&postalCode=80335"
		con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)

		//check if both addresses are listed on address book
		con = TestUtil.getSecureConnection("/customers/current/addresses", 'GET', 'XML',  HttpURLConnection.HTTP_OK, null, null, access_token)

		response = TestUtil.verifiedXMLSlurper(con)
		assert response.address[0].line1 == "${line1}"
		assert response.address[0].town == "${town}"
		assert response.address[0].country.isocode == "${countryIsoCode}"
		assert response.address[1].town == "${town2}"
		assert response.address[1].line1 == "Bayerstr. 10a"
		assert response.address[1].postalCode == "80335"
		assert response.address[1].country.isocode == "DE"

	}

	@Test
	void testAddressBookJSON() {
		def uid = registerUserJSON()
		def access_token = TestUtil.getAccessToken(uid, password);
		def aid = createAddressJSON(access_token, town)


		//check if address is listed on address book
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		def body = con.inputStream.text
		println body
		def response = new JsonSlurper().parseText(body)
		assert response.addresses[0].line1 == "${line1}"
		assert response.addresses[0].town == "${town}"
		assert response.addresses[0].country.isocode == "${countryIsoCode}"


		//add another address
		def postBody = "titleCode=${titleCode}&firstName=${firstName}&lastName=${lastName}&line1=Bayerstr.+10a&town=${town2}&country.isocode=DE&postalCode=80335"
		con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'JSON', HttpURLConnection.HTTP_OK, postBody, null, access_token)

		//check if both addresses are listed on address book
		con = TestUtil.getSecureConnection("/customers/current/addresses", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		body = con.inputStream.text
		//println body
		response = new JsonSlurper().parseText(body)
		assert response.addresses[0].line1 == "${line1}"
		assert response.addresses[0].town == "${town}"
		assert response.addresses[0].country.isocode == "${countryIsoCode}"
		assert response.addresses[1].line1 == "Bayerstr. 10a"
		assert response.addresses[1].town == "${town2}"
		assert response.addresses[1].postalCode == "80335"
		assert response.addresses[1].country.isocode == "DE"


	}

	@Test
	void testDeleteAddress() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password)
		def aid = createAddress(access_token, town)

		//check one addresses in GET /addresses/
		def con = TestUtil.getSecureConnection("/customers/current/addresses/", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)
		def response = TestUtil.verifiedXMLSlurper(con)
		assert response.name() == 'addresses'
		assert response.address.size() == 1

		//delete address
		con = TestUtil.getSecureConnection("/customers/current/addresses/" + aid, 'DELETE', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)

		//check no addresses in GET /addresses/
		con = TestUtil.getSecureConnection("/customers/current/addresses/", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)
		response = TestUtil.verifiedXMLSlurper(con)
		assert response.name() == 'addresses'
		assert response.address.size() == 0

	}

	@Test
	void testDeleteAddressJSON() {
		def uid = registerUserJSON()
		def access_token = TestUtil.getAccessToken(uid, password);
		def aid = createAddressJSON(access_token, town)

		//check one addresses in GET /addresses/
		def con = TestUtil.getSecureConnection("/customers/current/addresses/", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		def body = con.inputStream.text
		def response = new JsonSlurper().parseText(body)
		assert response.addresses.size() == 1

		//delete address
		con = TestUtil.getSecureConnection("/customers/current/addresses/" + aid, 'DELETE', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)

		//check no addresses in GET /addresses/
		con = TestUtil.getSecureConnection("/customers/current/addresses/", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		body = con.inputStream.text
		response = new JsonSlurper().parseText(body)
		assert response.addresses.size() == 0
	}

	@Test
	void testEditAddress() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password)
		def aid = createAddress(access_token, town)

		//edit address
		def postBody = "town=Montreal&postalCode=80335"
		def con = TestUtil.getSecureConnection("/customers/current/addresses/" + aid, 'PUT', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		def response = TestUtil.verifiedXMLSlurper(con)

		//check address book
		con = TestUtil.getSecureConnection("/customers/current/addresses", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)

		response = TestUtil.verifiedXMLSlurper(con)
		assert response.name() == 'addresses'
		assert response.address[0].line1 == "${line1}"
		assert response.address[0].town == "Montreal"
		assert response.address[0].postalCode == "80335"
		assert response.address[0].country.isocode == "${countryIsoCode}"
	}

	@Test
	void testEditAddressJSON() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);
		def aid = createAddressJSON(access_token)


		//edit address
		def postBody = "town=Montreal&postalCode=80335"
		def con = TestUtil.getSecureConnection("/customers/current/addresses/" + aid, 'PUT', 'JSON', HttpURLConnection.HTTP_OK, postBody, null, access_token)


		//check address book
		con = TestUtil.getSecureConnection("/customers/current/addresses", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)

		def response = new JsonSlurper().parseText(con.inputStream.text)
		assert response.addresses[0].line1 == "${line1}"
		assert response.addresses[0].town == "Montreal"
		assert response.addresses[0].postalCode == "80335"
		assert response.addresses[0].country.isocode == "${countryIsoCode}"
	}

	@Test
	void testSetDefaultAddress() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);
		def aid = createAddress(access_token)

		//add another address
		def postBody = "titleCode=${titleCode}&firstName=${firstName}&lastName=${lastName}&line1=Bayerstr.+10a&town=${town2}&country.isocode=DE&postalCode=80335"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)

		def response = TestUtil.verifiedXMLSlurper(con)
		def newAddressId = response.id

		//check if first address is set as default
		con = TestUtil.getSecureConnection("/customers/current", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)
		def body = con.inputStream.text
		//println body
		response = new XmlSlurper().parseText(body)
		assert response.uid == "${uid}"
		assert response.defaultAddress.line1 == "${line1}"
		assert response.defaultAddress.town == "${town}"
		assert response.defaultAddress.country.isocode == "${countryIsoCode}"

		//set second address as default
		con = TestUtil.getSecureConnection("/customers/current/addresses/default/" + newAddressId, 'PUT', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)

		//check if second address is set as customer's default
		con = TestUtil.getSecureConnection("/customers/current", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, access_token)
		response = new XmlSlurper().parseText(con.inputStream.text)
		assert response.uid == "${uid}"
		assert response.defaultAddress.id == "${newAddressId}"
		assert response.defaultAddress.line1 == "Bayerstr. 10a"
		assert response.defaultAddress.town == "${town2}"
		assert response.defaultAddress.country.isocode == "${countryIsoCode}"
	}

	@Test
	void testUpdateCustomerProfileJSON() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		//def con = TestUtil.getSecureConnection("/customers/current", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		//def response = TestUtil.verifiedJSONSlurper(con)
		//println response

		def postBody = "titleCode=mr&firstName=Udo&lastName=Hubertus&language=zh&currency=EUR"
		def con = TestUtil.getSecureConnection("/customers/current/profile", 'PUT', 'JSON', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		//println body
		def response = TestUtil.verifiedJSONSlurper(con)
		println response

		assert response.uid == uid
		assert response.name == "Udo Hubertus"
		assert response.firstName == "Udo"
		assert response.lastName == "Hubertus"
		assert response.currency.isocode == "EUR"
		assert response.language.isocode == "zh"
		assert response.titleCode == 'mr'
		assert response.title == 'Mr'

		//con = TestUtil.getSecureConnection("/customers/current", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		//response = TestUtil.verifiedJSONSlurper(con)
		//println response
	}

	// Waiting for https://jira.hybris.com/browse/COMWS-72
	void testUpdateCustomerProfileJSON2() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);
		//def con = TestUtil.getSecureConnection("/customers/current", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		//def response = TestUtil.verifiedJSONSlurper(con)
		//println response
		def postBody = "firstName=First%20Name&lastName=Last%20Name&titleCode=mr&language=en&currency=GBP"
		def con = TestUtil.getSecureConnection("/customers/current/profile", 'POST', 'JSON', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		//println body
		def response = TestUtil.verifiedJSONSlurper(con)
		println response
		assert response.uid == uid
		assert response.name == "First Name#Last Name"
		assert response.firstName == "First Name"
		assert response.lastName == "Last Name"
		assert response.currency.isocode == "GBP"
		//con = TestUtil.getSecureConnection("/customers/current", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, access_token)
		//response = TestUtil.verifiedJSONSlurper(con)
		//println response
	}


	@Test
	void testUpdateCustomerProfileXML() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "titleCode=mr&firstName=Udo&lastName=Hubertus&language=zh&currency=EUR"
		def con = TestUtil.getSecureConnection("/customers/current/profile", 'PUT', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		//println body
		def response =   TestUtil.verifiedXMLSlurper(con)

		assert response.uid == uid
		assert response.name == "Udo Hubertus"
		assert response.firstName == "Udo"
		assert response.lastName == "Hubertus"
		assert response.currency.isocode == "EUR"
		assert response.language.isocode == "zh"
	}

	@Test
	void testGetPaymentInfo() {

		/* given */
		def (uid, access_token, cookieNoPath, paymentInfoId) = createPaymentInfo()

		/* when */
		def con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		def response = TestUtil.verifiedXMLSlurper(con)

		/* then */
		println response.expiryMonth
		println response.expiryMonth.getClass()
		assert response
		assert response.id == paymentInfoId
		assert response.accountHolderName == 'Sven Haiges'
		assert response.cardType.code == 'visa'
		assert response.cardType.name == 'Visa'
		assert response.expiryYear == "2013"
		assert response.expiryMonth.text() == "01"
	}

	@Test
	void testGetPaymentInfoJSON() {

		/* given */
		def (uid, access_token, cookieNoPath, paymentInfoId) = createPaymentInfo()

		/* when */
		def con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		def response = TestUtil.verifiedJSONSlurper(con)

		/* then */
		assert response
		assert response.id == paymentInfoId.text()
		assert response.accountHolderName == 'Sven Haiges'
		assert response.cardType.code == 'visa'
		assert response.cardType.name == 'Visa'
		assert response.expiryYear == "2013"
		assert response.expiryMonth == "01"
	}

	@Test
	void testDeletePaymentInfo() {

		/* given */
		def (uid, access_token, cookieNoPath, paymentInfoId) = createPaymentInfo()

		/* when */
		def con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'DELETE', 'XML', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)

		/* then */
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		def response = TestUtil.verifiedXMLSlurper(con)

		assert response.paymentInfos.size() == 0
	}

	@Test
	void testDeletePaymentInfoJSON() {

		/* given */
		def (uid, access_token, cookieNoPath, paymentInfoId) = createPaymentInfo()

		/* when */
		def con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'DELETE', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)

		/* then */
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		def response = TestUtil.verifiedJSONSlurper(con)

		assert response.paymentInfos.size() == 0
	}

	@Test
	void testUpdatePaymentInfo() {

		/* given */
		def (uid, access_token, cookieNoPath, paymentInfoId) = createPaymentInfo()

		/* when */
		def postBody = "expiryMonth=02"
		def con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'PUT', 'XML', HttpURLConnection.HTTP_OK, postBody, cookieNoPath, access_token)

		/* then */
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		def response = TestUtil.verifiedXMLSlurper(con, true)

		assert response.expiryMonth == "02"
	}

	@Test
	void testUpdatePaymentInfoJSON() {

		/* given */
		def (uid, access_token, cookieNoPath, paymentInfoId) = createPaymentInfo()

		/* when */
		def postBody = "expiryMonth=02"
		def con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'PUT', 'JSON', HttpURLConnection.HTTP_OK, postBody, cookieNoPath, access_token)

		/* then */
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		def response = TestUtil.verifiedJSONSlurper(con, true)

		assert response.expiryMonth == "02"
	}

	@Test
	void testUpdatePaymentAddress() {

		/* given */
		def (uid, access_token, cookieNoPath, paymentInfoId) = createPaymentInfo()

		/* when */
		def postBody = "titleCode=mr&firstName=ChangedFirstName&lastName=ChangedLastName&line1=ChangedLine1&line2=ChangedLine2&postalCode=44-100&town=ChangedTown&country.isocode=DE"
		def con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}/address","POST","XML", HttpURLConnection.HTTP_OK, postBody, null, access_token)

		/* then */
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'GET', 'XML', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		def response = TestUtil.verifiedXMLSlurper(con)

		assert response.id == paymentInfoId
		assert response.billingAddress != null
		assert response.billingAddress.title =="Mr"
		assert response.billingAddress.firstName =="ChangedFirstName"
		assert response.billingAddress.lastName =="ChangedLastName"
		assert response.billingAddress.line1 =="ChangedLine1"
		assert response.billingAddress.line2 =="ChangedLine2"
		assert response.billingAddress.postalCode =="44-100"
		assert response.billingAddress.town=="ChangedTown"
		assert response.billingAddress.country.isocode=="DE"
	}

	@Test
	void testUpdatePaymentAddressJSON() {

		/* given */
		def (uid, access_token, cookieNoPath, paymentInfoId) = createPaymentInfo()

		/* when */
		def postBody = "titleCode=ms&firstName=ChangedFirstName&lastName=ChangedLastName&line1=ChangedLine1&line2=ChangedLine2&postalCode=44-100&town=ChangedTown&country.isocode=DE"
		def con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}/address","POST","JSON", HttpURLConnection.HTTP_OK, postBody, null, access_token)

		/* then */
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		def response = TestUtil.verifiedJSONSlurper(con)

		assert response.id == paymentInfoId.text()
		assert response.billingAddress != null
		assert response.billingAddress.title =="Ms"
		assert response.billingAddress.firstName =="ChangedFirstName"
		assert response.billingAddress.lastName =="ChangedLastName"
		assert response.billingAddress.line1 =="ChangedLine1"
		assert response.billingAddress.line2 =="ChangedLine2"
		assert response.billingAddress.postalCode =="44-100"
		assert response.billingAddress.town=="ChangedTown"
		assert response.billingAddress.country.isocode=="DE"
	}


	@Test
	void testPaymentInfosFlowJSON() {
		def  customerTests = new CustomerTests()
		def uid = customerTests.registerUserJSON()
		def access_token = TestUtil.getAccessToken(uid, password)
		def aid = customerTests.createAddressJSON(access_token)

		//add something to a cart
		def cookieNoPath = addToCardAndGetCookie("3429337")

		def postBody = "accountHolderName=Sven+Haiges&cardNumber=4111111111111111&cardType=visa&expiryMonth=01&expiryYear=2013&saved=true&defaultPaymentInfo=true&billingAddress.titleCode=mr&billingAddress.firstName=sven&billingAddress.lastName=haiges&billingAddress.line1=test1&billingAddress.line2=test2&billingAddress.postalCode=12345&billingAddress.town=somecity&billingAddress.country.isocode=DE"
		def con = TestUtil.getSecureConnection("/cart/paymentinfo", 'POST', 'JSON', HttpURLConnection.HTTP_OK, postBody, cookieNoPath, access_token)

		def response = TestUtil.verifiedJSONSlurper(con, true)
		assert response.paymentInfo.id
		assert response.paymentInfo.accountHolderName == 'Sven Haiges'
		assert response.paymentInfo.cardType.code == 'visa'
		assert response.paymentInfo.cardType.name == 'Visa'

		def paymentInfoId = response.paymentInfo.id

		postBody = "startMonth=02&startYear=11&accountHolderName=Admin00&cardNumber=4111111111111111&cardType=visa&expiryMonth=01&expiryYear=2013&saved=true&defaultPaymentInfo=true&billingAddress.titleCode=mr&billingAddress.firstName=sven&billingAddress.lastName=haiges&billingAddress.line1=test1&billingAddress.line2=test2&billingAddress.postalCode=12345&billingAddress.town=somecity&billingAddress.country.isocode=DE"
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'PUT', 'JSON', HttpURLConnection.HTTP_OK, postBody, cookieNoPath, access_token)

		con = TestUtil.getSecureConnection("/customers/current/paymentinfos", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		response = TestUtil.verifiedJSONSlurper(con)

		assert response.paymentInfos.size() == 1
		assert response.paymentInfos[0].id == paymentInfoId
		assert response.paymentInfos[0].accountHolderName == 'Admin00'
		assert response.paymentInfos[0].cardType.code == 'visa'
		assert response.paymentInfos[0].cardType.name == 'Visa'
		assert response.paymentInfos[0].expiryMonth == "01"
		assert response.paymentInfos[0].startMonth == "02"
		assert response.paymentInfos[0].startYear == "11"

		// changing only one attribute

		postBody = "expiryMonth=03"
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'PUT', 'JSON', HttpURLConnection.HTTP_OK, postBody, cookieNoPath, access_token)

		con = TestUtil.getSecureConnection("/customers/current/paymentinfos", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		response = TestUtil.verifiedJSONSlurper(con)

		println response.paymentInfos
		assert response.paymentInfos.size() == 1
		assert response.paymentInfos[0].id == paymentInfoId
		assert response.paymentInfos[0].accountHolderName == 'Admin00'
		assert response.paymentInfos[0].cardType.code == 'visa'
		assert response.paymentInfos[0].cardType.name == 'Visa'
		assert response.paymentInfos[0].expiryMonth == "03"
		assert response.paymentInfos[0].startMonth == "02"
		assert response.paymentInfos[0].startYear == "11"

		// you can change optional attributes like startMonth, startYear

		postBody = "startMonth=&expiryMonth=05&startYear="
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'PUT', 'JSON', HttpURLConnection.HTTP_OK, postBody, cookieNoPath, access_token)

		con = TestUtil.getSecureConnection("/customers/current/paymentinfos", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		response = TestUtil.verifiedJSONSlurper(con)

		println response.paymentInfos
		assert response.paymentInfos.size() == 1
		assert response.paymentInfos[0].id == paymentInfoId
		assert response.paymentInfos[0].accountHolderName == 'Admin00'
		assert response.paymentInfos[0].cardType.code == 'visa'
		assert response.paymentInfos[0].cardType.name == 'Visa'
		assert response.paymentInfos[0].expiryMonth == "05"
		assert !response.paymentInfos[0].startMonth
		assert !response.paymentInfos[0].startYear

		// it is not possible to set empty value for required attribute
		System.out.println("PAYMENT INFOS: " +  response.paymentInfos[0]);
		postBody = "accountHolderName=&startYear=2012"
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}", 'PUT', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST, postBody, cookieNoPath, access_token)

		// Updates billing address of existing customer's credit card payment info by payment info id.
		postBody = "titleCode=mr&firstName=ChangedFirstName&lastName=ChangedLastName&line1=ChangedLine1&line2=ChangedLine2&postalCode=44-100&town=ChangedTown&country.isocode=DE"
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}/address","POST","JSON", HttpURLConnection.HTTP_OK, postBody, null, access_token)

		con = TestUtil.getSecureConnection("/customers/current/paymentinfos", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		response = TestUtil.verifiedJSONSlurper(con)
		assert response.paymentInfos.size() == 1
		assert response.paymentInfos[0].id == paymentInfoId
		assert response.paymentInfos[0].billingAddress != null
		assert response.paymentInfos[0].billingAddress.title =="Mr"
		assert response.paymentInfos[0].billingAddress.firstName =="ChangedFirstName"
		assert response.paymentInfos[0].billingAddress.lastName =="ChangedLastName"
		assert response.paymentInfos[0].billingAddress.line1 =="ChangedLine1"
		assert response.paymentInfos[0].billingAddress.line2 =="ChangedLine2"
		assert response.paymentInfos[0].billingAddress.postalCode =="44-100"
		assert response.paymentInfos[0].billingAddress.town=="ChangedTown"
		assert response.paymentInfos[0].billingAddress.country.isocode=="DE"
		//delete payment infos
		con = TestUtil.getSecureConnection("/customers/current/paymentinfos/${paymentInfoId}","DELETE","JSON",HttpURLConnection.HTTP_OK,null,null, access_token);

		con = TestUtil.getSecureConnection("/customers/current/paymentinfos", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, cookieNoPath, access_token)
		response = TestUtil.verifiedJSONSlurper(con)
		assert response.paymentInfos.size() == 0
	}

	@Test
	public void testGetAllCustomerGroupsForCurrentCustomerXML() {
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def access_token = TestUtil.getAccessToken("customermanager", "1234")
		def userUid1 = "" + System.currentTimeMillis() +  "@hybris.de"
		def customerGroup1 = "" + System.currentTimeMillis() + "_customerGroup"
		//add user

		def body = "login=${userUid1}&password=password&firstName=firstName&lastName=lastName&titleCode=dr"
		def con = TestUtil.getSecureConnection("/customers", 'POST', 'XML', HttpURLConnection.HTTP_CREATED, body, null, client_credentials_token)
		//add customer group
		body = "uid=${customerGroup1}&localizedName=aaa"
		con = TestUtil.getSecureConnection("/customergroups", "POST", "XML", HttpURLConnection.HTTP_CREATED, body, null,access_token );
		//assign user to customer group1
		def putBody = "member=${userUid1}"
		con = TestUtil.getSecureConnection("/customergroups/${customerGroup1}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );
		def baseCustomerGroup = "customergroup"
		con = TestUtil.getSecureConnection("/customergroups/${baseCustomerGroup}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );


		//check
		def customer_access_token = TestUtil.getAccessToken(userUid1, "password")
		con = TestUtil.getSecureConnection("/customers/current/customergroups", "GET", "XML", HttpURLConnection.HTTP_OK, null, null, customer_access_token );

		def response = TestUtil.verifiedXMLSlurper(con)
		assert response.userGroup.size() == 2
		assert (response.userGroup[0].uid == baseCustomerGroup || response.userGroup[1].uid == baseCustomerGroup)
		assert (response.userGroup[0].uid == customerGroup1 || response.userGroup[1].uid == customerGroup1)

	}

	@Test
	public void testGetAllCustomerGroupsForCurrentCustomerJSON() {
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def access_token = TestUtil.getAccessToken("customermanager", "1234")
		def userUid1 = "" + System.currentTimeMillis() +  "@hybris.de"
		def customerGroup1 = "" + System.currentTimeMillis() + "_customerGroup"
		//add user

		def body = "login=${userUid1}&password=password&firstName=firstName&lastName=lastName&titleCode=dr"
		def con = TestUtil.getSecureConnection("/customers", 'POST', 'XML', HttpURLConnection.HTTP_CREATED, body, null, client_credentials_token)
		//add customer group
		body = "uid=${customerGroup1}&localizedName=aaa"
		con = TestUtil.getSecureConnection("/customergroups", "POST", "XML", HttpURLConnection.HTTP_CREATED, body, null,access_token );
		//assign user to customer group1
		def putBody = "member=${userUid1}"
		con = TestUtil.getSecureConnection("/customergroups/${customerGroup1}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );
		def baseCustomerGroup = "customergroup"
		con = TestUtil.getSecureConnection("/customergroups/${baseCustomerGroup}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );


		//check
		def customer_access_token = TestUtil.getAccessToken(userUid1, "password")
		con = TestUtil.getSecureConnection("/customers/current/customergroups", "GET", "JSON", HttpURLConnection.HTTP_OK, null, null, customer_access_token );

		def response = TestUtil.verifiedJSONSlurper(con, true)
		assert response.userGroups.size() == 2
		assert (response.userGroups[0].uid == baseCustomerGroup || response.userGroups[1].uid == baseCustomerGroup)
		assert (response.userGroups[0].uid == customerGroup1 || response.userGroups[1].uid == customerGroup1)

	}

	@Test
	public void testGetAllCustomerGroupsWhenUserIsNotCustomer() {
		def access_token = TestUtil.getAccessToken("admin", "nimda")
		def con = TestUtil.getSecureConnection("/customers/current/customergroups", "GET", "JSON", HttpURLConnection.HTTP_FORBIDDEN, null, null, access_token );
	}

	@Test
	public void testGetAllCustomerGroupsForCustomerXML() {
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def access_token = TestUtil.getAccessToken("customermanager","1234");
		def userUid1 = "" + System.currentTimeMillis() +  "@hybris.de"
		def customerGroup1 = "" + System.currentTimeMillis() + "_customerGroup"
		//add user

		def body = "login=${userUid1}&password=password&firstName=firstName&lastName=lastName&titleCode=dr"
		def con = TestUtil.getSecureConnection("/customers", 'POST', 'XML', HttpURLConnection.HTTP_CREATED, body, null, client_credentials_token)
		//add customer group
		body = "uid=${customerGroup1}&localizedName=aaa"
		con = TestUtil.getSecureConnection("/customergroups", "POST", "XML", HttpURLConnection.HTTP_CREATED, body, null,access_token );
		//assign user to customer group1
		def putBody = "member=${userUid1}"
		con = TestUtil.getSecureConnection("/customergroups/${customerGroup1}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );
		def baseCustomerGroup = "customergroup"
		con = TestUtil.getSecureConnection("/customergroups/${baseCustomerGroup}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );


		//check
		con = TestUtil.getSecureConnection("/customers/${userUid1}/customergroups", "GET", "XML", HttpURLConnection.HTTP_OK, null, null, access_token );

		def response = TestUtil.verifiedXMLSlurper(con)
		assert response.userGroup.size() == 2
		assert (response.userGroup[0].uid == baseCustomerGroup || response.userGroup[1].uid == baseCustomerGroup)
		assert (response.userGroup[0].uid == customerGroup1 || response.userGroup[1].uid == customerGroup1)

	}

	@Test
	public void testGetAllCustomerGroupsForCustomerJSON() {
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def access_token = TestUtil.getAccessToken("customermanager", "1234")
		def userUid1 = "" + System.currentTimeMillis() +  "@hybris.de"
		def customerGroup1 = "" + System.currentTimeMillis() + "_customerGroup"
		//add user

		def body = "login=${userUid1}&password=password&firstName=firstName&lastName=lastName&titleCode=dr"
		def con = TestUtil.getSecureConnection("/customers", 'POST', 'XML', HttpURLConnection.HTTP_CREATED, body, null, client_credentials_token)
		//add customer group
		body = "uid=${customerGroup1}&localizedName=aaa"
		con = TestUtil.getSecureConnection("/customergroups", "POST", "XML", HttpURLConnection.HTTP_CREATED, body, null,access_token );
		//assign user to customer group1
		def putBody = "member=${userUid1}"
		con = TestUtil.getSecureConnection("/customergroups/${customerGroup1}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );
		def baseCustomerGroup = "customergroup"
		con = TestUtil.getSecureConnection("/customergroups/${baseCustomerGroup}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );


		//check
		con = TestUtil.getSecureConnection("/customers/${userUid1}/customergroups", "GET", "JSON", HttpURLConnection.HTTP_OK, null, null, access_token );

		def response = TestUtil.verifiedJSONSlurper(con, true)
		assert response.userGroups.size() == 2
		assert (response.userGroups[0].uid == baseCustomerGroup || response.userGroups[1].uid == baseCustomerGroup)
		assert (response.userGroups[0].uid == customerGroup1 || response.userGroups[1].uid == customerGroup1)

	}

	@Test
	public void testGetAllCustomerGroupsForCustomerWhenNoRights(){
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def access_token = TestUtil.getAccessToken("customermanager", "1234")
		def userUid1 = "" + System.currentTimeMillis() +  "@hybris.de"
		def customerGroup1 = "" + System.currentTimeMillis() + "_customerGroup"

		//add user
		def body = "login=${userUid1}&password=password&firstName=firstName&lastName=lastName&titleCode=dr"
		def con = TestUtil.getSecureConnection("/customers", 'POST', 'XML', HttpURLConnection.HTTP_CREATED, body, null, client_credentials_token)

		//add customer group
		body = "uid=${customerGroup1}&localizedName=aaa"
		con = TestUtil.getSecureConnection("/customergroups", "POST", "XML", HttpURLConnection.HTTP_CREATED, body, null,access_token );

		//assign user to customer group1
		def putBody = "member=${userUid1}"
		con = TestUtil.getSecureConnection("/customergroups/${customerGroup1}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );
		def baseCustomerGroup = "customergroup"
		con = TestUtil.getSecureConnection("/customergroups/${baseCustomerGroup}/members", "PUT", "XML", HttpURLConnection.HTTP_OK, putBody, null,access_token );

		//check
		def customer_access_token = TestUtil.getAccessToken(userUid1, "password")
		con = TestUtil.getSecureConnection("/customers/${userUid1}/customergroups", "GET", "JSON", HttpURLConnection.HTTP_FORBIDDEN, null, null, customer_access_token );
	}

	@Test
	void testChangeLoginXML() {
		def uid = registerUser()
		def newUid = "AbC"+System.currentTimeMillis()+"@hybris.com"
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "newLogin=${newUid}&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		def response =   TestUtil.verifiedXMLSlurper(con)
		assert response.success == true
	}

	@Test
	void testChangeLoginJSON() {
		def uid = registerUser()
		def newUid = "AbC"+System.currentTimeMillis()+"@hybris.com"
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "newLogin=${newUid}&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'JSON', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		def response = TestUtil.verifiedJSONSlurper(con)
		assert response.success == true
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginDuplicateUIDXML() {
		def existingUid = registerUser()
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid from ${uid} to already existing ${existingUid}
		def postBody = "newLogin=${existingUid}&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'DuplicateUidException'
		assert response.message.toString().contains(existingUid)
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginDuplicateUIDJSON() {
		def existingUid = registerUserJSON()
		def uid = registerUserJSON()
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid from ${uid} to already existing ${existingUid}
		def postBody = "newLogin=${existingUid}&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new JsonSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'DuplicateUidException'
		assert response.message.toString().contains(existingUid)
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginInvalidEmailXML() {
		def newLogin = "notaValidEmail"
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid from ${uid} to ${newLogin} which is not a correct email
		def postBody = "newLogin=${newLogin}&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'IllegalArgumentException'
		assert response.message.toString().contains(newLogin)
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testUpdateCustomerProfileChangeLoginInvalidEmailJSON() {
		def newLogin = "notaValidEmail"
		def uid = registerUserJSON()
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid from ${uid} to ${newLogin} which is not a correct email
		def postBody = "newLogin=${newLogin}&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new JsonSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'IllegalArgumentException'
		assert response.message.toString().contains(newLogin)
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginEmptyNewLoginXML() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid from ${uid} to a not set newLogin
		def postBody = "newLogin=&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'IllegalArgumentException'
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginEmptyNewLoginJSON() {
		def uid = registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid from ${uid} to a not set newLogin
		def postBody = "newLogin=&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new JsonSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'IllegalArgumentException'
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginEmptyPasswordXML() {
		def uid = registerUser()
		def newUid = "AbC"+System.currentTimeMillis()+"@hybris.com"
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid without passed password
		def postBody = "newLogin=${newUid}&password="
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'IllegalArgumentException'
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginEmptyPasswordJSON() {
		def uid = registerUser()
		def newUid = "AbC"+System.currentTimeMillis()+"@hybris.com"
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid without passed password
		def postBody = "newLogin=${newUid}&password="
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new JsonSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'IllegalArgumentException'
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginWrongPasswordXML() {
		def uid = registerUser()
		def newUid = "AbC"+System.currentTimeMillis()+"@hybris.com"
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid with wrong password
		def postBody = "newLogin=${newUid}&password=paswd"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'PasswordMismatchException'
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginWrongPasswordJSON() {
		def uid = registerUser()
		def newUid = "AbC"+System.currentTimeMillis()+"@hybris.com"
		def access_token = TestUtil.getAccessToken(uid, password);

		// try to change uid with wrong password
		def postBody = "newLogin=${newUid}&password=paswd"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new JsonSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'PasswordMismatchException'
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginAndGetToken4ValidUIDXML() {
		def uid = registerUser()
		def newUid = "abc"+System.currentTimeMillis()+"@hybris.com"
		def access_token = TestUtil.getAccessToken(uid, password);
		def postBody = "newLogin=${newUid}&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		def response =   TestUtil.verifiedXMLSlurper(con)
		assert response.success == true

		// get access token with new UID
		def access_token2 = TestUtil.getAccessToken(newUid, password);
		assert access_token2!=null
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testChangeLoginAndGetToken4InvalidUIDXML() {
		def uid = registerUser()
		def newUid = "abc"+System.currentTimeMillis()+"@hybris.com"
		def access_token = TestUtil.getAccessToken(uid, password);
		def postBody = "newLogin=${newUid}&password=${password}"
		def con = TestUtil.getSecureConnection("/customers/current/login", 'PUT', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)
		def response =   TestUtil.verifiedXMLSlurper(con)
		assert response.success == true

		// get access token with old (non-existing) UID
		try{
			def access_token2 = TestUtil.getAccessToken(uid, password);
		}catch(java.io.IOException ex){
			return;
		}
		org.junit.Assert.fail("Failure expected for getting access token.");
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testRestorePasswordXML() {
		def uid = registerUser()
		def client_credentials_token = TestUtil.getClientCredentialsToken()

		// restore password for existing uid
		def postBody = "login=${uid}"
		def con = TestUtil.getSecureConnection("/customers/current/forgottenpassword", 'POST', 'XML', HttpURLConnection.HTTP_OK, postBody, null, client_credentials_token)
		def response = TestUtil.verifiedXMLSlurper(con)
		assert response.success == true
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testRestorePasswordJSON() {
		def uid = registerUser()
		def client_credentials_token = TestUtil.getClientCredentialsToken()

		// restore password for existing uid
		def postBody = "login=${uid}"
		def con = TestUtil.getSecureConnection("/customers/current/forgottenpassword", 'POST', 'JSON', HttpURLConnection.HTTP_OK, postBody, null, client_credentials_token)
		def response = TestUtil.verifiedJSONSlurper(con)
		assert response.success == true
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testRestorePasswordEmptyLoginXML() {
		def uid = registerUser()
		def client_credentials_token = TestUtil.getClientCredentialsToken()

		// restore password passing empty uid
		def postBody = "login="
		def con = TestUtil.getSecureConnection("/customers/current/forgottenpassword", 'POST', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, client_credentials_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'IllegalArgumentException'
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testRestorePasswordEmptyLoginJSON() {
		def uid = registerUser()
		def client_credentials_token = TestUtil.getClientCredentialsToken()

		// restore password passing empty uid
		def postBody = "login="
		def con = TestUtil.getSecureConnection("/customers/current/forgottenpassword", 'POST', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, client_credentials_token)
		def response = new JsonSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'IllegalArgumentException'
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testRestorePasswordNonExistingLoginXML() {
		def uid = System.currentTimeMillis()+"@hybris.com"
		def client_credentials_token = TestUtil.getClientCredentialsToken()

		// restore password passing non-existing uid
		def postBody = "login=${uid}"
		def con = TestUtil.getSecureConnection("/customers/current/forgottenpassword", 'POST', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, client_credentials_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'UnknownIdentifierException'
	}

	@Category(AvoidCollectingOutputFromTest.class)
	@Test
	void testRestorePasswordNonExistingLoginJSON() {
		def uid = System.currentTimeMillis()+"@hybris.com"
		def client_credentials_token = TestUtil.getClientCredentialsToken()

		// restore password passing non-existing uid
		def postBody = "login=${uid}"
		def con = TestUtil.getSecureConnection("/customers/current/forgottenpassword", 'POST', 'JSON', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, client_credentials_token)
		def response = new JsonSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'UnknownIdentifierException'
	}

}
