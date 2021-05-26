package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

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

public class Restassured_Automation_ETRolesUser {
	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> portalRoles;
	List<String> userId;
	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 5);
	}

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
	public void PortalRoles_GetQueryForUsers_Status200() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal/assignments";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		JsonPath getJson = myPermissionResponse.jsonPath();
		userId = getJson.get("userId");
		String user = userId.get(4);
		String email = "5f9f190d-69c9-4660-9d82-9b47f4432513";

		String rolesURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/users/search/";
		Response getRolesSearchResponse = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, rolesURI,"UserId",userId.get(4));
		getRolesSearchResponse.prettyPrint();
		Assert.assertEquals(getRolesSearchResponse.getStatusCode(), 200);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getRolesSearchResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getRolesSearchResponse.asString());

	}

	@Test(groups = "IntegrationTests")
	public void PortalRoles_GetQueryForUsers_Status400() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal/assignments";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		String rolesURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/users/search";
		Response getRolesSearchResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, rolesURI);
		getRolesSearchResponse.prettyPrint();
		Assert.assertEquals(getRolesSearchResponse.getStatusCode(), 400);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getRolesSearchResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getRolesSearchResponse.asString());

	}

	@Test(groups = "IntegrationTests")
	public void PortalRoles_GetUserWithRoles_Status200() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/users/withRoles";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,myPermissionResponse.asString());
	}
	@Test(groups = "IntegrationTests")
	public void PortalRoles_GetUserWithRoles_Status400() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("badmemberFirmSlug") + "/roles/users/withRoles";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 400);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,myPermissionResponse.asString());
	}
	
	
}
