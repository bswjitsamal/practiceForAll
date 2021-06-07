package restassured.automation.Pojo;

import java.util.Map;

import com.google.gson.Gson;

import restassured.automation.Pojo.MethodologyItem_Pojo.InitData;

public class User_Pojo {

	public String Create_Engagement(Map<String, String> data) {

		Engagement_Type_Pojo CreateEngagement = new Engagement_Type_Pojo();

		CreateEngagement.setTitle(data.get("title"));
		CreateEngagement.setOrganization(data.get("organization"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(CreateEngagement);
	}

	public String createMethodologyStore(Map<String, String> data) {
		Organization_Pojo createMethodology = new Organization_Pojo();
		createMethodology.setMethodologyId(data.get("methodologyId"));
		Gson jsonbody = new Gson();
		return jsonbody.toJson(createMethodology);
	}

	public String Get_Organization(Map<String, String> data) {

		Organization_Pojo CreateEngagement = new Organization_Pojo();
		CreateEngagement.setOrgId(data.get("orgId"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(CreateEngagement);
	}

	/*
	 * public String Update_Organization(Map<String,String> data) {
	 * 
	 * Organization_Pojo upDateId = new Organization_Pojo();
	 * upDateId.setCountryCode(Integer.parseInt(data.get("countryCode")));
	 * upDateId.setName("name");
	 * 
	 * Gson Josnbody = new Gson(); return Josnbody.toJson(upDateId);
	 * 
	 * }
	 */
	public String Delete(Map<String, String> data) {

		Organization_Pojo upDateId = new Organization_Pojo();
		upDateId.setId("id");

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);

	}

	public String workProgramAdd(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setTitle(data.get("titles"));
		upDateId.setParentId(data.get("parentId"));
		upDateId.setIndex(data.get("index"));
		upDateId.setItemType(data.get("itemType"));

		InitData initData = new InitData();
		initData.setWorkProgramType(data.get("workProgramType"));
		initData.setTailoring(data.get("tailoring"));
		upDateId.setInitData(initData);

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String phaseAdd(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setTitle(data.get("title"));
		upDateId.setItemType(data.get("itemType"));
		upDateId.setIndex(data.get("index"));
		upDateId.setParentId(data.get("parentId"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String methodologyAdd(Map<String, String> data) {

		Methodology_Pojo upDateId = new Methodology_Pojo();

		upDateId.setTitle(data.get("title"));
		upDateId.setEngagementType(data.get("engagementType"));
		upDateId.setDraftDescription(data.get("draftDescription"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String Methodology(Map<String, String> data) {

		Methodology_Pojo upDateId = new Methodology_Pojo();
		upDateId.setId("id");
		upDateId.setRevisionId("revisionId");
		upDateId.setTreeItemId("treeItemId");
		upDateId.setTitle("title");
		upDateId.setEngagementType("engagementType");
		upDateId.setImporting(data.get("importing"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String workProgramTypeAdd(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setWorkProgramType(data.get("workProgramType"));
		upDateId.setTailoring(data.get("tailoring"));
		upDateId.setVisibility(data.get("visibility"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String initializeWorkProgramType(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setRuleContextType(data.get("ruleContextType"));
		upDateId.setRuleContextSource(data.get("ruleContextSource"));
		upDateId.setItemizedWorkProgramType(data.get("itemizedWorkProgramType"));
		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String procedureAdd(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setTitle(data.get("title"));
		upDateId.setItemType(data.get("itemType"));
		upDateId.setIndex(data.get("index"));
		upDateId.setRevisions(data.get("parentId"));
		upDateId.setWPId(data.get("workProgramId"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);

	}

	public String procedureTypeAdd(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setWorkProgramItemType(data.get("workProgramItemType"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);

	}

	public String newRelationAdd(Map<String, String> data) {

		RelationPojo upDateId = new RelationPojo();

		upDateId.setMethodologyItemId(data.get("methodologyItemId"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);

	}

	public String patchRelation(Map<String, String> data) {

		RelationPojo upDateId = new RelationPojo();

		upDateId.setLinkedMethodologyItemId(data.get("linkedMethodologyItemId"));
		upDateId.setRelationshipType(data.get("relationshipType"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);

	}

	public String anotherPatchRelation(Map<String, String> data) {

		RelationPojo upDateId = new RelationPojo();

		upDateId.setRelationshipType(data.get("relationshipType"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);

	}

	public String UpdateOption() {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setOptionTitles(new String[] { "Yes", "No" });

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);

	}

	public String createOption(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setTitle(data.get("title"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);

	}

	public String PhaseCreate(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setTitle(data.get("title"));
		upDateId.setParentId(data.get("parentId"));
		upDateId.setIndex(data.get("index"));
		upDateId.setItemType(data.get("itemType"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String PhaseCreatePatch(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setWorkProgramType(data.get("workProgramType"));
		upDateId.setTailoring(data.get("tailoring"));
		upDateId.setVisibility(data.get("visibility"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String engagementTypeInherit(Map<String, String> data) {

		Engagement_Type_Pojo upDateId = new Engagement_Type_Pojo();

		upDateId.setStoreEngagementType(data.get("storeEngagementType"));
		upDateId.setOrganization(data.get("organization"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String methodologyInherit(Map<String, String> data) {

		Methodology_Pojo upDateId = new Methodology_Pojo();

		upDateId.setInheritFrom(data.get("inheritFrom"));
		upDateId.setOrganization(data.get("organization"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String publishInherit(Map<String, String> data) {

		Methodology_Pojo upDateId = new Methodology_Pojo();

		// upDateId.setInheritFrom(data.get("inheritFrom"));
		upDateId.setStoreEngagementType(data.get("storeEngagementType"));
		upDateId.setOrganization(data.get("organization"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String CreateFinancialStatment(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setFinancialStatementType(data.get("financialStatementType"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}

	public String UpdateFinancialStatment(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setWorkProgramItemType(data.get("workProgramItemType"));
		upDateId.setDecimalsAllowed(data.get("decimalsAllowed"));
		upDateId.setFormat(data.get("format"));
		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}
	public String UpdateFsliFinancialStatment(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();
		upDateId.setType(data.get("type"));
		upDateId.setLineItem(data.get("lineItem"));
		upDateId.setDisplayName(data.get("displayName"));
		upDateId.setFilterId(data.get("filterId"));
		upDateId.setIndex(data.get("index"));
		
		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}
	public String CreateFsliFilters(Map<String, String> data) {

		MethodologyItem_Pojo upDateId = new MethodologyItem_Pojo();

		upDateId.setFilterName(data.get("filterName"));

		Gson Josnbody = new Gson();
		return Josnbody.toJson(upDateId);
	}
}
