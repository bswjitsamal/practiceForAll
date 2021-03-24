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

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.ItemSelectQuestion_Pojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ItemSelectQuestion {

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
	public void getOptionsForASpecificItemSelectQuestionMethodologyItem_status200() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";

		Response getMethodologyRes1 = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void postCreateANewOptionForAnItemSelectQuestion_status200() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		
		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setTitle("Demo");

		Response getMethodologyRes1 = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void postCreateANewOptionForAnItemSelectQuestion_status400() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		
		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setTitle("");

		Response getMethodologyRes1 = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 400);

	}

	@Test(groups = "IntegrationTests")
	public void postCreateANewOptionForAnItemSelectQuestion_status404() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		
		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setTitle("");

		Response getMethodologyRes1 = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 400);

	}
	

	@Test(groups = "IntegrationTests")
	public void putSafelyInitializeNewOptionsForASelectQuestion_status200() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		
		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setOptionTitles(new String[] { "demoTesting" });

		Response getMethodologyRes1 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 200);

	}
	
	
	@Test(groups = "IntegrationTests")
	public void putSafelyInitializeNewOptionsForASelectQuestion_status400() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		
		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setOptionTitles(new String[] {"" });

		Response getMethodologyRes1 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 400);

	}
	
	@Test(groups = "IntegrationTests")
	public void patchUpdateASelectQuestionItemOption_status200() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator0 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator0.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		
		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setTitle("Demo");

		Response getMethodologyRes1 = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> ss =jsonPathEvaluator1.get("id");
		System.out.println(ss);
		
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
		+ "/itemSelectQuestion/" + s1 + "/option/"+ss.get(0);
		
		Response getMethodologyRes2 = getMethodology.patch_URLPOJO(URL, AuthorizationKey, patchId2, isq);
		getMethodologyRes2.prettyPrint();
		
		Assert.assertEquals(getMethodologyRes1.statusCode(), 200);

	}
	
	
	@Test(groups = "IntegrationTests")
	public void patchUpdateASelectQuestionItemOption_status400() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator0 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator0.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		
		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setTitle("");

		Response getMethodologyRes1 = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> ss =jsonPathEvaluator1.get("id");
		System.out.println(ss);
		
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
		+ "/itemSelectQuestion/" + s1 + "/option/"+ss.get(0);
		
		Response getMethodologyRes2 = getMethodology.patch_URLPOJO(URL, AuthorizationKey, patchId2, isq);
		getMethodologyRes2.prettyPrint();
		
		Assert.assertEquals(getMethodologyRes1.statusCode(), 400);

	}
	
	@Test(groups = "IntegrationTests")
	public void deleteAnOptionFromAnItemSelectQuestion_status200() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator0 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator0.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		
		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setTitle("Demo");

		Response getMethodologyRes1 = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> ss =jsonPathEvaluator1.get("id");
		System.out.println(ss);
		
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
		+ "/itemSelectQuestion/" + s1 + "/option/"+ss.get(0);
		
		Response getMethodologyRes2 = getMethodology.delete(URL, AuthorizationKey, patchId2);
		getMethodologyRes2.prettyPrint();
		
		Assert.assertEquals(getMethodologyRes1.statusCode(), 200);

	}
	
	//@Test(groups = "IntegrationTests")
	public void deleteAnOptionFromAnItemSelectQuestion_status400() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator0 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator0.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		
		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setTitle("Demo");

		Response getMethodologyRes1 = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> ss =jsonPathEvaluator1.get("id");
		System.out.println(ss);
		
		String patchId2 = "/api/methodologyItem/revisionss/" + revId.substring(1, revId.length() - 1)
		+ "/itemSelectQuestionss/"  + "/optionss/";
		
		Response getMethodologyRes2 = getMethodology.delete(URL, AuthorizationKey, patchId2);
		getMethodologyRes2.prettyPrint();
		
		Assert.assertEquals(getMethodologyRes1.statusCode(), 400);

	}
	
}
