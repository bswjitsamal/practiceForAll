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
	private String[] itemIds;
	private String hotFixMajorVersion;
	private String inheritFrom;
	private String storeEngagementType;
	private String importing;
	private String inheritedFrom;
	
	
	
	public String getInheritedFrom() {
		return inheritedFrom;
	}
	public void setInheritedFrom(String inheritedFrom) {
		this.inheritedFrom = inheritedFrom;
	}
	public String getStoreEngagementType() {
		return storeEngagementType;
	}
	public void setStoreEngagementType(String storeEngagementType) {
		this.storeEngagementType = storeEngagementType;
	}
	public String getInheritFrom() {
		return inheritFrom;
	}
	public void setInheritFrom(String inheritFrom) {
		this.inheritFrom = inheritFrom;
	}
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
	public String[] getItemIds() {
		return itemIds;
	}
	public void setItemIds(String[] itemIds) {
		this.itemIds = itemIds;
	}
	public String getHotFixMajorVersion() {
		return hotFixMajorVersion;
	}
	public void setHotFixMajorVersion(String hotFixMajorVersion) {
		this.hotFixMajorVersion = hotFixMajorVersion;
	}
	public String getImporting() {
		return importing;
	}
	public void setImporting(String importing) {
		this.importing = importing;
	}
	
	

}
