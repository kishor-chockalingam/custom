/**
 *
 */
package com.acc.test.groovy.webservicetests

import com.acc.test.groovy.webservicetests.docu.SaveWSOutputStrategy
import com.acc.test.groovy.webservicetests.docu.TestUtilCustomDelegatingMetaClass
import com.acc.test.groovy.webservicetests.markers.AvoidCollectingOutputFromTest
import com.acc.test.groovy.webservicetests.markers.CollectOutputFromTest

import org.codehaus.groovy.runtime.InvokerHelper
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.experimental.categories.Categories
import org.junit.experimental.categories.Categories.ExcludeCategory
import org.junit.experimental.categories.Categories.IncludeCategory
import org.junit.runner.RunWith

/**
 * Suite contains all tests from {@link AllTests} suite, including test from {@link CollectOutputFromTest} category
 * and excluding test from {@link AvoidCollectingOutputFromTest} category. Suite log responses from webservices
 */
@RunWith(Categories.class)
@IncludeCategory(CollectOutputFromTest.class)
@ExcludeCategory(AvoidCollectingOutputFromTest.class)
class DocumentationSuite extends AllTests {

	private static boolean isSuiteRunning = false;
	public static boolean isSuiteRunning() {
		return isSuiteRunning;
	}

	private static MetaClass defaultTestUtilMetaClass = null;

	@BeforeClass
	public static void beforeClass() {
		isSuiteRunning = true;
		defaultTestUtilMetaClass = TestUtil.getMetaClass();
		def customMetaClass = new TestUtilCustomDelegatingMetaClass(TestUtil.class)
		InvokerHelper.metaRegistry.setMetaClass(TestUtil.class, customMetaClass)
		cleanOutputDir();
	}

	@AfterClass
	public static void afterClass() {
		isSuiteRunning = false;
		InvokerHelper.metaRegistry.setMetaClass(TestUtil.class, defaultTestUtilMetaClass);
	}

	private static void cleanOutputDir() {
		File dir = new File(SaveWSOutputStrategy.WS_OUTPUT_DIR)
		dir.deleteDir();
	}
}
