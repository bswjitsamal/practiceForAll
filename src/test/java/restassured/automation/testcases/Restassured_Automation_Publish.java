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

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.Engagement_Type_Pojo;
import restassured.automation.Pojo.MethodologyItemRelationPojo;
import restassured.automation.Pojo.MethodologyItem_Pojo;
import restassured.automation.Pojo.Methodology_Pojo;
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

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		mi1.setTitle(post.getProperty("postMethodologyItemTitle"));
		mi1.setParentId(parentId.get(0));
		mi1.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi1.setItemType(post.getProperty("postMethodologyItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi1);
		postMethodologyItem.prettyPrint();
		
		/*
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
		*//**
		 * Create an procedure
		 *//*
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
		*//**
		 * CREATING THE RELATION
		 *//*

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
*/
	}

}
