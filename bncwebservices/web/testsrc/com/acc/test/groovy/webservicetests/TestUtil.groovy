package com.acc.test.groovy.webservicetests

import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.X509TrustManager

import groovy.json.*

class TestUtil {
	static String HOST = 'localhost'
	static String BASE = "http://${HOST}:9001/rest/v1/wsTest"
	static String SECURE_BASE = "https://${HOST}:9002/rest/v1/wsTest"
	//static String OAUTH2_TOKEN_ENDPOINT = 'https://localhost:9002/rest/oauth/token' for testing with https
	static String OAUTH2_TOKEN_ENDPOINT = "http://${HOST}:9001/rest/oauth/token"
	static String CLIENT_ID = 'mobile_android'
	static String CLIENT_SECRET = 'secret'
	static String TRUSTED_CLIENT_ID = 'trusted_client'
	static String TRUSTED_CLIENT_SECRET = 'secret'
	static String REDIRECT_URI = "http://${HOST}:8080/oauth2_callback"
	static String LOGOUT_URL = "https://${HOST}:9002/rest/v1/customers/current/logout"
	//static String BASE = 'http://api.hybrisdev.com:9001/rest/v1/electronics'
	//static String SECURE_BASE = 'http://api.hybrisdev.com:9001/rest/v1/electronics'

	static String USERNAME = 'demoCustomer'
	static String PASSWORD = '1234'

	static getClientCredentialsToken(clientId=CLIENT_ID, clientSecret=CLIENT_SECRET)
	{
		return getClientCredentialsAccessTokenMap(clientId, clientSecret).access_token
	}

	static getTrustedClientCredentialsToken(clientId=TRUSTED_CLIENT_ID, clientSecret=TRUSTED_CLIENT_SECRET)
	{
		return getClientCredentialsAccessTokenMap(clientId, clientSecret).access_token
	}

	static getClientCredentialsAccessTokenMap(clientId, clientSecret)
	{
		fakeSecurity()

		//direct exchange of username and password for access token
		def con = OAUTH2_TOKEN_ENDPOINT.toURL().openConnection()
		con.doOutput = true
		con.requestMethod = 'POST'
		//con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.outputStream << "client_id=${clientId}&client_secret=${clientSecret}&grant_type=client_credentials"

		if (con.errorStream)
			con.errorStream.text

		def body = con.inputStream.text
		println body
		def response = new JsonSlurper().parseText(body)

		assert response.access_token
		assert response.token_type == 'bearer'
		assert response.expires_in


		println "Access Token: ${response.access_token}"
		return response

	}

	static getAccessToken(username=USERNAME, password=PASSWORD)
	{
		return getAccessTokenMap(username,password).access_token
	}

	static getAccessTokenMap(username=USERNAME, password=PASSWORD)
	{
		fakeSecurity()

		//direct exchange of username and password for access token
		def con = OAUTH2_TOKEN_ENDPOINT.toURL().openConnection()
		con.doOutput = true
		con.requestMethod = 'POST'
		//con.setRequestProperty("Accept", "application/json");
		con.outputStream << "client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}&grant_type=password&username=${username}&password=${password}"

		def body = con.inputStream.text
		println body
		def response = new JsonSlurper().parseText(body)

		assert response.access_token
		assert response.token_type == 'bearer'
		assert response.refresh_token
		assert response.expires_in


		println "Access Token: ${response.access_token}"
		return response
	}

	static refreshToken(refresh_token)
	{
		fakeSecurity()

		//direct exchange of username and password for access token
		def con = OAUTH2_TOKEN_ENDPOINT.toURL().openConnection()
		con.doOutput = true
		con.requestMethod = 'POST'
		con.outputStream << "refresh_token=${refresh_token}&client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}&redirect_uri=${URLEncoder.encode(REDIRECT_URI, 'UTF-8')}&grant_type=refresh_token"

		println con.errorStream
		def body = con.inputStream.text

		def response = new JsonSlurper().parseText(body)

		assert response.access_token
		assert response.token_type == 'bearer'
		assert response.refresh_token
		assert response.expires_in

		println "Refreshed Access Token: ${response.access_token}"
		return response.access_token

	}

