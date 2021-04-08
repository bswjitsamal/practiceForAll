package restassured.automation.testcases;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.OrgAdminRolePojo;
import restassured.automation.Pojo.OrgRolesPojo;

import restassured.automation.listeners.ExtentTestManager;
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
	List<String> listResourceType;

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz-";

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

	}

	@Test(groups = { "IntegrationTests" })
	public void OrganisationRoles_GetSuperUserFuntionGetAListOfAllPremissionForAUser_status200() {

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
		Response getResourceId = OrganizationsGet.get_URL_QueryParams(URL, AuthorizationKey, "/api/org/users",
				"Organization", listOrgId.get(5));
		getResourceId.prettyPrint();

		JsonPath jsonPathEvaluator2 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator2.get("id");

		/**
		 * GET A LIST ALL PERMISSION WRT TO A USER
		 * 
		 */

		String patchURI = "/api/org/user/" + listResourceId.get(1) + "/permissions";

		Response getPermission = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, patchURI);
		getPermission.prettyPrint();
		Assert.assertEquals(getPermission.statusCode(), 200);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getPermission.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getPermission.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(getPermission);

	}

	//@Test(groups = "IntegrationTests")
	public void OrganisationRoles_PostSuperUserFuntionCreateANewUserPremission_status200() {

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
		Response getResourceId = OrganizationsGet.get_URL_QueryParams(URL, AuthorizationKey, "/api/org/users",
				"Organization", listOrgId.get(5));
		getResourceId.prettyPrint();

		JsonPath jsonPathEvaluator3 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator3.get("id");

		/**
		 * GET A LIST ALL PERMISSION WRT TO A USER
		 * 
		 */

		String patchURI = "/api/org/user/" + listResourceId.get(1) + "/permissions";

		Response getPermission = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, patchURI);
		getPermission.prettyPrint();

		/**
		 * Performing the post operation
		 */

		JsonPath jsonPathEvaluator2 = getPermission.jsonPath();

		listOrganization = jsonPathEvaluator2.get("organization");
		listResourceId = jsonPathEvaluator2.get("resource");
		listPermission = jsonPathEvaluator2.get("permission");
		listResourceType = jsonPathEvaluator2.get("resourceType");
		listUserId = jsonPathEvaluator2.get("user");

		OrgAdminRolePojo oarp = new OrgAdminRolePojo();
		oarp.setOrganization(listOrganization.get(0));
		oarp.setResource(listResourceId.get(5));
		oarp.setPermission(listPermission.get(1));
		oarp.setId(listResourceType.get(4));
		oarp.setUser("edogi23gfhdjg-fnjkdsf-" + getRandomAlphaNum());

		String PostURI = "/api/org/user/" + listResourceId.get(1) + "/permissions";

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, PostURI, oarp);
		Assert.assertEquals(postEngagementType.statusCode(), 200);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postEngagementType.asString());
		engagementType.validate_HTTPStrictTransportSecurity(postEngagementType);

	}

	// @Test(groups = "IntegrationTests")
	public void OrganisationRoles_DeleteSuperUserFuntionCreateANewUserPremission_status204() {

		/**
		 * Getting the orgId
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		// getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");

		/**
		 * FETCHING THE LIST OF USERS
		 */
		Response getResourceId = OrganizationsGet.get_URL_QueryParams(URL, AuthorizationKey, "/api/org/users",
				"Organization", listOrgId.get(4));
		getResourceId.prettyPrint();

		JsonPath jsonPathEvaluator3 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator3.get("id");

		/**
		 * GET A LIST ALL PERMISSION WRT TO A USER
		 * 
		 */

		String patchURI = "/api/org/user/" + listResourceId.get(1) + "/permissions";

		Response getPermission = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, patchURI);
		getPermission.prettyPrint();
		Assert.assertEquals(getPermission.statusCode(), 200);

		/**
		 * PERFORMING THE DELETE OPERATION
		 */

		JsonPath jsonPathEvaluator2 = getPermission.jsonPath();

		listOrganization = jsonPathEvaluator2.get("organization");
		listResourceId = jsonPathEvaluator2.get("resource");
		listPermission = jsonPathEvaluator2.get("permission");
		listId = jsonPathEvaluator2.get("id");
		listUserId = jsonPathEvaluator2.get("user");

		OrgAdminRolePojo oarp = new OrgAdminRolePojo();
		oarp.setOrganization(listOrganization.get(0));
		oarp.setResource(listResourceId.get(0));
		oarp.setPermission(listPermission.get(0));
		oarp.setId(listId.get(0));
		oarp.setUser(listUserId.get(0));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();
		String patchId3 = "/api/org/user/" + listUserId.get(1) + "/permissions/" + listId.get(0);
		Response postEngagementType = engagementType.delete_URLPOJO(URL, AuthorizationKey, patchId3, oarp);
		Assert.assertEquals(postEngagementType.statusCode(), 204);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postEngagementType.asString());
		engagementType.validate_HTTPStrictTransportSecurity(postEngagementType);

	}

	@Test(groups = "IntegrationTests")
	public void OrganisationRoles_LoadAllRolesInformationForAnOrganizationWithStatus_200() {

		/**
		 * Getting the orgId
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		// getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");

		String patchId = "/api/org/" + listOrgId.get(5) + "/roles/assignments";
		Response rolesInfoRes = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		rolesInfoRes.prettyPrint();
		Assert.assertEquals(rolesInfoRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(rolesInfoRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, rolesInfoRes.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(rolesInfoRes);
	}

	//@Test(groups = "IntegrationTests")
	public void OrganisationRoles_CreateAnNewRoleAssignmentForAnOrganizationWithStatus_200() {

		/**
		 * Getting the orgId
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		// getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");

		String patchId = "/api/org/" + listOrgId.get(4) + "/roles/assignments";
		Response rolesInfoRes = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		rolesInfoRes.prettyPrint();
		Assert.assertEquals(rolesInfoRes.getStatusCode(), 200);
		JsonPath roleJson = rolesInfoRes.jsonPath();
		List<String> userId = roleJson.get("userId");
		List<String> roleId = roleJson.get("role");
		List<String> resourceId = roleJson.get("resource");
		List<String> appLevel = roleJson.get("applicationLevel");
		String uId = userId.get(0);
		String rolId = roleId.get(0);
		String resId = resourceId.get(0);
		String apLevel = appLevel.get(0);

		OrgRolesPojo pojo = new OrgRolesPojo();
		pojo.setUserId(uId);
		pojo.setRole(rolId);
		pojo.setResource(resId);
		pojo.setApplicationLevel(apLevel);

		Response postRoleRes = OrganizationsGet.post_URLPOJO(URL, AuthorizationKey, patchId, pojo);
		postRoleRes.prettyPrint();
		Assert.assertEquals(postRoleRes.getStatusCode(),200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postRoleRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postRoleRes.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(postRoleRes);
	}
	//@Test(groups = "IntegrationTests")
	public void OrganisationRoles_DeleteAnNewRoleAssignmentForAnOrganizationWithStatus_200() {

		/**
		 * Getting the orgId
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		// getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");

		String patchId = "/api/org/" + listOrgId.get(4) + "/roles/assignments";
		Response rolesInfoRes = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		rolesInfoRes.prettyPrint();
		Assert.assertEquals(rolesInfoRes.getStatusCode(), 200);
		JsonPath roleJson = rolesInfoRes.jsonPath();
		List<String> userId = roleJson.get("userId");
		List<String> roleId = roleJson.get("role");
		List<String> resourceId = roleJson.get("resource");
		List<String> appLevel = roleJson.get("applicationLevel");
		String uId = userId.get(0);
		String rolId = roleId.get(0);
		String resId = resourceId.get(0);
		String apLevel = appLevel.get(0);

		OrgRolesPojo pojo = new OrgRolesPojo();
		pojo.setUserId(uId);
		pojo.setRole(rolId);
		pojo.setResource(resId);
		pojo.setApplicationLevel(apLevel);

		Response postRoleRes = OrganizationsGet.post_URLPOJO(URL, AuthorizationKey, patchId, pojo);
		postRoleRes.prettyPrint();
		Assert.assertEquals(postRoleRes.getStatusCode(),200);
		JsonPath postRoleJson=postRoleRes.jsonPath();
		String deleteId=postRoleJson.get("id");
		String deletePatch="/api/org/" + listOrgId.get(4) + "/roles/assignments/"+deleteId;
		Response deleteRes=OrganizationsGet.delete(URL, AuthorizationKey, deletePatch);
		Assert.assertEquals(deleteRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRes.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(deleteRes);
	}

	//@Test(groups = "IntegrationTests")
	public void OrganisationRoles_CreateANewRoleForAnOrganizationWithStatus_200() {

		/**
		 * Getting the orgId
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		// getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");
		String patchId = "/api/org/" + listOrgId.get(4) + "/roles/assignments";
		Response rolesInfoRes = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		rolesInfoRes.prettyPrint();
		Assert.assertEquals(rolesInfoRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(rolesInfoRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, rolesInfoRes.asString());
		OrganizationsGet.validate_HTTPStrictTransportSecurity(rolesInfoRes);
	}

	@Test(groups = "IntegrationTests")
	public void OrganisationRoles_GetTheListOfRoleDefinationsForTheOrganization_status200() {

		String patchId = "/api/org/roles";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getMethodologyByIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByIdRes.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(getMethodologyByIdRes);

	}

	//@Test(groups = "IntegrationTests")
	public void OrganisationRoles_SUPER_USE_ENDPOINT_CreateARole_WithStatus_200() throws IOException {
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();
		OrgRolesPojo pojo = new OrgRolesPojo();
		pojo.setName(post.getProperty("postOrganisationRoleName") + getMethodologyById.getRandomNumber(1, 20));
		pojo.setMinimumApplicationLevel(post.getProperty("postMinimumApplicationLevel"));
		System.out.println(pojo);
		String patchId = "/api/org/roles";
		Response roleResponse = getMethodologyById.post_URLPOJO(URL, AuthorizationKey, patchId, pojo);
		roleResponse.prettyPrint();
		Assert.assertEquals(roleResponse.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(roleResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, roleResponse.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(roleResponse);

	}

	//@Test(groups = "IntegrationTests")
	public void OrganisationRoles_SUPER_USE_ENDPOINT_UpdateARolePermissionWithStatus_200() throws IOException {
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();
		OrgRolesPojo pojo = new OrgRolesPojo();
		pojo.setName(post.getProperty("postOrganisationRoleName") + getMethodologyById.getRandomNumber(1, 30));
		pojo.setMinimumApplicationLevel(post.getProperty("postMinimumApplicationLevel"));
		System.out.println(pojo);
		String patchId = "/api/org/roles";
		Response roleResponse = getMethodologyById.post_URLPOJO(URL, AuthorizationKey, patchId, pojo);
		roleResponse.prettyPrint();
		Assert.assertEquals(roleResponse.getStatusCode(), 200);
		/**
		 * Update the role
		 */

		JsonPath rolejson = roleResponse.jsonPath();
		String roleId = rolejson.get("id");

		OrgRolesPojo pojo1 = new OrgRolesPojo();
		pojo1.setPermissions(new String[] { post.getProperty("patchRolePermission") });

		String patchId1 = "/api/org/roles/" + roleId;

		Response updateRoleRes = getMethodologyById.patch_URLPOJO(URL, AuthorizationKey, patchId1, pojo1);
		updateRoleRes.prettyPrint();
		Assert.assertEquals(updateRoleRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(updateRoleRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, updateRoleRes.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(updateRoleRes);

	}

	//@Test(groups = "IntegrationTests")
	public void OrganisationRoles_SUPER_USE_ENDPOINT_DeleteARole_WithStatus_200() throws IOException {
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();
		OrgRolesPojo pojo = new OrgRolesPojo();
		pojo.setName(post.getProperty("postOrganisationRoleName") + getMethodologyById.getRandomNumber(1, 20));
		pojo.setMinimumApplicationLevel(post.getProperty("postMinimumApplicationLevel"));
		System.out.println(pojo);
		String patchId = "/api/org/roles";
		Response roleResponse = getMethodologyById.post_URLPOJO(URL, AuthorizationKey, patchId, pojo);
		roleResponse.prettyPrint();
		Assert.assertEquals(roleResponse.getStatusCode(), 200);

		JsonPath rolejson = roleResponse.jsonPath();
		String roleId = rolejson.get("id");
		String deleteURI = "/api/org/roles/" + roleId;
		Response deleteRoleRes = getMethodologyById.delete(URL, AuthorizationKey, deleteURI);
		Assert.assertEquals(deleteRoleRes.getStatusCode(), 204);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRoleRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, deleteRoleRes.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(deleteRoleRes);

	}

	//@Test(groups = "EndToEnd")
	public void OrganisationRoles_OrganisationRolesScenario() {
		/**
		 * FETCHING THE ORGANISATION ID
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		// getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");

		/**
		 * FETCHING THE USER ID
		 */
		Response getResourceId = OrganizationsGet.get_URL_QueryParams(URL, AuthorizationKey, "/api/org/users",
				"Organization", listOrgId.get(4));
		getResourceId.prettyPrint();

		JsonPath jsonPathEvaluator3 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator3.get("id");

		/**
		 * GET A LIST ALL PERMISSION WRT TO A USER
		 * 
		 */

		String patchURI = "/api/org/user/" + listResourceId.get(1) + "/permissions";

		Response getPermission = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, patchURI);
		getPermission.prettyPrint();

		/**
		 * PERFORMING THE POST OPERATION
		 */

		JsonPath jsonPathEvaluator2 = getPermission.jsonPath();

		listOrganization = jsonPathEvaluator2.get("organization");
		listResourceId = jsonPathEvaluator2.get("resource");
		listPermission = jsonPathEvaluator2.get("permission");
		listResourceType = jsonPathEvaluator2.get("resourceType");
		listUserId = jsonPathEvaluator2.get("user");

		OrgAdminRolePojo oarp = new OrgAdminRolePojo();
		oarp.setOrganization(listOrganization.get(0));
		oarp.setResource(listResourceId.get(5));
		oarp.setPermission(listPermission.get(1));
		oarp.setId(listResourceType.get(4));
		oarp.setUser("edogi23gfhdjg-fnjkdsf-" + getRandomAlphaNum());

		String PostURI = "/api/org/user/" + listResourceId.get(1) + "/permissions";

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, PostURI, oarp);

		Assert.assertEquals(postEngagementType.statusCode(), 200);
		JsonPath jsonEvaluvator3 = postEngagementType.jsonPath();
		String userId = jsonEvaluvator3.get("user");
		String permissionId = jsonEvaluvator3.get("id");

		OrgAdminRolePojo oarp1 = new OrgAdminRolePojo();
		oarp1.setOrganization(listOrganization.get(0));

		System.out.println("User Id----->" + userId);
		System.out.println("Permission Id------>" + permissionId);

		String patchId3 = "/api/org/user/" + userId + "/permissions/" + permissionId;
		Response postEngagementType1 = engagementType.delete_URLPOJO(URL, AuthorizationKey, patchId3, oarp1);
		postEngagementType1.prettyPrint();
		Assert.assertEquals(postEngagementType1.statusCode(), 204);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementType1.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postEngagementType1.asString());
		engagementType.validate_HTTPStrictTransportSecurity(postEngagementType1);

	}

	// @Test(groups = "IntegrationTests")
	public void organisationRoles_GetTheListOfRolesAssociationsForTheUser_status200() {

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

		JsonPath jsonPathEvaluator2 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator2.get("permissions.resource");

		String patchId = "/api/org/" + listOrgId.get(7) + "/resource/" + listResourceId.get(5) + "/roles";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getOrgRoleId = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getOrgRoleId.prettyPrint();

		/**
		 * Getting the UserId
		 * 
		 */
		JsonPath jsonPathEvaluator1 = getOrgRoleId.jsonPath();
		listUserId = jsonPathEvaluator1.get("userId");
		// listUserId = jsonPathEvaluator.get("userId");
		System.out.println("-------" + listUserId);
		// String userId = jsonPathEvaluator.get("userId");

		String patchId1 = "/api/org/" + listOrgId.get(1) + "/user/" + listUserId.get(0) + "/roles/associations";

		Response getRoleAssociate = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId1);
		getRoleAssociate.prettyPrint();
		Assert.assertEquals(getRoleAssociate.statusCode(), 200);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getRoleAssociate.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getRoleAssociate.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(getRoleAssociate);

	}

	// @Test(groups = "IntegrationTests")
	public void organisationRoles_GetTheAssignedRolesForTheGivenResource_status200() {

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
		// getResourceId.prettyPrint();

		JsonPath jsonPathEvaluator2 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator2.get("permissions.resource");

		// System.out.println(listResourceId);

		String patchId = "/api/org/" + listOrgId.get(7) + "/resource/" + listResourceId.get(15) + "/roles";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getMethodologyByIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByIdRes.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(getMethodologyByIdRes);

	}

	/// @Test(groups = "IntegrationTests")
	public void organisationRoles_PutAssignARoleForAPersonToAGivenReqource_status200() {

		/**
		 * Getting the orgId
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		// getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");

		/**
		 * Get the resourceId
		 */
		Response getResourceId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/user");
		// getResourceId.prettyPrint();

		JsonPath jsonPathEvaluator2 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator2.get("permissions.resource");
		// System.out.println(listResourceId);

		String patchId = "/api/org/" + listOrgId.get(7) + "/resource/" + listResourceId.get(15) + "/roles";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		// getMethodologyByIdRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyByIdRes.jsonPath();

		listRole = jsonPathEvaluator1.get("role");
		listUserId = jsonPathEvaluator1.get("userId");
		listUserDisplayName = jsonPathEvaluator1.get("userDisplayName");
		listUserEmail = jsonPathEvaluator1.get("userEmail");
		// listResourceType=jsonPathEvaluator1.get("resourceType");
		listResourceId = jsonPathEvaluator1.get("resource");
		// listPermission=jsonPathEvaluator1.get("permissionSets");

		System.out.println(listRole);

		OrgRolesPojo or = new OrgRolesPojo();
		or.setUserId(listUserId.get(1));
		or.setRole(listRole.get(6));
		// or.setUserDisplayName(listUserDisplayName.get(0));
		// or.setUserEmail(listUserEmail.get(0));
		// or.setResourceType(listResourceType.get(0));
		// or.setResource(listResourceId.get(0));
		// or.setPermissionSets(listPermission.get(0));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.put_URLPOJO(URL, AuthorizationKey, patchId, or);

		Assert.assertEquals(postEngagementType.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postEngagementType.asString());
		engagementType.validate_HTTPStrictTransportSecurity(postEngagementType);
	}

	// @Test(groups = "IntegrationTests")
	public void organisationRoles_DeleteThePersonsAssignedRoleFromAGivenResource_status204() {

		/**
		 * Getting the orgId
		 */
		Restassured_Automation_Utils OrganizationsGet = new Restassured_Automation_Utils();

		Response getOrgId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		// getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");

		/**
		 * Get the resourceId
		 */
		Response getResourceId = OrganizationsGet.get_URL_Without_Params(URL, AuthorizationKey, "/api/org/user");
		// getResourceId.prettyPrint();

		JsonPath jsonPathEvaluator2 = getResourceId.jsonPath();
		listResourceId = jsonPathEvaluator2.get("permissions.resource");
		// System.out.println(listResourceId);

		String patchId = "/api/org/" + listOrgId.get(7) + "/resource/" + listResourceId.get(15) + "/roles";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		// getMethodologyByIdRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyByIdRes.jsonPath();

		listRole = jsonPathEvaluator1.get("role");
		listUserId = jsonPathEvaluator1.get("userId");
		listUserDisplayName = jsonPathEvaluator1.get("userDisplayName");
		listUserEmail = jsonPathEvaluator1.get("userEmail");

		listResourceId = jsonPathEvaluator1.get("resource");

		System.out.println(listRole);

		OrgRolesPojo or = new OrgRolesPojo();
		or.setUserId(listUserId.get(1));
		or.setRole(listRole.get(7));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Response postEngagementType = engagementType.delete_URLPOJO(URL, AuthorizationKey, patchId, or);

		Assert.assertEquals(postEngagementType.statusCode(), 204);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postEngagementType.asString());
		engagementType.validate_HTTPStrictTransportSecurity(postEngagementType);

	}

	// @Test(groups = "IntegrationTests")-----> Duplicate Test case
	public void getAListOfAllPremissionForAUser_status200() {

		String patchId = "/api/org/roles";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getMethodologyByIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);

		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByIdRes.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(getMethodologyByIdRes);
		
	}

	// @Test(groups = "IntegrationTests")
	public void organisationRoles_GetTheListOfRoleTemplate_status200() {

		String patchId = "/api/org/roles/templates";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getMethodologyByIdRes = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getMethodologyByIdRes.prettyPrint();
		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getMethodologyByIdRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getMethodologyByIdRes.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(getMethodologyByIdRes);

	}

	// @Test(groups = "IntegrationTests")
	public void organisationRoles_PutUpdateThePermissionForARole_status200() {

		String patchId = "/api/org/roles/templates";

		Restassured_Automation_Utils getMethodologyById = new Restassured_Automation_Utils();

		Response getPermissionSetsId = getMethodologyById.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getPermissionSetsId.prettyPrint();

		JsonPath jsonPathEvaluator = getPermissionSetsId.jsonPath();
		List<Object> listRevisionI1 = jsonPathEvaluator.getList("permissionGroups.permissionSets.id");
		System.out.println(listRevisionI1.get(0));

		/**
		 * Performing the put operation now
		 */
		OrgRolesPojo or = new OrgRolesPojo();
		or.setPermissionSets(listRevisionI1.get(0).toString());

		String roleId = String.valueOf(listRevisionI1.get(0));

		String patchId1 = "/api/org/roles/" + roleId.substring(1, roleId.length() - 1) + "/permissions/";
		Response patchPermissionId = getMethodologyById.patch_URLPOJO(URL, AuthorizationKey, patchId1, or);
		patchPermissionId.prettyPrint();
		Assert.assertEquals(getPermissionSetsId.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchPermissionId.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchPermissionId.asString());
		getMethodologyById.validate_HTTPStrictTransportSecurity(patchPermissionId);

	}

}
