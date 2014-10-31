/**
 *
 */
package com.acc.test.groovy.webservicetests.docu

/**
 * Aggregate information about web service request: resurce, httpMethod, accept(XML|JSON) and response
 */
class WSRequestSummary {
	def String resource;
	def String accept;
	def String httpMethod;
	def Object response;
}
