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
import org.json.JSONObject;
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

public class Restassured_Automation_FinancialStatements {

	String URL;
	String AuthorizationKey;
	List<String> listOrdId;
	List<String> LineItemsTitle;
	List<String> LineItemsId;
	Restassured_Automation_Utils allUtils;
	Properties post;
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
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey1");
		Awaitility.reset();
		Awaitility.setDefaultPollDelay(999, MILLISECONDS);
		Awaitility.setDefaultPollInterval(99, SECONDS);
		Awaitility.setDefaultTimeout(99, SECONDS);

	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_LoadTheFinancialStatementsForTheStore_Status200() {

		allUtils = new Restassured_Automation_Utils();

		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		String URI = "/api/financialStatements/org/" + listOrdId.get(0);

		Response getFinancialStatementRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, URI);
		getFinancialStatementRes.prettyPrint();
		Assert.assertEquals(getFinancialStatementRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getFinancialStatementRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getFinancialStatementRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getFinancialStatementRes);

	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_createAnNewFinancialStatementsForTheStore_Status200() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");

		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		String URI = "/api/financialStatements/org/" + listOrdId.get(0);
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postFinancialStatements") + allUtils.getRandomNumber(1, 30));
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.createAdminFinanceStatements(map);

		Response getFinancialStatementRes = allUtils.post_URLPOJO(URL, AuthorizationKey, URI, createFinStatement);
		getFinancialStatementRes.prettyPrint();
		Assert.assertEquals(getFinancialStatementRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getFinancialStatementRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getFinancialStatementRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getFinancialStatementRes);


	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_createAnNewFinancialStatementsForTheStore_Status400() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");

		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		String URI = "/api/financialStatements/org/" + listOrdId.get(0);
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", " ");
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.createAdminFinanceStatements(map);

		Response getFinancialStatementRes = allUtils.post_URLPOJO(URL, AuthorizationKey, URI, createFinStatement);
		getFinancialStatementRes.prettyPrint();
		Assert.assertEquals(getFinancialStatementRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getFinancialStatementRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getFinancialStatementRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getFinancialStatementRes);


	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_createAnNewFinancialStatementsForTheStore_Status409() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");

		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		String URI = "/api/financialStatements/org/" + listOrdId.get(0);

