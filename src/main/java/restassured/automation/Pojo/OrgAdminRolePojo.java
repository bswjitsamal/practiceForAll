package restassured.automation.Pojo;

import java.util.Map;

import com.google.gson.Gson;

public class OrgAdminRolePojo {

	private String organization;
	private String resource;
	private String permission;
	private String id;
	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String Create_Permission(Map<String, String> data) {

		OrgAdminRolePojo CreateRole = new OrgAdminRolePojo();

		CreateRole.setOrganization(data.get("organization"));
		CreateRole.setResource(data.get("resource"));
		CreateRole.setPermission(data.get("permission"));
		CreateRole.setId(data.get("resourceType"));
		CreateRole.setUser(data.get("user"));
		Gson Josnbody = new Gson();
		return Josnbody.toJson(CreateRole);
	}
}
