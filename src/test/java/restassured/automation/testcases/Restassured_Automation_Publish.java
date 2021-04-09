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
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");	
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");

		Object[] revId = listRevisionId.toArray();
		
		String rediD = revId[revId.length-1].toString();
		System.out.println(rediD);

		// step 3 perfrom validate
		PublishPojo PublishPojo = new PublishPojo();
		PublishPojo.setPublishType("Draft");
		
		Response createPublish = getMethodology.post_URLPOJO(URL, AuthorizationKey,
				"/api/publish/publish/" + methodologyId.get(methodologyId.size()-1) + "/" + rediD.substring(1, rediD.length() - 1), PublishPojo);
		createPublish.prettyPrint();
		
		
		Assert.assertEquals(createPublish.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(createPublish);
		
		//return createPublish.statusCode();

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
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		List<String> methodologyId = jsonPathEvaluator1.get("id");
		
		//fetching the last item from the list
		System.out.println(methodologyId.get(methodologyId.size()-1));		
		
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");

		Object[] revId = listRevisionId.toArray();
		
		String rediD = revId[revId.length-1].toString();
		System.out.println(rediD);
		
		Response createPublish = getMethodology.post_WithOutBody(URL, AuthorizationKey,
				"/api/publish/validate/" + methodologyId.get(methodologyId.size()-1) + "/" + rediD.substring(1, rediD.length() - 1));
		createPublish.prettyPrint();
		
		
		Assert.assertEquals(createPublish.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createPublish.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createPublish.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(createPublish);

	}
	
	
	@Test(groups = { "IntegrationTests" })
	
	public void getPublish_status200() throws IOException {

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
		

		//return createPublish.statusCode();
		Response createValidate = getMethodology.get_URL_Without_Params(URL, AuthorizationKey,
				"/api/publish/publish/" + methodologyId.get(methodologyId.size()-1));
		createValidate.prettyPrint();
		
		Assert.assertEquals(createValidate.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createValidate.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createValidate.asString());
		getMethodology.validate_HTTPStrictTransportSecurity(createValidate);
		
	}

	
	



}
