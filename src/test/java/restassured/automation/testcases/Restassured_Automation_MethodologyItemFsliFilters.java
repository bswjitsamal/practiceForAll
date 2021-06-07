package restassured.automation.testcases;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.ArrayList;
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
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_MethodologyItemFsliFilters {

	String URL;
	String AuthorizationKey;
	List<String> listRevisionId;
	List<String> listOrdId;
	List<String> methodologyId;
	List<String> workFlow;
	List<String> methodlogyItemId;
	Restassured_Automation_Utils allUtils;
	Properties post;

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 3);
	}

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
		Properties BaseUrl = configDetails.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");
		Awaitility.reset();
		Awaitility.setDefaultPollDelay(999, MILLISECONDS);
		Awaitility.setDefaultPollInterval(99, SECONDS);
		Awaitility.setDefaultTimeout(99, SECONDS);

	}

	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_CreateFinancialStatementFilters_200() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName", post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);
		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		Assert.assertEquals(postFilterRes.getStatusCode(), 200);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postFilterRes);
	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_CreateFinancialStatementFilters_400() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName"," ");
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);

		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		Assert.assertEquals(postFilterRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postFilterRes);
	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_CreateFinancialStatementFilters_404() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgramss/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName",post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);

		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		Assert.assertEquals(postFilterRes.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postFilterRes);
	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_CreateFinancialStatementFilters_409() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName", post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);
		allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);
		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		Assert.assertEquals(postFilterRes.getStatusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, postFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(postFilterRes);
	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_PatchFinancialStatementFilters_204() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName", post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);

		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		JsonPath filterJson=postFilterRes.jsonPath();
		String filterId=filterJson.get("id");
		String patchFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter/"+filterId;
		Map<String, String> map1 = new HashMap<String, String>();

		map1.put("filterName", post.getProperty("patchFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po1 = new User_Pojo();
		String UpdateFilter = po1.CreateFsliFilters(map1);
		
		Response patchFilterRes=allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFsli, UpdateFilter);
		
		Assert.assertEquals(patchFilterRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFilterRes);

	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_PatchFinancialStatementFilters_409() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName", post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);

		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		JsonPath filterJson=postFilterRes.jsonPath();
		String filterId=filterJson.get("id");
		String patchFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter/"+filterId;
		Map<String, String> map1 = new HashMap<String, String>();

		map1.put("filterName", post.getProperty("patchFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po1 = new User_Pojo();
		String UpdateFilter = po1.CreateFsliFilters(map1);
		allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFsli, UpdateFilter);
		Response patchFilterRes=allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFsli, UpdateFilter);
		
		Assert.assertEquals(patchFilterRes.getStatusCode(), 409);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFilterRes);

	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_PatchFinancialStatementFilters_400() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName", post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);

		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		JsonPath filterJson=postFilterRes.jsonPath();
		String filterId=filterJson.get("id");
		String patchFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter/"+filterId;
		Map<String, String> map1 = new HashMap<String, String>();

		map1.put("filterName", " ");
		User_Pojo po1 = new User_Pojo();
		String UpdateFilter = po1.CreateFsliFilters(map1);
		
		Response patchFilterRes=allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFsli, UpdateFilter);
		
		Assert.assertEquals(patchFilterRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFilterRes);

	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_PatchFinancialStatementFilters_404() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName", post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);

		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		JsonPath filterJson=postFilterRes.jsonPath();
		String filterId=filterJson.get("id");
		String patchFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filters/"+filterId;
		Map<String, String> map1 = new HashMap<String, String>();

		map1.put("filterName", post.getProperty("patchFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po1 = new User_Pojo();
		String UpdateFilter = po1.CreateFsliFilters(map1);
		
		Response patchFilterRes=allUtils.patch_URLPOJO(URL, AuthorizationKey, patchFsli, UpdateFilter);
		
		Assert.assertEquals(patchFilterRes.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFilterRes);

	}
	
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_DeleteFinancialStatementFilters_204() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName", post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);

		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		JsonPath filterJson=postFilterRes.jsonPath();
		String filterId=filterJson.get("id");
		String patchFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter/"+filterId;
		
		
		Response patchFilterRes=allUtils.delete(URL, AuthorizationKey, patchFsli);
		
		Assert.assertEquals(patchFilterRes.getStatusCode(), 204);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFilterRes);

	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_DeleteFinancialStatementFilters_404() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName", post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);

		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		JsonPath filterJson=postFilterRes.jsonPath();
		String filterId=filterJson.get("id");
		String patchFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filters/"+filterId;
		
		
		Response patchFilterRes=allUtils.delete(URL, AuthorizationKey, patchFsli);
		
		Assert.assertEquals(patchFilterRes.getStatusCode(), 404);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFilterRes);

	}
	@Test(groups = "IntegrationTests")
	public void MethodologyItem_FsliFliters_DeleteFinancialStatementFilters_400() throws IOException {

		allUtils = new Restassured_Automation_Utils();
		post = read_Configuration_Propertites.loadproperty("Configuration");
		/**
		 * FETCHING THE ORG DETAILS
		 */

		Response OrganizationsDetails = allUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		JsonPath jsonPathEvaluator1 = OrganizationsDetails.jsonPath();
		listOrdId = jsonPathEvaluator1.get("id");
		OrganizationsDetails.prettyPrint();
		/**
		 * GETTING THE REVISION ID FROM METHODOLOGY
		 */

		Response getMethodologyRes = allUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", listOrdId.get(7));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		System.out.println(String.valueOf(listRevisionI1.get(1)));
		String revId = String.valueOf(listRevisionI1.get(2));

		String patchId = "/api/methodologyItem/revision/" + revId.substring(1, 25);

		Response getEngagementTypeRes = allUtils.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		getEngagementTypeRes.prettyPrint();
		JsonPath getJson = getEngagementTypeRes.jsonPath();
		String s1 = getEngagementTypeRes.path("find { it.workProgramItemType=='FinancialStatement' }.workProgramId");
		System.out.println("methodologyId" + s1);

		String postFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filter";

		Map<String, String> map = new HashMap<String, String>();

		map.put("filterName", post.getProperty("postFilterName") + allUtils.getRandomNumber(1, 50));
		User_Pojo po = new User_Pojo();
		String createFilter = po.CreateFsliFilters(map);

		Response postFilterRes = allUtils.post_URLPOJO(URL, AuthorizationKey, postFsli, createFilter);

		postFilterRes.prettyPrint();
		JsonPath filterJson=postFilterRes.jsonPath();
		String filterId=filterJson.get("id");
		String patchFsli = "/api/methodologyItem/revision/" + revId.substring(1, 25) + "/workProgram/" + s1
				+ "/fsli/filters/"+filterId;
		
		Response patchFilterRes=allUtils.delete(URL, AuthorizationKey, patchFsli);
		
		Assert.assertEquals(patchFilterRes.getStatusCode(), 400);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(patchFilterRes.statusCode());
		ExtentTestManager.getTest().log(Status.INFO, patchFilterRes.asString());
		allUtils.validate_HTTPStrictTransportSecurity(patchFilterRes);

	}

	
}
