package com.acc.test.groovy.webservicetests

import com.acc.test.groovy.webservicetests.markers.CollectOutputFromTest

import org.junit.Test
import org.junit.experimental.categories.Category

import groovy.json.*


@Category(CollectOutputFromTest.class)
class MiscTests extends BaseWSTest {

	@Test
	void testGetTitleCodesJSON() {
		def con = TestUtil.getConnection("/titles", 'GET', 'JSON')

		def response = TestUtil.verifiedJSONSlurper(con)
		assert response.titles.size() > 0
	}

	@Test
	void testGetTitleCodesXML() {
		def con = TestUtil.getConnection("/titles", 'GET', 'XML')
		def response = TestUtil.verifiedXMLSlurper(con)
		assert response.name() == "titles" : 'Root element is not <titles>'
		assert response.title.size() > 0
	}

	@Test
	void testGetCardTypesJSON() {
		def con = TestUtil.getConnection("/cardtypes", 'GET', 'JSON')
		def response = TestUtil.verifiedJSONSlurper(con)
		def codes = [
			'maestro',
			'switch',
			'mastercard_eurocard',
			'amex',
			'diners',
			'visa',
			'master'
		]
		assert response.cardTypes.size() == codes.size()
		assert response.cardTypes.findAll { card -> card.code in codes }.size() == codes.size()
	}

	@Test
	void testGetCardTypesXML() {
		def con = TestUtil.getConnection("/cardtypes", 'GET', 'XML')
		def response = TestUtil.verifiedXMLSlurper(con)
		def codes = [
			'maestro',
			'switch',
			'mastercard_eurocard',
			'amex',
			'diners',
			'visa',
			'master'
		]
		assert response.name() == "cardTypes" : 'Root element is not <cardTypes>'
		assert response.cardType.size() == codes.size()
		assert response.cardType.findAll { card -> card.code in codes }.size() == codes.size()
	}

	@Test
	void testGetDeliveryCountriesJSON() {
		def con = TestUtil.getConnection("/deliverycountries", 'GET', 'JSON')
		def response = TestUtil.verifiedJSONSlurper(con)
		assert response.countries.size() > 0
	}

	@Test
	void testGetDeliveryCountriesXML() {
		def con = TestUtil.getConnection("/deliverycountries", 'GET', 'XML')
		def response = TestUtil.verifiedXMLSlurper(con);
		assert response.name() == "countries" : 'Root element is not <countries>'
		assert response.country.size() > 0
	}

	@Test
	void testGetCurrenciesJSON() {
		def con = TestUtil.getConnection("/currencies", 'GET', 'JSON')
		def response = TestUtil.verifiedJSONSlurper(con)
		def currencies = ['USD', 'JPY']
		assert response.currencies.size() == currencies.size()
		assert response.currencies.findAll { currency -> currency.isocode in currencies }.size() == currencies.size()
	}

	@Test
	void testGetCurrenciesXML() {
		def con = TestUtil.getConnection("/currencies", 'GET', 'XML')
		def response = TestUtil.verifiedXMLSlurper(con);
		def currencies = ['USD', 'JPY']
		assert response.name() == "currencies" : 'Root element is not <currencies>'
		assert response.currency.size() == currencies.size()
		assert response.currency.findAll { currency -> currency.isocode in currencies }.size() == currencies.size()
	}

	@Test
	void testGetLanguagesJSON() {
		def con = TestUtil.getConnection("/languages", 'GET', 'JSON')
		def response = TestUtil.verifiedJSONSlurper(con)
		def languages = ['ja', 'en', 'de', 'zh']
		assert response.languages.size() == languages.size()
		assert response.languages.findAll { language -> language.isocode in languages }.size() == languages.size()
	}

	@Test
	void testGetLanguagesXML() {
		def con = TestUtil.getConnection("/languages", 'GET', 'XML')
		def response = TestUtil.verifiedXMLSlurper(con)
		def languages = ['ja', 'en', 'de', 'zh']
		assert response.name() == 'languages'
		assert response.language.size() == languages.size()
		assert response.language.findAll { language -> language.isocode in languages }.size() == languages.size()
	}
}