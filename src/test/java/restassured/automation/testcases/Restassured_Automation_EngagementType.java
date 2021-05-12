package restassured.automation.testcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.ArrayList;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.ETUser_Pojo;
import restassured.automation.Pojo.Engagement_Type_Pojo;
import restassured.automation.Pojo.User_Pojo;

import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_EngagementType {

	String URL;
	String AuthorizationKey;
	List<String> listEngagementTypeId;
	List<String> listOrdId;
	List<String> listTitle;

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 4);
	}

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {
		read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");
		Awaitility.reset();
        Awaitility.setDefaultPollDelay(999, MILLISECONDS);
        Awaitility.setDefaultPollInterval(99, SECONDS);
        Awaitility.setDefaultTimeout(99, SECONDS);

	}

	
	@Test(groups = "IntegrationTests")
	public void EngagementType_GetListAllEngagementType() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		getEngagementTypeRes.prettyPrint();
		
		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);
		
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getEngagementTypeRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);
		
		
		

	}

	@Test(groups = "IntegrationTests")
	public void EngagementType_PostcreateANewEngagementType_status200() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") +engagementType.getRandomNumber(1, 20));
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));
		
		String URI="/api/engagementType";
		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey,URI, en);
		postEngagementType.prettyPrint();

		String idValue = postEngagementType.then().extract().path("id").toString();
		System.out.println(idValue);
		//engagementType.validate_HTTPStrictTransportSecurity(postEngagementType);
		//System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		//engagementType.validate_HTTPStrictTransportSecurity(postEngagementType);
		Assert.assertEquals(postEngagementType.statusCode(), 200);
		
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postEngagementType.asString());
		engagementType.validate_HTTPStrictTransportSecurity(postEngagementType);

	}

	@Test(groups = "IntegrationTests")
	public void EngagementType_PostcreateANewEngagementType_status400() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitleBadRequest"));
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		postEngagementType.prettyPrint();

		
		Assert.assertEquals(postEngagementType.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postEngagementType.asString());
		engagementType.validate_HTTPStrictTransportSecurity(postEngagementType);


	}

	@Test(groups = "IntegrationTests")
	public void EngagementType_PostcreateANewEngagementType_status409() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle"));
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		postEngagementType.prettyPrint();
		
		Assert.assertEquals(postEngagementType.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postEngagementType.asString());
		engagementType.validate_HTTPStrictTransportSecurity(postEngagementType);


	}

	//@Test(groups = "IntegrationTests")
	public void EngagementType_PatchUpdateAnEngagementType_status200() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ENGAGEMENT ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		 getEngagementTypeRes.prettyPrint();

		JsonPath jsonEvaluator1 = getEngagementTypeRes.jsonPath();
		listEngagementTypeId = jsonEvaluator1.get("id");
		listTitle = jsonEvaluator1.get("title");
		listOrdId = jsonEvaluator1.get("Organization");
		// System.out.println("Engagement id"+listEngagementTypeId.get(10));

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + allUtils.getRandomNumber(1, 7));
		// en.setOrganization(listOrdId.get(7));

		String patchId = "/api/engagementType/" + listEngagementTypeId.get(2);

		Response patchEngagementType = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId, en);
		patchEngagementType.prettyPrint();
		System.out.println("url passed");
		
		Assert.assertEquals(patchEngagementType.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementType.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchEngagementType);


	}

	@Test(groups = "IntegrationTests")
	public void EngagementType_PatchUpdateAnEngagementType_status400() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ENGAGEMENT ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		// getEngagementTypeRes.prettyPrint();

		JsonPath jsonEvaluator1 = getEngagementTypeRes.jsonPath();
		listEngagementTypeId = jsonEvaluator1.get("id");
		listTitle = jsonEvaluator1.get("title");
		listOrdId = jsonEvaluator1.get("Organization");
		System.out.println("Engagement id" + listEngagementTypeId.get(2));

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(" ");
		// en.setOrganization(listOrdId.get(7));

		String patchId = "/api/engagementType/" + listEngagementTypeId.get(2);

		Response patchEngagementType = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId, en);
		patchEngagementType.prettyPrint();
		// System.out.println("url passed");
		
		Assert.assertEquals(patchEngagementType.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementType.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchEngagementType);

	}

	@Test(groups = "IntegrationTests")
	public void EngagementType_PatchUpdateAnEngagementType_status409() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		String orgId=listOrdId.get(7);
		
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ENGAGEMENT ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		getEngagementTypeRes.prettyPrint();

		JsonPath jsonEvaluator1 = getEngagementTypeRes.jsonPath();
		listEngagementTypeId = jsonEvaluator1.get("id");
		listTitle = jsonEvaluator1.get("title");
		listOrdId = jsonEvaluator1.get("Organization");
		System.out.println("Engagement id---->" + listEngagementTypeId.get(2));
		String title = listTitle.get(1);
		System.out.println("Title------>" + title);

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(title);
		en.setOrganization(orgId);

		String patchId = "/api/engagementType/" + listEngagementTypeId.get(2);

		Response patchEngagementType = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId, en);
		patchEngagementType.prettyPrint();
		
		Assert.assertEquals(patchEngagementType.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementType.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchEngagementType);

	}

	//@Test(groups = "IntegrationTests")
	public void EngagementType_DeleteAnEngagementType_status204() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * Creating the new Engagement id
		 */
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum()
				+ engagementType.getRandomNumber(1, 20));
		en.setOrganization(listOrdId.get(7));

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		postEngagementType.prettyPrint();
		
		JsonPath jsonEvaluator=postEngagementType.jsonPath();
		String EngagementTypeId=jsonEvaluator.get("id");

		/**
		 * GETTING THE ENGAGEMENT ID
		 */

		/*Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		getEngagementTypeRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getEngagementTypeRes.jsonPath();
		listEngagementTypeId = jsonPathEvaluator1.get("id");

		String engagementTypeId = listEngagementTypeId.get(listEngagementTypeId.size() - 2);
		System.out.println(engagementTypeId);*/

		String patchId = "/api/engagementType/" + EngagementTypeId;

		Response deleteEngagementType = allUtils.delete(URL, AuthorizationKey, patchId);
		deleteEngagementType.prettyPrint();
		
		Assert.assertEquals(deleteEngagementType.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,deleteEngagementType.asString());
		allUtils.validate_HTTPStrictTransportSecurity(deleteEngagementType);

		

	}

	//@Test(groups = "IntegrationTests")
	public void EngagementType_DeleteAnEngagementType_status400() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ENGAGEMENT ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		getEngagementTypeRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getEngagementTypeRes.jsonPath();
		listEngagementTypeId = jsonPathEvaluator1.get("id");
		int size=listEngagementTypeId.size();

		String engagementTypeId = listEngagementTypeId.get(size-1);
		System.out.println("engagementTypeId----->" + engagementTypeId);

		String patchId = "/api/engagementType/" + engagementTypeId;

		allUtils.delete(URL, AuthorizationKey, patchId);
		Response deleteEngagementType = allUtils.delete(URL, AuthorizationKey, patchId);
		deleteEngagementType.prettyPrint();
		
		Assert.assertEquals(deleteEngagementType.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,deleteEngagementType.asString());
		allUtils.validate_HTTPStrictTransportSecurity(deleteEngagementType);


	}
	//@Test(groups = "IntegrationTests")
	public void EngagementType_ListAllEngagementTypesForTheStore(){
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		Response EngagementStore=allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/engagementType/store");
		EngagementStore.prettyPrint();
		Assert.assertEquals(EngagementStore.getStatusCode(),200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(EngagementStore.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,EngagementStore.asString());
		allUtils.validate_HTTPStrictTransportSecurity(EngagementStore);
	}
	
	

	//@Test(groups = { "EndToEnd" })
	public void EngagementType_EndToEnd_Scenario() throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * PERFORMING THE GET OPERATION
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		getEngagementTypeRes.prettyPrint();
		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);

		/**
		 * *PERFORMING THE POST OPERATION
		 */
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(
				post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum() + allUtils.getRandomNumber(1, 20));
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Response postEngagementType = allUtils.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		postEngagementType.prettyPrint();

		String idValue = postEngagementType.then().extract().path("id").toString();
		System.out.println("VALUE----->" + idValue);

		Assert.assertEquals(postEngagementType.statusCode(), 200);

		/**
		 * Updating new created records
		 */

		String patchId = "/api/engagementType/" + idValue;
		en.setTitle(
				post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum() + allUtils.getRandomNumber(1, 20));
		en.setOrganization(post.getProperty("postEngagementTypeorganization"));

		Response patchEngagementType = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId, en);
		patchEngagementType.prettyPrint();

		Assert.assertEquals(patchEngagementType.statusCode(), 200);

		/**
		 * Deleting the same record
		 */

		// String patchId = "/api/engagementType/" + idValue;

		Response deleteEngagementType = allUtils.delete(URL, AuthorizationKey, patchId);
		deleteEngagementType.prettyPrint();
		Assert.assertEquals(deleteEngagementType.statusCode(), 204);
		
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,deleteEngagementType.asString());
		allUtils.validate_HTTPStrictTransportSecurity(deleteEngagementType);


	}
	
	@Test(groups = "IntegrationTests")
	public void EngagementType_InheritAnEngagementType409() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		getEngagementTypeRes.prettyPrint();
		
		// Retrieving the inheritedFrom 
		
		JsonPath jp = getEngagementTypeRes.jsonPath();
		List<String> inheritedFrom = jp.getList("inheritedFrom");
		
		System.out.println(inheritedFrom);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("storeEngagementType", inheritedFrom.get(1));
		map.put("organization", listOrdId.get(7));
		
		User_Pojo po = new User_Pojo();
		String userOobj = po.engagementTypeInherit(map);
		
		String URI="/api/engagementType/inherit/";
		Response postEngagementTypeInheriteRes = allUtils.post_URLPOJO(URL, AuthorizationKey,URI, userOobj);
		postEngagementTypeInheriteRes.prettyPrint();
		
		/*Response postEngagementTypeInheriteRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType/inherit/",
				"Organization", inheritedFrom.get(5));
		postEngagementTypeInheriteRes.prettyPrint();
		*/
		Assert.assertEquals(postEngagementTypeInheriteRes.statusCode(), 409);
		
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getEngagementTypeRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);	

	}
	
	@Test(groups = "IntegrationTests")
	public void EngagementType_InheritAnEngagementType400() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		getEngagementTypeRes.prettyPrint();
		
		// Retrieving the inheritedFrom 
		
		JsonPath jp = getEngagementTypeRes.jsonPath();
		List<String> inheritedFrom = jp.getList("inheritedFrom");
		
		System.out.println(inheritedFrom);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("storeEngagementType", inheritedFrom.get(1).substring(1,6));
		map.put("organization", listOrdId.get(7).substring(1,6));
		
		User_Pojo po = new User_Pojo();
		String userOobj = po.engagementTypeInherit(map);
		
		String URI="/api/engagementType/inherit/";
		Response postEngagementTypeInheriteRes = allUtils.post_URLPOJO(URL, AuthorizationKey,URI, userOobj);
		postEngagementTypeInheriteRes.prettyPrint();
		
		Assert.assertEquals(postEngagementTypeInheriteRes.statusCode(), 400);
		
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getEngagementTypeRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);	

	}

}
