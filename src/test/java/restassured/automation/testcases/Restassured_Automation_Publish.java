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
import restassured.automation.Pojo.PublishPojo;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Publish extends read_Configuration_Propertites {

	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrdId;
	String methodologyId;
	String engagementTypeId;

	Long MAX_TIMEOUT = 3000l;

	read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
	Restassured_Automation_Utils restUtils = new Restassured_Automation_Utils();

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 2);
	}

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		// read_Configuration_Propertites configDetails = new
		// read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");

		Awaitility.reset();
		Awaitility.setDefaultPollDelay(999, MILLISECONDS);
		Awaitility.setDefaultPollInterval(99, SECONDS);
		Awaitility.setDefaultTimeout(99, SECONDS);

	}

	public void validate_HTTPStrictTransportSecurity(Response response) {

		// Reader header of a give name. In this line we will get Header named
		// Server
		String strictTransportSecurity = response.header("Strict-Transport-Security");
		System.out.println("Server value: " + strictTransportSecurity);

		if ("max-age=63072000; includeSubDomains; preload".equals(strictTransportSecurity)) {
			System.out.println("This is following HTTPStrictTransportSecurity");

		} else {
			System.out.println("This is NOT following HTTPStrictTransportSecurity");

		}
		// Assert.assertEquals("max-age=63072000; includeSubDomains; preload",
		// strictTransportSecurity);
	}

	/*
	 * @Test(groups = { "IntegrationTests" }) public void
	 * Publish_PostValidate_status200() throws IOException {
	 * 
	 * 
	 * Restassured_Automation_Utils allUtils = new
	 * Restassured_Automation_Utils();
	 * 
	 * // fetching Org Id
	 * 
	 * Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL,
	 * AuthorizationKey, "/api/org"); JsonPath jsonPathEvaluator =
	 * OrganizationsDetails.jsonPath(); listOrdId = jsonPathEvaluator.get("id");
	 * OrganizationsDetails.prettyPrint();
	 * 
	 *//**
		 * GETTING THE REVISION ID
		 */

	/*
	 * 
	 * Restassured_Automation_Utils getMethodology = new
	 * Restassured_Automation_Utils();
	 * 
	 * Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL,
	 * AuthorizationKey, "/api/methodology", "Organization", listOrdId.get(3));
	 * getMethodologyRes.prettyPrint();
	 * 
	 * JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath(); List<String>
	 * methodologyId = jsonPathEvaluator1.get("id");
	 * 
	 * //fetching the last item from the list
	 * System.out.println(methodologyId.get(methodologyId.size()-1));
	 * 
	 * //Fetching the updatedId //List<String> listRevisionI1 =
	 * jsonPathEvaluator1.
	 * get("find{it.title== 'createMethodologyAPI'}.revisions[0].updatedIds");
	 * List<String> listRevisionI1 = jsonPathEvaluator1.
	 * get("find{it.title== 'API CreateMethodology'}.revisions");
	 * System.out.println(listRevisionI1);
	 * 
	 * 
	 * //Step 3: CREATE A PUBLISH CANDIDATE
	 * 
	 * Map<String, String[]> data = new HashMap<String, String[]>();
	 * data.put("itemIds", new String[] {listRevisionI1.get(0)});
	 * 
	 * 
	 * Response postpublishCandaidate =
	 * allUtils.post_URL_WithOne_PathParams(URL, AuthorizationKey,
	 * "/api/methodology/candidate/{value}",
	 * methodologyId.get(methodologyId.size()-1), data);
	 * postpublishCandaidate.prettyPrint();
	 * 
	 * 
	 * //Now retrieve the JsonPath jsonPathEvaluator2 =
	 * postpublishCandaidate.jsonPath(); String candidateId =
	 * jsonPathEvaluator2.get("id"); System.out.println(candidateId);
	 * 
	 * Map<String, String> data1 = new HashMap<String, String>();
	 * //data1.put("","");
	 * 
	 * Response postpublish = allUtils.post_URL_WithTwo_PathParams(URL,
	 * AuthorizationKey, "/api/publish/validate/{methodologyId}/{revisionId}",
	 * methodologyId.get(methodologyId.size()-1),candidateId, data1);
	 * postpublish.prettyPrint();
	 *//**
		 * Extent report generation
		 *//*
		 * ExtentTestManager.statusLogMessage(postpublish.statusCode());
		 * ExtentTestManager.getTest().log(Status.INFO, postpublish.asString());
		 * allUtils.validate_HTTPStrictTransportSecurity(postpublish);
		 * 
		 * 
		 * }
		 */
	@Test(groups = { "IntegrationTests" })
	public void Publish_PostValidate_status200() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		String parntId = String.valueOf(parentId.get(6));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		// Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
		data6.put("parentId", methodItemId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postCreatePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId6, createPhaseData1);
		postCreatePhase1.prettyPrint();

		JsonPath jsonEvaluator2 = postCreatePhase1.jsonPath();
		String revision1 = jsonEvaluator2.get("revisionId");
		String methodItemId2 = jsonEvaluator2.get("methodologyItemId");

		String methodologyItemId1 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("displayAs", post.getProperty("patchWorkProgramType1"));
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		// Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType1"));
		data2.put("ruleContextType", post.getProperty("postRuleContextType2"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId2 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam;

		Response postWorkFlowRes2 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId2);
		postWorkFlowRes2.prettyPrint();

		Assert.assertEquals(postWorkFlowRes2.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");

		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		Assert.assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		User_Pojo canPojo = new User_Pojo();
		String createCandidate = canPojo.createCandidate(methodItemId2, methodItemId);
		Response candidateRes = allUtils.post_URLPOJO(URL, AuthorizationKey, CandidateURI, createCandidate);
		candidateRes.prettyPrint();

		// Now retrieve the
		JsonPath jsonPathEvaluator2 = candidateRes.jsonPath();
		String candidateId = jsonPathEvaluator2.get("id");
		System.out.println(candidateId);

		Map<String, String> data1 = new HashMap<String, String>();
		// data1.put("","");

		Response postpublish = allUtils.post_URL_WithTwo_PathParams(URL, AuthorizationKey,
				"/api/publish/validate/{methodologyId}/{revisionId}", parntId, candidateId, data1);
		postpublish.prettyPrint();
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postpublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postpublish.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postpublish);

	}


	/*@Test(groups = { "IntegrationTests" })
	public void Publish_PostPublish_status200() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		*//**
		 * GETTING THE REVISION ID
		 *//*

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");

		// fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size() - 1));

		// Fetching the updatedId
		
		 * List<String> listRevisionI1 = jsonPathEvaluator1.
		 * get("find{it.title== 'createMethodologyAPI'}.revisions[1].updatedIds"
		 * ); System.out.println(listRevisionI1.get(0));
		 

		List<String> listRevisionI1 = jsonPathEvaluator1.get("find{it.title== 'API Methodology Sprint 12'}.revisions");
		System.out.println(listRevisionI1);

		// Step 3: CREATE A PUBLISH CANDIDATE

		Map<String, String[]> data = new HashMap<String, String[]>();
		data.put("itemIds", new String[] { listRevisionI1.get(1) });

		Response postpublishCandaidate = allUtils.post_URL_WithOne_PathParams(URL, AuthorizationKey,
				"/api/methodology/candidate/{value}", methodologyId.get(methodologyId.size() - 1), data);
		postpublishCandaidate.prettyPrint();

		// Now retrieve the
		JsonPath jsonPathEvaluator2 = postpublishCandaidate.jsonPath();
		String candidateId = jsonPathEvaluator2.get("id");
		System.out.println(candidateId);

		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("publishType", "Draft");

		Response postpublish = allUtils.post_URL_WithTwo_PathParams(URL, AuthorizationKey,
				"/api/publish/{methodologyId}/{revisionId}", methodologyId.get(methodologyId.size() - 1), candidateId,
				data1);
		postpublish.prettyPrint();
		*//**
		 * Extent report generation
		 *//*
		ExtentTestManager.statusLogMessage(postpublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postpublish.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postpublish);

	}*/
	
	@Test(groups = { "IntegrationTests" })
	public void Publish_PostPublish_status200() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		String parntId = String.valueOf(parentId.get(6));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		// Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
		data6.put("parentId", methodItemId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postCreatePhase1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId6, createPhaseData1);
		postCreatePhase1.prettyPrint();

		JsonPath jsonEvaluator2 = postCreatePhase1.jsonPath();
		String revision1 = jsonEvaluator2.get("revisionId");
		String methodItemId2 = jsonEvaluator2.get("methodologyItemId");

		String methodologyItemId1 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("displayAs", post.getProperty("patchWorkProgramType1"));
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		// Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType1"));
		data2.put("ruleContextType", post.getProperty("postRuleContextType2"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId2 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam;

		Response postWorkFlowRes2 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId2);
		postWorkFlowRes2.prettyPrint();

		Assert.assertEquals(postWorkFlowRes2.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");

		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		Assert.assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		User_Pojo canPojo = new User_Pojo();
		String createCandidate = canPojo.createCandidate(methodItemId2, methodItemId);
		Response candidateRes = allUtils.post_URLPOJO(URL, AuthorizationKey, CandidateURI, createCandidate);
		candidateRes.prettyPrint();

		// Now retrieve the
		JsonPath jsonPathEvaluator2 = candidateRes.jsonPath();
		String candidateId = jsonPathEvaluator2.get("id");
		System.out.println(candidateId);

		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("publishType", "Draft");

		Response postpublish = allUtils.post_URL_WithTwo_PathParams(URL, AuthorizationKey,
				"/api/publish/{methodologyId}/{revisionId}", parntId, candidateId,
				data1);
		postpublish.prettyPrint();
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postpublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postpublish.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postpublish);

	}

}
