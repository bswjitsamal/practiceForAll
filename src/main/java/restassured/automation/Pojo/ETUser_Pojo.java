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
		po.setPermission(new String[] {data.get("permissions")});
		Gson jsonBody = new Gson();
		return jsonBody.toJson(po);
		
	}
	public String UpdatePortalRoles(Map<String,String>data){
		ETRoles_PortalRoles_Pojo po = new ETRoles_PortalRoles_Pojo();
		po.setPermission(new String[] {data.get("permissions")});
		Gson jsonBody = new Gson();
		return jsonBody.toJson(po);
		
	}
}
