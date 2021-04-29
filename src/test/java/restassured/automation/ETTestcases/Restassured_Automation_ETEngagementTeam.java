package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETEngagementTeam {
	
	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> engagementId;

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
	public void ETEngagement_GetEngagementHistory_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(1);
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/team/history";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		
	}
	@Test(groups = "IntegrationTests")
	public void ETEngagement_GetEngagementRoles_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(1);
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/team/roles";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
	}
	

}
