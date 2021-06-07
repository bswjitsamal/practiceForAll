package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	List<String> methodologyId;
	

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
		String id=engagementId.get(0);
		
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
		String id=engagementId.get(0);
		
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
	
	//@Test(groups = "IntegrationTests")
	public void ETEngagement_CreateAnNewEngagement_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		ArrayList<Map<String, ?>> accessManager = allEngagmentJson.get("accessManagers");
		String userid = String.valueOf(accessManager.get(1));
		methodologyId = allEngagmentJson.get("methodology");
		
		String engagementID = engagementId.get(0);
		String accessUserId=userid.substring(1, 37);
		String methodology=methodologyId.get(0);
		
		System.out.println("engagement id   "+engagementID+"    user id    "+accessUserId+"    methodology  "+methodology);
		
		String EngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements";
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("title",post.getProperty("postETEngagementTitle"));
		map.put("client",post.getProperty("postETEngagementClient"));
		map.put("accessManagers", accessUserId);
		map.put("methodology", methodology);
		ETUser_Pojo pojo=new ETUser_Pojo();
		String createEngagement=pojo.createEngagement(map);
		Response createEngagmentRes=rolesUtils.post_URLPOJO(URL, AuthorizationKey, EngagementURI, createEngagement);
		createEngagmentRes.prettyPrint();
		
		Assert.assertEquals(createEngagmentRes.getStatusCode(),200);
		
	}
	
	@Test(groups = "IntegrationTests")
	public void ETEngagement_DeleteAnNewEngagement_Status204() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String engagementID = engagementId.get(0);
		String EngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+engagementID;
		Response deleteEngagmentRes=rolesUtils.delete(URL, AuthorizationKey, EngagementURI);
		Assert.assertEquals(deleteEngagmentRes.getStatusCode(),204);
	}
	
}
