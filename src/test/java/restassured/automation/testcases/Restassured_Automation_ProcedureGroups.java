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
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ProcedureGroups extends read_Configuration_Propertites {
	
	
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

		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroup_GetCreateAProcedureGroup_status200() throws JsonIOException, JsonSyntaxException, IOException {
			

			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Response getProcedureGropusRes = AllUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId11);
			
			getProcedureGropusRes.prettyPrint();
		
			validate_HTTPStrictTransportSecurity(getProcedureGropusRes);
			Assert.assertEquals(getProcedureGropusRes.statusCode(), 200);
			
	
			/**
			 * Extent report generation
			 */
						
			ExtentTestManager.statusLogMessage(getProcedureGropusRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,getProcedureGropusRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(getProcedureGropusRes);

			
		}

		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroup_PostCreateAProcedureGroup_status200() throws JsonIOException, JsonSyntaxException, IOException {
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			data6.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
			data6.put("parentId", methodItemId1);
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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId2);
			data11.put("title", "New Group");
			

			/*User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
*/
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			
			validate_HTTPStrictTransportSecurity(postProcedureGropusRes);
			Assert.assertEquals(postProcedureGropusRes.statusCode(), 200);
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(postProcedureGropusRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,postProcedureGropusRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(postProcedureGropusRes);

			
		}
		
		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroup_PostCreateAProcedureGroup_status400() throws JsonIOException, JsonSyntaxException, IOException {
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			data6.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision.substring(0, 5));
			data11.put("methodologyItemId", methodItemId2.substring(0, 5));
			data11.put("title", "New Group");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			
			/**
			 * 
			 * CREATE A PROCEDURE GROUP TREE
			 * 
			 */
			
           /* String patchId12 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("newIndex",revision.substring(0, 5));
			data12.put("methodologyItemId", methodItemId2.substring(0, 5));
			data12.put("title", "New Group");
			
			Response postProcedureGropusTreeRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureGropusTreeRes.prettyPrint();
			
			
			validate_HTTPStrictTransportSecurity(postProcedureGropusRes);
			Assert.assertEquals(postProcedureGropusRes.statusCode(), 400)*/;
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(postProcedureGropusRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,postProcedureGropusRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(postProcedureGropusRes);

			
		}
		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroup_PostCreateAProcedureGroup_status409() throws JsonIOException, JsonSyntaxException, IOException {
			
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId2);
			data11.put("title", "New Group");
			

			/*User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			Response postProcedureGropusRes2 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			Response postProcedureGropusRes1 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes1.prettyPrint();
			
			
			validate_HTTPStrictTransportSecurity(postProcedureGropusRes1);
			Assert.assertEquals(postProcedureGropusRes1.statusCode(), 409);
			
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(postProcedureGropusRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,postProcedureGropusRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(postProcedureGropusRes);
			
			
		}
		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroup_PutCreateAProcedureGroup_status200() throws JsonIOException, JsonSyntaxException, IOException {
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			data6.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId3);
			data11.put("title", "New Group");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			// Performing PATCH operation

		   JsonPath jsonEvaluator4 = postProcedureGropusRes.jsonPath();
		   String procedureGroupId = jsonEvaluator4.get("procedureGroupId");
		 
			
			/**
			 * 
			 * NOW PRFORMING A PUT OPERATION 
			 * 
			 */
			
		   String patchId12 = "/api/procedureGroups/revision/" + revision+"/group/"+procedureGroupId ;

			
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("title", "APIGroUp");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
           */
			
			Response putProcedureGropusRes = AllUtils.put_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			putProcedureGropusRes.prettyPrint();
			
			Assert.assertEquals(putProcedureGropusRes.statusCode(), 200)
			
			/**
			 * 
			 * CREATE A PROCEDURE GROUP TREE
			 * 
			 */
			
           /* String patchId12 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("newIndex",revision.substring(0, 5));
			data12.put("methodologyItemId", methodItemId2.substring(0, 5));
			data12.put("title", "New Group");
			
			Response postProcedureGropusTreeRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureGropusTreeRes.prettyPrint();
			
			
			validate_HTTPStrictTransportSecurity(postProcedureGropusRes);
			Assert.assertEquals(postProcedureGropusRes.statusCode(), 400)*/;
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(putProcedureGropusRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,putProcedureGropusRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(putProcedureGropusRes);

			
		}

		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroup_PutCreateAProcedureGroup_status400() throws JsonIOException, JsonSyntaxException, IOException {
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId2);
			data11.put("title", "New Group");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			// Performing PATCH operation

		   JsonPath jsonEvaluator4 = postProcedureGropusRes.jsonPath();
		   String procedureGroupId = jsonEvaluator4.get("procedureGroupId");
		 
			
			/**
			 * 
			 * NOW PRFORMING A PUT OPERATION 
			 * 
			 */
			
		   String patchId12 = "/api/procedureGroups/revision/" + revision+"/group/"+procedureGroupId ;

			
			Map<String, String> data12 = new HashMap<String, String>();
		
			data12.put("title", " ");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
           */
			
			Response putProcedureGropusRes = AllUtils.put_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			putProcedureGropusRes.prettyPrint();
			Assert.assertEquals(putProcedureGropusRes.statusCode(), 400)
			
			
			/**
			 * 
			 * CREATE A PROCEDURE GROUP TREE
			 * 
			 */
			
           /* String patchId12 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("newIndex",revision.substring(0, 5));
			data12.put("methodologyItemId", methodItemId2.substring(0, 5));
			data12.put("title", "New Group");
			
			Response postProcedureGropusTreeRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureGropusTreeRes.prettyPrint();
			
			
			validate_HTTPStrictTransportSecurity(postProcedureGropusRes);
			Assert.assertEquals(postProcedureGropusRes.statusCode(), 400)*/;
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(putProcedureGropusRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,putProcedureGropusRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(putProcedureGropusRes);

			
		}
		
		
		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroupTree_PostCreateAProcedureGroupTree_status204() throws JsonIOException, JsonSyntaxException, IOException { 
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			data6.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
			data6.put("parentId", methodItemId1);
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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId2);
			data11.put("title", "New Group");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			// Performing PATCH operation

		   JsonPath jsonEvaluator4 = postProcedureGropusRes.jsonPath();
		   String procedureGroupId = jsonEvaluator4.get("procedureGroupId");
		   
		   String patchId12 = "/api/methodologyItem/revision/" + revision+"/item/";
		   
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("itemType", "WorkProgramItem");
			data12.put("parentId", methodItemId2);
			data12.put("workProgramId", methodItemId2);
			data12.put("index", "1");
			data12.put("title", "New Group");
			
			Response postProcedureRes12 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureRes12.prettyPrint();
			
			JsonPath jsonEvaluator5 = postProcedureRes12.jsonPath();
			String methodologyItemId12 = jsonEvaluator5.get("methodologyItemId");
			
			/**
			 * 
			 * NOW PRFORMING A POST OPERATION 
			 * 
			 */
			
		   String patchId13 = "/api/procedureGroups/revision/" + revision+"/tree" ;

			
			Map<String, String> data13 = new HashMap<String, String>();
		
			data13.put("newIndex", "1");
			data13.put("newParentGroupId", procedureGroupId);
			data13.put("itemId", methodologyItemId12);
			data13.put("itemType", "MethodologyItem");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
           */
			 
			Response putProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId13, data13);
			putProcedureGropusRes.prettyPrint();
			Assert.assertEquals(putProcedureGropusRes.statusCode(), 204)
			
			
			/**
			 * 
			 * CREATE A PROCEDURE GROUP TREE
			 * 
			 */
			
           /* String patchId12 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("newIndex",revision.substring(0, 5));
			data12.put("methodologyItemId", methodItemId2.substring(0, 5));
			data12.put("title", "New Group");
			
			Response postProcedureGropusTreeRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureGropusTreeRes.prettyPrint();
			
			
			validate_HTTPStrictTransportSecurity(postProcedureGropusRes);
			Assert.assertEquals(postProcedureGropusRes.statusCode(), 400)*/;
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(putProcedureGropusRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,putProcedureGropusRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(putProcedureGropusRes);

			
		}

		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroupTree_PostCreateAProcedureGroupTree_status400() throws JsonIOException, JsonSyntaxException, IOException { 
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			data6.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId2);
			data11.put("title", "New Group");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			// Performing PATCH operation

		   JsonPath jsonEvaluator4 = postProcedureGropusRes.jsonPath();
		   String procedureGroupId = jsonEvaluator4.get("procedureGroupId");
		   
		   String patchId12 = "/api/methodologyItem/revision/" + revision+"/item/";
		   
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("itemType", "WorkProgramItem");
			data12.put("parentId", methodItemId2);
			data12.put("workProgramId", methodItemId2);
			data12.put("index", "1");
			data12.put("title", "New Group");
			
			Response postProcedureRes12 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureRes12.prettyPrint();
			
			JsonPath jsonEvaluator5 = postProcedureRes12.jsonPath();
			String methodologyItemId12 = jsonEvaluator5.get("methodologyItemId");
			
			/**
			 * 
			 * NOW PRFORMING A POST OPERATION 
			 * 
			 */
			
		   String patchId13 = "/api/procedureGroups/revision/" + revision+"/tree" ;

			
			Map<String, String> data13 = new HashMap<String, String>();
		
			data13.put("newIndex", "1");
			data13.put("newParentGroupId", procedureGroupId.substring(0,5));
			data13.put("itemId", methodologyItemId12.substring(0, 5));
			data13.put("itemType", "MethodologyItem");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
           */
			
			Response putProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId13, data13);
			putProcedureGropusRes.prettyPrint();
			Assert.assertEquals(putProcedureGropusRes.statusCode(), 400)
			
			
			/**
			 * 
			 * CREATE A PROCEDURE GROUP TREE
			 * 
			 */
			
           /* String patchId12 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("newIndex",revision.substring(0, 5));
			data12.put("methodologyItemId", methodItemId2.substring(0, 5));
			data12.put("title", "New Group");
			
			Response postProcedureGropusTreeRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureGropusTreeRes.prettyPrint();
			
			
			validate_HTTPStrictTransportSecurity(postProcedureGropusRes);
			Assert.assertEquals(postProcedureGropusRes.statusCode(), 400)*/;
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(putProcedureGropusRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,putProcedureGropusRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(putProcedureGropusRes);
			
		}
		
		
		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroupTree_DeleteCreateAProcedureGroupTree_status404() throws JsonIOException, JsonSyntaxException, IOException { 
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			data6.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId2);
			data11.put("title", "New Group");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			// Performing PATCH operation

		   JsonPath jsonEvaluator4 = postProcedureGropusRes.jsonPath();
		   String procedureGroupId = jsonEvaluator4.get("procedureGroupId");
		   
		   String patchId12 = "/api/methodologyItem/revision/" + revision+"/item/";
		   
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("itemType", "WorkProgramItem");
			data12.put("parentId", methodItemId2);
			data12.put("workProgramId", methodItemId2);
			data12.put("index", "1");
			data12.put("title", "New Group");
			
			Response postProcedureRes12 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureRes12.prettyPrint();
			
			JsonPath jsonEvaluator5 = postProcedureRes12.jsonPath();
			String methodologyItemId12 = jsonEvaluator5.get("methodologyItemId");
			
			/**
			 * 
			 * NOW PRFORMING A POST OPERATION 
			 * 
			 */
			
		   String patchId13 = "/api/procedureGroups/revision/" + revision+"/tree" ;

			
			Map<String, String> data13 = new HashMap<String, String>();
		
			data13.put("newIndex", "1");
			data13.put("newParentGroupId", procedureGroupId);
			data13.put("itemId", methodologyItemId12);
			data13.put("itemType", "MethodologyItem");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
           */
			
			Response putProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId13, data13);
			putProcedureGropusRes.prettyPrint();
			
			/**
			 * DELETE THE TREE
			 */
			
			 String patchId14 = "/api/procedureGroups/revision/" + revision+"/tree/"+procedureGroupId ;
			 Map<String, String> data14 = new HashMap<String, String>();
				
			data14.put("itemId", methodItemId3);
			Response deleteProcedureGropusTreeRes = AllUtils.delete_URLPOJO(URL, AuthorizationKey, patchId14, data14);
			deleteProcedureGropusTreeRes.prettyPrint();
			
			Assert.assertEquals(deleteProcedureGropusTreeRes.statusCode(), 404);
			
		
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(putProcedureGropusRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,putProcedureGropusRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(putProcedureGropusRes);
			
		}
		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroupTree_DeleteCreateAProcedureGroupTree_status400() throws JsonIOException, JsonSyntaxException, IOException { 
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId2);
			data11.put("title", "New Group");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			// Performing PATCH operation

		   JsonPath jsonEvaluator4 = postProcedureGropusRes.jsonPath();
		   String procedureGroupId = jsonEvaluator4.get("procedureGroupId");
		   
		   String patchId12 = "/api/methodologyItem/revision/" + revision+"/item/";
		   
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("itemType", "WorkProgramItem");
			data12.put("parentId", methodItemId2);
			data12.put("workProgramId", methodItemId2);
			data12.put("index", "1");
			data12.put("title", "New Group");
			
			Response postProcedureRes12 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureRes12.prettyPrint();
			
			JsonPath jsonEvaluator5 = postProcedureRes12.jsonPath();
			String methodologyItemId12 = jsonEvaluator5.get("methodologyItemId");
			
			/**
			 * 
			 * NOW PRFORMING A POST OPERATION 
			 * 
			 */
			
		   String patchId13 = "/api/procedureGroups/revision/" + revision+"/tree" ;

			
			Map<String, String> data13 = new HashMap<String, String>();
		
			data13.put("newIndex", "1");
			data13.put("newParentGroupId", procedureGroupId);
			data13.put("itemId", methodologyItemId12);
			data13.put("itemType", "MethodologyItem");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
           */
			
			Response putProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId13, data13);
			putProcedureGropusRes.prettyPrint();
			
			/**
			 * DELETE THE TREE
			 */
			
			 String patchId14 = "/api/procedureGroups/revision/" + revision+"/tree/"+procedureGroupId ;
			 Map<String, String> data14 = new HashMap<String, String>();
				
			data14.put("itemId", methodItemId3.substring(0, 5));
			Response deleteProcedureGropusTreeRes = AllUtils.delete_URLPOJO(URL, AuthorizationKey, patchId14, data14);
			deleteProcedureGropusTreeRes.prettyPrint();
			
			Assert.assertEquals(deleteProcedureGropusTreeRes.statusCode(), 400);
			
		
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(deleteProcedureGropusTreeRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,deleteProcedureGropusTreeRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(deleteProcedureGropusTreeRes);
			
		}
		
		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroupTree_PutCreateAProcedureGroupTree_status204() throws JsonIOException, JsonSyntaxException, IOException { 
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			data6.put("title", post.getProperty("postMethodologyItemTitle")+AllUtils.getRandomNumber(1, 20));
			data6.put("parentId", methodItemId1);
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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId2);
			data11.put("title", "New Group");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			// Performing PATCH operation

		   JsonPath jsonEvaluator4 = postProcedureGropusRes.jsonPath();
		   String procedureGroupId = jsonEvaluator4.get("procedureGroupId");
		   
		   String patchId12 = "/api/methodologyItem/revision/" + revision+"/item/";
		   
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("itemType", "WorkProgramItem");
			data12.put("parentId", methodItemId2);
			data12.put("workProgramId", methodItemId2);
			data12.put("index", "1");
			data12.put("title", "New Group");
			
			Response postProcedureRes12 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureRes12.prettyPrint();
			
			JsonPath jsonEvaluator5 = postProcedureRes12.jsonPath();
			String methodologyItemId12 = jsonEvaluator5.get("methodologyItemId");
			
			/**
			 * 
			 * NOW PRFORMING A POST OPERATION 
			 * 
			 */
			
		   String patchId13 = "/api/procedureGroups/revision/" + revision+"/tree" ;

			
			Map<String, String> data13 = new HashMap<String, String>();
		
			data13.put("newIndex", "1");
			data13.put("newParentGroupId", procedureGroupId);
			data13.put("itemId", methodologyItemId12);
			data13.put("itemType", "MethodologyItem");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
           */
			
			Response putProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId13, data13);
			putProcedureGropusRes.prettyPrint();
			
			/**
			 * UPDATE THE TREE
			 */
			
			 String patchId14 = "/api/procedureGroups/revision/" + revision+"/tree/"+procedureGroupId ;
			 Map<String, String> data14 = new HashMap<String, String>();
				
			data14.put("itemId", methodItemId3);
			Response putProcedureGropusTreeRes = AllUtils.put_URLPOJO(URL, AuthorizationKey, patchId14, data14);
			putProcedureGropusTreeRes.prettyPrint();
			
			Assert.assertEquals(putProcedureGropusTreeRes.statusCode(), 404);
			
		
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(putProcedureGropusTreeRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,putProcedureGropusTreeRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(putProcedureGropusTreeRes);
			
		}


		@Test(groups = { "IntegrationTests" })
		public void ProcedureGroupTree_PutCreateAProcedureGroupTree_status400() throws JsonIOException, JsonSyntaxException, IOException { 
			
			Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
			// fetching Org Id

			Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
			JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
			listOrdId = jsonPathEvaluator.get("id");
			// OrganizationsDetails.prettyPrint();

			Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

			Response getMethodologyRes = getMethodology.get_URL_MultiQueryParams(URL, AuthorizationKey, "/api/methodology",
					"Organization", listOrdId.get(3), "PublishedOnly", "true");

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
			 * CREATE A Procedure Group
			 * 
			 */
			
			String patchId11 = "/api/procedureGroups/revision/" + revision ;

			
			Map<String, String> data11 = new HashMap<String, String>();
			data11.put("revision",revision);
			data11.put("methodologyItemId", methodItemId2);
			data11.put("title", "New Group");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
            */
			
			Response postProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, data11);
			postProcedureGropusRes.prettyPrint();
			
			// Performing PATCH operation

		   JsonPath jsonEvaluator4 = postProcedureGropusRes.jsonPath();
		   String procedureGroupId = jsonEvaluator4.get("procedureGroupId");
		   
		   String patchId12 = "/api/methodologyItem/revision/" + revision+"/item/";
		   
			Map<String, String> data12 = new HashMap<String, String>();
			data12.put("itemType", "WorkProgramItem");
			data12.put("parentId", methodItemId2);
			data12.put("workProgramId", methodItemId2);
			data12.put("index", "1");
			data12.put("title", "New Group");
			
			Response postProcedureRes12 = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId12, data12);
			postProcedureRes12.prettyPrint();
			
			JsonPath jsonEvaluator5 = postProcedureRes12.jsonPath();
			String methodologyItemId12 = jsonEvaluator5.get("methodologyItemId");
			
			/**
			 * 
			 * NOW PRFORMING A POST OPERATION 
			 * 
			 */
			
		   String patchId13 = "/api/procedureGroups/revision/" + revision+"/tree" ;

			
			Map<String, String> data13 = new HashMap<String, String>();
		
			data13.put("newIndex", "1");
			data13.put("newParentGroupId", procedureGroupId);
			data13.put("itemId", methodologyItemId12);
			data13.put("itemType", "MethodologyItem");

			/**
			User_Pojo postProcedureGroups = new User_Pojo();
			String postProcedureGroup = postProcedureGroups.procedureAdd(data11);
           */
			
			Response putProcedureGropusRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId13, data13);
			putProcedureGropusRes.prettyPrint();
			
			/**
			 * DELETE THE TREE
			 */
			
			 String patchId14 = "/api/procedureGroups/revision/" + revision+"/tree/"+procedureGroupId ;
			 Map<String, String> data14 = new HashMap<String, String>();
				
			data14.put("itemId", methodItemId3.substring(0, 5));
			Response putProcedureGropusTreeRes = AllUtils.put_URLPOJO(URL, AuthorizationKey, patchId14, data14);
			putProcedureGropusTreeRes.prettyPrint();
			
			Assert.assertEquals(putProcedureGropusTreeRes.statusCode(), 400);
			
		
			
			/**
			 * Extent report generation
			 */
			
			
			ExtentTestManager.statusLogMessage(putProcedureGropusTreeRes.statusCode());
			ExtentTestManager.getTest().log(Status.INFO,putProcedureGropusTreeRes.asString());
			AllUtils.validate_HTTPStrictTransportSecurity(putProcedureGropusTreeRes);
			
		}


		
}
