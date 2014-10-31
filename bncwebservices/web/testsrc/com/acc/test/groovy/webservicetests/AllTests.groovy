package com.acc.test.groovy.webservicetests
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite.class)
@Suite.SuiteClasses([MiscTests.class,
	FlowTests.class,CustomerTests.class, CartTests.class,CatalogTests.class,
	OAuth2Tests.class,ProductTests.class,OCCDemo_Oauth2.class,LogoutTests.class,
	CustomerGroupTests.class, HttpsRestrictedUrlsTests.class, StoresTests.class,  AddressTest.class, PromotionTests.class ])
class AllTests {
}
