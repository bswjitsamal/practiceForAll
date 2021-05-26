package restassured.automation.Pojo;

public class ET_Engagement_Pojo {
	private String userId;
	private String engagementTeamRoleId;
	private String methodologyId;
	private String[] accessManager;
	private String title;
	private String client;

	public String getMethodologyId() {
		return methodologyId;
	}

	public void setMethodologyId(String methodologyId) {
		this.methodologyId = methodologyId;
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

	public String[] getAccessManager() {
		return accessManager;
	}

	public void setAccessManager(String[] accessManager) {
		this.accessManager = accessManager;
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
