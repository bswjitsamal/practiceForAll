package restassured.automation.testcases;

import java.io.FileNotFoundException;
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
import restassured.automation.Pojo.OrgRolesPojo;
import restassured.automation.Pojo.Organization_Pojo;
import restassured.automation.Pojo.ServiceDetailsPojo;
import restassured.automation.Pojo.User_Pojo;
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

	@Test(groups = { "IntegrationTests" })
	public void getTheListOfOrganization() {

		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response OrganizationsDetails = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		OrganizationsDetails.prettyPrint();

		Assert.assertEquals(OrganizationsDetails.statusCode(), 200);

	}

	@Test(groups = { "IntegrationTests" })
	public void getThePermissionForTheAuthUser() {

		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response OrganizationsDetails = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/user");
		OrganizationsDetails.prettyPrint();

		Assert.assertEquals(OrganizationsDetails.statusCode(), 200);

	}
	
	@Test(groups = { "IntegrationTests" })
	public void getQueryForUsers_status200() throws Exception {
		/**
		 * Getting the orgId
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");

		/**
		 * Get the resourceId
		 */
		Response getResourceId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/user");
		getResourceId.prettyPrint();
		JsonPath jsonPathEvaluator1 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator1.get("permissions.id");
		
		System.out.println("----"+listResourceId);

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_WithOne_QueryParams(URL, AuthorizationKey,"/api/org/uers/", listResourceId.get(0));
		getMethodologyByIdRes.prettyPrint();
		
}

	@Test(groups = { "IntegrationTests" })
	public void getQueryForUsers_status400() throws Exception {

		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();


		Response postOrganizationData = Organization.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/users");
		postOrganizationData.prettyPrint();

		Assert.assertEquals(postOrganizationData.statusCode(), 400);
	}

	//@Test(groups = { "IntegrationTests" })
	public void postCreateNewOrg200() throws Exception {
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();
		
		Organization_Pojo or = new Organization_Pojo();
		or.setMemberFirmId(post.getProperty("postOrgMemberFirmId")+getRandomAlphaNum());
		or.setName(post.getProperty("postOrgname")+getRandomAlphaNum());
		or.setCountryCode(post.getProperty("postOrgCountryCode"));
		

		Response getMethodologyByrevisonIdRes = Organization.post_URLPOJO(URL, AuthorizationKey,
				"/api/org", or);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);
	}
	
	
	@Test(groups = { "IntegrationTests" })
	public void patchCreateNewOrg200() throws Exception {
		
		/*Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();
		
		Organization_Pojo or = new Organization_Pojo();
		or.setMemberFirmId(post.getProperty("postOrgMemberFirmId")+getRandomAlphaNum());
		or.setName(post.getProperty("postOrgname")+getRandomAlphaNum());
		or.setCountryCode(post.getProperty("postOrgCountryCode"));

		Response getMethodologyByrevisonIdRes = Organization.post_URLPOJO(URL, AuthorizationKey,
				"/api/org", or);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);
		
		
		*//**
		 * PERFORMING PATCH
		 *//*
		
		
		
		JsonPath jsonPathEvaluator = getMethodologyByrevisonIdRes.jsonPath();
		String orgId = jsonPathEvaluator.get("id");*/
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		
		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();
		String demoId = "601ebf07c85e05a3484c8dd9";
		String patchId = "/api/org/"+ demoId;
		
		Organization_Pojo or = new Organization_Pojo();
		//or.setMemberFirmId(post.getProperty("postOrgMemberFirmId")+getRandomAlphaNum());
		or.setName(post.getProperty("postOrgname")+getRandomAlphaNum());
		//or.setCountryCode(post.getProperty("postOrgCountryCode"));
		
		Response patchOrgRes = Organization.patch_URLPOJO(URL, AuthorizationKey,
				patchId, or);
		patchOrgRes.prettyPrint();

		Assert.assertEquals(patchOrgRes.statusCode(), 200);
	}
	
	
	@Test(groups = "IntegrationTests")
	public void putUpdateThePermissionForARole_status200() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		
		String organizationId = "5f950768fc13ae0ade000002";
		String roleId = "60084f76fc13ae28ac0000e2";
		String patchId = "/api/org/" + organizationId +"/roles/"+roleId+"/permissions";
		
		OrgRolesPojo orR = new OrgRolesPojo();
		orR.setUserId("2a5a09bc-4c0d-44f9-8acc-46d4521d1394");
		orR.setUserDisplayName("MMS Test Amer 1-update");
		orR.setUserEmail("mms.test.amer.1@gtadhoc.gt.com");
		orR.setRole("60084f76fc13ae28ac0000e1");
		orR.setResource("60156034d808fd57d5fd9709");
		
		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		Response putOrganizationRuleData = Organization.put_URLPOJO(URL, AuthorizationKey, patchId, orR);
		Assert.assertEquals(putOrganizationRuleData.statusCode(), 200);

		putOrganizationRuleData.prettyPrint();

	}
	
	@Test(groups = "IntegrationTests")
	public void putAssignedARoleForAPersonToAGivenResource_status200() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		
		String organizationId = "5f950768fc13ae0ade000002";
		String resourceId = "5f950768fc13ae0ade000002";
		String patchId = "/api/org/" + organizationId +"/roles/resource/"+resourceId;
		
		OrgRolesPojo orR = new OrgRolesPojo();
		orR.setUserId("2a5a09bc-4c0d-44f9-8acc-46d4521d1394");
		orR.setUserDisplayName("MMS Test Amer 1-update");
		orR.setUserEmail("mms.test.amer.1@gtadhoc.gt.com");
		orR.setRole("60084f76fc13ae28ac0000e1");
		orR.setResource("60156034d808fd57d5fd9709");
		
		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		Response putOrganizationRuleData = Organization.put_URLPOJO(URL, AuthorizationKey, patchId, orR);
		Assert.assertEquals(putOrganizationRuleData.statusCode(), 200);

		putOrganizationRuleData.prettyPrint();

	}
	
	
	//@Test(groups = "IntegrationTests")
	public void deleteThePersonsAssignedRoleFromAGivenResources_status200() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		
		String organizationId = "5f950768fc13ae0ade000002";
		String resourceId = "5f950768fc13ae0ade000002";
		String patchId = "/api/org/" + organizationId +"/roles/resource/"+resourceId;
	
		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		Response putOrganizationRuleData = Organization.delete(URL, AuthorizationKey, patchId);
		Assert.assertEquals(putOrganizationRuleData.statusCode(), 200);

		putOrganizationRuleData.prettyPrint();

	}

	
	//@Test(groups = "IntegrationTests")
	public void putAssignsARoleForAPersonToAGivenResources() {

		String organizationId = "5f950768fc13ae0ade000002";
		String resourceId = "5f950768fc13ae0ade000002";
		String patchId = "/api/org/" + organizationId +"/roles/resource/"+resourceId;
		
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getMethodologyByIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);

	}

	//@Test(groups = { "EndToEnd" })
	public void Organizations_Scenario() throws JsonIOException, JsonSyntaxException, FileNotFoundException {

		String Organizations_Add = "Organizations_Add";
		String Organizations = "Organizations";

		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		DataSources_Read_Utils dataSource = new DataSources_Read_Utils();
		Map<String, String> Organizationdata = dataSource.Json_File_Reader(Organizations_Add, Organizations,
				"Organizations.json");

		Response postOrganizationData = Organization.post_URL(URL, AuthorizationKey, "/api/org", Organizationdata);
		Assert.assertEquals(postOrganizationData.statusCode(), 200);
		String idValue = postOrganizationData.then().extract().path("id").toString();

		Response getOrganization = Organization.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/list");
		Assert.assertEquals(getOrganization.statusCode(), 200);

		String patchId = "/api/org/" + idValue;

		Map<String, String> data = new HashMap<String, String>();
		data.put("countryCode", "91");
		data.put("name", "IND");

		User_Pojo updateOrganization = new User_Pojo();
		//String updateOrganizationData = updateOrganization.Update_Organization(data);

		//Response updateOrg = Organization.patch_URL(URL, AuthorizationKey, patchId, updateOrganizationData);
		//Assert.assertEquals(updateOrg.statusCode(), 204);

		Response deleteOrg = Organization.delete(URL, AuthorizationKey, patchId);
		//Assert.assertEquals(updateOrg.statusCode(), 204);

	}
	

	//@Test(groups = { "IntegrationTests" })
	public void Organizations_Add() throws Exception {

		String Organizations_Add = "Organizations_Add";
		String Organizations = "Organizations";

		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		/*
		 * Create_Organization_Pojo OrgData = new Create_Organization_Pojo(); String
		 * Organizationdata = OrgData.postOrganization();
		 */

		DataSources_Read_Utils dataSource = new DataSources_Read_Utils();
		Map<String, String> Organizationdata = dataSource.Json_File_Reader(Organizations_Add, Organizations,
				"Organizations.json");

		Response postOrganizationData = Organization.post_URL(URL, AuthorizationKey, "/api/org", Organizationdata);
		postOrganizationData.prettyPrint();

		String ActualName = postOrganizationData.jsonPath().get("id").toString();
		String ExpectedName = Organizationdata.get("name").toString();

		Assert.assertEquals(postOrganizationData.statusCode(), 200);
		Assert.assertEquals(postOrganizationData.jsonPath().get("name"), Organizationdata.get("name"));
		// Custom_Assertion.assertEqual(ActualName, ExpectedName);
		Assert.assertEquals(postOrganizationData.jsonPath().get("countryCode"), Organizationdata.get("name"));
		Assert.assertEquals(postOrganizationData.asString().contains("GER1"), true);

	}

	/*@Test(groups = { "IntegrationTests" })
	public void Organizations_Update() {

		Restassured_Automation_Utils UpdateOrganization = new Restassured_Automation_Utils();

		Map<String, String> data = new HashMap<String, String>();
		data.put("countryCode", "91");
		data.put("name", "IND");

		User_Pojo updateOrganization = new User_Pojo();
		String updateOrganizationData = updateOrganization.Update_Organization(data);

		Response UpdateOrganizationRes = UpdateOrganization.patch_URL(URL, AuthorizationKey,
				"/api/org/5fcdb842f110f428402a3bad", updateOrganizationData);
		UpdateOrganizationRes.prettyPrint();

		Assert.assertEquals(UpdateOrganizationRes.statusCode(), 204);

	}*/

	//@Test(groups = { "IntegrationTests" })
	public void Organizations_Delete() {

		Restassured_Automation_Utils DeleteOrganization = new Restassured_Automation_Utils();

		Map<String, String> data = new HashMap<String, String>();
		data.put("id", "5fcdb842f110f428402a3bad");

		User_Pojo deleteOrganization = new User_Pojo();
		String deleteOrganizationData = deleteOrganization.Delete(data);

		Response DeleteOrg = DeleteOrganization.delete(URL, AuthorizationKey, "/api/org/5fcdb842f110f428402a3bad",
				deleteOrganizationData);
		DeleteOrg.prettyPrint();

		Assert.assertEquals(DeleteOrg.statusCode(), 204);

	}
	
	//@Test(groups = "IntegrationTests", dataProvider = "dataSource", dataProviderClass = Excel_Data_Source_Utils.class)
		public void Organizations_Roles_Get_Status200(String orgId, String responseCode) {
			
			//String patchId = "/api/org/" + orgId + "/roles/templates";
			int statusCode = Integer.parseInt(responseCode);

			Restassured_Automation_Utils OrgRolesGet = new Restassured_Automation_Utils();

			Map<String, String> data = new HashMap<String, String>();
			data.put("orgId", orgId);

			User_Pojo getOrganisationRole = new User_Pojo();
			String fetchrganisationRole = getOrganisationRole.Get_Organization(data);
			System.out.println("--->" + fetchrganisationRole);

			Response orgRoleFetch = OrgRolesGet.get_URL_WithOne_PathParams(URL, AuthorizationKey, "\r\n" + 
					"​/api​/org​/{value}​/roles​/templates",fetchrganisationRole);
			orgRoleFetch.prettyPrint();

			Assert.assertEquals(orgRoleFetch.statusCode(), statusCode);

		}


}
