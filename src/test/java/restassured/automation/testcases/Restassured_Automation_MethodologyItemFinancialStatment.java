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

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_MethodologyItemFinancialStatment {
	String URL;
	String AuthorizationKey;
	List<String> listRevisionId;
	List<String> listOrdId;
	List<String> methodologyId;
	List<String> workFlow;
	List<String> methodlogyItemId;
	Restassured_Automation_Utils allUtils;
	Properties post;

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
		Awaitility.reset();
		Awaitility.setDefaultPollDelay(999, MILLISECONDS);
		Awaitility.setDefaultPollInterval(99, SECONDS);
		Awaitility.setDefaultTimeout(99, SECONDS);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_CreateANewFinancialStatementForAWorkProgramStatus_200()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();

		Assert.assertEquals(finanacialStatmentRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(finanacialStatmentRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, finanacialStatmentRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(finanacialStatmentRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_CreateANewFinancialStatementForAWorkProgramStatus_400()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();
		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + revisionId + "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("itemType", post.getProperty("postWorkProgramItemItemType"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();

		Assert.assertEquals(finanacialStatmentRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(finanacialStatmentRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, finanacialStatmentRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(finanacialStatmentRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_CreateANewFinancialStatementForAWorkProgramStatus_404()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();
		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgramss/" + revisionId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("itemType", post.getProperty("postWorkProgramItemItemType"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();

		Assert.assertEquals(finanacialStatmentRes.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(finanacialStatmentRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, finanacialStatmentRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(finanacialStatmentRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_UpdateFinancialStatementForAWorkProgramStatus_204()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli/" + fsliId;
		Map<String, String> fin = new HashMap<String, String>();
		fin.put("type", "LineItem");
		User_Pojo fPojo = new User_Pojo();
		String patchFsli = fPojo.UpdateFsliFinancialStatment(fin);
		Response patchFsliRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFinStatment, patchFsli);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_UpdateFinancialStatementForAWorkProgramStatus_404()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli" + fsliId;
		Map<String, String> fin = new HashMap<String, String>();
		fin.put("type", "LineItem");
		User_Pojo fPojo = new User_Pojo();
		String patchFsli = fPojo.UpdateFsliFinancialStatment(fin);
		Response patchFsliRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFinStatment, patchFsli);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);
	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_UpdateFinancialStatementForAWorkProgramStatus_400()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli/" + fsliId;
		Map<String, String> fin = new HashMap<String, String>();
		fin.put("type", "sdfjskdhfskj");
		User_Pojo fPojo = new User_Pojo();
		String patchFsli = fPojo.UpdateFsliFinancialStatment(fin);
		Response patchFsliRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFinStatment, patchFsli);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_UpdateFinancialStatementForAWorkProgramStatus_409()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement1"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");
		

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli/" + fsliId;
		Map<String, String> fin = new HashMap<String, String>();
		fin.put("type", "LineItem");
		
		User_Pojo fPojo = new User_Pojo();
		String patchFsli = fPojo.UpdateFsliFinancialStatment(fin);
		Response patchFsliRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFinStatment, patchFsli);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);
	}
	
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_DeleteFinancialStatementForAWorkProgramStatus_204()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli/" + fsliId;
		
		Response patchFsliRes = allUtils.delete(URL, AuthorizationKey, patchFinStatment);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);
	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_DeleteFinancialStatementForAWorkProgramStatus_404()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli/" + workProgramId1;
		
		Response patchFsliRes = allUtils.delete(URL, AuthorizationKey, patchFinStatment);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);
	}
	
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_DeleteFinancialStatementForAWorkProgramStatus_400()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");
		String wpId= workProgramId.substring(0, 23)+"0";

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + wpId
				+ "/fsli/" + fsliId;
		
		Response patchFsliRes = allUtils.delete(URL, AuthorizationKey, patchFinStatment);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);
	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_MoveFinancialStatementForAWorkProgramStatus_204()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli/" + fsliId+"/move";
		Map<String, String> fin = new HashMap<String, String>();
		fin.put("index", "1");
		User_Pojo fPojo = new User_Pojo();
		String patchFsli = fPojo.UpdateFsliFinancialStatment(fin);
		Response patchFsliRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFinStatment, patchFsli);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);

	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_MoveFinancialStatementForAWorkProgramStatus_400()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli/" + fsliId+"/move";
		Map<String, String> fin = new HashMap<String, String>();
		fin.put("index", " ");
		User_Pojo fPojo = new User_Pojo();
		String patchFsli = fPojo.UpdateFsliFinancialStatment(fin);
		Response patchFsliRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFinStatment, patchFsli);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);

	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FinancialStatement_MoveFinancialStatementForAWorkProgramStatus_404()
			throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		List<String> Phase = getEngagementTypeRes
				.path("findAll { it.itemType=='Folder' && it.workFlowState=='Draft'}.methodologyItemId");
		System.out.println(Phase);

		String parentId = Phase.get(1);
		System.out.println(parentId);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
		/**
		 * Create an workProgram
		 */

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + getRandomAlphaNum());
		data6.put("parentId", parentId);
		data6.put("index", post.getProperty("postMethodologyItemIndex"));
		data6.put("itemType", post.getProperty("postMethodologyItemType"));

		User_Pojo createPhase1 = new User_Pojo();
		String createPhaseData1 = createPhase1.PhaseCreate(data6);

		Response postMethodologyItem = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId1, createPhaseData1);
		postMethodologyItem.prettyPrint();

		JsonPath phaseJson = postMethodologyItem.jsonPath();
		String revisionId = phaseJson.get("revisionId");
		String methodologyItemId = phaseJson.get("methodologyItemId");
		System.out.println("Revision id------>" + revisionId);
		System.out.println("MethodologyItemId------>" + methodologyItemId);

		String patchId7 = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodologyItemId;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();

		String patchId2 = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + methodologyItemId
				+ "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType"));
		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();

		String patchId3 = "/api/methodologyItem/revision/" + revisionId + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemType"));
		data3.put("parentId", methodologyItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data3.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);

		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");

		Response postProcedureRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		JsonPath postJson = postProcedureRes.jsonPath();
		String methodId = postJson.get("methodologyItemId");
		String workProgramId = postJson.get("workProgramId");

		String fspatchId = "/api/methodologyItem/revision/" + revisionId + "/item/" + methodId;

		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgramItemType", post.getProperty("postprocedureWorkProgramItemType"));
		User_Pojo pojo = new User_Pojo();
		String workType = pojo.procedureTypeAdd(map);

		Response workTypeRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, fspatchId, workType);
		workTypeRes.prettyPrint();

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("title", "value");
		data4.put("parentId", methodologyItemId);
		data4.put("index", "1");
		data4.put("itemType", post.getProperty("postWorkProgramItemItemType"));
		data4.put("workProgramId", methodologyItemId);

		User_Pojo postProcedure1 = new User_Pojo();
		String postProcedureData1 = postProcedure1.procedureAdd(data3);

		String firstReplacment1 = postProcedureData1.replace("revisions", "parentId");
		String scndReplacement1 = firstReplacment1.replace("WPId", "workProgramId");

		Response postProcedureRes1 = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement1);
		postProcedureRes1.prettyPrint();
		JsonPath postJson1 = postProcedureRes1.jsonPath();
		String methodId1 = postJson1.get("methodologyItemId");
		String workProgramId1 = postJson1.get("workProgramId");

		String postFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli";
		User_Pojo pojo1 = new User_Pojo();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("financialStatementType", post.getProperty("postMethodologyItemFinancialStatement"));

		String createFinStat = pojo1.CreateFinancialStatment(map2);
		Response finanacialStatmentRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFinStatment, createFinStat);
		finanacialStatmentRes.prettyPrint();
		JsonPath statmentJson = finanacialStatmentRes.jsonPath();
		String fsliId = statmentJson.get("id");

		String patchFinStatment = "/api/methodologyItem/revision/" + revisionId + "/workProgram/" + workProgramId
				+ "/fsli/" + workProgramId+"/move";
		Map<String, String> fin = new HashMap<String, String>();
		fin.put("index", "1");
		User_Pojo fPojo = new User_Pojo();
		String patchFsli = fPojo.UpdateFsliFinancialStatment(fin);
		Response patchFsliRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFinStatment, patchFsli);
		patchFsliRes.prettyPrint();
		Assert.assertEquals(patchFsliRes.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFsliRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFsliRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFsliRes);

	}
}
