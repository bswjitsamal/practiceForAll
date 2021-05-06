package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.ETRoles_PortalRoles_Pojo;
import restassured.automation.Pojo.ETUser_Pojo;
import restassured.automation.Pojo.ET_Engagement_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETEngagementTeam {

	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> engagementId;
	List<String> userId;
	List<String> roleId;

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
	public void ETEngagement_GetEngagementHistory_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(0);

		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team/history";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, myPermissionResponse.asString());
		// rolesUtils.validate_HTTPStrictTransportSecurity(myPermissionResponse);

	}

	@Test(groups = "IntegrationTests")
	public void ETEngagement_GetEngagementRoles_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(1);

		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team/roles";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(myPermissionResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, myPermissionResponse.asString());
		// rolesUtils.validate_HTTPStrictTransportSecurity(myPermissionResponse);
	}

	@Test(groups = "IntegrationTests")
	public void ETEngagement_RemoveAccessManager_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(1);
		String user = "9a7faa38-92eb-4934-a48f-ed43478d5f86";
		String accessUri = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team/accessManager";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", user);
		ETUser_Pojo po = new ETUser_Pojo();
		String userOobj = po.UpdateAccessManager(map);
		Response putAccessRes = rolesUtils.put_URLPOJO(URL, AuthorizationKey, accessUri, userOobj);
		putAccessRes.prettyPrint();
		Response deleteAccessRes = rolesUtils.delete_URLPOJO(URL, AuthorizationKey, accessUri, userOobj);
		deleteAccessRes.prettyPrint();
		Assert.assertEquals(deleteAccessRes.getStatusCode(), 204);
	}

	@Test(groups = "IntegrationTests")
	public void ETEngagement_UpdateAccessManager_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(0);
		String user = "9a7faa38-92eb-4934-a48f-ed43478d5f86";
		String accessUri = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team/accessManager";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", user);
		ETUser_Pojo po = new ETUser_Pojo();
		String userOobj = po.UpdateAccessManager(map);
		Response deleteAccessRes = rolesUtils.delete_URLPOJO(URL, AuthorizationKey, accessUri, userOobj);
		deleteAccessRes.prettyPrint();
		Response putAccessRes = rolesUtils.put_URLPOJO(URL, AuthorizationKey, accessUri, userOobj);
		putAccessRes.prettyPrint();
		Assert.assertEquals(putAccessRes.getStatusCode(), 204);
	}

	@Test(groups = "IntegrationTests")
	public void ETEngagement_UpdateEngagementTeamMember_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * Getting user id
		 */
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/users/withRoles";
		Response myrolesResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myrolesResponse.prettyPrint();
		JsonPath rolsJson = myrolesResponse.jsonPath();
		userId = rolsJson.get("id");
		String user = userId.get(0);
		/**
		 * fetching engagement id
		 */

		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(1);
		/**
		 * Fetching role id
		 */

		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team/roles";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		JsonPath rolesJson = myPermissionResponse.jsonPath();
		List<String> arr = rolesJson.get("id");
		String role = arr.get(1);
		System.out.println("Role id====>" + role + "\n\n" + "User id----->" + user + "\n\n" + "Engagment id---->" + id);
		/**
		 * Performing Delete operation
		 */

		String teamId = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", user);
		// map.put("engagementRoleId", role);
		ETUser_Pojo po = new ETUser_Pojo();
		String team = po.UpdateEngagementTeamMember(map);
		Response delRes = rolesUtils.delete_URLPOJO(URL, AuthorizationKey, teamId, team);

		delRes.prettyPrint();
		Assert.assertEquals(delRes.getStatusCode(), 204);

		/**
		 * performing put operation
		 */
		String teamId1 = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team";

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("userId", user);
		map1.put("engagementTeamRoleId", role);
		ETUser_Pojo po1 = new ETUser_Pojo();
		String team1 = po1.UpdateEngagementTeamMember(map1);
		Response putRes = rolesUtils.put_URLPOJO(URL, AuthorizationKey, teamId1, team1);
		putRes.prettyPrint();
		Assert.assertEquals(putRes.getStatusCode(), 200);

	}
	
	@Test(groups = "IntegrationTests")
	public void ETEngagement_DeleteEngagementTeamMember_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * Getting user id
		 */
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/roles/users/withRoles";
		Response myrolesResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myrolesResponse.prettyPrint();
		JsonPath rolsJson = myrolesResponse.jsonPath();
		userId = rolsJson.get("id");
		String user = userId.get(0);
		/**
		 * fetching engagement id
		 */

		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(1);
		/**
		 * Fetching role id
		 */

		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team/roles";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		JsonPath rolesJson = myPermissionResponse.jsonPath();
		List<String> arr = rolesJson.get("id");
		String role = arr.get(1);
		System.out.println("Role id====>" + role + "\n\n" + "User id----->" + user + "\n\n" + "Engagment id---->" + id);
		

		/**
		 * performing put operation
		 */
		String teamId1 = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team";

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("userId", user);
		map1.put("engagementTeamRoleId", role);
		ETUser_Pojo po1 = new ETUser_Pojo();
		String team1 = po1.UpdateEngagementTeamMember(map1);
		Response putRes = rolesUtils.put_URLPOJO(URL, AuthorizationKey, teamId1, team1);
		putRes.prettyPrint();
		JsonPath putJson=putRes.jsonPath();
		String user1=putJson.get("user");
		//Assert.assertEquals(putRes.getStatusCode(), 200);
		/**
		 * Performing Delete operation
		 */

		String teamId = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/" + id + "/team";
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", user1);
		// map.put("engagementRoleId", role);
		ETUser_Pojo po = new ETUser_Pojo();
		String team = po.UpdateEngagementTeamMember(map);
		Response delRes = rolesUtils.delete_URLPOJO(URL, AuthorizationKey, teamId, team);

		delRes.prettyPrint();
		Assert.assertEquals(delRes.getStatusCode(), 204);

	}
	

}
