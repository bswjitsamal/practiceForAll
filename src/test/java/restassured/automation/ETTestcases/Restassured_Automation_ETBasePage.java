package restassured.automation.ETTestcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.awaitility.Awaitility;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_ETBasePage {
	String URL;
	String AuthorizationKey;
	Properties post;
	List<String> engagementId;

	Restassured_Automation_Utils restUtils = new Restassured_Automation_Utils();
	Map<String, String> data = new HashMap<String, String>();

	// Primary excel file
	String originalExcelPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
			+ "TestDataSource - Copy.xls";

	// copy of the Primary excel file
	String copyExcelPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
			+ "TestDataSource - Copy.xls";

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 4);
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

	@Test(priority = 0)
	public void updateExcelSheet() throws IOException {
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
		JsonPath res = myPermissionResponse.jsonPath();
		List<Object> o=new ArrayList<Object>();
		HashMap<String, HashMap<String,String>> item = res.get("items");
		Map<String, Object> list = new HashMap<String, Object>();
		for(Entry e:item.entrySet()){
		System.out.println(e.getValue());
		
		
		JSONObject jObj=new JSONObject(myPermissionResponse);
		JSONObject keyJson=jObj.getJSONObject("items");
		Iterator keys = keyJson.keys();
		while(keys.hasNext()) {
		    // loop to get the dynamic key
		    String currentDynamicKey = (String)keys.next();
		    JSONObject currentDynamicValue = keyJson.getJSONObject(currentDynamicKey);
		    int jsonSize=currentDynamicValue.length();
		    System.out.println(jsonSize);
		    
		}
		
	
			
			
		}

		
	}
}
