package restassured.automation.testcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.math3.ml.distance.CanberraDistance;
import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.MethodologyItem_Pojo;
import restassured.automation.Pojo.Methodology_Pojo;
import restassured.automation.Pojo.User_Pojo;

import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Excel_Data_Source_Utils;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Methodology {

	String URL;
	String AuthorizationKey;
	List<String> methodologyId;
	List<String> methodologyId1;
	List<String> engagementTypeId;
	List<String> organizationId;
	List<String> listOrdId;
	List<String> listRevision;
	List<String> listDraftDescription;
	List<String> methodlogyItemId;
	List<String> title;
	Restassured_Automation_Utils allUtils;
	Properties post;

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 5);
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
	public void Methodology_GetListAllMethodologiesTheUserHasAccessTo_Status200() {

		allUtils = new Restassured_Automation_Utils();

		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		Assert.assertEquals(getMethodologyRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_GetDetialsAboutASpecificMethodology_status200() {

		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		System.out.println("methodologyId---------------->" + methodologyId.get(2));

		Response getMethodologyByIdRes = allUtils.get_URL_WithOne_PathParams(URL, AuthorizationKey,
				"/api/methodology/{value}", methodologyId.get(2));
		getMethodologyByIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_GetASpecificRevisionItemTree_status200() {

		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		listRevision = jsonPathEvaluator1.get("revisions");

		// System.out.println(String.valueOf(listRevision.get(0)));

		String revId = String.valueOf(listRevision.get(2));
		System.out.println("revision-------->" + revId);

		System.out.println(methodologyId);

		String patchId = "/api/methodology/" + methodologyId.get(2) + "/revision/" + revId.substring(1, 25);

		Response getMethodologyByrevisonIdRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_GetASpecificRevisionItemTree_status404() {

		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * FETCHING METHODOLOGY FROM ORGANISATION - 1
		 */

		Response getMethodologyRes1 = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));
		getMethodologyRes1.prettyPrint();

		JsonPath jsonPathEvaluator2 = getMethodologyRes1.jsonPath();
		methodologyId1 = jsonPathEvaluator2.get("id");

		/**
		 * FETCHING REVISION ID FROM ORGANISATION - 2
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		listRevision = jsonPathEvaluator1.get("revisions");

		// System.out.println(String.valueOf(listRevision.get(0)));

		String revId = String.valueOf(listRevision.get(2));

		System.out.println(methodologyId);

		String patchId = "/api/methodology/" + methodologyId1.get(0) + "/revisionss/" + revId.substring(1, 25);

		Response getMethodologyByrevisonIdRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	// @Test(groups = "IntegrationTests")
	public void Methodology_PostAddANewMethodology_status200() throws IOException {
		post = read_Configuration_Propertites.loadproperty("Configuration");
		allUtils = new Restassured_Automation_Utils();

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> eType = jsonPathEvaluator1.get("engagementType");
		String engagementType = eType.get(0);
		/*
		 * ArrayList<Map<String, ?>> listRevisionI1 =
		 * jsonPathEvaluator1.get("revisions.id");
		 * 
		 * ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		 * System.out.println(String.valueOf(listRevisionI1.get(0)));
		 * 
		 * String revId = String.valueOf(listRevisionI1.get(9)); String parntId
		 * = String.valueOf(parentId.get(0));
		 */

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postMethodologyTitle") + allUtils.getRandomNumber(1, 30));
		map.put("engagementType", engagementType);
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription"));

		User_Pojo po = new User_Pojo();
		String createMethodology = po.methodologyAdd(map);

		String postMethodologyId = "/api/methodology";
		Response methodRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postMethodologyId, createMethodology);
		methodRes.prettyPrint();
		Assert.assertEquals(methodRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(methodRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, methodRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(methodRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_PostAddANewMethodology_status400() throws IOException {

		post = read_Configuration_Propertites.loadproperty("Configuration");

		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("engagementType");
		organizationId = jsonPathEvaluator.get("organization");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId1 = String.valueOf(listRevisionI1.get(2));
		String revId = revId1.substring(1, 25);

		System.out.println(engagementTypeId);
		System.out.println(revId);

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", "  ");
		map.put("engagementType", engagementTypeId.get(0));
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription"));

		User_Pojo po = new User_Pojo();
		String createMethodology = po.methodologyAdd(map);

		Response getMethodologyByrevisonIdRes = allUtils.post_URLPOJO(URL, AuthorizationKey, "/api/methodology",
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_PostAddANewMethodology_status409() throws IOException {

		post = read_Configuration_Propertites.loadproperty("Configuration");
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		engagementTypeId = jsonPathEvaluator.get("engagementType");
		organizationId = jsonPathEvaluator.get("organization");
		title = jsonPathEvaluator.get("title");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId1 = String.valueOf(listRevisionI1.get(2));
		String revId = revId1.substring(1, 25);

		System.out.println(engagementTypeId);
		System.out.println(revId);

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", title.get(1));
		map.put("engagementType", engagementTypeId.get(1));
		// map.put("draftDescription",
		// post.getProperty("postMethodologyDraftDescription"));

		User_Pojo po = new User_Pojo();
		String createMethodology = po.methodologyAdd(map);

		Response getMethodologyByrevisonIdRes = allUtils.post_URLPOJO(URL, AuthorizationKey, "/api/methodology",
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_PostAddANewMethodology_status404() throws IOException {

		post = read_Configuration_Propertites.loadproperty("Configuration");
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		engagementTypeId = jsonPathEvaluator.get("engagementType");
		organizationId = jsonPathEvaluator.get("organization");
		title = jsonPathEvaluator.get("title");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId1 = String.valueOf(listRevisionI1.get(2));
		String revId = revId1.substring(1, 25);

		System.out.println(engagementTypeId);
		System.out.println(revId);

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", title.get(0));
		map.put("engagementType", engagementTypeId.get(0));
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription"));

		User_Pojo po = new User_Pojo();
		String createMethodology = po.methodologyAdd(map);

		Response getMethodologyByrevisonIdRes = allUtils.post_URLPOJO(URL, AuthorizationKey, "/api/methodologyss",
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_PatchUpdateAMethodology_status400() throws IOException {

		post = read_Configuration_Propertites.loadproperty("Configuration");
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		methodologyId = jsonPathEvaluator.get("id");
		String patchId = "/api/methodology/" + methodologyId.get(2);

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("patchMethodologyTitle"));

		User_Pojo po = new User_Pojo();
		String createMethodology = po.methodologyAdd(map);

		Response getMethodologyByrevisonIdRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	// @Test(groups = "IntegrationTests")
	public void Methodology_PatchUpdateAMethodology_status204() throws IOException {

		post = read_Configuration_Propertites.loadproperty("Configuration");
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		methodologyId = jsonPathEvaluator.get("id");
		String patchId = "/api/methodology/" + methodologyId.get(2);

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postMethodologyTitle") + allUtils.getRandomNumber(1, 30));

		User_Pojo po = new User_Pojo();
		String createMethodology = po.methodologyAdd(map);

		Response getMethodologyByrevisonIdRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	// @Test(groups = "IntegrationTests")
	public void Methodology_PatchUpdateASpecificMethodologiesWith_Status_204() throws IOException {
		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		listRevision = jsonPathEvaluator1.get("revisions");
		// System.out.println(String.valueOf(listRevision.get(0)));
		System.out.println("List---->" + listRevision);
		String revId = String.valueOf(listRevision.get(2));
		System.out.println("Revision id------>" + revId);
		System.out.println(methodologyId);
		listDraftDescription = jsonPathEvaluator1.get("revisions.draftDescription");

		Map<String, String> map = new HashMap<String, String>();
		map.put("draftDescription",
				post.getProperty("postMethodologyDraftDescription") + allUtils.getRandomNumber(1, 20));
		User_Pojo po = new User_Pojo();
		String createMethodology = po.methodologyAdd(map);

		String patchId = "/api/methodology/" + methodologyId.get(1) + "/revision/" + revId.substring(1, 25);

		Response getMethodologyByrevisonIdRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_PatchUpdateASpecificMethodologiesWith_Status_404() throws IOException {
		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");

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
		methodologyId = jsonPathEvaluator1.get("id");
		listRevision = jsonPathEvaluator1.get("revisions");
		// System.out.println(String.valueOf(listRevision.get(0)));
		System.out.println("List---->" + listRevision);
		String revId = String.valueOf(listRevision.get(2));
		System.out.println("Revision id------>" + revId);
		System.out.println(methodologyId);
		listDraftDescription = jsonPathEvaluator1.get("revisions");
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Map<String, String> map = new HashMap<String, String>();
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription1"));

		User_Pojo po = new User_Pojo();
		String createMethodology = po.methodologyAdd(map);

		String patchId = "/api/methodology/" + methodologyId.get(2) + "/revision/" + methodologyId.get(2);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();
		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_PatchUpdateASpecificMethodologiesWith_Status_400() throws IOException {
		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		listRevision = jsonPathEvaluator1.get("revisions");
		// System.out.println(String.valueOf(listRevision.get(0)));
		System.out.println("List---->" + listRevision);
		String revId = String.valueOf(listRevision.get(2));
		System.out.println("Revision id------>" + revId);
		System.out.println(methodologyId);

		Map<String, String> map = new HashMap<String, String>();
		map.put("importing", post.getProperty("patchSpecificMethodologyBad"));
		User_Pojo po = new User_Pojo();
		String createMethodology = po.Methodology(map);

		String patchId = "/api/methodology/" + methodologyId.get(1) + "/revision/" + revId.substring(1, 25);

		Response getMethodologyByrevisonIdRes = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology);

		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_CreateANewCandidateRevisionWithStatus_200() throws IOException {
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		String parntId = String.valueOf(parentId.get(6));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		// Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
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

		String methodologyItemId1 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("displayAs", post.getProperty("patchWorkProgramType1"));
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		// Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType1"));
		data2.put("ruleContextType", post.getProperty("postRuleContextType2"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId2 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam;

		Response postWorkFlowRes2 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId2);
		postWorkFlowRes2.prettyPrint();

		Assert.assertEquals(postWorkFlowRes2.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");

		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		Assert.assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		User_Pojo canPojo = new User_Pojo();
		String createCandidate = canPojo.createCandidate(methodItemId2, methodItemId);
		Response candidateRes = allUtils.post_URLPOJO(URL, AuthorizationKey, CandidateURI, createCandidate);
		candidateRes.prettyPrint();
		Assert.assertEquals(candidateRes.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(candidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, candidateRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(candidateRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_CreateANewCandidateRevisionWithStatus_404() throws IOException {
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		String parntId = String.valueOf(parentId.get(6));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		// Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
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

		String methodologyItemId1 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("displayAs", post.getProperty("patchWorkProgramType1"));
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		// Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType1"));
		data2.put("ruleContextType", post.getProperty("postRuleContextType2"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId2 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam;

		Response postWorkFlowRes2 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId2);
		postWorkFlowRes2.prettyPrint();

		Assert.assertEquals(postWorkFlowRes2.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");

		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		Assert.assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidatess/" + parntId;
		User_Pojo canPojo = new User_Pojo();
		String createCandidate = canPojo.createCandidate(methodItemId2, methodItemId);
		Response candidateRes = allUtils.post_URLPOJO(URL, AuthorizationKey, CandidateURI, createCandidate);
		candidateRes.prettyPrint();
		Assert.assertEquals(candidateRes.getStatusCode(), 404);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(candidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, candidateRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(candidateRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_CreateANewCandidateRevisionWithStatus_400() throws IOException {
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		String parntId = String.valueOf(parentId.get(3));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		// Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
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

		String methodologyItemId1 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("displayAs", post.getProperty("patchWorkProgramType1"));
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		// Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType1"));
		data2.put("ruleContextType", post.getProperty("postRuleContextType2"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId2 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam;

		Response postWorkFlowRes2 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId2);
		postWorkFlowRes2.prettyPrint();

		Assert.assertEquals(postWorkFlowRes2.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");

		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		Assert.assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		User_Pojo canPojo = new User_Pojo();
		String createCandidate = canPojo.createCandidate(methodItemId2, methodItemId);
		Response candidateRes = allUtils.post_URLPOJO(URL, AuthorizationKey, CandidateURI, createCandidate);
		candidateRes.prettyPrint();
		Assert.assertEquals(candidateRes.getStatusCode(), 400);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(candidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, candidateRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(candidateRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_GetCandidateRevisionWithStatus_204() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<String> methodologyId = jsonPathEvaluator1.get("id");
		String parntId = String.valueOf(methodologyId.get(1));
		String getCandidateURI = "/api/methodology/candidate/" + parntId;
		Response getCandidateRes = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, getCandidateURI);
		getCandidateRes.prettyPrint();
		Assert.assertEquals(getCandidateRes.getStatusCode(), 204);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getCandidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getCandidateRes.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(getCandidateRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_GetCandidateRevisionWithStatus_404() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<String> methodologyId = jsonPathEvaluator1.get("id");
		String parntId = String.valueOf(methodologyId.get(2));
		String getCandidateURI = "/api/methodology/candidates/" + parntId;
		Response getCandidateRes = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, getCandidateURI);
		getCandidateRes.prettyPrint();
		Assert.assertEquals(getCandidateRes.getStatusCode(), 404);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getCandidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getCandidateRes.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(getCandidateRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_DeleteAnCandidateRevisionWithStatus_200() throws IOException {
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		String parntId = String.valueOf(parentId.get(6));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		// Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
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

		String methodologyItemId1 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("displayAs", post.getProperty("patchWorkProgramType1"));
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		// Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType1"));
		data2.put("ruleContextType", post.getProperty("postRuleContextType2"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId2 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam;

		Response postWorkFlowRes2 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId2);
		postWorkFlowRes2.prettyPrint();

		Assert.assertEquals(postWorkFlowRes2.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");

		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		Assert.assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		User_Pojo canPojo = new User_Pojo();
		String createCandidate = canPojo.createCandidate(methodItemId2, methodItemId);
		Response candidateRes = allUtils.post_URLPOJO(URL, AuthorizationKey, CandidateURI, createCandidate);
		candidateRes.prettyPrint();
		JsonPath candidateJson = candidateRes.jsonPath();
		String candidateMethodologyId = candidateJson.get("methodologyId");
		System.out.println("Methodology id------>" + candidateMethodologyId);
		String candidateId=candidateJson.get("id");

		/**
		 * Delete the candidate
		 */
		String deleteUri = "/api/methodology/candidate/" + candidateMethodologyId + "/" + candidateId;
		Response deleteRes = allUtils.delete(URL, AuthorizationKey, deleteUri);
		deleteRes.prettyPrint();
		Assert.assertEquals(deleteRes.getStatusCode(), 200);

		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(deleteRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(deleteRes);

	}
	@Test(groups = "IntegrationTests")
	public void Methodology_DeleteAnCandidateRevisionWithStatus_400() throws IOException {
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		String parntId = String.valueOf(parentId.get(6));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		// Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
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

		String methodologyItemId1 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("displayAs", post.getProperty("patchWorkProgramType1"));
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		// Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType1"));
		data2.put("ruleContextType", post.getProperty("postRuleContextType2"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId2 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam;

		Response postWorkFlowRes2 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId2);
		postWorkFlowRes2.prettyPrint();

		Assert.assertEquals(postWorkFlowRes2.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");

		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		Assert.assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		User_Pojo canPojo = new User_Pojo();
		String createCandidate = canPojo.createCandidate(methodItemId2, methodItemId);
		Response candidateRes = allUtils.post_URLPOJO(URL, AuthorizationKey, CandidateURI, createCandidate);
		candidateRes.prettyPrint();
		JsonPath candidateJson = candidateRes.jsonPath();
		String candidateMethodologyId = candidateJson.get("methodologyId");
		String str = candidateMethodologyId.toString();
		System.out.println("Methodology id------>" + candidateMethodologyId);
		String candidateId=candidateJson.get("id");
		/**
		 * Delete the candidate
		 */
		String deleteUri = "/api/methodology/candidate/" + candidateMethodologyId + "/" + candidateId;
		Response deleteRes1 = allUtils.delete(URL, AuthorizationKey, deleteUri);
		Response deleteRes = allUtils.delete(URL, AuthorizationKey, deleteUri);
		deleteRes.prettyPrint();
		Assert.assertEquals(deleteRes.getStatusCode(), 400);

		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(deleteRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(deleteRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_DeleteAnCandidateRevisionWithStatus_404() throws IOException {
		allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		// OrganizationsDetails.prettyPrint();

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		String parntId = String.valueOf(parentId.get(6));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		// Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postWorkProgramTitle") + allUtils.getRandomNumber(1, 30));
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

		String methodologyItemId1 = postCreatePhase1.asString();
		System.out.println("---->" + methodItemId2);

		// Performing PATCH operation

		String patchId7 = "/api/methodologyItem/revision/" + revision1 + "/item/" + methodItemId2;

		Map<String, String> data7 = new HashMap<String, String>();
		data7.put("displayAs", post.getProperty("patchWorkProgramType1"));
		data7.put("workProgramType", post.getProperty("patchWorkProgramType1"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		// Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("itemizedWorkProgramType", post.getProperty("postItemizedWorkProgramType1"));
		data2.put("ruleContextType", post.getProperty("postRuleContextType2"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId2 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam;

		Response postWorkFlowRes2 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId2);
		postWorkFlowRes2.prettyPrint();

		Assert.assertEquals(postWorkFlowRes2.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");

		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + methodItemId
				+ "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = allUtils.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		Assert.assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		User_Pojo canPojo = new User_Pojo();
		String createCandidate = canPojo.createCandidate(methodItemId2, methodItemId);
		Response candidateRes = allUtils.post_URLPOJO(URL, AuthorizationKey, CandidateURI, createCandidate);
		candidateRes.prettyPrint();
		JsonPath candidateJson = candidateRes.jsonPath();
		String candidateMethodologyId = candidateJson.get("methodologyId");
		String str = candidateMethodologyId.toString();
		System.out.println("Methodology id------>" + candidateMethodologyId);
		String candidateId=candidateJson.get("id");
		/**
		 * Delete the candidate
		 */
		String deleteUri = "/api/methodology/candidate/" + candidateMethodologyId + "/" + revId.substring(1, 25);
		
		Response deleteRes = allUtils.delete(URL, AuthorizationKey, deleteUri);
		deleteRes.prettyPrint();
		Assert.assertEquals(deleteRes.getStatusCode(), 404);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(deleteRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(deleteRes);

	}

	// @Test(groups = "IntegrationTests")
	public void Methodology_ListAllPublishedStoreMethodologies() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		Response EngagementStore = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/methodology/store");
		EngagementStore.prettyPrint();
		Assert.assertEquals(EngagementStore.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(EngagementStore.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, EngagementStore.asString());
		allUtils.validate_HTTPStrictTransportSecurity(EngagementStore);
	}

	// @Test(groups = { "EndToEnd" })
	public void Methodology_ENDTOEND_Scenario() throws IOException {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		/**
		 * FETCHING THE ORGANISATION ID
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * FETCHING THE ENGAGEMENT ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(3));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator1.get("engagementType");
		organizationId = jsonPathEvaluator1.get("organization");
		/**
		 * PERFORMING THE POST OPERATION
		 */
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postMethodologyTitle") + allUtils.getRandomNumber(1, 30));
		map.put("engagementType", engagementTypeId.get(2));
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription"));

		User_Pojo po = new User_Pojo();
		String createMethodology = po.methodologyAdd(map);

		String postMethodologyId = "/api/methodology";
		Response methodRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postMethodologyId, createMethodology);
		methodRes.prettyPrint();
		Assert.assertEquals(methodRes.statusCode(), 200);
		JsonPath jsonEvaluator2 = methodRes.jsonPath();
		String methodologyId1 = jsonEvaluator2.get("id");
		System.out.println("Mehtodology id----->" + methodologyId1);

		/**
		 * PERFORMING THE PATCH OPERATION
		 */
		String patchId = "/api/methodology/" + methodologyId1;
		Map<String, String> map1 = new HashMap<String, String>();
		map.put("title",
				post.getProperty("postMethodologyTitle") + getRandomAlphaNum() + allUtils.getRandomNumber(1, 30));

		User_Pojo po1 = new User_Pojo();
		String createMethodology1 = po1.methodologyAdd(map1);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology1);
		getMethodologyByrevisonIdRes.prettyPrint();
		// Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 204);

		/**
		 * PERFORMING GET OPERATION-->FETCHING THE SPECIFIC METHODOLOGY
		 */

		Response getMethodologyByIdRes = allUtils.get_URL_WithOne_PathParams(URL, AuthorizationKey,
				"/api/methodology/{value}", methodologyId1);
		getMethodologyByIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByIdRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_InheritAMethodology400() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(3));
		getEngagementTypeRes.prettyPrint();

		// Retrieving the inheritedFrom

		JsonPath jp = getEngagementTypeRes.jsonPath();
		List<String> inheritedFrom = jp.getList("inheritedFrom");

		System.out.println(inheritedFrom);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("inheritFrom", inheritedFrom.get(0).substring(1, 6));
		map.put("organization", listOrdId.get(3).substring(1, 6));

		User_Pojo po = new User_Pojo();
		String userOobj = po.methodologyInherit(map);

		String URI = "/api/methodology/inherit/";
		Response postEngagementTypeInheriteRes = allUtils.post_URLPOJO(URL, AuthorizationKey, URI, userOobj);
		postEngagementTypeInheriteRes.prettyPrint();

		/*
		 * Response postEngagementTypeInheriteRes =
		 * allUtils.get_URL_QueryParams(URL, AuthorizationKey,
		 * "/api/engagementType/inherit/", "Organization",
		 * inheritedFrom.get(5)); postEngagementTypeInheriteRes.prettyPrint();
		 */
		Assert.assertEquals(postEngagementTypeInheriteRes.statusCode(), 400);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getEngagementTypeRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_InheritAMethodology200() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(3));
		getEngagementTypeRes.prettyPrint();

		// Retrieving the inheritedFrom

		JsonPath jp = getEngagementTypeRes.jsonPath();
		List<String> inheritedFrom = jp.getList("inheritedFrom");

		System.out.println(inheritedFrom);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("inheritFrom", inheritedFrom.get(0));
		map.put("organization", listOrdId.get(3));

		User_Pojo po = new User_Pojo();
		String userOobj = po.methodologyInherit(map);

		String URI = "/api/methodology/inherit/";
		Response postEngagementTypeInheriteRes = allUtils.post_URLPOJO(URL, AuthorizationKey, URI, userOobj);
		postEngagementTypeInheriteRes.prettyPrint();

		/*
		 * Response postEngagementTypeInheriteRes =
		 * allUtils.get_URL_QueryParams(URL, AuthorizationKey,
		 * "/api/engagementType/inherit/", "Organization",
		 * inheritedFrom.get(5)); postEngagementTypeInheriteRes.prettyPrint();
		 */
		Assert.assertEquals(postEngagementTypeInheriteRes.statusCode(), 200);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getEngagementTypeRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);
	}

	@Test(groups = "IntegrationTests")
	public void Methodology_InheritAMethodology404() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(3));
		getEngagementTypeRes.prettyPrint();

		// Retrieving the inheritedFrom

		JsonPath jp = getEngagementTypeRes.jsonPath();
		List<String> inheritedFrom = jp.getList("inheritedFrom");

		System.out.println(inheritedFrom);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("inheritFrom", inheritedFrom.get(0));
		map.put("organization", listOrdId.get(3));

		User_Pojo po = new User_Pojo();
		String userOobj = po.methodologyInherit(map);

		String URI = "/api/methodologyss/inherit";

		Response postEngagementTypeInheriteRes = allUtils.post_URLPOJO(URL, AuthorizationKey, URI, userOobj);

		postEngagementTypeInheriteRes.prettyPrint();

		/*
		 * Response postEngagementTypeInheriteRes =
		 * allUtils.get_URL_QueryParams(URL, AuthorizationKey,
		 * "/api/engagementType/inherit/", "Organization",
		 * inheritedFrom.get(5)); postEngagementTypeInheriteRes.prettyPrint();
		 */
		Assert.assertEquals(postEngagementTypeInheriteRes.statusCode(), 404);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getEngagementTypeRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);
	}
}
