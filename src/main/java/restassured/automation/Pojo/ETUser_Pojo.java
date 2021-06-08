package restassured.automation.Pojo;

import java.util.Map;

import com.google.gson.Gson;

public class ETUser_Pojo {

	public String putPortalRoles(Map<String, String> data) {

		ETRoles_PortalRoles_Pojo po = new ETRoles_PortalRoles_Pojo();
		po.setPortalRoleId(data.get("portalRoles"));
		po.setUserID(data.get("userId"));
		Gson jsonBody = new Gson();
		return jsonBody.toJson(po);
	}
	public String createPortalRoles(Map<String,String>data){
		ETRoles_PortalRoles_Pojo po = new ETRoles_PortalRoles_Pojo();
		po.setName(data.get("name"));
		po.setPermissions(new String[] {data.get("permissions")});
		Gson jsonBody = new Gson();
		return jsonBody.toJson(po);
		
	}
	public String UpdatePortalRoles(Map<String,String>data){
		ETRoles_PortalRoles_Pojo po = new ETRoles_PortalRoles_Pojo();
		po.setPermissions(new String[] {data.get("permissions")});
		Gson jsonBody = new Gson();
		return jsonBody.toJson(po);
		
	}
	public String UpdateAccessManager(Map<String,String>data){
		ETRoles_PortalRoles_Pojo po = new ETRoles_PortalRoles_Pojo();
		po.setUserID(data.get("userId"));
		Gson jsonBody=new Gson();
		return jsonBody.toJson(po);
	}
	public String UpdateEngagementTeamMember(Map<String,String> data){
		
		ET_Engagement_Pojo po=new ET_Engagement_Pojo();
		po.setUserId(data.get("userId"));
		po.setEngagementTeamRoleId(data.get("engagementTeamRoleId"));
		Gson jsonBody=new Gson();
		return jsonBody.toJson(po);
	}
	public String UpdateResponses(Map<String,String>data){
		ET_Responses_Pojo po=new ET_Responses_Pojo();
		po.setProcedure(data.get("procedure"));
		po.setResponse(data.get("response"));
		po.setRow(data.get("row"));
		Gson jsonBody=new Gson();
		return jsonBody.toJson(po);
	}
	public String createResponses(Map<String,String>data){
		ET_Responses_Pojo po=new ET_Responses_Pojo();
		po.setWorkProgram(data.get("workProgram"));
		Gson jsonBody=new Gson();
		return jsonBody.toJson(po);
	}
	public String createEngagement(Map<String,String>data){
		ET_Engagement_Pojo pojo=new ET_Engagement_Pojo();
		pojo.setTitle(data.get("title"));
		pojo.setClient(data.get("client"));
		pojo.setAccessManagers(new String[] {data.get("AccessManagers")});
		pojo.setMethodology(data.get("Methodology"));
		Gson jsonBody=new Gson();
		return jsonBody.toJson(pojo);
		}
	public String createReviewNotes(Map<String,String>data){
		
		ET_ReviewNotes_Pojo pojo=new ET_ReviewNotes_Pojo();
		pojo.setWorkProgram(data.get("workProgram"));
		pojo.setNoteText(data.get("notesText"));
		Gson jsonBody=new Gson();
		return jsonBody.toJson(pojo);
	}
	public String createFinancialStatement(Map<String,String> data){
		
		ET_FinanacialStatment_Pojo po=new ET_FinanacialStatment_Pojo();
		po.setIndex(data.get("index"));
		po.setItemType(data.get("itemType"));
		po.setTaxonomy(data.get("taxonomy"));
		po.setDisplayText(data.get("displayText"));
		po.setNewIndex(data.get("newIndex"));
		Gson jsonBody=new Gson();
		return jsonBody.toJson(po);
	}
	
}
