/**
 *
 */
package com.acc.test.groovy.webservicetests

import org.junit.Test



class AddressTest extends BaseWSTest {


	final firstName = "John"
	final lastName = "Doe"
	final titleCode = "dr"
	final title = "dr"
	final password = "test"
	final line1 = "Zygmunta Starego 11"
	final line2 = "2nd floor"
	final town = "Gliwice"
	final postalCode = "44-100"
	final plIsoCode = "PL"
	final jpIsoCode = "JP"
	final cnIsoCode = "CN"
	final phone = "+4855488755296"
	final companyName = "hybris"
	final email = "email@email.dot.com"

	final japanRegonCode = "JP-23" //Aiti [Aichi]
	final chinaRegonCode = "CN-34" //Anhui


	@Test
	void shouldReturnBadRequestDueToMissingCountryCode() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "titleCode=${title}&firstName=${firstName}&lastName=${lastName}&line1=${line1}&town=${town}&postalCode=${postalCode}"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)

		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'CustomValidationException'
		assert response.message == "Address validation error"
		def ve = response.validationErrors.children();
		assert ve.size() == 1;
		assert containsOneValidationError(ve, "country.isocode : This field is required and must to be between 1 and 2 characters long.")
	}

	@Test
	void shouldReturnValidationErrorsDueToMissingFields() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "country.isocode=${plIsoCode}"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)

		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'CustomValidationException'
		assert response.message == "Address validation error"
		def ve = response.validationErrors.children();
		assert ve.size() == 6;
		assert containsOneValidationError(ve, "postalCode : This field is required and must to be between 1 and 10 characters long.")
		assert containsOneValidationError(ve, "titleCode : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "firstName : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "lastName : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "line1 : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "town : This field is required and must to be between 1 and 255 characters long.")
	}

	@Test
	void shouldReturnValidationErrorsDueToMissingFields_JAPAN() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "country.isocode=${jpIsoCode}"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)

		assert response.'class' == 'CustomValidationException'
		assert response.message == "Address validation error"
		def ve = response.validationErrors.children();
		assert ve.size() == 7;
		assert containsOneValidationError(ve, "postalCode : This field is required and must to be between 1 and 10 characters long.")
		assert containsOneValidationError(ve, "region.isocode : This field is required and must to be between 1 and 7 characters long.")
		assert containsOneValidationError(ve, "firstName : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "lastName : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "line1 : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "line2 : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "town : This field is required and must to be between 1 and 255 characters long.")
	}

	@Test
	void shouldReturnValidationErrorsDueToMissingFields_CHINA() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "country.isocode=${cnIsoCode}"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, postBody, null, access_token)

		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'CustomValidationException'
		assert response.message == "Address validation error"
		def ve = response.validationErrors.children();
		assert ve.size() == 7;
		assert containsOneValidationError(ve, "postalCode : This field is required and must to be between 1 and 10 characters long.")
		assert containsOneValidationError(ve, "region.isocode : This field is required and must to be between 1 and 7 characters long.")
		assert containsOneValidationError(ve, "titleCode : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "firstName : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "lastName : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "line1 : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "town : This field is required and must to be between 1 and 255 characters long.")
	}

	@Test
	void shouldSaveNewAddress() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "titleCode=${title}&firstName=${firstName}&lastName=${lastName}&line1=${line1}&town=${town}&country.isocode=${plIsoCode}&postalCode=${postalCode}&phone=${phone}&companyName=${companyName}&email=${email}&defaultAddress=true"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)

		def response = TestUtil.verifiedXMLSlurper(con,true)

		assert response.firstName == "${firstName}"
		assert response.lastName == "${lastName}"
		assert response.titleCode == "${titleCode}"
		assert response.line1 == "${line1}"
		assert response.town == "${town}"
		assert response.postalCode == "${postalCode}"
		assert response.country.isocode == "${plIsoCode}"
	}

	@Test
	void shouldSaveJapaneseAddress() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "firstName=${firstName}&lastName=${lastName}&line1=${line1}&line2=${line2}&town=${town}&country.isocode=${jpIsoCode}&postalCode=${postalCode}&region.isocode=${japanRegonCode}"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)

		def response = TestUtil.verifiedXMLSlurper(con)

		assert response.firstName == "${firstName}"
		assert response.lastName == "${lastName}"
		assert response.line1 == "${line1}"
		assert response.line2 == "${line2}"
		assert response.town == "${town}"
		assert response.postalCode == "${postalCode}"
		assert response.country.isocode == "${jpIsoCode}"
		assert response.region.isocode == "${japanRegonCode}"
	}

	@Test
	void shouldSaveChineseAddress() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		def postBody = "titleCode=${title}&firstName=${firstName}&lastName=${lastName}&line1=${line1}&town=${town}&country.isocode=${cnIsoCode}&postalCode=${postalCode}&region.isocode=${chinaRegonCode}"
		def con = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_OK, postBody, null, access_token)

		def response = TestUtil.verifiedXMLSlurper(con)

		assert response.titleCode == "${titleCode}"
		assert response.firstName == "${firstName}"
		assert response.lastName == "${lastName}"
		assert response.line1 == "${line1}"
		assert response.town == "${town}"
		assert response.postalCode == "${postalCode}"
		assert response.country.isocode == "${cnIsoCode}"
		assert response.region.isocode == "${chinaRegonCode}"
	}

	@Test
	void shouldNotAllowToEditAddressDueToValidationError() {
		def customerTests = new CustomerTests()
		def uid = customerTests.registerUser()
		def access_token = TestUtil.getAccessToken(uid, password);

		//create address
		def createAddressPostBody = "titleCode=${title}&firstName=${firstName}&lastName=${lastName}&line1=${line1}&town=${town}&country.isocode=${plIsoCode}&postalCode=${postalCode}&phone=${phone}&companyName=${companyName}&email=${email}&defaultAddress=true"
		def createAddressCon = TestUtil.getSecureConnection("/customers/current/addresses", 'POST', 'XML', HttpURLConnection.HTTP_OK, createAddressPostBody, null, access_token)

		def createAddressResponse = TestUtil.verifiedXMLSlurper(createAddressCon,true)

		assert createAddressResponse.firstName == "${firstName}"
		assert createAddressResponse.lastName == "${lastName}"
		assert createAddressResponse.titleCode == "${titleCode}"
		assert createAddressResponse.line1 == "${line1}"
		assert createAddressResponse.town == "${town}"
		assert createAddressResponse.postalCode == "${postalCode}"
		assert createAddressResponse.country.isocode == "${plIsoCode}"

		def aid = createAddressResponse.id

		//edit address
		def editAddressPostBody = "country.isocode=JP"
		def editAddressCon = TestUtil.getSecureConnection("/customers/current/addresses/" + aid, 'PUT', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, editAddressPostBody, null, access_token)

		def editAddressResponse =  new XmlSlurper().parseText(editAddressCon.errorStream.text)

		assert editAddressResponse.class == 'CustomValidationException'
		assert editAddressResponse.message == "Address validation error"
		def ve = editAddressResponse.validationErrors.children();
		assert ve.size() == 2;
		assert containsOneValidationError(ve, "line2 : This field is required and must to be between 1 and 255 characters long.")
		assert containsOneValidationError(ve, "region.isocode : This field is required and must to be between 1 and 7 characters long.")
	}

	static boolean containsOneValidationError(gpathCollection, error) {
		def res = gpathCollection.findAll{it.text() == error};
		return res.size() == 1;
	}

}
