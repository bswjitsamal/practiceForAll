package restassured.automation.testcases;

import java.io.File;
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
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		Assert.assertEquals(getMethodologyRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
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
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		System.out.println(methodologyId);

		Response getMethodologyByIdRes = getMethodology.get_URL_WithOne_PathParams(URL, AuthorizationKey,
				"/api/methodology/{value}", methodologyId.get(0));
		getMethodologyByIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
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
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		listRevision = jsonPathEvaluator1.get("revisions.id");

		// System.out.println(String.valueOf(listRevision.get(0)));

		String revId = String.valueOf(listRevision.get(0));

		System.out.println(methodologyId);

		String patchId = "/api/methodology/" + methodologyId.get(0) + "/revision/"
				+ revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.get_URL_Without_Params(URL, AuthorizationKey,
				patchId);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByrevisonIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
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
				"Organization", listOrdId.get(4));
		getMethodologyRes1.prettyPrint();

		JsonPath jsonPathEvaluator2 = getMethodologyRes1.jsonPath();
		methodologyId1 = jsonPathEvaluator2.get("id");

		/**
		 * FETCHING REVISION ID FROM ORGANISATION - 2
		 */

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		listRevision = jsonPathEvaluator1.get("revisions.id");

		// System.out.println(String.valueOf(listRevision.get(0)));

		String revId = String.valueOf(listRevision.get(0));

		System.out.println(methodologyId);

		String patchId = "/api/methodology/" + methodologyId1.get(2) + "/revision/"
				+ revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.get_URL_Without_Params(URL, AuthorizationKey,
				patchId);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByrevisonIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	/*@Test(groups = "IntegrationTests")
	public void methodology_PostAddANewMethodology_status200() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("engagementType");
		organizationId = jsonPathEvaluator.get("organization");
	ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));

		System.out.println(engagementTypeId);
		System.out.println(revId);

		
		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setOrganization(listOrdId.get(7));
		mp.setTitle(post.getProperty("postMethodologyTitle")+getRandomAlphaNum()+allUtils.getRandomNumber(1,20));
		mp.setEngagementType(engagementTypeId.get(1));
	    
		//mp.setRevisions(new String[] { String.valueOf(listRevisionI1.get(1)) });
		// mp.setId("6015609b192b4718edc2da5d");
		mp.setDraftDescription("This for testing purpose");

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_URLPOJO(URL, AuthorizationKey,
				"/api/methodology", mp);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);
		*//**
		 * Extent report generation
		 *//*
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByrevisonIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);


	}*/
	
	@Test(groups = "IntegrationTests")
	public void Methodology_PostAddANewMethodology_status200() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("engagementType");
		organizationId = jsonPathEvaluator.get("organization");
	/*ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));

		System.out.println(engagementTypeId);
		System.out.println(revId);
*/
		
		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setId(organizationId.get(5));
		mp.setTitle(post.getProperty("postMethodologyTitle")+getRandomAlphaNum()+allUtils.getRandomNumber(1,20));
		mp.setEngagementType(engagementTypeId.get(5));
	    
		//mp.setRevisions(new String[] { String.valueOf(listRevisionI1.get(1)) });
		// mp.setId("6015609b192b4718edc2da5d");
		mp.setDraftDescription("This for testing purpose");

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_URLPOJO(URL, AuthorizationKey,
				"/api/methodology", mp);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByrevisonIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);


	}

	@Test(groups = "IntegrationTests")
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
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("engagementType");
		organizationId = jsonPathEvaluator.get("organization");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));

		System.out.println(engagementTypeId);
		System.out.println(revId);

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("postMethodologyTitle") + getRandomAlphaNum() + allUtils.getRandomNumber(1, 20));
		mp.setEngagementType(engagementTypeId.get(0) + getRandomAlphaNum());
		mp.setOrganization(organizationId.get(0));
		mp.setRevisions(new String[] { String.valueOf(listRevisionI1.get(1)) });
		// mp.setId("6015609b192b4718edc2da5d");

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_URLPOJO(URL, AuthorizationKey,
				"/api/methodology", mp);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByrevisonIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
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
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		methodologyId = jsonPathEvaluator.get("id");
		String patchId = "/api/methodology/" + methodologyId.get(0);

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("patchMethodologyTitle"));

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				mp);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByrevisonIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);


	}

	@Test(groups = "IntegrationTests")
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
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		methodologyId = jsonPathEvaluator.get("id");
		String patchId = "/api/methodology/" + methodologyId.get(0);

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("postMethodologyTitle") + getRandomAlphaNum());

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				mp);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByrevisonIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);


	}

	

	//@Test
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
		ExtentTestManager.getTest().log(Status.INFO,methodologyTypeRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		allUtils.validate_HTTPStrictTransportSecurity(methodologyTypeRes);


	}
	
	@Test
	public void Methodology_PatchUpdateASpecificMethodologiesWith_Status_204() throws IOException
	{
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
		methodologyId = jsonPathEvaluator1.get("id");
		listRevision = jsonPathEvaluator1.get("revisions.id");
		// System.out.println(String.valueOf(listRevision.get(0)));
		System.out.println("List---->"+listRevision);
		String revId = String.valueOf(listRevision.get(1));
		System.out.println("Revision id------>"+revId);
		System.out.println(methodologyId);
		listDraftDescription=jsonPathEvaluator1.get("revisions.draftDescription");
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Methodology_Pojo mp=new Methodology_Pojo();
		mp.setDraftDescription(post.getProperty("postMethodologyDraftDescription")+getMethodology.getRandomNumber(1,20));
		

		String patchId = "/api/methodology/" + methodologyId.get(1) + "/revision/"
				+ revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();
		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey,
				patchId,mp);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByrevisonIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}
	@Test
	public void Methodology_PatchUpdateASpecificMethodologiesWith_Status_400() throws IOException
	{
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		//OrganizationsDetails.prettyPrint();
		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		//getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		listRevision = jsonPathEvaluator1.get("revisions.id");
		// System.out.println(String.valueOf(listRevision.get(0)));
		System.out.println("List---->"+listRevision);
		String revId = String.valueOf(listRevision.get(1));
		System.out.println("Revision id------>"+revId);
		System.out.println(methodologyId);
		listDraftDescription=jsonPathEvaluator1.get("revisions.draftDescription");
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Methodology_Pojo mp=new Methodology_Pojo();
		mp.setDraftDescription(post.getProperty("postMethodologyDraftDescription")+getRandomAlphaNum()+getMethodology.getRandomNumber(1,20));
		

		String patchId = "/api/methodology/" + methodologyId.get(0) + "/revision/"
				+ revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();
		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey,
				patchId,mp);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByrevisonIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getMethodologyByrevisonId.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}
	@Test(groups={"EndToEnd"})
	public void Methodology_ENDTOEND_Scenario() throws IOException{
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();
		

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

	/*ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));

		System.out.println(engagementTypeId);
		System.out.println(revId);
*/
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		/*Methodology_Pojo mp = new Methodology_Pojo();
		mp.setId(organizationId.get(7));
		mp.setTitle(post.getProperty("postMethodologyTitle")+getRandomAlphaNum()+allUtils.getRandomNumber(1,20));
		mp.setEngagementType(engagementTypeId.get(7));
	    
		//mp.setRevisions(new String[] { String.valueOf(listRevisionI1.get(1)) });
		// mp.setId("6015609b192b4718edc2da5d");
		mp.setDraftDescription("This for testing purpose");

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_URLPOJO(URL, AuthorizationKey,
				"/api/methodology", mp);
		getMethodologyByrevisonIdRes.prettyPrint();*/

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
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator1.get("engagementType");
		organizationId = jsonPathEvaluator1.get("organization");
		/**
		 * PERFORMING THE POST OPERATION
		 */
		Methodology_Pojo methodology = new Methodology_Pojo();
		methodology.setId(listOrdId.get(7));
		methodology.setEngagementType(engagementTypeId.get(7));
		methodology.setTitle(
				post.getProperty("postMethodologyTitle")+ allUtils.getRandomNumber(1, 120));
		methodology.setDraftDescription("This is for testing purpose");

		String patchId = "/api/methodology";
		Response methodologyTypeRes = allUtils.post_URLPOJO(URL, AuthorizationKey, patchId, methodology);

		methodologyTypeRes.prettyPrint();
		Assert.assertEquals(methodologyTypeRes.statusCode(), 200);
		JsonPath jsonEvaluator2=methodologyTypeRes.jsonPath();
		String methodologyId1=jsonEvaluator2.get("id");
		System.out.println("Mehtodology id----->"+methodologyId1);
				
		/**
		 * PERFORMING THE PATCH OPERATION
		 */
		
		String patchId1="/api/methodology/" + methodologyId1;
		Methodology_Pojo methodology1 = new Methodology_Pojo();
		methodology1.setTitle(post.getProperty("postMethodologyTitle") + getRandomAlphaNum() + allUtils.getRandomNumber(1, 20));
		Response methodologyPatchRes1=allUtils.patch_URLPOJO(URL, AuthorizationKey, patchId1, methodology1);
		methodologyPatchRes1.prettyPrint();
		Assert.assertEquals(methodologyPatchRes1.statusCode(), 204);
		
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
		ExtentTestManager.getTest().log(Status.INFO,getMethodologyByIdRes.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		allUtils.validate_HTTPStrictTransportSecurity(getMethodologyByIdRes);

		
		
		
		
		
	}
	


}
