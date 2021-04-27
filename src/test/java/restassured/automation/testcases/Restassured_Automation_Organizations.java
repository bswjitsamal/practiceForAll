package restassured.automation.testcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.FileNotFoundException;
import java.io.IOException;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.OrgRolesPojo;
import restassured.automation.Pojo.Organization_Pojo;
import restassured.automation.Pojo.ServiceDetailsPojo;
import restassured.automation.Pojo.User_Pojo;

import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.DataSources_Read_Utils;
import restassured.automation.utils.Excel_Data_Source_Utils;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Organizations {

	String URL;
	String AuthorizationKey;
	List<String> listOrgId;
	List<String> listResourceId;
	List<String> listUserId;
	List<String> listMethodologyId;

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
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey1");
		Awaitility.reset();
		Awaitility.setDefaultPollDelay(999, MILLISECONDS);
		Awaitility.setDefaultPollInterval(99, SECONDS);
		Awaitility.setDefaultTimeout(99, SECONDS);

	}

	@Test(groups = "IntegrationTests")
	public void Organisation_GetTheListOfOrganization() {

		/**
		 * FETCHING THE LIST OF ORGANISATION
		 */

		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response OrganizationsDetails = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		OrganizationsDetails.prettyPrint();

		// OrganizationsGet.validate_HTTPStrictTransportSecurity(OrganizationsDetails);
		Assert.assertEquals(OrganizationsDetails.statusCode(), 200);
		/**
		 * Extent Report Generation
		 */

		ExtentTestManager.statusLogMessage(OrganizationsDetails.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, OrganizationsDetails.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(OrganizationsDetails);

	}

	@Test(groups = "IntegrationTests")
	public void Organisation_GetThePermissionForTheAuthUser() {
		/**
		 * PERFORMING THE GET OPERATION ----->Get the permissions for the
		 * authenticated user
		 */

		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response OrganizationsDetails = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/user");
		OrganizationsDetails.prettyPrint();
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		// OrganizationsGet.validate_HTTPStrictTransportSecurity(OrganizationsDetails);
		Assert.assertEquals(OrganizationsDetails.statusCode(), 200);

		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(OrganizationsDetails.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, OrganizationsDetails.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(OrganizationsDetails);

	}

	@Test(groups = "IntegrationTests")
	public void Organisation_GetQueryForUsers_status200() throws Exception {
		/**
		 * FETCHING THE ORGANISATION LIST
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");

		/**
		 * FETCHING THE RESOURCE ID FROM THE ORGANISATION
		 */
		Response getResourceId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/user");
		getResourceId.prettyPrint();
		JsonPath jsonPathEvaluator1 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator1.get("permissions.resource");

		System.out.println("----" + listResourceId);
		/**
		 * PERFORMING THE GET OPERATION
		 */

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_WithOne_QueryParams(URL, AuthorizationKey,
				"/api/org/user/", listResourceId.get(0));
		getMethodologyByIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);
		/**
		 * Extent Report Generation
		 */

		ExtentTestManager.statusLogMessage(getMethodologyByIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByIdRes.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(getMethodologyByIdRes);

	}

	@Test(groups = "IntegrationTests")
	public void Organisation_GetQueryForUsers_status400() throws Exception {

		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		Response postOrganizationData = Organization.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/users");
		postOrganizationData.prettyPrint();

		// Organization.validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 400);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(postOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postOrganizationData.asString());
		Organization.validate_HTTPStrictTransportSecurity(postOrganizationData);
	}

	// @Test(groups = { "IntegrationTests" })
	public void Organisation_PostCreateNewOrg200() throws Exception {
		/**
		 * CREATING THE NEW ORGANISATION
		 */
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		Organization_Pojo or = new Organization_Pojo();
		or.setMemberFirmId(post.getProperty("postOrgMemberFirmId") + getRandomAlphaNum());
		or.setName(post.getProperty("postOrgname") + getRandomAlphaNum());
		or.setCountryCode(post.getProperty("postOrgCountryCode"));

		Response getMethodologyByrevisonIdRes = Organization.post_URLPOJO(URL, AuthorizationKey, "/api/org", or);
		getMethodologyByrevisonIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByrevisonIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByrevisonIdRes.asString());
		Organization.validate_HTTPStrictTransportSecurity(getMethodologyByrevisonIdRes);

	}

	// @Test(groups = "IntegrationTests")
	public void Organisation_PatchCreateNewOrg200() throws Exception {

		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response OrganizationsDetails = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		OrganizationsDetails.prettyPrint();
		JsonPath jsonEvaluavator = OrganizationsDetails.jsonPath();
		listOrgId = jsonEvaluavator.get("id");

		/**
		 * PERFORMING THE PATCH OPERATION FOR THE ORGANISATION
		 */
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		String orgId = post.getProperty("patchOrganisationId");
		String patchId = "/api/org/" + listOrgId.get(8);

		Organization_Pojo or = new Organization_Pojo();
		or.setMemberFirmId(post.getProperty("postOrgMemberFirmId"));
		or.setName(post.getProperty("postOrgname") + getRandomAlphaNum() + Organization.getRandomNumber(1, 20));

		Response patchOrgRes = Organization.patch_URLPOJO(URL, AuthorizationKey, patchId, or);
		patchOrgRes.prettyPrint();

		// Organization.validate_HTTPStrictTransportSecurity(patchOrgRes);

		Assert.assertEquals(patchOrgRes.statusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(patchOrgRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchOrgRes.asString());
		Organization.validate_HTTPStrictTransportSecurity(patchOrgRes);
	}

	@Test(groups = "IntegrationTests")
	public void Organisation_RemovePublishedStoreMethodologyFromOrganizationsHiddenMethodologyList(){
		
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response OrganizationsDetails = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		OrganizationsDetails.prettyPrint();
		JsonPath jsonEvaluavator=OrganizationsDetails.jsonPath();
		listOrgId=jsonEvaluavator.get("id");
		String OrganisationId=listOrgId.get(5);
		System.out.println("Organisation Id----->"+OrganisationId);
		
		
		String URI="/api/methodology/store"; 
		Response methodologyStore=OrganizationsGet.get_URL_Without_Params(URL,AuthorizationKey,URI);
		methodologyStore.prettyPrint();
		JsonPath methodologyJson=methodologyStore.jsonPath();
		listMethodologyId=methodologyJson.get("id");
		String methodologyId=listMethodologyId.get(0);
		System.out.println("MethodologyId------>"+methodologyId);
		/**
		 * performing post operation
		 * 
		 */
		HashMap<String,String>map=new HashMap<String, String>();
		map.put("methodologyId",methodologyId);
		User_Pojo po=new User_Pojo();
		String body=po.createMethodologyStore(map);
		String postURI="/api/org/"+OrganisationId+"/methodology";
		Response postMethodology=OrganizationsGet.post_URL(URL, AuthorizationKey, postURI, body);
		postMethodology.prettyPrint();
			
		/**
		 * Performing the deletion
		 */
		String deleteURI="/api/org/"+OrganisationId+"/methodology/"+methodologyId;
		Response deleteMethodology=OrganizationsGet.delete(URL, AuthorizationKey, deleteURI);
		deleteMethodology.prettyPrint();
		String result=deleteMethodology.getBody().asString();
		System.out.println("REsult====>"+result);
		Assert.assertEquals(result.contains("true"),true);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(deleteMethodology.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteMethodology.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(deleteMethodology);
	}
	@Test(groups = "IntegrationTests")
	public void Organisation_CreatePublishedStoreMethodologyFromOrganizationsHiddenMethodologyList(){
		
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response OrganizationsDetails = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		OrganizationsDetails.prettyPrint();
		JsonPath jsonEvaluavator=OrganizationsDetails.jsonPath();
		listOrgId=jsonEvaluavator.get("id");
		String OrganisationId=listOrgId.get(5);
		System.out.println("Organisation Id----->"+OrganisationId);
		
		
		String URI="/api/methodology/store"; 
		Response methodologyStore=OrganizationsGet.get_URL_Without_Params(URL,AuthorizationKey,URI);
		methodologyStore.prettyPrint();
		JsonPath methodologyJson=methodologyStore.jsonPath();
		listMethodologyId=methodologyJson.get("id");
		String methodologyId=listMethodologyId.get(1);
		System.out.println("MethodologyId------>"+methodologyId);
		/**
		 * Performing the deletion
		 */
		String deleteURI="/api/org/"+OrganisationId+"/methodology/"+methodologyId;
		Response deleteMethodology=OrganizationsGet.delete(URL, AuthorizationKey, deleteURI);
		deleteMethodology.prettyPrint();
		
		/**
		 * performing post operation
		 * 
		 */
		HashMap<String,String>map=new HashMap<String, String>();
		map.put("methodologyId",methodologyId);
		User_Pojo po=new User_Pojo();
		String body=po.createMethodologyStore(map);
		String postURI="/api/org/"+OrganisationId+"/methodology";
		Response postMethodology=OrganizationsGet.post_URL(URL, AuthorizationKey, postURI, body);
		postMethodology.prettyPrint();
		String result=postMethodology.getBody().asString();
		System.out.println("REsult====>"+result);	
		Assert.assertEquals(result.contains("true"),true);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(postMethodology.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postMethodology.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(postMethodology);
	}
	// @Test(groups = "EndToEnd")
	public void Organisation_EndToEnd_Scenario() throws JsonIOException, JsonSyntaxException, IOException {
		/**
		 * CRREATING THE NEW ORGANISATION
		 */
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		Organization_Pojo or = new Organization_Pojo();
		or.setMemberFirmId(post.getProperty("postOrgMemberFirmId") + Organization.getRandomNumber(1, 20));
		or.setName(post.getProperty("postOrgname") + Organization.getRandomNumber(1, 20));
		or.setCountryCode(post.getProperty("postOrgCountryCode"));

		Response getMethodologyByrevisonIdRes = Organization.post_URLPOJO(URL, AuthorizationKey, "/api/org", or);
		getMethodologyByrevisonIdRes.prettyPrint();

		JsonPath jsonEvaluvator = getMethodologyByrevisonIdRes.jsonPath();
		String memberFirmid = jsonEvaluvator.get("memberFirmId");
		String name = jsonEvaluvator.get("name");
		String countryCode = jsonEvaluvator.get("countryCode");
		String orgId = jsonEvaluvator.get("id");

		/**
		 * UPDATING THE ORGANISATION
		 */

		Organization_Pojo or1 = new Organization_Pojo();

		or1.setMemberFirmId(memberFirmid);
		or1.setName(post.getProperty("postOrgname") + Organization.getRandomNumber(1, 100));
		or1.setCountryCode(post.getProperty("postOrgCountryCode"));
		String patchId = "/api/org/" + orgId;

		Response patchOrgRes = Organization.patch_URLPOJO(URL, AuthorizationKey, patchId, or1);
		patchOrgRes.prettyPrint();

		/**
		 * FETCHING THE ORGANISATION ID
		 */

		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		getOrgId.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getOrgId.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getOrgId.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(getOrgId);

	}

}
