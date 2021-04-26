package restassured.automation.testcases;

import java.io.File;
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
import restassured.automation.Pojo.Engagement_Type_Pojo;
import restassured.automation.Pojo.MethodologyItem_Pojo;
import restassured.automation.Pojo.Organization_Pojo;
import restassured.automation.Pojo.Result;
import restassured.automation.Pojo.RootConditionGroup;
import restassured.automation.Pojo.RootConditionGroup.Children;
import restassured.automation.Pojo.ServiceDetailsPojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Security {

	String URL;
	String AuthorizationKey;
	ArrayList<String> listOrdId;

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 100);
	}

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
		Properties BaseUrl = read_Configuration_Propertites.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");

	}

	@Test(groups = "IntegrationTests")
	public void PostInsertAMethodology_status405_XML() {

		String id = "6017f65d192b4718edc2dbff";
		String patchId = "/api/methodology/" + id;

		File xmlDataInFile = new File("src/test/resources/SamplePaylod.xml");

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_XML(URL, AuthorizationKey, patchId,
				xmlDataInFile);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 405);

	}

	@Test(groups = "IntegrationTests")
	public void PostInsertANewOrg_status415_XML() {

		File xmlDataInFile = new File("src/test/resources/SamplePaylod.xml");

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_XML(URL, AuthorizationKey, "/api/org/",
				xmlDataInFile);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 415);

	}

	@Test(groups = "IntegrationTests")
	public void PatchUpdateAnEngagementType_status405() throws IOException {

		/**
		 * 
		 * RETRIVING THE ID
		 * 
		 */
		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum()
				+ engagementType.getRandomNumber(1, 20));
		en.setOrganization(post.getProperty("postEngagementTypeorganization"));

		Response postEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType/", en);
		postEngagementType.prettyPrint();

		String idValue = postEngagementType.then().extract().path("id").toString();
		System.out.println(idValue);

		/**
		 * 
		 * PERFORMING THE PATCH OPERATION BASED ON THE RETRIVED ID
		 * 
		 */

		String patchId = "/api/engagementType/" + idValue;

		File xmlDataInFile = new File("src/test/resources/SamplePaylod.xml");

		Response patchEngagementType = engagementType.post_XML(URL, AuthorizationKey, patchId, xmlDataInFile);
		patchEngagementType.prettyPrint();

		Assert.assertEquals(patchEngagementType.statusCode(), 405);

	}

	@Test(groups = "IntegrationTests")
	public void PostInsertAMethodology_status_MaxLength() {

		String id = "6017f65d192b4718edc2dbff";
		String patchId = "/api/methodology/" + id;

		File xmlDataInFile = new File("src/test/resources/SamplePaylod.xml");

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_XML(URL, AuthorizationKey, patchId,
				xmlDataInFile);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 405);

	}

	@Test(groups = "IntegrationTests")
	public void postCreateANewMethodologyItemWithinTheMethodologyTree_status200()
			throws JsonIOException, JsonSyntaxException, IOException {

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_Without_Params(URL, AuthorizationKey, "/api/methodology");
		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, revId.length() - 1) + "/item";

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		MethodologyItem_Pojo mi = new MethodologyItem_Pojo();

		mi.setTitle(post.getProperty("postMethodologyItemTitle") + getRandomAlphaNum());
		mi.setParentId(post.getProperty("postMethodologyItemParentId"));
		mi.setIndex(post.getProperty("postMethodologyItemIndex"));
		mi.setItemType(post.getProperty("postMethodologyItemItemType"));

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URLPOJO(URL, AuthorizationKey, patchId, mi);
		postMethodologyItem.prettyPrint();

		Assert.assertEquals(postMethodologyItem.statusCode(), 200);

	}

	@Test(groups = { "IntegrationTests" })
	public void postCreateNewOrg400() throws Exception {
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		Restassured_Automation_Utils Organization = new Restassured_Automation_Utils();

		Organization_Pojo or = new Organization_Pojo();
		or.setMemberFirmId(post.getProperty("postOrgMemberFirmId") + post.getProperty("postSpecialChar"));
		or.setName(post.getProperty("postOrgname")  + post.getProperty("postSpecialChar"));
		or.setCountryCode(post.getProperty("postOrgCountryCode"));

		Response getMethodologyByrevisonIdRes = Organization.post_URLPOJO(URL, AuthorizationKey, "/api/org", or);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);
	}
	
	
	@Test(groups = { "IntegrationTests" })
	public void postCreateANewRuleSet_status200() throws JsonIOException, JsonSyntaxException, IOException {
		
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		List<Children> children = new ArrayList<Children>();
		RootConditionGroup.Children sel = new RootConditionGroup.Children();
		//sel.setId(post.getProperty("postRulesId"));
		//sel.setName("Work Program 1 /\\nProcedure 2");
		sel.setTempId("1615527874317");
		sel.setIsNew(true);
		sel.setIsMultipleInstancesConjunction(false);
		sel.setSourceId("604afe90776641acb09dbfa5");
		sel.setType("MultipleItem");
		sel.setValue("");
		sel.setValues(new String[] { "604afe93776641acb09dbfa8" } );
		sel.setMultipleLogicOperator("Any");

		children.add(sel);

		RootConditionGroup rootConditionGroup = new RootConditionGroup();
		rootConditionGroup.setTempId("tempId");
		rootConditionGroup.setOperator("operator");
		//rootConditionGroup.setIsNew("isNew");
		rootConditionGroup.setChildren(children);
		

		Result result = new Result();
		//result.setName("result");
		result.setOperation("Show");
		result.setTargetId("604afe04776641acb09dbfa1");

		ServiceDetailsPojo sp = new ServiceDetailsPojo();
		//sp.setName("Demo");
		sp.setRootConditionGroup(rootConditionGroup);
		sp.setResult(result);
		sp.setIsComplex(false);

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

        // Fetching Org Id
		
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

		/**
		 * Retrieving list of all records
		 */

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));


		String patchId = "/api/rules/revision/" +  revId.substring(1, revId.length() - 1);


		Response postOrganizationData = getRulsByRevId.post_URLPOJO(URL, AuthorizationKey, patchId, sp);
		System.out.println(postOrganizationData.asString());
		Assert.assertEquals(postOrganizationData.statusCode(), 200);

	}

}
