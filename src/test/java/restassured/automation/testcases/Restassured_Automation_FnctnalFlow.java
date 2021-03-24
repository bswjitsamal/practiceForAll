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

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.utils.Excel_Data_Source_Utils;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class Restassured_Automation_FnctnalFlow {
	ExtentReports extent;
	ExtentTest logger;
	
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
	
	
	
	
	// Validating HTTP Strict Transport security
	public void validate_HTTPStrictTransportSecurity(Response response) {

		// Reader header of a give name. In this line we will get Header named Server
		String strictTransportSecurity = response.header("Strict-Transport-Security");
		System.out.println("Server value: " + strictTransportSecurity);
		
		if("max-age=63072000; includeSubDomains; preload".equals(strictTransportSecurity)) {
			System.out.println("This is following HTTPStrictTransportSecurity");
				
		}else {
			System.out.println("This is NOT following HTTPStrictTransportSecurity");
			
		}
		//Assert.assertEquals("max-age=63072000; includeSubDomains; preload", strictTransportSecurity);
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
		
		validate_HTTPStrictTransportSecurity(createEngagementType);

		Assert.assertEquals(createEngagementType.statusCode(), statusCode);

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
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions.id");
		String methodologyId = jsonPathEvaluator.get("id");

		// Printing the value on the console and saving on excel
		System.out.println("The newly created Methodology with revision ID: " + String.valueOf(listRevisionI1.get(0)));
		String revId = String.valueOf(listRevisionI1.get(0));
		// System.out.println(revId.);

		// Updating the revId on the 3rd and 4th sheet
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 3, 1, 0);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 4, 1, 0);
		//Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 4, 2, 0);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 5, 1, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 5, 2, 0);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 6, 1, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 6, 2, 0);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 7, 1, 0);
		/*Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 7, 2, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 7, 3, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 7, 4, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 7, 5, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 7, 6, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 7, 7, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 7, 8, 0);*/
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 8, 1, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 8, 2, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 8, 3, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 8, 4, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 8, 5, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 8, 6, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 8, 7, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 8, 8, 0);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 9, 1, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 9, 2, 0);
		/*Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 9, 3, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 9, 4, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 9, 5, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 9, 6, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 9, 7, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 9, 8, 0);*/
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 10, 1, 0);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, revId, 10, 2, 0);

		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, methodologyId, 3, 1, 5);
		// Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
		// methodologyId, 5, 1, 4);

		
		validate_HTTPStrictTransportSecurity(createMethodology);
		
		Assert.assertEquals(createMethodology.statusCode(), statusCode);

	}
	
	public void sample() {
		String a = "biswajit";
		
		char[] c = a.toCharArray();
	for(int i=c.length;i>=0;i--) {
			System.out.println(c[i]);
		}
		
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
		// data.put("index", revisions);
		data.put("title", title);
		data.put("itemType", itemType);
		data.put("index", index);

		User_Pojo createMethodologyPojo = new User_Pojo();
		String createMethodologyData = createMethodologyPojo.phaseAdd(data);
		System.out.println("--->" + createMethodologyData);

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postMethodologyItem = MethodologyItem.post_URL(URL, AuthorizationKey, patchId, createMethodologyData);
		postMethodologyItem.prettyPrint();
		
		JsonPath jsonPathEvaluator = postMethodologyItem.jsonPath();
		
		// Storing the value with name Sheet4 - Row 1, Column 0
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(jsonPathEvaluator.get("methodologyItemId")), 4, 1, 4);
		
		// Storing the value with name Sheet4 - Row 1, Column 0
		/*Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 4, 1, 4);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 4, 2, 4);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 1, 3);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 1, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 2, 3);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 2, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 3, 3);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 3, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 4, 3);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 4, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 5, 3);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 5, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 6, 3);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 6, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 7, 3);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 7, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 8, 3);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath,
				removeDoubleQuotes(postMethodologyItem.asString()), 7, 8, 1);
		*/
		

		validate_HTTPStrictTransportSecurity(postMethodologyItem);
		Assert.assertEquals(postMethodologyItem.statusCode(), statusCode);

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
			String workProgramType, String tailoring, String responseCode)
			throws IOException, RowsExceededException, BiffException, WriteException {

		ArrayList<String> list = new ArrayList<String>();
		/*List<String> list = new ArrayList<String>();
		List<String> list1 = new ArrayList<String>();*/
		
		
		for (int i = 0; i < 2; i++) {
		
			int statusCode = Integer.parseInt(responseCode);

			String patchId = "/api/methodologyItem/revision/" + revisions + "/item";

			// Iterating with data

			// data.put("index", revisions);
			data.put("title", title+getRandomAlphaNum());
			data.put("itemType", itemType);
			data.put("index", index);
			data.put("parentId", parentId);
			data.put("workProgramType", workProgramType);
			data.put("tailoring", tailoring);

			User_Pojo createQnAWorkProgramPojo = new User_Pojo();
			String createQnAWorkProgramData = createQnAWorkProgramPojo.workProgramAdd(data);
			System.out.println("--->" + createQnAWorkProgramData);

			Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

			// For nu of iteration:
			Response postQnAWP = MethodologyItem.post_URL(URL, AuthorizationKey, patchId,
					createQnAWorkProgramData.replace("revisions", "parentId"));
			System.out.println("Biswajit--------" + postQnAWP.asString());

			// Excel_Data_Source_Utils.writeToExcelWithMinMax(originalExcelPath,copyExcelPath,
			// removeDoubleQuotes(postQnAWP.asString()), 5, 7, 1, 2, 1);
			postQnAWP.prettyPrint();
			
			JsonPath jsonPathEvaluator = postQnAWP.jsonPath(); 
			
			validate_HTTPStrictTransportSecurity(postQnAWP);
			Assert.assertEquals(postQnAWP.statusCode(), statusCode);
             
			list.add(jsonPathEvaluator.get("methodologyItemId"));
			
		}
		

		System.out.println("Satyajit-- " + list);

		// Storing the WPId value with name Sheet5,6,7 - Row 1&2, Column 1 - starting
		// form 0th
		// index
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 5, 1, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 5, 2, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 6, 1, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 6, 2, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 7, 1, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 7, 1, 3);
		
		
		/*Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 7, 2, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 7, 2, 3);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 7, 3, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 7, 3, 3);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 7, 4, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 7, 4, 3);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 7, 5, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 7, 5, 3);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 7, 6, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 7, 6, 3);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 7, 7, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 7, 7, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 7, 8, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 7, 8, 3);*/


	}
	
	
	
	
	

	/**
	 * 
	 * STEP-5: CREATE A SINGLE INSTANCE WP (Performing patch operation)
	 * 
	 */

	@DataProvider(name = "fifthDataProvider")
	public Object[][] createWorkProgramType() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "createWPtype", 6);
		System.out.println("getData function executed!!");
		return retObjArr;
	}

	@Test(priority = 6, groups = "IntegrationTests", dataProvider = "fifthDataProvider")
	public void createWorkProgramType(String revisions, String WPId, String workProgramType, String tailoring,
			String visibility,String responseCode) throws IOException, RowsExceededException, BiffException, WriteException {

		int statusCode = Integer.parseInt(responseCode);

		// System.out.println("==========================="+WPId);

		String patchId = "/api/methodologyItem/revision/" + revisions + "/item/" + WPId;

		// Iterating with data

		data.put("workProgramType", workProgramType);
		data.put("tailoring", tailoring);
		data.put("visibility", visibility);

		User_Pojo createQnAWorkProgramPojo1 = new User_Pojo();
		String createQnAWorkProgramData1 = createQnAWorkProgramPojo1.workProgramTypeAdd(data);
		System.out.println("--->" + createQnAWorkProgramData1);

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postQnAWP = MethodologyItem.patch_URL(URL, AuthorizationKey, patchId, createQnAWorkProgramData1);
		postQnAWP.prettyPrint();

		validate_HTTPStrictTransportSecurity(postQnAWP);
		Assert.assertEquals(postQnAWP.statusCode(), statusCode);

	}
	
	
	
	
	

	/**
	 * 
	 * STEP -7: CREATE A SINGLE INSTANCE WP (Performing POST operation)
	 * 
	 */

	@DataProvider(name = "sixthDataProvider")
	public Object[][] initializeWorkProgramType() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "initializeWP", 5);
		System.out.println("getData function executed!!");
		return retObjArr;
	}

	@Test(priority = 7, groups = "IntegrationTests", dataProvider = "sixthDataProvider")
	public void initializeWorkProgramType(String revisions, String WPId, String ruleContextType,
			String ruleContextSource, String responseCode)
			throws IOException, RowsExceededException, BiffException, WriteException {

		int statusCode = Integer.parseInt(responseCode);

		String patchId = "/api/methodologyItem/revision/" + revisions + "/workProgram/" + WPId + "/initialize";

		// Iterating with data

		data.put("ruleContextType", ruleContextType);
		//data.put("ruleContextSource", ruleContextSource);

		User_Pojo initializeQnAWorkProgramPojo = new User_Pojo();
		String initializeQnAWorkProgramData = initializeQnAWorkProgramPojo.initializeWorkProgramType(data);
		System.out.println("--->" + initializeQnAWorkProgramData);

		Restassured_Automation_Utils MethodologyItem = new Restassured_Automation_Utils();

		Response postQnAWP = MethodologyItem.post_URL(URL, AuthorizationKey, patchId, initializeQnAWorkProgramData);
		postQnAWP.prettyPrint();

		validate_HTTPStrictTransportSecurity(postQnAWP);
		Assert.assertEquals(postQnAWP.statusCode(), statusCode);

	}
	
	
	
	
	

	/**
	 * 
	 * STEP -8: CREATE A PROCEDURE (Performing POST operation)
	 * 
	 */

	@DataProvider(name = "seventhDataProvider")
	public Object[][] createProcedure() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "createProcedure", 7);
		System.out.println("getData function executed!!");
		return retObjArr;
	}

	@Test(priority = 8, groups = "IntegrationTests", dataProvider = "seventhDataProvider")
	public void createProcedure(String revisions, String workProgramId, String itemType, String parentId, String index,
			String title, String responseCode)
			throws IOException, RowsExceededException, BiffException, WriteException {

		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < 8; i++) {
							
			int statusCode = Integer.parseInt(responseCode);

			String patchId = "/api/methodologyItem/revision/" + revisions + "/item";

			// Iterating with datais

			data.put("itemType", itemType);
			data.put("index", index);
			data.put("parentId", parentId);
			data.put("workProgramId", workProgramId);
			data.put("title", "procedure"+i);

			User_Pojo createMethodologyPojo = new User_Pojo();
			String createMethodologyData = createMethodologyPojo.procedureAdd(data);
			System.out.println("--->" + createMethodologyData);

			Restassured_Automation_Utils CreateProcedure = new Restassured_Automation_Utils();
			
			String firstReplacment = createMethodologyData.replace("revisions", "parentId");
			String scndReplacement = firstReplacment.replace("WPId", "workProgramId");
			

			Response postCreateProcedure = CreateProcedure.post_URL(URL, AuthorizationKey, patchId, scndReplacement);
			postCreateProcedure.prettyPrint();

			validate_HTTPStrictTransportSecurity(postCreateProcedure);
			Assert.assertEquals(postCreateProcedure.statusCode(), statusCode);
			
			JsonPath jsonPathEvaluator = postCreateProcedure.jsonPath(); 
			
			list.add(jsonPathEvaluator.get("methodologyItemId"));	

		}

		System.out.println("Satyajit-- " + list);

		
		// Storing the value with name Sheet8 & 9 - Row 1 & 2, Column 1
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 8, 1, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 8, 2, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(2)), 8, 3, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(3)), 8, 4, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(4)), 8, 5, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(5)), 8, 6, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(6)), 8, 7, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(7)), 8, 8, 1);
		

		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(6)), 9, 1, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(7)), 9, 2, 1);
		
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(6)), 10, 1, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(7)), 10, 2, 1);
		
		/*Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(2)), 9, 3, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(3)), 9, 4, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(4)), 9, 5, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(5)), 9, 6, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(6)), 9, 7, 1);
		Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(7)), 9, 8, 1);*/
         
	}
	
	
	
	
	
	
	/**
	 * 
	 * STEP -9: CREATE A PROCEDURE TYPE (Performing PATCH operation)
	 * 
	 */
	
	@DataProvider(name = "eighthDataProvider")
	public Object[][] createProcedureType() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "createProcedureType", 4);
		System.out.println("getData function executed!!");
		return retObjArr;
	}
	
	
	@Test(priority = 9, groups = "IntegrationTests", dataProvider = "eighthDataProvider")
	public void createProcedureType(String revisions, String itemId, String workProgramItemType, String responseCode)
			throws IOException, RowsExceededException, BiffException, WriteException {
		
		//String[] workProgramItemType1 =  {"ResponseLink","Number","Statement","DatePicker","TextContent","TextQuestion","MultiItemSelectQuestion","SingleItemSelectQuestion"};
		

			int statusCode = Integer.parseInt(responseCode);

			String patchId = "/api/methodologyItem/revision/" + revisions + "/item/" + itemId;

			// Iterating with datais
			
			data.put("workProgramItemType", workProgramItemType);
			
			User_Pojo createMethodologyPojo = new User_Pojo();
			String createMethodologyData = createMethodologyPojo.procedureTypeAdd(data);
			System.out.println("--->" + createMethodologyData);

			Restassured_Automation_Utils CreateProcedure = new Restassured_Automation_Utils();

			Response postCreateProcedure = CreateProcedure.patch_URL(URL, AuthorizationKey, patchId, createMethodologyData.replace("WPId", "workProgramItemType"));
			postCreateProcedure.prettyPrint();

			validate_HTTPStrictTransportSecurity(postCreateProcedure);
			Assert.assertEquals(postCreateProcedure.statusCode(), statusCode);
	
		
		// Storing the value with name Sheet8 - Row 1 & 2, Column 1
		//Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(0)), 8, 1, 1);
		//Excel_Data_Source_Utils.writeToExcel(originalExcelPath, copyExcelPath, removeDoubleQuotes(list.get(1)), 8, 2, 1);

	}
	
	
	
	
	
	/**
	 * 
	 * STEP -10: CREATE A OPTIONS (Performing PUT operation)
	 * 
	 */
	
	@DataProvider(name = "ninthDataProvider")
	public Object[][] createProcedureOptions() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "createOptions", 3);
		System.out.println("getData function executed!!");
		return retObjArr;
	}
	
	

	@Test(priority = 10, groups = "IntegrationTests", dataProvider = "ninthDataProvider")
	public void createProcedureOptions(String revisions, String itemId, String responseCode)
			throws IOException, RowsExceededException, BiffException, WriteException {

			int statusCode = Integer.parseInt(responseCode);

			String patchId = "/api/methodologyItem/revision/" + revisions + "/itemSelectQuestion/" + itemId + "/option";

			User_Pojo createMethodologyPojo = new User_Pojo();
			String createMethodologyData = createMethodologyPojo.createOption();
			System.out.println("--->" + createMethodologyData);

			Restassured_Automation_Utils CreateProcedure = new Restassured_Automation_Utils();

			Response postCreateProcedure = CreateProcedure.put_URL(URL, AuthorizationKey, patchId, createMethodologyData);
			postCreateProcedure.prettyPrint();

			validate_HTTPStrictTransportSecurity(postCreateProcedure);
			Assert.assertEquals(postCreateProcedure.statusCode(), statusCode);
	
	}
	
	
	/**
	 * 
	 * STEP -11: CREATE A OPTIONS (Performing PUT operation)
	 * 
	 */
	
	@DataProvider(name = "tenthDataProvider")
	public Object[][] updateProcedureOptions() throws Exception {
		Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(originalExcelPath, "updateOptios-10", 4);
		System.out.println("getData function executed!!");
		return retObjArr;
	}
	
	
	@Test(priority = 11, groups = "IntegrationTests", dataProvider = "tenthDataProvider")
	public void updateProcedureOptions(String revisions, String itemId, String title, String responseCode)
			throws IOException, RowsExceededException, BiffException, WriteException {

			int statusCode = Integer.parseInt(responseCode);
			
			//Adding more options the respected Item

			String patchIdOption = "/api/methodologyItem/revision/" + revisions + "/itemSelectQuestion/" + itemId + "/option";
			//String patchIdOption1 = "/api/methodologyItem/revision/" + revisions + "/item/" + itemId ;
			
			// Iterating with datais
			data.put("title", title);
						

			User_Pojo createMethodologyPojo = new User_Pojo();
			String createMethodologyData = createMethodologyPojo.updateOption(data);
			System.out.println("--->" + createMethodologyData);

			Restassured_Automation_Utils CreateProcedure = new Restassured_Automation_Utils();

			Response postCreateProcedure = CreateProcedure.post_URL(URL, AuthorizationKey, patchIdOption, createMethodologyData);
			postCreateProcedure.prettyPrint();
			
			/*Response postCreateProcedure1 = CreateProcedure.patch_URL(URL, AuthorizationKey, patchIdOption1, createMethodologyData);
			postCreateProcedure1.prettyPrint();
			*/
			
			
			Assert.assertEquals(postCreateProcedure.statusCode(), statusCode);
			//Assert.assertEquals(postCreateProcedure1.statusCode(), statusCode);
	
	}
	
}
