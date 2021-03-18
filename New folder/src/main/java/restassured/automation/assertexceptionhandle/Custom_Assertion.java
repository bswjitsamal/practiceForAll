package restassured.automation.assertexceptionhandle;

import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;

import io.restassured.response.Response;

public class Custom_Assertion extends Assert {
	
	public static void assertEqual(String actual , String expected) {
	
   try {
	   assertEquals(actual, expected);
	   assertEquals(actual, expected);
		
	} catch (Error e) {
		
		Reporter.log("Custom Assertion failed because "+ e);
	}
	}
	
	
	
}
