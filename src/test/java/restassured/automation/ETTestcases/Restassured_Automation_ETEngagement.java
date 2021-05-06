package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.ETUser_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETEngagement {
	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> engagementId;
	

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ETBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey2");
		Awaitility.reset();
		Awaitility.setDefaultPollDelay(999, MILLISECONDS);
		Awaitility.setDefaultPollInterval(99, SECONDS);
		Awaitility.setDefaultTimeout(99, SECONDS);

	}

	@Test(groups = "IntegrationTests")
	public void ETEngagement_GetListOfMyEngagement_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response myPermissionResponse = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, myPermissionURI, "1", "100");
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,myPermissionResponse.asString());
		//rolesUtils.validate_HTTPStrictTransportSecurity(myPermissionResponse);
	}
	@Test(groups = "IntegrationTests")
	public void ETEngagement_GetListOfAllEngagement_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response myPermissionResponse = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, myPermissionURI, "1", "100");
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,myPermissionResponse.asString());
		//rolesUtils.validate_HTTPStrictTransportSecurity(myPermissionResponse);
	}
	
	@Test(groups = "IntegrationTests")
	public void ETEngagement_GetEngagementById_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(1);
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id;
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,myPermissionResponse.asString());
		//rolesUtils.validate_HTTPStrictTransportSecurity(myPermissionResponse);
	}
	@Test(groups = "IntegrationTests")
	public void ETEngagement_GetTheUsersOwnEngagementPermissionsForTheSpecifiedEngagement_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(0);
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/myPermissions";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,myPermissionResponse.asString());
		//rolesUtils.validate_HTTPStrictTransportSecurity(myPermissionResponse);
	}
	@Test(groups = "IntegrationTests")
	public void ETEngagement_GetEngagementEvent_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(1);
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/events";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,myPermissionResponse.asString());
		//rolesUtils.validate_HTTPStrictTransportSecurity(myPermissionResponse);
	}
	
	
	
}
