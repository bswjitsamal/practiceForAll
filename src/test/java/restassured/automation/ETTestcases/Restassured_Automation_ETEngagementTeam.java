package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.awaitility.Awaitility;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.ETUser_Pojo;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETEngagementTeam {
	
	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> engagementId;
	List<String> userId;

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
	public void ETEngagement_GetEngagementHistory_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(1);
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/team/history";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
		
	}
	@Test(groups = "IntegrationTests")
	public void ETEngagement_GetEngagementRoles_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(1);
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/team/roles";
		Response myPermissionResponse = rolesUtils.get_URL_Without_Params(URL, AuthorizationKey, getEngagementURI);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
	}
	@Test(groups = "IntegrationTests")
	public void ETEngagement_RemoveAccessManager_Status200() throws IOException {
		Restassured_Automation_Utils rolesUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		String getAllEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/all";
		Response allEnagementRes = rolesUtils.get_URL_QueryParams(URL, AuthorizationKey, getAllEngagementURI, "1", "100");
		allEnagementRes.prettyPrint();
		JsonPath allEngagmentJson=allEnagementRes.jsonPath();
		engagementId=allEngagmentJson.get("id");
		String id=engagementId.get(1);
		Collection<? extends List<String>> userId1=allEngagmentJson.get("accessManagers");
		ArrayList<List<String>> list=new ArrayList<List<String>>(userId1.size());
		list.addAll(userId1);
		String user=list.get(1).get(0);
		
		/*
		for (int i = 0; i < list.size(); i++) {
			System.out.println("List size"+list.size());
            for (int j = 0; j < list.get(i).size(); j++) {
            	System.out.println("Column size"+list.get(i).size());
                System.out.print(list.get(i).get(j) + " ");
            }
            System.out.println();
        }*/
    
				
		
		/*userId=allEngagmentJson.get("accessManagers");
		List<List<String>> list=Lists.newArrayList();
		list.add(userId);
		List<String> user=list.get(1);
		System.out.println("User---->"+user);
		System.out.println();
	
		System.out.println("User Id----->"+list.size());*/
		
		
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("userId",user);
		ETUser_Pojo po=new ETUser_Pojo();
		String user2=po.putPortalRoles(map);
		
		String getEngagementURI = "/api/" + post.getProperty("memberFirmSlug") + "/engagements/"+id+"/team/accessManager";
		Response myPermissionResponse = rolesUtils.delete_URLPOJO(URL, AuthorizationKey, getEngagementURI,user2);
		myPermissionResponse.prettyPrint();
		Assert.assertEquals(myPermissionResponse.getStatusCode(), 200);
	}
	

}
