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
import restassured.automation.Pojo.MethodologyItem_Pojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_MethodologItem {

	String URL;
	String AuthorizationKey;
	List<String> listRevisionId;
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
	
	@Test(groups = "IntegrationTests")
	public void getAllMethodologyItemsForARevision_status404() {

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

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology","Organization",listOrdId.get(4));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));
		String id = revId.substring(1, revId.length() - 1);
		String subId = id.substring(8, 12);

		String patchId = "/api/methodologyItem/revision/";

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		Assert.assertEquals(getEngagementTypeRes.statusCode(), 404);

	}
	

	@Test(groups = "IntegrationTests")
	public void getAllMethodologyItemsForARevision_status400() {

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

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));
		String id = revId.substring(1, revId.length() - 1);
		String subId = id.substring(8, 12);

		String patchId = "/api/methodologyItem/revision/" + subId + getRandomAlphaNum();

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		Assert.assertEquals(getEngagementTypeRes.statusCode(), 400);

	}
	
	@Test(groups = "IntegrationTests")
	public void getAllMethodologyItemsForARevision_status200() {

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));
		
		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();

		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void postCreateANewMethodologyItemWithinTheMethodologyTree_status200()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		
		//fetching Org Id
		
		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology","Organization",listOrdId.get(4));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

	}

	@Test(groups = "IntegrationTests")
	public void postCreateANewMethodologyItemWithinTheMethodologyTree_status400()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemBadTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 400);

	}
	
	@Test(groups = "IntegrationTests")
	public void postCreateANewMethodologyItemWithinTheMethodologyTree_status404()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, "/api/methodology");
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revisionss/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemBadTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 404);

	}

	@Test(groups = "IntegrationTests")
	public void putMoveATreeItemToAFolderAtSpecifiedIndex_status204()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/move/"
				+ methodologyItemId.substring(1, revId.length() - 1);

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.put_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();

		Assert.assertEquals(putMethodologyItem.statusCode(), 204);

	}

	@Test(groups = "IntegrationTests")
	public void putMoveATreeItemToAFolderAtSpecifiedIndex_status400()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/move/"
				+ methodologyItemId.substring(1, revId.length() - 1);

		mi.setTitle(post.getProperty("putMethodologyItemBadTitle"));
		mi.setParentId(post.getProperty("putMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.put_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();

		Assert.assertEquals(putMethodologyItem.statusCode(), 400);

	}
	
	@Test(groups = "IntegrationTests")
	public void patchUpdateAMethodologyItemForTheRevision_status200()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item/"
				+ methodologyItemId.substring(1, revId.length() - 1);

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();

		Assert.assertEquals(putMethodologyItem.statusCode(), 204);

	}
	
	@Test(groups = "IntegrationTests")
	public void patchUpdateAMethodologyItemForTheRevision_status400()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item/"
				+ methodologyItemId.substring(1, revId.length() - 1);

		mi.setTitle(post.getProperty("putMethodologyItemBadTitle"));
		mi.setParentId(post.getProperty("putMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.patch_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();

		Assert.assertEquals(putMethodologyItem.statusCode(), 400);

	}
	
	
	@Test(groups = "IntegrationTests")
	public void patchUpdateAMethodologyItemForTheRevision_status404()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

	}
	
	
	@Test(groups = "IntegrationTests")
	public void postInitialiseAWorkProgram_status204()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

		String methodologyItemId = postMethodologyItem.asString();
		System.out.println("---->" + methodologyItemId);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/workProgram/"
				+ methodologyItemId.substring(1, revId.length() - 1)+"/initialize";

		mi.setTitle(post.getProperty("putMethodologyItemTitle"));
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();

		Assert.assertEquals(putMethodologyItem.statusCode(), 204);

	}
	
	@Test(groups = "IntegrationTests")
	public void postInitialiseAWorkProgram_status400()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

		String patchId1 = "/api/methodologyItem/revisionss/" + revId.substring(1, revId.length() - 1)
				+ "/workProgramss/" + methodologyItemId.substring(1, methodologyItemId.length() - 1) + "initialize";

		mi.setTitle(post.getProperty("putMethodologyItemBadTitle"));
		mi.setParentId(post.getProperty("putMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Response putMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId1, mi);
		putMethodologyItem.prettyPrint();

		Assert.assertEquals(putMethodologyItem.statusCode(), 400);

	}
	
	
	@Test(groups = "IntegrationTests")
	public void postInitialiseAWorkProgram_status404()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

	}
	
	@Test(groups = "IntegrationTests")
	public void deleteMarkAnItemAsDeleted_status200()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item/"
				+ methodologyItemId.substring(1, methodologyItemId.length() - 1);


		Response putMethodologyItem = MethodologyItem.delete(URL, AuthorizationKey, patchId1);
		putMethodologyItem.prettyPrint();

		Assert.assertEquals(putMethodologyItem.statusCode(), 204);

	}
	
	@Test(groups = "IntegrationTests")
	public void deleteMarkAnItemAsDeleted_status400()
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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

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

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item/"
				+ methodologyItemId.substring(1, methodologyItemId.length() - 1);

		MethodologyItem.delete(URL, AuthorizationKey, patchId1);
		Response putMethodologyItem = MethodologyItem.delete(URL, AuthorizationKey, patchId1);
		putMethodologyItem.prettyPrint();

		Assert.assertEquals(putMethodologyItem.statusCode(), 400);

	}
	
	
	
}
