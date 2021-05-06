package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.collections4.map.HashedMap;
import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.ETUser_Pojo;
import restassured.automation.Pojo.ET_Responses_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETResponses {
	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> engagementId;
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
		URL = BaseUrl.getProperty("ETBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey2");
		Awaitility.reset();
		Awaitility.setDefaultPollDelay(999, MILLISECONDS);
		Awaitility.setDefaultPollInterval(99, SECONDS);
		Awaitility.setDefaultTimeout(99, SECONDS);

	}
	
	@Test(groups = "IntegrationTests")
	public void ETResponses_PutResponses_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(1);
		String accessUri = "/api/" + post.getProperty("memberFirmSlug") + "/responses/" + id;
		String procedure="609269f737139c81e05304ee";
		String response="609269f937139c81e05304f1";
		/*Map<String,String>map=new HashedMap<String,String>();
		map.put("procedure",procedure);
		map.put("response",responses);
		ETUser_Pojo po=new ETUser_Pojo();
		String body=po.UpdateResponses(map);*/
		ET_Responses_Pojo po=new ET_Responses_Pojo();
		po.setProcedure(procedure);
		po.setResponse(response);
		Response res=rolesUtils.put_URLPOJO(URL, AuthorizationKey, accessUri, po);
		res.prettyPrint();
		Assert.assertEquals(res.getStatusCode(),204);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(res.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,res.asString());
		
		
		
		
		
	}

}
