package restassured.automation.Pojo;

import java.util.Map;

import com.google.gson.Gson;

import restassured.automation.Pojo.MethodologyItem_Pojo.InitData;

public class User_Pojo {
	
	public String Create_Engagement (Map<String,String> data) {
		
		Engagement_Type_Pojo CreateEngagement = new Engagement_Type_Pojo();
		
		CreateEngagement.setTitle(data.get("title"));
		CreateEngagement.setOrganization(data.get("organization"));
		
		Gson Josnbody = new Gson();
		return Josnbody.toJson(CreateEngagement);
	}
	
	public String Get_Organization(Map<String, String> data) {

		Organization_Pojo CreateEngagement = new Organization_Pojo();
		CreateEngagement.setOrgId(data.get("orgId"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(CreateEngagement);
	}
	
	/*public String Update_Organization(Map<String,String> data) {
		
		Organization_Pojo upDateId = new Organization_Pojo();
		upDateId.setCountryCode(Integer.parseInt(data.get("countryCode")));
		upDateId.setName("name");
		
		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
		
	}
	*/
	public String Delete(Map<String,String> data) {
		
		Organization_Pojo upDateId = new Organization_Pojo();
		upDateId.setId("id");
		
		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
		
	}
	
	 public String workProgramAdd(Map<String,String> data) {
			
		 MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();
		 
		 InitData initData = new InitData();
		 initData.setWorkProgramType(data.get("workProgramType"));
		 initData.setTailoring(data.get("tailoring"));
		 
		 upDateId.setRevisions(data.get("parentId"));
		 upDateId.setTitle(data.get("title"));
		 upDateId.setItemType(data.get("itemType"));
		 upDateId.setIndex(data.get("index"));
		 upDateId.setInitData(initData);
		 
		 Gson Josnbody = new Gson();
		 return Josnbody.toJson(upDateId);		
	}
	
	 
	
	 public String phaseAdd(Map<String,String> data) {
			
		 MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();
		
		 upDateId.setTitle(data.get("title"));
		 upDateId.setItemType(data.get("itemType"));
		 upDateId.setIndex(data.get("index"));
		 upDateId.setRevisions(data.get("revisions"));		
		 
		 Gson Josnbody = new Gson();
		 return Josnbody.toJson(upDateId);		
	}
	
	 public String methodologyAdd(Map<String,String> data) {
			
    	 Methodology_Pojo upDateId = new Methodology_Pojo();
    	 
		 upDateId.setTitle(data.get("title"));
		 upDateId.setEngagementType(data.get("engagementType"));
		 upDateId.setDraftDescription(data.get("draftDescription"));
		 
		 Gson Josnbody = new Gson();
		 return Josnbody.toJson(upDateId);		
	}
	
     public String Methodology(Map<String,String> data) {
		
    	 Methodology_Pojo upDateId = new Methodology_Pojo();
		 upDateId.setId("id");
		 upDateId.setRevisionId("revisionId");
		 upDateId.setTreeItemId("treeItemId");
		 upDateId.setTitle("title");
		 upDateId.setEngagementType("engagementType");
		 
		 
		 Gson Josnbody = new Gson();
		 return Josnbody.toJson(upDateId);
	}
	

}
