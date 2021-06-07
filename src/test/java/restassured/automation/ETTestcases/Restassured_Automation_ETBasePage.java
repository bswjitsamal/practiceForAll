package restassured.automation.ETTestcases;

import java.net.ConnectException;
import java.util.Base64;

import org.testng.annotations.Test;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;


public class Restassured_Automation_ETBasePage {

	public static String AUTHORITY1 = "https://login.microsoftonline.com/a76db78b-d945-4a3a-bb42-fd0411f58bd8/login";
	public static String ACCESSURL = "https://login.microsoftonline.com/a76db78b-d945-4a3a-bb42-fd0411f58bd8/oauth2/v2.0/token";
	public static String CLIENT_ID = "7e7498e7-af6c-47bd-bf16-4f9c5d9601c8";
	public static String CS = "-Yl08~7hnomK9LzUbrnxc_5~O.Y182TI.y";
	public static String UN = "mms.test.amer.7@gtadhoc.gt.com";
	public static String PSSWD = "Gr@nt2021!";
	public static String URL = "https://mmsqa.leap.gtinet.org/";

	public static String encode(String str1, String str2) {
		return new String(Base64.getEncoder().encode((str1 + ":" + str2).getBytes()));
	}

	public static Response getCode() {
		String authorization = encode(UN, PSSWD);

		return given().header("authorization", "Basic " + authorization).contentType(ContentType.URLENC)
				.formParam("Auth URL",AUTHORITY1).formParam("Access Token URL", ACCESSURL).queryParam("client_id", CLIENT_ID).queryParam("Client Secret", CS)
				.queryParam("scope", "read").formParam("response_type", "code").post("/oauth2/authorize").then().statusCode(200).extract().response();
	}

	public static String parseForOAuth2Code(Response response) {
		return response.jsonPath().getString("code");
	}

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "https://mmsqa.leap.gtinet.org/";
	}

	@Test
	public void getToken() throws ConnectException {
		Response response = getCode();
		String code = parseForOAuth2Code(response);
		System.out.println("Access code" + code);
		Assertions.assertNotNull(code);
	}

}
