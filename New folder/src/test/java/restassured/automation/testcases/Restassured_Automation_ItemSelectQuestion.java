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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath jsonEvaluvator2 = getEngagementTypeRes.jsonPath();
		listRevisionI1 = jsonEvaluvator2.get("revisionId");
		listMethodologyId = jsonEvaluvator2.get("methodologyItemId");
		System.out.println(listRevisionI1);

		String patchId1 = "/api/methodologyItem/revision/" + listRevisionI1.get(5) + "/itemSelectQuestion/"
				+ listRevisionI1.get(7) + "/option";
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
	public void ItemSelectQuestion_GetOptionsForASpecificItemSelectQuestionMethodologyItem_status404() {

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));
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
				+ "/itemSelectQuestion/" + s1 + "/opti";
		/**
		 * PEROFRMING THE GET OPERATION
		 */

		Response getMethodologyRes1 = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getMethodologyRes1.prettyPrint();
		// System.out.println("This particular below line is based on Sprint 7 &
		// the Requirement ID : 1008");
		Assert.assertEquals(getMethodologyRes1.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes1.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes1.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes1);

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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
	public void ItemSelectQuestion_PutSafelyInitializeNewOptionsForASelectQuestion_status404() {

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");

		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/opti";

		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setOptionTitles(new String[] { "demoTesting" });

		Response getMethodologyRes1 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.statusCode(), 404);
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		ItemSelectQuestion_Pojo isq1 = new ItemSelectQuestion_Pojo();
		isq1.setOptionTitles(new String[] { "demoTesting" });

		Response getMethodologyRes2 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId2, isq1);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 200);
		ItemSelectQuestion_Pojo isq2 = new ItemSelectQuestion_Pojo();
		isq2.setTitle("Patching option");
		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option/" + ss;
		Response patchOptionRes = getMethodology.patch_URLPOJO(URL, AuthorizationKey, patchId3, isq2);
		patchOptionRes.prettyPrint();
		Assert.assertEquals(patchOptionRes.getStatusCode(), 204);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchOptionRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchOptionRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(patchOptionRes);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_PatchUpdateASelectQuestionItemOption_status404() {

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

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
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/opti";

		Response getMethodologyRes2 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId2, isq1);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 404);
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");

		String MethodId = getEngagementTypeRes.path("find { it.workProgramId }.methodologyItemId");
		System.out.println("Methodology id--------->" + MethodId);
		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId");
		System.out.println(s1);
		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";

		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setTitle("Demo1");

		Response getMethodologyRes1 = getMethodology.post_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();
		Assert.assertEquals(getMethodologyRes1.getStatusCode(), 200);
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";

		/**
		 * PEROFRMING THE GET OPERATION
		 */

		Response getMethodologyRes2 = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, patchId2);
		getMethodologyRes2.prettyPrint();
		// System.out.println("This particular below line is based on Sprint 7 &
		// the Requirement ID : 1008");
		Assert.assertEquals(getMethodologyRes2.statusCode(), 200);
		JsonPath optJson = getMethodologyRes2.jsonPath();
		List<String> oId = optJson.get("id");
		String oId1 = oId.get(oId.size() - 1);
		System.out.println("Fetching option id--------->" + oId1);

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + MethodId + "/option/" + oId1;

		Response getMethodologyRes3 = getMethodology.delete(URL, AuthorizationKey, patchId3);
		getMethodologyRes3.prettyPrint();
		Assert.assertEquals(getMethodologyRes3.statusCode(), 204);
		/**
		 * Extent report generation
		 */

		ExtentTestManager.statusLogMessage(getMethodologyRes3.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes3.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes3);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_DeleteAnOptionFromAnItemSelectQuestion_status404() {

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

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
				+ "/itemSelectQuestion/" + s1 + "/opti/" + ss.get(0);

		Response getMethodologyRes2 = getMethodology.delete(URL, AuthorizationKey, patchId2);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes2.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes2.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes2);

	}

	@Test(groups = "IntegrationTests")
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

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
		String mid = " ";

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options.id");
		JsonPath pa = getEngagementTypeRes.jsonPath();
		List<String> mId = pa.get("methodologyItemId");
		System.out.println("Method Id--------------->" + mId);

		String s1 = getEngagementTypeRes.path("find { it.renderAsSelect == false }.methodologyItemId ");

		System.out.println(s1);

		String patchId1 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";

		ItemSelectQuestion_Pojo isq = new ItemSelectQuestion_Pojo();
		isq.setOptionTitles(new String[] { "API Demo" });

		Response getMethodologyRes1 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId1, isq);
		getMethodologyRes1.prettyPrint();

		Assert.assertEquals(getMethodologyRes1.statusCode(), 200);
		JsonPath optionEvaluvator = getMethodologyRes1.jsonPath();
		List<String> op = optionEvaluvator.get("id");
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo pojo3 = new MethodologyItem_Pojo();
		pojo3.setTitle(post.getProperty("postOptionGroupTitle") + getMethodology.getRandomNumber(1, 20));

		String PatchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup";
		Response methodologyItemRes3 = getMethodology.post_URLPOJO(URL, AuthorizationKey, PatchId4, pojo3);
		methodologyItemRes3.prettyPrint();
		Assert.assertEquals(methodologyItemRes3.getStatusCode(), 200);

		/**
		 * Extent report generation
		 */

		ExtentTestManager.statusLogMessage(methodologyItemRes3.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, methodologyItemRes3.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(methodologyItemRes3);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_CreateANewOptionGroupForAnItemSelectQuestionStatus_400() throws IOException {
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

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
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo pojo3 = new MethodologyItem_Pojo();
		// pojo3.setTitle(post.getProperty("postOptionGroupTitle"));

		String PatchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup";
		Response methodologyItemRes3 = getMethodology.post_URLPOJO(URL, AuthorizationKey, PatchId4, pojo3);
		methodologyItemRes3.prettyPrint();
		Assert.assertEquals(methodologyItemRes3.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(methodologyItemRes3.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, methodologyItemRes3.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(methodologyItemRes3);
	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_CreateANewOptionGroupForAnItemSelectQuestionStatus_404() throws IOException {
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

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
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo pojo3 = new MethodologyItem_Pojo();
		pojo3.setTitle(post.getProperty("postOptionGroupTitle") + getMethodology.getRandomNumber(1, 20));

		String PatchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGro";
		Response methodologyItemRes3 = getMethodology.post_URLPOJO(URL, AuthorizationKey, PatchId4, pojo3);
		methodologyItemRes3.prettyPrint();
		Assert.assertEquals(methodologyItemRes3.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(methodologyItemRes3.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, methodologyItemRes3.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(methodologyItemRes3);
	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_UpdateASelectQuestionItemOptionGroup_204() throws IOException {
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo pojo3 = new MethodologyItem_Pojo();
		pojo3.setTitle(post.getProperty("postOptionGroupTitle") + getMethodology.getRandomNumber(1, 20));

		String PatchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup";
		Response methodologyItemRes3 = getMethodology.post_URLPOJO(URL, AuthorizationKey, PatchId4, pojo3);
		methodologyItemRes3.prettyPrint();
		Assert.assertEquals(methodologyItemRes3.getStatusCode(), 200);
		JsonPath optionGroupId = methodologyItemRes3.jsonPath();
		String optionGrpId = optionGroupId.get("id");
		System.out.println("Option Group id----------------->" + optionGrpId);
		// String optionGrpId1=String.valueOf(optionGrpId.get(1));

		Response getOptionRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getOptionRes.prettyPrint();
		Assert.assertEquals(getOptionRes.getStatusCode(), 200);
		JsonPath optionJson = getOptionRes.jsonPath();
		List<String> optionId = optionJson.get("id");
		String optId = String.valueOf(optionId.get(1));
		System.out.println("Option id--------------->" + optId);

		/**
		 * Update the option group
		 */
		ItemSelectQuestion_Pojo pojo = new ItemSelectQuestion_Pojo();
		pojo.setOptions(new String[] { optId });
		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup/" + optionGrpId;

		Response optionGroupRes = getEngagementType.patch_URLPOJO(URL, AuthorizationKey, patchId5, pojo);
		optionGroupRes.prettyPrint();
		Assert.assertEquals(optionGroupRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(optionGroupRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, optionGroupRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(optionGroupRes);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_UpdateASelectQuestionItemOptionGroup_404() throws IOException {
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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
		 * create an new option group
		 */
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo pojo3 = new MethodologyItem_Pojo();
		pojo3.setTitle(post.getProperty("postOptionGroupTitle") + getMethodology.getRandomNumber(1, 20));

		String PatchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup";
		Response methodologyItemRes3 = getMethodology.post_URLPOJO(URL, AuthorizationKey, PatchId4, pojo3);
		methodologyItemRes3.prettyPrint();
		Assert.assertEquals(methodologyItemRes3.getStatusCode(), 200);
		JsonPath optionGroupId = methodologyItemRes3.jsonPath();
		String optionGrpId = optionGroupId.get("id");
		System.out.println("Option Group id----------------->" + optionGrpId);
		// String optionGrpId1=String.valueOf(optionGrpId.get(1));

		Response getOptionRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getOptionRes.prettyPrint();
		Assert.assertEquals(getOptionRes.getStatusCode(), 200);
		JsonPath optionJson = getOptionRes.jsonPath();
		List<String> optionId = optionJson.get("id");
		String optId = String.valueOf(optionId.get(1));
		System.out.println("Option id--------------->" + optId);

		/**
		 * Update the option group
		 */
		ItemSelectQuestion_Pojo pojo = new ItemSelectQuestion_Pojo();
		pojo.setOptions(new String[] { optId });
		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGro/" + optionGrpId;

		Response optionGroupRes = getEngagementType.patch_URLPOJO(URL, AuthorizationKey, patchId5, pojo);
		optionGroupRes.prettyPrint();
		Assert.assertEquals(optionGroupRes.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(optionGroupRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, optionGroupRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(optionGroupRes);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_UpdateASelectQuestionItemOptionGroup_400() throws IOException {
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo pojo3 = new MethodologyItem_Pojo();
		pojo3.setTitle(post.getProperty("postOptionGroupTitle") + getMethodology.getRandomNumber(1, 20));

		String PatchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup";
		Response methodologyItemRes3 = getMethodology.post_URLPOJO(URL, AuthorizationKey, PatchId4, pojo3);
		methodologyItemRes3.prettyPrint();
		Assert.assertEquals(methodologyItemRes3.getStatusCode(), 200);
		JsonPath optionGroupId = methodologyItemRes3.jsonPath();
		String optionGrpId = optionGroupId.get("id");
		System.out.println("Option Group id----------------->" + optionGrpId);
		// String optionGrpId1=String.valueOf(optionGrpId.get(1));

		Response getOptionRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getOptionRes.prettyPrint();
		Assert.assertEquals(getOptionRes.getStatusCode(), 200);
		JsonPath optionJson = getOptionRes.jsonPath();
		List<String> optionId = optionJson.get("id");
		String optId = String.valueOf(optionId.get(1));
		System.out.println("Option id--------------->" + optId);

		/**
		 * Update the option group
		 */
		ItemSelectQuestion_Pojo pojo = new ItemSelectQuestion_Pojo();
		pojo.setOptions(new String[] {
				(String) post.get("putOptionGroupBadRequest") + getEngagementType.getRandomNumber(1, 20) });
		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup/" + optionGrpId;

		Response optionGroupRes = getEngagementType.patch_URLPOJO(URL, AuthorizationKey, patchId5, pojo);
		optionGroupRes.prettyPrint();
		Assert.assertEquals(optionGroupRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(optionGroupRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, optionGroupRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(optionGroupRes);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_DeleteAnOptionGroupFromAnItemSelectQuestion_204() throws IOException {
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo pojo3 = new MethodologyItem_Pojo();
		pojo3.setTitle(post.getProperty("postOptionGroupTitle") + getMethodology.getRandomNumber(1, 20));

		String PatchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup";
		Response methodologyItemRes3 = getMethodology.post_URLPOJO(URL, AuthorizationKey, PatchId4, pojo3);
		methodologyItemRes3.prettyPrint();
		Assert.assertEquals(methodologyItemRes3.getStatusCode(), 200);
		JsonPath optionGroupId = methodologyItemRes3.jsonPath();
		String optionGrpId = optionGroupId.get("id");
		System.out.println("Option Group id----------------->" + optionGrpId);
		// String optionGrpId1=String.valueOf(optionGrpId.get(1));

		Response getOptionRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getOptionRes.prettyPrint();
		Assert.assertEquals(getOptionRes.getStatusCode(), 200);
		JsonPath optionJson = getOptionRes.jsonPath();
		List<String> optionId = optionJson.get("id");
		String optId = String.valueOf(optionId.get(1));
		System.out.println("Option id--------------->" + optId);

		/**
		 * Delete the option group
		 */

		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup/" + optionGrpId;

		Response optionGroupRes = getEngagementType.delete(URL, AuthorizationKey, patchId5);
		optionGroupRes.prettyPrint();
		Assert.assertEquals(optionGroupRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(optionGroupRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, optionGroupRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(optionGroupRes);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_DeleteAnOptionGroupFromAnItemSelectQuestion_404() throws IOException {
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo pojo3 = new MethodologyItem_Pojo();
		pojo3.setTitle(post.getProperty("postOptionGroupTitle") + getMethodology.getRandomNumber(1, 20));

		String PatchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup";
		Response methodologyItemRes3 = getMethodology.post_URLPOJO(URL, AuthorizationKey, PatchId4, pojo3);
		methodologyItemRes3.prettyPrint();
		Assert.assertEquals(methodologyItemRes3.getStatusCode(), 200);
		JsonPath optionGroupId = methodologyItemRes3.jsonPath();
		String optionGrpId = optionGroupId.get("id");
		System.out.println("Option Group id----------------->" + optionGrpId);
		// String optionGrpId1=String.valueOf(optionGrpId.get(1));

		Response getOptionRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getOptionRes.prettyPrint();
		Assert.assertEquals(getOptionRes.getStatusCode(), 200);
		JsonPath optionJson = getOptionRes.jsonPath();
		List<String> optionId = optionJson.get("id");
		String optId = String.valueOf(optionId.get(1));
		System.out.println("Option id--------------->" + optId);

		/**
		 * Delete the option group
		 */

		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGro/" + optionGrpId;

		Response optionGroupRes = getEngagementType.delete(URL, AuthorizationKey, patchId5);
		optionGroupRes.prettyPrint();
		Assert.assertEquals(optionGroupRes.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(optionGroupRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, optionGroupRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(optionGroupRes);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_DeleteAnOptionGroupFromAnItemSelectQuestion_400() throws IOException {
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));
		
		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath methJson=getEngagementTypeRes.jsonPath();
		listMethodologyId=methJson.get("methodologyItemId");
		String mid=listMethodologyId.get(0);
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
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo pojo3 = new MethodologyItem_Pojo();
		pojo3.setTitle(post.getProperty("postOptionGroupTitle") + getMethodology.getRandomNumber(1, 20));

		String PatchId4 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/optionGroup";
		Response methodologyItemRes3 = getMethodology.post_URLPOJO(URL, AuthorizationKey, PatchId4, pojo3);
		methodologyItemRes3.prettyPrint();
		Assert.assertEquals(methodologyItemRes3.getStatusCode(), 200);
		JsonPath optionGroupId = methodologyItemRes3.jsonPath();
		String optionGrpId = optionGroupId.get("id");
		System.out.println("Option Group id----------------->" + optionGrpId);
		// String optionGrpId1=String.valueOf(optionGrpId.get(1));

		Response getOptionRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getOptionRes.prettyPrint();
		Assert.assertEquals(getOptionRes.getStatusCode(), 200);
		JsonPath optionJson = getOptionRes.jsonPath();
		List<String> optionId = optionJson.get("id");
		String optId = String.valueOf(optionId.get(1));
		System.out.println("Option id--------------->" + optId);

		/**
		 * Delete the option group
		 */

		String patchId5 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + mid + "/optionGroup/" + optionGrpId;

		Response optionGroupRes = getEngagementType.delete(URL, AuthorizationKey, patchId5);
		optionGroupRes.prettyPrint();
		Assert.assertEquals(optionGroupRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(optionGroupRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, optionGroupRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getEngagementType.validate_HTTPStrictTransportSecurity(optionGroupRes);

	}

	
	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_MoveASelectQuestionFromItemOption_status200() {

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		ItemSelectQuestion_Pojo isq1 = new ItemSelectQuestion_Pojo();
		isq1.setOptionTitles(new String[] { "demoTesting" });

		Response getMethodologyRes2 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId2, isq1);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 200);
		ItemSelectQuestion_Pojo isq2 = new ItemSelectQuestion_Pojo();
		isq2.setIndex("1");
		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/moveOption/" + ss;
		Response patchOptionRes = getMethodology.patch_URLPOJO(URL, AuthorizationKey, patchId3, isq2);
		patchOptionRes.prettyPrint();
		Assert.assertEquals(patchOptionRes.getStatusCode(), 204);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchOptionRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchOptionRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(patchOptionRes);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_MoveASelectQuestionFromItemOption_status400() {

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));

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
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		ItemSelectQuestion_Pojo isq1 = new ItemSelectQuestion_Pojo();
		isq1.setOptionTitles(new String[] { "demoTesting" });

		Response getMethodologyRes2 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId2, isq1);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 200);
		ItemSelectQuestion_Pojo isq2 = new ItemSelectQuestion_Pojo();
		isq2.setIndex("abc");
		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/moveOption/" + ss;
		Response patchOptionRes = getMethodology.patch_URLPOJO(URL, AuthorizationKey, patchId3, isq2);
		patchOptionRes.prettyPrint();
		Assert.assertEquals(patchOptionRes.getStatusCode(), 400);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchOptionRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchOptionRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(patchOptionRes);

	}

	@Test(groups = "IntegrationTests")
	public void ItemSelectQuestion_MoveASelectQuestionFromItemOption_status404() {

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

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
		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/option";
		ItemSelectQuestion_Pojo isq1 = new ItemSelectQuestion_Pojo();
		isq1.setOptionTitles(new String[] { "demoTesting" });

		Response getMethodologyRes2 = getMethodology.put_URLPOJO(URL, AuthorizationKey, patchId2, isq1);
		getMethodologyRes2.prettyPrint();
		Assert.assertEquals(getMethodologyRes2.statusCode(), 200);
		ItemSelectQuestion_Pojo isq2 = new ItemSelectQuestion_Pojo();
		isq2.setIndex("1");
		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + s1 + "/moveOption/";
		Response patchOptionRes = getMethodology.patch_URLPOJO(URL, AuthorizationKey, patchId3, isq2);
		patchOptionRes.prettyPrint();
		Assert.assertEquals(patchOptionRes.getStatusCode(), 404);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchOptionRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchOptionRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodology.validate_HTTPStrictTransportSecurity(patchOptionRes);

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(7));
		/**
		 * FETCHING THE METHODOLOGY ID FROM METHODOLOGY ITEM
		 */
		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		List<String> s = getEngagementTypeRes.body().path("options");
		String MethodId = getEngagementTypeRes.path("find { it.workProgramId }.methodologyItemId");
		System.out.println("Methodology id--------->" + MethodId);
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
		JsonPath optJson = getMethodologyRes3.jsonPath();
		List<String> oId = optJson.get("id");
		String oId1 = oId.get(oId.size() - 1);
		System.out.println("Fetching option id--------->" + oId1);

		/**
		 * PERFORMING THE DELETE OPERATION ---->Delete an option from an
		 * ItemSelectQuestion
		 */
		JsonPath jsonPathEvaluator2 = getMethodologyRes3.jsonPath();
		List<String> ss = jsonPathEvaluator2.get("id");
		System.out.println(ss);

		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1)
				+ "/itemSelectQuestion/" + MethodId + "/option/" + oId1;

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
