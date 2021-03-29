package restassured.automation.testcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.Engagement_Type_Pojo;
import restassured.automation.Pojo.MethodologyItemRelationPojo;
import restassured.automation.Pojo.MethodologyItem_Pojo;
import restassured.automation.Pojo.Methodology_Pojo;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.Pojo.Validate_Pojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Publish extends read_Configuration_Propertites {

	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrdId;
	String methodologyId;
	String engagementTypeId;

	read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
	Restassured_Automation_Utils restUtils = new Restassured_Automation_Utils();

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 3);
	}

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		// read_Configuration_Propertites configDetails = new
		// read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");

	}

	@Test(groups = { "IntegrationTests" })
	public void getPublish_status200() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		// Step 1 create an engagement
		Response createEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		createEngagementType.prettyPrint();

		// Retrieving the id as engagementType
		JsonPath jsonPathEvaluator = createEngagementType.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("id");

		System.out.println(engagementTypeId);

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
		mp.setEngagementType(engagementTypeId);
		mp.setDraftDescription(post.getProperty("postMethodologyDraftDescription"));
		mp.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		// Step 2 create a Methodology
		Response createMethodology = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/methodology", mp);
		createMethodology.prettyPrint();

		JsonPath jsonPathEvaluator1 = createMethodology.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionId.get(0)));

		String revId = String.valueOf(listRevisionId.get(0));

		// step 3 perfrom validate
		Validate_Pojo vp = new Validate_Pojo();
		Response createPublish = engagementType.post_WithOutBody(URL, AuthorizationKey,
				"/api/publish/publish/" + methodologyId + "/" + revId);
		createPublish.prettyPrint();
		Assert.assertEquals(createPublish.statusCode(), 200);

	}

	@Test(groups = { "IntegrationTests" })
	public void getValidate_status200() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		// Step 1 create an engagement
		Response createEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		createEngagementType.prettyPrint();

		// Retrieving the id as engagementType
		JsonPath jsonPathEvaluator = createEngagementType.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("id");

		System.out.println(engagementTypeId);

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
		mp.setEngagementType(engagementTypeId);
		mp.setDraftDescription(post.getProperty("postMethodologyDraftDescription"));
		mp.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		// Step 2 create a Methodology
		Response createMethodology = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/methodology", mp);
		createMethodology.prettyPrint();

		JsonPath jsonPathEvaluator1 = createMethodology.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionId.get(0)));

		String revId = String.valueOf(listRevisionId.get(0));

		// step 3 perfrom validate
		Validate_Pojo vp = new Validate_Pojo();
		Response createValidate = engagementType.post_WithOutBody(URL, AuthorizationKey,
				"/api/publish/validate/" + methodologyId + "/" + revId);
		createValidate.prettyPrint();
		Assert.assertEquals(createValidate.statusCode(), 200);

	}
	
	
	@Test(groups = "IntegrationTests")
	public void MethodologyItemRelation_DeleteLinkedMethodologyItem_status400()
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

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(9));
		String parntId = String.valueOf(parentId.get(0));
		
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		//MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemType"));
		
		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		
		
		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);
		
		//Performing PATCH operation
		
		String patchId1 = "/api/methodologyItem/revision/"+revision+ "/item/"+ methodItemId;
		
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
		
		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";
		
		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemTitle"));
		data3.put("parentId", methodItemId);
		data3.put("index", post.getProperty("postMethodologyItemIndex"));
		data3.put("itemType", post.getProperty("postProcedureItemType"));
		data3.put("workProgramId", parntId);
		
		User_Pojo postProcedure = new User_Pojo();
		String postProcedureData = postProcedure.procedureAdd(data3);
		
		String firstReplacment = postProcedureData.replace("revisions", "parentId");
		String scndReplacement = firstReplacment.replace("WPId", "workProgramId");
		
		Response postProcedureRes = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId3, scndReplacement);
		postProcedureRes.prettyPrint();
		
		
		//Performing PATCH operation
		
		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");
		
		
	    String patchId4 = "/api/methodologyItem/revision/"+revision+ "/item/"+ methodItemId1;
				
		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("patchWorkProgramItemType"));
				
		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);
				
		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4, patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();
		
		String patchId5 = "/api/methodologyItem/revision/"+revision+ "/itemSelectQuestion/"+ methodItemId1+ "/option";

		 
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
		

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";
		
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

		String methodologyItemId1 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);
		
		//Performing PATCH operation
		
		String patchId7 = "/api/methodologyItem/revision/"+revision1+ "/item/"+ methodItemId2;
		
		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));
		
		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);
		
		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		
		
		String patchId8 = "/api/methodologyItem/revision/" + revision1 + "/workProgram/" + methodItemId2 + "/initialize";

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
		
		
		String patchId9 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";
		
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
		
		
		//Performing PATCH operation
		
		JsonPath jsonEvaluator3 = postProcedureRes1.jsonPath();
		String methodItemId3 = jsonEvaluator3.get("methodologyItemId");
		
		
	    String patchId10 = "/api/methodologyItem/revision/"+revision+ "/item/"+ methodItemId3;
				
		Map<String, String> data10 = new HashMap<String, String>();
		data10.put("workProgramItemType", post.getProperty("patchWorkProgramItemType1"));
				
		User_Pojo patchProcedure1 = new User_Pojo();
		String patchProcedureData1 = patchProcedure1.procedureTypeAdd(data10);
				
		Response patchCreateProcedure1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId10, patchProcedureData1.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure1.prettyPrint();
		
		/**
		 * 
		 * PERFORMING PATCH ON RELATION 
		 * 
		 */
		
		String patchId11 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation";
		
		
		Map<String, String> data11 = new HashMap<String, String>();
		data11.put("methodologyItemId", methodItemId3);
		
		User_Pojo postRelation = new User_Pojo();
		String postRelationData = postRelation.newRelationAdd(data11);
		
		Response postNewRelation = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId11, postRelationData);
		postNewRelation.prettyPrint();
		
		//Retriving relation id
		JsonPath jp = postNewRelation.jsonPath();
		String relationId =  jp.get("relationId");
		
		String patchId12 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"+relationId;
		
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
		
		
		String patchId13 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/relation/"
				+ relationId+"/linked";
		Response deleteRelationRes = AllUtils.delete(URL, AuthorizationKey, patchId13);
		Response deleteRelationRes1 = AllUtils.delete(URL, AuthorizationKey, patchId13);
		deleteRelationRes1.prettyPrint();
		Assert.assertEquals(deleteRelationRes1.getStatusCode(), 400);

	}

}