		Response getFinancialStatementRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, URI);
		getFinancialStatementRes.prettyPrint();
		JsonPath getjson = getFinancialStatementRes.jsonPath();
		LineItemsTitle = getjson.get("lineItems.title");

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", LineItemsTitle.get(0));
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.createAdminFinanceStatements(map);

		Response postFinancialStatmentsRes = allUtils.post_URLPOJO(URL, AuthorizationKey, URI, createFinStatement);
		postFinancialStatmentsRes.prettyPrint();
		Assert.assertEquals(postFinancialStatmentsRes.getStatusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postFinancialStatmentsRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postFinancialStatmentsRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postFinancialStatmentsRes);


	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_GetFinancialStatementsForTheRevision_Status200() {

		allUtils = new Restassured_Automation_Utils();

		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(0));
		getMethodologyRes.prettyPrint();
		JsonPath getJson = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = getJson.get("revisions");
		String revId = String.valueOf(listRevisionI1.get(2));

		String getURI = "/api/financialStatements/revision/" + revId.substring(1, 25);
		Response getResponse = allUtils.get_URL_Without_Params(URL, AuthorizationKey, getURI);
		getResponse.prettyPrint();
		Assert.assertEquals(getResponse.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(getResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getResponse.asString());
		allUtils.validate_HTTPStrictTransportSecurity(getResponse);


	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_PatchTheFinancialStatementsLineItems_Status200() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		String orgId = listOrdId.get(0);

		String URI = "/api/financialStatements/org/" + orgId;

		Response getFinancialStatementRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, URI);
		getFinancialStatementRes.prettyPrint();
		JsonPath getJson = getFinancialStatementRes.jsonPath();
		LineItemsId = getJson.get("lineItems.id");
		int size = LineItemsId.size();
		String id = LineItemsId.get(size - 1);
		System.out.println(id);
		String patchUri = "/api/financialStatements/org/" + orgId + "/lineItem/" + id;

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("patchFinancialStatements") + allUtils.getRandomNumber(1, 20));
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.createAdminFinanceStatements(map);

		Response patchLineItemsres = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchUri, createFinStatement);
		patchLineItemsres.prettyPrint();

		Assert.assertEquals(patchLineItemsres.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchLineItemsres.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchLineItemsres.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchLineItemsres);



	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_PatchTheFinancialStatementsLineItems_Status400() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		String orgId = listOrdId.get(0);

		String URI = "/api/financialStatements/org/" + orgId;

		Response getFinancialStatementRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, URI);
		getFinancialStatementRes.prettyPrint();
		JsonPath getJson = getFinancialStatementRes.jsonPath();
		LineItemsId = getJson.get("lineItems.id");
		int size = LineItemsId.size();
		String id = LineItemsId.get(size - 1);
		System.out.println(id);
		String patchUri = "/api/financialStatements/org/" + orgId + "/lineItem/" + id;

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", " ");
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.createAdminFinanceStatements(map);

		Response patchLineItemsres = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchUri, createFinStatement);
		patchLineItemsres.prettyPrint();

		Assert.assertEquals(patchLineItemsres.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchLineItemsres.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchLineItemsres.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchLineItemsres);



	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_PatchTheFinancialStatementsLineItems_Status409() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		String orgId = listOrdId.get(0);

		String URI = "/api/financialStatements/org/" + orgId;

		Response getFinancialStatementRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, URI);
		getFinancialStatementRes.prettyPrint();
		JsonPath getJson = getFinancialStatementRes.jsonPath();
		LineItemsId = getJson.get("lineItems.id");
		LineItemsTitle = getJson.get("lineItems.title");
		int size = LineItemsId.size();
		String id = LineItemsId.get(size - 1);
		String title = LineItemsTitle.get(size - 1);
		System.out.println(id);
		String patchUri = "/api/financialStatements/org/" + orgId + "/lineItem/" + id;

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", title);
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.createAdminFinanceStatements(map);

		Response patchLineItemsres = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchUri, createFinStatement);
		patchLineItemsres.prettyPrint();

		Assert.assertEquals(patchLineItemsres.getStatusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchLineItemsres.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchLineItemsres.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchLineItemsres);



	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_PatchTheFinancialStatementsLineItems_Status404() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		String orgId = listOrdId.get(0);

		String URI = "/api/financialStatements/org/" + orgId;

		Response getFinancialStatementRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, URI);
		getFinancialStatementRes.prettyPrint();
		JsonPath getJson = getFinancialStatementRes.jsonPath();
		LineItemsId = getJson.get("lineItems.id");
		int size = LineItemsId.size();
		String id = LineItemsId.get(size - 1);
		System.out.println(id);
		String patchUri = "/api/financialStatements/org/" + orgId + "/lineItemss/" + id;

		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("patchFinancialStatements") + allUtils.getRandomNumber(1, 20));
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.createAdminFinanceStatements(map);

		Response patchLineItemsres = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchUri, createFinStatement);
		patchLineItemsres.prettyPrint();

		Assert.assertEquals(patchLineItemsres.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchLineItemsres.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchLineItemsres.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchLineItemsres);



	}

	@Test(groups = "IntegrationTests")
	public void FinancialStatements_MoveFinancialStatementsLineItems_Status404() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		String orgId = listOrdId.get(0);

		String URI = "/api/financialStatements/org/" + orgId;

		Response getFinancialStatementRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, URI);
		getFinancialStatementRes.prettyPrint();
		JsonPath getJson = getFinancialStatementRes.jsonPath();
		LineItemsId = getJson.get("lineItems.id");
		int size = LineItemsId.size();
		Integer size1 = (int) size;
		String index = String.valueOf(size - 3);
		String id = LineItemsId.get(size - 1);
		System.out.println(id);
		String patchUri = "/api/financialStatements/org/" + orgId + "/lineItemss/" + id + "/move";

		Map<String, String> map = new HashMap<String, String>();
		map.put("index", post.getProperty("FincacialStatementIndexValue"));
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.createAdminFinanceStatements(map);

		Response patchLineItemsres = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchUri, createFinStatement);
		patchLineItemsres.prettyPrint();

		Assert.assertEquals(patchLineItemsres.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchLineItemsres.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchLineItemsres.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchLineItemsres);



	}

	//@Test(groups = "IntegrationTests")
	public void FinancialStatements_MoveFinancialStatementsLineItems_Status204() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		String orgId = listOrdId.get(0);

		String URI = "/api/financialStatements/org/" + orgId;

		Response getFinancialStatementRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, URI);
		getFinancialStatementRes.prettyPrint();
		JsonPath getJson = getFinancialStatementRes.jsonPath();
		LineItemsId = getJson.get("lineItems.id");
		int size = LineItemsId.size();
		Integer size1 = (int) size;
		String index = String.valueOf(size - 3);
		String id = LineItemsId.get(size - 1);
		System.out.println(id);
		String patchUri = "/api/financialStatements/org/" + orgId + "/lineItem/" + id + "/move";

		/*JSONObject obj = new JSONObject();
		obj.put("index", 59);*/
		int i=56;
		 Integer intObj = new Integer(i);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("index",intObj);
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.UpdateAdminFinanceStatements(map);

		Response patchLineItemsres = allUtils.patch_URLPOJO(URL, AuthorizationKey, patchUri, createFinStatement);
		patchLineItemsres.prettyPrint();

		Assert.assertEquals(patchLineItemsres.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchLineItemsres.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchLineItemsres.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchLineItemsres);



	}
	
	@Test(groups = "IntegrationTests")
	public void FinancialStatements_PatchFinancialStatementsForTheRevision_Status400() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");

		// Fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();
		String orgId=listOrdId.get(0);
		/**
		 * GETTING THE REVISION ID
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", orgId);
		getMethodologyRes.prettyPrint();
		JsonPath getJson = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = getJson.get("revisions");
		String revId = String.valueOf(listRevisionI1.get(2));
		
		
		String URI = "/api/financialStatements/org/" + orgId;
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", post.getProperty("postFinancialStatements") + allUtils.getRandomNumber(1, 30));
		User_Pojo po = new User_Pojo();
		String createFinStatement = po.createAdminFinanceStatements(map);

		Response getFinancialStatementRes = allUtils.post_URLPOJO(URL, AuthorizationKey, URI, createFinStatement);
		getFinancialStatementRes.prettyPrint();
		JsonPath postJson=getFinancialStatementRes.jsonPath();
		
		LineItemsTitle=postJson.get("lineItems.title");
		int titleSize=LineItemsTitle.size();
		String title=LineItemsTitle.get(titleSize-1);
		
		LineItemsId=postJson.get("lineItems.id");
		int idSize=LineItemsId.size();
		String id=LineItemsId.get(idSize-1);
		
		System.out.println(LineItemsTitle.get(titleSize-1));
		
		

		String patchURI = "/api/financialStatements/org/"+orgId +"/revision/"+ revId.substring(1, 25); 
		Map<String,String> map1=new HashMap<String, String>();
		map1.put("id",id);
		map1.put("title",title);
		
		String patchRevision=po.UpdateFinancialStatmentForRevision(map1);
		Response patchRevisionRes=allUtils.patch_URLPOJO(URL, AuthorizationKey, patchURI, patchRevision);
		Assert.assertEquals(patchRevisionRes.getStatusCode(),400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchRevisionRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchRevisionRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchRevisionRes);


		
	}

}
