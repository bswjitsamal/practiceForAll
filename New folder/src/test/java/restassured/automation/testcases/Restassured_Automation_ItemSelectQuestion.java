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

import com.aventstack.extentreports.Status;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import restassured.automation.Pojo.ItemSelectQuestion_Pojo;
import restassured.automation.Pojo.MethodologyItem_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ItemSelectQuestion {

	String URL;
	String AuthorizationKey;
	List<String> listRevisionId;
	List<String> listOrdId;
	List<String> listMethodologyId;

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
	public void ItemSelectQuestion_GetOptionsForASpecificItemSelectQuestionMethodologyItem_status200() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

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

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));
		/**
		 * FETCHING THE ALL METHODOLOGY FOR AN REVISION
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");

		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		/**
		 * PEROFRMING THE GET OPERATION
		 */

		Response getMethodologyRes1 = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getMethodologyRes1.prettyPrint();
		// System.out.println("This particular below line is based on Sprint 7 &
		// the Requirement ID : 1008");
		Assert.assertEquals(getMethodologyRes1.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes1.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes1.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes1);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_GetOptionsForASpecificItemSelectQuestionMethodologyItem_status400() {

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath jsonEvaluvator2 = getEngagementTypeRes.jsonPath();
		listRevisionI1 = jsonEvaluvator2.get("revisionId");
		listMethodologyId = jsonEvaluvator2.get("methodologyItemId");
		System.out.println(listRevisionI1);

		String patchId1 = "/api/methodologyItem/revision/" + listRevisionI1.get(5) + "/itemSelectQuestion/"
				+ listMethodologyId.get(7) + "/option";
		/**
		 * PEROFRMING THE GET OPERATION
		 */

		Response getMethodologyRes1 = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getMethodologyRes1.prettyPrint();
		// System.out.println("This particular below line is based on Sprint 7 &
		// the Requirement ID : 1008");
		Assert.assertEquals(getMethodologyRes1.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes1.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes1.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(getMethodologyRes1);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_PostCreateANewOptionForAnItemSelectQuestion_status200() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		/**
		 * fetcching the org id
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

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
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes1.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes1.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes1);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_PostCreateANewOptionForAnItemSelectQuestion_status400() {

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

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
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes1.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes1.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes1);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_PostCreateANewOptionForAnItemSelectQuestion_status404() {

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

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
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes1.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes1.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes1);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_PutSafelyInitializeNewOptionsForASelectQuestion_status200() {

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

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
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes1.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes1.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes1);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_PutSafelyInitializeNewOptionsForASelectQuestion_status400() {

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

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
		isq.setOptionTitles(new String[] { "" });

		Response getMethodologyRes1 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes1.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes1.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes1);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_PatchUpdateASelectQuestionItemOption_status200() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		/**
		 * FETCHING THE ORGANISATION ID
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator0 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator0.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */
		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

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

		JsonPath jsonPathEvaluator1 = getMethodologyRes1.jsonPath();
		String ss = jsonPathEvaluator1.get("id");
		System.out.println(ss);

		ItemSelectQuestion_Pojo isq1 = new ItemSelectQuestion_Pojo();
		isq1.setOptionTitles(new String[] { "demoTesting" });

		Response getMethodologyRes2 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId1, isq1);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes2.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes2.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes2);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_PatchUpdateASelectQuestionItemOption_status400() {

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

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
		List<String> ss = jsonPathEvaluator1.get("id");
		System.out.println(ss);

		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option/" + ss.get(0);

		Response getMethodologyRes2 = getMethodology.patch_URLPOJO(URL, AuthorizationKey, patchId2, isq);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes2.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes2.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes2);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_DeleteAnOptionFromAnItemSelectQuestion_status204() {

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

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
		List<String> ss = jsonPathEvaluator1.get("id");
		System.out.println(ss);

		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option/" + ss.get(0);

		Response getMethodologyRes2 = getMethodology.delete(URL, AuthorizationKey, patchId2);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes2.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes2.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes2);

	}

	// @Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_DeleteAnOptionFromAnItemSelectQuestion_status400() {

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

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
		List<String> ss = jsonPathEvaluator1.get("id");
		System.out.println(ss);

		String patchId2 = "/api/methodologyItem/revisionss/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestionss/" + "/optionss/";

		Response getMethodologyRes2 = getMethodology.delete(URL, AuthorizationKey, patchId2);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes2.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes2.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes2);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_CreateANewOptionGroupForAnItemSelectQuestionStatus_200() throws IOException {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		/**
		 * FETCHING THE ORGANISATION DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator0 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator0.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE LIST OF METHODOLOGY WRT TO ORGANISATION
		 */

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();
		/**
		 * CREATING THE NEW METHODOLOGY ITEM FOR THE PARTICULAR MEHTODOLOGY
		 */

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		listMethodologyId = jsonPathEvaluator.get("id");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");
		System.out.println("Methodology id----------->" + listMethodologyId);
		System.out.println("Revision id--------->" + listRevisionI1);

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle") + getRandomAlphaNum()
				+ getMethodology.getRandomNumber(1, 40));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));
		mi.setParentId(post.getProperty(listMethodologyId.get(4)));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();
		JsonPath jsonEvaluator1 = postMethodologyItem.jsonPath();
		String methodId = jsonEvaluator1.get("methodologyItemId");
		System.out.println(methodId);
		/**
		 * CREATE A NEW WORK PROGRAM WRT TO METHODOLOGY ITEM
		 */
		MethodologyItem_Pojo pojo = new MethodologyItem_Pojo();
		pojo.setTitle(post.getProperty("postMethodologyItemTitle") + getMethodology.getRandomNumber(1, 50));
		pojo.setParentId(methodId);
		pojo.setIndex(post.getProperty("postMethodologyItemIndex"));
		pojo.setItemType(post.getProperty("postMethodologyItemType"));

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Response methodologyItemRes = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, pojo);
		methodologyItemRes.prettyPrint();
		Assert.assertEquals(methodologyItemRes.getStatusCode(), 200);
		/**
		 * initializing the work program
		 */
		JsonPath jsonEvaluator = methodologyItemRes.jsonPath();
		String reviId = jsonEvaluator.get("revisionId");
		String methodId1 = jsonEvaluator.get("methodologyItemId");
		System.out.println("revision id---------->"+revId);
		System.out.println("methodId-------->"+methodId);
		
		MethodologyItem_Pojo pojo1 = new MethodologyItem_Pojo();
		pojo1.setRuleContextType(post.getProperty("postRuleContextType"));
		pojo1.setRelationshipType(post.getProperty("postRelationshipType"));
		
		String patchId2= "/api/methodologyItem/revision/"+reviId +"/workProgram/"+methodId1 +"/initialize";
		
		Response workInitializeRes=getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId2, pojo1);

		Assert.assertEquals(workInitializeRes.getStatusCode(), 200);

	}

	@Test(groups = { "EndToEnd" })
	public void ItemSelectQuestion_EndToEnd_Scenario() {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		/**
		 * FETCHING THE ORGANISATION ID
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * FETCHING THE REVISION ID FROM METHODOLOGY
		 */

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));
		/**
		 * FETCHING THE METHODOLOGY ID FROM METHODOLOGY ITEM
		 */
		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");

		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);
		/**
		 * PERFORMING POST OPERATION --->Create a new option for an
		 * ItemSelectQuestion
		 */

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";

		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setTitle("Demo");

		Response getMethodologyRes1 = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 200);

		/**
		 * PERFORMING PUT OPERATION ---->Safely initializes new options for a
		 * select question.
		 */

		ItemSelectQuestion_Pojo isq1 = new ItemSelectQuestion_Pojo();
		isq1.setOptionTitles(new String[] { "demoTesting" });

		Response getMethodologyRes2 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId1, isq1);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 200);

		/**
		 * PERFORMING GET OPERATION ------>Get options for a specific
		 * ItemSelectQuestion methodology item
		 */
		Response getMethodologyRes3 = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getMethodologyRes3.prettyPrint();
		Assert.assertEquals(getMethodologyRes3.statusCode(), 200);

		/**
		 * PERFORMING THE DELETE OPERATION ---->Delete an option from an
		 * ItemSelectQuestion
		 */
		JsonPath jsonPathEvaluator2 = getEngagementTypeRes.jsonPath();
		List<String> ss = jsonPathEvaluator2.get("id");
		System.out.println(ss);

		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option/" + ss.get(0);

		Response getMethodologyRes4 = getMethodology.delete(URL, AuthorizationKey, patchId2);
		getMethodologyRes4.prettyPrint();
		Assert.assertEquals(getMethodologyRes4.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes4.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes4.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes4);
	}

}
