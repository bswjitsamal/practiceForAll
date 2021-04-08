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

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));
		getMethodologyRes.prettyPrint();
		Assert.assertEquals(getMethodologyRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_GetDetialsAboutASpecificMethodology_status200() {

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
				"Organization", listOrdId.get(5));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		System.out.println("methodologyId---------------->"+methodologyId.get(2));

		Response getMethodologyByIdRes = getMethodology.get_URL_WithOne_PathParams(URL, AuthorizationKey,
				"/api/methodology/{value}", methodologyId.get(2));
		getMethodologyByIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyRes.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(getMethodologyRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_GetASpecificRevisionItemTree_status200() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		listRevision = jsonPathEvaluator1.get("revisions.id");

		// System.out.println(String.valueOf(listRevision.get(0)));

		String revId = String.valueOf(listRevision.get(2));
		System.out.println("revision-------->" + revId);

		System.out.println(methodologyId);

		String patchId = "/api/methodology/" + methodologyId.get(2) + "/revision/"
				+ revId.substring(1,25);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.get_URL_Without_Params(URL, AuthorizationKey,
				patchId);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_getASpecificRevisionItemTree_status404() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * FETCHING METHODOLOGY FROM ORGANISATION - 1
		 */

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes1 = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));
		getMethodologyRes1.prettyPrint();

		JsonPath jsonPathEvaluator2 = getMethodologyRes1.jsonPath();
		methodologyId1 = jsonPathEvaluator2.get("id");

		/**
		 * FETCHING REVISION ID FROM ORGANISATION - 2
		 */

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		listRevision = jsonPathEvaluator1.get("revisions.id");

		// System.out.println(String.valueOf(listRevision.get(0)));

		String revId = String.valueOf(listRevision.get(2));

		System.out.println(methodologyId);

		String patchId = "/api/methodology/" + methodologyId1.get(0) + "/revisionss/"
				+ revId.substring(1,25);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.get_URL_Without_Params(URL, AuthorizationKey,
				patchId);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	//@Test(groups = "IntegrationTests")
	public void Methodology_PostAddANewMethodology_status200() throws IOException {
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();
		

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String>eType=jsonPathEvaluator1.get("engagementType");
		String engagementType=eType.get(0);
		/*ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(9));
		String parntId = String.valueOf(parentId.get(0));*/

		Map<String,String>map=new HashMap<String, String>();
		map.put("title",post.getProperty("postMethodologyTitle")+allUtils.getRandomNumber(1,30));
		map.put("engagementType", engagementType);
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription"));
		
		User_Pojo po=new User_Pojo();
		String createMethodology=po.methodologyAdd(map);
		
		String postMethodologyId="/api/methodology";
		Response methodRes=allUtils.post_URLPOJO(URL, AuthorizationKey, postMethodologyId,createMethodology);
		methodRes.prettyPrint();
		Assert.assertEquals(methodRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(methodRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, methodRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(methodRes);

	}

	//@Test(groups = "IntegrationTests")
	public void Methodology_PostAddANewMethodology_status400() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("engagementType");
		organizationId = jsonPathEvaluator.get("organization");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId1 = String.valueOf(listRevisionI1.get(2));
		String revId=revId1.substring(1,25); 

		System.out.println(engagementTypeId);
		System.out.println(revId);
		
		Map<String,String>map=new HashMap<String, String>();
		map.put("title",post.getProperty("postMethodologyTitle")+allUtils.getRandomNumber(1,30));
		map.put("engagementType", engagementTypeId.get(0) + getRandomAlphaNum());
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription"));
		
		User_Pojo po=new User_Pojo();
		String createMethodology=po.methodologyAdd(map);
		
		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_URLPOJO(URL, AuthorizationKey,
				"/api/methodology", createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_PatchUpdateAMethodology_status400() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		methodologyId = jsonPathEvaluator.get("id");
		String patchId = "/api/methodology/" + methodologyId.get(2);

		Map<String,String>map=new HashMap<String, String>();
		map.put("title",post.getProperty("patchMethodologyTitle"));
		
		User_Pojo po=new User_Pojo();
		String createMethodology=po.methodologyAdd(map);
		

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	//@Test(groups = "IntegrationTests")
	public void Methodology_PatchUpdateAMethodology_status204() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		methodologyId = jsonPathEvaluator.get("id");
		String patchId = "/api/methodology/" + methodologyId.get(1);

		Map<String,String>map=new HashMap<String, String>();
		map.put("title",post.getProperty("postMethodologyTitle")+allUtils.getRandomNumber(1,30));
		
		
		User_Pojo po=new User_Pojo();
		String createMethodology=po.methodologyAdd(map);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	// @Test
	public void createANewMethodology() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		/**
		 * FETCHING THE ORGANISATION ID
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * FETCHING THE ENGAGEMENT ID
		 */

		Response getEngagementTypeRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType",
				"Organization", listOrdId.get(7));
		getEngagementTypeRes.prettyPrint();
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		JsonPath jsonEvaluator1 = getEngagementTypeRes.jsonPath();
		engagementTypeId = jsonEvaluator1.get("id");

		/**
		 * PERFORMING THE POST OPERATION
		 */
		Methodology_Pojo methodology = new Methodology_Pojo();
		methodology.setOrganization(listOrdId.get(7));
		methodology.setEngagementType(engagementTypeId.get(7));
		methodology.setTitle(
				post.getProperty("postMethodologyTitle") + getRandomAlphaNum() + allUtils.getRandomNumber(1, 20));
		methodology.setDraftDescription("This is for testing purpose");

		String patchId = "/api/methodology";
		Response methodologyTypeRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId, methodology);

		methodologyTypeRes.prettyPrint();
		Assert.assertEquals(methodologyTypeRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(methodologyTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, methodologyTypeRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(methodologyTypeRes);

	}

	//@Test(groups = "IntegrationTests")
	public void Methodology_PatchUpdateASpecificMethodologiesWith_Status_204() throws IOException {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		listRevision = jsonPathEvaluator1.get("revisions.id");
		// System.out.println(String.valueOf(listRevision.get(0)));
		System.out.println("List---->" + listRevision);
		String revId = String.valueOf(listRevision.get(1));
		System.out.println("Revision id------>" + revId);
		System.out.println(methodologyId);
		listDraftDescription = jsonPathEvaluator1.get("revisions.draftDescription");
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Map<String,String>map=new HashMap<String, String>();
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription")+ getMethodology.getRandomNumber(1, 20));
		
		User_Pojo po=new User_Pojo();
		String createMethodology=po.methodologyAdd(map);
		

		String patchId = "/api/methodology/" + methodologyId.get(1) + "/revision/"
				+ revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();
		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Methodology_PatchUpdateASpecificMethodologiesWith_Status_400() throws IOException {
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
		methodologyId = jsonPathEvaluator1.get("id");
		listRevision = jsonPathEvaluator1.get("revisions.id");
		// System.out.println(String.valueOf(listRevision.get(0)));
		System.out.println("List---->" + listRevision);
		String revId = String.valueOf(listRevision.get(2));
		System.out.println("Revision id------>" + revId);
		System.out.println(methodologyId);
		listDraftDescription = jsonPathEvaluator1.get("revisions.draftDescription");
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		
		Map<String,String>map=new HashMap<String, String>();
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription") + getRandomAlphaNum()
		+ getMethodology.getRandomNumber(1, 20));
		
		User_Pojo po=new User_Pojo();
		String createMethodology=po.methodologyAdd(map);
	
		String patchId = "/api/methodology/" + methodologyId.get(0) + "/revision/"
				+ revId.substring(1,25);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();
		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	//@Test(groups = "IntegrationTests")
	public void Methodology_CreateANewCandidateRevisionWithStatus_200() throws IOException {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));
		String parntId = String.valueOf(parentId.get(1));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		//Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
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
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		//Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		//Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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
		//Assert.assertEquals(postProcedureRes.getStatusCode(), 200);

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("postWorkProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();
		//Assert.assertEquals(patchCreateProcedure.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */
		String workFlowURI = "/api/methodologyItem/revision/" + revId.substring(1,25);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, workFlowURI);
		getEngagementTypeRes.prettyPrint();
		//Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);
		JsonPath workFlowJson = getEngagementTypeRes.jsonPath();
		// workFlow=workFlowJson.get("workFlowState");
		methodlogyItemId = workFlowJson.get("methodologyItemId");
		String methodItemId3 = methodlogyItemId.get(1);

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId3 + "/workFlow/" + workFlowParam;

		Response postWorkFlowRes = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId);
		postWorkFlowRes.prettyPrint();
		// Assert.assertEquals(postWorkFlowRes.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");
		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId2 + "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		// .assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		Methodology_Pojo mi = new Methodology_Pojo();
		mi.setItemIds(new String[] { methodItemId, methodItemId2 });
		Response candidateRes = getEngagementType.post_URLPOJO(URL, AuthorizationKey, CandidateURI, mi);
		candidateRes.prettyPrint();
		Assert.assertEquals(candidateRes.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(candidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, candidateRes.asString());
		getEngagementType.validate_HTTPStrictTransportSecurity(candidateRes);

	}
	
	//@Test(groups = "IntegrationTests")
	public void Methodology_CreateANewCandidateRevisionWithStatus_400() throws IOException {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));
		String parntId = String.valueOf(parentId.get(0));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
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
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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
		Assert.assertEquals(postProcedureRes.getStatusCode(), 200);

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("postWorkProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();
		Assert.assertEquals(patchCreateProcedure.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */
		String workFlowURI = "/api/methodologyItem/revision/" + revId.substring(1,25);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, workFlowURI);
		getEngagementTypeRes.prettyPrint();
		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);
		JsonPath workFlowJson = getEngagementTypeRes.jsonPath();
		// workFlow=workFlowJson.get("workFlowState");
		methodlogyItemId = workFlowJson.get("methodologyItemId");
		String methodItemId3 = methodlogyItemId.get(1);

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId3 + "/workFlow/" + workFlowParam;

		Response postWorkFlowRes = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId);
		postWorkFlowRes.prettyPrint();
		// Assert.assertEquals(postWorkFlowRes.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");
		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId2 + "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		// .assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		Methodology_Pojo mi = new Methodology_Pojo();
		mi.setItemIds(new String[] { post.getProperty("postWorkFlowState2") });
		Response candidateRes = getEngagementType.post_URLPOJO(URL, AuthorizationKey, CandidateURI, mi);
		candidateRes.prettyPrint();
		Assert.assertEquals(candidateRes.getStatusCode(), 400);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(candidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, candidateRes.asString());
		getEngagementType.validate_HTTPStrictTransportSecurity(candidateRes);

	}
	//@Test(groups = "IntegrationTests")
	public void Methodology_CreateANewCandidateRevisionWithStatus_409() throws IOException {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));
		String parntId = String.valueOf(parentId.get(0));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
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
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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
		Assert.assertEquals(postProcedureRes.getStatusCode(), 200);

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("postWorkProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();
		Assert.assertEquals(patchCreateProcedure.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */
		String workFlowURI = "/api/methodologyItem/revision/" + revId.substring(1,25);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, workFlowURI);
		getEngagementTypeRes.prettyPrint();
		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);
		JsonPath workFlowJson = getEngagementTypeRes.jsonPath();
		// workFlow=workFlowJson.get("workFlowState");
		methodlogyItemId = workFlowJson.get("methodologyItemId");
		String methodItemId3 = methodlogyItemId.get(1);

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId3 + "/workFlow/" + workFlowParam;

		Response postWorkFlowRes = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId);
		postWorkFlowRes.prettyPrint();
		// Assert.assertEquals(postWorkFlowRes.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");
		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId2 + "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		// .assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		Methodology_Pojo mi = new Methodology_Pojo();
		mi.setItemIds(new String[] { methodItemId1, methodItemId1 });
		
		Response candidateRes = getEngagementType.post_URLPOJO(URL, AuthorizationKey, CandidateURI, mi);
		candidateRes.prettyPrint();
		Assert.assertEquals(candidateRes.getStatusCode(), 409);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(candidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, candidateRes.asString());
		getEngagementType.validate_HTTPStrictTransportSecurity(candidateRes);

	}
	//@Test(groups = "IntegrationTests")
	public void Methodology_GetCandidateRevisionWithStatus_200(){
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<String> methodologyId = jsonPathEvaluator1.get("id");
		String parntId = String.valueOf(methodologyId.get(0));
		String getCandidateURI="/api/methodology/candidate/" + parntId;
		Response getCandidateRes=getMethodology.get_URL_Without_Params(URL, AuthorizationKey, getCandidateURI);
		getCandidateRes.prettyPrint();
		Assert.assertEquals(getCandidateRes.getStatusCode(),200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getCandidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getCandidateRes.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(getCandidateRes);

	}
	//@Test(groups = "IntegrationTests")
	public void Methodology_GetCandidateRevisionWithStatus_404(){
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<String> methodologyId = jsonPathEvaluator1.get("id");
		String parntId = String.valueOf(methodologyId.get(0));
		String getCandidateURI="/api/methodology/candidates/" + parntId;
		Response getCandidateRes=getMethodology.get_URL_Without_Params(URL, AuthorizationKey, getCandidateURI);
		getCandidateRes.prettyPrint();
		Assert.assertEquals(getCandidateRes.getStatusCode(),404);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getCandidateRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getCandidateRes.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(getCandidateRes);

	}
	
	//@Test(groups="IntegrationTests")
	public void Methodology_DeleteACandidateRevisionWithStatus_200() throws IOException {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();
		

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String>eType=jsonPathEvaluator1.get("engagementType");
		String engagementType=eType.get(0);
		/*ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(9));
		String parntId = String.valueOf(parentId.get(0));*/

		Map<String,String>map=new HashMap<String, String>();
		map.put("title",post.getProperty("postMethodologyTitle")+allUtils.getRandomNumber(1,30));
		map.put("engagementType", engagementType);
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription"));
		
		User_Pojo po=new User_Pojo();
		String createMethodology=po.methodologyAdd(map);
		
		String postMethodologyId="/api/methodology";
		Response methodRes=allUtils.post_URLPOJO(URL, AuthorizationKey, postMethodologyId,createMethodology);
		methodRes.prettyPrint();
		JsonPath methodJson=methodRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1=methodJson.get("revisions.id");
		String parentId=methodJson.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(2)));

		String revId = String.valueOf(listRevisionI1.get(2));
		String parntId = String.valueOf(parentId);
			
		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		//Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		//String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
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
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		//Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		//Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("title", post.getProperty("postMethodologyItemTitle"));
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
		//Assert.assertEquals(postProcedureRes.getStatusCode(), 200);

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("postWorkProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();
		//Assert.assertEquals(patchCreateProcedure.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */
		String workFlowURI = "/api/methodologyItem/revision/" + revId.substring(1,25);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, workFlowURI);
		getEngagementTypeRes.prettyPrint();
		//Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);
		JsonPath workFlowJson = getEngagementTypeRes.jsonPath();
		// workFlow=workFlowJson.get("workFlowState");
		methodlogyItemId = workFlowJson.get("methodologyItemId");
		String methodItemId3 = methodlogyItemId.get(1);

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId3 + "/workFlow/" + workFlowParam;

		Response postWorkFlowRes = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId);
		postWorkFlowRes.prettyPrint();
		// Assert.assertEquals(postWorkFlowRes.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");
		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId2 + "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		// .assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		Methodology_Pojo mi = new Methodology_Pojo();
		mi.setItemIds(new String[] { methodItemId, methodItemId2 });
		Response candidateRes = getEngagementType.post_URLPOJO(URL, AuthorizationKey, CandidateURI, mi);
		candidateRes.prettyPrint();
		/**
		 * Delete the candidate
		 */
		String deleteUri="/api/methodology/candidate/" + parntId+"/"+revId;
		Response deleteRes=getEngagementType.delete(URL, AuthorizationKey, deleteUri);
		deleteRes.prettyPrint();
		Assert.assertEquals(deleteRes.getStatusCode(),200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(deleteRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRes.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(deleteRes);

	}
	
	
	//@Test(groups = "IntegrationTests")
	public void Methodology_DeleteACandidateRevisionWithStatus_404() throws IOException {
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		ArrayList<String> parentId = jsonPathEvaluator1.get("id");
		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(2));
		String parntId = String.valueOf(parentId.get(2));

		/**
		 * Creating an phase
		 */

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		// MethodologyItem_Pojo mi1 = new MethodologyItem_Pojo();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", post.getProperty("postMethodologyItemTitle"));
		// data.put("parentId", parntId);
		data.put("index", post.getProperty("postMethodologyItemIndex"));
		data.put("itemType", post.getProperty("postMethodologyItemItemType"));

		User_Pojo createPhase = new User_Pojo();
		String createPhaseData = createPhase.PhaseCreate(data);

		Restassured_Automation_Utils AllUtils = new Restassured_Automation_Utils();
		System.out.println("Creating an phase");

		Response postCreatePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId, createPhaseData);
		postCreatePhase.prettyPrint();
		Assert.assertEquals(postCreatePhase.getStatusCode(), 200);

		JsonPath jsonEvaluator = postCreatePhase.jsonPath();
		String revision = jsonEvaluator.get("revisionId");
		String methodItemId = jsonEvaluator.get("methodologyItemId");

		String methodologyItemId = postCreatePhase.asString();
		System.out.println("---->" + methodItemId);

		/**
		 * 
		 * CREATING ONE MORE WP AGAIN WITH NEW DATA
		 */

		String patchId6 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("title", post.getProperty("postMethodologyItemTitle"));
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
		data7.put("workProgramType", post.getProperty("patchWorkProgramType"));
		data7.put("tailoring", post.getProperty("patchTailoring"));
		data7.put("visibility", post.getProperty("patchVisibility"));

		User_Pojo patchPhase1 = new User_Pojo();
		String patchPhaseData1 = patchPhase1.PhaseCreatePatch(data7);

		Response patchCreatePhase1 = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId7, patchPhaseData1);
		patchCreatePhase1.prettyPrint();
		//Assert.assertEquals(patchCreatePhase1.getStatusCode(), 204);

		String patchId2 = "/api/methodologyItem/revision/" + revision + "/workProgram/" + methodItemId2 + "/initialize";

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("ruleContextType", post.getProperty("postRuleContextType"));

		User_Pojo postPhase = new User_Pojo();
		String initializePhaseData = postPhase.initializeWorkProgramType(data2);

		Response initializePhase = AllUtils.post_URLPOJO(URL, AuthorizationKey, patchId2, initializePhaseData);
		initializePhase.prettyPrint();
		Assert.assertEquals(initializePhase.getStatusCode(), 204);

		/**
		 * Create an procedure
		 */

		String patchId3 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/item";

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
		//Assert.assertEquals(postProcedureRes.getStatusCode(), 200);

		// Performing PATCH operation

		JsonPath jsonEvaluator1 = postProcedureRes.jsonPath();
		String methodItemId1 = jsonEvaluator1.get("methodologyItemId");

		String patchId4 = "/api/methodologyItem/revision/" + revision + "/item/" + methodItemId1;

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("workProgramItemType", post.getProperty("postWorkProgramItemType"));

		User_Pojo patchProcedure = new User_Pojo();
		String patchProcedureData = patchProcedure.procedureTypeAdd(data4);

		Response patchCreateProcedure = AllUtils.patch_URLPOJO(URL, AuthorizationKey, patchId4,
				patchProcedureData.replaceFirst("WPId", "workProgramItemType"));
		patchCreateProcedure.prettyPrint();
		//Assert.assertEquals(patchCreateProcedure.getStatusCode(), 204);

		/**
		 * Create an workflow - ReadyForApprove
		 */
		String workFlowURI = "/api/methodologyItem/revision/" + revId.substring(1,25);

		Restassured_Automation_Utils getEngagementType = new Restassured_Automation_Utils();

		Response getEngagementTypeRes = getEngagementType.get_URL_Without_Params(URL, AuthorizationKey, workFlowURI);
		getEngagementTypeRes.prettyPrint();
		Assert.assertEquals(getEngagementTypeRes.statusCode(), 200);
		JsonPath workFlowJson = getEngagementTypeRes.jsonPath();
		// workFlow=workFlowJson.get("workFlowState");
		methodlogyItemId = workFlowJson.get("methodologyItemId");
		String methodItemId3 = methodlogyItemId.get(1);

		String workFlowParam = post.getProperty("postWorkFlowState");

		String postId = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId3 + "/workFlow/" + workFlowParam;

		Response postWorkFlowRes = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId);
		postWorkFlowRes.prettyPrint();
		// Assert.assertEquals(postWorkFlowRes.getStatusCode(), 204);
		/**
		 * Create an workflow - Approved
		 */

		String workFlowParam1 = post.getProperty("postWorkFlowState2");
		String postId1 = "/api/methodologyItem/revision/" + revId.substring(1,25) + "/workProgram/"
				+ methodItemId2 + "/workFlow/" + workFlowParam1;

		Response postWorkFlowRes1 = getEngagementType.post_URL_WithoutBody(URL, AuthorizationKey, postId1);
		postWorkFlowRes1.prettyPrint();
		// .assertEquals(postWorkFlowRes1.getStatusCode(), 204);
		/**
		 * CREATE AN CANDIDATE FOR AN METHODOLOGY
		 */

		String CandidateURI = "/api/methodology/candidate/" + parntId;
		Methodology_Pojo mi = new Methodology_Pojo();
		mi.setItemIds(new String[] { methodItemId, methodItemId2 });
		Response candidateRes = getEngagementType.post_URLPOJO(URL, AuthorizationKey, CandidateURI, mi);
		candidateRes.prettyPrint();
		Assert.assertEquals(candidateRes.getStatusCode(), 200);
		/**
		 * Delete the candidate
		 */
		String deleteUri="/api/methodology/candidates/" + parntId+"/"+revId.substring(1,25);
		Response deleteRes=getEngagementType.delete(URL, AuthorizationKey, deleteUri);
		deleteRes.prettyPrint();
		Assert.assertEquals(deleteRes.getStatusCode(),404);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(deleteRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRes.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(deleteRes);


	}
	

	//@Test(groups = { "EndToEnd" })
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
				"Organization", listOrdId.get(5));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator1.get("engagementType");
		organizationId = jsonPathEvaluator1.get("organization");
		/**
		 * PERFORMING THE POST OPERATION
		 */
		Map<String,String>map=new HashMap<String, String>();
		map.put("title",post.getProperty("postMethodologyTitle")+allUtils.getRandomNumber(1,30));
		map.put("engagementType", engagementTypeId.get(0));
		map.put("draftDescription", post.getProperty("postMethodologyDraftDescription"));
		
		User_Pojo po=new User_Pojo();
		String createMethodology=po.methodologyAdd(map);
		
		String postMethodologyId="/api/methodology";
		Response methodRes=allUtils.post_URLPOJO(URL, AuthorizationKey, postMethodologyId,createMethodology);
		methodRes.prettyPrint();
		Assert.assertEquals(methodRes.statusCode(), 200);
		JsonPath jsonEvaluator2 = methodRes.jsonPath();
		String methodologyId1 = jsonEvaluator2.get("id");
		System.out.println("Mehtodology id----->" + methodologyId1);

		/**
		 * PERFORMING THE PATCH OPERATION
		 */
		String patchId = "/api/methodology/" + methodologyId1;
		Map<String,String>map1=new HashMap<String, String>();
		map.put("title",post.getProperty("postMethodologyTitle")+getRandomAlphaNum()+allUtils.getRandomNumber(1,30));
		
		
		User_Pojo po1=new User_Pojo();
		String createMethodology1=po1.methodologyAdd(map1);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				createMethodology1);
		getMethodologyByrevisonIdRes.prettyPrint();
		//Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 204);

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

}
