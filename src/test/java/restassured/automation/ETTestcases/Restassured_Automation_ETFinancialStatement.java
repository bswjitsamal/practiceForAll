package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.util.SystemOutLogger;
import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.ETUser_Pojo;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETFinancialStatement {
	
	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> engagementId;
	List<String> fsliId;
	List<String> methodologyId;
	

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
	public void ETEngagement_CreateAnNewFsliItem_Status204() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "0",
				"100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson = allEnagementRes.jsonPath();
		engagementId = allEngagmentJson.get("id");
		String id = engagementId.get(0);
		
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/methodologies/"+id+"/methodology";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		
		JsonPath values = myPermissionResponse.jsonPath();
		String workProgramId=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='FinancialStatement'}.workProgramId ");
		System.out.println("Workprogram--------------> "+workProgramId);
		String taxonomy=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='FinancialStatement'}.options.id[0] ");
		System.out.println("Taxonomy -------->   "+taxonomy);
		String postURI="/api/"+post.getProperty("memberFirmSlug")+"/financialstatements/"+id+"/"+workProgramId+"/"+"0"+"/fsItem";
		Map<String,String> map=new HashMap<String,String>();
		map.put("index", post.getProperty("ETFinancialStatmentIndex"));
		map.put("itemType", post.getProperty("ETFinancialStatementItemType"));
		map.put("taxonomy", taxonomy);
		map.put("displayText", post.getProperty("ETFinancialStatementDisplayText")+rolesUtils.getRandomNumber(1,30));
		
		ETUser_Pojo po=new ETUser_Pojo();
		String createLineItem=po.createFinancialStatement(map);
		
		Response postResponse=rolesUtils.post_URLPOJO(URL, AuthorizationKey, postURI, createLineItem);
		postResponse.prettyPrint();
		Assert.assertEquals(postResponse.getStatusCode(),204);
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		
	}
	@Test(groups = "IntegrationTests")
	public void ETEngagement_DeleteFsli_Status204() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(0);
		
		
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/methodologies/"+id+"/methodology";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		
		JsonPath values = myPermissionResponse.jsonPath();
		String workProgramId=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='FinancialStatement'}.workProgramId ");
		System.out.println("Workprogram--------------> "+workProgramId);
		String taxonomy=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='FinancialStatement'}.options.id[0] ");
		System.out.println("Taxonomy -------->   "+taxonomy);
		String postURI="/api/"+post.getProperty("memberFirmSlug")+"/financialstatements/"+id+"/"+workProgramId+"/"+"0"+"/fsItem";
		Map<String,String> map=new HashMap<String,String>();
		map.put("index", post.getProperty("ETFinancialStatmentIndex"));
		map.put("itemType", post.getProperty("ETFinancialStatementItemType"));
		map.put("taxonomy", taxonomy);
		map.put("displayText", post.getProperty("ETFinancialStatementDisplayText")+rolesUtils.getRandomNumber(1,30));
		
		ETUser_Pojo po=new ETUser_Pojo();
		String createLineItem=po.createFinancialStatement(map);
		
		Response postResponse=rolesUtils.post_URLPOJO(URL, AuthorizationKey, postURI, createLineItem);
		postResponse.prettyPrint();
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/events";
		Response myPermissionResponse1 = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		//myPermissionResponse.prettyPrint();
		JsonPath getJson=myPermissionResponse1.jsonPath();
		 fsliId =getJson.get("events.fsliId");
		 int size=fsliId.size();
		System.out.println("Size---->" +size);
		String fsliid=fsliId.get(size-1);
		System.out.println("Fsli id------> "+fsliid);
		
		String deleteUri="/api/"+post.getProperty("memberFirmSlug")+"/financialstatements/"+id+"/"+workProgramId+"/"+"0"+"/fsItem/"+fsliid;
				
		Response deleteRes=rolesUtils.delete(URL, AuthorizationKey, deleteUri);
		Assert.assertEquals(deleteRes.getStatusCode(),204);
		
	}
	
	@Test(groups = "IntegrationTests")
	public void ETEngagement_PutFsli_Status204() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(0);
		
		
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/methodologies/"+id+"/methodology";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		
		JsonPath values = myPermissionResponse.jsonPath();
		String workProgramId=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='FinancialStatement'}.workProgramId ");
		System.out.println("Workprogram--------------> "+workProgramId);
		String taxonomy=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='FinancialStatement'}.options.id[0] ");
		System.out.println("Taxonomy -------->   "+taxonomy);
		String postURI="/api/"+post.getProperty("memberFirmSlug")+"/financialstatements/"+id+"/"+workProgramId+"/"+"0"+"/fsItem";
		Map<String,String> map=new HashMap<String,String>();
		map.put("index", post.getProperty("ETFinancialStatmentIndex"));
		map.put("itemType", post.getProperty("ETFinancialStatementItemType"));
		map.put("taxonomy", taxonomy);
		map.put("displayText", post.getProperty("ETFinancialStatementDisplayText")+rolesUtils.getRandomNumber(1,30));
		
		ETUser_Pojo po=new ETUser_Pojo();
		String createLineItem=po.createFinancialStatement(map);
		
		Response postResponse=rolesUtils.post_URLPOJO(URL, AuthorizationKey, postURI, createLineItem);
		postResponse.prettyPrint();
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/events";
		Response myPermissionResponse1 = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		//myPermissionResponse.prettyPrint();
		JsonPath getJson=myPermissionResponse1.jsonPath();
		 fsliId =getJson.get("events.fsliId");
		 int size=fsliId.size();
		System.out.println("Size---->" +size);
		String fsliid=fsliId.get(size-1);
		System.out.println("Fsli id------> "+fsliid);
		String PutUri="/api/"+post.getProperty("memberFirmSlug")+"/financialstatements/"+id+"/"+workProgramId+"/"+"0"+"/fsItem/"+fsliid;
		
		Map<String,String> map1=new HashMap<String, String>();
		map.put("newIndex","1");
		String putApi=po.createFinancialStatement(map1);
		Response putResponse=rolesUtils.put_URLPOJO(URL, AuthorizationKey, PutUri, putApi);
		Assert.assertEquals(putResponse.getStatusCode(),204);
		
	}
	@Test(groups = "IntegrationTests")
	public void ETEngagement_PatchFsli_Status204() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/mine";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(0);
		
		
		String myPermissionURI = "/api/" + post.getProperty("memberFirmSlug") + "/methodologies/"+id+"/methodology";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, myPermissionURI);
		myPermissionResponse.prettyPrint();
		
		JsonPath values = myPermissionResponse.jsonPath();
		String workProgramId=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='FinancialStatement'}.workProgramId ");
		System.out.println("Workprogram--------------> "+workProgramId);
		String taxonomy=values.get("items.collect { it.value }.reverse().data.find{it.workProgramItemType=='FinancialStatement'}.options.id[0] ");
		System.out.println("Taxonomy -------->   "+taxonomy);
		String postURI="/api/"+post.getProperty("memberFirmSlug")+"/financialstatements/"+id+"/"+workProgramId+"/"+"0"+"/fsItem";
		Map<String,String> map=new HashMap<String,String>();
		map.put("index", post.getProperty("ETFinancialStatmentIndex"));
		map.put("itemType", post.getProperty("ETFinancialStatementItemType"));
		map.put("taxonomy", taxonomy);
		map.put("displayText", post.getProperty("ETFinancialStatementDisplayText")+rolesUtils.getRandomNumber(1,30));
		
		ETUser_Pojo po=new ETUser_Pojo();
		String createLineItem=po.createFinancialStatement(map);
		
		Response postResponse=rolesUtils.post_URLPOJO(URL, AuthorizationKey, postURI, createLineItem);
		postResponse.prettyPrint();
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/events";
		Response myPermissionResponse1 = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		//myPermissionResponse.prettyPrint();
		JsonPath getJson=myPermissionResponse1.jsonPath();
		 fsliId =getJson.get("events.fsliId");
		 int size=fsliId.size();
		System.out.println("Size---->" +size);
		String fsliid=fsliId.get(size-1);
		System.out.println("Fsli id------> "+fsliid);
		String PutUri="/api/"+post.getProperty("memberFirmSlug")+"/financialstatements/"+id+"/"+workProgramId+"/"+"0"+"/fsItem/"+fsliid;
	
		Map<String,String> map1=new HashMap<String, String>();
		map1.put("displayText",post.getProperty("ETPatchFinancialStatment")+rolesUtils.getRandomNumber(1, 30));
		//ETUser_Pojo po=new ETUser_Pojo();
		String patchApi=po.createFinancialStatement(map1);
		Response patchRes=rolesUtils.patch_URLPOJO(URL, AuthorizationKey, PutUri,patchApi);
		Assert.assertEquals(patchRes.getStatusCode(),204);
		
	}
}
