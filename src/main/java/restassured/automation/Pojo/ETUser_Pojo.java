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
		Gson jsonBody=new Gson();
		return jsonBody.toJson(po);
	}
	
}
