package restassured.automation.testcases;

import java.io.IOException;
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
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_EngagementType extends read_Configuration_Propertites {

	String URL;
	String AuthorizationKey;
	List<String> listEngagementTypeId;
	List<String> listOrdId;

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

	}

	// @Test(groups = "IntegrationTests", dataProvider = "dataSource",
	// dataProviderClass = Excel_Data_Source_Utils.class)
	public void createANewEngagementType(String title, String responseCode) {

		int statusCode = Integer.parseInt(responseCode);

		Restassured_Automation_Utils CreateEngagemenType = new Restassured_Automation_Utils();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", title);

		User_Pojo createEngagement = new User_Pojo();
		String createEngagementData = createEngagement.Create_Engagement(data);
		System.out.println("--->" + createEngagementData);
		Response createEngagementType = CreateEngagemenType.post_URL(URL, AuthorizationKey, "/api/engagementtype",
				createEngagementData);
		createEngagementType.prettyPrint();

		Assert.assertEquals(createEngagementType.statusCode(), statusCode);

	}

	// @Test(groups = "IntegrationTests", dataProvider = "dataSource",
	// dataProviderClass = Excel_Data_Source_Utils.class)
	public void updateAnEngagementType(String title, String engagementType, String responseCode) {

		int statusCode = Integer.parseInt(responseCode);

		Restassured_Automation_Utils CreateEngagemenType = new Restassured_Automation_Utils();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", title);
		data.put("engagementType", engagementType);

		User_Pojo createEngagement = new User_Pojo();
		String createEngagementData = createEngagement.Create_Engagement(data);
		System.out.println("--->" + createEngagementData);
		Response createEngagementType = CreateEngagemenType.patch_URL(URL, AuthorizationKey,
				"/api/engagementType/{engagementType}", createEngagementData, "5fb5c087fdd2fe1fec4b9917");
		createEngagementType.prettyPrint();

		Assert.assertEquals(createEngagementType.statusCode(), statusCode);

	}

	@Test(groups = "IntegrationTests")
	public void getListAllEngagementType() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey,
				"/api/engagementType","Organization", listOrdId.get(4));
		getEngagementTypeRes.prettyPrint();

		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void PostcreateANewEngagementType_status200() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		postEngagementType.prettyPrint();

		String idValue = postEngagementType.then().extract().path("id").toString();
		System.out.println(idValue);
		Assert.assertEquals(postEngagementType.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void PostcreateANewEngagementType_status400() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitleBadRequest"));
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		postEngagementType.prettyPrint();

		Assert.assertEquals(postEngagementType.statusCode(), 400);

	}

	@Test(groups = "IntegrationTests")
	public void PostcreateANewEngagementType_status409() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle"));
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		postEngagementType.prettyPrint();

		Assert.assertEquals(postEngagementType.statusCode(), 409);

	}

	@Test(groups = "IntegrationTests")
	public void PatchUpdateAnEngagementType_status200() throws IOException {

		/**
		 * 
		 * RETRIVING THE ID
		 * 
		 */
		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum() + engagementType.getRandomNumber(1,20));
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		
		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType/", en);
		postEngagementType.prettyPrint();

		String idValue = postEngagementType.then().extract().path("id").toString();
		System.out.println(idValue);

		/**
		 * 
		 * PERFORMING THE PATCH OPERATION BASED ON THE RETRIVED ID
		 * 
		 */

		String patchId = "/api/engagementType/" + idValue;
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum()+ Math.random());
		en.setOrganization(post.getProperty("postEngagementTypeorganization"));

		Response patchEngagementType = engagementType.patch_URLPOJO(URL, AuthorizationKey, patchId, en);
		patchEngagementType.prettyPrint();

		Assert.assertEquals(patchEngagementType.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void PatchUpdateAnEngagementType_status400() throws IOException {

		/**
		 * 
		 * RETRIVING THE ID
		 * 
		 */

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum()+ Math.random());
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType/", en);
		postEngagementType.prettyPrint();

		String idValue = postEngagementType.then().extract().path("id").toString();
		System.out.println(idValue);

		/**
		 * 
		 * PERFORMING THE PATCH OPERATION BASED ON THE RETRIVED ID
		 * 
		 */
		Engagement_Type_Pojo en1 = new Engagement_Type_Pojo();
		String patchId = "/api/engagementType/" + idValue;
		en1.setTitle("");
		en1.setOrganization(post.getProperty("postEngagementTypeorganization"));

		Response patchEngagementType = engagementType.patch_URLPOJO(URL, AuthorizationKey, patchId, en1);
		patchEngagementType.prettyPrint();

		Assert.assertEquals(patchEngagementType.statusCode(), 400);

	}

	@Test(groups = "IntegrationTests")
	public void PatchUpdateAnEngagementType_status409() throws IOException {

		/**
		 * 
		 * RETRIVING THE ID
		 * 
		 */

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum()+ Math.random());
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType/", en);
		postEngagementType.prettyPrint();

		String idValue = postEngagementType.then().extract().path("id").toString();

		System.out.println(idValue);
		String patchEngagementTypeTitle = postEngagementType.then().extract().path("title").toString();

		/**
		 * 
		 * PERFORMING THE PATCH OPERATION BASED ON THE RETRIVED ID
		 * 
		 */

		String patchId = "/api/engagementType/" + idValue;
		en.setTitle(post.getProperty(patchEngagementTypeTitle));
		en.setOrganization(post.getProperty("postEngagementTypeorganization"));

		Response patchEngagementType = engagementType.patch_URLPOJO(URL, AuthorizationKey, patchId, en);
		patchEngagementType.prettyPrint();

		Assert.assertEquals(patchEngagementType.statusCode(), 409);

	}

	@Test(groups = "IntegrationTests")
	public void deleteAnEngagementType_status204() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey,
				"/api/engagementType","Organization", listOrdId.get(4));
		getEngagementTypeRes.prettyPrint();


		JsonPath jsonPathEvaluator = getEngagementTypeRes.jsonPath();
		listEngagementTypeId = jsonPathEvaluator.get("id");

		String engagementTypeId = listEngagementTypeId.get(listEngagementTypeId.size() - 1);

		String patchId = "/api/engagementType/" + engagementTypeId;

		Response deleteEngagementType = allUtils.delete(URL, AuthorizationKey, patchId);
		deleteEngagementType.prettyPrint();

		Assert.assertEquals(deleteEngagementType.statusCode(), 204);

	}

	@Test(groups = "IntegrationTests")
	public void deleteAnEngagementType_status400() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey,
				"/api/engagementType","Organization", listOrdId.get(4));
		getEngagementTypeRes.prettyPrint();
		
		JsonPath jsonPathEvaluator = getEngagementTypeRes.jsonPath();
		listEngagementTypeId = jsonPathEvaluator.get("id");

		String engagementTypeId = listEngagementTypeId.get(0);

		String patchId = "/api/engagementType/" + engagementTypeId;

		allUtils.delete(URL, AuthorizationKey, patchId);
		Response deleteEngagementType = allUtils.delete(URL, AuthorizationKey, patchId);
		deleteEngagementType.prettyPrint();

		Assert.assertEquals(deleteEngagementType.statusCode(), 400);

	}

	@Test(groups = { "EndToEnd" })
	public void EngagementType_Scenario() throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey,
				"/api/engagementType","Organization", listOrdId.get(4));
		getEngagementTypeRes.prettyPrint();

		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);

		/**
		 * Inserting new records
		 */

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum() + Math.random());
		en.setOrganization(listOrdId.get(4));

		Response postEngagementType = allUtils.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType", en);
		postEngagementType.prettyPrint();

		String idValue = postEngagementType.then().extract().path("id").toString();
		System.out.println(idValue);
		Assert.assertEquals(postEngagementType.statusCode(), 200);

		/**
		 * Updating new created records
		 */

		String patchId = "/api/engagementType/" + idValue;
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
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

	}

}
