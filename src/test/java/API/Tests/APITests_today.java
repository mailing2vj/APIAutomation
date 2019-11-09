package API.Tests;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import genericLibrary.RestAssuredAPI;
import io.restassured.response.Response;

public class APITests_today {

	protected static String baseURL = null;
	protected static String accessToken = null;
	protected static String contentType = null;
	protected static Map<String, String> body = new HashMap<>();
	protected static String newUserName = null;
	private static ExtentReports extentReport = null;
	ExtentTest test = null;

	@BeforeClass
	public void init()
	{
		XmlTest xml = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		baseURL = xml.getParameter("baseURL");
		accessToken = xml.getParameter("accessToken");
		contentType = xml.getParameter("contentType");
		String reportFilePath = System.getProperty("user.dir") + "\\Reports\\" + "AutomationExtentReport.html";

		// Create the fresh report file.
		// -----------------------------
		try {
			File report = new File(reportFilePath);

			if(report.exists())
				report.delete();

			report.createNewFile();
		} 
		catch (IOException e1) {}
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportFilePath);
		htmlReporter.config().setDocumentTitle("API Test Automation Results");
		htmlReporter.config().setReportName("API Test Automation Results");
		htmlReporter.config().setTheme(Theme.DARK);
		extentReport = new ExtentReports();
		extentReport.setSystemInfo("User Name", "VJ");
		extentReport.setSystemInfo("Java Version", System.getProperty("java.version"));
		extentReport.attachReporter(htmlReporter);
	}	

	@AfterMethod
	public void afterTest() throws Exception
	{
		RestAssuredAPI.closeConnection();
		extentReport.flush();
	}

	/**
	 * This function is used to assert the body used to create the user with the response of the request.
	 * @param body
	 * @param response
	 * @return
	 */
	public String assertExpectedBodyWithReponse(Map<String, String> body, Response response)
	{
		String result = "";

		Iterator<String> it = body.keySet().iterator();

		while(it.hasNext())
		{
			String key = it.next();
			if(!response.asString().contains("\""+key+"\":\""+body.get(key)))
				result += "\""+key+"\":\""+body.get(key)+"\" is not present in body;";
		}

		return result;
	}

	/**
	 * Upload a new file using the  Upload files API
	 * @throws Exception
	 */
	@Test(description="Upload a new file using the  Upload files API")
	public void UploadFileusingPOST() throws Exception
	{
		try {

			System.out.print("baseURL---------------");
			// Instantiate the extent test.
			String description = Reporter.getCurrentTestResult().getMethod().getDescription();
			String methodName = Reporter.getCurrentTestResult().getMethod().getMethodName();

			test = extentReport.createTest(methodName, description).assignCategory("API Test").assignAuthor("VJ");

			body.put("fileId","1213");
			// Post URL.
			String postURL = "/upload";
			// Make post request and get the response.
			Response response = RestAssuredAPI.POST(baseURL, accessToken, contentType, body, postURL,test);

			test.log(Status.INFO, "");

			System.out.print("RESPPP :"+response);
			String resp = null;

			// Assert that post request is success.
			if(!response.asString().contains("\"success\":true"))
				test.fail("Test case failed."
						+ "Additional info. : "+ response.asString());

			// Assert that expected details present in the response body.
			String result = assertExpectedBodyWithReponse(body, response);

			// Verify that file deatails are returned in the  post request.
			if(result.equalsIgnoreCase(""))
			{
				resp = response.getBody().toString();
				if(resp.contains("status")&&resp.contains("name")&&resp.contains("fileHash")&&resp.contains("createdOn")&&resp.contains("bytesComplted")
						&&resp.contains("size")&&resp.contains("fileId"))
					test.pass("Test case passed. File details returned for the file :"+resp.split("fileId:")[1]

							+ "<br>Additional info. : Response - "+ response.asString());
			}
			else
			{
				test.fail("Test case failed. File details returned for the file :"

						+ "<br>Additional info. : Response - "+ response.asString());
				Assert.fail("Failed");

			}

		}
		catch(Exception ex)
		{
			test.error(ex);

			Assert.fail("Failed");
		}
	} // End CreateUserUsingPostRequest


}
