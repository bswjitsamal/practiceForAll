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
import org.testng.annotations.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import restassured.automation.Pojo.Methodology_Pojo;
import restassured.automation.Pojo.User_Pojo;
import restassured.automation.utils.ExcelUtils;
import restassured.automation.utils.ExcelWriter;
import restassured.automation.utils.Excel_Data_Source_Utils;
import restassured.automation.utils.Restassured_Automation_Utils;
import restassured.automation.utils.read_Configuration_Propertites;

public class SampleExtentReport {

	String exlPth = "./resources/TestData.xlsx";
	String sheetName = "Sheet1";
	String URL;
	String AuthorizationKey;
	List<String> listRuleId;
	List<String> listOrgId;
	List<String> engagementTypeId;
	List<String> methodologyId;

	final static String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";

	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 4);
	}

	@BeforeSuite
	public void beforeSuite() {

	}

	@BeforeTest(groups = { "IntegrationTests", "EndToEnd", "IntegrationTests1" })
	public void setup() throws IOException {

		read_Configuration_Propertites configDetails = new read_Configuration_Propertites();
		Properties BaseUrl = read_Configuration_Propertites.loadproperty("Configuration");
		URL = BaseUrl.getProperty("ApiBaseUrl");
		AuthorizationKey = BaseUrl.getProperty("AuthorizationKey");

	}

	@Test(groups = { "IntegrationTests" })
	public void getTheListOfRulesForARevision_status200() throws IOException {

		Restassured_Automation_Utils getRulesByRevId = new Restassured_Automation_Utils();

		/**
		 * Retrieving list of all records
		 */
		Response getMethodologyRes = getRulesByRevId.get_URL_Without_Params(URL, AuthorizationKey, "/api/methodology");
		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		System.out.println(String.valueOf(listRevisionI1.get(1)));

		String revId = String.valueOf(listRevisionI1.get(1));

		String patchId = "/api/rules/revision/" + revId.substring(1, revId.length() - 1) + "/list";

		Restassured_Automation_Utils rules = new Restassured_Automation_Utils();

		Response RulesDetails = rules.get_URL_Without_Params(URL, AuthorizationKey, patchId);
		RulesDetails.prettyPrint();

		Assert.assertEquals(RulesDetails.statusCode(), 200);

		JsonPath jsonPathEvaluator1 = RulesDetails.jsonPath();
		listRuleId = jsonPathEvaluator1.get("ruleId");
		System.out.println(listRuleId);

	}

	@Test(groups = { "IntegrationTests" })
	public void performEndToEndForAll() throws IOException {

		Restassured_Automation_Utils getRulesByRevId = new Restassured_Automation_Utils();
		ExcelUtils exlUtil = new ExcelUtils(exlPth, sheetName);
		exlUtil.getRowCount();
		exlUtil.getSpecificCellData(1, 2);
		exlUtil.getAllCellData();

	}

	Restassured_Automation_Utils restUtils = new Restassured_Automation_Utils();
	Map<String, String> data = new HashMap<String, String>();

	@Test
	public void updateExcelFieds() throws IOException, RowsExceededException, BiffException, WriteException {

		/**
		 * STEP-0: GETITNG AN ORG ID AND UPDATING THE SAME IN THE EXCEL
		 */

		Response getOrgId = restUtils.get_URL_Without_Params(URL, AuthorizationKey, "/api/org");
		getOrgId.prettyPrint();

		JsonPath jsonPathEvaluator = getOrgId.jsonPath();
		listOrgId = jsonPathEvaluator.get("id");
		System.out.println("---------------" + listOrgId.get(4));

		/*
		 * Excel_Data_Source_Utils.updateExcelFile(1,1,listOrgId.get(4),1);
		 * Excel_Data_Source_Utils.updateExcelFile(3,1,listOrgId.get(4),1);
		 */
		
		String ExcelPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
				+ "TestDataSource.xls";
		
		String copyExcelPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
				+ "TestDataSource1.xls";
		

		Excel_Data_Source_Utils.writeToExcel(ExcelPath, copyExcelPath, listOrgId.get(4), 1, 1, 1);

	}
	
	
	String ExcelPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
			+ "TestDataSource.xls";
	
	  @DataProvider (name = "dataSource")
	    public Object[][] getLoginData() throws Exception {
	        Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(ExcelPath, "endToEnd",3);
	        System.out.println("getData function executed!!");
	        return retObjArr;
	    }
	  

	@Test(dependsOnMethods = {"updateExcelFieds" }, groups = "IntegrationTests", dataProvider = "dataSource")
	public void createEngagement(String title, String organization, String responseCode)
			throws IOException, RowsExceededException, BiffException, WriteException {

		/**
		 * STEP-1: CREATE AN ENGAGEMENT TYPE (Calling the POST method)
		 */

		int statusCode = Integer.parseInt(responseCode);

		// Restassured_Automation_Utils restUtils = new Restassured_Automation_Utils();

		// Map<String, String> data = new HashMap<String, String>();
		data.put("title", title);
		data.put("organization", organization);

		User_Pojo createEngagement = new User_Pojo();
		String createEngagementData = createEngagement.Create_Engagement(data);
		System.out.println("--->" + createEngagementData);
		Response createEngagementType = restUtils.post_URL(URL, AuthorizationKey, "/api/engagementtype",
				createEngagementData);
		createEngagementType.prettyPrint();

		// Retrieving the EngagementId
		JsonPath jsonPathEvaluator = createEngagementType.jsonPath();
		String engagementId1 = jsonPathEvaluator.get("id");
		System.out.println("---------------" + engagementId1);

		// Update the engagementId in the excel
		// Excel_Data_Source_Utils.updateExcelFile(1,1,engagementId1,2);
		
		String ExcelPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
				+ "TestDataSource.xls";
		
		String copyExcelPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator
				+ "TestDataSource1.xls";
		
		Excel_Data_Source_Utils.writeToExcel(ExcelPath, copyExcelPath,engagementId1,1, 2, 1 );

		Assert.assertEquals(createEngagementType.statusCode(), statusCode);

	}
	
	String ExcelPath1 = System.getProperty("user.dir") + File.separator + "resources" + File.separator
			+ "TestDataSource1.xls";
	
	  @DataProvider (name = "dataSource")
	    public Object[][] getLoginData1() throws Exception {
	        Object[][] retObjArr = Excel_Data_Source_Utils.get_Data(ExcelPath1, "Test",3);
	        System.out.println("getData function executed!!");
	        return retObjArr;
	    }

	@Test(dependsOnMethods = {
			"createEngagement" }, groups = "IntegrationTests", dataProvider = "dataSource", dataProviderClass = Excel_Data_Source_Utils.class)
	public void createMethodology(String title, String organization, String responseCode, String engagementType,
			String draftDescription) throws IOException, RowsExceededException, BiffException, WriteException {

		/**
		 * RETRIVING LIST OF METHODOLOGY
		 */

		Response getMethodologyRes = restUtils.get_URL_QueryParams(URL, AuthorizationKey, "/api/methodology",
				"Organization", data.put("organization", organization));

		getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator = getMethodologyRes.jsonPath();
		engagementTypeId = jsonPathEvaluator.get("engagementType");
		listOrgId = jsonPathEvaluator.get("organization");
		ArrayList<Map<String, ?>> listRevisionI1 = jsonPathEvaluator.get("revisions");

		String revId = String.valueOf(listRevisionI1.get(1));
		System.out.println(engagementTypeId);
		System.out.println(revId);

		// Update the engagementId in the excel
		// Excel_Data_Source_Utils.updateExcelFile(1,4,revId,1);
		
		Excel_Data_Source_Utils.writeToExcel("./TestDataSource.xls", "./TestDataSource1.xls",revId,1, 3, 1 );

		/**
		 * STEP-2: CREATE A NEW METHODOLOGY (Calling the POST method)
		 */

		Methodology_Pojo mp = new Methodology_Pojo();
		mp.setTitle(data.put("title", title));
		mp.setEngagementType(data.put("engagementType", engagementType));
		mp.setOrganization(data.put("draftDescription", draftDescription));
		// mp.setRevisions(new String[] { String.valueOf(listRevisionI1.get(1)) });
		// mp.setId("6015609b192b4718edc2da5d");

		// Restassured_Automation_Utils getMethodologyByrevisonId = new
		// Restassured_Automation_Utils();

		Response getMethodologyByrevisonIdRes = restUtils.post_URLPOJO(URL, AuthorizationKey, "/api/methodology", mp);
		getMethodologyByrevisonIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByrevisonIdRes.statusCode(), 200);

		/**
		 * SPTE-3: GET DETAILS ABOUT A SPECIFIC METHODOLOGY
		 */

		Restassured_Automation_Utils getMethodology = new Restassured_Automation_Utils();

		// Response getMethodologyRes = getMethodology.get_URL_QueryParams(URL,
		// AuthorizationKey, "/api/methodology","Organization",data.put("organization",
		// organization));
		// getMethodologyRes.prettyPrint();

		JsonPath jsonPathEvaluator1 = getMethodologyRes.jsonPath();
		methodologyId = jsonPathEvaluator1.get("id");

		System.out.println(methodologyId);

		Response getMethodologyByIdRes = getMethodology.get_URL_WithOne_PathParams(URL, AuthorizationKey,
				"/api/methodology/{value}", methodologyId.get(0));
		getMethodologyByIdRes.prettyPrint();

		Assert.assertEquals(getMethodologyByIdRes.statusCode(), 200);

	}

}
