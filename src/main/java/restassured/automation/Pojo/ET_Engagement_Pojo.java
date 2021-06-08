package restassured.automation.Pojo;

public class ET_Engagement_Pojo {
	private String userId;
	private String engagementTeamRoleId;
	private String Methodology;
	private String[] AccessManagers;
	private String title;
	private String client;

	
	public String getMethodology() {
		return Methodology;
	}

	public void setMethodology(String methodology) {
		Methodology = methodology;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEngagementTeamRoleId() {
		return engagementTeamRoleId;
	}

	public void setEngagementTeamRoleId(String engagementTeamRoleId) {
		this.engagementTeamRoleId = engagementTeamRoleId;
	}

	

	public String[] getAccessManagers() {
		return AccessManagers;
	}

	public void setAccessManagers(String[] accessManagers) {
		AccessManagers = accessManagers;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	
	
	

}
