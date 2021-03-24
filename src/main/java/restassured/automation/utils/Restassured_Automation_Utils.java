package restassured.automation.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class Restassured_Automation_Utils {
	
	String AUTHORITY1 = "https://login.microsoftonline.com/a76db78b-d945-4a3a-bb42-fd0411f58bd8/login";
	String ACCESSURL = "https://login.microsoftonline.com/a76db78b-d945-4a3a-bb42-fd0411f58bd8/oauth2/v2.0/token";
	String CLIENT_ID = "7e7498e7-af6c-47bd-bf16-4f9c5d9601c8";
	String CS = "-Yl08~7hnomK9LzUbrnxc_5~O.Y182TI.y";
	String UN = "mms.test.amer.7@gtadhoc.gt.com";
	String PSSWD = "Gr@nt2021!";
	String URL = "http://13.83.56.14/api/org/user";
	
	public static final String ALPHANUMERIC_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";
	
	public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	public Response get_URL_Without_Params(String BaseURL, String AuthorizationKey , String URI ) {
		
		BaseURL = BaseURL + URI	;
		return RestAssured.given()
		.header("Authorization",AuthorizationKey )		
		.header("Content-Type","application/json")
		.log().all()
		.get(BaseURL);
					
	}
	
public Response get(String BaseURL, String AuthorizationKey , String URI ) {
		
		BaseURL = BaseURL + URI;	
		return RestAssured.given()
		.header("Authorization",AuthorizationKey )		
		.header("Content-Type","application/json")
		.log().all()
		.get(BaseURL).then().extract().response().path("find { it.renderAsSelect == 'true' }.methodologyItemId");
					
	}

	public Response get_URL_QueryParams(String BaseURL, String AuthorizationKey, String URI, String value ,String pathParam) {

		BaseURL = BaseURL + URI;
		return RestAssured.given().header("Authorization", AuthorizationKey).header("Content-Type", "application/json")
				.queryParam(value, pathParam).log().all().get(BaseURL);

	}
	
	public Response get_URL_WithOne_QueryParams(String BaseURL, String AuthorizationKey, String URI, String pathParam) {

		BaseURL = BaseURL + URI;
		return RestAssured.given().header("Authorization", AuthorizationKey).header("Content-Type", "application/json")
				.queryParam(pathParam).log().all().get(BaseURL);

	}
	
	public Response get_URL_WithOne_PathParams(String BaseURL, String AuthorizationKey, String URI, String pathParam) {

		BaseURL = BaseURL + URI;
		return RestAssured.given().header("Authorization", AuthorizationKey).header("Content-Type", "application/json")
				.pathParam("value", pathParam).log().all().get(BaseURL);

	}
	
	public Response get_URL_WithTwo_Params(String BaseURL, String AuthorizationKey, String URI, String pathParam1,String pathParam2) {

		BaseURL = BaseURL + URI;
		return RestAssured.given().header("Authorization", AuthorizationKey).header("Content-Type", "application/json")
				.pathParam(pathParam1, pathParam2).log().all().get(BaseURL);

	}


public Response post_URL(String BaseURL ,String AuthorizationKey , String URI , Map<String, String> body ) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(body)
	.log().all()
	.post(BaseURL);		
}

public Response post_WithOutBody(String BaseURL ,String AuthorizationKey , String URI) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.log().all()
	.post(BaseURL);		
}

public Response put_URL(String BaseURL ,String AuthorizationKey , String URI , Map<String, String> body ) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(body)
	.log().all()
	.put(BaseURL);		
}

public Response patch_URL(String BaseURL ,String AuthorizationKey , String URI , Map<String, String> body ) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(body)
	.log().all()
	.patch(BaseURL);		
}

public Response post_URL(String BaseURL ,String AuthorizationKey , String URI , String body ) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(body)
	.log().all()
	.post(BaseURL);
	
				
}


public Response patch_URL(String BaseURL ,String AuthorizationKey , String URI , String body, String engagementType ) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json").pathParam("engagementType", engagementType)
	.body(body)
	.log().all()
	.patch(BaseURL);
				
}

public Response patch_URL(String BaseURL ,String AuthorizationKey , String URI ,String body) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(body)
	.log().all()
	.patch(BaseURL);
				
}


public Response put_URL(String BaseURL ,String AuthorizationKey , String URI ,String body) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(body)
	.log().all()
	.put(BaseURL);
				
}


public Response delete(String BaseURL ,String AuthorizationKey , String URI , String body ) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.log().all()
	.delete(BaseURL);
				
}

public Response delete(String BaseURL ,String AuthorizationKey , String URI) {
	
	BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.log().all()
	.delete(BaseURL);
				
}


public Response put_URLPOJO(String BaseURL, String AuthorizationKey, String URI, Object obj ) {
	// TODO Auto-generated method stub
BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(obj)
	.log().all()
	.put(BaseURL);	
}

public Response delete_URLPOJO(String BaseURL, String AuthorizationKey, String URI, Object obj ) {
	// TODO Auto-generated method stub
BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(obj)
	.log().all()
	.delete(BaseURL);	
}

public Response post_URLPOJO(String BaseURL, String AuthorizationKey, String URI, Object obj) {
	// TODO Auto-generated method stub
BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(obj)
	.log().all()
	.post(BaseURL);	
}

public Response patch_URLPOJO(String BaseURL, String AuthorizationKey, String URI, Object obj) {
	// TODO Auto-generated method stub
BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)		
	.header("Content-Type","application/json")
	.body(obj)
	.log().all()
	.patch(BaseURL);	
}

public Response post_XML(String BaseURL, String AuthorizationKey, String URI,File path) {
	// TODO Auto-generated method stub
BaseURL = BaseURL + URI;
	
	return RestAssured.given()
	.header("Authorization", AuthorizationKey)	
	.header("Content-Type","text/xml")
	//.contentType(ContentType.XML)
	.body(path)
	.log().all()
	.post(BaseURL);	
}


// Create a random alphanumeric character in Java
// Random alphanumeric generator function in Java
// Only lowercase letters
	private static String getRandomAlphaNum() {
		Random r = new Random();
		int offset = r.nextInt(ALPHANUMERIC_CHARACTERS.length());
		return ALPHANUMERIC_CHARACTERS.substring(offset, offset + 1);
	}

	// Create a random alphabet in Java
	// Only lowercase letters
	private static String getRandomAlphabet() {
		Random r = new Random();
		return String.valueOf((char) (r.nextInt(26) + 'a'));
	}

	// Create a random ASCII printable character in Java
	// Returns both lowercase and uppercase letters
	private static String getRandomCharacter() {
		Random r = new Random();
		return String.valueOf((char) (r.nextInt(95) + 32));
	}
	
}