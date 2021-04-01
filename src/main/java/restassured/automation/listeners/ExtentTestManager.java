package restassured.automation.listeners;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.restassured.response.Response;

public class ExtentTestManager {
	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
	static ExtentReports extent = ExtentManager.getInstance();

	public static synchronized ExtentTest getTest() {
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}

	public static synchronized void endTest() {
		extent.flush();
	}

	public static synchronized ExtentTest startTest(String testName) {
		ExtentTest test = extent.createTest(testName);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		return test;
	}
    
    public static void statusLogMessage(int status){
    	
    	if(status==200)
    	{
    		ExtentTestManager.getTest().log(Status.INFO,"The status code is 200");
    	}
    	else if(status==204)
    	{
    		ExtentTestManager.getTest().log(Status.INFO,"The status code is 204");
    	}
    		
    	else if(status==400)
    	{
    		ExtentTestManager.getTest().log(Status.INFO,"The status code is 400");
    	}
    	else if(status==404)
    	{
    		ExtentTestManager.getTest().log(Status.INFO,"The status code is 404");
    		
    	}
    	else if(status==409)
    	{
    		ExtentTestManager.getTest().log(Status.INFO,"The status code is 409");
    	}
    	else
    	{
    		ExtentTestManager.getTest().log(Status.INFO,"The status code is not present");
    	}
    	
    	
    }
    public static String responseLogMessage(Response response){
    	String res=response.asString();
    	return res;
    	
    }
}
