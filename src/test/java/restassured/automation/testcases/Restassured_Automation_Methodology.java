package restassured.automation.testcases;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.Methodology_Pojo;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.utils.Excel_Data_Source_Utils;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Methodology extends read_Configuration_Propertites {

	String URL;
	String AuthorizationKey;
	List<String> methodologyId;
	List<String> engagementTypeId;
	List<String> organizationId;
	List<String> listOrdId;

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
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");

	}

	@Test(groups = "IntegrationTests")
	public void getListAllMethodologiesTheUserHasAccessTo_Status200() {

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

		Assert.assertEquals(getMethodologyRes.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void getDetialsAboutASpecificMethodology_status200() {

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

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology","Organization",listOrdId.get(4));
		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		System.out.println(methodologyId);

		Response getMethodologyByIdRes = getMethodology.get_URL_WithOne_PathParams(URL, AuthorizationKey,
				"/api/methodology/{value}", methodologyId.get(0));
		getMethodologyByIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void getASpecificRevisionItemTree_status200() {

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(0));

		System.out.println(methodologyId);

		String patchId = "/api/methodology/" + methodologyId.get(0) + "/revision/"
				+ revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.get_URL_Without_Params(URL, AuthorizationKey,
				patchId);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void getASpecificRevisionItemTree_status404() {
		
		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator1.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));

		System.out.println(methodologyId);

		String patchId = "/api/methodology/" + methodologyId.get(0) + "/revision/"
				+ revId.substring(1, revId.length() - 1);

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.get_URL_Without_Params(URL, AuthorizationKey,
				patchId);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 404);

	}

	@Test(groups = "IntegrationTests")
	public void PostAddANewMethodology_status200() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();
		
		
		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("engagementType");
		organizationId = jsonPathEvaluator.get("organization");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));

		System.out.println(engagementTypeId);
		System.out.println(revId);

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("postMethodologyTitle"));
		mp.setEngagementType(engagementTypeId.get(0));
		mp.setOrganization(organizationId.get(0));
		mp.setRevisions(new String[] { String.valueOf(listRevisionI1.get(1)) });
		// mp.setId("6015609b192b4718edc2da5d");

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_URLPOJO(URL, AuthorizationKey,
				"/api/methodology", mp);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);

	}

	@Test(groups = "IntegrationTests")
	public void PostAddANewMethodology_status400() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("engagementType");
		organizationId = jsonPathEvaluator.get("organization");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(0)));
		System.out.println(String.valueOf(organizationId.get(0)));

		String revId = String.valueOf(listRevisionI1.get(1));

		System.out.println(engagementTypeId);
		System.out.println(revId);

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("postMethodologyTitle") + getRandomAlphaNum());
		mp.setEngagementType(engagementTypeId.get(0) + getRandomAlphaNum());
		mp.setOrganization(organizationId.get(0));
		mp.setRevisions(new String[] { String.valueOf(listRevisionI1.get(1)) });
		// mp.setId("6015609b192b4718edc2da5d");

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.post_URLPOJO(URL, AuthorizationKey,
				"/api/methodology", mp);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);

	}

	@Test(groups = "IntegrationTests")
	public void PatchUpdateAMethodology_status400() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();		
		
		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		methodologyId = jsonPathEvaluator.get("id");
		String patchId = "/api/methodology/" + methodologyId.get(0);

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("patchMethodologyTitle"));

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				mp);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 400);

	}

	@Test(groups = "IntegrationTests")
	public void PatchUpdateAMethodology_status204() throws IOException {

		Properties post = read_Configuration_Propertites.loadproperty("Configuration");

		Restassured_Automation_Utils allUtils = new Restassured_Automation_Utils();

		// fetching Org Id

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(4));

		getMethodologyRes.prettyPrint();
		
		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();

		methodologyId = jsonPathEvaluator.get("id");
		String patchId = "/api/methodology/" + methodologyId.get(0);

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("postMethodologyTitle") + getRandomAlphaNum());

		Restassured_Automation_Utils getMethodologyByrevisonId = new Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = getMethodologyByrevisonId.patch_URLPOJO(URL, AuthorizationKey, patchId,
				mp);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 204);

	}


	 @Test(groups = "IntegrationTests", dataProvider = "dataSource" ,
	 dataProviderClass = Excel_Data_Source_Utils.class)
	public void addANewMethodology(String title, String engagementType, String responseCode) {

		int statusCode = Integer.parseInt(responseCode);

		Restassured_Automation_Utils addMethodology = new Restassured_Automation_Utils();

		Map<String, String> data = new HashMap<String, String>();
		data.put("title", title);
		data.put("engagementType", engagementType);
		
		User_Pojo addMethodologyData = new User_Pojo();
		String addMethodologyDataValue = addMethodologyData.methodologyAdd(data);
		Response addMethodologyRes = addMethodology.post_URL(URL, AuthorizationKey, "/api/methodology",
				addMethodologyDataValue);
		addMethodologyRes.prettyPrint();

		Assert.assertEquals(addMethodologyRes.statusCode(), statusCode);

	}

}
