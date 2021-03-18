package restassured.automation.utils;

public class URI_Specification {
	
	public String  getEngagementype   = "/v2/store/inventory";
	public String  createOrganization  = "/api/org";
	public String  createEngagementType = "/api/org/engagementtype";
	public String  updateOrganization  = "/api/org/";
	public String  deleteOrganization  = "/api/org/";
	
	
	URI_Specification (String apiBaseUrl) {
		getEngagementype    = apiBaseUrl + apiBaseUrl;
		createOrganization  = apiBaseUrl + createOrganization ;
		createEngagementType= apiBaseUrl + createEngagementType ;
		updateOrganization  = apiBaseUrl + updateOrganization;
		deleteOrganization  = apiBaseUrl + deleteOrganization ;
		
	}

}
