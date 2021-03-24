package restassured.automation.Pojo;

public class Methodology_Pojo {
	
	private String id ;
	private String title;
	private String engagementType;
	
	private String organization;
	private String[] revisions;
	
	private String revisionId;
	private String treeItemId ;
	private String draftDescription;
	
	
	public String getDraftDescription() {
		return draftDescription;
	}
	public void setDraftDescription(String draftDescription) {
		this.draftDescription = draftDescription;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String[] getRevisions() {
		return revisions;
	}
	public void setRevisions(String[] revisions) {
		this.revisions = revisions;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEngagementType() {
		return engagementType;
	}
	public void setEngagementType(String engagementType) {
		this.engagementType = engagementType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRevisionId() {
		return revisionId;
	}
	public void setRevisionId(String revisionId) {
		this.revisionId = revisionId;
	}
	public String getTreeItemId() {
		return treeItemId;
	}
	public void setTreeItemId(String treeItemId) {
		this.treeItemId = treeItemId;
	}

}