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
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETPortalRoles {

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
	public void PortalRoles_GetRolesPortalMyPermission_Status200() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal/myPermissions";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, myPermissionResponse.asString());

	}

	@Test(groups = "IntegrationTests")
	public void PortalRoles_GetRolesPortalAssignments_Status200() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal/assignments";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, myPermissionResponse.asString());

	}

	@Test(groups = "IntegrationTests")
	public void PortalRoles_PutRolesPortalAssignments_Status204() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * Fetching the roleID
		 */
		String patchURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal/assignments";
		Response getRolesResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, patchURI);
		getRolesResponse.prettyPrint();
		JsonPath rolesJson = getRolesResponse.jsonPath();
		portalRoles = rolesJson.get("portalRoleId");
		userId = rolesJson.get("userId");
		String rolesId = portalRoles.get(0);
		String user = userId.get(0);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("portalRoles", rolesId);
		map.put("userId", user);
		ETUser_Pojo pojo = new ETUser_Pojo();
		String putRoles = pojo.putPortalRoles(map);
		Response putRolesResponse = rolesUtils.put_URLPOJO(URL, AuthorizationKey, patchURI, putRoles);
		putRolesResponse.prettyPrint();
		Assert.assertEquals(putRolesResponse.getStatusCode(), 204);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(putRolesResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, putRolesResponse.asString());

	}

	@Test(groups = "IntegrationTests")
	public void PortalRoles_GetRolesPortal_Status200() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, myPermissionResponse.asString());

	}

	@Test(groups = "IntegrationTests")
	public void PortalRoles_PostRolesPortal_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", post.getProperty("postPermissionName") + rolesUtils.getRandomNumber(1, 50));
		map.put("permissions", post.getProperty("postPermission"));
		ETUser_Pojo po = new ETUser_Pojo();
		String createPortalRoles = po.createPortalRoles(map);

		Response postPortalRolesRes = rolesUtils.post_URLPOJO(URL, AuthorizationKey, myPermissionURI,
				createPortalRoles);
		postPortalRolesRes.prettyPrint();
		Assert.assertEquals(postPortalRolesRes.getStatusCode(), 200);
	}

	@Test(groups = "IntegrationTests")
	public void PortalRoles_PatchRolesPortal_Status204() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", post.getProperty("postPermissionName") + getRandomAlphaNum());
		map.put("permissions", post.getProperty("postPermission"));
		ETUser_Pojo po = new ETUser_Pojo();
		String createPortalRoles = po.createPortalRoles(map);

		Response postPortalRolesRes = rolesUtils.post_URLPOJO(URL, AuthorizationKey, myPermissionURI,
				createPortalRoles);
		postPortalRolesRes.prettyPrint();
		JsonPath rolesJson = postPortalRolesRes.jsonPath();
		String rolesId = rolesJson.get("id");
		System.out.println("Roles id====>" + rolesId);
		/**
		 * PERforming patch operation
		 */
		String patchURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal/" + rolesId;
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("permissions", post.getProperty("patchPermission"));
		ETUser_Pojo po1 = new ETUser_Pojo();
		String patch = po1.putPortalRoles(map1);
		Response patchPortalRolesRes = rolesUtils.patch_URLPOJO(URL, AuthorizationKey, patchURI, patch);
		patchPortalRolesRes.prettyPrint();
		Assert.assertEquals(patchPortalRolesRes.getStatusCode(), 204);

	}

	@Test(groups = "IntegrationTests")
	public void PortalRoles_DeleteRolesPortal_Status204() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal";
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", post.getProperty("postPermissionName") + getRandomAlphaNum());
		map.put("permissions", post.getProperty("postPermission"));
		ETUser_Pojo po = new ETUser_Pojo();
		String createPortalRoles = po.createPortalRoles(map);

		Response postPortalRolesRes = rolesUtils.post_URLPOJO(URL, AuthorizationKey, myPermissionURI,
				createPortalRoles);
		postPortalRolesRes.prettyPrint();
		JsonPath rolesJson = postPortalRolesRes.jsonPath();
		String rolesId = rolesJson.get("id");
		String deleteURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/portal/" + rolesId;
		Response deleteRolesRes = rolesUtils.delete(URL, AuthorizationKey, deleteURI);
		Assert.assertEquals(deleteRolesRes.getStatusCode(), 204);
	}

}
