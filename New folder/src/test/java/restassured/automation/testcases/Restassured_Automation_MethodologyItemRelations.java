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
import restassured.automation.Pojo.MethodologyItemRelationPojo;
import restassured.automation.Pojo.MethodologyItem_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_MethodologyItemRelations {

	String URL;
	String AuthorizationKey;
	List<String> listOrdId;

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
	public void MethodologyItemRelation_PostCreateANewMethodologyRelation_status200()
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

		String revId = String.valueOf(listRevisionI1.get(1));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator = postMethodologyItem.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Create an procedure
		 */
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		mi.setTitle(post.getProperty("postWorkProgramProcedureTitle"));
		mi.setParentId(methodItemId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postWorkProgramItemItemType"));

		Response putMethodologyItem1 = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem1.prettyPrint();
		Assert.assertEquals(putMethodologyItem1.statusCode(), 200);
		JsonPath procedureJson = putMethodologyItem1.jsonPath();
		String mId = procedureJson.get("methodologyItemId");
		/**
		 * CREATING THE RELATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";

		MethodologyItemRelationPojo po = new MethodologyItemRelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(relationResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, relationResponse.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(relationResponse);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_PostCreateANewMethodologyRelation_status400()
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

		String revId = String.valueOf(listRevisionI1.get(1));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator = postMethodologyItem.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Create an procedure
		 */
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		mi.setTitle(post.getProperty("postWorkProgramProcedureTitle"));
		mi.setParentId(methodItemId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postWorkProgramItemItemType"));

		Response putMethodologyItem1 = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem1.prettyPrint();
		Assert.assertEquals(putMethodologyItem1.statusCode(), 200);
		JsonPath procedureJson = putMethodologyItem1.jsonPath();
		String mId = procedureJson.get("methodologyItemId");
		/**
		 * CREATING THE RELATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		MethodologyItemRelationPojo po = new MethodologyItemRelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(relationResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, relationResponse.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(relationResponse);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_ListOfAllMethodologyRelation_status200()
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

		String revId = String.valueOf(listRevisionI1.get(1));
		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";
		Response getRelationRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getRelationRes.prettyPrint();
		Assert.assertEquals(getRelationRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getRelationRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		allUtils.validate_HTTPStrictTransportSecurity(getRelationRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_ListOfAllMethodologyRelation_status400()
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

		String revId = String.valueOf(listRevisionI1.get(1));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relations";
		Response getRelationRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getRelationRes.prettyPrint();
		Assert.assertEquals(getRelationRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getRelationRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		allUtils.validate_HTTPStrictTransportSecurity(getRelationRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_UpdateAMethodologyRelation_status200()
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

		String revId = String.valueOf(listRevisionI1.get(1));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator = postMethodologyItem.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Create an procedure
		 */
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		mi.setTitle(post.getProperty("postWorkProgramProcedureTitle"));
		mi.setParentId(methodItemId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postWorkProgramItemItemType"));

		Response putMethodologyItem1 = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem1.prettyPrint();
		Assert.assertEquals(putMethodologyItem1.statusCode(), 200);
		JsonPath procedureJson = putMethodologyItem1.jsonPath();
		String mId = procedureJson.get("methodologyItemId");
		/**
		 * CREATING THE RELATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";

		MethodologyItemRelationPojo po = new MethodologyItemRelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		/**
		 * PATCH THE METHODOLOGY RELATION
		 */
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId;
		po.setLinkedMethodologyItemId(methodItemId);
		Response patchRelationRes = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId4, po);
		patchRelationRes.prettyPrint();
		Assert.assertEquals(patchRelationRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchRelationRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(patchRelationRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_UpdateAMethodologyRelation_status400()
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

		String revId = String.valueOf(listRevisionI1.get(1));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator = postMethodologyItem.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Create an procedure
		 */
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		mi.setTitle(post.getProperty("postWorkProgramProcedureTitle"));
		mi.setParentId(methodItemId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postWorkProgramItemItemType"));

		Response putMethodologyItem1 = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem1.prettyPrint();
		Assert.assertEquals(putMethodologyItem1.statusCode(), 200);
		JsonPath procedureJson = putMethodologyItem1.jsonPath();
		String mId = procedureJson.get("methodologyItemId");
		/**
		 * CREATING THE RELATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";

		MethodologyItemRelationPojo po = new MethodologyItemRelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		/**
		 * PATCH THE METHODOLOGY RELATION
		 */
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId;
		po.setLinkedMethodologyItemId("Dummy text");
		Response patchRelationRes = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId4, po);
		patchRelationRes.prettyPrint();
		Assert.assertEquals(patchRelationRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchRelationRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(patchRelationRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_DeleteAnMethodologyRelation_status204()
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

		String revId = String.valueOf(listRevisionI1.get(1));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator = postMethodologyItem.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Create an procedure
		 */
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		mi.setTitle(post.getProperty("postWorkProgramProcedureTitle"));
		mi.setParentId(methodItemId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postWorkProgramItemItemType"));

		Response putMethodologyItem1 = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem1.prettyPrint();
		Assert.assertEquals(putMethodologyItem1.statusCode(), 200);
		JsonPath procedureJson = putMethodologyItem1.jsonPath();
		String mId = procedureJson.get("methodologyItemId");
		/**
		 * CREATING THE RELATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";

		MethodologyItemRelationPojo po = new MethodologyItemRelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId;
		Response deleteRelationRes = MethodologyItem.delete(URL, AuthorizationKey, patchId4);
		deleteRelationRes.prettyPrint();
		Assert.assertEquals(deleteRelationRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRelationRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(deleteRelationRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_DeleteAnMethodologyRelation_status400()
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

		String revId = String.valueOf(listRevisionI1.get(1));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator = postMethodologyItem.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Create an procedure
		 */
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		mi.setTitle(post.getProperty("postWorkProgramProcedureTitle"));
		mi.setParentId(methodItemId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postWorkProgramItemItemType"));

		Response putMethodologyItem1 = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem1.prettyPrint();
		Assert.assertEquals(putMethodologyItem1.statusCode(), 200);
		JsonPath procedureJson = putMethodologyItem1.jsonPath();
		String mId = procedureJson.get("methodologyItemId");
		/**
		 * CREATING THE RELATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";

		MethodologyItemRelationPojo po = new MethodologyItemRelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId;
		Response deleteRelationRes = MethodologyItem.delete(URL, AuthorizationKey, patchId4);
		deleteRelationRes.prettyPrint();
		Assert.assertEquals(deleteRelationRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRelationRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(deleteRelationRes);
	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_DeleteLinkedMethodologyItemRelation_status400()
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

		String revId = String.valueOf(listRevisionI1.get(1));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator = postMethodologyItem.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Create an procedure
		 */
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		mi.setTitle(post.getProperty("postWorkProgramProcedureTitle"));
		mi.setParentId(methodItemId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postWorkProgramItemItemType"));

		Response putMethodologyItem1 = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem1.prettyPrint();
		Assert.assertEquals(putMethodologyItem1.statusCode(), 200);
		JsonPath procedureJson = putMethodologyItem1.jsonPath();
		String mId = procedureJson.get("methodologyItemId");
		/**
		 * CREATING THE RELATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";

		MethodologyItemRelationPojo po = new MethodologyItemRelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		/**
		 * PATCH THE METHODOLOGY RELATION
		 */
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId;
		po.setLinkedMethodologyItemId(methodItemId);
		Response patchRelationRes = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId4, po);
		patchRelationRes.prettyPrint();
		Assert.assertEquals(patchRelationRes.getStatusCode(), 200);

		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId + "/linked";
		Response deleteRelationRes = MethodologyItem.delete(URL, AuthorizationKey, patchId5);
		deleteRelationRes.prettyPrint();
		Assert.assertEquals(deleteRelationRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRelationRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(deleteRelationRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_DeleteLinkedMethodologyItemRelation_status200()
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

		String revId = String.valueOf(listRevisionI1.get(1));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator = postMethodologyItem.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Create an procedure
		 */
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		mi.setTitle(post.getProperty("postWorkProgramProcedureTitle"));
		mi.setParentId(methodItemId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postWorkProgramItemItemType"));

		Response putMethodologyItem1 = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem1.prettyPrint();
		Assert.assertEquals(putMethodologyItem1.statusCode(), 200);
		JsonPath procedureJson = putMethodologyItem1.jsonPath();
		String mId = procedureJson.get("methodologyItemId");
		/**
		 * CREATING THE RELATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";

		MethodologyItemRelationPojo po = new MethodologyItemRelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		/**
		 * PATCH THE METHODOLOGY RELATION
		 */
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId;
		po.setLinkedMethodologyItemId(methodItemId);
		Response patchRelationRes = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId4, po);
		patchRelationRes.prettyPrint();
		Assert.assertEquals(patchRelationRes.getStatusCode(), 200);
		/*
		 * JsonPath relationJson1=patchRelationRes.jsonPath(); String
		 * linkedrelationId=relationJson.get("relationId");
		 */
		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId + "/linked";
		Response deleteRelationRes = MethodologyItem.delete(URL, AuthorizationKey, patchId5);
		deleteRelationRes.prettyPrint();
		Assert.assertEquals(deleteRelationRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRelationRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		MethodologyItem.validate_HTTPStrictTransportSecurity(deleteRelationRes);

	}

	@Test(groups = { "IntegrationTests", "EndToEnd" })
	public void MethodologyItemRelation_MethodologyItemRelation_EndToEnd_Scenario() throws IOException {
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

		String revId = String.valueOf(listRevisionI1.get(1));
		/**
		 * PERFORMING GET OPERATION WRT RELATION
		 */
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";
		Response getRelationRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId4);
		getRelationRes.prettyPrint();
		Assert.assertEquals(getRelationRes.getStatusCode(), 200);

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);
		JsonPath jsonEvaluator = postMethodologyItem.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();
		Assert.assertEquals(putMethodologyItem.statusCode(), 204);
		/**
		 * Create an procedure
		 */
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		mi.setTitle(post.getProperty("postWorkProgramProcedureTitle"));
		mi.setParentId(methodItemId);
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postWorkProgramItemItemType"));

		Response putMethodologyItem1 = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId2, mi);
		putMethodologyItem1.prettyPrint();
		Assert.assertEquals(putMethodologyItem1.statusCode(), 200);
		JsonPath procedureJson = putMethodologyItem1.jsonPath();
		String mId = procedureJson.get("methodologyItemId");
		/**
		 * PERFORMING POST OPERATION
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";

		MethodologyItemRelationPojo po = new MethodologyItemRelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		/**
		 * PATCH THE METHODOLOGY RELATION
		 */
		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId;
		po.setLinkedMethodologyItemId(methodItemId);
		Response patchRelationRes = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId5, po);
		patchRelationRes.prettyPrint();
		Assert.assertEquals(patchRelationRes.getStatusCode(), 200);
		/**
		 * PERFORMING DELETION OPERATION
		 */
		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"+ relationId;
		Response deleteRelationRes = MethodologyItem.delete(URL, AuthorizationKey, patchId6);
		deleteRelationRes.prettyPrint();
		Assert.assertEquals(deleteRelationRes.getStatusCode(), 204);
		

	}

}
