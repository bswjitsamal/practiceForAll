package restassured.automation.testcases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
import restassured.automation.Pojo.Result;
import restassured.automation.Pojo.RootConditionGroup;
import restassured.automation.Pojo.ServiceDetailsPojo;
import restassured.automation.Pojo.RootConditionGroup.Conditions;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.DataSources_Read_Utils;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Restassured_Automation_Rules extends read_Configuration_Propertites {

	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrdId;

	read_Configuration_Propertites configDetails = new read_Configuration_Propertites();

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
	
	// Validating HTTP Strict Transport security
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

	@Test(priority = 1 ,groups = { "IntegrationTests" })
	public void getTheListOfRulesForARevision_status200() throws IOException {
		


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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(2)));

		String revId = String.valueOf(listRevisionI1.get(2));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1) + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		RulesDetails.prettyPrint();

		validate_HTTPStrictTransportSecurity(RulesDetails);
		Assert.assertEquals(RulesDetails.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(RulesDetails.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,RulesDetails.asString());
		rules.validate_HTTPStrictTransportSecurity(RulesDetails);

	}

	@Test(priority = 2 ,groups = { "IntegrationTests" })
	public void postCreateANewRuleSet_status200() throws JsonIOException, JsonSyntaxException, IOException {
		
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		//sel.setId(post.getProperty("postRulesId"));
		sel.setName("Work Program 1 ");
		sel.setTempId("1615527874316");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(false);
		sel.setSourceId("6052e228d35d4e7456dbe1e4");
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("tempId");
		rootConditionGroup.setOperator("And");
		rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setConditions(conditions);
		

		Result result = new Result();
		result.setName("result");
		result.setOperation("Show");
		result.setTargetId("6052e229d35d4e7456dbe1e6");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);


		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Restassured_Automation_Utils getRulsByRevId = new Restassured_Automation_Utils();

		Response getMethodologyRes = getRulsByRevId.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(2)));

		String revId = String.valueOf(listRevisionI1.get(2));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1);


		Response postOrganizationData = getRulsByRevId.post_URLPOJO(URL, AuthorizationKey, patchId, sp);
		System.out.println(postOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postOrganizationData.asString());
		getRulsByRevId.validate_HTTPStrictTransportSecurity(postOrganizationData);


	}

	@Test(priority = 3 ,groups = { "IntegrationTests" })
	public void postCreateANewRuleSet_status400() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Restassured_Automation_Utils getRulsByRevId = new Restassured_Automation_Utils();

		Response getMethodologyRes = getRulsByRevId.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(2)));

		String revId = String.valueOf(listRevisionI1.get(2));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1);
		

		String Rules_Post = "Rules_Post200";
		String Organizations = "Organizations";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		DataSources_Read_Utils dataSource = new DataSources_Read_Utils();
		Map<String, String> Organizationdata = dataSource.Json_File_Reader(Rules_Post, Organizations, "Rules.json");

		System.out.println("- BODY--" + Organizationdata);

		Response postOrganizationData = rules.post_URL(URL, AuthorizationKey, patchId, Organizationdata);
		System.out.println(postOrganizationData.asString());
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postOrganizationData.asString());
		rules.validate_HTTPStrictTransportSecurity(postOrganizationData);
		

	}
	
	@Test(priority = 4 ,groups = { "IntegrationTests" })
	public void postCreateANewRuleSet_status409() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 /\\nProcedure 2");
		//sel.setTempId("1615527874317");
		//sel.setIsNew(true);
		//sel.setIsMultipleInstancesConjunction(false);
		sel.setSourceId("6052e228d35d4e7456dbe1e4");
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { } );
		sel.setMultipleLogicOperator("Any");
		
		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("tempId");
		rootConditionGroup.setOperator("And");
		rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setConditions(conditions);
		

		Result result = new Result();
		result.setName("result");
		result.setOperation("Hide");
		result.setTargetId("6052e229d35d4e7456dbe1e6");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);


		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Restassured_Automation_Utils getRulsByRevId = new Restassured_Automation_Utils();

		Response getMethodologyRes = getRulsByRevId.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(2)));

		String revId = String.valueOf(listRevisionI1.get(2));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1)+"/list";


		//Response postOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId, sp);
		Response postOrganizationData = getRulsByRevId.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 200);
		
		/**
		 * RETRIVING THE RULE ID
		 */
		JsonPath jsonPathEvaluator2 = postOrganizationData.jsonPath();
		ArrayList<String> ruleId = jsonPathEvaluator2.get("ruleId");

		System.out.println("-------"+ruleId);
		
		String patchId1 = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1);


		Response putOrganizationData = getRulsByRevId.post_URLPOJO(URL, AuthorizationKey, patchId1, sp);
		System.out.println(putOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(putOrganizationData);
		Assert.assertEquals(putOrganizationData.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putOrganizationData.asString());
		getRulsByRevId.validate_HTTPStrictTransportSecurity(putOrganizationData);

		
	}

	@Test(priority = 5 ,groups = { "IntegrationTests" })
	public void putUpdateANewRuleSet_status200() throws JsonIOException, JsonSyntaxException, IOException {
		
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 /\\nProcedure 2");
		//sel.setTempId("1615527874317");
		//sel.setIsNew(true);
		//sel.setIsMultipleInstancesConjunction(false);
		sel.setSourceId("6052e228d35d4e7456dbe1e4");
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { } );
		sel.setMultipleLogicOperator("Any");
		
		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("tempId");
		rootConditionGroup.setOperator("And");
		rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setConditions(conditions);
		

		Result result = new Result();
		result.setName("result");
		result.setOperation("Hide");
		result.setTargetId("6052e229d35d4e7456dbe1e6");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);


		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Restassured_Automation_Utils getRulsByRevId = new Restassured_Automation_Utils();

		Response getMethodologyRes = getRulsByRevId.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(2)));

		String revId = String.valueOf(listRevisionI1.get(2));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1)+"/list";


		//Response postOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId, sp);
		Response postOrganizationData = getRulsByRevId.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 200);
		
		/**
		 * RETRIVING THE RULE ID
		 */
		JsonPath jsonPathEvaluator2 = postOrganizationData.jsonPath();
		ArrayList<String> ruleId = jsonPathEvaluator2.get("ruleId");

		System.out.println("-------"+ruleId);
		
		String patchId1 = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1)+"/rule/"+ruleId.get(0);


		Response putOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId1, sp);
		System.out.println(putOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(putOrganizationData.statusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putOrganizationData.asString());
		getRulsByRevId.validate_HTTPStrictTransportSecurity(putOrganizationData);
		
	}

	@Test(priority = 6 ,groups = { "IntegrationTests" })
	public void putUpdateANewRuleSet_status400() throws JsonIOException, JsonSyntaxException, IOException {


		
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 /\\nProcedure 2");
		//sel.setTempId("1615527874317");
		//sel.setIsNew(true);
		//sel.setIsMultipleInstancesConjunction(false);
		sel.setSourceId("6052e228d35d4e7456dbe1e4");
		sel.setType("MultipleItem");
		sel.setValue("");
		//sel.setValues(new String[] { } );
		sel.setMultipleLogicOperator("Any");
		
		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("tempId");
		rootConditionGroup.setOperator("And");
		rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setConditions(conditions);
		

		Result result = new Result();
		result.setName("result");
		result.setOperation("Hide");
		result.setTargetId("6052e229d35d4e7456dbe1e6");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);


		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Restassured_Automation_Utils getRulsByRevId = new Restassured_Automation_Utils();

		Response getMethodologyRes = getRulsByRevId.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(2)));

		String revId = String.valueOf(listRevisionI1.get(2));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1)+"/list";


		//Response postOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId, sp);
		Response postOrganizationData = getRulsByRevId.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 200);
		
		/**
		 * RETRIVING THE RULE ID
		 */
		JsonPath jsonPathEvaluator2 = postOrganizationData.jsonPath();
		ArrayList<String> ruleId = jsonPathEvaluator2.get("ruleId");

		System.out.println("-------"+ruleId);
		
		String patchId1 = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1)+"/rule/"+ruleId.get(0);


		Response putOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId1, sp);
		System.out.println(putOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(putOrganizationData.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putOrganizationData.asString());
		getRulsByRevId.validate_HTTPStrictTransportSecurity(putOrganizationData);
		
	

	}
	
	@Test(priority = 7 ,groups = { "IntegrationTests" })
	public void putUpdateANewRuleSet_status409() throws JsonIOException, JsonSyntaxException, IOException {
		
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		//sel.setId(post.getProperty("postRulesId"));
		sel.setName("Work Program 1 ");
		sel.setTempId("1615527874316");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(false);
		sel.setSourceId("6052e228d35d4e7456dbe1e4");
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");
		
		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("tempId");
		rootConditionGroup.setOperator("And");
		rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setConditions(conditions);
		

		Result result = new Result();
		result.setName("result");
		result.setOperation("Show");
		result.setTargetId("6052e229d35d4e7456dbe1e6");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);


		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		/**
		 * GETTING THE REVISION ID
		 */

		Restassured_Automation_Utils getRulsByRevId = new Restassured_Automation_Utils();

		Response getMethodologyRes = getRulsByRevId.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(2)));

		String revId = String.valueOf(listRevisionI1.get(2));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1)+"/list";


		//Response postOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId, sp);
		Response postOrganizationData = getRulsByRevId.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 200);


		/**
		 * RETRIVING THE RULE ID
		 */
		JsonPath jsonPathEvaluator2 = postOrganizationData.jsonPath();
		ArrayList<String> ruleId = jsonPathEvaluator2.get("ruleId");

		System.out.println("-------"+ruleId);
		
		
		String patchId1 = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1)+"/rule/"+ruleId.get(0);


		Response putRuleData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId1, sp);
		System.out.println(putRuleData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(putRuleData.statusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(putRuleData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,putRuleData.asString());
		getRulsByRevId.validate_HTTPStrictTransportSecurity(putRuleData);
		
	}

	@Test(priority = 8 ,groups = { "IntegrationTests" })
	public void deleteARuleSet_status204() throws IOException {
		
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

		// 1st retrive the ruleID		

		String patchId = "/api/rules/revision/" + rediD.substring(1, rediD.length() - 1) + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		JsonPath jsonPathEvaluator2 = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator2.get("ruleId");

		// Now delete the value

		String ruleId = listRuleId.get(0);
		String patchId1 = "/api/rules/revision/" + rediD.substring(1, rediD.length() - 1) + "/rule/" + ruleId;

		Response postOrganizationData = rules.delete(URL, AuthorizationKey, patchId1);
		System.out.println(postOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postOrganizationData.asString());
		rules.validate_HTTPStrictTransportSecurity(postOrganizationData);

	}

	@Test(priority = 9 ,groups = { "IntegrationTests" })
	public void deleteARuleSet_status400() throws IOException {
		
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

		// 1st retrive the ruleID		

		String patchId = "/api/rules/revision/" + rediD.substring(1, rediD.length() - 1) + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		JsonPath jsonPathEvaluator2 = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator2.get("ruleId");

		// Now delete the value

		String ruleId = listRuleId.get(0);
		String patchId1 = "/api/rules/revision/" + rediD.substring(1, rediD.length() - 1) + "/rule/" + ruleId;

		Response postOrganizationData = rules.delete(URL, AuthorizationKey, patchId1);
		System.out.println(postOrganizationData.asString());
		
		validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postOrganizationData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postOrganizationData.asString());
		rules.validate_HTTPStrictTransportSecurity(postOrganizationData);

	}
	
	//@Test(groups = { "EndToEnd" })
	public void Rule_ENDTOEND_Scenario() throws JsonIOException, JsonSyntaxException, IOException {
		
		//PERFORMING THE GET OPERATION TO RETRIVE THE RECORD 
		
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		String patchGETId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/list";
		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();
		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchGETId);
		RulesDetails.prettyPrint();
		Assert.assertEquals(RulesDetails.statusCode(), 200);
		
		//PERFORMING THE POST OPERATION TO ISERT RECORD
		
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		//sel.setId(post.getProperty("postRulesId"));
		sel.setName("Work Program 1 /\\nProcedure 2");
		sel.setTempId("1615527874317");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(false);
		sel.setSourceId("604afe90776641acb09dbfa5");
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");

		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("tempId");
		rootConditionGroup.setOperator("And");
		rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setConditions(conditions);
		

		Result result = new Result();
		result.setName("result");
		result.setOperation("Show");
		result.setTargetId("604afe04776641acb09dbfa1");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);


		// 1st retrive the ruleID

		String patchPOSTId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId");

		Response postRuleData = rules.post_URLPOJO(URL, AuthorizationKey, patchPOSTId, sp);
		System.out.println(postRuleData.asString());
		Assert.assertEquals(postRuleData.statusCode(), 200);
		
		JsonPath jsonPathEvaluator = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator.get("ruleId");
		System.out.println("The Newly Created Rule ID is : "+listRuleId);
		
		// PERFORMING THE PUT OPERATION TO UPDATE RECORD
		
		
		
		String ruleId = listRuleId.get(0);
		String patchPUTId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/rule/" + ruleId;

		Response putRuleData = rules.put_URLPOJO(URL, AuthorizationKey, patchPUTId, sp);
		System.out.println(putRuleData.asString());
		Assert.assertEquals(putRuleData.statusCode(), 200);
		
		
		// PERFORMING DELETE OPERATION TO THE SMAE UPDATE RECORD
		
		//String ruleId = listRuleId.get(0);
		String patchDELETEId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/rule/" + ruleId;

		Response deleteRuleData = rules.delete(URL, AuthorizationKey, patchDELETEId);
		System.out.println(deleteRuleData.asString());
		
		validate_HTTPStrictTransportSecurity(deleteRuleData);
		Assert.assertEquals(deleteRuleData.statusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(deleteRuleData.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,deleteRuleData.asString());
		rules.validate_HTTPStrictTransportSecurity(deleteRuleData);
	}

		
}
