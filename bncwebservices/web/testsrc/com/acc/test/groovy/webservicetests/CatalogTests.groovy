package com.acc.test.groovy.webservicetests
import com.acc.test.groovy.webservicetests.markers.CollectOutputFromTest
import org.junit.Test
import org.junit.experimental.categories.Category

import static com.acc.test.groovy.webservicetests.TestUtil.*

@Category(CollectOutputFromTest.class)
class CatalogTests extends BaseWSTest {

	@Test
	void testGetCatalogsXML() {
		def con = getConnection('/catalogs', 'GET', 'XML')
		assert con.responseCode == HttpURLConnection.HTTP_OK : TestUtil.messageResponseCode(con.responseCode, HttpURLConnection.HTTP_OK)


		def response = verifiedXMLSlurper(con)
		assert response.name() == 'catalogs'
		assert response.catalog.size() == 1
		assert response.catalog[0].id == 'wsTestProductCatalog'
		assert response.catalog[0].name == 'wsTest Product Catalog'
		assert response.catalog[0].url == '/wsTestProductCatalog'
		//TODO change to catalogversion - https://jira.hybris.com/browse/COMWS-34
		assert response.catalog[0].catalogVersions.catalogVersion.size() == 2
		def catVersions = response.catalog[0].catalogVersions.catalogVersion.collect { it.id.toString() }
		assert catVersions.containsAll(['Staged', 'Online'])
		//assert response.catalog[0].catalogVersions.catalogVersion[1].id == 'Online'

		//OPTIONS CATEGORIES
		response = verifiedXMLSlurper(getConnection('/catalogs?options=CATEGORIES', 'GET', 'XML'))
		//TODO catalogVVVersion
		assert ['Staged', 'Online'].contains(response.catalog[0].catalogVersions.catalogVersion[1].id)
		//TODO cateogories
		def onlineCatalog = response.catalog[0].catalogVersions.catalogVersion.find({it.id == 'Online'})
		assert onlineCatalog != null
		assert onlineCatalog.categories.category.size() == 2

		//OPTIONS PRODUCTS (requires categories)
		response = verifiedXMLSlurper(getConnection('/catalogs?options=PRODUCTS,CATEGORIES', 'GET', 'XML'))
		onlineCatalog = onlineCatalog = response.catalog[0].catalogVersions.catalogVersion.find({it.id == 'Online'})
		assert onlineCatalog != null
		assert onlineCatalog.categories.category[0].products.product.size() > 0
		//OPTIONS SUBCATEGORIES (requires categories)
		response = verifiedXMLSlurper(getConnection('/catalogs?options=CATEGORIES,SUBCATEGORIES', 'GET', 'XML'))
		onlineCatalog = onlineCatalog = response.catalog[0].catalogVersions.catalogVersion.find({it.id == 'Online'})
		assert onlineCatalog != null
		assert onlineCatalog.categories.category[0].subcategories.category.size() > 0
	}

	@Test
	void testGetCatalogXML() {
		def con = getConnection('/catalogs/wsTestProductCatalog')

		def response = verifiedXMLSlurper(con)
		assert response.name() == 'catalog'
		assert response.id == 'wsTestProductCatalog'
		assert response.name == 'wsTest Product Catalog'
		assert response.url == '/wsTestProductCatalog'
		//TODO change to catalogVVVersion - https://jira.hybris.com/browse/COMWS-34

		assert response.catalogVersions.catalogVersion.size() == 2
		def catVersions = response.catalogVersions.catalogVersion.collect { it.id.toString() }
		assert catVersions.containsAll(['Staged', 'Online'])
	}

	@Test
	void testGetCatalogVersionXML() {
		def con = getConnection('/catalogs/wsTestProductCatalog/Online')


		def response = verifiedXMLSlurper(con)
		//TODO catalogVersion //TODO change to catalogVVVersion - https://jira.hybris.com/browse/COMWS-34
		assert response.name() == 'catalogVersion'
		assert response.id == 'Online'
		//TODO needs to have '/' first  - https://jira.hybris.com/browse/COMWS-35
		assert response.url == '/wsTestProductCatalog/Online'
	}

