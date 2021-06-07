package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
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

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.ETUser_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETReviewNotes {

	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> engagementId;

	List<String> reviewNotesId;
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
	public void ETMethodology_GetAllReviewNotes_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(0);

		String getreviewURI = "/api/" + post.getProperty("memberFirmSlug") + "/reviewNotes/" + id;
		Response getReiviewResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getreviewURI);
		getReiviewResponse.prettyPrint();
		Assert.assertEquals(getReiviewResponse.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getReiviewResponse.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getReiviewResponse.asString());

	}

	@Test(groups = "IntegrationTests")
	public void ETMethodology_CreateAnNewReviewNotes_Status200() throws IOException {
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
		
		String postURI = "/api/" + post.getProperty("memberFirmSlug") + "/reviewNotes/" + id;
		Map<String, String> map = new HashMap<String, String>();
		map.put("workProgram", workProgramId);
		map.put("notesText", post.getProperty("postETReviewNotesText"));
		ETUser_Pojo po = new ETUser_Pojo();
		String createReviewNotes = po.createReviewNotes(map);

		Response postReviewNotesRes = rolesUtils.post_URLPOJO(URL, AuthorizationKey, postURI, createReviewNotes);
		postReviewNotesRes.prettyPrint();
		Assert.assertEquals(postReviewNotesRes.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(postReviewNotesRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postReviewNotesRes.asString());
	}

	@Test(groups = "IntegrationTests")
	public void ETMethodology_GetReviewNotesByKey_Status200() throws IOException {
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

		String getreviewURI = "/api/" + post.getProperty("memberFirmSlug") + "/reviewNotes/" + id + "/byKey";
		Response getReviewNotesRes = rolesUtils.get_URL_ThreeQueryParams(URL, AuthorizationKey, getreviewURI,
				"WorkProgram", workProgramId, "Instance", "0", "Row", "0");
		getReviewNotesRes.prettyPrint();
		Assert.assertEquals(getReviewNotesRes.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getReviewNotesRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getReviewNotesRes.asString());

	}

	@Test(groups = "IntegrationTests")
	public void ETMethodology_GetSpecificReviewNotes_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(0);
		// Getting reviewNotes ID
		String getreviewURI = "/api/" + post.getProperty("memberFirmSlug") + "/reviewNotes/" + id;
		Response getReiviewResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getreviewURI);
		getReiviewResponse.prettyPrint();
		JsonPath reviewJson = getReiviewResponse.jsonPath();
		reviewNotesId = reviewJson.get("id");
		String reviewId = reviewNotesId.get(0);

		String getSpecficReviewUri = "/api/" + post.getProperty("memberFirmSlug") + "/reviewNotes/" + id + "/"
				+ reviewId;
		Response getSpecificReviewNotesRes = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey,
				getSpecficReviewUri);
		getSpecificReviewNotesRes.prettyPrint();
		Assert.assertEquals(getSpecificReviewNotesRes.getStatusCode(), 200);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(getSpecificReviewNotesRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, getSpecificReviewNotesRes.asString());

	}

	@Test(groups = "IntegrationTests")
	public void ETMethodology_UpdateSpecificReviewNotes_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(0);
		// Getting reviewNotes ID
		String getreviewURI = "/api/" + post.getProperty("memberFirmSlug") + "/reviewNotes/" + id;
		Response getReiviewResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getreviewURI);
		getReiviewResponse.prettyPrint();
		JsonPath reviewJson = getReiviewResponse.jsonPath();
		reviewNotesId = reviewJson.get("id");
		String reviewId = reviewNotesId.get(0);

		// performing put operation

		String putSpecficReviewUri = "/api/" + post.getProperty("memberFirmSlug") + "/reviewNotes/" + id + "/"
				+ reviewId;
		Map<String,String>map=new HashMap<String, String>();
		map.put("notesText",post.getProperty("putETReviewNotesText")+getRandomAlphaNum());
		ETUser_Pojo po=new ETUser_Pojo();
		String putReviewNotes=po.createReviewNotes(map);
		
		Response putSpecificReviewRes=rolesUtils.put_URLPOJO(URL, AuthorizationKey, putSpecficReviewUri, putReviewNotes);
		putSpecificReviewRes.prettyPrint();
		Assert.assertEquals(putSpecificReviewRes.getStatusCode(),204);
		/**
		 * Extent Report Generation
		 */
		ExtentTestManager.statusLogMessage(putSpecificReviewRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, putSpecificReviewRes.asString());
		
	}
	
	@Test(groups = "IntegrationTests")
	public void ETMethodology_DeleteSpecificReviewNotes_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();

		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(0);
		// Getting reviewNotes ID
		String getreviewURI = "/api/" + post.getProperty("memberFirmSlug") + "/reviewNotes/" + id;
		Response getReiviewResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getreviewURI);
		getReiviewResponse.prettyPrint();
		JsonPath reviewJson = getReiviewResponse.jsonPath();
		reviewNotesId = reviewJson.get("id");
		int size=reviewNotesId.size();
		String reviewId = reviewNotesId.get(size-1);

		// performing put operation

		String deleteSpecficReviewUri = "/api/" + post.getProperty("memberFirmSlug") + "/reviewNotes/" + id + "/"
				+ reviewId;
	Response deleteSpecificRes=rolesUtils.delete(URL, AuthorizationKey, deleteSpecficReviewUri);
	Assert.assertEquals(deleteSpecificRes.getStatusCode(),204);
	/**
	 * Extent Report Generation
	 */
	ExtentTestManager.statusLogMessage(deleteSpecificRes.statusCode());
	ExtentTestManager.getTest().log(Status.INFO, deleteSpecificRes.asString());
	
	}

}
