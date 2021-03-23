package restassured.automation.Pojo;

public class MethodologyItem_Pojo {

	private String revisions;
	private String title;
	private String index;
	private String itemType;
	private String parentId;
	private InitData initData;
	private String ruleContextType;
    private String ruleContextSource;
    private String relationshipType;
    private String workProgramId;
    private String workProgramItemType;
	private String tailoring;
	private String visibility;
	private String workProgramType;

	public String getTailoring() {
		return tailoring;
	}

	public void setTailoring(String tailoring) {
		this.tailoring = tailoring;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getWorkProgramType() {
		return workProgramType;
	}

	public void setWorkProgramType(String workProgramType) {
		this.workProgramType = workProgramType;
	}

	public InitData getInitData() {
		return initData;
	}

	public void setInitData(InitData initData) {
		this.initData = initData;
	}

	public String getRevisions() {
		return revisions;
	}

	public void setRevisions(String revisions) {
		this.revisions = revisions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	

	public String getRuleContextType() {
		return ruleContextType;
	}

	public void setRuleContextType(String ruleContextType) {
		this.ruleContextType = ruleContextType;
	}

	public String getRuleContextSource() {
		return ruleContextSource;
	}

	public void setRuleContextSource(String ruleContextSource) {
		this.ruleContextSource = ruleContextSource;
	}


	public String getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}


	public String getWorkProgramId() {
		return workProgramId;
	}

	public void setWorkProgramId(String workProgramId) {
		this.workProgramId = workProgramId;
	}


	public String getWorkProgramItemType() {
		return workProgramItemType;
	}

	public void setWorkProgramItemType(String workProgramItemType) {
		this.workProgramItemType = workProgramItemType;
	}
	


	public static class InitData {

		private String workProgramType;
		private String tailoring;

		public String getWorkProgramType() {
			return workProgramType;
		}

		public void setWorkProgramType(String workProgramType) {
			this.workProgramType = workProgramType;
		}

		public String getTailoring() {
			return tailoring;
		}

		public void setTailoring(String tailoring) {
			this.tailoring = tailoring;
		}

	}

}
