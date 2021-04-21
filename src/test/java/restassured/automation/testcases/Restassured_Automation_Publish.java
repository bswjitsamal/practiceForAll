package restassured.automation.testcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.ArrayList;
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
import restassured.automation.Pojo.PublishPojo;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Publish extends read_Configuration_Propertites{
	


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

	
	public void validate_HTTPStrictTransportSecurity(Response response) {

		// Reader header of a give name. In this line we will get Header named Server
		String strictTransportSecurity = response.header("Strict-Transport-Security");
		System.out.println("Server value: " + strictTransportSecurity);
		
		if("max-age=63072000; includeSubDomains; preload".equals(strictTransportSecurity)) {
			System.out.println("This is following HTTPStrictTransportSecurity");
				
		}else {
			System.out.println("This is NOT following HTTPStrictTransportSecurity");
			
		}
		//Assert.assertEquals("max-age=63072000; includeSubDomains; preload", strictTransportSecurity);
	}
	
	
	@Test(groups = { "IntegrationTests" })
	public void postValidate_status200() throws IOException {
		
		
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
				"Organization", listOrdId.get(5));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//Fetching the updatedId
		//List<String> listRevisionI1 = jsonPathEvaluator1.get("find{it.title== 'createMethodologyAPI'}.revisions[0].updatedIds");
		List<String> listRevisionI1 = jsonPathEvaluator1.get("find{it.title== 'createMethodologyAPI'}.revisions");
		System.out.println(listRevisionI1);
		
		
		//Step 3: CREATE A PUBLISH CANDIDATE
		
		Map<String, String[]> data = new HashMap<String, String[]>();
		data.put("itemIds", new String[]  {listRevisionI1.get(0)});
		
		
		Response postpublishCandaidate = allUtils.post_URL_WithOne_PathParams(URL, AuthorizationKey, "/api/methodology/candidate/{value}",
				 methodologyId.get(methodologyId.size()-1), data);
		postpublishCandaidate.prettyPrint();
		
		
		//Now retrieve the 
		JsonPath jsonPathEvaluator2 = postpublishCandaidate.jsonPath();
		String candidateId = jsonPathEvaluator2.get("id");
		System.out.println(candidateId);
		
		Map<String, String> data1 = new HashMap<String, String>();
		//data1.put("","");
		
		Response postpublish = allUtils.post_URL_WithTwo_PathParams(URL, AuthorizationKey, "/api/publish/validate/{methodologyId}/{revisionId}",
				 methodologyId.get(methodologyId.size()-1),candidateId, data1);
		postpublish.prettyPrint();
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postpublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postpublish.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postpublish);
		
		
	}

	@Test(groups = { "IntegrationTests" })
	public void postPublish_status200() throws IOException {
		
		
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
				"Organization", listOrdId.get(5));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		//Fetching the updatedId
		/*List<String> listRevisionI1 = jsonPathEvaluator1.get("find{it.title== 'createMethodologyAPI'}.revisions[1].updatedIds");
		System.out.println(listRevisionI1.get(0));*/
		
		List<String> listRevisionI1 = jsonPathEvaluator1.get("find{it.title== 'createMethodologyAPI'}.revisions");
		System.out.println(listRevisionI1);
		
		
		//Step 3: CREATE A PUBLISH CANDIDATE
		
		Map<String, String[]> data = new HashMap<String, String[]>();
		data.put("itemIds", new String[]  {listRevisionI1.get(0)});
		
		
		Response postpublishCandaidate = allUtils.post_URL_WithOne_PathParams(URL, AuthorizationKey, "/api/methodology/candidate/{value}",
				 methodologyId.get(methodologyId.size()-1), data);
		postpublishCandaidate.prettyPrint();
		
		
		//Now retrieve the 
		JsonPath jsonPathEvaluator2 = postpublishCandaidate.jsonPath();
		String candidateId = jsonPathEvaluator2.get("id");
		System.out.println(candidateId);
		
		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("publishType","Draft");
		
		Response postpublish = allUtils.post_URL_WithTwo_PathParams(URL, AuthorizationKey, "/api/publish/publish/{methodologyId}/{revisionId}",
				 methodologyId.get(methodologyId.size()-1),candidateId, data1);
		postpublish.prettyPrint();
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postpublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postpublish.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postpublish);
		
		
	}
	

}
