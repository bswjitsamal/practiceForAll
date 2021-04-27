package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.awaitility.Awaitility;
import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETPortalRoles{
	
	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> portalRoles;
	List<String> userId;
	
	
	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ETBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey2");
		Awaitility.reset();
        Awaitility.setDefaultPollDelay(999, MILLISECONDS);
        Awaitility.setDefaultPollInterval(99, SECONDS);
        Awaitility.setDefaultTimeout(99, SECONDS);

	}
	
	@Test(groups = "IntegrationTests")
	public void PortalRoles_GetRolesPortalMyPermission_Status200() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		
		post=read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI="/api/"+post.getProperty("memberFirmSlug") +"/roles/portal/myPermissions";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		
	}
	@Test(groups = "IntegrationTests")
	public void PortalRoles_GetRolesPortalAssignments_Status200() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		
		post=read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI="/api/"+post.getProperty("memberFirmSlug") +"/roles/portal/assignments";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		
	}
	
	@Test(groups = "IntegrationTests")
	public void PortalRoles_PutRolesPortalAssignments_Status204() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post=read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * Fetching the roleID
		 */
		String patchURI="/api/"+post.getProperty("memberFirmSlug") +"/roles/portal/assignments";
		Response getRolesResponse=rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, patchURI);
		getRolesResponse.prettyPrint();
		JsonPath rolesJson=getRolesResponse.jsonPath();
		portalRoles=rolesJson.get("portalRoleId");
		userId=rolesJson.get("userId");
		String rolesId=portalRoles.get(0);
		String user=userId.get(0);
		HashMap<String,String>map=new HashMap<String,String>();
		map.put("portalRoles",rolesId);
		map.put("userId",user);
		User_Pojo pojo=new User_Pojo();
		String putRoles=pojo.putPortalRoles(map);
		Response putRolesResponse = rolesUtils.put_URLPOJO(URL, AuthorizationKey, patchURI,putRoles);
		putRolesResponse.prettyPrint();
		Assert.assertEquals(putRolesResponse.getStatusCode(), 204);
		
	}
	@Test(groups = "IntegrationTests")
	public void PortalRoles_GetRolesPortal_Status200() throws IOException {

		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		
		post=read_Configuration_Propertites.loadproperty("Configuration");
		String myPermissionURI="/api/"+post.getProperty("memberFirmSlug") +"/roles/portal";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		
	}
	
	
	

}
