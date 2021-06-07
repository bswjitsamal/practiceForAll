package restassured.automation.testcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_PublishEngagementType {

	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrdId;
	String methodologyId;
	String engagementTypeId;
	
	Long MAX_TIMEOUT = 3000l;

	read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
	Restassured_Automation_Utils restUtils = new Restassured_Automation_Utils();

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 2);
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
	@Test(groups = "IntegrationTests")
	public void Publish_PostPublishInherit_status200() {

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
				"Organization", listOrdId.get(7));
		getEngagementTypeRes.prettyPrint();
		
		// Retrieving the inheritedFrom 
		
		JsonPath jp = getEngagementTypeRes.jsonPath();
		List<String> inheritedFrom = jp.getList("inheritedFrom");
		
		System.out.println(inheritedFrom);
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("storeEngagementType", inheritedFrom.get(1));
		map.put("organization", listOrdId.get(7));
		
		User_Pojo po = new User_Pojo();
		String userOobj = po.publishInherit(map);
		
		String URI="/api/publish/inherit";
		Response postEngagementTypeInheriteRes = allUtils.post_URLPOJO(URL, AuthorizationKey,URI, userOobj);
		postEngagementTypeInheriteRes.prettyPrint();
		
		/*Response postEngagementTypeInheriteRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/engagementType/inherit/",
				"Organization", inheritedFrom.get(5));
		postEngagementTypeInheriteRes.prettyPrint();
		*/
		Assert.assertEquals(postEngagementTypeInheriteRes.statusCode(), 200);
		
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getEngagementTypeRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,getEngagementTypeRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getEngagementTypeRes);	

	}

}
