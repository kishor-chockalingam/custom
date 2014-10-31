package com.acc.test.groovy.webservicetests

import static com.acc.test.groovy.webservicetests.TestUtil.*

import com.acc.test.groovy.webservicetests.markers.AvoidCollectingOutputFromTest

import org.junit.Test
import org.junit.experimental.categories.Category


@Category(AvoidCollectingOutputFromTest.class)
class VoucherTests extends BaseWSTest {
	def PROMOTION_V_CODE = "abc";
	def PROMOTION_VOUCHER_CODE = "abc-9PSW-EDH2-RXKA";
	def PROMOTION_VOUCHER_DESC = "Promotion Voucher Description";
	def PROMOTION_VOUCHER_NAME = "New Promotional Voucher";
	def PROMOTION_VOUCHER_VALUE = 10.0d;
	def PROMOTION_VOUCHER_VALUE_STRING = "10.0%"

	def ABSOLUTE_V_CODE = "xyz";
	def ABSOLUTE_VOUCHER_CODE = "xyz-MHE2-B8L5-LPHE";
	def ABSOLUTE_VOUCHER_DESC = "Voucher Description";
	def ABSOLUTE_VOUCHER_NAME = "New Voucher";
	def ABSOLUTE_VOUCHER_VALUE = 15.0d;
	def ABSOLUTE_VOUCHER_VALUE_STRING = "15.0 USD"
	def USD="USD"

	def getVoucher(code) {
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def con = TestUtil.getSecureConnection("/vouchers/${code}", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, client_credentials_token)
		def response = TestUtil.verifiedJSONSlurper(con, false, false)
		return response
	}


	@Test
	void testGetVoucher() {
		def response = getVoucher(PROMOTION_VOUCHER_CODE)
		assert response.code == PROMOTION_V_CODE
		assert response.voucherCode == PROMOTION_VOUCHER_CODE
		assert response.description == PROMOTION_VOUCHER_DESC
		assert response.name == PROMOTION_VOUCHER_NAME
		assert response.value == PROMOTION_VOUCHER_VALUE
		assert response.valueString == PROMOTION_VOUCHER_VALUE_STRING
		assert response.valueFormatted == PROMOTION_VOUCHER_VALUE_STRING
		assert response.freeShipping == false
	}

	@Test
	void testGetVoucherAbsolute() {
		def response = getVoucher(ABSOLUTE_VOUCHER_CODE)
		assert response.code == ABSOLUTE_V_CODE
		assert response.voucherCode == ABSOLUTE_VOUCHER_CODE
		assert response.description == ABSOLUTE_VOUCHER_DESC
		assert response.name == ABSOLUTE_VOUCHER_NAME
		assert response.value == ABSOLUTE_VOUCHER_VALUE
		assert response.valueString == ABSOLUTE_VOUCHER_VALUE_STRING
		assert response.valueFormatted == ABSOLUTE_VOUCHER_VALUE_STRING
		assert response.freeShipping == true
		assert response.currency.isocode == USD
	}

	@Test
	void testGetNotExistingVoucher() {
		def client_credentials_token = TestUtil.getClientCredentialsToken()
		def con = TestUtil.getSecureConnection("/vouchers/notExistingVoucher", 'GET', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, null, null, client_credentials_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'UnknownIdentifierException'
	}
}