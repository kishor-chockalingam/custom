/**
 *
 */
package com.acc.test.groovy.webservicetests.docu

import groovy.transform.ToString

/**
 * Aggregate information about running test (test name, called web service request)
 */
@ToString(includeNames=true)
class SummaryOfRunningTest {
	def String testName;
	List<WSRequestSummary> requests = new ArrayList<WSRequestSummary>();

	void addRequest(String resource, String accept, String method)	{
		requests.add(new WSRequestSummary(resource: resource, accept: accept, httpMethod:method));
	}

	void addResponse(String response) {
		int lastIndex = requests.size() - 1;
		WSRequestSummary last = requests.get(lastIndex);
		if(last.response != null) {
			throw new IllegalStateException();
		}
		last.response = response;
	}
}
