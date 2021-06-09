package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.HashMap;
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
		String id = engagementId.get(0);
		String accessUri = "/api/" + post.getProperty("memberFirmSlug") + "/responses/" + id;
	
		String row="1";
		

		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/methodologies/" + id + "/methodology";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		
		JsonPath values = myPermissionResponse.jsonPath();
		String procedure=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='SingleItemSelectQuestion'}.id ");
		String response=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='SingleItemSelectQuestion'}.options.id[0] ");
		System.out.println("response value"+response);
		System.out.println(procedure);
		Map<String,String>map=new HashedMap<String,String>();
		map.put("procedure",procedure);
		map.put("response",response);
		map.put("row", row);
		ETUser_Pojo po=new ETUser_Pojo();
		String body=po.UpdateResponses(map);
		
	/*	ET_Responses_Pojo po=new ET_Responses_Pojo();
		po.setProcedure(post.getProperty("ETProcedure"));
		po.setResponse(post.getProperty("ETResponse"));
		po.setRow(row);*/
		Response res=rolesUtils.put_URLPOJO(URL, AuthorizationKey, accessUri, body);
		res.prettyPrint();
		Assert.assertEquals(res.getStatusCode(),204);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(res.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,res.asString());
						
	}
	@Test(groups = "IntegrationTests")
	public void ETResponses_PostAddANewRowToAnItemizedWorkProgram_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(0);
		
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/methodologies/" + id + "/methodology";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		
		JsonPath values = myPermissionResponse.jsonPath();
		String workProgramId=values.get("items.collect { it.value }.reverse().data.find{it.workProgramType=='Itemized'}.id ");
		
		String accessUri = "/api/" + post.getProperty("memberFirmSlug") + "/responses/" + id+"/row";
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("workProgram",workProgramId);
		ETUser_Pojo pojo=new ETUser_Pojo();
		String po=pojo.createResponses(map);
		Response res=rolesUtils.post_URLPOJO(URL, AuthorizationKey, accessUri, po);
		res.prettyPrint();
		Assert.assertEquals(res.getStatusCode(),200);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(res.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,res.asString());
						
	}
	@Test(groups = "IntegrationTests")
	public void ETResponses_deleteAddANewRowToAnItemizedWorkProgram_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(0);
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/methodologies/" + id + "/methodology";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		
		JsonPath values = myPermissionResponse.jsonPath();
		String workProgramId=values.get("items.collect { it.value }.reverse().data.find{it.workProgramType=='Itemized'}.id ");
		
		String accessUri = "/api/" + post.getProperty("memberFirmSlug") + "/responses/" + id+"/row";
		HashMap<String,String>  map=new HashMap<String, String>();
		map.put("workProgram",workProgramId);
		ETUser_Pojo pojo=new ETUser_Pojo();
		String po=pojo.createResponses(map);
		Response res=rolesUtils.delete_URLPOJO(URL, AuthorizationKey, accessUri, po);
		res.prettyPrint();
		Assert.assertEquals(res.getStatusCode(),204);
		/** 
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(res.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,res.asString());
						
	}
	
	

}
