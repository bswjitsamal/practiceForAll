package restassured.automation.Pojo;

public class OrgRolesPojo {

	private String userId;
	private String userDisplayName;
	private String userEmail;
	private String role;
	private String permissionSets;
	private String resourceType;
	private String resource;
	private String name;
	private String minimumApplicationLevel;
	private String[] permissions;
	private String applicationLevel;


	public String getApplicationLevel() {
		return applicationLevel;
	}

	public void setApplicationLevel(String applicationLevel) {
		this.applicationLevel = applicationLevel;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	
	public String getPermissionSets() {
		return permissionSets;
	}

	public void setPermissionSets(String permissionSets) {
		this.permissionSets = permissionSets;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMinimumApplicationLevel() {
		return minimumApplicationLevel;
	}

	public void setMinimumApplicationLevel(String minimumApplicationLevel) {
		this.minimumApplicationLevel = minimumApplicationLevel;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}
	

	
	
	

}
