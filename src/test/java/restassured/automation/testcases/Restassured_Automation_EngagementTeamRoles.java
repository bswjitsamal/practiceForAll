package restassured.automation.testcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.EngagementTeamRoles_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_EngagementTeamRoles extends read_Configuration_Propertites{
	
	

	
	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrdId;
	List<String> engagementRoleId;
	String methodologyId;
	String engagementTypeId;
	
	read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
	Restassured_Automation_Utils restUtils = new Restassured_Automation_Utils();

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 5);
	}

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		// read_Configuration_Propertites configDetails = new
		// read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");	
		
		Awaitility.reset();
        Awaitility.setDefaultPollDelay(999, MILLISECONDS);
        Awaitility.setDefaultPollInterval(99, SECONDS);
        Awaitility.setDefaultTimeout(99, SECONDS);

	}
	
	@Test(groups = { "IntegrationTests" }, priority =1)
	public void EngagementTeamRoles_GetLoadAllEngagementRolesInformationForAnOrganization_status200() throws IOException {

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

		Response getEngagementTeamRoles = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, "/api/engagementTeam/org/"+listOrdId.get(3)+"/roles");
		getEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(getEngagementTeamRoles.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getEngagementTeamRoles.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(getEngagementTeamRoles);
		
	}
	
	@Test(groups = { "IntegrationTests" }, priority =2)
	public void EngagementTeamRoles_PostCreateAnEngagementRoleForAnOrganization_status200() throws IOException {

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
		// step 3 perfrom validate
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000001");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("Ss"+getRandomAlphaNum());
		

		Response postEngagementTeamRoles = getMethodology.post_URLPOJO(URL, AuthorizationKey, 
				"/api/engagementTeam/org/"+listOrdId.get(3)+"/roles", engagementTeamRolesPojo);
		postEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(postEngagementTeamRoles.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postEngagementTeamRoles.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(postEngagementTeamRoles);
		
	}
	@Test(groups = { "IntegrationTests" }, priority =2)
	public void EngagementTeamRoles_PostCreateAnEngagementRoleForAnOrganization_status404() throws IOException {

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
		// step 3 perfrom validate
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000001");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("Abc2"+getRandomAlphaNum());
		

		Response postEngagementTeamRoles = getMethodology.post_URLPOJO(URL, AuthorizationKey, 
				"/api/engagementTeams/org/"+listOrdId.get(3)+"/roles", engagementTeamRolesPojo);
		postEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(postEngagementTeamRoles.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postEngagementTeamRoles.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(postEngagementTeamRoles);
		
	}
	
	@Test(groups = { "IntegrationTests" }, priority =3)
	public void EngagementTeamRoles_PostCreateAnEngagementRoleForAnOrganization_status409() throws IOException {

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
		// step 3 perfrom validate
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000002");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("Abc1");
		

		Response postEngagementTeamRoles = getMethodology.post_URLPOJO(URL, AuthorizationKey, 
				"/api/engagementTeam/org/"+listOrdId.get(3)+"/roles", engagementTeamRolesPojo);
		postEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(postEngagementTeamRoles.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postEngagementTeamRoles.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(postEngagementTeamRoles);
	}

	@Test(groups = { "IntegrationTests" }, priority =4)
	public void EngagementTeamRoles_PostCreateAnEngagementRoleForAnOrganization_status400() throws IOException {

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
		// step 3 perfrom validate
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000002");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("");
		

		Response postEngagementTeamRoles = getMethodology.post_URLPOJO(URL, AuthorizationKey, 
				"/api/engagementTeam/org/"+listOrdId.get(3)+"/roles", engagementTeamRolesPojo);
		postEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(postEngagementTeamRoles.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postEngagementTeamRoles.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(postEngagementTeamRoles);
		
	}
	
	@Test(groups = { "IntegrationTests" }, priority =5)
	public void EngagementTeamRoles_PatchUpdateAnEngagementRoleForAnOrganization_status204() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ROLE ID
		 */
		Response getEngagementTeamRolesID = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/engagementTeam/org/"+listOrdId.get(3)+"/roles");
		JsonPath jsonPathEvaluator1 = getEngagementTeamRolesID.jsonPath();
		engagementRoleId = jsonPathEvaluator1.get("engagementRoleId");
		getEngagementTeamRolesID.prettyPrint();
		

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();
		// step 3 perfrom validate
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000002");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("Bs-Update"+getRandomAlphaNum());
		

		Response patchEngagementTeamRoles = allUtils.patch_URLPOJO(URL, AuthorizationKey, 
				"/api/engagementTeam/org/"+listOrdId.get(3)+"/roles/"+engagementRoleId.get(0), engagementTeamRolesPojo);
		patchEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(patchEngagementTeamRoles.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementTeamRoles.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(patchEngagementTeamRoles);
		
	}
	@Test(groups = { "IntegrationTests" }, priority =6)
	public void EngagementTeamRoles_PatchUpdateAnEngagementRoleForAnOrganization_status404() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ROLE ID
		 */
		Response getEngagementTeamRolesID = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/engagementTeam/org/"+listOrdId.get(3)+"/roles");
		JsonPath jsonPathEvaluator1 = getEngagementTeamRolesID.jsonPath();
		engagementRoleId = jsonPathEvaluator1.get("engagementRoleId");
		getEngagementTeamRolesID.prettyPrint();
		

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();
		// step 3 perfrom validate
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000001");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("Bs-Update"+getRandomAlphaNum());
		

		Response patchEngagementTeamRoles = allUtils.patch_URLPOJO(URL, AuthorizationKey, 
				"/api/engagementTeams/org/"+listOrdId.get(3)+"/roles/"+engagementRoleId.get(0), engagementTeamRolesPojo);
		patchEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(patchEngagementTeamRoles.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementTeamRoles.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(patchEngagementTeamRoles);
		
	}
	
	@Test(groups = { "IntegrationTests" }, priority =7)
	public void EngagementTeamRoles_PatchUpdateAnEngagementRoleForAnOrganization_status409() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ROLE ID
		 */
		Response getEngagementTeamRolesID = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/engagementTeam/org/"+listOrdId.get(3)+"/roles");
		JsonPath jsonPathEvaluator1 = getEngagementTeamRolesID.jsonPath();
		engagementRoleId = jsonPathEvaluator1.get("engagementRoleId");
		getEngagementTeamRolesID.prettyPrint();
		

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();
		// step 3 perfrom validate
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000002");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("Bs-Update");
		

		Response patchEngagementTeamRoles = allUtils.patch_URLPOJO(URL, AuthorizationKey, 
				"/api/engagementTeam/org/"+listOrdId.get(3)+"/roles/"+engagementRoleId.get(0), engagementTeamRolesPojo);
		patchEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(patchEngagementTeamRoles.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementTeamRoles.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(patchEngagementTeamRoles);
		
	}
	
	@Test(groups = { "IntegrationTests" }, priority =8)
	public void EngagementTeamRoles_PatchUpdateAnEngagementRoleForAnOrganization_status400() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ROLE ID
		 */
		Response getEngagementTeamRolesID = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/engagementTeam/org/"+listOrdId.get(3)+"/roles");
		JsonPath jsonPathEvaluator1 = getEngagementTeamRolesID.jsonPath();
		engagementRoleId = jsonPathEvaluator1.get("engagementRoleId");
		getEngagementTeamRolesID.prettyPrint();
		

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();
		// step 3 perfrom validate
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000002");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("");
		

		Response patchEngagementTeamRoles = allUtils.patch_URLPOJO(URL, AuthorizationKey, 
				"/api/engagementTeam/org/"+listOrdId.get(3)+"/roles/"+engagementRoleId.get(0), engagementTeamRolesPojo);
		patchEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(patchEngagementTeamRoles.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementTeamRoles.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(patchEngagementTeamRoles);
		
	}
	
	
	@Test(groups = { "IntegrationTests" }, priority =9)
	public void EngagementTeamRoles_DeleteDeleteAnEngagementRoleForAnOrganization_status204() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ROLE ID
		 */
		Response getEngagementTeamRolesID = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/engagementTeam/org/"+listOrdId.get(3)+"/roles");
		JsonPath jsonPathEvaluator1 = getEngagementTeamRolesID.jsonPath();
		engagementRoleId = jsonPathEvaluator1.get("engagementRoleId");
		getEngagementTeamRolesID.prettyPrint();
		

		Response patchEngagementTeamRoles = allUtils.delete(URL, AuthorizationKey, 
				"/api/engagementTeam/org/"+listOrdId.get(3)+"/roles/"+engagementRoleId.get(0));
		patchEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(patchEngagementTeamRoles.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementTeamRoles.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchEngagementTeamRoles);
		
	}
	@Test(groups = { "IntegrationTests" }, priority =10)
	public void EngagementTeamRoles_DeleteDeleteAnEngagementRoleForAnOrganization_status404() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ROLE ID
		 */
		Response getEngagementTeamRolesID = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/engagementTeam/org/"+listOrdId.get(3)+"/roles");
		JsonPath jsonPathEvaluator1 = getEngagementTeamRolesID.jsonPath();
		engagementRoleId = jsonPathEvaluator1.get("engagementRoleId");
		getEngagementTeamRolesID.prettyPrint();
		

		Response patchEngagementTeamRoles = allUtils.delete(URL, AuthorizationKey, 
				"/api/engagementTeams/org/"+listOrdId.get(3)+"/roles/"+engagementRoleId.get(0));
		patchEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(patchEngagementTeamRoles.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementTeamRoles.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchEngagementTeamRoles);
		
	}
	
	@Test(groups = { "IntegrationTests" }, priority =11)
	public void EngagementTeamRoles_DeleteAnEngagementRoleForAnOrganization_status400() throws IOException {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE ROLE ID
		 */
		Response getEngagementTeamRolesID = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/engagementTeam/org/"+listOrdId.get(3)+"/roles");
		JsonPath jsonPathEvaluator1 = getEngagementTeamRolesID.jsonPath();
		engagementRoleId = jsonPathEvaluator1.get("engagementRoleId");
		getEngagementTeamRolesID.prettyPrint();		
		

		Response patchEngagementTeamRoles = allUtils.delete(URL, AuthorizationKey, 
				"/api/engagementTeam/org/"+listOrdId.get(3)+"/roles/6050fec083bab05523e2fbf6");
		patchEngagementTeamRoles.prettyPrint();
		
		Assert.assertEquals(patchEngagementTeamRoles.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchEngagementTeamRoles.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchEngagementTeamRoles.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchEngagementTeamRoles);
		
	}
	
	
	@Test(groups = { "IntegrationTests" }, priority =12)
	public void EngagementTeamRoles_GetLoadAllEngagementRolesInformationForARevision_status200() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");

		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		Response createPublish = getMethodology.get_URL_Without_Params(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles");
		createPublish.prettyPrint();
		
		Assert.assertEquals(createPublish.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(createPublish);

	}
	
	
	@Test(groups = { "IntegrationTests" }, priority =13)
	public void EngagementTeamRoles_PostCreateAnEngagementRoleForAMethodology_status200() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");
		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000002");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("Tst"+getRandomAlphaNum());
		
		
		Response createPublish = getMethodology.post_URLPOJO(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles",engagementTeamRolesPojo);
		createPublish.prettyPrint();
		
		Assert.assertEquals(createPublish.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(createPublish);


	}
	@Test(groups = { "IntegrationTests" }, priority =14)
	public void EngagementTeamRoles_PostCreateAnEngagementRoleForAMethodology_status404() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");
		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000002");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("Tst"+getRandomAlphaNum());
		
		
		Response createPublish = getMethodology.post_URLPOJO(URL, AuthorizationKey,
				"/api/engagementTeams/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles",engagementTeamRolesPojo);
		createPublish.prettyPrint();
		
		Assert.assertEquals(createPublish.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(createPublish);


	}
	@Test(groups = { "IntegrationTests" }, priority =15)
	public void EngagementTeamRoles_PostCreateAnEngagementRoleForAMethodology_status400() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");
		
		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf5");
		engagementTeamRolesPojo.setOrganization("5f950768fc13ae0ade000002");
		engagementTeamRolesPojo.setRevision("6065690a2477890efb183f90");
		engagementTeamRolesPojo.setTitle("");
		
		
		Response createPublish = getMethodology.post_URLPOJO(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles",engagementTeamRolesPojo);
		createPublish.prettyPrint();
		
		Assert.assertEquals(createPublish.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(createPublish);


	}
	
	@Test(groups = { "IntegrationTests" }, priority =16)
	public void EngagementTeamRoles_PostCreateAnEngagementRoleForAMethodology_status409() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");

		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		EngagementTeamRoles_Pojo engagementTeamRolesPojo = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo.setEngagementRoleId("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo.setOrganization("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo.setRevision("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo.setTitle("string");
		
		
		Response createPublish = getMethodology.post_URLPOJO(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles",engagementTeamRolesPojo);
		createPublish.prettyPrint();
		
		Assert.assertEquals(createPublish.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(createPublish);


	}
	
	@Test(groups = { "IntegrationTests" }, priority =17)
	public void EngagementTeamRoles_PatchUpdateAnEngagementRoleForAMethodology_status204() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");

		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		//Retriving th eroleID
		
		Response createPublish = getMethodology.get_URL_Without_Params(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles");
		createPublish.prettyPrint();
		
		
		JsonPath jsonPathEvaluator2 = createPublish.jsonPath();
		List<String> engagementRoleId = jsonPathEvaluator2.get("engagementRoleId");
		System.out.println(engagementRoleId.get(0));
		
		//Performing Patch operation
		EngagementTeamRoles_Pojo engagementTeamRolesPojo1 = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo1.setEngagementRoleId("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setOrganization("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setRevision("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setTitle("Test"+getRandomAlphaNum());
		
		Response patchPublish = getMethodology.patch_URLPOJO(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles/"+engagementRoleId.get(0),engagementTeamRolesPojo1);
		patchPublish.prettyPrint();
		
		
		Assert.assertEquals(patchPublish.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(patchPublish);


	}
	@Test(groups = { "IntegrationTests" }, priority =18)
	public void EngagementTeamRoles_PatchUpdateAnEngagementRoleForAMethodology_status404() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");

		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		//Retriving th eroleID
		
		Response createPublish = getMethodology.get_URL_Without_Params(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles");
		createPublish.prettyPrint();
		
		
		JsonPath jsonPathEvaluator2 = createPublish.jsonPath();
		List<String> engagementRoleId = jsonPathEvaluator2.get("engagementRoleId");
		System.out.println(engagementRoleId);
		
		//Performing Patch operation
		EngagementTeamRoles_Pojo engagementTeamRolesPojo1 = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo1.setEngagementRoleId("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setOrganization("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setRevision("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setTitle("Test"+getRandomAlphaNum());
		
		Response patchPublish = getMethodology.patch_URLPOJO(URL, AuthorizationKey,
				"/api/engagementTeams/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles/"+engagementRoleId.get(0),engagementTeamRolesPojo1);
		patchPublish.prettyPrint();
		
		
		Assert.assertEquals(patchPublish.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(patchPublish);


	}
	
	@Test(groups = { "IntegrationTests" }, priority =19)
	public void EngagementTeamRoles_PatchUpdateAnEngagementRoleForAMethodology_status409() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");

		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		//Retriving th eroleID
		
		Response createPublish = getMethodology.get_URL_Without_Params(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles");
		createPublish.prettyPrint();
		
		
		JsonPath jsonPathEvaluator2 = createPublish.jsonPath();
		List<String> engagementRoleId = jsonPathEvaluator2.get("engagementRoleId");
		System.out.println(engagementRoleId);
		
		//Performing Patch operation
		EngagementTeamRoles_Pojo engagementTeamRolesPojo1 = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo1.setEngagementRoleId("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setOrganization("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setRevision("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setTitle("Test");
		
		Response patchPublish = getMethodology.patch_URLPOJO(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles/"+engagementRoleId.get(0),engagementTeamRolesPojo1);
		patchPublish.prettyPrint();
		
		
		Assert.assertEquals(patchPublish.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(patchPublish);
	}
	
	@Test(groups = { "IntegrationTests" }, priority =20)
	public void EngagementTeamRoles_PatchUpdateAnEngagementRoleForAMethodology_status400() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");

		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		//Retriving th eroleID
		
		Response createPublish = getMethodology.get_URL_Without_Params(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles");
		createPublish.prettyPrint();
		
		
		JsonPath jsonPathEvaluator2 = createPublish.jsonPath();
		List<String> engagementRoleId = jsonPathEvaluator2.get("engagementRoleId");
		System.out.println(engagementRoleId);
		
		//Performing Patch operation
		EngagementTeamRoles_Pojo engagementTeamRolesPojo1 = new EngagementTeamRoles_Pojo();
		engagementTeamRolesPojo1.setEngagementRoleId("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setOrganization("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setRevision("6050fec083bab05523e2fbf6");
		engagementTeamRolesPojo1.setTitle("");
		
		Response patchPublish = getMethodology.patch_URLPOJO(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles/"+engagementRoleId.get(0),engagementTeamRolesPojo1);
		patchPublish.prettyPrint();
		
		
		Assert.assertEquals(patchPublish.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,patchPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(patchPublish);

	}

	@Test(groups = { "IntegrationTests" }, priority =21)
	public void EngagementTeamRoles_DeleteAnEngagementRoleForAMethodology_status204() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");

		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		//Retriving th eroleID
		
		Response createPublish = getMethodology.get_URL_Without_Params(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles");
		createPublish.prettyPrint();
		
		
		JsonPath jsonPathEvaluator2 = createPublish.jsonPath();
		List<String> engagementRoleId = jsonPathEvaluator2.get("engagementRoleId");
		System.out.println(engagementRoleId);
		
		String id = engagementRoleId.get(0);
		
		Response dalatePublish = getMethodology.delete(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles/"+id);
		dalatePublish.prettyPrint();		
		
		Assert.assertEquals(dalatePublish.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(dalatePublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,dalatePublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(dalatePublish);

	}

	@Test(groups = { "IntegrationTests" }, priority =22)
	public void EngagementTeamRoles_DeleteAnEngagementRoleForAMethodology_status404() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");

		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		//Retriving th eroleID
		
		Response createPublish = getMethodology.get_URL_Without_Params(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles");
		createPublish.prettyPrint();
		
		
		JsonPath jsonPathEvaluator2 = createPublish.jsonPath();
		List<String> engagementRoleId = jsonPathEvaluator2.get("engagementRoleId");
		System.out.println(engagementRoleId);
		
		String id = engagementRoleId.get(0);
		
		Response dalatePublish = getMethodology.delete(URL, AuthorizationKey,
				"/api/engagementTeams/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles/"+id);
		dalatePublish.prettyPrint();		
		
		Assert.assertEquals(dalatePublish.statusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(dalatePublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,dalatePublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(dalatePublish);

	}

	@Test(groups = { "IntegrationTests" }, priority =23)
	public void EngagementTeamRoles_DeleteAnEngagementRoleForAMethodology_status400() throws IOException {

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
				"Organization", listOrdId.get(3));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions");

		Object[] revId = listRevisionId.toArray();
		
		String reviD = revId[revId.length-1].toString();
		System.out.println(reviD);
		
		//Retriving th eroleID
		
		Response createPublish = getMethodology.get_URL_Without_Params(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles");
		createPublish.prettyPrint();
		
		
		JsonPath jsonPathEvaluator2 = createPublish.jsonPath();
		List<String> engagementRoleId = jsonPathEvaluator2.get("engagementRoleId");
		System.out.println(engagementRoleId);
		
		String id = engagementRoleId.get(1);
		
		Response dalatePublish = getMethodology.delete(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1, 25)+"/roles/"+id);
		dalatePublish.prettyPrint();
		
		Response deletePublishAgain = getMethodology.delete(URL, AuthorizationKey,
				"/api/engagementTeam/org/" +listOrdId.get(3) + "/revision/" + reviD.substring(1,25)+"/roles/"+id);
		deletePublishAgain.prettyPrint();
		
		
		Assert.assertEquals(deletePublishAgain.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deletePublishAgain.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,deletePublishAgain.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(deletePublishAgain);

	}

	


}