	static getConnection(path, method='GET', accept='XML', code=HttpURLConnection.HTTP_OK, body = null, cookie = null, auth = null) {
		def url
		if (path.startsWith('http'))
			url = path
		else
			url = BASE + path

		def con = url.toURL().openConnection()

		if (method == "POST" || method == 'PUT')
			con.doOutput = true

		if (accept == 'XML')
			con.setRequestProperty("Accept", "application/xml");
		else if (accept == 'JSON')
			con.setRequestProperty("Accept", "application/json");

		if (auth)
		{
			if (auth.contains(':'))
				con.setRequestProperty("Authorization", "Basic " + auth.toString().bytes.encodeBase64().toString())
			else
				con.setRequestProperty('Authorization', "Bearer ${auth}")
		}

		if (cookie)
			con.setRequestProperty("Cookie", cookie)

		con.requestMethod = method

		if (body)
		{
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
			con.outputStream << body
		}

		//if (con.responseCode >= 400)
		//	println con.errorStream.text

		assert con.responseCode == code : "Expected response code ${code}, but got ${con.responseCode}"

		return con
	}

	static getURLConnection(url, method) {
		def con = url.toURL().openConnection()
		con.requestMethod = method
		return con
	}

	static getSecureURLConnection(url, method) {
		fakeSecurity()
		def con = url.toURL().openConnection()
		con.requestMethod = method
		return con
	}

	static getSecureConnection(path, method='GET', accept='XML', code=HttpURLConnection.HTTP_OK, body = null, cookie = null, auth = null) {
		fakeSecurity()

		def url
		if (path.startsWith('https'))
			url = path
		else
			url = SECURE_BASE + path

		def con = url.toURL().openConnection()

		if (method == "POST" || method == 'PUT'){
			con.doOutput = true
		}

		if (accept == 'XML')
			con.setRequestProperty("Accept", "application/xml");
		else if (accept == 'JSON')
			con.setRequestProperty("Accept", "application/json");


		if (auth)
		{
			if (auth.contains(':'))
				con.setRequestProperty("Authorization", "Basic " + auth.toString().bytes.encodeBase64().toString())
			else
				con.setRequestProperty('Authorization', "Bearer ${auth}")
		}

		if (cookie)
			con.setRequestProperty("Cookie", cookie)

		con.requestMethod = method

		if (body)
		{
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
			con.outputStream << body
		}

		//if (con.responseCode >= 400)
		//	println con.errorStream.text

		assert con.responseCode == code : "Expected response code ${code}, but got ${con.responseCode}"

		return con
	}

	static fakeSecurity()
	{
		def trustManager = new DummyTrustManager()
		def hostnameVerifier = new DummyHostnameVerifier();
		def sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, [trustManager]as X509TrustManager[], new java.security.SecureRandom());
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	}

	static messageResponseCode(returned, expected) {
		return "Response Code is: " + returned + ", expected: " + expected
	}

	static basicAuth(con) {
		String userpassword = TestUtil.USERNAME + ":" + TestUtil.PASSWORD;
		String encodedAuthorization = userpassword.bytes.encodeBase64().toString()
		con.setRequestProperty("Authorization", "Basic " + encodedAuthorization)
	}

	static basicAuth(con, username, password) {
		String userpassword = username + ":" + password;
		String encodedAuthorization = userpassword.bytes.encodeBase64().toString()

		assert con instanceof HttpsURLConnection : "Basic Auth always requires HTTPS!"

		con.setRequestProperty("Authorization", "Basic " + encodedAuthorization)
	}

	static cookieString(con, cookieString) {
		con.setRequestProperty("Cookie", cookieString)
	}

	static acceptXML(con) {
		con.setRequestProperty("Accept", "application/xml");
	}

	static acceptJSON(con) {
		con.setRequestProperty("Accept", "application/json");
	}

	static verifyJSON(jsonString, fail=true)
	{
		assert !jsonString.contains('de.hybris') : "JSON Serialization issue - Check XStream Config! ${jsonString}"
		TestUtil.dataModelWarnings(jsonString, fail)
	}

	static verifyXML(xmlString, fail=true)
	{
		//TODO change to strong assert
		assert !xmlString.contains('de.hybris') : "XML Serialization issue - Check XStream Config! ${xmlString}"
		TestUtil.dataModelWarnings(xmlString, fail)
	}

	static dataModelWarnings(text, fail=true)
	{
		def m = text =~ /([^<>"]*Data[^- ])/

		m.each {
			if (fail)
				assert false : "Choose a better name for ${it[0]}?"
			else
				println "WARNING: Choose a better name for ${it[0]}?"
		}


	}

	static verifiedXMLSlurper(con, debug=false, fail=true)
	{
		def body = con.inputStream.text

		if (debug)
			println body

		TestUtil.verifyXML(body, fail)
		return new XmlSlurper().parseText(body)
	}

	static verifiedJSONSlurper(con, debug=false, fail=true)
	{
		def body = con.inputStream.text

		if (debug)
			println body

		TestUtil.verifyJSON(body, fail)
		return new JsonSlurper().parseText(body)
	}
}




