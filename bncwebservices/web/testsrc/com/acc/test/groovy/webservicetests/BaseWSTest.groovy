/**
 *
 */
package com.acc.test.groovy.webservicetests

import static org.junit.Assert.*

import com.acc.test.groovy.webservicetests.docu.BaseWSTestWatcher

import org.junit.Rule

/**
 * Base class for all groovy webservice tests
 */
class BaseWSTest {

	/**
	 * Ancillary method to mark that some webservice resources is not tested
	 * @param resource
	 */
	protected void missingTest(String resource) {
		fail("Missing test for resource :" + resource);
	}

	@Rule
	public BaseWSTestWatcher testWatcher = new BaseWSTestWatcher();
}