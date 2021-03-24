package restassured.automation.testcases;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1) + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		RulesDetails.prettyPrint();

		Assert.assertEquals(RulesDetails.statusCode(), 200);

		JsonPath jsonPathEvaluator2 = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator2.get("ruleId");
		System.out.println(listRuleId);

	}

	@Test(groups = { "IntegrationTests" })
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1);


		Response postOrganizationData = getRulsByRevId.post_URLPOJO(URL, AuthorizationKey, patchId, sp);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 200);

	}

	@Test(groups = { "IntegrationTests" })
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1);
		

		String Rules_Post = "Rules_Post200";
		String Organizations = "Organizations";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		DataSources_Read_Utils dataSource = new DataSources_Read_Utils();
		Map<String, String> Organizationdata = dataSource.Json_File_Reader(Rules_Post, Organizations, "Rules.json");

		System.out.println("- BODY--" + Organizationdata);

		Response postOrganizationData = rules.post_URL(URL, AuthorizationKey, patchId, Organizationdata);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 400);

	}

	@Test(groups = { "IntegrationTests" })
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
		//sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
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

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1);


		Response postOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId, sp);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 200);
		
		/**
		 * RETRIVING THE RULE ID
		 */
		JsonPath jsonPathEvaluator2 = postOrganizationData.jsonPath();
		String ruleId = jsonPathEvaluator2.getString("ruleId");

		System.out.println("-------"+ruleId);
		
		String patchId1 = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1)+"/rule/"+ruleId;


		Response putOrganizationData = getRulsByRevId.put_URLPOJO(URL, AuthorizationKey, patchId1, sp);
		System.out.println(putOrganizationData.asString());
		Assert.assertEquals(putOrganizationData.statusCode(), 200);
		
	}

	@Test(groups = { "IntegrationTests" })
	public void putUpdateANewRuleSet_status400() throws JsonIOException, JsonSyntaxException, IOException {

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
	public void deleteARuleSet_status204() throws IOException {

		Properties BaseUrl = configDetails.loadproperty("Configuration");

		// 1st retrive the ruleID
		
		
		
		

		String patchId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		JsonPath jsonPathEvaluator = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator.get("ruleId");

		// Now delete the value

		String ruleId = listRuleId.get(0);
		String patchId1 = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/rule/" + ruleId;

		Response postOrganizationData = rules.delete(URL, AuthorizationKey, patchId1);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 204);

	}

	@Test(groups = { "IntegrationTests" })
	public void deleteARuleSet_status400() throws IOException {

		Properties BaseUrl = configDetails.loadproperty("Configuration");

		// 1st retrive the ruleID

		String patchId = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		String patchId1 = "/api/rules/revision/" + BaseUrl.getProperty("revisionId") + "/rule/"
				+ BaseUrl.getProperty("ruleId");

		Response postOrganizationData = rules.delete(URL, AuthorizationKey, patchId1);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 400);

	}
	
	@Test(groups = { "EndToEnd" })
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
		rootConditionGroup.setOperator("operator");
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
		Assert.assertEquals(deleteRuleData.statusCode(), 204);
	}

		
}
