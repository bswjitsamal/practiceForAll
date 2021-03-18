package restassured.automation.testcases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.Result;
import restassured.automation.Pojo.RootConditionGroup;
import restassured.automation.Pojo.ServiceDetailsPojo;
import restassured.automation.Pojo.RootConditionGroup.Conditions;
import restassured.automation.utils.DataSources_Read_Utils;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Rules extends read_Configuration_Propertites {

	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrdId;
	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 5);
	}

	read_Configuration_Propertites configDetails = new read_Configuration_Propertites();

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		// read_Configuration_Propertites configDetails = new
		// read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");

	}

	@Test(groups = { "IntegrationTests" })
	public void Rules_GetTheListOfRulesForARevision_status200() throws IOException {

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

		String patchId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		RulesDetails.prettyPrint();
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		rules.validate_HTTPStrictTransportSecurity(RulesDetails);
		Assert.assertEquals(RulesDetails.statusCode(), 200);

		JsonPath jsonPathEvaluator2 = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator2.get("ruleId");
		System.out.println(listRuleId);

	}

	@Test(groups = { "IntegrationTests" })
	public void Rules_PostCreateANewRuleSet_status200() throws JsonIOException, JsonSyntaxException, IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		sel.setId(post.getProperty("postRulesId"));
		sel.setSourceId(post.getProperty("postRulesSourceId"));
		sel.setName(post.getProperty("postRulesName"));
		sel.setMultipleInstanceConjunction(post.getProperty("postRulesMultipleInstanceConjunction"));
		sel.setType(post.getProperty("postRulesType"));
		sel.setValue(post.getProperty("postRulesValue"));
		sel.setSingleLogicOperator(post.getProperty("postRulesSingleLogicOperator"));

		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setId(post.getProperty("postRulesId1"));
		rootConditionGroup.setOperator(post.getProperty("postRulesOperator"));
		// rootConditionGroup.setChildren("null");
		rootConditionGroup.setConditions(conditions);

		Result result = new Result();
		result.setTargetId("604621c9952a46c14b9b4302");
		result.setName("string");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

		String patchId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1);

		Response postOrganizationData = getRulsByRevId.post_URLPOJO(URL, AuthorizationKey, patchId, sp);
		System.out.println(postOrganizationData.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getRulsByRevId.validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 200);

	}
	
	//@Test(groups = { "IntegrationTests" })
	public void Rules_PostCreateANewRuleSet_status2002() throws JsonIOException, JsonSyntaxException, IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));
		
		/**
		 * Getting the list of rules
		 */
		
		String patchGETId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/list";
		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();
		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchGETId);
		RulesDetails.prettyPrint();
		JsonPath jsonEvaluator=RulesDetails.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI2=jsonEvaluator.get("revisionId");
		String resId=String.valueOf(listRevisionI2.get(0));

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		
		sel.setResourceId(resId);
		sel.setId(post.getProperty("postRulesId"));
		sel.setSourceId(post.getProperty("postRulesSourceId"));
		sel.setName(post.getProperty("postRulesName"));
		sel.setMultipleInstanceConjunction(post.getProperty("postRulesMultipleInstanceConjunction"));
		sel.setType(post.getProperty("postRulesType"));
		sel.setValue(post.getProperty("postRulesValue"));
		sel.setSingleLogicOperator(post.getProperty("postRulesSingleLogicOperator"));

		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setId(post.getProperty("postRulesId1"));
		rootConditionGroup.setOperator(post.getProperty("postRulesOperator"));
		// rootConditionGroup.setChildren("null");
		rootConditionGroup.setConditions(conditions);

		Result result = new Result();
		result.setTargetId("604621c9952a46c14b9b4302");
		result.setName("string");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);

		
		
		String patchId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1)+"/list";
		

		Response postOrganizationData = getRulsByRevId.post_URLPOJO(URL, AuthorizationKey, patchId, sp);
		System.out.println(postOrganizationData.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getRulsByRevId.validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 200);

	}

	@Test(groups = { "IntegrationTests" })
	public void Rules_PostCreateANewRuleSet_status400() throws JsonIOException, JsonSyntaxException, FileNotFoundException {

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

		String patchId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1);

		String Rules_Post = "Rules_Post200";
		String Organizations = "Organizations";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		DataSources_Read_Utils dataSource = new DataSources_Read_Utils();
		Map<String, String> Organizationdata = dataSource.Json_File_Reader(Rules_Post, Organizations, "Rules.json");

		System.out.println("- BODY--" + Organizationdata);

		Response postOrganizationData = rules.post_URL(URL, AuthorizationKey, patchId, Organizationdata);
		System.out.println(postOrganizationData.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getRulsByRevId.validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 400);

	}

	@Test(groups = { "IntegrationTests" })
	public void Rules_PostCreateANewRuleSet_status409() throws JsonIOException, JsonSyntaxException, IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		sel.setId(post.getProperty("postRulesId"));
		sel.setSourceId(post.getProperty("postRulesSourceId"));
		sel.setName(post.getProperty("postRulesName")+getRandomAlphaNum());
		sel.setMultipleInstanceConjunction(post.getProperty("postRulesMultipleInstanceConjunction"));
		sel.setType(post.getProperty("postRulesType"));
		sel.setValue(post.getProperty("postRulesValue"));
		sel.setSingleLogicOperator(post.getProperty("postRulesSingleLogicOperator"));

		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setId(post.getProperty("postRulesId1"));
		rootConditionGroup.setOperator(post.getProperty("postRulesOperator"));
		// rootConditionGroup.setChildren("null");
		rootConditionGroup.setConditions(conditions);

		Result result = new Result();
		result.setName("String"+getRandomAlphaNum());
		result.setTargetId("604621c9952a46c14b9b4302");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo"+getRandomAlphaNum());
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

		String patchId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1);

		Response postOrganizationData = getRulsByRevId.post_URLPOJO(URL, AuthorizationKey, patchId, sp);
		System.out.println(postOrganizationData.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getRulsByRevId.validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 409);

		/**
		 * RETRIVING THE RULE ID
		 *//*
		JsonPath jsonPathEvaluator2 = postOrganizationData.jsonPath();
		String ruleId = jsonPathEvaluator2.getString("ruleId");

		System.out.println("-------" + ruleId);

		String patchId1 = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/rule/" + ruleId;

		Response putOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId1, sp);
		System.out.println(putOrganizationData.asString());
		Assert.assertEquals(putOrganizationData.statusCode(), 409);*/

	}
	
	@Test(groups = { "IntegrationTests" })
	public void Rules_PutUpdateANewRuleSet_status200() throws JsonIOException, JsonSyntaxException, IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		sel.setId(post.getProperty("postRulesId"));
		sel.setSourceId(post.getProperty("postRulesSourceId"));
		sel.setName(post.getProperty("postRulesName")+getRandomAlphaNum());
		sel.setMultipleInstanceConjunction(post.getProperty("postRulesMultipleInstanceConjunction"));
		sel.setType(post.getProperty("postRulesType"));
		sel.setValue(post.getProperty("postRulesValue"));
		sel.setSingleLogicOperator(post.getProperty("postRulesSingleLogicOperator"));

		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setId(post.getProperty("postRulesId1"));
		rootConditionGroup.setOperator(post.getProperty("postRulesOperator"));
		// rootConditionGroup.setChildren("null");
		rootConditionGroup.setConditions(conditions);

		Result result = new Result();
		result.setName("String"+getRandomAlphaNum());
		result.setTargetId("604621c9952a46c14b9b4302");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo"+getRandomAlphaNum());
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

		String patchId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1);

		Response postOrganizationData = getRulsByRevId.post_URLPOJO(URL, AuthorizationKey, patchId, sp);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 200);

		/**
		 * RETRIVING THE RULE ID
		 */
		JsonPath jsonPathEvaluator2 = postOrganizationData.jsonPath();
		String ruleId = jsonPathEvaluator2.getString("ruleId");

		System.out.println("-------" + ruleId);

		String patchId1 = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/rule/" + ruleId;

		Response putOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId1, sp);
		System.out.println(putOrganizationData.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		getRulsByRevId.validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(putOrganizationData.statusCode(), 200);

	}


	@Test(groups = { "IntegrationTests" })
	public void Rules_PutUpdateANewRuleSet_status400() throws JsonIOException, JsonSyntaxException, IOException {

		Properties BaseUrl = configDetails.loadproperty("Configuration");

		String patchId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/rule/"
				+ BaseUrl.getProperty("ruleId");

		String Rules_Post = "Rules_Update";
		String Organizations = "Organizations";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		DataSources_Read_Utils dataSource = new DataSources_Read_Utils();
		Map<String, String> Organizationdata = dataSource.Json_File_Reader(Rules_Post, Organizations, "Rules.json");

		Response postOrganizationData = rules.put_URL(URL, AuthorizationKey, patchId, Organizationdata);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 400);

	}

	

	@Test(groups = { "IntegrationTests" })
	public void Rules_DeleteARuleSet_status204() throws IOException {

		Properties BaseUrl = configDetails.loadproperty("Configuration");
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator0 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator0.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI = jsonPathEvaluator.get("revisions.id");
		

		System.out.println(String.valueOf(listRevisionI));

		String revId = String.valueOf(listRevisionI.get(8));
		System.out.println("Revision ID---->"+revId);

		// 1st retrive the ruleID

		String patchId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		JsonPath jsonPathEvaluator1 = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator1.get("ruleId");

		//String revId1 = String.valueOf(listRevisionI1.get(10));
		// Now delete the value
		
		
		String ruleId = listRuleId.get(0);
		String patchId1 = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/rule/" + ruleId;

		Response postOrganizationData = rules.delete(URL, AuthorizationKey, patchId1);
		System.out.println(postOrganizationData.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		rules.validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 204);

	}
	@Test(groups = { "IntegrationTests" })
	public void Rules_DeleteARuleSet_status400() throws IOException {

		Properties BaseUrl = configDetails.loadproperty("Configuration");
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator0 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator0.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI = jsonPathEvaluator.get("revisions.id");
		

		System.out.println(String.valueOf(listRevisionI));

		String revId = String.valueOf(listRevisionI.get(8));
		String revId1=String.valueOf(listRevisionI.get(7));
		System.out.println("Revision ID---->"+revId);

		// 1st retrive the ruleID

		String patchId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		String patchId1 = "/api/rules/revision/" + revId1.substring(1, revId1.length() - 1) + "/rule/"
				+ BaseUrl.getProperty("ruleId");

		Response postOrganizationData = rules.delete(URL, AuthorizationKey, patchId1);
		System.out.println(postOrganizationData.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		rules.validate_HTTPStrictTransportSecurity(postOrganizationData);
		Assert.assertEquals(postOrganizationData.statusCode(), 400);

	}

	@Test(groups = { "EndToEnd" })
	public void Rules_ENDTOEND_Scenario() throws JsonIOException, JsonSyntaxException, IOException {

		// PERFORMING THE GET OPERATION TO RETRIVE THE RECORD

		Properties BaseUrl = configDetails.loadproperty("Configuration");
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator0 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator0.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI = jsonPathEvaluator.get("revisions.id");
		

		System.out.println(String.valueOf(listRevisionI));

		String revId = String.valueOf(listRevisionI.get(8));
		String revId1=String.valueOf(listRevisionI.get(7));
		System.out.println("Revision ID---->"+revId);
		
		
		String patchGETId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/list";
		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();
		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchGETId);
		RulesDetails.prettyPrint();
		Assert.assertEquals(RulesDetails.statusCode(), 200);

		// PERFORMING THE POST OPERATION TO ISERT RECORD

		List<Conditions> conditions = new ArrayList<Conditions>();
		RootConditionGroup.Conditions sel = new RootConditionGroup.Conditions();
		sel.setId(BaseUrl.getProperty("postId"));
		sel.setSourceId(BaseUrl.getProperty("postSourceId"));
		sel.setName("Velocity");
		sel.setMultipleInstanceConjunction(BaseUrl.getProperty("postMultipleInstanceConjunction"));
		sel.setType(BaseUrl.getProperty("postType"));
		sel.setValue(BaseUrl.getProperty("postValue"));
		sel.setSingleLogicOperator(BaseUrl.getProperty("postSingleLogicOperator"));

		conditions.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setId(BaseUrl.getProperty("postId2"));
		rootConditionGroup.setOperator(BaseUrl.getProperty("postOperator"));
		// rootConditionGroup.setChildren("null");
		rootConditionGroup.setConditions(conditions);

		Result result = new Result();
		result.setName("String");
		result.setTargetId("604621c9952a46c14b9b4302");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);

		// 1st retrive the ruleID

		String patchPOSTId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1);

		Response postRuleData = rules.post_URLPOJO(URL, AuthorizationKey, patchPOSTId, sp);
		System.out.println(postRuleData.asString());
		Assert.assertEquals(postRuleData.statusCode(), 200);

		JsonPath jsonPathEvaluator3 = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator3.get("ruleId");
		System.out.println("The Newly Created Rule ID is : " + listRuleId);

		// PERFORMING THE PUT OPERATION TO UPDATE RECORD

		String ruleId = listRuleId.get(0);
		String patchPUTId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1)+ "/rule/" + ruleId;

		Response putRuleData = rules.put_URLPOJO(URL, AuthorizationKey, patchPUTId, sp);
		System.out.println(putRuleData.asString());
		Assert.assertEquals(putRuleData.statusCode(), 200);

		// PERFORMING DELETE OPERATION TO THE SMAE UPDATE RECORD

		// String ruleId = listRuleId.get(0);
		String patchDELETEId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/rule/" + ruleId;

		Response deleteRuleData = rules.delete(URL, AuthorizationKey, patchDELETEId);
		System.out.println(deleteRuleData.asString());
		System.out.println("This particular below line is based on Sprint 7 & the Requirement ID : 1008");
		rules.validate_HTTPStrictTransportSecurity(deleteRuleData);
		Assert.assertEquals(deleteRuleData.statusCode(), 204);
	}

}
