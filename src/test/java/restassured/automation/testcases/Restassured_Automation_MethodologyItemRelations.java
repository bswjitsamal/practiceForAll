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
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.MethodologyItem_Pojo;
import restassured.automation.Pojo.RelationPojo;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_MethodologyItemRelations {

	String URL;
	String AuthorizationKey;
	List<String> listOrdId;
	String parntId;
	String revId;

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
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		Awaitility.reset();
        Awaitility.setDefaultPollDelay(999, MILLISECONDS);
        Awaitility.setDefaultPollInterval(99, SECONDS);
        Awaitility.setDefaultTimeout(99, SECONDS);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_PostCreateANewMethodologyRelation_status200() throws IOException {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		String parntId = String.valueOf(parentId.get(2));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String rev = jsonEvaluator.get("revisionId");
		String mId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + mId);
		/**
		 * CREATING THE WP
		 */
		String PatchUri="/api/methodologyItem/revision/" + rev + "/item/";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
	    map.put("parentId", mId);
		map.put("index", post.getProperty("postMethodologyItemIndex"));
		map.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createWp = new User_Pojo();
		String createWpData = createWp.PhaseCreate(map);

		Response postCreateWp = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createWpData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator21 = postCreateWp.jsonPath();
		String revision = jsonEvaluator21.get("revisionId");
		String methodItemId = jsonEvaluator21.get("methodologyItemId");

		String methodologyItemId1 = postCreateWp.asString();
		System.out.println("---->" + methodItemId);

		// Performing PATCH operation

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId;

		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data1.put("tailoring", post.getProperty("patchTailoring"));
		data1.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase = new User_Pojo();
		String patchPhaseData = patchPhase.PhaseCreatePatch(data1);

		Response patchCreatePhase = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId1, patchPhaseData);
		patchCreatePhase.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemTitle"));
		data3.put("parentId", methodItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", parntId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("patchWorkProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();

		String patchId5 = "/api/methodologyItem/revision/" + revision + "/itemSelectQuestion/" + methodItemId1
				+ "/option";

		User_Pojo createMethodologyPojo = new User_Pojo();
		String createMethodologyData = createMethodologyPojo.createOption();
		System.out.println("--->" + createMethodologyData);

		Restassured_Automation_Utils CreateProcedure = new Restassured_Automation_Utils();
		Response postCreateProcedure = CreateProcedure.put_URL(URL, AuthorizationKey, patchId5, createMethodologyData);
		postCreateProcedure.prettyPrint();

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 * 
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
		data6.put("parentId", parntId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postCreatePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId6, createPhaseData1);
		postCreatePhase1.prettyPrint();

		JsonPath jsonEvaluator2 = postCreatePhase1.jsonPath();
		String revision1 = jsonEvaluator2.get("revisionId");
		String methodItemId2 = jsonEvaluator2.get("methodologyItemId");

		String methodologyItemId11 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId8 = "/api/methodologyItem/revision/" + revision1 + "/workProgram/" + methodItemId2
				+ "/initialize";

		Map<String, String> data8 = new HashMap<String, String>();
		data8.put("ruleContextType", post.getProperty("postRuleContextType1"));

		User_Pojo postPhase1 = new User_Pojo();
		String initializePhaseData1 = postPhase1.initializeWorkProgramType(data8);

		Response initializePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId8, initializePhaseData1);
		initializePhase1.prettyPrint();

		/**
		 * 
		 * CREATE A PROCEDURE-1 UNDER THE SAME WORK PROGRAM
		 * 
		 */

		String patchId9 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data9 = new HashMap<String, String>();
		data9.put("title", post.getProperty("postMethodologyItemTitle"));
		data9.put("parentId", methodItemId2);
		data9.put("index", post.getProperty("postMethodologyItemIndex"));
		data9.put("itemType", post.getProperty("postProcedureItemType"));
		data9.put("workProgramId", parntId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId9, scndReplacement1);
		postProcedureRes1.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator3 = postProcedureRes1.jsonPath();
		String methodItemId3 = jsonEvaluator3.get("methodologyItemId");

		String patchId10 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId3;

		Map<String, String> data10 = new HashMap<String, String>();
		data10.put("workProgramItemType", post.getProperty("patchWorkProgramItemType1"));

		User_Pojo patchProcedure1 = new User_Pojo();
		String patchProcedureData1 = patchProcedure1.procedureTypeAdd(data10);

		Response patchCreateProcedure1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId10,
				patchProcedureData1.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure1.prettyPrint();

		/**
		 * 
		 * PERFORMING POST ON RELATION
		 * 
		 */

		String patchId11 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/relation";

		Map<String, String> data11 = new HashMap<String, String>();
		data11.put("methodologyItemId", methodItemId3);

		User_Pojo postRelation = new User_Pojo();
		String postRelationData = postRelation.newRelationAdd(data11);

		Response postNewRelation = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, postRelationData);
		postNewRelation.prettyPrint();
		Assert.assertEquals(postNewRelation.getStatusCode(), 200);
		/**
		 * Extent Report generation
		 */
		ExtentTestManager.statusLogMessage(postNewRelation.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postNewRelation.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(postNewRelation);

	}
	

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_PostCreateANewMethodologyRelation_status400()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

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
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

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

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		RelationPojo po = new RelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(relationResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, relationResponse.asString());
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
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/relation";
		Response getRelationRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getRelationRes.prettyPrint();
		Assert.assertEquals(getRelationRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getRelationRes.asString());
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
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/relation";
		Response getRelationRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getRelationRes.prettyPrint();
		Assert.assertEquals(getRelationRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getRelationRes.asString());
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
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		String parntId = String.valueOf(parentId.get(2));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String rev = jsonEvaluator.get("revisionId");
		String mId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + mId);
		/**
		 * CREATING THE WP
		 */
		String PatchUri="/api/methodologyItem/revision/" + rev + "/item/";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
	    map.put("parentId", mId);
		map.put("index", post.getProperty("postMethodologyItemIndex"));
		map.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createWp = new User_Pojo();
		String createWpData = createWp.PhaseCreate(map);

		Response postCreateWp = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createWpData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator21 = postCreateWp.jsonPath();
		String revision = jsonEvaluator21.get("revisionId");
		String methodItemId = jsonEvaluator21.get("methodologyItemId");

		String methodologyItemId1 = postCreateWp.asString();
		System.out.println("---->" + methodItemId);

		// Performing PATCH operation

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId;

		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data1.put("tailoring", post.getProperty("patchTailoring"));
		data1.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase = new User_Pojo();
		String patchPhaseData = patchPhase.PhaseCreatePatch(data1);

		Response patchCreatePhase = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId1, patchPhaseData);
		patchCreatePhase.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemTitle"));
		data3.put("parentId", methodItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", parntId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("patchWorkProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();

		String patchId5 = "/api/methodologyItem/revision/" + revision + "/itemSelectQuestion/" + methodItemId1
				+ "/option";

		User_Pojo createMethodologyPojo = new User_Pojo();
		String createMethodologyData = createMethodologyPojo.createOption();
		System.out.println("--->" + createMethodologyData);

		Restassured_Automation_Utils CreateProcedure = new Restassured_Automation_Utils();
		Response postCreateProcedure = CreateProcedure.put_URL(URL, AuthorizationKey, patchId5, createMethodologyData);
		postCreateProcedure.prettyPrint();

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 * 
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
		data6.put("parentId", parntId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postCreatePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId6, createPhaseData1);
		postCreatePhase1.prettyPrint();

		JsonPath jsonEvaluator2 = postCreatePhase1.jsonPath();
		String revision1 = jsonEvaluator2.get("revisionId");
		String methodItemId2 = jsonEvaluator2.get("methodologyItemId");

		String methodologyItemId11 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId8 = "/api/methodologyItem/revision/" + revision1 + "/workProgram/" + methodItemId2
				+ "/initialize";

		Map<String, String> data8 = new HashMap<String, String>();
		data8.put("ruleContextType", post.getProperty("postRuleContextType1"));

		User_Pojo postPhase1 = new User_Pojo();
		String initializePhaseData1 = postPhase1.initializeWorkProgramType(data8);

		Response initializePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId8, initializePhaseData1);
		initializePhase1.prettyPrint();

		/**
		 * 
		 * CREATE A PROCEDURE-1 UNDER THE SAME WORK PROGRAM
		 * 
		 */

		String patchId9 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data9 = new HashMap<String, String>();
		data9.put("title", post.getProperty("postMethodologyItemTitle"));
		data9.put("parentId", methodItemId2);
		data9.put("index", post.getProperty("postMethodologyItemIndex"));
		data9.put("itemType", post.getProperty("postProcedureItemType"));
		data9.put("workProgramId", parntId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId9, scndReplacement1);
		postProcedureRes1.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator3 = postProcedureRes1.jsonPath();
		String methodItemId3 = jsonEvaluator3.get("methodologyItemId");

		String patchId10 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId3;

		Map<String, String> data10 = new HashMap<String, String>();
		data10.put("workProgramItemType", post.getProperty("patchWorkProgramItemType1"));

		User_Pojo patchProcedure1 = new User_Pojo();
		String patchProcedureData1 = patchProcedure1.procedureTypeAdd(data10);

		Response patchCreateProcedure1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId10,
				patchProcedureData1.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure1.prettyPrint();

		/**
		 * 
		 * PERFORMING POST ON RELATION
		 * 
		 */

		String patchId11 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/relation";

		Map<String, String> data11 = new HashMap<String, String>();
		data11.put("methodologyItemId", methodItemId3);

		User_Pojo postRelation = new User_Pojo();
		String postRelationData = postRelation.newRelationAdd(data11);

		Response postNewRelation = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, postRelationData);
		postNewRelation.prettyPrint();
		Assert.assertEquals(postNewRelation.getStatusCode(), 200);


		// Retriving relation id
		JsonPath jp1 = postNewRelation.jsonPath();
		String relationId = jp1.get("relationId");

		String patchId12 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation/"
				+ relationId;

		Map<String, String> data12 = new HashMap<String, String>();
		data12.put("linkedMethodologyItemId", methodItemId);
		data12.put("relationshipType", post.getProperty("patchRelationshipType"));

		User_Pojo patchRelation = new User_Pojo();
		String patchRelationData = patchRelation.patchRelation(data12);

		Response patchNewRelation = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId12, patchRelationData);
		patchNewRelation.prettyPrint();
		Assert.assertEquals(patchNewRelation.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchNewRelation.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchNewRelation.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(patchNewRelation);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_UpdateAMethodologyRelation_status400()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		String parntId = String.valueOf(parentId.get(2));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String rev = jsonEvaluator.get("revisionId");
		String mId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + mId);
		/**
		 * CREATING THE WP
		 */
		String PatchUri="/api/methodologyItem/revision/" + rev + "/item/";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
	    map.put("parentId", mId);
		map.put("index", post.getProperty("postMethodologyItemIndex"));
		map.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createWp = new User_Pojo();
		String createWpData = createWp.PhaseCreate(map);

		Response postCreateWp = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createWpData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator21 = postCreateWp.jsonPath();
		String revision = jsonEvaluator21.get("revisionId");
		String methodItemId = jsonEvaluator21.get("methodologyItemId");

		String methodologyItemId1 = postCreateWp.asString();
		System.out.println("---->" + methodItemId);

		// Performing PATCH operation

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId;

		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data1.put("tailoring", post.getProperty("patchTailoring"));
		data1.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase = new User_Pojo();
		String patchPhaseData = patchPhase.PhaseCreatePatch(data1);

		Response patchCreatePhase = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId1, patchPhaseData);
		patchCreatePhase.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemTitle"));
		data3.put("parentId", methodItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", parntId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("patchWorkProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();

		String patchId5 = "/api/methodologyItem/revision/" + revision + "/itemSelectQuestion/" + methodItemId1
				+ "/option";

		User_Pojo createMethodologyPojo = new User_Pojo();
		String createMethodologyData = createMethodologyPojo.createOption();
		System.out.println("--->" + createMethodologyData);

		Restassured_Automation_Utils CreateProcedure = new Restassured_Automation_Utils();
		Response postCreateProcedure = CreateProcedure.put_URL(URL, AuthorizationKey, patchId5, createMethodologyData);
		postCreateProcedure.prettyPrint();

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 * 
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
		data6.put("parentId", parntId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postCreatePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId6, createPhaseData1);
		postCreatePhase1.prettyPrint();

		JsonPath jsonEvaluator2 = postCreatePhase1.jsonPath();
		String revision1 = jsonEvaluator2.get("revisionId");
		String methodItemId2 = jsonEvaluator2.get("methodologyItemId");

		String methodologyItemId11 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId8 = "/api/methodologyItem/revision/" + revision1 + "/workProgram/" + methodItemId2
				+ "/initialize";

		Map<String, String> data8 = new HashMap<String, String>();
		data8.put("ruleContextType", post.getProperty("postRuleContextType1"));

		User_Pojo postPhase1 = new User_Pojo();
		String initializePhaseData1 = postPhase1.initializeWorkProgramType(data8);

		Response initializePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId8, initializePhaseData1);
		initializePhase1.prettyPrint();

		/**
		 * 
		 * CREATE A PROCEDURE-1 UNDER THE SAME WORK PROGRAM
		 * 
		 */

		String patchId9 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data9 = new HashMap<String, String>();
		data9.put("title", post.getProperty("postMethodologyItemTitle"));
		data9.put("parentId", methodItemId2);
		data9.put("index", post.getProperty("postMethodologyItemIndex"));
		data9.put("itemType", post.getProperty("postProcedureItemType"));
		data9.put("workProgramId", parntId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId9, scndReplacement1);
		postProcedureRes1.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator3 = postProcedureRes1.jsonPath();
		String methodItemId3 = jsonEvaluator3.get("methodologyItemId");

		String patchId10 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId3;

		Map<String, String> data10 = new HashMap<String, String>();
		data10.put("workProgramItemType", post.getProperty("patchWorkProgramItemType1"));

		User_Pojo patchProcedure1 = new User_Pojo();
		String patchProcedureData1 = patchProcedure1.procedureTypeAdd(data10);

		Response patchCreateProcedure1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId10,
				patchProcedureData1.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure1.prettyPrint();

		/**
		 * 
		 * PERFORMING POST ON RELATION
		 * 
		 */

		String patchId11 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/relation";

		Map<String, String> data11 = new HashMap<String, String>();
		data11.put("methodologyItemId", methodItemId3);

		User_Pojo postRelation = new User_Pojo();
		String postRelationData = postRelation.newRelationAdd(data11);

		Response postNewRelation = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, postRelationData);
		postNewRelation.prettyPrint();
		Assert.assertEquals(postNewRelation.getStatusCode(), 200);


		// Retriving relation id
		JsonPath jp1 = postNewRelation.jsonPath();
		String relationId = jp1.get("relationId");

		String patchId12 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation/"
				+ relationId;

		Map<String, String> data12 = new HashMap<String, String>();
		data12.put("linkedMethodologyItemId", post.getProperty("postWorkFlowState1"));
		data12.put("relationshipType", post.getProperty("patchRelationshipType"));

		User_Pojo patchRelation = new User_Pojo();
		String patchRelationData = patchRelation.patchRelation(data12);

		Response patchNewRelation = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId12, patchRelationData);
		patchNewRelation.prettyPrint();
		Assert.assertEquals(patchNewRelation.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchNewRelation.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchNewRelation.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(patchNewRelation);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_DeleteAnMethodologyRelation_status204()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation";

		RelationPojo po = new RelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation/"
				+ relationId;
		Response deleteRelationRes = MethodologyItem.delete(URL, AuthorizationKey, patchId4);
		deleteRelationRes.prettyPrint();
		Assert.assertEquals(deleteRelationRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRelationRes.asString());
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
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation";

		RelationPojo po = new RelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation/"
				+ relationId;
		Response deleteRelationRes = MethodologyItem.delete(URL, AuthorizationKey, patchId4);
		deleteRelationRes.prettyPrint();
		Assert.assertEquals(deleteRelationRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRelationRes.asString());
		MethodologyItem.validate_HTTPStrictTransportSecurity(deleteRelationRes);
	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_DeleteLinkedMethodologyItem_status400()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		String parntId = String.valueOf(parentId.get(2));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String rev = jsonEvaluator.get("revisionId");
		String mId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + mId);
		/**
		 * CREATING THE WP
		 */
		String PatchUri="/api/methodologyItem/revision/" + rev + "/item/";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
	    map.put("parentId", mId);
		map.put("index", post.getProperty("postMethodologyItemIndex"));
		map.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createWp = new User_Pojo();
		String createWpData = createWp.PhaseCreate(map);

		Response postCreateWp = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createWpData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator21 = postCreateWp.jsonPath();
		String revision = jsonEvaluator21.get("revisionId");
		String methodItemId = jsonEvaluator21.get("methodologyItemId");

		String methodologyItemId1 = postCreateWp.asString();
		System.out.println("---->" + methodItemId);

		// Performing PATCH operation

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId;

		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data1.put("tailoring", post.getProperty("patchTailoring"));
		data1.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase = new User_Pojo();
		String patchPhaseData = patchPhase.PhaseCreatePatch(data1);

		Response patchCreatePhase = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId1, patchPhaseData);
		patchCreatePhase.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemTitle"));
		data3.put("parentId", methodItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", parntId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("patchWorkProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();

		String patchId5 = "/api/methodologyItem/revision/" + revision + "/itemSelectQuestion/" + methodItemId1
				+ "/option";

		User_Pojo createMethodologyPojo = new User_Pojo();
		String createMethodologyData = createMethodologyPojo.createOption();
		System.out.println("--->" + createMethodologyData);

		Restassured_Automation_Utils CreateProcedure = new Restassured_Automation_Utils();
		Response postCreateProcedure = CreateProcedure.put_URL(URL, AuthorizationKey, patchId5, createMethodologyData);
		postCreateProcedure.prettyPrint();

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 * 
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
		data6.put("parentId", parntId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postCreatePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId6, createPhaseData1);
		postCreatePhase1.prettyPrint();

		JsonPath jsonEvaluator2 = postCreatePhase1.jsonPath();
		String revision1 = jsonEvaluator2.get("revisionId");
		String methodItemId2 = jsonEvaluator2.get("methodologyItemId");

		String methodologyItemId11 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId8 = "/api/methodologyItem/revision/" + revision1 + "/workProgram/" + methodItemId2
				+ "/initialize";

		Map<String, String> data8 = new HashMap<String, String>();
		data8.put("ruleContextType", post.getProperty("postRuleContextType1"));

		User_Pojo postPhase1 = new User_Pojo();
		String initializePhaseData1 = postPhase1.initializeWorkProgramType(data8);

		Response initializePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId8, initializePhaseData1);
		initializePhase1.prettyPrint();

		/**
		 * 
		 * CREATE A PROCEDURE-1 UNDER THE SAME WORK PROGRAM
		 * 
		 */

		String patchId9 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data9 = new HashMap<String, String>();
		data9.put("title", post.getProperty("postMethodologyItemTitle"));
		data9.put("parentId", methodItemId2);
		data9.put("index", post.getProperty("postMethodologyItemIndex"));
		data9.put("itemType", post.getProperty("postProcedureItemType"));
		data9.put("workProgramId", parntId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId9, scndReplacement1);
		postProcedureRes1.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator3 = postProcedureRes1.jsonPath();
		String methodItemId3 = jsonEvaluator3.get("methodologyItemId");

		String patchId10 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId3;

		Map<String, String> data10 = new HashMap<String, String>();
		data10.put("workProgramItemType", post.getProperty("patchWorkProgramItemType1"));

		User_Pojo patchProcedure1 = new User_Pojo();
		String patchProcedureData1 = patchProcedure1.procedureTypeAdd(data10);

		Response patchCreateProcedure1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId10,
				patchProcedureData1.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure1.prettyPrint();

		/**
		 * 
		 * PERFORMING POST ON RELATION
		 * 
		 */

		String patchId11 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/relation";

		Map<String, String> data11 = new HashMap<String, String>();
		data11.put("methodologyItemId", methodItemId3);

		User_Pojo postRelation = new User_Pojo();
		String postRelationData = postRelation.newRelationAdd(data11);

		Response postNewRelation = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, postRelationData);
		postNewRelation.prettyPrint();
		Assert.assertEquals(postNewRelation.getStatusCode(), 200);


		// Retriving relation id
		JsonPath jp1 = postNewRelation.jsonPath();
		String relationId = jp1.get("relationId");

		String patchId12 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation/"
				+ relationId;

		Map<String, String> data12 = new HashMap<String, String>();
		data12.put("linkedMethodologyItemId", methodItemId);
		data12.put("relationshipType", post.getProperty("patchRelationshipType"));

		User_Pojo patchRelation = new User_Pojo();
		String patchRelationData = patchRelation.patchRelation(data12);

		Response patchNewRelation = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId12, patchRelationData);
		patchNewRelation.prettyPrint();		
		
		String patchId13 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation/"
				+ relationId+"/linked";
		Response deleteRelationRes = AllUtils.delete(URL, AuthorizationKey, patchId13);
		Response deleteRelationRes1 = AllUtils.delete(URL, AuthorizationKey, patchId13);
		deleteRelationRes1.prettyPrint();
		Assert.assertEquals(deleteRelationRes1.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
     	ExtentTestManager.statusLogMessage(deleteRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRelationRes.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(deleteRelationRes);

	}


	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_DeleteLinkedMethodologyItem_status200()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		String parntId = String.valueOf(parentId.get(2));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String rev = jsonEvaluator.get("revisionId");
		String mId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + mId);
		/**
		 * CREATING THE WP
		 */
		String PatchUri="/api/methodologyItem/revision/" + rev + "/item/";
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
	    map.put("parentId", mId);
		map.put("index", post.getProperty("postMethodologyItemIndex"));
		map.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createWp = new User_Pojo();
		String createWpData = createWp.PhaseCreate(map);

		Response postCreateWp = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createWpData);
		postCreatePhase.prettyPrint();

		JsonPath jsonEvaluator21 = postCreateWp.jsonPath();
		String revision = jsonEvaluator21.get("revisionId");
		String methodItemId = jsonEvaluator21.get("methodologyItemId");

		String methodologyItemId1 = postCreateWp.asString();
		System.out.println("---->" + methodItemId);

		// Performing PATCH operation

		String patchId1 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId;

		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data1.put("tailoring", post.getProperty("patchTailoring"));
		data1.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase = new User_Pojo();
		String patchPhaseData = patchPhase.PhaseCreatePatch(data1);

		Response patchCreatePhase = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId1, patchPhaseData);
		patchCreatePhase.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemTitle"));
		data3.put("parentId", methodItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", parntId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("workProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();

		String patchId5 = "/api/methodologyItem/revision/" + revision + "/itemSelectQuestion/" + methodItemId1
				+ "/option";

		User_Pojo createMethodologyPojo = new User_Pojo();
		String createMethodologyData = createMethodologyPojo.createOption();
		System.out.println("--->" + createMethodologyData);

		Restassured_Automation_Utils CreateProcedure = new Restassured_Automation_Utils();
		Response postCreateProcedure = CreateProcedure.put_URL(URL, AuthorizationKey, patchId5, createMethodologyData);
		postCreateProcedure.prettyPrint();

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 * 
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
		data6.put("parentId", parntId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postCreatePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId6, createPhaseData1);
		postCreatePhase1.prettyPrint();

		JsonPath jsonEvaluator2 = postCreatePhase1.jsonPath();
		String revision1 = jsonEvaluator2.get("revisionId");
		String methodItemId2 = jsonEvaluator2.get("methodologyItemId");

		String methodologyItemId11 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId8 = "/api/methodologyItem/revision/" + revision1 + "/workProgram/" + methodItemId2
				+ "/initialize";

		Map<String, String> data8 = new HashMap<String, String>();
		data8.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase1 = new User_Pojo();
		String initializePhaseData1 = postPhase1.initializeWorkProgramType(data8);

		Response initializePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId8, initializePhaseData1);
		initializePhase1.prettyPrint();

		/**
		 * 
		 * CREATE A PROCEDURE-1 UNDER THE SAME WORK PROGRAM
		 * 
		 */

		String patchId9 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data9 = new HashMap<String, String>();
		data9.put("title", post.getProperty("postMethodologyItemTitle"));
		data9.put("parentId", methodItemId2);
		data9.put("index", post.getProperty("postMethodologyItemIndex"));
		data9.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data9.put("workProgramId", parntId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId9, scndReplacement1);
		postProcedureRes1.prettyPrint();

		// Performing PATCH operation

		JsonPath jsonEvaluator3 = postProcedureRes1.jsonPath();
		String methodItemId3 = jsonEvaluator3.get("methodologyItemId");

		String patchId10 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId3;

		Map<String, String> data10 = new HashMap<String, String>();
		data10.put("workProgramItemType", post.getProperty("patchWorkProgramItemType1"));

		User_Pojo patchProcedure1 = new User_Pojo();
		String patchProcedureData1 = patchProcedure1.procedureTypeAdd(data10);

		Response patchCreateProcedure1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId10,
				patchProcedureData1.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure1.prettyPrint();

		/**
		 * 
		 * PERFORMING POST ON RELATION
		 * 
		 */

		String patchId11 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/relation";

		Map<String, String> data11 = new HashMap<String, String>();
		data11.put("methodologyItemId", methodItemId3);

		User_Pojo postRelation = new User_Pojo();
		String postRelationData = postRelation.newRelationAdd(data11);

		Response postNewRelation = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, postRelationData);
		postNewRelation.prettyPrint();
		Assert.assertEquals(postNewRelation.getStatusCode(), 200);
		
		//Retriving relation id
		JsonPath jp = postNewRelation.jsonPath();
		String relationId =  jp.get("relationId");
		
		String patchId12 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/relation/"+relationId;
		
		Map<String, String> data12 = new HashMap<String, String>();
		data12.put("linkedMethodologyItemId", methodItemId);
		data12.put("relationshipType", post.getProperty("patchRelationshipType") );
		
		User_Pojo patchRelation = new User_Pojo();
		String patchRelationData = patchRelation.patchRelation(data12);
		
		Response patchNewRelation = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId12, patchRelationData);
		patchNewRelation.prettyPrint();
		
		//Performing another patch 
		
		Map<String, String> data13 = new HashMap<String, String>();
		data13.put("relationshipType", post.getProperty("patchRelationshipType1") );
		
		String patchRelationData1 = patchRelation.anotherPatchRelation(data13);
		
		Response patchNewRelation1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId12, patchRelationData1);
		patchNewRelation1.prettyPrint();
		
		
		String patchId13 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation/"
				+ relationId+"/linked";
		
		Response deleteRelationRes = AllUtils.delete(URL, AuthorizationKey, patchId13);
		deleteRelationRes.prettyPrint();
		
		
		Assert.assertEquals(deleteRelationRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRelationRes.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(deleteRelationRes);


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
				"Organization", listOrdId.get(5));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		/**
		 * PERFORMING GET OPERATION WRT RELATION
		 */
		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation";
		Response getRelationRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId4);
		getRelationRes.prettyPrint();
		Assert.assertEquals(getRelationRes.getStatusCode(), 200);

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation";

		RelationPojo po = new RelationPojo();
		po.setMethodologyItemId(mId);

		Response relationResponse = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId3, po);
		relationResponse.prettyPrint();
		Assert.assertEquals(relationResponse.getStatusCode(), 200);
		JsonPath relationJson = relationResponse.jsonPath();
		String relationId = relationJson.get("relationId");
		/**
		 * PATCH THE METHODOLOGY RELATION
		 */
		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation/"
				+ relationId;
		po.setLinkedMethodologyItemId(methodItemId);
		Response patchRelationRes = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId5, po);
		patchRelationRes.prettyPrint();
		Assert.assertEquals(patchRelationRes.getStatusCode(), 200);
		/**
		 * PERFORMING DELETION OPERATION
		 */
		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/relation/"
				+ relationId;
		Response deleteRelationRes = MethodologyItem.delete(URL, AuthorizationKey, patchId6);
		deleteRelationRes.prettyPrint();
		Assert.assertEquals(deleteRelationRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRelationRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRelationRes.asString());
		MethodologyItem.validate_HTTPStrictTransportSecurity(deleteRelationRes);

	}

}
