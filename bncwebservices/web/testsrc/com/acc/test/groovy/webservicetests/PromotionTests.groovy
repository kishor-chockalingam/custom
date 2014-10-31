package com.acc.test.groovy.webservicetests

import com.acc.test.groovy.webservicetests.markers.AvoidCollectingOutputFromTest

import org.junit.Test
import org.junit.experimental.categories.Category

@Category(AvoidCollectingOutputFromTest.class)
class PromotionTests extends BaseWSTest {

	def ORDER_PROMOTION = "order";
	def PRODUCT_PROMOTION = "product";
	def ALL_PROMOTION = "all";
	def PROMOTION_GROUP = "wsTestPromoGrp";
	def PROMOTION_CODE = "WS_OrderThreshold15Discount"
	def PROMOTION_TYPE = "Order threshold fixed discount"
	def PROMOTION_DESCRIPTION = "You saved bunch of bucks for spending quite much"
	def PROMOTION_END_DATE = "2099-01-01T00:00:00"
	def PROMOTION_START_DATE = "2000-01-01T00:00:00"
	def PROMOTION_ENABLED = true
	def PROMOTION_TITLE = PROMOTION_DESCRIPTION
	def PROMOTION_PRIORITY = 500

	def getPromotions(String params) {
		def client_credentials_token = TestUtil.getTrustedClientCredentialsToken()
		def con = TestUtil.getSecureConnection("/promotions?" + params, 'GET', 'XML', HttpURLConnection.HTTP_OK, null, null, client_credentials_token)
		def response = TestUtil.verifiedXMLSlurper(con, true, false)
		return response;
	}

	def getPromotionsJSON(String params) {
		def client_credentials_token = TestUtil.getTrustedClientCredentialsToken()
		def con = TestUtil.getSecureConnection("/promotions?${params}", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, client_credentials_token)
		def response = TestUtil.verifiedJSONSlurper(con, false, false)
		return response;
	}

	def getPromotion(code, params = null) {
		def client_credentials_token = TestUtil.getTrustedClientCredentialsToken()
		def con = TestUtil.getSecureConnection("/promotions/${code}?${params}", 'GET', 'JSON', HttpURLConnection.HTTP_OK, null, null, client_credentials_token)
		def response = TestUtil.verifiedJSONSlurper(con, false, false)
		return response
	}

	@Test
	void testGetAllPromotion() {
		def response = getPromotions("type=${ALL_PROMOTION}");
		assert response.name() == 'promotions'
		assert response.promotion
		assert response.promotion.size() > 0
	}


	@Test
	void testGetProductPromotion() {
		def response = getPromotions("type=${PRODUCT_PROMOTION}");
		assert response.name() == 'promotions'
		assert response.promotion
		assert response.promotion.size() > 0
	}

	@Test
	void testGetOrderPromotion() {
		def response = getPromotions("type=${ORDER_PROMOTION}");
		assert response.name() == 'promotions'
		assert response.promotion
		assert response.promotion.size() > 0
	}

	@Test
	void testGetAllPromotionJSON() {
		def response = getPromotionsJSON("type=${ALL_PROMOTION}");
		assert response.promotions.size() > 0
	}

	@Test()
	void testGetPromotionForNotSupportedType() {
		def client_credentials_token = TestUtil.getTrustedClientCredentialsToken()
		def con = TestUtil.getSecureConnection("/promotions?type=notSupportedType", 'GET', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, null, null, client_credentials_token)
		def response = new XmlSlurper().parseText(con.errorStream.text)
		assert response.'class' == 'IllegalArgumentException'
	}

	@Test()
	void testGetPromotionWithoutSecureToken() {
		def con = TestUtil.getSecureConnection("/promotions?type=${ALL_PROMOTION}", 'GET', 'XML', HttpURLConnection.HTTP_UNAUTHORIZED)
	}

	@Test()
	void testGetPromotionWithoutMandatoryParam() {
		def client_credentials_token = TestUtil.getTrustedClientCredentialsToken()
		def con = TestUtil.getSecureConnection("/promotions", 'GET', 'XML', HttpURLConnection.HTTP_BAD_REQUEST, null, null, client_credentials_token)
	}

	@Test
	void testGetAllPromotionForGroup() {
		def response = getPromotions("type=${ALL_PROMOTION}&promotionGroup=${PROMOTION_GROUP}");
		assert response.name() == 'promotions'
		assert response.promotion
		assert response.promotion.size() == 5
	}

	@Test
	void testGetProductPromotionForGroup() {
		def response = getPromotions("type=${PRODUCT_PROMOTION}&promotionGroup=${PROMOTION_GROUP}");
		assert response.name() == 'promotions'
		assert response.promotion
		assert response.promotion.size() == 3
	}

	@Test
	void testGetOrderPromotionForGroup() {
		def response = getPromotions("type=${ORDER_PROMOTION}&promotionGroup=${PROMOTION_GROUP}");
		assert response.name() == 'promotions'
		assert response.promotion
		assert response.promotion.size() == 2
	}

	@Test
	void testGetOrderPromotionForGroupJSON() {
		def response = getPromotionsJSON("type=${ORDER_PROMOTION}&promotionGroup=${PROMOTION_GROUP}");
		assert response.promotions.size() == 2
		def promotion = response.promotions.find { it.code == "WS_OrderThreshold15Discount" }
		assert promotion.promotionType == "Order threshold fixed discount"
	}

	@Test
	void testGetPromotionByCode() {
		def response = getPromotion(PROMOTION_CODE)

		// BASIC
		assert response.code == PROMOTION_CODE
		assert response.promotionType == PROMOTION_TYPE
		assert response.description == PROMOTION_DESCRIPTION
		assert response.endDate.startsWith(PROMOTION_END_DATE)
	}

	@Test
	void testGetPromotionByCodeAndOptions() {
		def response = getPromotion(PROMOTION_CODE, "options=BASIC,EXTENDED")

		// BASIC
		assert response.code == PROMOTION_CODE
		assert response.promotionType == PROMOTION_TYPE
		assert response.description == PROMOTION_DESCRIPTION
		assert response.endDate.startsWith(PROMOTION_END_DATE)
		// EXTENDED
		assert response.startDate.startsWith(PROMOTION_START_DATE)
		assert response.enabled == PROMOTION_ENABLED
		assert response.title == PROMOTION_TITLE
		assert response.priority == PROMOTION_PRIORITY
		assert response.promotionGroup == PROMOTION_GROUP
		assert response.restrictions.size() == 1

		def restriction = response.restrictions[0]
		assert restriction.restrictionType == "User restriction"
		assert restriction.description == "Only users: regulargroup"
	}
}