	@Test
	void testGetCategoriesXML() {
		def con = getConnection('/catalogs/wsTestProductCatalog/Online/categories/brands')

		def response = verifiedXMLSlurper(con)
		assert response.name() == 'category'
		assert response.id == 'brands'
		assert response.name == 'Brands'
        assert response.url == "/wsTest/catalogs/wsTestProductCatalog/Online/categories/brands"
	}

	//JSON JSON JSON
	@Test
	void testGetCatalogsJSON() {
		def con = getConnection('/catalogs', 'GET', 'JSON')


		def response = verifiedJSONSlurper(con)
		assert response.catalogs.size() == 1
		assert response.catalogs[0].id == 'wsTestProductCatalog'
		assert response.catalogs[0].name == 'wsTest Product Catalog'
		assert response.catalogs[0].url == '/wsTestProductCatalog'
		//TODO change to catalogversion - https://jira.hybris.com/browse/COMWS-34
		assert response.catalogs[0].catalogVersions.size() == 2
		def catVersions = response.catalogs[0].catalogVersions.collect { it.id.toString() }
		assert catVersions.containsAll(['Staged', 'Online'])

		//OPTIONS CATEGORIES
		response = verifiedJSONSlurper(getConnection('/catalogs?options=CATEGORIES', 'GET', 'JSON'))
		//TODO catalogVVVersion
		assert ['Staged', 'Online'].contains(response.catalogs[0].catalogVersions[1].id)
		//TODO cateogories

		def onlineCatalog = response.catalogs[0].catalogVersions.find{ version -> version.id == 'Online'}
		assert onlineCatalog != null
		assert onlineCatalog.categories.category.size() == 2

		//OPTIONS PRODUCTS (requires categories)
		response = verifiedJSONSlurper(getConnection('/catalogs?options=PRODUCTS,CATEGORIES', 'GET', 'JSON'))
		onlineCatalog = response.catalogs[0].catalogVersions.find{ version -> version.id == 'Online'}
		assert onlineCatalog != null
		assert onlineCatalog.categories[0].products.size() > 0

		//OPTIONS SUBCATEGORIES (requires categories)
		response = verifiedJSONSlurper(getConnection('/catalogs?options=CATEGORIES,SUBCATEGORIES', 'GET', 'JSON'))
		onlineCatalog = response.catalogs[0].catalogVersions.find{ version -> version.id == 'Online'}
		assert onlineCatalog != null
		assert onlineCatalog.categories[0].subcategories.size() > 0
	}

	@Test
	void testGetCatalogJSON() {
		def con = getConnection('/catalogs/wsTestProductCatalog', 'GET', 'JSON')

		def response = verifiedJSONSlurper(con)
		assert response.id == 'wsTestProductCatalog'
		assert response.name == 'wsTest Product Catalog'
		assert response.url == '/wsTestProductCatalog'
		//TODO change to catalogVVVersion - https://jira.hybris.com/browse/COMWS-34
		assert response.catalogVersions.size() == 2
		def catVersions = response.catalogVersions.collect { it.id.toString() }
		assert catVersions.containsAll(['Staged', 'Online'])
	}

	@Test
	void testGetCatalogVersionJSON() {
		def con = getConnection('/catalogs/wsTestProductCatalog/Online', 'GET', 'JSON')

		def response = verifiedJSONSlurper(con)
		//TODO catalogVersion //TODO change to catalogVVVersion - https://jira.hybris.com/browse/COMWS-34
		assert response.id == 'Online'
		//TODO needs to have '/' first  - https://jira.hybris.com/browse/COMWS-35
		assert response.url == '/wsTestProductCatalog/Online'
	}

	@Test
	void testGetCategoriesJSON() {
		def con = getConnection('/catalogs/wsTestProductCatalog/Online/categories/brands', 'GET', 'JSON')

		def response = verifiedJSONSlurper(con)
		assert response.id == 'brands'
		assert response.name == 'Brands'
		assert response.subcategories.size() == 0
		assert response.products.size() == 0
	}


}