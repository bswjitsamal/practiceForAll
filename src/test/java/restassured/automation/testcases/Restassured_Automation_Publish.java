package restassured.automation.testcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.Engagement_Type_Pojo;
import restassured.automation.Pojo.Methodology_Pojo;
import restassured.automation.Pojo.Validate_Pojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_Publish extends read_Configuration_Propertites {

	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrdId;
	String methodologyId;
	String engagementTypeId;

	read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
	Restassured_Automation_Utils restUtils = new Restassured_Automation_Utils();
	

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 3);
	}

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		// read_Configuration_Propertites configDetails = new
		// read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");

	}
	
	
	@Test(groups = { "IntegrationTests" })
	public void getPublish_status200() throws IOException {
		
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		
		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();
		
		//Step 1 create an engagement 
		Response createEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType",en);
		createEngagementType.prettyPrint();
		
		//Retrieving the id as engagementType
		JsonPath jsonPathEvaluator = createEngagementType.jsonPath(); 
		engagementTypeId = jsonPathEvaluator.get("id");
		
		System.out.println(engagementTypeId);
	
		
		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
		mp.setEngagementType(engagementTypeId);
		mp.setDraftDescription(post.getProperty("postMethodologyDraftDescription"));
		mp.setOrganization(post.getProperty("postEngagementTypeOrganization"));
		
		//Step 2 create a Methodology
		Response createMethodology = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/methodology",mp);
		createMethodology.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = createMethodology.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		
		System.out.println(String.valueOf(listRevisionId.get(0)));

		String revId = String.valueOf(listRevisionId.get(0));
		
		//step 3 perfrom validate 
		Validate_Pojo vp = new Validate_Pojo();
		Response createValidate = engagementType.post_WithOutBody(URL, AuthorizationKey, "/api/publish/publish/"+methodologyId+"/"+revId);
		createValidate.prettyPrint();
		
	}	
	
	@Test(groups = { "IntegrationTests" })
	public void getValidate_status200() throws IOException {
		

		
		Properties post = read_Configuration_Propertites.loadproperty("Configuration");
		
		Engagement_Type_Pojo en = new Engagement_Type_Pojo();
		en.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
		en.setOrganization(post.getProperty("postEngagementTypeOrganization"));

		Restassured_Automation_Utils engagementType = new Restassured_Automation_Utils();
		
		//Step 1 create an engagement 
		Response createEngagementType = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/engagementType",en);
		createEngagementType.prettyPrint();
		
		//Retrieving the id as engagementType
		JsonPath jsonPathEvaluator = createEngagementType.jsonPath(); 
		engagementTypeId = jsonPathEvaluator.get("id");
		
		System.out.println(engagementTypeId);
	
		
		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(post.getProperty("postEngagementTypeTitle") + getRandomAlphaNum());
		mp.setEngagementType(engagementTypeId);
		mp.setDraftDescription(post.getProperty("postMethodologyDraftDescription"));
		mp.setOrganization(post.getProperty("postEngagementTypeOrganization"));
		
		//Step 2 create a Methodology
		Response createMethodology = engagementType.post_URLPOJO(URL, AuthorizationKey, "/api/methodology",mp);
		createMethodology.prettyPrint();
		
		JsonPath jsonPathEvaluator1 = createMethodology.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");
		ArrayList<Map<String, ?>> listRevisionId = jsonPathEvaluator1.get("revisions.id");
		
		System.out.println(String.valueOf(listRevisionId.get(0)));

		String revId = String.valueOf(listRevisionId.get(0));
		
		//step 3 perfrom validate 
		Validate_Pojo vp = new Validate_Pojo();
		Response createValidate = engagementType.post_WithOutBody(URL, AuthorizationKey, "/api/publish/validate/"+methodologyId+"/"+revId);
		createValidate.prettyPrint();
		
	
		
		
	}

}
