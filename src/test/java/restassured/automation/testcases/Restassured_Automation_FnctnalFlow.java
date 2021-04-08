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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import restassured.automation.Pojo.MethodologyItem_Pojo;
import restassured.automation.Pojo.Methodology_Pojo;
import restassured.automation.Pojo.RootConditionGroup;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.Pojo.MethodologyItem_Pojo.InitData;
import restassured.automation.listeners.ExtentTestManager;
import restassured.automation.utils.Excel_Data_Source_Utils;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_FnctnalFlow {

	String exlPth = "./resources/TestData.xlsx";
	String sheetName = "Sheet1";
	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrgId;
	List<String> engagementTypeId;
	List<String> methodologyId;

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
		Properties BaseUrl = read_Configuration_Propertites.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");

	}

	@Test(priority = 1)
	public void updateExcelFieds() throws IOException, RowsExceededException, BiffException, WriteException {

		/**
		 * STEP-0: GETITNG AN ORG ID AND UPDATING THE SAME IN THE EXCEL
		 */

		Response getOrgId = restUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		getOrgId.prettyPrint();

		// Retrieving the OrgId with index from the response
		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");
		System.out.println("---------------" + listOrgId.get(3));

		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, listOrgId.get(3), 1, 1, 1);

	}

	/**
	 * 
	 * STEP-1: CREATE AN ENGAGEMENT TYPE (Calling the POST method)
	 * 
	 */

	// Using testNg @DataProvider to iterate over the excel value
	@DataProvider(name = "firstDataProvider")
	public Object[][] createEngagement() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "createEngagement", 3);
		System.out.println("getData function executed!!");
		return retObjArr;
	}

	@Test(priority = 2, groups = "IntegrationTests", dataProvider = "firstDataProvider")
	public void createEngagement(String title, String organization, String responseCode)
			throws IOException, RowsExceededException, BiffException, WriteException {

		int statusCode = Integer.parseInt(responseCode);

		// Iterating with data
		data.put("title", title);
		data.put("organization", organization);

		User_Pojo createEngagement = new User_Pojo();
		String createEngagementData = createEngagement.Create_Engagement(data);
		System.out.println("--->" + createEngagementData);
		Response createEngagementType = restUtils.post_URL(URL, AuthorizationKey, "/api/engagementtype",
				createEngagementData);
		createEngagementType.prettyPrint();

		// Retrieving the newly created EngagementId
		JsonPath jsonPathEvaluator = createEngagementType.jsonPath();
		String engagementId1 = jsonPathEvaluator.get("id");
		System.out.println("The newly created Engatement Id is - " + engagementId1);

		// Storing the value with name Sheet1 - Column 1, Row 1
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, engagementId1, 2, 1, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, engagementId1, 2, 2, 1);

		Assert.assertEquals(createEngagementType.statusCode(), statusCode);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createEngagementType.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createEngagementType.asString());
		restUtils.validate_HTTPStrictTransportSecurity(createEngagementType);

	}
	
	/**
	 * 
	 * SPTE-2: CREATING A NEW METHODOLOGY (calling the post Method)
	 * 
	 */

	@DataProvider(name = "secondDataProvider")
	public Object[][] createMethodology() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "createMethodology", 4);
		System.out.println("getData function executed!!");
		return retObjArr;
	}

	@Test(priority = 3, groups = "IntegrationTests", dataProvider = "secondDataProvider")
	public void createMethodology(String title, String engagementType, String responseCode, String draftDescription)
			throws IOException, RowsExceededException, BiffException, WriteException {

		int statusCode = Integer.parseInt(responseCode);

		// Iterating with data
		data.put("title", title);
		data.put("engagementType", engagementType);
		data.put("draftDescription", draftDescription);

		User_Pojo createMethodologyPojo = new User_Pojo();
		String createMethodologyData = createMethodologyPojo.methodologyAdd(data);
		System.out.println("--->" + createMethodologyData);

		Response createMethodology = restUtils.post_URL(URL, AuthorizationKey, "/api/methodology",
				createMethodologyData);
		createMethodology.prettyPrint();

		// Retrieving the revisionId for newly created Methodology
		JsonPath jsonPathEvaluator = createMethodology.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");
		String methodologyId = jsonPathEvaluator.get("id");

		// Printing the value on the console and saving on excel
		System.out.println("The newly created Methodology with revision ID: " + String.valueOf(listRevisionI1.get(0)));
		String revId = String.valueOf(listRevisionI1.get(0));

		// Updating the revId on the 3rd and 4th sheet
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 3, 1, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 4, 1, 0);

		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, methodologyId, 3, 1, 5);

		Assert.assertEquals(createMethodology.statusCode(), statusCode);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(createMethodology.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,createMethodology.asString());
		restUtils.validate_HTTPStrictTransportSecurity(createMethodology);


	}

	/**
	 * 
	 * SPTE-3: CREATING A NEW PHASE-1 (Or MethodologyItem With in the methodology
	 * Tree)(calling the post Method)
	 * 
	 */
	
	public static String removeDoubleQuotes(String input) {

		StringBuilder sb = new StringBuilder();

		char[] tab = input.toCharArray();
		for (char current : tab) {
			if (current != '"')
				sb.append(current);
		}

		return sb.toString();
	}
	

	@DataProvider(name = "thirdDataProvider")
	public Object[][] createPhase() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "createPhase", 5);
		System.out.println("getData function executed!!");
		return retObjArr;
	}

	@Test(priority = 4, groups = "IntegrationTests", dataProvider = "thirdDataProvider")
	public void createPhase(String revisions, String title, String itemType, String index, String responseCode)
			throws IOException, RowsExceededException, BiffException, WriteException {

		int statusCode = Integer.parseInt(responseCode);

		String patchId = "/api/methodologyItem/revision/" + revisions + "/item";

		// Iterating with data
		//data.put("index", revisions);
		data.put("title", title);
		data.put("itemType", itemType);
		data.put("index", index);

		User_Pojo createMethodologyPojo = new User_Pojo();
		String createMethodologyData = createMethodologyPojo.phaseAdd(data);
		System.out.println("--->" + createMethodologyData);

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URL(URL, AuthorizationKey, patchId, createMethodologyData);
		postMethodologyItem.prettyPrint();

		// Storing the value with name Sheet4 - Row 1, Column 0
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(postMethodologyItem.asString()), 4, 1, 4);

		Assert.assertEquals(postMethodologyItem.statusCode(), statusCode);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postMethodologyItem.asString());
		restUtils.validate_HTTPStrictTransportSecurity(postMethodologyItem);

	}

	/**
	 * 
	 * STEP-4: CREATE A NEW METHODOLOGY ITEM WITHIN THE METHODOLOGY TREE
	 * 
	 */
	 

	@DataProvider(name = "fourthDataProvider")
	public Object[][] createQnAworkProgram() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "createQnAWorkPro", 8);
		System.out.println("getData function executed!!");
		return retObjArr;
	}

	@Test(priority = 5, groups = "IntegrationTests", dataProvider = "fourthDataProvider")
	public void createQnAWorkProgram(String revisions, String title, String itemType, String index, String parentId,
			String workProgramType ,String tailoring, String responseCode) throws IOException, RowsExceededException, BiffException, WriteException {
		
		int statusCode = Integer.parseInt(responseCode);

		String patchId = "/api/methodologyItem/revision/" + revisions + "/item";

		// Iterating with data
		
		//data.put("index", revisions);
		data.put("title", title);
		data.put("itemType", itemType);
		data.put("index", index);
		data.put("parentId", parentId);
		data.put("workProgramType", workProgramType);
		data.put("tailoring", tailoring);
		
		User_Pojo createQnAWorkProgramPojo = new User_Pojo();
		String createMethodologyData = createQnAWorkProgramPojo.workProgramAdd(data);
		System.out.println("--->" + createQnAWorkProgramPojo);
		
		//replacing the values
		//createMethodologyData.replace("revisions", "parentId");

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URL(URL, AuthorizationKey, patchId, createMethodologyData.replace("revisions", "parentId"));
		postMethodologyItem.prettyPrint();

		// Storing the value with name Sheet4 - Row 1, Column 0
		//Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, postMethodologyItem.asString(), 4, 1, 0);

		Assert.assertEquals(postMethodologyItem.statusCode(), statusCode);
		/**
		 * Extent report generation
		 */
		ExtentTestManager.statusLogMessage(postMethodologyItem.statusCode());
		ExtentTestManager.getTest().log(Status.INFO,postMethodologyItem.asString());
		MethodologyItem.validate_HTTPStrictTransportSecurity(postMethodologyItem);



	}

}
