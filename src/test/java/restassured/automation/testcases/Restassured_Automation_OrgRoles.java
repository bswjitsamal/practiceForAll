package restassured.automation.testcases;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.OrgAdminRolePojo;
import restassured.automation.Pojo.OrgRolesPojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_OrgRoles {
	
	String URL;
	String AuthorizationKey;
	List<String> listOrgId;
	List<String> listResourceId;
	List<String> listUserId;
	List<String> listRole;
	List<String> listUserDisplayName;
	List<String> listUserEmail;
	List<String> listOrganization;
	List<String> listUser;
	List<String> listId;
	List<String> listPermission;
	
	
	
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
	public void getTheListOfRoleTemplate_status200() {

		String patchId = "/api/org/roles/templates";
		
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getMethodologyByIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);

	}
	
	
	@Test(groups = "IntegrationTests")
	public void putUpdateThePermissionForARole_status200() {

		String patchId = "/api/org/roles/templates";
		
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getPermissionSetsId = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getPermissionSetsId.prettyPrint();
		
		JsonPath jsonPathEvaluator = getPermissionSetsId.jsonPath();
	    //listOrgId = jsonPathEvaluator.getList("permissionGroups.permissionSets.id");
	    List<Object> listRevisionI1 = jsonPathEvaluator.getList("permissionGroups.permissionSets.id");
		System.out.println(listRevisionI1.get(0));

		/**
		 * Performing the put operation now
		 */
		OrgRolesPojo or = new OrgRolesPojo();
		or.setPermissionSets(listRevisionI1.get(0).toString());
		
		String roleId = String.valueOf(listRevisionI1.get(0));
		
		String patchId1 = "/api/org/roles/"+roleId.substring(1, roleId.length() - 1)+"/permissions/";
		Response patchPermissionId = getMethodologyById.patch_URLPOJO(URL, AuthorizationKey, patchId1, or);
		patchPermissionId.prettyPrint();
		
		
		Assert.assertEquals(getPermissionSetsId.statusCode(), 200);

	}
	
	
	@Test(groups = "IntegrationTests")
	public void getSuperUserFuntionGetAListOfAllPremissionForAUser_status200() {

		
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
		
		listResourceId = jsonPathEvaluator.get("id");
		
		
		String patchId = "/api/org/" + listOrgId.get(4) +"/resource/"+listResourceId.get(4)+"/roles";
		
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getOrgRoleId = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getOrgRoleId.prettyPrint();
		
		/**
		 * Getting the UserId
		 * 
		 */
		JsonPath jsonPathEvaluator1 = getOrgRoleId.jsonPath();
		listUserId = jsonPathEvaluator1.get("userId");
		//listUserId = jsonPathEvaluator.get("userId");
		System.out.println("-------"+listUserId);
		//String userId = jsonPathEvaluator.get("userId");
		
		String patchId1 = "/api/org/" + listOrgId.get(1) +"/user/"+listUserId.get(1)+"/roles/associations";
		
		Response getRoleAssociate = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId1);
		getRoleAssociate.prettyPrint();
		
		/**
		 * Getting the permission for the same 
		 */
		
		String patchId2 = "/api/org/user/" +listUserId.get(1)+"/permissions";
		Response getPermission = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId2);
		getPermission.prettyPrint();
		Assert.assertEquals(getPermission.statusCode(), 200);
		
	}
	
	
	@Test(groups = "IntegrationTests")
	public void postSuperUserFuntionCreateANewUserPremission_status200() {

		
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
		/*Response getResourceId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/user");
		getResourceId.prettyPrint();*/
		
		listResourceId = jsonPathEvaluator.get("id");
		
		
		String patchId = "/api/org/" + listOrgId.get(4) +"/resource/"+listResourceId.get(4)+"/roles";
		
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getOrgRoleId = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getOrgRoleId.prettyPrint();
		
		/**
		 * Getting the UserId
		 * 
		 */
		JsonPath jsonPathEvaluator1 = getOrgRoleId.jsonPath();
		listUserId = jsonPathEvaluator1.get("userId");
		//listUserId = jsonPathEvaluator.get("userId");
		System.out.println("-------"+listUserId);
		//String userId = jsonPathEvaluator.get("userId");
		
		String patchId1 = "/api/org/" + listOrgId.get(1) +"/user/"+listUserId.get(1)+"/roles/associations";
		
		Response getRoleAssociate = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId1);
		getRoleAssociate.prettyPrint();
		
		/**
		 * Getting the permission for the same 
		 */
		
		String patchId2 = "/api/org/user/" +listUserId.get(1)+"/permissions";
		Response getPermission = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId2);
		getPermission.prettyPrint();
		Assert.assertEquals(getPermission.statusCode(), 200);
		
		
		/**
		 * Performing the post operation 
		 */
		
		JsonPath jsonPathEvaluator2 = getPermission.jsonPath();
		
		listOrganization = jsonPathEvaluator2.get("organization");
		listResourceId =  jsonPathEvaluator2.get("resource");
		listPermission =  jsonPathEvaluator2.get("permission");
		listId =  jsonPathEvaluator2.get("id");
		listUserId =  jsonPathEvaluator2.get("user");
		
		OrgAdminRolePojo oarp = new OrgAdminRolePojo();
		oarp.setOrganization(listOrganization.get(0));
		oarp.setResource(listResourceId.get(0));
		oarp.setPermission(listPermission.get(0));
		oarp.setId(listId.get(0));
		oarp.setUser(listUserId.get(0));
		
		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, patchId2, oarp);

		Assert.assertEquals(postEngagementType.statusCode(), 200);

	}
	
	
	@Test(groups = "IntegrationTests")
	public void deleteSuperUserFuntionCreateANewUserPremission_status204() {

		
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
		
		listResourceId = jsonPathEvaluator.get("id");
		
		
		String patchId = "/api/org/" + listOrgId.get(4) +"/resource/"+listResourceId.get(4)+"/roles";
		
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getOrgRoleId = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getOrgRoleId.prettyPrint();
		
		/**
		 * Getting the UserId
		 * 
		 */
		JsonPath jsonPathEvaluator1 = getOrgRoleId.jsonPath();
		listUserId = jsonPathEvaluator1.get("userId");
		//listUserId = jsonPathEvaluator.get("userId");
		System.out.println("-------"+listUserId);
		//String userId = jsonPathEvaluator.get("userId");
		
		String patchId1 = "/api/org/" + listOrgId.get(1) +"/user/"+listUserId.get(1)+"/roles/associations";
		
		Response getRoleAssociate = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId1);
		getRoleAssociate.prettyPrint();
		
		/**
		 * Getting the permission for the same 
		 */
		
		String patchId2 = "/api/org/user/" +listUserId.get(1)+"/permissions";
		Response getPermission = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId2);
		getPermission.prettyPrint();
		Assert.assertEquals(getPermission.statusCode(), 200);
		
		
		/**
		 * Performing the post operation 
		 */
		
		JsonPath jsonPathEvaluator2 = getPermission.jsonPath();
		
		listOrganization = jsonPathEvaluator2.get("organization");
		listResourceId =  jsonPathEvaluator2.get("resource");
		listPermission =  jsonPathEvaluator2.get("permission");
		listId =  jsonPathEvaluator2.get("id");
		listUserId =  jsonPathEvaluator2.get("user");
		
		OrgAdminRolePojo oarp = new OrgAdminRolePojo();
		oarp.setOrganization(listOrganization.get(0));
		oarp.setResource(listResourceId.get(0));
		oarp.setPermission(listPermission.get(0));
		oarp.setId(listId.get(0));
		oarp.setUser(listUserId.get(0));
		
		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();
		String patchId3 = "/api/org/user/" +listUserId.get(1)+"/permissions/"+listId.get(0);
		Response postEngagementType = engagementType.delete_URLPOJO(URL, AuthorizationKey, patchId3, oarp);

		Assert.assertEquals(postEngagementType.statusCode(), 204);

	}
	


	@Test(groups = "IntegrationTests")
	public void getTheListOfRoleDefinationsForTheOrganization_status200() {

		String patchId = "/api/org/roles";
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getMethodologyByIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void getTheListOfRolesAssociationsForTheUser_status200() {

		
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
		
		listResourceId = jsonPathEvaluator.get("id");
		
		
		String patchId = "/api/org/" + listOrgId.get(4) +"/resource/"+listResourceId.get(4)+"/roles";
		
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getOrgRoleId = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getOrgRoleId.prettyPrint();
		
		/**
		 * Getting the UserId
		 * 
		 */
		JsonPath jsonPathEvaluator1 = getOrgRoleId.jsonPath();
		listUserId = jsonPathEvaluator1.get("userId");
		//listUserId = jsonPathEvaluator.get("userId");
		System.out.println("-------"+listUserId);
		//String userId = jsonPathEvaluator.get("userId");
		
		String patchId1 = "/api/org/" + listOrgId.get(1) +"/user/"+listUserId.get(1)+"/roles/associations";
		
		Response getRoleAssociate = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId1);
		getRoleAssociate.prettyPrint();
		Assert.assertEquals(getRoleAssociate.statusCode(), 200);

	}
	
	@Test(groups = "IntegrationTests")
	public void getTheAssignedRolesForTheGivenResource_status200() {

		
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
		
		listResourceId = jsonPathEvaluator.get("id");
		
		
		String patchId = "/api/org/" + listOrgId.get(1) +"/resource/"+listResourceId.get(1)+"/roles";
		
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getMethodologyByIdRes.prettyPrint();
		
	
		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);

	}
	
	
	@Test(groups = "IntegrationTests")
	public void putAssignARoleForAPersonToAGivenReqource_status200() {

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

		listResourceId = jsonPathEvaluator.get("id");

		String patchId = "/api/org/" + listOrgId.get(4) + "/resource/" + listResourceId.get(4) + "/roles";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getMethodologyByIdRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyByIdRes.jsonPath();
		listRole = jsonPathEvaluator1.get("role");
		listUserId = jsonPathEvaluator1.get("userId");
		listUserDisplayName = jsonPathEvaluator1.get("userDisplayName");
		listUserEmail = jsonPathEvaluator1.get("userEmail");
		listResourceId = jsonPathEvaluator1.get("resource");
		

		System.out.println(listRole);
		
		OrgRolesPojo or = new OrgRolesPojo();
		or.setUserId(listUserId.get(0));
		or.setUserDisplayName(listUserDisplayName.get(0));
		or.setUserEmail(listUserEmail.get(0));
		or.setRole(listRole.get(1));
		or.setResource(listResourceId.get(0));
		
		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.put_URLPOJO(URL, AuthorizationKey, patchId, or);

		Assert.assertEquals(postEngagementType.statusCode(), 200);
	}
	
	@Test(groups = "IntegrationTests")
	public void deleteThePersonsAssignedRoleFromAGivenResource_status204() {

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

		listResourceId = jsonPathEvaluator.get("id");

		String patchId = "/api/org/" + listOrgId.get(4) + "/resource/" + listResourceId.get(4) + "/roles";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getMethodologyByIdRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyByIdRes.jsonPath();
		listRole = jsonPathEvaluator1.get("role");
		listUserId = jsonPathEvaluator1.get("userId");
		listUserDisplayName = jsonPathEvaluator1.get("userDisplayName");
		listUserEmail = jsonPathEvaluator1.get("userEmail");
		listResourceId = jsonPathEvaluator1.get("resource");
		

		System.out.println(listRole);
		
		OrgRolesPojo or = new OrgRolesPojo();
		or.setUserId(listUserId.get(0));
		or.setUserDisplayName(listUserDisplayName.get(0));
		or.setUserEmail(listUserEmail.get(0));
		or.setRole(listRole.get(0));
		or.setResource(listResourceId.get(0));
		
		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.delete_URLPOJO(URL, AuthorizationKey, patchId, or);

		Assert.assertEquals(postEngagementType.statusCode(), 204);

	}
	
	@Test(groups = "IntegrationTests")
	public void getAListOfAllPremissionForAUser_status200() {

		String patchId = "/api/org/roles";
		
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey,patchId);
		getMethodologyByIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);

	}
	
}
