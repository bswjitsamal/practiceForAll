package restassured.automation.testcases;

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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.Result;
import restassured.automation.Pojo.RootConditionGroup;
import restassured.automation.Pojo.RootConditionGroup.Children;
import restassured.automation.Pojo.ServiceDetailsPojo;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Rules extends read_Configuration_Propertites {

	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrdId;

	read_Configuration_Propertites configDetails = new read_Configuration_Propertites();

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
	
	// Validating HTTP Strict Transport security
		public void validate_HTTPStrictTransportSecurity(Response response) {

			// Reader header of a give name. In this line we will get Header named Server
			String strictTransportSecurity = response.header("Strict-Transport-Security");
			System.out.println("Server value: " + strictTransportSecurity);
			
			if("max-age=63072000; includeSubDomains; preload".equals(strictTransportSecurity)) {
				System.out.println("This is following HTTPStrictTransportSecurity");
					
			}else {
				System.out.println("This is NOT following HTTPStrictTransportSecurity");
				
			}
			//Assert.assertEquals("max-age=63072000; includeSubDomains; preload", strictTransportSecurity);
		}

	@Test(priority = 1 ,groups = { "IntegrationTests" })
	public void Rules_getTheListOfRulesForARevision_status200() throws IOException {
		


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
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(2)));

		String revId = String.valueOf(listRevisionI1.get(2));


		String patchId = "/api/rules/revision/" +  revId.substring(1,25) + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		RulesDetails.prettyPrint();

		validate_HTTPStrictTransportSecurity(RulesDetails);
		Assert.assertEquals(RulesDetails.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(RulesDetails.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,RulesDetails.asString());
		rules.validate_HTTPStrictTransportSecurity(RulesDetails);

	}

	@Test(priority = 4 ,groups = { "IntegrationTests" })
	public void Rules_PostCreateANewRuleSet_status200() throws JsonIOException, JsonSyntaxException, IOException {
		
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

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
		 * Create an procedure 1
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		String createMethodologyData = createMethodologyPojo.UpdateOption();
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
		data9.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		 * CREATE A NEW RULESET
		 * 
		 */
		

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel.setTempId("1619011296814child");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(true);
		sel.setSourceId(methodItemId3);
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("1619011296814group");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setType("ConditionGroup");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId(methodItemId1);
		result.setType("MethodologyItem");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);

		
		String patchId11 = "/api/rules/revision/" +  revision;


		Response postOrganizationData = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);
		System.out.println(postOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		
		
		ExtentTestManager.statusLogMessage(postOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postOrganizationData.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(postOrganizationData);

	}

	@Test(priority = 5 ,groups = { "IntegrationTests" })
	public void Rules_postCreateANewRuleSet_status400() throws JsonIOException, JsonSyntaxException, IOException {
		
		
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
		 * Create an procedure 1
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		String createMethodologyData = createMethodologyPojo.UpdateOption();
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
		data9.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		 * CREATE A NEW RULESET
		 * 
		 */
		

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel.setTempId("1619011296814child");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(true);
		sel.setSourceId(methodItemId3.substring(1, 10));
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("1619011296814group");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setType("ConditionGroup");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId(methodItemId1.substring(1, 10));
		result.setType("MethodologyItem");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);

		
		String patchId11 = "/api/rules/revision/" +  revision;


		Response postOrganizationData = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);
		System.out.println(postOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		
		
		ExtentTestManager.statusLogMessage(postOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postOrganizationData.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(postOrganizationData);

	}
	
	@Test(priority = 6 ,groups = { "IntegrationTests" })
	public void Rules_postCreateANewRuleSet_status409() throws JsonIOException, JsonSyntaxException, IOException {
		
		
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
		 * Create an procedure 1
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		String createMethodologyData = createMethodologyPojo.UpdateOption();
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
		data9.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		 * CREATE A NEW RULESET
		 * 
		 */
		

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel.setTempId("1619011296814child");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(true);
		sel.setSourceId(methodItemId3);
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("1619011296814group");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setType("ConditionGroup");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId(methodItemId1);
		result.setType("MethodologyItem");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);

		
		String patchId11 = "/api/rules/revision/" +  revision;


		Response postOrganizationData = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);
		Response postOrganizationData1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);
		System.out.println(postOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData1);
		Assert.assertEquals(postOrganizationData1.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		
		
		ExtentTestManager.statusLogMessage(postOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postOrganizationData.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(postOrganizationData);

	}

	@Test(priority = 7 ,groups = { "IntegrationTests" })
	public void Rules_putUpdateANewRuleSet_status200() throws JsonIOException, JsonSyntaxException, IOException {
		
		
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
		 * Create an procedure 1
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		String createMethodologyData = createMethodologyPojo.UpdateOption();
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
		data9.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		 * CREATE A NEW RULESET
		 * 
		 */
		

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel.setTempId("1619011296814child");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(true);
		sel.setSourceId(methodItemId3);
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("1619011296814group");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setType("ConditionGroup");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId(methodItemId1);
		result.setType("MethodologyItem");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);

		
		String patchId11 = "/api/rules/revision/" +  revision;


		Response postOrganizationData = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);

		System.out.println(postOrganizationData.asString());
	
		//Retriving relation id
		JsonPath jp = postOrganizationData.jsonPath();
		String ruleId = jp.get("ruleId");
	
		/**
		 * 
		 * PERFORING PUT REQUEST
		 * 
		 */
		
		List<Children> children1 = new ArrayList<Children>();
		RootConditionGroup.Children sel1 = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel1.setTempId("1619011296814child");
		sel1.setIsNew(true);
		sel1.setIsMultipleInstancesConjunction(true);
		sel1.setSourceId(methodItemId1);
		sel1.setType("MultipleItem");
		sel1.setValue("");
		sel1.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel1.setMultipleLogicOperator("Any");
		
		children1.add(sel1);

		RootConditionGroup rootConditionGroup1 = new RootConditionGroup();
		rootConditionGroup1.setTempId("1619011296814group");
		rootConditionGroup1.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup1.setType("ConditionGroup");
		rootConditionGroup1.setChildren(children1);
		

		Result result1 = new Result();
		//result.setName("result");
		result1.setOperation("Show");
		result1.setTargetId(methodItemId3);
		result1.setType("MethodologyItem");

		ServiceDetailsPojo sp1 = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp1.setRootConditionGroup(rootConditionGroup1);
		sp1.setResult(result1);
		sp1.setIsComplex(false);

		String patchId12 = "/api/rules/revision/" + revision+"/rule/" + ruleId;

		Response putOrganizationData = AllUtils.put_URLPOJO(URL, AuthorizationKey, patchId12, sp1);
		System.out.println(putOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(putOrganizationData.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putOrganizationData.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(putOrganizationData);
		
	}

	@Test(priority = 8 ,groups = { "IntegrationTests" })
	public void Rules_putUpdateANewRuleSet_status400() throws JsonIOException, JsonSyntaxException, IOException {
		
		
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
		 * Create an procedure 1
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		String createMethodologyData = createMethodologyPojo.UpdateOption();
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
		data9.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		 * CREATE A NEW RULESET
		 * 
		 */
		

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel.setTempId("1619011296814child");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(true);
		sel.setSourceId(methodItemId3);
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("1619011296814group");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setType("ConditionGroup");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId(methodItemId1);
		result.setType("MethodologyItem");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);

		
		String patchId11 = "/api/rules/revision/" +  revision;


		Response postOrganizationData = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);

		System.out.println(postOrganizationData.asString());
	
		//Retriving relation id
		JsonPath jp = postOrganizationData.jsonPath();
		String ruleId = jp.get("ruleId");
	
		/**
		 * 
		 * PERFORING PUT REQUEST
		 * 
		 */
		
		List<Children> children1 = new ArrayList<Children>();
		RootConditionGroup.Children sel1 = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel1.setTempId("1619011296814child");
		sel1.setIsNew(true);
		sel1.setIsMultipleInstancesConjunction(true);
		sel1.setSourceId(methodItemId1.substring(1, 5));
		sel1.setType("MultipleItem");
		sel1.setValue("");
		sel1.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel1.setMultipleLogicOperator("Any");
		
		children1.add(sel1);

		RootConditionGroup rootConditionGroup1 = new RootConditionGroup();
		rootConditionGroup1.setTempId("1619011296814group");
		rootConditionGroup1.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup1.setType("ConditionGroup");
		rootConditionGroup1.setChildren(children);
		

		Result result1 = new Result();
		//result.setName("result");
		result1.setOperation("Show");
		result1.setTargetId(methodItemId3.substring(1,10));
		result1.setType("MethodologyItem");

		ServiceDetailsPojo sp1 = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp1.setRootConditionGroup(rootConditionGroup1);
		sp1.setResult(result1);
		sp1.setIsComplex(false);

		String patchId12 = "/api/rules/revision/" + revision+"/rule/" + ruleId;

		Response putOrganizationData = AllUtils.put_URLPOJO(URL, AuthorizationKey, patchId12, sp1);
		System.out.println(putOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(putOrganizationData.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putOrganizationData.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(putOrganizationData);
		
	}
	
	@Test(priority = 9 ,groups = { "IntegrationTests" })
	public void Rules_putUpdateANewRuleSet_status409() throws JsonIOException, JsonSyntaxException, IOException {
		
		
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
		 * Create an procedure 1
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		String createMethodologyData = createMethodologyPojo.UpdateOption();
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
		data9.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		 * CREATE A NEW RULESET
		 * 
		 */
		

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel.setTempId("1619011296814child");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(true);
		sel.setSourceId(methodItemId3);
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("1619011296814group");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setType("ConditionGroup");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId(methodItemId1);
		result.setType("MethodologyItem");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);

		
		String patchId11 = "/api/rules/revision/" +  revision;


		Response postOrganizationData = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);

		System.out.println(postOrganizationData.asString());
	
		//Retriving relation id
		JsonPath jp = postOrganizationData.jsonPath();
		String ruleId = jp.get("ruleId");
	
		/**
		 * 
		 * PERFORING PUT REQUEST
		 * 
		 */
		
		List<Children> children1 = new ArrayList<Children>();
		RootConditionGroup.Children sel1 = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel1.setTempId("1619011296814child");
		sel1.setIsNew(true);
		sel1.setIsMultipleInstancesConjunction(true);
		sel1.setSourceId(methodItemId1);
		sel1.setType("MultipleItem");
		sel1.setValue("");
		sel1.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel1.setMultipleLogicOperator("Any");
		
		children1.add(sel1);

		RootConditionGroup rootConditionGroup1 = new RootConditionGroup();
		rootConditionGroup1.setTempId("1619011296814group");
		rootConditionGroup1.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup1.setType("ConditionGroup");
		rootConditionGroup1.setChildren(children);
		

		Result result1 = new Result();
		//result.setName("result");
		result1.setOperation("Show");
		result1.setTargetId(methodItemId3);
		result1.setType("MethodologyItem");

		ServiceDetailsPojo sp1 = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp1.setRootConditionGroup(rootConditionGroup1);
		sp1.setResult(result1);
		sp1.setIsComplex(false);

		String patchId12 = "/api/rules/revision/" + revision+"/rule/" + ruleId;

		Response putOrganizationData = AllUtils.put_URLPOJO(URL, AuthorizationKey, patchId12, sp1);
		Response putOrganizationData1 = AllUtils.put_URLPOJO(URL, AuthorizationKey, patchId12, sp1);
		System.out.println(putOrganizationData1.asString());
		
		validate_HTTPStrictTransportSecurity(putOrganizationData1);
		Assert.assertEquals(putOrganizationData1.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putOrganizationData.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(putOrganizationData);
		
	}

	@Test(priority = 2 ,groups = { "IntegrationTests" })
	public void Rules_deleteARuleSet_status204() throws IOException {
		
		
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
		 * Create an procedure 1
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		String createMethodologyData = createMethodologyPojo.UpdateOption();
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
		data9.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		 * CREATE A NEW RULESET
		 * 
		 */
		

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel.setTempId("1619011296814child");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(true);
		sel.setSourceId(methodItemId3);
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("1619011296814group");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setType("ConditionGroup");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId(methodItemId1);
		result.setType("MethodologyItem");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);

		
		String patchId11 = "/api/rules/revision/" +  revision;


		Response postOrganizationData = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);

		System.out.println(postOrganizationData.asString());
	
		//Retriving relation id
		JsonPath jp = postOrganizationData.jsonPath();
		String ruleId = jp.get("ruleId");
	
		

		String patchId12 = "/api/rules/revision/" + revision+"/rule/" + ruleId;

		Response deleteOrganizationData = AllUtils.delete(URL, AuthorizationKey, patchId12);
		
		System.out.println(deleteOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(deleteOrganizationData);
		Assert.assertEquals(deleteOrganizationData.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,deleteOrganizationData.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(deleteOrganizationData);
		
	}

	@Test(priority = 3 ,groups = { "IntegrationTests" })
	public void Rules_deleteARuleSet_status400() throws IOException {
		
		
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
		 * Create an procedure 1
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		String createMethodologyData = createMethodologyPojo.UpdateOption();
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
		data9.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		 * CREATE A NEW RULESET
		 * 
		 */
		

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel.setTempId("1619011296814child");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(true);
		sel.setSourceId(methodItemId3);
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("1619011296814group");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setType("ConditionGroup");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId(methodItemId1);
		result.setType("MethodologyItem");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);

		
		String patchId11 = "/api/rules/revision/" +  revision;


		Response postOrganizationData = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);

		System.out.println(postOrganizationData.asString());
	
		//Retriving relation id
		JsonPath jp = postOrganizationData.jsonPath();
		String ruleId = jp.get("ruleId");
	
		

		String patchId12 = "/api/rules/revision/" + revision+"/rule" + ruleId;

		Response deleteOrganizationData = AllUtils.delete(URL, AuthorizationKey, patchId12);
		
		System.out.println(deleteOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(deleteOrganizationData);
		Assert.assertEquals(deleteOrganizationData.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,deleteOrganizationData.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(deleteOrganizationData);
		
	}
	
	//@Test(groups = { "EndToEnd" })
	public void Rule_ENDTOEND_Scenario() throws JsonIOException, JsonSyntaxException, IOException {
		
		//PERFORMING THE GET OPERATION TO RETRIVE THE RECORD 
		
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		String patchGETId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/list";
		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();
		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchGETId);
		RulesDetails.prettyPrint();
		Assert.assertEquals(RulesDetails.statusCode(), 200);
		
		//PERFORMING THE POST OPERATION TO ISERT RECORD
		
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 /\\nProcedure 2");
		sel.setTempId("1615527874317");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(false);
		sel.setSourceId("604afe90776641acb09dbfa5");
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");

		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("tempId");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId("604afe04776641acb09dbfa1");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);


		// 1st retrive the ruleID

		String patchPOSTId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId");

		Response postRuleData = rules.post_URLPOJO(URL, AuthorizationKey, patchPOSTId, sp);
		System.out.println(postRuleData.asString());
		Assert.assertEquals(postRuleData.statusCode(), 200);
		
		JsonPath jsonPathEvaluator = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator.get("ruleId");
		System.out.println("The Newly Created Rule ID is : "+listRuleId);
		
		// PERFORMING THE PUT OPERATION TO UPDATE RECORD
		
		
		
		String ruleId = listRuleId.get(0);
		String patchPUTId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/rule/" + ruleId;

		Response putRuleData = rules.put_URLPOJO(URL, AuthorizationKey, patchPUTId, sp);
		System.out.println(putRuleData.asString());
		Assert.assertEquals(putRuleData.statusCode(), 200);
		
		
		// PERFORMING DELETE OPERATION TO THE SMAE UPDATE RECORD
		
		//String ruleId = listRuleId.get(0);
		String patchDELETEId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/rule/" + ruleId;

		Response deleteRuleData = rules.delete(URL, AuthorizationKey, patchDELETEId);
		System.out.println(deleteRuleData.asString());
		
		validate_HTTPStrictTransportSecurity(deleteRuleData);
		Assert.assertEquals(deleteRuleData.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRuleData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,deleteRuleData.asString());
		rules.validate_HTTPStrictTransportSecurity(deleteRuleData);
	}

	@Test(priority = 4 ,groups = { "IntegrationTests" })
	public void Rules_postCreateANewRuleSet_status200() throws JsonIOException, JsonSyntaxException, IOException {
		
		
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
		 * Create an procedure 1
		 */
	
		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";
	
		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		String createMethodologyData = createMethodologyPojo.UpdateOption();
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
		data9.put("title", post.getProperty("postProcedureTitle")+AllUtils.getRandomNumber(1, 20));
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
		 * CREATE A NEW RULESET
		 * 
		 */
		
	
		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 ");
		sel.setTempId("1619011296814child");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(true);
		sel.setSourceId(methodItemId3);
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		children.add(sel);
	
		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("1619011296814group");
		rootConditionGroup.setOperator("And");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setType("ConditionGroup");
		rootConditionGroup.setChildren(children);
		
	
		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId(methodItemId1);
		result.setType("MethodologyItem");
	
		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);
	
		
		String patchId11 = "/api/rules/revision/" +  revision;
	
	
		Response postOrganizationData = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, sp);
		System.out.println(postOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		
		
		ExtentTestManager.statusLogMessage(postOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postOrganizationData.asString());
		AllUtils.validate_HTTPStrictTransportSecurity(postOrganizationData);
	
	}
	

}
