/**
 * 
 */
package com.acc.test.groovy.webservicetests.docu;



/**
 * Interface for strategies which save summary of test to file
 */
public interface SaveWSOutputStrategy
{
	public static final String WS_OUTPUT_DIR = "resources/WS_OUTPUT";

	void saveFailedTest(SummaryOfRunningTest summary, Throwable t);

	void saveSucceededTest(SummaryOfRunningTest summary);
}
