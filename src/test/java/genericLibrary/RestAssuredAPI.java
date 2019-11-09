package genericLibrary;

import java.util.Map;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

/**
 * @author praveen.sudharsan
 *
 */
public class RestAssuredAPI {


	/**setBaseURL method sets baseURL
	 * @param baseURL as url string
	 */
	public static void setBaseURL(String baseURL, ExtentTest test)
	{
		try
		{
			if(!baseURL.isEmpty()||!baseURL.contains(null))
			{
				RestAssured.baseURI = baseURL;
				test.log(Status.INFO, "Set base URI as " + baseURL);
			}}catch (NullPointerException e) {
				test.log(Status.INFO, "Base URL is not set : - "+e);
			}	
	}

	/**
	 * Returns response of POST API method execution
	 * 
	 * @param baseURL as base url
	 * @param username as accessToken for authentication
	 * @param contentType as content can be of type text,json,xml
	 * @param body as content which can be sent via post request
	 * @param url as service url
	 * @return Response of POST command
	 */
	public static Response POST(String baseURL, String accessToken, String contentType, Map<String, String> body, String postURL, ExtentTest test)
	{
		setBaseURL(baseURL, test);
		Response resp = RestAssured.given()
				.auth().oauth2(accessToken)
				.contentType(contentType)				
				.body(body)
				.post(postURL)
				.andReturn();
		test.log(Status.INFO,"Send POST command");
		test.log(Status.INFO,"URL \n" + postURL);
		test.log(Status.INFO,"Status Code \n" + resp.getStatusCode());
		test.log(Status.INFO,"Time taken to get response is \n" + resp.getTime()+" milli second");
		return resp;
	}

	/**
	 * Returns response of GET API method execution
	 * 
	 * @param baseURL as base url
	 * @param accessToken as authentication key
	 * @param contentType as content can be of type text,json,xml
	 * @param getURL as service url
	 * @return Response of GET command
	 */
	public static Response GET(String baseURL, String accessToken, String contentType, String getURL, ExtentTest test)
	{
		setBaseURL(baseURL, test);
		Response resp = RestAssured.given()
				.auth().oauth2(accessToken)
				.contentType(contentType)
				.get(getURL)
				.andReturn();
		test.log(Status.INFO,"Send GET command");
		test.log(Status.INFO,"URL \n" + getURL);
		test.log(Status.INFO,"Status Code \n" + resp.getStatusCode());
		test.log(Status.INFO,"Time taken to get response is \n" + resp.getTime()+" milli second");
		return resp;
	}

	/**
	 * closeConnection method would be closing idle Connection
	 */
	public static void closeConnection()
	{
		RestAssured.config = RestAssuredConfig.newConfig().
				connectionConfig(ConnectionConfig.connectionConfig().closeIdleConnectionsAfterEachResponse());
	}

}
