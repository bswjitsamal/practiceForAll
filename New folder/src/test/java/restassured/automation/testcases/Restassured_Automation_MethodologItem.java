package restassured.automation.testcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.MethodologyItem_Pojo;

import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_MethodologItem  {

	String URL;
	String AuthorizationKey;
	List<String> listRevisionId;
	List<String> listOrdId;
	List<String> methodologyId;

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 3);
	}

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_getAllMethodologyItemsForARevision_status404() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));
		String id = revId.substring(1, revId.length() - 1);
		String subId = id.substring(8, 12);

		String patchId = "/api/methodologyItem/revision/";

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		
		Assert.assertEquals(getEngagementTypeRes.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getEngagementTypeRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);
	}

	
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_GetAllMethodologyItemsForARevision_status400() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(1));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(3));
		System.out.println("revision id----->" + revId);
		String id = revId.substring(1, revId.length() - 1);
		System.out.println("substring------>" + id);
		System.out.println("Length----->" + id.length());
		String subId = id.substring(0, 19);

		/*
		 * String patchId = "/api/methodologyItem/revision/" + subId +
		 * getRandomAlphaNum() + allUtils.getRandomNumber(1, 20);
		 */
		String patchId = "/api/methodologyItem/revision/" + subId + getRandomAlphaNum();
		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		Assert.assertEquals(getEngagementTypeRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getEngagementTypeRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_GetAllMethodologyItemsForARevision_status200() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getEngagementTypeRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PostCreateANewMethodologyItemWithinTheMethodologyTree_status200()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();
		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(postMethodologyItem);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PostCreateANewMethodologyItemWithinTheMethodologyTree_status400()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		//mi.setTitle(post.getProperty("postMethodologyItemBadTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();
		Assert.assertEquals(postMethodologyItem.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(postMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PostCreateANewMethodologyItemWithinTheMethodologyTree_status404()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();
		// fetching Org Id

		Response OrganizationsDetails = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator2 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator2.get("id");
		OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();

		/**
		 * FETCHING REVISION ID
		 */
		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");
		String revId = String.valueOf(listRevisionI1.get(0));

		/**
		 * FETCHING METHODOLOGY ID
		 */
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		System.out.println("Methodology id------->" + methodologyId);
		String mId = String.valueOf(methodologyId.get(1));
		System.out.println(mId);

		String patchId = "/api/methodologyItem/revisionss/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemBadTitle"));
		mi.setParentId(mId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();
		Assert.assertEquals(postMethodologyItem.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(postMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PutMoveATreeItemToAFolderAtSpecifiedIndex_status204()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();
		String title = post.getProperty("postMethodologyTitle") + getRandomAlphaNum()
				+ getMethodology.getRandomNumber(1, 20);

		mi.setTitle(title);
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);
		JsonPath jsonEvaluator=postMethodologyItem.jsonPath();
		String revision=jsonEvaluator.get("revisionId");
		String methodItemId=jsonEvaluator.get("methodologyItemId");

		String patchId1 = "/api/methodologyItem/revision/" +revision + "/move/"+ methodItemId;

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.put_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PutMoveATreeItemToAFolderAtSpecifiedIndex_status400()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(10)));

		String revId = String.valueOf(listRevisionI1.get(9));
		System.out.println("Revision id-------->" + revId);

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));
		mi.setTitle(post.getProperty("postMethodologyItemTitle"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		// postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);
		JsonPath jsonEvaluator=postMethodologyItem.jsonPath();
		String revision=jsonEvaluator.get("revisionId");
		String methodItemId=jsonEvaluator.get("methodologyItemId");

		String patchId1 = "/api/methodologyItem/revision/" +revision + "/move/"+methodItemId ;

		mi.setTitle(post.getProperty("postMethodologyItemBadTitle"));
		mi.setParentId(post.getProperty("putMethodologyItemParentId")+getRandomAlphaNum()+MethodologyItem.getRandomNumber(1,5));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));

		Response putMethodologyItem = MethodologyItem.put_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		// putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);

		

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PutMoveATreeItemToAFolderAtSpecifiedIndex_status409()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();
		// String
		// title=post.getProperty("postMethodologyTitle")+getRandomAlphaNum()+getMethodology.getRandomNumber(1,
		// 20);

		mi.setTitle("postMethodologyItemTitle");
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		// Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);
		
		JsonPath jsonEvaluator=postMethodologyItem.jsonPath();
		String revision=jsonEvaluator.get("revisionId");
		String methodItemId=jsonEvaluator.get("methodologyItemId");

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/move/"+ methodItemId;

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.put_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PatchUpdateAMethodologyItemForTheRevision_status200()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle") +getMethodology.getRandomNumber(1, 20));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));
		

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();
		

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		JsonPath jsonEvaluvator2=postMethodologyItem.jsonPath();
		String revision=jsonEvaluvator2.get("revisionId");
		String methodItemId=jsonEvaluvator2.get("methodologyItemId");
		
		System.out.println("Method id------>"+methodItemId);
		System.out.println("Revision id------>"+revision);
		String patchId1 = "/api/methodologyItem/revision/" + revision + "/item/"
				+ methodItemId;

		mi.setTitle(post.getProperty("putMethodologyItemTitle") + getRandomAlphaNum());
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PatchUpdateAMethodologyItemForTheRevision_status400()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));
		mi.setTitle(post.getProperty("postMethodologyItemTitle"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		JsonPath jsonEvaluvator2=postMethodologyItem.jsonPath();
		String revision=jsonEvaluvator2.get("revisionId");
		String methodItemId=jsonEvaluvator2.get("methodologyItemId");
		
		System.out.println("Method id------>"+methodItemId);
		System.out.println("Revision id------>"+revision);
		String patchId1 = "/api/methodologyItem/revision/" + revision + "/item/"
				+ methodItemId;


		mi.setTitle(post.getProperty("putMethodologyItemBadTitle"));
		// mi.setParentId(post.getProperty("putMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PatchUpdateAMethodologyItemForTheRevision_status404()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));
		mi.setTitle(post.getProperty("postMethodologyItemTitle"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		String patchId1 = "/api/methodologyItem/revisionss/" + revId.substring(1, revId.length() - 1) + "/item/"
				+ methodologyItemId.substring(1, revId.length() - 1);

		mi.setTitle(post.getProperty("putMethodologyItemBadTitle"));
		mi.setParentId(post.getProperty("putMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PostInitialiseAWorkProgram_status204() throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator=postMethodologyItem.jsonPath();
		String revision=jsonEvaluator.get("revisionId");
		String methodItemId=jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/"	+ methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PostInitialiseAWorkProgram_status400() throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listMethodologyId = jsonPathEvaluator1.get();

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));
		mi.setTitle(post.getProperty("postMethodologyItemTitle"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		// Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);
		
		JsonPath jsonEvaluator=postMethodologyItem.jsonPath();
		String revision=jsonEvaluator.get("revisionId");
		String methodItemId=jsonEvaluator.get("methodologyItemId");

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/"	+methodItemId + "/initialize";

		MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();
		mi1.setRuleContextType(post.getProperty("postRuleContextType"));
		mi1.setRuleContextSource(post.getProperty("postRuleContextSource"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi1);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_PostInitialiseAWorkProgram_status404() throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));
		mi.setTitle(post.getProperty("postMethodologyItemTitle"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/workProgram/"
				+ methodologyItemId.substring(1, methodologyItemId.length() - 1);

		mi.setTitle(post.getProperty("putMethodologyItemBadTitle"));
		mi.setParentId(post.getProperty("putMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_DeleteMarkAnItemAsDeleted_status200() throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));
		mi.setTitle(post.getProperty("postMethodologyItemTitle"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);
		JsonPath jsonEvaluator=postMethodologyItem.jsonPath();
		String revision=jsonEvaluator.get("revisionId");
		String methodItemId=jsonEvaluator.get("methodologyItemId");
		

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/item/"+methodItemId ;

		Response putMethodologyItem = MethodologyItem.delete(URL, AuthorizationKey, patchId1);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_DeleteMarkAnItemAsDeleted_status400() throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));
		mi.setTitle(post.getProperty("postMethodologyItemTitle"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);
		
		JsonPath jsonEvaluator=postMethodologyItem.jsonPath();
		String revision=jsonEvaluator.get("revisionId");
		String methodItemId=jsonEvaluator.get("methodologyItemId");

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/item/"+ methodItemId;

		MethodologyItem.delete(URL, AuthorizationKey, patchId1);
		Response putMethodologyItem = MethodologyItem.delete(URL, AuthorizationKey, patchId1);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


	}
	
	@Test(groups={"EndToEnd"})
	public void MethodologyItem_EndToEnd_Scenario() throws IOException{
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		/**
		 * FETCHING THE ORGANISATION ID
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		//OrganizationsDetails.prettyPrint();

		/**
		 * FETCHING THE PARTICULAR REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
	//getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		//getEngagementTypeRes.prettyPrint();

		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);
		
		/**
		 * PERFORMING THE POST OPERATION
		 */
		
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post1 = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post1.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post1.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post1.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post1.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		postMethodologyItem.prettyPrint();

		JsonPath jsonEvaluator=postMethodologyItem.jsonPath();
		String revision=jsonEvaluator.get("revisionId");
		String methodItemId=jsonEvaluator.get("methodologyItemId");
		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		
		
		/**
		 * PERFORMING THE PUT OPERATION
		 */
		
		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/item/"+ methodItemId;

		mi.setTitle(post1.getProperty("putMethodologyItemTitle") + MethodologyItem.getRandomNumber(1, 20));
		mi.setParentId(post1.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post1.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post1.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem.prettyPrint();

		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		
		JsonPath jsonEvaluator1=putMethodologyItem.jsonPath();
		String revision1=jsonEvaluator.get("revisionId");
		String methodItemId1=jsonEvaluator.get("methodologyItemId");

		/**
		 * PERFORMING DELETE OPERATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revision1 + "/item/"+ methodItemId1;

		Response deleteMethodologyItem = MethodologyItem.delete(URL, AuthorizationKey, patchId3);
		deleteMethodologyItem.prettyPrint();
		Assert.assertEquals(deleteMethodologyItem.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putMethodologyItem.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(putMethodologyItem);


		
		
	}
	
}
